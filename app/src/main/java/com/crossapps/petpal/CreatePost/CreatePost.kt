package com.crossapps.petpal.CreatePost

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.*
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import android.support.annotation.RequiresApi
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.exifinterface.media.ExifInterface
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.crossapps.petpal.Preview.PreviewImage
import com.crossapps.petpal.Preview.PreviewVideo
import com.crossapps.petpal.R
import com.crossapps.petpal.Server.Model.MediaModel
import com.crossapps.petpal.Server.ProgressRequestBody
import com.crossapps.petpal.Server.Response.LoginResponseData
import com.crossapps.petpal.Server.RetrofitApiAuthSingleTon
import com.crossapps.petpal.Server.TCApi
import com.crossapps.petpal.Util.Constant
import com.crossapps.petpal.Util.Logger
import com.crossapps.petpal.Util.PrefernceFile
import com.crossapps.petpal.Util.UtilityofActivity
import com.google.gson.Gson
import com.vincent.videocompressor.VideoCompress
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.content_create_post.*
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.net.URLConnection
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CreatePost : AppCompatActivity() {


    var utilityofActivity: UtilityofActivity? = null
    var appCompatActivity: AppCompatActivity? = this
    var context: Context? = null
    var sheetBehavior: BottomSheetBehavior<*>? = null
    var mCurrentPhotoPath: String? = null

    val REQUEST_TAKE_PHOTO = 1
    val REQUEST_VIDEO_CAPTURE = 2
    val REQUEST_PICK_PHOTO = 3
    val REQUEST_PICK_MULTIPLE_VIDEOS = 4
    val REQUEST_PICK_MULTIPLE_IMAGES = 5
    val REQUEST_VIEW_DELETE_MEDIA = 6

    var imagePath: String? = null
    var videoPath: String? = null
    var outputLocation: String? = null
    lateinit var images: ArrayList<MediaModel>
    lateinit var mediaAdapter: MediaAdapter
    var dialogView: View? = null
    var handler: Handler? = null

    var loginResponse: LoginResponseData? = null
    lateinit var compressedMediaList: ArrayList<MediaModel>
    var glide: RequestManager? = null


    val listner = object : MediaAdapter.OnItemClickListener {
        override fun onItemClick(image: MediaModel?, pos: Int, view: View) {
            if (image!!.type == "image" || image.type == "galleryimage") {

                val intent = Intent(this@CreatePost, PreviewImage::class.java)

                if (image.type == "image") {
                    val bundle = Bundle()
                    bundle.putString("path", image.path)
                    bundle.putString("type", image.type)
                    intent.putExtras(bundle)
                    startActivityForResult(intent, REQUEST_VIEW_DELETE_MEDIA)
                } else if (image.type == "galleryimage") {
                    val bundle = Bundle()
                    bundle.putString("uri", image.uri.toString())
                    bundle.putString("type", image.type)
                    intent.putExtras(bundle)
                    startActivityForResult(intent, REQUEST_VIEW_DELETE_MEDIA)
                }

            }

            if (image.type == "video" || image.type == "galleryvideo") {

                val intent = Intent(this@CreatePost, PreviewVideo::class.java)

                if (image.type == "video") {
                    val bundle = Bundle()
                    bundle.putString("path", image.path)
                    bundle.putString("type", image.type)
                    intent.putExtras(bundle)
                    startActivityForResult(intent, REQUEST_VIEW_DELETE_MEDIA)
                } else if (image.type == "galleryvideo") {
                    val bundle = Bundle()
                    bundle.putString("uri", image.uri.toString())
                    bundle.putString("type", image.type)
                    intent.putExtras(bundle)
                    startActivityForResult(intent, REQUEST_VIEW_DELETE_MEDIA)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.rootView.windowToken, 0)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContentView(R.layout.activity_create_post)

        context = this

        loginResponse = Gson().fromJson(
            PrefernceFile.getInstance(context!!).getString(Constant.PREF_KEY_USER_DATA),
            LoginResponseData::class.java
        )


        glide = Glide.with(context!!)

        if (loginResponse!!.profile_picture != null)
            glide!!.load(loginResponse!!.profile_picture).into(profilePic)

        if (loginResponse!!.name != null)
            name.text = loginResponse!!.name


        sheetBehavior = BottomSheetBehavior.from(bottom_sheet)




        utilityofActivity = UtilityofActivity(appCompatActivity!!)




        setSupportActionBar(toolbar)

        val actionBar = supportActionBar

        if (actionBar != null) {
            actionBar.title = ""
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }


//        supportActionBar!!.setBackgroundDrawable(ContextCompat.getDrawable(context!!,R.drawable.gradient_rectangle))


//        val customview = layoutInflater.inflate(R.layout.toolbar_create_post, null)
//        supportActionBar!!.setDisplayShowCustomEnabled(true)
//        supportActionBar!!.customView = customview
//        supportActionBar!!.setBackgroundDrawable(ContextCompat.getDrawable(context!!,R.drawable.toolbar_white))


        takePhoto.setOnClickListener {
            if (((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) && (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )) != PackageManager.PERMISSION_GRANTED || (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                )) != PackageManager.PERMISSION_GRANTED)
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                    101
                )
            } else {
                dispatchTakePictureIntent()
            }
        }

        takeVideo.setOnClickListener {
            if (((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) && (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )) != PackageManager.PERMISSION_GRANTED || (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                )) != PackageManager.PERMISSION_GRANTED)
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                    103
                )
            } else {
                dispatchTakeVideoIntent()
            }
        }

        galleryImage.setOnClickListener {
            if (((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) && (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )) != PackageManager.PERMISSION_GRANTED)
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 104)
            } else {
                dispatchImageGalleryIntent()
            }
        }
        galleryVideo.setOnClickListener {
            if (((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) && (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )) != PackageManager.PERMISSION_GRANTED)
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 105)
            } else {
                dispatchVideoGalleryIntent()
            }
        }



        sheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
        menu.visibility = View.VISIBLE
        collpased_desc.visibility = View.GONE

        sheetBehavior!!.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        menu.visibility = View.VISIBLE
                        collpased_desc.visibility = View.GONE
                        seperator.visibility = View.VISIBLE
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        menu.visibility = View.GONE
                        collpased_desc.visibility = View.VISIBLE
                        seperator.visibility = View.GONE

                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        seperator.setOnClickListener {
            expandCloseSheet()
        }
        collpased_desc.setOnClickListener {
            expandCloseSheet()
        }




        comment.setOnClickListener {
            if (sheetBehavior!!.state == BottomSheetBehavior.STATE_EXPANDED)
                sheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        }


        images = ArrayList()
        compressedMediaList = ArrayList()

        mediaAdapter = MediaAdapter(context!!, images, listner)

        thumbnails.adapter = mediaAdapter
        thumbnails.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


        next.setOnClickListener {

            var count = 0
            var flag = 0


            if (!images.isEmpty()) {

                for (i in images) {

                    ++count

                    if (i.type == "image") {

                        val compressedImage = Compressor(this)
                            .setMaxWidth(640)
                            .setMaxHeight(480)
                            .setQuality(75)
                            .setCompressFormat(Bitmap.CompressFormat.WEBP)
                            .setDestinationDirectoryPath("storage/emulated/0/Android/data/com.crossapps.petpal/files/Compressed")
                            .compressToFile(File(i.path))

                        compressedImage.createNewFile()
                        compressedMediaList.add(MediaModel(compressedImage.path, i.type, i.uri))

                        flag++
                    }
                    if (i.type == "galleryimage") {

                        val compressedImage = Compressor(this)
                            .setMaxWidth(640)
                            .setMaxHeight(480)
                            .setQuality(75)
                            .setCompressFormat(Bitmap.CompressFormat.WEBP)
                            .setDestinationDirectoryPath("storage/emulated/0/Android/data/com.crossapps.petpal/files/Compressed")
                            .compressToFile(File(getPath(context!!, i.uri)))

                        compressedImage.createNewFile()
                        compressedMediaList.add(MediaModel(compressedImage.path, i.type, i.uri))
                        flag++


                    }
                    if (i.type == "video") {

                        val dialogs = Dialog(appCompatActivity!!)
                        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        dialogs.setCancelable(false)
                        dialogs.setContentView(R.layout.dialog_loading_bar)

                        val file = createCompressedVideoFile()

                        VideoCompress.compressVideoLow(i.path, file, object : VideoCompress.CompressListener {
                            override fun onStart() {
                                dialogs.show()
                                utilityofActivity!!.toast("Compressing $count file")
                            }

                            override fun onSuccess() {
                                dialogs.dismiss()
                                compressedMediaList.add(MediaModel(file, i.type, i.uri))
                                flag++
                            }

                            override fun onFail() {
                                dialogs.dismiss()
                            }

                            override fun onProgress(percent: Float) {
                                dialogs.findViewById<ProgressBar>(R.id.progress_bar).progress = percent.toInt()
                            }
                        })

                    }
                    if (i.type == "galleryvideo") {


                        val dialogs = Dialog(appCompatActivity!!)
                        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        dialogs.setCancelable(false)
                        dialogs.setContentView(R.layout.dialog_loading_bar)

                        val file = createCompressedVideoFile()

                        VideoCompress.compressVideoLow(
                            getPath(context!!, i.uri),
                            file,
                            object : VideoCompress.CompressListener {
                                override fun onStart() {
                                    dialogs.show()
                                    utilityofActivity!!.toast("Compressing $count file")
                                }

                                override fun onSuccess() {
                                    dialogs.dismiss()
                                    compressedMediaList.add(MediaModel(file, i.type, i.uri))
                                    flag++

                                }

                                override fun onFail() {
                                    dialogs.dismiss()
                                }

                                override fun onProgress(percent: Float) {
                                    dialogs.findViewById<ProgressBar>(R.id.progress_bar).progress = percent.toInt()
                                }
                            })

                    }


                }

            }

        }


    }


    val uploadListener: ProgressRequestBody.UploadCallbacks = object : ProgressRequestBody.UploadCallbacks {
        override fun onProgressUpdate(percentage: Int) {

            Logger.e(" Progress: ", percentage.toString())
            utilityofActivity!!.updateProgressDialog(percentage)

        }

        override fun onError() {
            utilityofActivity!!.dismissUploadDialog()
            utilityofActivity!!.toast("Upload Failed")
        }

        override fun onFinish() {
            utilityofActivity!!.dismissUploadDialog()
        }

        override fun uploadStart() {
            utilityofActivity!!.showUploadDialog()
        }
    }


    fun uploadMedia(
        user_id: RequestBody,
        description: RequestBody,
        category: RequestBody,
        location: RequestBody,
        list: HashMap<String, RequestBody>
    ) {
        utilityofActivity?.showProgressDialog()

        val tcApi = RetrofitApiAuthSingleTon.createService(TCApi::class.java, "token")
        val call = tcApi?.uploadMultipleFilesDynamic(user_id, description, category, location, list)


        call?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    utilityofActivity!!.dismissProgressDialog()

                    for (i in compressedMediaList) {
                        File(i.path).delete()
                    }
                    val intent = Intent()
                    intent.putExtra("result", "result")
                    setResult(Activity.RESULT_OK, intent)
                    finish()

                    Log.v("Upload Media Post ", "Success")

                } catch (e: java.lang.Exception) {
                    utilityofActivity!!.toast("Something went wrong, Please try again.")
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                call.cancel()
                Log.v(" Upload Media ", "fail")
            }
        })

    }


    fun next() {

        val inflater = this.layoutInflater
        dialogView = inflater.inflate(R.layout.dialog_next, null)
        val dialogBuilder = AlertDialog.Builder(context!!)
        val alertDialog = dialogBuilder.setCancelable(false).setView(dialogView).create()


        val positive = dialogView!!.findViewById<RelativeLayout>(R.id.positive)
        val add = dialogView!!.findViewById<TextView>(R.id.address)
        val negative = dialogView!!.findViewById<RelativeLayout>(R.id.negative)
        val cat = dialogView!!.findViewById<Spinner>(R.id.category)

        add.text = outputLocation


        positive.setOnClickListener {
            val user_id = RequestBody.create(
                MediaType.parse("text/plain"), loginResponse!!.userId
            )

            val description = RequestBody.create(
                MediaType.parse("text/plain"), comment.text.toString()
            )

            val category = RequestBody.create(
                MediaType.parse("text/plain"), cat.selectedItem.toString()
            )

            val address = RequestBody.create(
                MediaType.parse("text/plain"), add.text.toString()
            )

            val map = HashMap<String, RequestBody>()

            val uploadFileList = ArrayList<File>()
            var fileToUpload: File? = null

            for (i in compressedMediaList) {
                when (i.type) {
                    "image" -> {
                        fileToUpload = File(i.path)
                    }
                    "galleryimage" -> {
                        fileToUpload = File(i.path)
                    }
                    "video" -> {
                        fileToUpload = File(i.path)
                    }
                    "galleryvideo" -> {

                        fileToUpload = File(i.path)
                    }

                }
                uploadFileList.add(fileToUpload!!)
            }


            for (file in uploadFileList) {

                val fileBody = ProgressRequestBody(file, uploadListener)

                map.put("post_medias[]\"; filename=\"" + file.name + "\"", fileBody)
            }


            uploadMedia(user_id, description, category, address, map)
            alertDialog.dismiss()
        }

        negative.setOnClickListener { alertDialog.dismiss() }
        alertDialog.show()
        alertDialog.setCanceledOnTouchOutside(true)

    }


    private fun expandCloseSheet() {
        if (sheetBehavior!!.state != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
        } else {
            sheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun dispatchImageGalleryIntent() {

        if (Build.VERSION.SDK_INT < 19) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(
                Intent.createChooser(intent, "Select App"),
                REQUEST_PICK_MULTIPLE_IMAGES
            )
        } else {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(intent, REQUEST_PICK_MULTIPLE_IMAGES)
        }

    }

    private fun dispatchVideoGalleryIntent() {

        if (Build.VERSION.SDK_INT < 19) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "video/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(
                Intent.createChooser(intent, "Select App"),
                REQUEST_PICK_MULTIPLE_VIDEOS
            )
        } else {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "video/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(intent, REQUEST_PICK_MULTIPLE_VIDEOS)
        }

    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File

                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.crossapps.petpal.fileprovider", it
                    )
                    imagePath = photoFile.absolutePath
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    private fun dispatchTakeVideoIntent() {
        Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takeVideoIntent ->
            takeVideoIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createVideoFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File

                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.crossapps.petpal.fileprovider", it
                    )
                    videoPath = photoFile.absolutePath
                    takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
//            galleryAddPic()

            thumbnails.visibility = View.VISIBLE
            images.add((MediaModel(imagePath!!, "image", null)))
            mediaAdapter.notifyDataSetChanged()
        }
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {

//            galleryAddPic()


            thumbnails.visibility = View.VISIBLE
            images.add(MediaModel(videoPath!!, "video", null))
            mediaAdapter.notifyDataSetChanged()
        }

        if (requestCode == REQUEST_PICK_PHOTO && resultCode == RESULT_OK) {
            val selectedMediaUri = data!!.data

            val cr = context!!.contentResolver
            if (cr.getType(selectedMediaUri!!)!!.contains("image")) {
                thumbnails.visibility = View.VISIBLE
                images.add(MediaModel("", "galleryimage", selectedMediaUri))
                mediaAdapter.notifyDataSetChanged()

            } else if (cr.getType(selectedMediaUri)!!.contains("video")) {
                thumbnails.visibility = View.VISIBLE
                images.add(MediaModel("", "galleryvideo", selectedMediaUri))
                mediaAdapter.notifyDataSetChanged()


            }
        }

        if (requestCode == REQUEST_PICK_MULTIPLE_VIDEOS && resultCode == Activity.RESULT_OK) {

            if (data != null) {
                val clipData = data.clipData
                if (clipData != null) {
                    thumbnails.visibility = View.VISIBLE
                    for (i in 0 until clipData.itemCount) {
                        val videoItem = clipData.getItemAt(i)
                        val videoURI = videoItem.uri
                        images.add(MediaModel("", "galleryvideo", videoURI))
                    }
                } else {
                    thumbnails.visibility = View.VISIBLE
                    val videoURI = data.data

                    images.add(MediaModel("", "galleryvideo", videoURI))
                }

                mediaAdapter.notifyDataSetChanged()

            }
        }
        if (requestCode == REQUEST_PICK_MULTIPLE_IMAGES && resultCode == Activity.RESULT_OK) {

            if (data != null) {
                val clipData = data.clipData
                if (clipData != null) {
                    thumbnails.visibility = View.VISIBLE
                    for (i in 0 until clipData.itemCount) {
                        val imageItem = clipData.getItemAt(i)
                        val imageURI = imageItem.uri
                        images.add(MediaModel("", "galleryimage", imageURI))
                    }
                } else {
                    thumbnails.visibility = View.VISIBLE
                    val imageURI = data.data

                    images.add(MediaModel("", "galleryimage", imageURI))
                }

                mediaAdapter.notifyDataSetChanged()

            }
        }



        if (requestCode == REQUEST_VIEW_DELETE_MEDIA) {


            if (data != null) {
                var index: Int? = null

                if (data.getStringExtra("type") == "galleryimage") {
                    for (i in 0 until images.count()) {
                        if (images[i].uri.toString() == data.getStringExtra("uri")) {
                            index = i
                        }
                    }


                    if (index != null)
                        images.removeAt(index)
                    mediaAdapter.notifyDataSetChanged()

                    if (images.isEmpty()) {
                        thumbnails.visibility = View.GONE
                    }
                }

                if (data.getStringExtra("type") == "image") {
                    for (i in 0 until images.count()) {
                        if (images[i].path == data.getStringExtra("path")) {
                            index = i
                        }
                    }

                    if (index != null)
                        images.removeAt(index)
                    mediaAdapter.notifyDataSetChanged()

                    if (images.isEmpty()) {
                        thumbnails.visibility = View.GONE
                    }
                }
                if (data.getStringExtra("type") == "video") {
                    for (i in 0 until images.count()) {
                        if (images[i].path == data.getStringExtra("path")) {
                            index = i
                        }
                    }

                    if (index != null)
                        images.removeAt(index)
                    mediaAdapter.notifyDataSetChanged()

                    if (images.isEmpty()) {
                        thumbnails.visibility = View.GONE
                    }
                }

                if (data.getStringExtra("type") == "galleryvideo") {
                    for (i in 0 until images.count()) {
                        if (images[i].uri.toString() == data.getStringExtra("uri")) {
                            index = i
                        }
                    }


                    if (index != null)
                        images.removeAt(index)
                    mediaAdapter.notifyDataSetChanged()

                    if (images.isEmpty()) {
                        thumbnails.visibility = View.GONE
                    }
                }

            }
        }

    }


    fun isImageFile(path: String): Boolean {
        val mimeType = URLConnection.guessContentTypeFromName(path)
        return mimeType != null && mimeType.startsWith("image")
    }

    fun isVideoFile(path: String): Boolean {
        val mimeType = URLConnection.guessContentTypeFromName(path)
        return mimeType != null && mimeType.startsWith("video")
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

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath
        }
    }

    @Throws(IOException::class)
    private fun createVideoFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "MP4_${timeStamp}_", /* prefix */
            ".mp4", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath
        }
    }

    @Throws(IOException::class)
    private fun createCompressedVideoFile(): String? {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File = getExternalFilesDir("Compressed")!!
        return File.createTempFile(
            "MP4_${timeStamp}_", /* prefix */
            ".mp4", /* suffix */
            storageDir /* directory */
        ).absolutePath
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty()) {
            when (requestCode) {

                101 -> {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {


                        dispatchTakePictureIntent()

                    } else {
                        utilityofActivity!!.toast("Some functions may not work unless you allow permissions")
                    }

                }
                103 -> {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        dispatchTakeVideoIntent()
                    } else {
                        utilityofActivity!!.toast("Some functions may not work unless you allow permissions")
                    }

                }
                104 -> {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                        dispatchImageGalleryIntent()

                    } else {
                        utilityofActivity!!.toast("Some functions may not work unless you allow permissions")
                    }

                }
                105 -> {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                        dispatchVideoGalleryIntent()

                    } else {
                        utilityofActivity!!.toast("Some functions may not work unless you allow permissions")
                    }

                }

                106 -> {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        var count = 0

                        var flag = 0


                        if (!images.isEmpty()) {

                            for (i in images) {

                                ++count

                                if (i.type == "image") {

                                    val compressedImage = Compressor(this)
                                        .setMaxWidth(640)
                                        .setMaxHeight(480)
                                        .setQuality(75)
                                        .setCompressFormat(Bitmap.CompressFormat.WEBP)
                                        .setDestinationDirectoryPath("storage/emulated/0/Android/data/com.crossapps.petpal/files/Compressed")
                                        .compressToFile(File(i.path))

                                    compressedImage.createNewFile()
                                    compressedMediaList.add(MediaModel(compressedImage.path, i.type, i.uri))

                                    flag++

                                }
                                if (i.type == "galleryimage") {

                                    val compressedImage = Compressor(this)
                                        .setMaxWidth(640)
                                        .setMaxHeight(480)
                                        .setQuality(75)
                                        .setCompressFormat(Bitmap.CompressFormat.WEBP)
                                        .setDestinationDirectoryPath("storage/emulated/0/Android/data/com.crossapps.petpal/files/Compressed")
                                        .compressToFile(File(getPath(context!!, i.uri)))

                                    compressedImage.createNewFile()
                                    compressedMediaList.add(MediaModel(compressedImage.path, i.type, i.uri))
                                    flag++


                                }
                                if (i.type == "video") {

//                    val mediaAsyncModel=MediaAsyncModel(i.path,createCompressedVideoFile(),null,context)
//
//                    val compress=Compress()
//                    compress.execute(mediaAsyncModel)


                                    val dialogs = Dialog(appCompatActivity!!)
                                    dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                    dialogs.setCancelable(false)
                                    dialogs.setContentView(R.layout.dialog_loading_bar)

                                    val file = createCompressedVideoFile()

                                    VideoCompress.compressVideoLow(
                                        i.path,
                                        file,
                                        object : VideoCompress.CompressListener {
                                            override fun onStart() {
                                                dialogs.show()
                                                utilityofActivity!!.toast("Compressing $count file")
                                            }

                                            override fun onSuccess() {
                                                dialogs.dismiss()
                                                compressedMediaList.add(MediaModel(file, i.type, i.uri))

                                                flag++

                                            }

                                            override fun onFail() {
                                                dialogs.dismiss()
                                            }

                                            override fun onProgress(percent: Float) {
                                                dialogs.findViewById<ProgressBar>(R.id.progress_bar).progress =
                                                    percent.toInt()
                                            }
                                        })

                                }
                                if (i.type == "galleryvideo") {

                                    val dialogs = Dialog(appCompatActivity!!)
                                    dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                    dialogs.setCancelable(false)
                                    dialogs.setContentView(R.layout.dialog_loading_bar)

                                    val file = createCompressedVideoFile()

                                    VideoCompress.compressVideoLow(
                                        getPath(context!!, i.uri),
                                        file,
                                        object : VideoCompress.CompressListener {
                                            override fun onStart() {
                                                dialogs.show()
                                                utilityofActivity!!.toast("Compressing $count file")
                                            }

                                            override fun onSuccess() {
                                                dialogs.dismiss()
                                                compressedMediaList.add(MediaModel(file, i.type, i.uri))
                                                flag++
                                                dialogs.dismiss()
                                            }

                                            override fun onFail() {
                                                dialogs.dismiss()
                                            }

                                            override fun onProgress(percent: Float) {
                                                dialogs.findViewById<ProgressBar>(R.id.progress_bar).progress =
                                                    percent.toInt()
                                            }
                                        })

                                }


                            }

                        }

                    } else {
                        utilityofActivity!!.toast("Some functions may not work unless you allow permissions")
                    }
                }
            }
        }
    }

    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(imagePath)
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
        }
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

                if (id != null && id.startsWith("raw:")) {
                    return id.substring(4)
                }

                val contentUriPrefixesToTry = arrayOf(
                    "content://downloads/public_downloads",
                    "content://downloads/my_downloads",
                    "content://downloads/all_downloads"
                )

                for (contentUriPrefix in contentUriPrefixesToTry) {
                    val contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), id.toLong())
                    try {
                        val path = getDataColumn(context, contentUri, null, null)
                        if (path != null) {
                            return path
                        }
                    } catch (e: java.lang.Exception) {
                    }
                }

                // path could not be retrieved using ContentResolver, therefore copy file to accessible cache using streams
                var fileName = getFileName(context, uri)
                var cacheDir = getDocumentCacheDir(context)
                var file = generateFileName(fileName, cacheDir)
                var destinationPath: String? = null
                if (file != null) {
                    destinationPath = file.absolutePath
                    saveFileFromUri(context, uri, destinationPath)
                }

                return destinationPath;
            }    // TODO handle non-primary volumes
//            } else if (isDownloadsDocument(uri)) {
//
//                val id = DocumentsContract.getDocumentId(uri)
//                val contentUri = ContentUris.withAppendedId(
//                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
//                )
//
//                return getDataColumn(context, contentUri, null, null)
//            }
            else if (isMediaDocument(uri)) {
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

    fun getFileName(@NonNull context: Context, uri: Uri): String? {
        val mimeType = context.contentResolver.getType(uri)
        var filename: String? = null

        if (mimeType == null && context != null) {
            val path = getPath(context, uri)
            if (path == null) {
                filename = uri.toString()
                if (filename == null) {
                    return null
                }
                val index = filename.lastIndexOf('/')
                filename = filename.substring(index + 1)
            } else {
                val file = File(path)
                filename = file.name
            }
        } else {
            val returnCursor = context.contentResolver.query(uri, null, null, null, null)
            if (returnCursor != null) {
                val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                returnCursor.moveToFirst()
                filename = returnCursor.getString(nameIndex)
                returnCursor.close()
            }
        }
        return filename
    }


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

    fun getDocumentCacheDir(@NonNull context: Context): File {
        val dir = File(context.cacheDir, "documents")
        if (!dir.exists()) {
            dir.mkdirs()
        }


        return dir
    }

    @Nullable
    fun generateFileName(@Nullable name: String?, directory: File): File? {
        var name: String? = name ?: return null

        var file = File(directory, name)

        if (file.exists()) {
            var fileName: String = name!!
            var extension = ""
            val dotIndex = name.lastIndexOf('.')
            if (dotIndex > 0) {
                fileName = name.substring(0, dotIndex)
                extension = name.substring(dotIndex)
            }

            var index = 0

            while (file.exists()) {
                index++
                name = "$fileName($index)$extension"
                file = File(directory, name)
            }
        }

        try {
            if (!file.createNewFile()) {
                return null
            }
        } catch (e: IOException) {

            return null
        }


        return file
    }


    private fun saveFileFromUri(context: Context, uri: Uri, destinationPath: String) {
        var `is`: InputStream? = null
        var bos: BufferedOutputStream? = null
        try {
            `is` = context.contentResolver.openInputStream(uri)
            bos = BufferedOutputStream(FileOutputStream(destinationPath, false))
            val buf = ByteArray(1024)
            `is`!!.read(buf)
            do {
                bos.write(buf)
            } while (`is`.read(buf) != -1)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if (`is` != null) `is`.close()
                if (bos != null) bos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    fun readBytesFromFile(filePath: String): ByteArray {

        var fileInputStream: FileInputStream? = null
        var bytesArray: ByteArray? = null

        try {

            val file = File(filePath)
            bytesArray = ByteArray(file.length() as Int)

            //read file into bytes[]
            fileInputStream = FileInputStream(file)
            fileInputStream.read(bytesArray)

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

        }

        return bytesArray!!

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


}
