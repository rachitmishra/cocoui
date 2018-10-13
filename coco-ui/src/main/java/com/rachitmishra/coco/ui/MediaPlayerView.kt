package com.rachitmishra.coco.ui

import android.arch.lifecycle.*
import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.support.v4.util.ArrayMap
import android.util.AttributeSet
import com.google.android.exoplayer2.ui.PlayerView


class MediaPlayerView(ctx: Context, attrs: AttributeSet) : PlayerView(ctx, attrs), LifecycleObserver {

    private lateinit var playerManager: PlayerManager
    private var userAgent = BuildConfig.APPLICATION_ID
    private var headers = ArrayMap<String, String>()
    private var url = ""
    private val stateObserver = MutableLiveData<MediaPlayerState>()
    private lateinit var observer: Observer<MediaPlayerState>
    private var currentWindow = 0
    private var currentPlaybackPosition = 0L

    fun headers(headers: ArrayMap<String, String>): MediaPlayerView {
        this.headers = headers
        return this
    }

    fun userAgent(userAgent: String): MediaPlayerView {
        this.userAgent = userAgent
        return this
    }

    fun from(url: String): MediaPlayerView {
        this.url = url
        return this
    }

    fun observe(lifeCycle: Lifecycle, observer: Observer<MediaPlayerState>) {
        this.observer = observer
        stateObserver.observeForever(observer)
        playerManager = PlayerManager(url, userAgent, headers, stateObserver)
        lifeCycle.addObserver(this)
    }

    fun play() {
        warnLowVolume()
        if (playerManager.hasPlaybackEnded()) {
            playerManager.replay()
        } else {
            playerManager.play()
        }
    }

    fun pause() {
        playerManager.pause()
    }

    fun replay() {
        playerManager.replay()
    }

    private fun warnLowVolume() {
        if (context != null) {
            val audio = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC)
            if (currentVolume < 2) {
                stateObserver.value = MediaPlayerState.LOW_VOLUME
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onStart() {
        if (hasMarshmallow()) {
            playerManager.initialise(this, currentWindow, currentPlaybackPosition)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume() {
        if (!hasMarshmallow() || !playerManager.isInitialised()) {
            playerManager.initialise(this, currentWindow, currentPlaybackPosition)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun onPause() {
        if (!hasMarshmallow()) {
            currentWindow = playerManager.currentWindowIndex()
            currentPlaybackPosition = playerManager.currentPosition()
            playerManager.release()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onStop() {
        if (hasMarshmallow()) {
            currentWindow = playerManager.currentWindowIndex()
            currentPlaybackPosition = playerManager.currentPosition()
            playerManager.release()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDestroy() {
        stateObserver.removeObserver(observer)
    }

    private fun hasMarshmallow() = Build.VERSION.SDK_INT > Build.VERSION_CODES.M
}
