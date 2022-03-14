package com.cmput301w22t13.inquiry.ui.myQRs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.classes.QRCode;
import com.cmput301w22t13.inquiry.databinding.FragmentMyqrsBinding;
import com.cmput301w22t13.inquiry.ui.myQRs.MyQRsViewModel;

import java.util.ArrayList;

public class MyQRsFragment extends Fragment {

    private MyQRsViewModel myQRsViewModel;
    private FragmentMyqrsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myQRsViewModel =
                new ViewModelProvider(this).get(MyQRsViewModel.class);

        binding = FragmentMyqrsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView emptyStateText = root.findViewById(R.id.myqrs_text_empty_state);
        RecyclerView recyclerView = root.findViewById(R.id.myqrs_list);

        myQRsViewModel.getData(new onQrDataListener() {
            @Override
            public void getQrData(ArrayList<QRCode> qrCodes) {
                if(qrCodes != null && qrCodes.size() != 0) {
                    emptyStateText.setVisibility(View.GONE);
                }
                else {

                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}