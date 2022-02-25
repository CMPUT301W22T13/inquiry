package com.cmput301w22t13.inquiry.ui.scanner;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cmput301w22t13.inquiry.activities.ScanActivity;
import com.cmput301w22t13.inquiry.databinding.ScannerFragmentBinding;

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
        Intent intent = new Intent(getActivity(), ScanActivity.class);
        startActivity(intent);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}