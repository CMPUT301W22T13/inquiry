package com.cmput301w22t13.inquiry.ui.leaderboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.activities.PlayerProfileActivity;
import com.cmput301w22t13.inquiry.databinding.FragmentLeaderboardBinding;
import com.cmput301w22t13.inquiry.db.Database;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class LeaderboardFragment extends Fragment {

    private FragmentLeaderboardBinding binding;
    private final Database db = new Database();

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LeaderboardViewModel leaderboardViewModel = new ViewModelProvider(this).get(LeaderboardViewModel.class);

        binding = FragmentLeaderboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textLeaderboard;
        leaderboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        final EditText searchEditText = root.findViewById(R.id.leaderBoardEditTextSearch);
        final Button searchButton = root.findViewById(R.id.leaderBoardSearchButton);
        final TextView errorTextView = root.findViewById(R.id.leaderBoardSearchErrorTextView);
        searchButton.setOnClickListener(view -> {
            String searchString = searchEditText.getText().toString();
            if (searchString.isEmpty()) {
                searchEditText.setError("Please enter a username");
            } else {
                Task<QuerySnapshot> queryTask = db.query("users", "username", searchString.trim());
                queryTask.addOnCompleteListener(task -> {
                    if (queryTask.isSuccessful()) {
                        QuerySnapshot queryResults = queryTask.getResult();
                        List<DocumentSnapshot> documents = queryResults.getDocuments();
                        if (documents.size() != 0) {
                            Log.i("LeaderboardFragment", "Found users");
                            Log.i("LeaderboardFragment", documents.toString());
                            DocumentSnapshot document = documents.get(0);
                            String uid = (String) document.get("id");
                            Intent intent = new Intent(getActivity(), PlayerProfileActivity.class);
                            intent.putExtra("uid", uid);
                            startActivity(intent);
                        } else {
                            Log.i("LeaderboardFragment", "No users found");
                            errorTextView.setText("no users found with username: " + searchString);
                        }
                    } else {
                        Log.i("LeaderboardFragment", "Query failed");
                    }
                });

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