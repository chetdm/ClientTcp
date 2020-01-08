package com.example.clienttcp;

import android.graphics.Bitmap;

public class NetworkResponse {
    private Bitmap bitmap;
    private String text;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getText() {
        return text;
    }

    NetworkResponse(Bitmap bitmap, String text) {
        this.bitmap = bitmap;
        this.text = text;
    }
}
