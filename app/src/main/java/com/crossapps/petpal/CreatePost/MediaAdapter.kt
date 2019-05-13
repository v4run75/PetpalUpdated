package com.crossapps.petpal.CreatePost

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.crossapps.petpal.R
import com.crossapps.petpal.Server.Model.MediaModel


class MediaAdapter(
    private val context: Context,
    private val items: List<MediaModel>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<MediaAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(image: MediaModel?, pos: Int, view: View)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_thumbnail, parent, false)
        return ViewHolder(v)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, items[position], position, listener)
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView
        private val videoPlay: ImageView

        init {
            imageView = view.findViewById(R.id.imageView)
            videoPlay = view.findViewById(R.id.video_play)
        }

        @RequiresApi(Build.VERSION_CODES.KITKAT)
        fun bind(context: Context, item: MediaModel?, pos: Int, listener: OnItemClickListener) {

            if (item != null && item.type == "video") {
                val bMap = ThumbnailUtils.createVideoThumbnail(item.path, MediaStore.Video.Thumbnails.MICRO_KIND)
                imageView.setImageBitmap(bMap)
                videoPlay.visibility = View.VISIBLE
            }
            if (item != null && item.type == "image") {
                imageView.setImageBitmap(decodeFile(item.path!!))
                videoPlay.visibility = View.GONE
            }
            if (item != null && item.type == "galleryimage") {
                if (item.uri != null) {
                    val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, item.uri)
                    imageView.setImageBitmap(bitmap)
                    videoPlay.visibility = View.GONE

                }
            }
            if (item != null && item.type == "galleryvideo") {
                if (item.uri != null) {
                    val bMap = ThumbnailUtils.createVideoThumbnail(
                        getPath(context, item.uri),
                        MediaStore.Video.Thumbnails.MICRO_KIND
                    )
                    imageView.setImageBitmap(bMap)
                    videoPlay.visibility = View.VISIBLE
                }
            }


            imageView.setOnClickListener { listener.onItemClick(item, pos, imageView) }


        }

        @RequiresApi(Build.VERSION_CODES.KITKAT)
        fun getPath(context: Context, uri: Uri): String? {

            val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    if ("primary".equals(type, ignoreCase = true)) {
                        return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                    }

                    // TODO handle non-primary volumes
                } else if (isDownloadsDocument(uri)) {

                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                    )

                    return getDataColumn(context, contentUri, null, null)
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    var contentUri: Uri? = null
                    if ("image" == type) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }

                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])

                    return getDataColumn(context, contentUri, selection, selectionArgs)
                }// MediaProvider
                // DownloadsProvider
            } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {
                return getDataColumn(context, uri, null, null)
            } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
                return uri.path
            }// File
            // MediaStore (and general)

            return null
        }

        /**
         * Get the value of the data column for this Uri. This is useful for
         * MediaStore Uris, and other file-based ContentProviders.
         *
         * @param context The context.
         * @param uri The Uri to query.
         * @param selection (Optional) Filter used in the query.
         * @param selectionArgs (Optional) Selection arguments used in the query.
         * @return The value of the _data column, which is typically a file path.
         */
        fun getDataColumn(
            context: Context, uri: Uri?, selection: String?,
            selectionArgs: Array<String>?
        ): String? {

            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(column)

            try {
                cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
                if (cursor != null && cursor.moveToFirst()) {
                    val column_index = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(column_index)
                }
            } finally {
                cursor?.close()
            }
            return null
        }


        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is ExternalStorageProvider.
         */
        fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         */
        fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.authority
        }

        fun decodeFile(path: String): Bitmap? {
            try {
                // Decode image size
                val o = BitmapFactory.Options()
                o.inJustDecodeBounds = true
                BitmapFactory.decodeFile(path, o)
                // The new size we want to scale to
                val REQUIRED_SIZE = 70

                // Find the correct scale value. It should be the power of 2.
                var scale = 1
                while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2

                // Decode with inSampleSize
                val o2 = BitmapFactory.Options()
                o2.inSampleSize = scale
                return BitmapFactory.decodeFile(path, o2)
            } catch (e: Throwable) {
                e.printStackTrace()
            }

            return null

        }
    }


}