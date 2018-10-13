# cocoui
A customview built on top of Exoplayer lib

### How to add

- Create a layout for playback controls

- Add `MediaPlayerView` to xml

```
<com.rachitmishra.coco.ui.MediaPlayerView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/mediaPlayerView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:auto_show="true"
    app:controller_layout_id="@layout/layout_playback_controls"
    app:hide_on_touch="false"
    app:show_timeout="0" />
```

And in your activity

```
    val mediaUrl: String = ...
    val headers: ArrayMap<String, String> = ...
    val userAgent: String = ...
     mediaPlayerView
        .headers(headers)
        .userAgent(userAgent)
        .from(mediaUrl)
        .observe(lifecycle) {
            mediaPlayerListener(it)
        }
```
