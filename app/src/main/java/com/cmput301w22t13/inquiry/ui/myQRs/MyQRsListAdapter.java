package com.cmput301w22t13.inquiry.ui.myQRs;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.activities.QRDetailsActivity;
import com.cmput301w22t13.inquiry.auth.Auth;
import com.cmput301w22t13.inquiry.classes.QRCode;
import com.cmput301w22t13.inquiry.db.Storage;

import java.util.ArrayList;


public class MyQRsListAdapter extends RecyclerView.Adapter<MyQRsListAdapter.ViewHolder> {
    private final ArrayList<QRCode> qrCodes;
    private final Context context;
    Storage storage = new Storage();
    private final Auth auth = new Auth();

    public MyQRsListAdapter(Context context, ArrayList<QRCode> qrCodes) {
        this.qrCodes = qrCodes;
        this.context = context;
        Auth.init((player) -> {});



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

        String locationImage = qrCodes.get(position).getLocationImage();

        // add extra top margin to first item
        if (position == 0) {
            holder.itemView.setPadding(0, 40, 0, 0);
        }

        holder.nameTextView.setText(name);
        holder.initialsTextView.setText(initials);
        holder.scoreTextView.setText(String.valueOf(score) + " pts");


        if (locationImage != null) {
            Log.d("MyQRsListAdapter", "locationImage is not null");
            holder.locationImageView.setVisibility(View.VISIBLE);
            storage.getStorageRef("location_images", locationImage + ".jpg").getDownloadUrl().addOnSuccessListener(uri -> {
                String url = uri.toString();
                Log.d("MyQRsListAdapter", "url is " + url);
//                GlideApp.with(context).load(url).into(holder.locationImageView);
                Glide.with(context).load(url).into(holder.locationImageView);
            });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), QRDetailsActivity.class);
                intent.putExtra("code", qrCodes.get(holder.getAdapterPosition()));
                intent.putExtra("player", Auth.getPlayer().getUsername());
                view.getContext().startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return qrCodes.size();
    }

    public QRCode getQRAt(int position) {
        return qrCodes.get(position); //returns qr object at that position
    }

    public void removeQrAt(int position) {
        qrCodes.remove(position);
        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nameTextView;
        TextView initialsTextView;
        TextView scoreTextView;
        ImageView locationImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.myqrs_qr_name);
            initialsTextView = itemView.findViewById(R.id.myqrs_qr_initials);
            scoreTextView = itemView.findViewById(R.id.myqrs_qr_score);
            locationImageView = itemView.findViewById(R.id.myqrs_qr_location_image);
        }

        @Override
        public void onClick(View view) {
            Log.d("HELLO", "HELLO");
        }
    }
}
