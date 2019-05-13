package com.crossapps.petpal.Util

import android.annotation.TargetApi
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Typeface
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.crossapps.petpal.R
import kotlinx.android.synthetic.main.dialog_loading.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Server on 2/1/2018.
 */
class UtilityofActivity(activity: AppCompatActivity) {
    var appCompatActivity = activity
    var mConetxt: Context? =activity.applicationContext
    var progressDialog: ProgressDialog? = null
    var dialogs:Dialog?=null

    fun setTypeFaceToEditText(editText: EditText){
        val myTypeface = Typeface.createFromAsset(mConetxt!!.assets, "fonts/Nexa-Bold.otf")
        editText.typeface = myTypeface
    }
    fun setTypeFaceToTextView(textView: TextView){
        val myTypeface = Typeface.createFromAsset(mConetxt!!.assets, "fonts/Nexa-Bold.otf")
        textView.typeface = myTypeface
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun getPath(context: Context, uri: Uri): String? {
        try {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR2) {
                return getRealPathFromURI(context, uri)

            }
            val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    return if ("primary".equals(type, ignoreCase = true)) {
                        Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                    } else {
                        "/storage/" + type + "/" + split[1]

                    }
                } else if (isDownloadsDocument(uri)) {
                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(id)
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
            } else if ("content".equals(uri.scheme, ignoreCase = true)) {
                // Return the remote address
                return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(context, uri, null, null)
            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                return uri.path
            }// File
            // MediaStore (and general)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        return null
    }

    fun decodeScaledBitmapFromSdCard(filePath: String): Bitmap {

        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filePath, options)

        // Calculate inSampleSize
        val reqWidth = 800
        val reqHeight = 650
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight)
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        val bitmap = BitmapFactory.decodeFile(filePath, options)
        return checkOrientationOfImage(bitmap, filePath)
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        val totalPixels = (width * height).toFloat()
        val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }
        return inSampleSize
    }

    fun checkOrientationOfImage(bitmap: Bitmap, imgurl: String): Bitmap {
        var bitmap = bitmap


        var ei: ExifInterface? = null
        try {
            ei = ExifInterface(imgurl)

        } catch (e: IOException) {
            e.printStackTrace()
        }

        assert(ei != null)
        val orientation = ei!!.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> bitmap = rotateImage(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> bitmap = rotateImage(bitmap, 180f)
        }// etc.
        return bitmap
    }


    fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val retVal: Bitmap

        val matrix = Matrix()
        matrix.postRotate(angle)
        retVal = Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)

        return retVal
    }

    fun getRealPathFromURI(context: Context, contentUri: Uri): String {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
    }

    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

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

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            if (cursor != null)
                cursor.close()
        }
        return null
    }


    fun toast(message: String) {
        Toast.makeText(appCompatActivity, message,
                Toast.LENGTH_LONG).show()

    }
    fun showUploadDialog() {
        appCompatActivity.runOnUiThread(Runnable {
            if (dialogs == null) {
                dialogs = Dialog(appCompatActivity)
                dialogs!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialogs!!.setCancelable(false)
                dialogs!!.setContentView(R.layout.dialog_upload)
            }

            dialogs?.show()
        })
    }

    fun updateProgressDialog(percentage:Int) {
        try {
            if (dialogs != null) {
                if(percentage<100)
                    dialogs?.findViewById<ProgressBar>(R.id.progress_bar)!!.progress =percentage
                else
                    dialogs?.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun dismissUploadDialog() {
        try {
            if (dialogs != null) {
                dialogs?.dismiss()
                dialogs = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showProgressDialog() {
        appCompatActivity.runOnUiThread(Runnable {
            if (dialogs == null) {
                dialogs = Dialog(appCompatActivity)
                dialogs!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialogs!!.setCancelable(false)
                dialogs!!.setContentView(R.layout.dialog_loading)
                Glide.with(appCompatActivity).load(R.drawable.car).into(dialogs!!.splash)
            }

            dialogs?.show()
        })
    }

    fun dismissProgressDialog() {
        try {
            if (dialogs != null) {
                dialogs?.dismiss()
                dialogs = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showResponse(body: String) {
        Log.e("Full response => ", body)
    }


    fun getTimeElapsed(date: String): String {
        try {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            format.timeZone = TimeZone.getTimeZone("GMT")
            val past = format.parse(date)
            val now = Date()
            val seconds = TimeUnit.MILLISECONDS.toSeconds(now.time - past.time)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
            val hours = TimeUnit.MILLISECONDS.toHours(now.time - past.time)
            val days = TimeUnit.MILLISECONDS.toDays(now.time - past.time)
            return when {
                seconds < 60 -> seconds.toString() + "s"
                minutes < 60 -> minutes.toString() + "m"
                hours < 24 -> hours.toString() + "h"
                else -> days.toString() + "d"
            }
        } catch (j: Exception) {
            j.printStackTrace()
        }

        return ""
    }

    /**
     * Returns format for a given double number
     * @param n - double number
     * @return result in format like (1100 => 1.1k)
     */
    fun formatNumberStyle(n: Int): String {
        val result: Double
        return when {
            n < 1000 -> "" + n
            n in 1000..999999 -> {
                result = round(n.toDouble() / 1000, 1)
                result.toString() + "k"
            }
            n >= 1000000 -> {
                result = round(n.toDouble() / 1000000, 1)
                result.toString() + "m"
            }
            else -> "" + n
        }
    }

    private fun round(value: Double, precision: Int): Double {
        val scale = Math.pow(10.0, precision.toDouble()).toInt()
        return Math.round(value * scale).toDouble() / scale
    }


    fun configureToolbar(appCompatActivity: AppCompatActivity) {
        val toolbar = appCompatActivity.findViewById<View>(R.id.toolbar) as Toolbar
        appCompatActivity.setSupportActionBar(toolbar)
        val actionbar = appCompatActivity.supportActionBar
        actionbar!!.setDisplayShowHomeEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    fun configureDrawerToolbar(appCompatActivity: AppCompatActivity) {
        val toolbar = appCompatActivity.findViewById<View>(R.id.toolbar) as Toolbar
        appCompatActivity.setSupportActionBar(toolbar)
        val actionbar = appCompatActivity.supportActionBar
        actionbar!!.setHomeAsUpIndicator(R.drawable.ic_menu_black)
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

}