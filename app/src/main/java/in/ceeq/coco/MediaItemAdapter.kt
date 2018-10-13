package `in`.ceeq.coco

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Observer
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.rachitmishra.coco.ui.MediaPlayerState

class MediaItemAdapter(private val lifecycle: Lifecycle) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = listOf(
            MediaItem("one", "https://instaudio.s3.amazonaws.com/live/MCr.mp3?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAIWSFYJ7JZIDLCYZQ%2F20180510%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20180510T110604Z&X-Amz-Expires=1800&X-Amz-SignedHeaders=host&X-Amz-Signature=7a36b098abe589b3f7f08b836c0d2c1701669958b333ac9cf932c990243bf478"),
            MediaItem("two", "https://instaudio.s3.amazonaws.com/live/MCr.mp3?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAIWSFYJ7JZIDLCYZQ%2F20180510%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20180510T110604Z&X-Amz-Expires=1800&X-Amz-SignedHeaders=host&X-Amz-Signature=7a36b098abe589b3f7f08b836c0d2c1701669958b333ac9cf932c990243bf478"),
            MediaItem("three", "https://instaudio.s3.amazonaws.com/live/MCr.mp3?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAIWSFYJ7JZIDLCYZQ%2F20180510%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20180510T110604Z&X-Amz-Expires=1800&X-Amz-SignedHeaders=host&X-Amz-Signature=7a36b098abe589b3f7f08b836c0d2c1701669958b333ac9cf932c990243bf478"),
            MediaItem("four", "https://instaudio.s3.amazonaws.com/live/MCr.mp3?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAIWSFYJ7JZIDLCYZQ%2F20180510%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20180510T110604Z&X-Amz-Expires=1800&X-Amz-SignedHeaders=host&X-Amz-Signature=7a36b098abe589b3f7f08b836c0d2c1701669958b333ac9cf932c990243bf478"),
            MediaItem("five", "https://instaudio.s3.amazonaws.com/live/MCr.mp3?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAIWSFYJ7JZIDLCYZQ%2F20180510%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20180510T110604Z&X-Amz-Expires=1800&X-Amz-SignedHeaders=host&X-Amz-Signature=7a36b098abe589b3f7f08b836c0d2c1701669958b333ac9cf932c990243bf478"),
            MediaItem("six", "https://instaudio.s3.amazonaws.com/live/MCr.mp3?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAIWSFYJ7JZIDLCYZQ%2F20180510%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20180510T110604Z&X-Amz-Expires=1800&X-Amz-SignedHeaders=host&X-Amz-Signature=7a36b098abe589b3f7f08b836c0d2c1701669958b333ac9cf932c990243bf478"),
            MediaItem("seven", "https://instaudio.s3.amazonaws.com/live/MCr.mp3?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAIWSFYJ7JZIDLCYZQ%2F20180510%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20180510T110604Z&X-Amz-Expires=1800&X-Amz-SignedHeaders=host&X-Amz-Signature=7a36b098abe589b3f7f08b836c0d2c1701669958b333ac9cf932c990243bf478"),
            MediaItem("eight", "https://instaudio.s3.amazonaws.com/live/MCr.mp3?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAIWSFYJ7JZIDLCYZQ%2F20180510%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20180510T110604Z&X-Amz-Expires=1800&X-Amz-SignedHeaders=host&X-Amz-Signature=7a36b098abe589b3f7f08b836c0d2c1701669958b333ac9cf932c990243bf478"),
            MediaItem("nine", "https://instaudio.s3.amazonaws.com/live/MCr.mp3?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAIWSFYJ7JZIDLCYZQ%2F20180510%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20180510T110604Z&X-Amz-Expires=1800&X-Amz-SignedHeaders=host&X-Amz-Signature=7a36b098abe589b3f7f08b836c0d2c1701669958b333ac9cf932c990243bf478"),
            MediaItem("ten", "https://instaudio.s3.amazonaws.com/live/MCr.mp3?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAIWSFYJ7JZIDLCYZQ%2F20180510%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20180510T110604Z&X-Amz-Expires=1800&X-Amz-SignedHeaders=host&X-Amz-Signature=7a36b098abe589b3f7f08b836c0d2c1701669958b333ac9cf932c990243bf478"),
            MediaItem("eleven", "https://instaudio.s3.amazonaws.com/live/MCr.mp3?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAIWSFYJ7JZIDLCYZQ%2F20180510%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20180510T110604Z&X-Amz-Expires=1800&X-Amz-SignedHeaders=host&X-Amz-Signature=7a36b098abe589b3f7f08b836c0d2c1701669958b333ac9cf932c990243bf478"),
            MediaItem("twelve", "https://instaudio.s3.amazonaws.com/live/MCr.mp3?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAIWSFYJ7JZIDLCYZQ%2F20180510%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20180510T110604Z&X-Amz-Expires=1800&X-Amz-SignedHeaders=host&X-Amz-Signature=7a36b098abe589b3f7f08b836c0d2c1701669958b333ac9cf932c990243bf478"))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaItemViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val mediaItemView = inflater.inflate(R.layout.list_item_media, parent, false)
        return MediaItemViewHolder(mediaItemView)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        bindItem(viewHolder as MediaItemViewHolder, items[position])
    }

    private fun bindItem(viewHolder: MediaItemViewHolder, mediaItem: MediaItem) {
        viewHolder.titleView.text = mediaItem.title
        viewHolder.mediaPlayerView
                .from(mediaItem.url)
                .observe(lifecycle, Observer { mediaPlayerListener(viewHolder, it) })
    }

    private fun mediaPlayerListener(viewHolder: MediaItemViewHolder, state: MediaPlayerState?) {
        when (state) {
            MediaPlayerState.LOW_VOLUME -> {
                // Toast.makeText(this, "Increase volume!", Toast.LENGTH_SHORT).show()
            }
            MediaPlayerState.ERROR -> {
                //  Toast.makeText(this, "Playback failed!", Toast.LENGTH_SHORT).show()
            }
            MediaPlayerState.BUFFERING -> {

            }
            MediaPlayerState.READY -> {
                viewHolder.playButton.setImageDrawable(
                        VectorDrawableCompat.create(viewHolder.itemView.resources,
                                R.drawable.ic_play_circle_filled_black_24dp, null))
            }
            MediaPlayerState.COMPLETED ->
                viewHolder.playButton.setImageDrawable(
                        VectorDrawableCompat.create(viewHolder.itemView.resources,
                                R.drawable.ic_replay_black_24dp, null))
            MediaPlayerState.UNKNOWN_MEDIA -> {
                // Error
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class MediaItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mediaPlayerView: MediaPlayerView = itemView.findViewById(R.id.mediaPlayerView) as MediaPlayerView
        var titleView: TextView = itemView.findViewById(R.id.textView) as TextView
        var playButton: ImageButton = itemView.findViewById(R.id.exo_play) as ImageButton
    }

}
