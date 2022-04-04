package com.cmput301w22t13.inquiry.ui.myQRs;

import android.content.DialogInterface;
import android.graphics.Canvas;
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
import com.cmput301w22t13.inquiry.auth.Auth;
import com.cmput301w22t13.inquiry.classes.Owner;
import com.cmput301w22t13.inquiry.classes.Player;
import com.cmput301w22t13.inquiry.classes.QRCode;
import com.cmput301w22t13.inquiry.databinding.FragmentMyqrsBinding;
import com.cmput301w22t13.inquiry.db.onQrDataListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

/**
 * see QRCodes that either has been scanned for Owner or QRCodes they have scanned for player
 */
public class MyQRsFragment extends Fragment {

    private FragmentMyqrsBinding binding;
    MyQRsListAdapter qrCodeListAdapter;
    private final Auth auth = new Auth();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d("VERBS","hi there");
        if(auth.getPlayer() != null && auth.getPlayer().getIsOwner()){



            Log.d("VERBS","is a Owner");
            MyQRsViewModel myQRsViewModel = new ViewModelProvider(this).get(MyQRsViewModel.class);

            binding = FragmentMyqrsBinding.inflate(inflater, container, false);
            View root = binding.getRoot();

            TextView emptyStateText = root.findViewById(R.id.myqrs_text_empty_state);
            RecyclerView recyclerView = root.findViewById(R.id.myqrs_list);

            LinearLayoutManager layoutManager = new LinearLayoutManager(root.getContext());

            recyclerView.setLayoutManager(layoutManager);

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

            /**user swipes right to delete QR code
             * from youtube.com
             * url : https://youtu.be/QJUCD32dzHE
             * author : Florian Walther https://www.youtube.com/channel/UC_Fh8kvtkVPkeihBs42jGcA
            */
            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                    ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    int position = viewHolder.getAdapterPosition();
                    QRCode qrCode = qrCodeListAdapter.getQRAt(position);

                    //ask for user confirmation
            /*
            //from youtube.com
            //url : https://youtu.be/yDuydV2IPEA
            //author : Sanath Gosavi https://www.youtube.com/channel/UC5hwBZynOhshCbqTGGeoRSA
            */
                    MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(requireActivity());
                    dialog.setTitle("Delete " + qrCode.getName() + "?");
                    dialog.setMessage("Are you sure you want to delete this QR code?");
                    dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int p) {

                            Log.d("VERBS",qrCode.getId());
                            myQRsViewModel.deleteQRCode(qrCode);
                            Toast.makeText(getActivity(), "QR Code Deleted", Toast.LENGTH_SHORT).show();
                            myQRsViewModel.getPlayer().fetchQRCodes(task -> {
                                qrCodeListAdapter.removeQrAt(position);
                                if(task.size() == 0) {
                                    recyclerView.setVisibility(View.GONE);
                                    emptyStateText.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });

                    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // cancel swipe
//                        qrCodeListAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        }
                    });

                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            // cancel swipe
                            qrCodeListAdapter.notifyItemChanged(viewHolder.getAdapterPosition());

                        }
                    });

                    dialog.show();
                }

                @Override
                public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                    new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                            .addActionIcon(R.drawable.ic_baseline_delete_24)
                            .create()
                            .decorate();
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }).attachToRecyclerView(recyclerView);
            return root;
        }else{

            MyQRsViewModel myQRsViewModel = new ViewModelProvider(this).get(MyQRsViewModel.class);

            binding = FragmentMyqrsBinding.inflate(inflater, container, false);
            View root = binding.getRoot();

            TextView emptyStateText = root.findViewById(R.id.myqrs_text_empty_state);
            RecyclerView recyclerView = root.findViewById(R.id.myqrs_list);

            LinearLayoutManager layoutManager = new LinearLayoutManager(root.getContext());

            recyclerView.setLayoutManager(layoutManager);

            Player player = myQRsViewModel.getPlayer();

            if (player != null) {
                player.fetchQRCodes(new onQrDataListener() {
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
            }
          
            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                    ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    int position = viewHolder.getAdapterPosition();
                    QRCode qrCode = qrCodeListAdapter.getQRAt(position);

                    //ask for user confirmation
                /*
                //from youtube.com
                //url : https://youtu.be/yDuydV2IPEA
                //author : Sanath Gosavi https://www.youtube.com/channel/UC5hwBZynOhshCbqTGGeoRSA
                */
                    MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(requireActivity());
                    dialog.setTitle("Delete " + qrCode.getName() + "?");
                    dialog.setMessage("Are you sure you want to delete this QR code?");
                    dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int p) {

                            myQRsViewModel.deleteQRFromPlayer(qrCode.getId());
                            Toast.makeText(getActivity(), "QR Code Deleted", Toast.LENGTH_SHORT).show();
                            myQRsViewModel.getPlayer().fetchQRCodes(task -> {
                                qrCodeListAdapter.removeQrAt(position);
                                if(task.size() == 0) {
                                    recyclerView.setVisibility(View.GONE);
                                    emptyStateText.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });

                    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // cancel swipe
//                        qrCodeListAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        }
                    });

                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            // cancel swipe
                            qrCodeListAdapter.notifyItemChanged(viewHolder.getAdapterPosition());

                        }
                    });

                    dialog.show();
                }

                @Override
                public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                    new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                            .addActionIcon(R.drawable.ic_baseline_delete_24)
                            .create()
                            .decorate();
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }).attachToRecyclerView(recyclerView);
            return root;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}