package com.daasuu.epf.filter;

import android.content.res.Resources;
import android.opengl.GLES20;

import com.daasuu.epf.EFramebufferObject;
import com.daasuu.epf.EglUtil;

import java.util.HashMap;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUseProgram;

/**
 * Created by sudamasayuki on 2017/05/16.
 */

public class GlFilter {

    public static final String DEFAULT_UNIFORM_SAMPLER = "sTexture";


    protected static final String DEFAULT_VERTEX_SHADER =
            "attribute vec4 aPosition;\n" +
                    "attribute vec4 aTextureCoord;\n" +
                    "varying highp vec2 vTextureCoord;\n" +
                    "void main() {\n" +
                    "gl_Position = aPosition;\n" +
                    "vTextureCoord = aTextureCoord.xy;\n" +
                    "}\n";

    protected static final String DEFAULT_FRAGMENT_SHADER =
            "precision mediump float;\n" +
                    "varying highp vec2 vTextureCoord;\n" +
                    "uniform lowp sampler2D sTexture;\n" +
                    "void main() {\n" +
                    "gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "}\n";


    private static final float[] VERTICES_DATA = new float[]{
            // X, Y, Z, U, V
            -1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, 0.0f, 0.0f, 0.0f,
            1.0f, -1.0f, 0.0f, 1.0f, 0.0f
    };

    private static final int FLOAT_SIZE_BYTES = 4;
    protected static final int VERTICES_DATA_POS_SIZE = 3;
    protected static final int VERTICES_DATA_UV_SIZE = 2;
    protected static final int VERTICES_DATA_STRIDE_BYTES = (VERTICES_DATA_POS_SIZE + VERTICES_DATA_UV_SIZE) * FLOAT_SIZE_BYTES;
    protected static final int VERTICES_DATA_POS_OFFSET = 0 * FLOAT_SIZE_BYTES;
    protected static final int VERTICES_DATA_UV_OFFSET = VERTICES_DATA_POS_OFFSET + VERTICES_DATA_POS_SIZE * FLOAT_SIZE_BYTES;

    private final String vertexShaderSource;
    private final String fragmentShaderSource;

    private int program;

    private int vertexShader;
    private int fragmentShader;

    private int vertexBufferName;

    private final HashMap<String, Integer> handleMap = new HashMap<String, Integer>();

    public GlFilter() {
        this(DEFAULT_VERTEX_SHADER, DEFAULT_FRAGMENT_SHADER);
    }

    public GlFilter(final Resources res, final int vertexShaderSourceResId, final int fragmentShaderSourceResId) {
        this(res.getString(vertexShaderSourceResId), res.getString(fragmentShaderSourceResId));
    }

    public GlFilter(final String vertexShaderSource, final String fragmentShaderSource) {
        this.vertexShaderSource = vertexShaderSource;
        this.fragmentShaderSource = fragmentShaderSource;
    }

    public void setup() {
        release();
        vertexShader = EglUtil.loadShader(vertexShaderSource, GL_VERTEX_SHADER);
        fragmentShader = EglUtil.loadShader(fragmentShaderSource, GL_FRAGMENT_SHADER);
        program = EglUtil.createProgram(vertexShader, fragmentShader);
        vertexBufferName = EglUtil.createBuffer(VERTICES_DATA);
    }

    public void setFrameSize(final int width, final int height) {
    }


    public void release() {
        GLES20.glDeleteProgram(program);
        program = 0;
        GLES20.glDeleteShader(vertexShader);
        vertexShader = 0;
        GLES20.glDeleteShader(fragmentShader);
        fragmentShader = 0;
        GLES20.glDeleteBuffers(1, new int[]{vertexBufferName}, 0);
        vertexBufferName = 0;

        handleMap.clear();
    }

    public void draw(final int texName, final EFramebufferObject fbo) {
        useProgram();

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferName);
        GLES20.glEnableVertexAttribArray(getHandle("aPosition"));
        GLES20.glVertexAttribPointer(getHandle("aPosition"), VERTICES_DATA_POS_SIZE, GL_FLOAT, false, VERTICES_DATA_STRIDE_BYTES, VERTICES_DATA_POS_OFFSET);
        GLES20.glEnableVertexAttribArray(getHandle("aTextureCoord"));
        GLES20.glVertexAttribPointer(getHandle("aTextureCoord"), VERTICES_DATA_UV_SIZE, GL_FLOAT, false, VERTICES_DATA_STRIDE_BYTES, VERTICES_DATA_UV_OFFSET);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texName);
        GLES20.glUniform1i(getHandle("sTexture"), 0);

        onDraw();

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glDisableVertexAttribArray(getHandle("aPosition"));
        GLES20.glDisableVertexAttribArray(getHandle("aTextureCoord"));
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    public float getVideoAspect(float originalVideoAspect) {
        return originalVideoAspect;
    }

    protected void onDraw() {
    }

    protected final void useProgram() {
        glUseProgram(program);
    }

    protected final int getVertexBufferName() {
        return vertexBufferName;
    }

    protected final int getHandle(final String name) {
        final Integer value = handleMap.get(name);
        if (value != null) {
            return value.intValue();
        }

        int location = glGetAttribLocation(program, name);
        if (location == -1) {
            location = glGetUniformLocation(program, name);
        }
        if (location == -1) {
            throw new IllegalStateException("Could not get attrib or uniform location for " + name);
        }
        handleMap.put(name, Integer.valueOf(location));
        return location;
    }

}
