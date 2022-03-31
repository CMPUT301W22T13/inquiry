package com.cmput301w22t13.inquiry.ui.myQRs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.classes.QRCode;
import com.cmput301w22t13.inquiry.databinding.FragmentMyqrsBinding;
import com.cmput301w22t13.inquiry.db.onQrDataListener;

import java.util.ArrayList;

public class MyQRsFragment extends Fragment {

    private FragmentMyqrsBinding binding;
    MyQRsListAdapter qrCodeListAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MyQRsViewModel myQRsViewModel = new ViewModelProvider(this).get(MyQRsViewModel.class);

        binding = FragmentMyqrsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView emptyStateText = root.findViewById(R.id.myqrs_text_empty_state);
        RecyclerView recyclerView = root.findViewById(R.id.myqrs_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

        myQRsViewModel.getPlayer().fetchQRCodes(new onQrDataListener() {
            @Override
            public void getQrData(ArrayList<QRCode> qrCodes) {
                if (qrCodes != null && qrCodes.size() > 0) {
                    qrCodeListAdapter = new MyQRsListAdapter(root.getContext(), qrCodes);
                    recyclerView.setAdapter(qrCodeListAdapter);

                    recyclerView.setVisibility(View.VISIBLE);
                    emptyStateText.setVisibility(View.GONE);
                }
            }
        });
        //user swipes right to delete QR code
        /*
        //from youtube.com
        //url : https://youtu.be/QJUCD32dzHE
        //author : Florian Walther https://www.youtube.com/channel/UC_Fh8kvtkVPkeihBs42jGcA
        */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                //ask for user confirmation
                /*
                //from youtube.com
                //url : https://youtu.be/yDuydV2IPEA
                //author : Sanath Gosavi https://www.youtube.com/channel/UC5hwBZynOhshCbqTGGeoRSA
                */

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Delete QRCode");
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        //data.removeAt(position);
                        //notifyDataSetChanged();
                        //myQRsViewModel.delete(adapter.getQRAt(viewHolder.getAdapterPosition()));
                        //Toast.makeText(getActivity(), "Game Deleted", Toast.LENGTH_SHORT).show();
                        //adapter.getRef(position).removeValue()
                        

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //adapter.notifyItemChanged(viewHolder.getAdapterPosition());

                    }
                });
                builder.show();
            }
        }).attachToRecyclerView(recyclerView);


        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}