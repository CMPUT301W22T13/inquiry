package com.cmput301w22t13.inquiry.ui.myQRs;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.classes.QRCode;

import java.util.ArrayList;

public class AllQRsListAdapter  extends RecyclerView.Adapter<AllQRsListAdapter.ViewHolder> {

        private final ArrayList<QRCode> qrCodes;
        private final Context context;

        public AllQRsListAdapter(Context context, ArrayList<QRCode> qrCodes) {
            this.qrCodes = qrCodes;
            this.context = context;
        }

        @NonNull
        @Override
        public AllQRsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(context, R.layout.myqrs_list_item, null);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(lp);
            return new AllQRsListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AllQRsListAdapter.ViewHolder holder, int position) {
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

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView nameTextView;
            TextView initialsTextView;
            TextView scoreTextView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                nameTextView = itemView.findViewById(R.id.myqrs_qr_name);
                initialsTextView = itemView.findViewById(R.id.myqrs_qr_initials);
                scoreTextView = itemView.findViewById(R.id.myqrs_qr_score);
            }

        }
    }

