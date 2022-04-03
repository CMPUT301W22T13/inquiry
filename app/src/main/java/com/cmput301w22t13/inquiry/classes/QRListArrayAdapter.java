package com.cmput301w22t13.inquiry.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cmput301w22t13.inquiry.R;

import java.util.ArrayList;

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

        RelativeQRLocation rqr = list.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.myqrs_list_item, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.myqrs_qr_name);
        TextView initialsTextView = convertView.findViewById(R.id.qr_details_initials);
        TextView scoreTextView = convertView.findViewById(R.id.myqrs_qr_score);

        nameTextView.setText(rqr.getQr().getName());
        initialsTextView.setText(rqr.getQr().getName().substring(0, 1));
        scoreTextView.setText(String.format("%.2f m", rqr.getDist()));

        return convertView;
    }




}
