package com.cmput301w22t13.inquiry.ui.scanner;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.databinding.FragmentHomeBinding;
import com.cmput301w22t13.inquiry.databinding.ScannerFragmentBinding;
import com.cmput301w22t13.inquiry.ui.home.HomeViewModel;

public class ScannerFragment extends Fragment {

    private ScannerViewModel scannerViewModel;
    private ScannerFragmentBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        scannerViewModel = new ViewModelProvider(this).get(ScannerViewModel.class);
        binding = ScannerFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        final TextView textView = binding.textScanner;
        scannerViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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