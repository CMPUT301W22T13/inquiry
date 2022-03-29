package com.cmput301w22t13.inquiry.ui.leaderboard;

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


    public LeaderBoardRankListAdapter(@NonNull Context context, ArrayList<Player> players) {
        super(context,0, players);
        this.context = context;
        this.players = players;

    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.leaderboard_view_list_item, parent,false);
        }
        Player player = players.get(position);
        TextView playerTextView = view.findViewById(R.id.leaderBoardPlayerTextView);
        TextView rankTextView = view.findViewById(R.id.leaderBoardRankTextView);
        TextView scoreTextView = view.findViewById(R.id.leaderBoardScoreTextView);
        playerTextView.setText(player.getUsername()); // player class not complete may need to change
        rankTextView.setText(Integer.toString(position+1));
        scoreTextView.setText(Integer.toString(player.getTotalScore()));



        return view;
    }
}
