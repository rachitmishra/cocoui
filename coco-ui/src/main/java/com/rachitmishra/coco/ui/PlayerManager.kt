package com.rachitmishra.coco.ui

import android.arch.lifecycle.*
import android.content.Context
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.support.v4.util.ArrayMap
import android.text.TextUtils
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util

class PlayerManager(private val userAgent: String,
                    private val headers: ArrayMap<String, String>? = null,
                    lifecycle: Lifecycle) : LifecycleObserver {

    private var dataSourceFactory: DataSource.Factory
    private val bandwidthMeter = DefaultBandwidthMeter()
    private var mediaPlayer: SimpleExoPlayer? = null
    private var mediaSource: MediaSource? = null
    private var currentWindow = 0
    private var currentPlaybackPosition = 0L
    private val stateObserver = MutableLiveData<MediaPlayerState>()
    lateinit var playbackControlView: PlayerView
    private lateinit var eventObserver: Observer<MediaPlayerState>

    init {
        dataSourceFactory = buildHttpDataSourceFactory(true)
        lifecycle.addObserver(this)
    }

    fun isInitialised() = this.mediaPlayer != null

    fun initialise(currentWindow: Int = 0, playbackPosition: Long = 0L, url: String, playbackControlView: PlayerView,
                   eventObserver: Observer<MediaPlayerState>) {

        val uri = Uri.parse(url)
        mediaSource = buildMediaSource(uri)
        this.playbackControlView = playbackControlView
        reinitialize(mediaSource, currentWindow, playbackPosition)
        this.eventObserver = eventObserver
        stateObserver.observeForever(eventObserver)
    }

    private fun reinitialize(mediaSource: MediaSource?, currentWindow: Int, playbackPosition: Long) {
        if (::playbackControlView.isInitialized) {
            if (mediaPlayer == null) {
                mediaPlayer = createMediaPlayer(playbackControlView.context)
            }

            if (mediaSource != null) {
                playbackControlView.player = mediaPlayer
                initMediaPlayer(mediaSource, currentWindow, playbackPosition)
            } else {
                stateObserver.value = MediaPlayerState.UNKNOWN_MEDIA
            }
        }
    }

    private fun createMediaPlayer(context: Context): SimpleExoPlayer {
        val trackSelectionFactory = AdaptiveTrackSelection.Factory()
        val trackSelector = DefaultTrackSelector(trackSelectionFactory)
        return ExoPlayerFactory.newSimpleInstance(context, trackSelector)
    }

    private fun initMediaPlayer(mediaSource: MediaSource,
                                currentWindow: Int = 0, playbackPosition: Long = 0L): SimpleExoPlayer? {
        return mediaPlayer?.apply {
            val haveResumePosition = currentWindow != C.INDEX_UNSET
            if (haveResumePosition) {
                seekTo(currentWindow, playbackPosition)
            }
            prepare(mediaSource, !haveResumePosition, false)
            addListener(playerEventListener)
            playWhenReady = false
        }
    }

    private fun buildMediaSource(uri: Uri, overrideExtension: String = ""): MediaSource? {
        val type = if (TextUtils.isEmpty(overrideExtension)) {
            Util.inferContentType(uri)
        } else {
            Util.inferContentType(".$overrideExtension")
        }

        if (type == C.TYPE_OTHER) {
            return ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
        }

        return null
    }

    fun currentPosition() = mediaPlayer?.currentPosition ?: 0

    fun currentWindowIndex() = mediaPlayer?.currentWindowIndex ?: 0

    fun release() {
        mediaPlayer?.apply {
            release()
            removeListener(playerEventListener)
        }
        mediaPlayer = null
    }

    private fun buildHttpDataSourceFactory(useBandwidthMeter: Boolean): DefaultHttpDataSourceFactory {
        val sourceFactory = DefaultHttpDataSourceFactory(userAgent, if (useBandwidthMeter) {
            bandwidthMeter
        } else {
            null
        })
        headers?.forEach {
            sourceFactory.defaultRequestProperties.set(it.key, it.value)
        }
        return sourceFactory
    }

    fun play() {
        warnLowVolume()
        if (hasPlaybackEnded()) {
            replay()
        }
        mediaPlayer?.playWhenReady = true
    }

    private fun warnLowVolume() {
        if (playbackControlView.context != null) {
            val audio = playbackControlView.context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC)
            if (currentVolume < 2) {
                stateObserver.value = MediaPlayerState.LOW_VOLUME
            }
        }
    }

    fun pause() {
        mediaPlayer?.playWhenReady = false
    }

    fun replay() {
        mediaPlayer?.seekTo(0)
    }

    private fun hasPlaybackEnded() = mediaPlayer?.playbackState == Player.STATE_ENDED

    private val playerEventListener = object : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                Player.STATE_BUFFERING -> stateObserver.value = MediaPlayerState.BUFFERING
                Player.STATE_READY -> stateObserver.value = MediaPlayerState.READY
                Player.STATE_ENDED -> stateObserver.value = MediaPlayerState.COMPLETED
            }
        }

        override fun onPlayerError(error: ExoPlaybackException?) {
            stateObserver.value = MediaPlayerState.ERROR
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        if (hasMarshmallow()) {
            reinitialize(mediaSource, currentWindow, currentPlaybackPosition)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        if (!hasMarshmallow() || !isInitialised()) {
            reinitialize(mediaSource, currentWindow, currentPlaybackPosition)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        if (!hasMarshmallow()) {
            releaseMediaPlayer()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        if (hasMarshmallow()) {
            releaseMediaPlayer()
        }
    }

    private fun hasMarshmallow() = Build.VERSION.SDK_INT > Build.VERSION_CODES.M

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        removeObserver()
    }

    fun removeObserver() {
        if (::eventObserver.isInitialized) {
            stateObserver.removeObserver(eventObserver)
        }
    }

    fun releaseMediaPlayer() {
        currentWindow = currentWindowIndex()
        currentPlaybackPosition = currentPosition()
        release()
    }
}
