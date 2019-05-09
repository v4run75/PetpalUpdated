package com.crossapps.petpal.RegisterPet

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Display
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidmads.library.qrgenearator.QRGSaver
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.crossapps.petpal.R
import com.crossapps.petpal.RoomModel.Entities.PetsModel
import com.crossapps.petpal.RoomModel.ViewModel.PetsViewModel
import com.crossapps.petpal.Util.UtilityofActivity
import com.crossapps.petpal.Util.custom.TextViewOpenSans
import com.google.zxing.WriterException
import kotlinx.android.synthetic.main.content_register_pet.*
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class RegisterPet : AppCompatActivity() {
    var utilityofActivity: UtilityofActivity? = null
    var appCompatActivity: AppCompatActivity = this
    var context: Context? = null
    var glide: RequestManager? = null
    var imagePath: String? = null
    val REQUEST_TAKE_PHOTO = 7
    val REQUEST_PICK_PHOTO = 8
    var croppedImageFile: File? = null
    var viewModel: PetsViewModel? = null

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.register_pet_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.done -> {


                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_pet)

        context = this

        glide = Glide.with(context!!)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar

        if (actionBar != null) {
            actionBar.title = ""
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        utilityofActivity = UtilityofActivity(appCompatActivity)



        changeProfilePicture.setOnClickListener {
            if (((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) && (ContextCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )) != PackageManager.PERMISSION_GRANTED && (ContextCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )) != PackageManager.PERMISSION_GRANTED)
            ) {
                ActivityCompat.requestPermissions(
                    appCompatActivity, arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ), 101
                )
            } else {

                val inflater = layoutInflater
                val dialogView = inflater.inflate(R.layout.dialog_image_chooser, null)
                val dialogBuilder = android.support.v7.app.AlertDialog.Builder(context!!, R.style.PinDialog)
                val alertDialog = dialogBuilder.setCancelable(false).setView(dialogView).create()
                alertDialog.show()
                alertDialog.setCanceledOnTouchOutside(true)
                val gallery = dialogView.findViewById<TextViewOpenSans>(R.id.gallery)
                val camera = dialogView.findViewById<TextViewOpenSans>(R.id.camera)

                gallery.setOnClickListener {
                    alertDialog.dismiss()
                    dispatchImageGalleryIntent()
                }

                camera.setOnClickListener {
                    alertDialog.dismiss()
                    dispatchTakePictureIntent()
                }

                val negative = dialogView.findViewById<Button>(R.id.negative)
                negative.setOnClickListener { alertDialog.dismiss() }

            }
        }


        viewModel = ViewModelProviders.of(this).get(PetsViewModel::class.java)


        submit.setOnClickListener {
            val jsonObject = JSONObject()
            jsonObject.put("name", name.text.toString()).put("owner", owner.text.toString())
                .put("contact", contact.text.toString()).put("address", address.text.toString())
                .put("image","https://images.dog.ceo/breeds/shiba/shiba-12.jpg")

            val id=viewModel!!.addItem(PetsModel(name.text.toString(),owner.text.toString(),contact.text.toString(),address.text.toString(),"https://images.dog.ceo/breeds/shiba/shiba-12.jpg"))
            generateQr(jsonObject.toString())

        }


    }


    private fun generateQr(jsonData: String) {
        if (jsonData.isNotEmpty()) {
            val manager = context!!.getSystemService(WINDOW_SERVICE) as WindowManager;
            val display = manager.defaultDisplay
            val point = Point()
            display.getSize(point)
            val width = point.x
            val height = point.y
            var smallerDimension = if (width < height) {
                width
            } else {
                height
            }
            smallerDimension = smallerDimension * 3 / 4

            val qrgEncoder = QRGEncoder(
                jsonData, null,
                QRGContents.Type.TEXT,
                smallerDimension
            )

            try {
                val bitmap = qrgEncoder.encodeAsBitmap();

                val inflater = layoutInflater
                val dialogView = inflater.inflate(R.layout.dialog_display_qr, null)
                val dialogBuilder = android.support.v7.app.AlertDialog.Builder(context!!, R.style.PinDialog)
                val alertDialog = dialogBuilder.setCancelable(false).setView(dialogView).create()
                alertDialog.show()
                alertDialog.setCanceledOnTouchOutside(true)
                val qrImage = dialogView.findViewById<ImageView>(R.id.qrImage)

                val save = dialogView.findViewById<TextViewOpenSans>(R.id.save)
                val close = dialogView.findViewById<TextViewOpenSans>(R.id.close)

                qrImage.setImageBitmap(bitmap);


                save.setOnClickListener {
                    saveQr(jsonData,bitmap)
                }

                close.setOnClickListener {
                    alertDialog.dismiss()
                }
            } catch (e: WriterException) {
                Log.v("QR", e.toString());
            }
        } else {
            utilityofActivity!!.toast("Required");
        }

    }

    private fun saveQr(jsonData: String, bitmap: Bitmap) {
        val save: Boolean?
        val result: String?
        try {
            save = QRGSaver.save("test", jsonData.trim(), bitmap, QRGContents.ImageType.IMAGE_JPEG);
            result = if (save) {
                "Image Saved"
            } else {
                "Image Not Saved"
            }
            utilityofActivity!!.toast(result)
        } catch (e: Exception) {
            e.printStackTrace();
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(context!!.packageManager)?.also {
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
                        context!!,
                        "com.webpulse.trafficcontrol.fileprovider", it
                    )
                    imagePath = photoFile.absolutePath
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    private fun dispatchImageGalleryIntent() {

        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"

        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"

        val chooserIntent = Intent.createChooser(getIntent, "Select Image")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

        startActivityForResult(chooserIntent, REQUEST_PICK_PHOTO)

    }


    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(imagePath)
            mediaScanIntent.data = Uri.fromFile(f)
            context!!.sendBroadcast(mediaScanIntent)
        }
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File = context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_PICK_PHOTO && resultCode == Activity.RESULT_OK) {


            val selectedMedia = data!!.data


            imagePath = (utilityofActivity!!.getPath(context!!, selectedMedia!!))


            val image = File(imagePath)
            val bmOptions = BitmapFactory.Options()
            val bitmap = BitmapFactory.decodeFile(image.absolutePath, bmOptions)


            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.dialog_crop_image, null)
            val dialogBuilder = android.support.v7.app.AlertDialog.Builder(context!!, R.style.PinDialog)
            val alertDialog = dialogBuilder.setCancelable(false).setView(dialogView).create()
            alertDialog.show()
            alertDialog.setCanceledOnTouchOutside(false)

            val negative = dialogView.findViewById<Button>(R.id.negative)
            val kropView = dialogView.findViewById<com.avito.android.krop.KropView>(R.id.krop_view)
            kropView.setBitmap(bitmap)

            negative.setOnClickListener {
                val croppedImage = kropView.getCroppedBitmap()
                alertDialog.dismiss()

                croppedImageFile = File(bitmapToFile(croppedImage!!))

                glide!!.load(croppedImageFile).into(profilePic)

            }


        }
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            galleryAddPic()

            val image = File(imagePath)
            val bmOptions = BitmapFactory.Options()
            val bitmap = BitmapFactory.decodeFile(image.absolutePath, bmOptions)


            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.dialog_crop_image, null)
            val dialogBuilder = android.support.v7.app.AlertDialog.Builder(context!!, R.style.PinDialog)
            val alertDialog = dialogBuilder.setCancelable(false).setView(dialogView).create()
            alertDialog.show()
            alertDialog.setCanceledOnTouchOutside(false)

            val negative = dialogView.findViewById<Button>(R.id.negative)
            val kropView = dialogView.findViewById<com.avito.android.krop.KropView>(R.id.krop_view)
            kropView.setBitmap(bitmap)

            negative.setOnClickListener {
                val croppedImage = kropView.getCroppedBitmap()
                alertDialog.dismiss()


                croppedImageFile = File(bitmapToFile(croppedImage!!))
                glide!!.load(croppedImageFile).into(profilePic)

            }


        }
    }


    // Method to save an bitmap to a file
    private fun bitmapToFile(bitmap: Bitmap): String {
        // Get the context wrapper
        val wrapper = ContextWrapper(applicationContext)

        // Initialize a new file instance to save bitmap object
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            // Compress the bitmap and save in jpg format
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // Return the saved bitmap uri
        return file.absolutePath
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
}
