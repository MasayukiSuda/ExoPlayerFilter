package com.daasuu.epf.filter;

/**
 * Also known as Luma Matte.
 * Used for videos which comprise half of content, and half of alpha-mask / luma-matte.
 * The result is a video containing only content, masked by the alpha-mask to add transparency.
 * <p>
 * To use this filter, you need to use a EPlayerTranslucentView rather than an ordinary EPlayerView.
 * <p>
 * Or, use an EPlayerView and ensure you call `EPlayerView.setZOrderOnTop(true)`
 * before the surface view's containing window is attached to the window manager
 */
public class AlphaFrameFilter extends GlFilter {

    private static final String VERTEX_SHADER =
            "attribute vec4 aPosition;\n" +
                    "attribute vec4 aTextureCoord;\n" +
                    "varying highp vec2 vTextureCoordContent;\n" +
                    "varying highp vec2 vTextureCoordMask;\n" +
                    "void main() {\n" +
                    "gl_Position = aPosition;\n" +
                    "vTextureCoordContent = %s;\n" +
                    "vTextureCoordMask = %s;\n" +
                    "}\n";

    private static final String FRAGMENT_SHADER =
            "precision mediump float;\n" +
                    "varying highp vec2 vTextureCoordContent;\n" +
                    "varying highp vec2 vTextureCoordMask;\n" +
                    "uniform lowp sampler2D sTexture;\n" +
                    "void main() {\n" +
                    "vec4 colorContent = texture2D(sTexture, vTextureCoordContent);\n" +
                    "vec4 colorMask = texture2D(sTexture, vTextureCoordMask);\n" +
                    "gl_FragColor = vec4(colorContent.rgb, colorMask.r);\n" +
                    "}\n";

    private AlphaMaskPosition alphaMaskPosition;

    /**
     * @param alphaMaskPosition the position of the alpha-mask in the video.
     */
    public AlphaFrameFilter(AlphaMaskPosition alphaMaskPosition) {
        super(getVertexShader(alphaMaskPosition), FRAGMENT_SHADER);
        this.alphaMaskPosition = alphaMaskPosition;
    }

    @Override
    public float getVideoAspect(float originalVideoAspect) {
        float factor;
        if (alphaMaskPosition == AlphaMaskPosition.TOP || alphaMaskPosition == AlphaMaskPosition.BOTTOM) {
            factor = 2f;
        } else {
            factor = 1f / 2f;
        }
        return originalVideoAspect * factor;
    }

    private static String getVertexShader(AlphaMaskPosition alphaMaskPosition) {
        String vTextureCoordContent;
        String vTextureCoordMask;

        // Note: LEFT and TOP are untested! If funky stuff is occurring, check here
        if (alphaMaskPosition == AlphaMaskPosition.LEFT) {
            vTextureCoordContent = "vec2(aTextureCoord.x*0.5+0.5, aTextureCoord.y)";
            vTextureCoordMask = "vec2(aTextureCoord.x*0.5, aTextureCoord.y)";
        } else if (alphaMaskPosition == AlphaMaskPosition.TOP) {
            vTextureCoordContent = "vec2(aTextureCoord.x, aTextureCoord.y*0.5)";
            vTextureCoordMask = "vec2(aTextureCoord.x, aTextureCoord.y*0.5+0.5)";
        } else if (alphaMaskPosition == AlphaMaskPosition.RIGHT) {
            vTextureCoordContent = "vec2(aTextureCoord.x*0.5, aTextureCoord.y)";
            vTextureCoordMask = "vec2(aTextureCoord.x*0.5+0.5, aTextureCoord.y)";
        } else if (alphaMaskPosition == AlphaMaskPosition.BOTTOM) {
            vTextureCoordContent = "vec2(aTextureCoord.x, aTextureCoord.y*0.5+0.5)";
            vTextureCoordMask = "vec2(aTextureCoord.x, aTextureCoord.y*0.5)";
        } else {
            throw new RuntimeException("No vertex shader found for alphaMaskPosition" + alphaMaskPosition);
        }

        return String.format(VERTEX_SHADER, vTextureCoordContent, vTextureCoordMask);
    }

    public enum AlphaMaskPosition {
        LEFT, TOP, RIGHT, BOTTOM
    }
}