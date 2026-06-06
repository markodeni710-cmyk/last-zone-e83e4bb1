package app.lovable.lastzone.gamebroadcast

import android.content.Context
import android.content.Intent
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import io.agora.rtc2.RtcEngineEx
import io.agora.rtc2.ScreenCaptureParameters
import io.agora.rtc2.video.VideoEncoderConfiguration

class AgoraScreenPusher(private val androidContext: Context) {

    fun start(
        appId: String,
        channel: String,
        token: String,
        uid: Int,
        resultData: Intent
    ) {
        try {
            if (rtcEngine != null) {
                android.util.Log.w("AgoraScreenPusher", "engine already exists, skipping")
                return
            }

            val ctx = androidContext.applicationContext
                ?: throw IllegalStateException("applicationContext is null")

            val cfg = RtcEngineConfig().apply {
                mContext = ctx
                mAppId = appId
                mEventHandler = object : IRtcEngineEventHandler() {
                    override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
                        android.util.Log.i("AgoraScreenPusher", "✅ join ok ch=$channel uid=$uid")
                    }
                    override fun onError(err: Int) {
                        android.util.Log.e("AgoraScreenPusher", "❌ agora err=$err ${RtcEngine.getErrorDescription(err)}")
                    }
                    override fun onUserJoined(uid: Int, elapsed: Int) {
                        android.util.Log.i("AgoraScreenPusher", "👤 viewer joined uid=$uid")
                    }
                    override fun onFirstLocalVideoFramePublished(source: Constants.VideoSourceType?, elapsed: Int) {
                        android.util.Log.i("AgoraScreenPusher", "🎥 first frame published src=$source")
                    }
                }
            }
            val engine = RtcEngine.create(cfg) as RtcEngineEx
            rtcEngine = engine

            engine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING)
            engine.setClientRole(Constants.CLIENT_ROLE_BROADCASTER)
            engine.enableVideo()
            engine.enableAudio()

            // أبعاد ديناميكية تعتمد على دوران الشاشة (ADAPTIVE يتولى التدوير)
            val metrics = ctx.resources.displayMetrics
            val shortSide = 720
            val longSide = if (metrics.widthPixels > 0 && metrics.heightPixels > 0) {
                val ratio = maxOf(metrics.widthPixels, metrics.heightPixels).toFloat() /
                    minOf(metrics.widthPixels, metrics.heightPixels).toFloat()
                (shortSide * ratio).toInt()
            } else 1280

            engine.setVideoEncoderConfiguration(
                VideoEncoderConfiguration(
                    VideoEncoderConfiguration.VideoDimensions(longSide, shortSide),
                    VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_24,
                    VideoEncoderConfiguration.STANDARD_BITRATE,
                    VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_ADAPTIVE
                )
            )

            // فعّل صوت النظام (Android 10+) في التقاط الشاشة
            val params = ScreenCaptureParameters().apply {
                captureVideo = true
                captureAudio = true
                videoCaptureParameters.width = longSide
                videoCaptureParameters.height = shortSide
                videoCaptureParameters.framerate = 24
                audioCaptureParameters.captureSignalVolume = 100
            }

            val options = ChannelMediaOptions().apply {
                publishCameraTrack = false
                publishMicrophoneTrack = false
                publishScreenCaptureVideo = true
                publishScreenCaptureAudio = true
                autoSubscribeAudio = false
                autoSubscribeVideo = false
                clientRoleType = Constants.CLIENT_ROLE_BROADCASTER
                channelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING
            }
            val joinRet = engine.joinChannel(token, channel, uid, options)
            android.util.Log.i("AgoraScreenPusher", "joinChannel ret=$joinRet uid=$uid")

            Thread.sleep(300)

            val ret = engine.startScreenCapture(resultData, params)
            android.util.Log.i("AgoraScreenPusher", "startScreenCapture ret=$ret")

            engine.updateChannelMediaOptions(options)
        } catch (t: Throwable) {
            android.util.Log.e("AgoraScreenPusher", "start failed", t)
            try { stop() } catch (_: Throwable) {}
        }
    }

    fun stop() {
        try {
            rtcEngine?.stopScreenCapture()
            rtcEngine?.leaveChannel()
            RtcEngine.destroy()
        } catch (_: Exception) {}
        rtcEngine = null
    }

    companion object {
        private var rtcEngine: RtcEngine? = null

        fun muteLocalVideo(mute: Boolean) {
            rtcEngine?.muteLocalVideoStream(mute)
        }
    }
}
