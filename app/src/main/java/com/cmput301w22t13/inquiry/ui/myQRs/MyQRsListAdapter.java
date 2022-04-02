package com.cmput301w22t13.inquiry.ui.myQRs;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.activities.QRDetailsActivity;
import com.cmput301w22t13.inquiry.auth.Auth;
import com.cmput301w22t13.inquiry.classes.QRCode;

import java.util.ArrayList;


public class MyQRsListAdapter extends RecyclerView.Adapter<MyQRsListAdapter.ViewHolder> {
    private final ArrayList<QRCode> qrCodes;
    private final Context context;
    private final Auth auth = new Auth();

    public MyQRsListAdapter(Context context, ArrayList<QRCode> qrCodes ) {
        this.qrCodes = qrCodes;
        this.context = context;
        auth.init();



    }

    @NonNull
    @Override
    public MyQRsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.myqrs_list_item, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyQRsListAdapter.ViewHolder holder, int position) {
        String name = qrCodes.get(position).getName();
        String initials = name.substring(0, 1);
        int score = qrCodes.get(position).getScore();

        // add extra top margin to first item
        if (position == 0) {
            holder.itemView.setPadding(0, 40, 0, 0);
        }

        holder.nameTextView.setText(name);
        holder.initialsTextView.setText(initials);
        holder.scoreTextView.setText(String.valueOf(score) + " pts");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), QRDetailsActivity.class);
                intent.putExtra("code", qrCodes.get(holder.getAdapterPosition()));
                intent.putExtra("player", auth.getPlayer().getUsername());
                view.getContext().startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return qrCodes.size();
    }

    public QRCode getQRAt(int position){
        return  qrCodes.get(position); //returns qr object at that position
    }

    public void removeQrAt(int position){
        qrCodes.remove(position);
        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nameTextView;
        TextView initialsTextView;
        TextView scoreTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.myqrs_qr_name);
            initialsTextView = itemView.findViewById(R.id.myqrs_qr_initials);
            scoreTextView = itemView.findViewById(R.id.myqrs_qr_score);
        }

        @Override
        public void onClick(View view) {
            Log.d("HELLO", "HELLO");
        }
    }
}
