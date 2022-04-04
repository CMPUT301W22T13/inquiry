package com.cmput301w22t13.inquiry.ui.leaderboard;
/**
 * an adapter for the ListView in the leaderboard fragment
 * displays rank player name and the contextual score
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.classes.Player;
import com.cmput301w22t13.inquiry.classes.QRCode;

import java.util.ArrayList;

public class LeaderBoardRankListAdapter extends ArrayAdapter {
    private ArrayList<Player> players;
    private Context context;
    private int type = 1;


    public LeaderBoardRankListAdapter(@NonNull Context context, ArrayList<Player> players) {
        super(context,0, players);
        this.context = context;
        this.players = players;

    }

    /**
     * Set type
     * @param type type
     */
    public void setType(int type){
        this.type = type;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.leaderboard_view_list_item, parent,false);
        }

        // get player
        Player player = players.get(position);

        // get views
        TextView playerTextView = view.findViewById(R.id.leaderBoardPlayerTextView);
        TextView rankTextView = view.findViewById(R.id.leaderBoardRankTextView);
        TextView scoreTextView = view.findViewById(R.id.leaderBoardScoreTextView);
        playerTextView.setText(player.getUsername()); // player class not complete may need to change
        rankTextView.setText(Integer.toString(position+1));

        // depending on the type of score chosen it displays a different score for each user
        if (type == 1) scoreTextView.setText(Integer.toString(player.getTotalScore()));
        if (type == 2) scoreTextView.setText(Integer.toString(player.getHighestScore()));
        if (type == 3) scoreTextView.setText(Integer.toString(player.getQRCodeCount()));
        if (type == 4) scoreTextView.setText(Integer.toString(player.getLowestScore()));



        return view;
    }
}
