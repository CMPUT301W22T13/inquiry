package com.cmput301w22t13.inquiry.ui.myQRs;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.activities.MatchingQRActivity;
import com.cmput301w22t13.inquiry.classes.QRCode;

import java.util.ArrayList;


public class MyQRsListAdapter extends RecyclerView.Adapter<MyQRsListAdapter.ViewHolder> {
    private final ArrayList<QRCode> qrCodes;
    private final Context context;

    public MyQRsListAdapter(Context context, ArrayList<QRCode> qrCodes) {
        this.qrCodes = qrCodes;
        this.context = context;
    }

    @NonNull
    @Override
    public MyQRsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.myqrs_list_item, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "This is my Toast message!",
                        Toast.LENGTH_LONG).show();
                Log.d("HELLO","HELOOOOOOOOOOOOOO");
            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyQRsListAdapter.ViewHolder holder, int position) {
        String name = qrCodes.get(position).getName();
        String initials = name.substring(0, 1);
        int score = qrCodes.get(position).getScore();

        holder.nameTextView.setText(name);
        holder.initialsTextView.setText(initials);
        holder.scoreTextView.setText(String.valueOf(score) + " pts");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = view.getContext();
                /*
                Intent intent = new Intent(context, MatchingQRActivity.class);
                intent.putExtra("code", qrCodes.get(holder.getAdapterPosition()));
                intent.putExtra("player","YaBoyIlia");

                 */
                Toast.makeText(context, "This is my Toast message!",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return qrCodes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
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
            Log.d("BITCH", "HOE");
        }

    }
}
