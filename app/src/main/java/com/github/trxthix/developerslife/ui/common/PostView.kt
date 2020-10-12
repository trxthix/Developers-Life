package com.github.trxthix.developerslife.ui.common


import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.updateLayoutParams
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.trxthix.developerslife.R
import com.github.trxthix.developerslife.data.Post
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.post_layout.view.*
import kotlin.math.max

class PostView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attributeSet, defStyleAttr, defStyleRes) {
    private val authorTextView: TextView get() = _author
    private val dateTextView: TextView get() = _dateTime

    private val descriptionTextView: TextView get() = _postDescription
    private val imageView: ImageView get() = _postImage

    private val btnVotesCount: MaterialButton get() = _votesCount
    private val bthCommentsCount: MaterialButton get() = _commentsCount

    var imageLoadListener: ((Boolean) -> Unit)? = null

    private val glideRequestListener = object : RequestListener<Drawable?> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable?>?,
            isFirstResource: Boolean
        ): Boolean {
            imageLoadListener?.invoke(false)
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable?>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            imageLoadListener?.invoke(true)
            return false
        }
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.post_layout, this, true)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val specHeight = MeasureSpec.getSize(heightMeasureSpec)
        var totalHeight = 0

        val authorParams = authorTextView.layoutParams as MarginLayoutParams
        val dateTimeParams = dateTextView.layoutParams as MarginLayoutParams
        val descriptionParams = descriptionTextView.layoutParams as MarginLayoutParams
        val imageParams = imageView.layoutParams as MarginLayoutParams
        val votesParams = btnVotesCount.layoutParams as MarginLayoutParams
        val commentsParams = bthCommentsCount.layoutParams as MarginLayoutParams

        //Author
        measureChildWithMargins(authorTextView, widthMeasureSpec, 0, heightMeasureSpec, 0)
        val authorHeight = authorTextView.measuredHeight + authorParams.topMargin + authorParams.bottomMargin
        totalHeight += authorHeight

        //DateTime
        measureChildWithMargins(dateTextView, widthMeasureSpec, 0, heightMeasureSpec, 0)
        val dateTimeHeight = dateTextView.measuredHeight + dateTimeParams.topMargin + dateTimeParams.bottomMargin
        totalHeight += dateTimeHeight

        //Buttons
        val buttonWidth = MeasureSpec.getSize(widthMeasureSpec) / 2
        val buttonMeasureSpec = MeasureSpec.makeMeasureSpec(buttonWidth, MeasureSpec.EXACTLY)

        measureChildWithMargins(btnVotesCount, buttonMeasureSpec, 0, heightMeasureSpec, 0)
        measureChildWithMargins(bthCommentsCount, buttonMeasureSpec, 0, heightMeasureSpec, 0)

        val btnVotesHeight = btnVotesCount.measuredHeight + votesParams.topMargin + votesParams.bottomMargin
        val btnCommentsHeight = bthCommentsCount.measuredHeight + commentsParams.topMargin + commentsParams.bottomMargin

        totalHeight += max(btnVotesHeight, btnCommentsHeight)

        totalHeight += paddingTop + paddingBottom

        //Description & ImageContent.

        //1) If height is set as MATCH_PARENT or a constant value (e.x. 400dp), we will place the Image & Text in these dimensions
        //2) If the height is not limited, then we will not do anything
        val contentHeightMeasureSpec = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.AT_MOST, MeasureSpec.EXACTLY -> MeasureSpec.makeMeasureSpec(
                specHeight - totalHeight,
                MeasureSpec.EXACTLY
            )
            else -> heightMeasureSpec
        }

        measureChildWithMargins(descriptionTextView, widthMeasureSpec, 0, contentHeightMeasureSpec, 0)
        measureChildWithMargins(imageView, widthMeasureSpec, 0, contentHeightMeasureSpec, 0)

        val descriptionHeight =
            descriptionTextView.measuredHeight + descriptionParams.topMargin + descriptionParams.bottomMargin
        val imageHeight =
            imageView.measuredHeight + imageParams.topMargin + imageParams.bottomMargin

        totalHeight += max(descriptionHeight, imageHeight)

        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), max(totalHeight, specHeight))
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var top: Int
        var left: Int
        var right: Int
        var bottom: Int

        val authorParams = authorTextView.layoutParams as MarginLayoutParams
        val dateTimeParams = dateTextView.layoutParams as MarginLayoutParams
        val descriptionParams = descriptionTextView.layoutParams as MarginLayoutParams
        val imageParams = imageView.layoutParams as MarginLayoutParams
        val votesParams = btnVotesCount.layoutParams as MarginLayoutParams
        val commentsParams = bthCommentsCount.layoutParams as MarginLayoutParams


        //Author
        top = paddingTop + authorParams.topMargin
        left = paddingLeft + authorParams.leftMargin
        right = r - authorParams.rightMargin
        bottom = top + authorTextView.measuredHeight
        authorTextView.layout(left, top, right, bottom)

        //DateTime
        top = authorTextView.bottom + authorParams.bottomMargin + dateTimeParams.topMargin
        left = paddingLeft + dateTimeParams.leftMargin
        right = r - dateTimeParams.rightMargin
        bottom = top + dateTextView.measuredHeight
        dateTextView.layout(left, top, right, bottom)

        //TextContent & ImageContent
        top = dateTextView.bottom + dateTimeParams.bottomMargin + imageParams.topMargin
        left = paddingLeft + descriptionParams.leftMargin
        right = r - descriptionParams.rightMargin
        bottom = top + max(descriptionTextView.measuredHeight, imageView.measuredHeight)

        descriptionTextView.layout(left, bottom - descriptionTextView.measuredHeight, right, bottom)
        imageView.layout(left, top, right, bottom)

        //Buttons
        val buttonTop = imageView.bottom + imageParams.bottomMargin
        val buttonWidth = r / 2

        //ButtonVotes
        top = buttonTop + votesParams.topMargin
        left = paddingLeft + votesParams.leftMargin
        right = left + buttonWidth
        bottom = top + btnVotesCount.measuredHeight
        btnVotesCount.layout(left, top, right, bottom)

        //ButtonComments
        top = buttonTop + commentsParams.topMargin
        left = btnVotesCount.right + commentsParams.rightMargin
        right = left + buttonWidth
        bottom = top + bthCommentsCount.measuredHeight
        bthCommentsCount.layout(left, top, right, bottom)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
        return MarginLayoutParams(p)
    }

    override fun checkLayoutParams(p: LayoutParams?): Boolean {
        return p is MarginLayoutParams
    }

    fun bind(post: Post, payload: List<Any>? = null) {
        authorTextView.text = post.author
        descriptionTextView.text = post.description
        dateTextView.text = post.date
        btnVotesCount.text = post.votes.toString()
        bthCommentsCount.text = post.commentsCount.toString()

        Glide.with(imageView)
            .load(post.getImage())
            .transition(DrawableTransitionOptions().crossFade())
            .transform(FitCenter())
            .placeholder(R.drawable.ic_place_holder)
            .addListener(glideRequestListener)
            .into(imageView)
    }

    fun clearImage() {
        Glide.with(imageView)
            .clear(imageView)
    }
}
