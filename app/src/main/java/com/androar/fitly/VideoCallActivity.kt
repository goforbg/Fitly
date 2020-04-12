package com.androar.fitly

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.SurfaceView
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas
import io.agora.rtc.video.VideoEncoderConfiguration


class VideoCallActivity : AppCompatActivity() {

    private var mRtcEngine: RtcEngine? = null
    private val PERMISSION_REQ_ID = 22
    private val REQUESTED_PERMISSIONS = arrayOf<String>(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)
    private val LOG_TAG = VideoCallActivity::class.java.simpleName

    private val mRtcEventHandler: IRtcEngineEventHandler = object : IRtcEngineEventHandler() {
        override fun onFirstRemoteVideoDecoded(
            uid: Int,
            width: Int,
            height: Int,
            elapsed: Int
        ) {
            runOnUiThread { // set first remote user to the main bg video container
                setupRemoteVideoStream(uid)
            }
        }

        // remote user has left channel
        override fun onUserOffline(uid: Int, reason: Int) {
            runOnUiThread { onRemoteUserLeft() }
        }

        // remote user has toggled their video
        override fun onUserMuteVideo(
            uid: Int,
            toggle: Boolean
        ) { // Tutorial Step 10
            runOnUiThread { onRemoteUserVideoToggle(uid, toggle) }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.androar.fitly.R.layout.activity_videocall)

        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
            checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID)) {
            initAgoraEngine();
        }

        findViewById<ImageView>(com.androar.fitly.R.id.audioBtn).setVisibility(View.GONE); // set the audio button hidden
        findViewById<ImageView>(com.androar.fitly.R.id.leaveBtn).setVisibility(View.GONE); // set the leave button hidden
        findViewById<ImageView>(com.androar.fitly.R.id.videoBtn).setVisibility(View.GONE); // set the video button hidden
    }

    private fun initAgoraEngine() {
        mRtcEngine = try {
            RtcEngine.create(
                baseContext,
                getString(com.androar.fitly.R.string.agora_app_id),
                mRtcEventHandler
            )
        } catch (e: Exception) {
            Log.e(LOG_TAG, Log.getStackTraceString(e))
            throw RuntimeException(
                "NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(
                    e
                )
            )
        }
        setupSession()
    }

    private fun setupSession() {
        mRtcEngine!!.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION)
        mRtcEngine!!.enableVideo()
        mRtcEngine!!.setVideoEncoderConfiguration(
            VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_1280x720,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_30,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
            )
        )
    }

    private fun setupLocalVideoFeed() {

        // setup the container for the local user
        val videoContainer: FrameLayout = findViewById(com.androar.fitly.R.id.floating_video_container)
        val videoSurface = RtcEngine.CreateRendererView(baseContext)
        videoSurface.setZOrderMediaOverlay(true)
        videoContainer.addView(videoSurface)
        mRtcEngine!!.setupLocalVideo(VideoCanvas(videoSurface, VideoCanvas.RENDER_MODE_FIT, 0))
    }

    private fun setupRemoteVideoStream(uid: Int) {
        // setup ui element for the remote stream
        val videoContainer: FrameLayout = findViewById(com.androar.fitly.R.id.bg_video_container)
        // ignore any new streams that join the session
        if (videoContainer.childCount >= 1) {
            return
        }
        val videoSurface = RtcEngine.CreateRendererView(baseContext)
        videoContainer.addView(videoSurface)
        mRtcEngine!!.setupRemoteVideo(VideoCanvas(videoSurface, VideoCanvas.RENDER_MODE_FIT, uid))
        mRtcEngine!!.setRemoteSubscribeFallbackOption(Constants.STREAM_FALLBACK_OPTION_AUDIO_ONLY)
    }

    fun onAudioMuteClicked(view: View) {
        val btn: ImageView = view as ImageView
        if (btn.isSelected()) {
            btn.setSelected(false)
            btn.setImageResource(com.androar.fitly.R.drawable.audio_toggle_btn)
        } else {
            btn.setSelected(true)
            btn.setImageResource(com.androar.fitly.R.drawable.audio_toggle_active_btn)
        }
        mRtcEngine!!.muteLocalAudioStream(btn.isSelected())
    }

    fun onVideoMuteClicked(view: View) {
        val btn: ImageView = view as ImageView
        if (btn.isSelected()) {
            btn.setSelected(false)
            btn.setImageResource(com.androar.fitly.R.drawable.video_toggle_btn)
        } else {
            btn.setSelected(true)
            btn.setImageResource(com.androar.fitly.R.drawable.video_toggle_active_btn)
        }
        mRtcEngine!!.muteLocalVideoStream(btn.isSelected())
        val container: FrameLayout = findViewById(com.androar.fitly.R.id.floating_video_container)
        container.setVisibility(if (btn.isSelected()) View.GONE else View.VISIBLE)
        val videoSurface = container.getChildAt(0) as SurfaceView
        videoSurface.setZOrderMediaOverlay(!btn.isSelected())
        videoSurface.setVisibility(if (btn.isSelected()) View.GONE else View.VISIBLE)
    }

    // join the channel when user clicks UI button
    fun onjoinChannelClicked(view: View?) {
        mRtcEngine!!.joinChannel(
            getString(com.androar.fitly.R.string.agora_access_token),
            "test",
            "Extra Optional Data",
            0
        ) // if you do not specify the uid, Agora will assign one.
        setupLocalVideoFeed()
        findViewById<ImageView>(com.androar.fitly.R.id.joinBtn).setVisibility(View.GONE) // set the join button hidden
        findViewById<ImageView>(com.androar.fitly.R.id.audioBtn).setVisibility(View.VISIBLE) // set the audio button hidden
        findViewById<ImageView>(com.androar.fitly.R.id.leaveBtn).setVisibility(View.VISIBLE) // set the leave button hidden
        findViewById<ImageView>(com.androar.fitly.R.id.videoBtn).setVisibility(View.VISIBLE) // set the video button hidden
    }

    fun onLeaveChannelClicked(view: View?) {
        leaveChannel()
        removeVideo(com.androar.fitly.R.id.floating_video_container)
        removeVideo(com.androar.fitly.R.id.bg_video_container)
        findViewById<ImageView>(com.androar.fitly.R.id.joinBtn).setVisibility(View.VISIBLE) // set the join button visible
        findViewById<ImageView>(com.androar.fitly.R.id.audioBtn).setVisibility(View.GONE) // set the audio button hidden
        findViewById<ImageView>(com.androar.fitly.R.id.leaveBtn).setVisibility(View.GONE) // set the leave button hidden
        findViewById<ImageView>(com.androar.fitly.R.id.videoBtn).setVisibility(View.GONE) // set the video button hidden
    }

    private fun leaveChannel() {
        mRtcEngine!!.leaveChannel()
    }

    private fun removeVideo(containerID: Int) {
        val videoContainer = findViewById<FrameLayout>(containerID)
        videoContainer.removeAllViews()
    }

    private fun onRemoteUserVideoToggle(uid: Int, toggle: Boolean) {
        val videoContainer: FrameLayout = findViewById(com.androar.fitly.R.id.bg_video_container)
        val videoSurface = videoContainer.getChildAt(0) as SurfaceView
        videoSurface.setVisibility(if (toggle) View.GONE else View.VISIBLE)

        // add an icon to let the other user know remote video has been disabled
        if (toggle) {
            val noCamera = ImageView(this)
            noCamera.setImageResource(com.androar.fitly.R.drawable.video_disabled)
            videoContainer.addView(noCamera)
        } else {
            val noCamera: ImageView = videoContainer.getChildAt(1) as ImageView
            if (noCamera != null) {
                videoContainer.removeView(noCamera)
            }
        }
    }

    private fun onRemoteUserLeft() {
        removeVideo(com.androar.fitly.R.id.bg_video_container)
    }


    fun checkSelfPermission(permission: String?, requestCode: Int): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                permission!!
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                REQUESTED_PERMISSIONS,
                requestCode
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>, grantResults: IntArray
    ) {
        Log.i(LOG_TAG, "onRequestPermissionsResult " + grantResults[0] + " " + requestCode)
        when (requestCode) {
            PERMISSION_REQ_ID -> {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    Log.i(
                        LOG_TAG,
                        "Need permissions " + Manifest.permission.RECORD_AUDIO.toString() + "/" + Manifest.permission.CAMERA
                    )
                }
                else {
                    Log.i(LOG_TAG, "Permission has been granted by user")
                }
                // if permission granted, initialize the engine
                initAgoraEngine()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        leaveChannel()
        RtcEngine.destroy()
        mRtcEngine = null
    }

    fun showLongToast(msg: String?) {
        runOnUiThread { Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show() }
    }

}
