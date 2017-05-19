package com.daasuu.epf.contextfactory;

import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

import static javax.microedition.khronos.egl.EGL10.EGL_NONE;
import static javax.microedition.khronos.egl.EGL10.EGL_NO_CONTEXT;

/**
 * Created by sudamasayuki on 2017/05/16.
 */

public class EContextFactory implements GLSurfaceView.EGLContextFactory {

    private static final String TAG = "EContextFactory";

    private final int EGL_CLIENT_VERSION = 2;

    private static final int EGL_CONTEXT_CLIENT_VERSION = 0x3098;

    @Override
    public EGLContext createContext(final EGL10 egl, final EGLDisplay display, final EGLConfig config) {
        final int[] attrib_list;
        attrib_list = new int[]{EGL_CONTEXT_CLIENT_VERSION, EGL_CLIENT_VERSION, EGL_NONE};
        return egl.eglCreateContext(display, config, EGL_NO_CONTEXT, attrib_list);
    }

    @Override
    public void destroyContext(final EGL10 egl, final EGLDisplay display, final EGLContext context) {
        if (!egl.eglDestroyContext(display, context)) {
            Log.e(TAG, "display:" + display + " context: " + context);
            throw new RuntimeException("eglDestroyContex" + egl.eglGetError());
        }
    }

}
