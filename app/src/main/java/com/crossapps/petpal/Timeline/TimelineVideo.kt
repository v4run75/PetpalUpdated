package com.crossapps.petpal.Timeline

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.MediaController
import android.widget.VideoView
import com.crossapps.petpal.R

class TimelineVideo : AppCompatActivity() {
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



                position =bundle!!.getInt("pos")
                videoView.setVideoURI(Uri.parse(bundle!!.getString("Url")))
                mediaController = MediaController(this)
                mediaController!!.setAnchorView(videoView)
                videoView.setMediaController(mediaController)
                videoView.requestFocus()
                videoView.start()

//                Logger.d(" EXIF: ", ReadExif(bundle!!.getString("path")))

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




            remove.visibility=View.GONE


        }

    }


}


