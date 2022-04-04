package com.cmput301w22t13.inquiry.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.activities.QRDetailsActivity;
import com.cmput301w22t13.inquiry.auth.Auth;

import java.util.ArrayList;

/**
 * List adapter to show QR Codes
 */
public class QRListArrayAdapter extends ArrayAdapter<RelativeQRLocation> {
    private final ArrayList<RelativeQRLocation> list;

    public QRListArrayAdapter(Context context, ArrayList<RelativeQRLocation> list) {
        super(context, 0, list);
        this.list = list;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View getView (int position,
                         View convertView,
                         ViewGroup parent) {

        // get the relevant location
        RelativeQRLocation rqr = list.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.myqrs_list_item, parent, false);
        }

        // get views

        TextView nameTextView = convertView.findViewById(R.id.myqrs_qr_name);
        TextView initialsTextView = convertView.findViewById(R.id.myqrs_qr_initials);
        TextView scoreTextView = convertView.findViewById(R.id.myqrs_qr_score);

        // update views
        nameTextView.setText(rqr.getQr().getName());
        initialsTextView.setText(rqr.getQr().getName().substring(0, 1));
        scoreTextView.setText(String.format("%d pts  |  %.2f m", rqr.getQr().getScore(), rqr.getDist()));

        convertView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), QRDetailsActivity.class);
            intent.putExtra("code", rqr.getQr());
            intent.putExtra("player", Auth.getPlayer().getUsername());
            view.getContext().startActivity(intent);
        });
        return convertView;
    }




}
