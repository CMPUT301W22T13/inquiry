package com.cmput301w22t13.inquiry.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.classes.QRBitmap;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.zxing.WriterException;

/**
 * Display a bottom sheet dialog with a QR login code.
 */
public class QRLoginCodeDialog extends BottomSheetDialogFragment {

    String uid;

    public QRLoginCodeDialog(String uid) {
        super();
        this.uid = uid;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login_qr, container, false);

        // find qr code view
        ImageView shareProfileQrCode = view.findViewById(R.id.login_profile_qr);
        int size = 200;
        try {
            // generate a 200x200 QR code encoded with the user's id
            shareProfileQrCode.setImageBitmap(new QRBitmap("INQUIRY_LOGIN_" + uid, size).getBitmap(getResources()));
        } catch (WriterException e) {
        }

        return view;
    }
}
