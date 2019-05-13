package com.crossapps.petpal.Preview

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.MediaController
import android.widget.VideoView
import androidx.exifinterface.media.ExifInterface
import com.crossapps.petpal.R
import com.crossapps.petpal.Util.Logger
import java.io.IOException

class PreviewVideo : AppCompatActivity() {
    internal var bundle: Bundle? = null
    lateinit var videoView: VideoView
    lateinit var remove: ImageView
    lateinit var back: ImageView
    private var position: Int = 0
    private var mediaController: MediaController? = null

    // When you change direction of phone, this method will be called.
    // It store the state of video (Current position)
    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)

        // Store current position.
        savedInstanceState.putInt("CurrentPosition", videoView.currentPosition)
        videoView.pause()
    }


    // After rotating the phone. This method is called.
    public override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        // Get saved position.
        position = savedInstanceState.getInt("CurrentPosition")
        videoView.seekTo(position)
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.view_video)

        videoView = findViewById(R.id.previewVideo)
        remove = findViewById(R.id.remove)
        back = findViewById(R.id.back)

        back.setOnClickListener { finish() }

        if (intent != null) {

            bundle = intent.extras

        }

        if (bundle != null) {

            if (bundle!!.getString("type") == "video") {


                videoView.setVideoPath(bundle!!.getString("path"))
                mediaController = MediaController(this)
                mediaController!!.setAnchorView(videoView)
                videoView.setMediaController(mediaController)
                videoView.requestFocus()
                videoView.start()


                videoView.setOnPreparedListener { mediaPlayer ->
                    videoView.seekTo(position)
                    if (position == 0) {
                        videoView.start()
                    }

                    // When video Screen change size.
                    mediaPlayer.setOnVideoSizeChangedListener { mp, width, height ->
                        // Re-Set the videoView that acts as the anchor for the MediaController
                        mediaController!!.setAnchorView(videoView)
                    }
                }


            } else if (bundle!!.getString("type") == "galleryvideo") {
                Logger.d("Bundle", bundle!!.getString("uri"))
                val uri = Uri.parse(bundle!!.getString("uri"))
                videoView.setMediaController(MediaController(this))
                videoView.setVideoURI(uri)
                videoView.requestFocus()
                videoView.start()

            }

            remove.setOnClickListener {
                val intent = Intent()
                val bundle2 = Bundle()
                bundle2.putString("uri", bundle!!.getString("uri"))
                bundle2.putString("path", bundle!!.getString("path"))
                bundle2.putString("type", bundle!!.getString("type"))
                intent.putExtras(bundle2)
                //                intent.putExtra("index",bundle.getString("uri"));
                setResult(REQUEST_VIEW_DELETE_MEDIA, intent)
                finish()//finishing activity
            }

        }

    }


    internal fun ReadExif(file: String?): String {
        var exif = "Exif: " + file!!
        try {
            val exifInterface = ExifInterface(file)

            exif += "\nIMAGE_LENGTH: " + exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH)
            exif += "\nIMAGE_WIDTH: " + exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH)
            exif += "\n DATETIME: " + exifInterface.getAttribute(ExifInterface.TAG_DATETIME)
            exif += "\n TAG_MAKE: " + exifInterface.getAttribute(ExifInterface.TAG_MAKE)
            exif += "\n TAG_MODEL: " + exifInterface.getAttribute(ExifInterface.TAG_MODEL)
            exif += "\n TAG_ORIENTATION: " + exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION)
            exif += "\n TAG_WHITE_BALANCE: " + exifInterface.getAttribute(ExifInterface.TAG_WHITE_BALANCE)
            exif += "\n TAG_FOCAL_LENGTH: " + exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH)
            exif += "\n TAG_FLASH: " + exifInterface.getAttribute(ExifInterface.TAG_FLASH)
            exif += "\nGPS related:"
            exif += "\n TAG_GPS_DATESTAMP: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_DATESTAMP)
            exif += "\n TAG_GPS_TIMESTAMP: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP)
            exif += "\n TAG_GPS_LATITUDE: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE)
            exif += "\n TAG_GPS_LATITUDE_REF: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF)
            exif += "\n TAG_GPS_LONGITUDE: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)
            exif += "\n TAG_GPS_LONGITUDE_REF: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF)
            exif += "\n TAG_GPS_PROCESSING_METHOD: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD)

        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        return exif
    }

    companion object {
        private val REQUEST_VIEW_DELETE_MEDIA = 6
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


