package com.cmput301w22t13.inquiry.classes;
/** a custom listAdapter that uses the activity_player_status.xml
 * sets the views in the xml depending on the player chosen and their QRCodes
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.cmput301w22t13.inquiry.R;

import java.util.ArrayList;



public class PlayerStatusQRCodeListAdapter extends ArrayAdapter {
    private ArrayList<QRCode> qrCodes;
    private Context context;
    private Player player;

    public PlayerStatusQRCodeListAdapter(@NonNull Context context, ArrayList<QRCode> qrCodes, Player player) {
        super(context,0,qrCodes);
        this.qrCodes = qrCodes;
        this.context = context;
        this.player = player;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.profile_view_list_item, parent,false);
        }
        QRCode qrCode = qrCodes.get(position);
        TextView playerTextView = view.findViewById(R.id.QRCodePlayerTextView);
        TextView numberTextView = view.findViewById(R.id.QRCodeNumberTextView);
        TextView scoreTextView = view.findViewById(R.id.QRCodeScoreTextView);
        playerTextView.setText(player.getUsername()); // player class not complete may need to change
        numberTextView.setText(position+1);
        scoreTextView.setText(qrCode.getScore());


        return view;
    }

}
