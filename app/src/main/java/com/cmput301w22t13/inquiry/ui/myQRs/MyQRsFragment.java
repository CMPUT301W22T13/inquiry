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

import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.databinding.FragmentMyqrsBinding;
import com.cmput301w22t13.inquiry.ui.myQRs.MyQRsViewModel;

public class MyQRsFragment extends Fragment {

    private MyQRsViewModel myQRsViewModel;
    private FragmentMyqrsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myQRsViewModel =
                new ViewModelProvider(this).get(MyQRsViewModel.class);

        binding = FragmentMyqrsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textMyqrs;
        myQRsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
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