package com.example.stepan.androidlearn;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;


/**
 * Created by stepan on 3/27/17.
 */

public class LocalGallery implements Target {
    private static int UNIQUE_ID = 0;
    private final String TAG = "LocalGallery" + String.valueOf(++UNIQUE_ID);
    Request request;
    String fileName;

    LocalGallery(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void onLoadStarted(Drawable placeholder) {
    }

    @Override
    public void onLoadFailed(Exception e, Drawable errorDrawable) {
    }

    @Override
    public void onResourceReady(Object resource, GlideAnimation glideAnimation) {
        Log.d(TAG, "Image cached: " + fileName);
    }

    @Override
    public void onLoadCleared(Drawable placeholder) {
    }

    @Override
    public void getSize(SizeReadyCallback cb) {
        cb.onSizeReady(500, 500);
    }

    @Override
    public void setRequest(Request request) {
        this.request = request;
    }

    @Override
    public Request getRequest() {
        return this.request;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onDestroy() {
    }
}
