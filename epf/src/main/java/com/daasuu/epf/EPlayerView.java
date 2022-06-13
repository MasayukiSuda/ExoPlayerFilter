package com.daasuu.epf;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.daasuu.epf.chooser.EConfigChooser;
import com.daasuu.epf.contextfactory.EContextFactory;
import com.daasuu.epf.filter.GlFilter;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.video.VideoSize;

import static com.daasuu.epf.chooser.EConfigChooser.EGL_CONTEXT_CLIENT_VERSION;

/**
 * Created by sudamasayuki on 2017/05/16.
 */
public class EPlayerView extends GLSurfaceView implements Player.Listener {

    private final static String TAG = EPlayerView.class.getSimpleName();

    private final EPlayerRenderer renderer;
    private SimpleExoPlayer player;

    /* Video Aspect according to the video */
    private float measuredVideoAspect = 1f;

    /* Video Aspect according to the video, adjusted to the needs of the filter */
    private float adjustedVideoAspect = measuredVideoAspect;

    private GlFilter glFilter = null;

    private PlayerScaleType playerScaleType = PlayerScaleType.RESIZE_FIT_WIDTH;

    public EPlayerView(Context context) {
        this(context, null);
    }

    public EPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextFactory(new EContextFactory());
        setEGLConfigChooser(new EConfigChooser(8, 8, 8, 8, 16, 0, EGL_CONTEXT_CLIENT_VERSION));
        getHolder().setFormat(PixelFormat.RGBA_8888);
        renderer = new EPlayerRenderer(this);
        setRenderer(renderer);
    }

    public EPlayerView setSimpleExoPlayer(SimpleExoPlayer player) {
        if (this.player != null) {
            this.player.release();
            this.player = null;
        }
        this.player = player;
        this.player.addListener(this);
        this.renderer.setSimpleExoPlayer(player);
        return this;
    }

    public void setGlFilter(GlFilter glFilter) {
        this.glFilter = glFilter;
        renderer.setGlFilter(glFilter);

        adjustedVideoAspect = calculateAdjustedVideoAspect();

        requestLayout();
    }

    public void setPlayerScaleType(PlayerScaleType playerScaleType) {
        this.playerScaleType = playerScaleType;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();

        int viewWidth = measuredWidth;
        int viewHeight = measuredHeight;

        switch (playerScaleType) {
            case RESIZE_FIT_WIDTH:
                viewHeight = (int) (measuredWidth / adjustedVideoAspect);
                break;
            case RESIZE_FIT_HEIGHT:
                viewWidth = (int) (measuredHeight * adjustedVideoAspect);
                break;
        }

        // Log.d(TAG, "onMeasure viewWidth = " + viewWidth + " viewHeight = " + viewHeight);

        setMeasuredDimension(viewWidth, viewHeight);

    }

    @Override
    public void onPause() {
        super.onPause();
        renderer.release();
    }

    //////////////////////////////////////////////////////////////////////////
    // Player.Listener

    @Override

    public void onVideoSizeChanged(VideoSize videoSize) {
        // Log.d(TAG, "width = " + width + " height = " + height + " unappliedRotationDegrees = " + unappliedRotationDegrees + " pixelWidthHeightRatio = " + pixelWidthHeightRatio);
        measuredVideoAspect = ((float) videoSize.width / videoSize.height) * videoSize.pixelWidthHeightRatio;
        adjustedVideoAspect = calculateAdjustedVideoAspect();
        // Log.d(TAG, "measuredVideoAspect = " + measuredVideoAspect);

        requestLayout();
    }

    @Override
    public void onRenderedFirstFrame() {
        // do nothing
    }

    private float calculateAdjustedVideoAspect() {
        if (glFilter == null) {
            return measuredVideoAspect;
        } else {
            return glFilter.getVideoAspect(measuredVideoAspect);
        }
    }
}
