package com.cmput301w22t13.inquiry.classes;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.cmput301w22t13.inquiry.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.stream.IntStream;

public class QRBitmap {

    String code;
    int size;

    public QRBitmap(String code, int size) {
       this.code = code;
       this.size = size;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Bitmap getBitmap(Resources r) throws WriterException {
        BitMatrix bm = new QRCodeWriter().encode(code, BarcodeFormat.QR_CODE, size, size);
        int qrColor = r.getString(R.string.theme_mode).equals("dark") ? Color.argb(255, 176, 116,255) : Color.argb(200, 140, 60,255);
        return Bitmap.createBitmap(IntStream.range(0, size).flatMap(h -> IntStream.range(0, size).map(w -> bm.get(w, h) ? qrColor : Color.argb(0, 0, 0, 0))).toArray(),
                size, size, Bitmap.Config.ARGB_8888);
    }
}
