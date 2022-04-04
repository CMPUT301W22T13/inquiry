package com.cmput301w22t13.inquiry.ui.leaderboard;

/** populates leaderboard fragment with the rankings of all players and their score
 * can be swapped between 4 different display types (total Score, highest Score, lowest Score, QRCode Count)
 * also gives users the ability to search for a player by username and go to their profile,
 * you can also get to their profile by clicking their name in the leaderboard
 */
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.activities.PlayerProfileActivity;
import com.cmput301w22t13.inquiry.classes.LeaderBoard;
import com.cmput301w22t13.inquiry.classes.Player;
import com.cmput301w22t13.inquiry.databinding.FragmentLeaderboardBinding;
import com.cmput301w22t13.inquiry.db.Database;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class LeaderboardFragment extends Fragment {

    private FragmentLeaderboardBinding binding;
    private final Database db = new Database();
    private int mode = 1;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LeaderboardViewModel leaderboardViewModel = new ViewModelProvider(this).get(LeaderboardViewModel.class);

        binding = FragmentLeaderboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final EditText searchEditText = root.findViewById(R.id.leaderBoardEditTextSearch);
        final Button searchButton = root.findViewById(R.id.leaderBoardSearchButton);
        final TextView errorTextView = root.findViewById(R.id.leaderBoardSearchErrorTextView);

        //when search button is clicked gets the id from the database and starts the playerProfile activity if successful
        searchButton.setOnClickListener(view -> {
            String searchString = searchEditText.getText().toString();
            if (searchString.isEmpty()) {
                searchEditText.setError("Please enter a username");
            } else {
                Task<DocumentSnapshot> queryTask = db.getById("user_accounts", searchString.trim());
                queryTask.addOnCompleteListener(task -> {
                    if (queryTask.isSuccessful()) {
                        DocumentSnapshot document = queryTask.getResult();
                        if (document != null && document.exists()) {
                            Log.i("LeaderboardFragment", "Found users");
                            Log.i("LeaderboardFragment", document.toString());
                            Player player = new Player(document.getId(), document.getId(), true);
                            Intent intent = new Intent(getActivity(), PlayerProfileActivity.class);
                            intent.putExtra("Player", player);
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
        // initializations
        ArrayList<Player> playersArrayList = new ArrayList<>();
        ListView playersListView = root.findViewById(R.id.leaderBoardListView);
        LeaderBoardRankListAdapter playerListAdapter = new LeaderBoardRankListAdapter(requireActivity(), playersArrayList);
        playersListView.setAdapter(playerListAdapter);
        playersListView.setClickable(true);
        LeaderBoard.getPlayers(playersArrayList);
        LeaderBoard.bubbleSort(playersArrayList, 1);
        playerListAdapter.notifyDataSetChanged();

        // refreshes the list every second and sorts it again
        final Handler timerHandler = new Handler();
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                LeaderBoard.bubbleSort(playersArrayList, mode);
                playerListAdapter.notifyDataSetChanged();
                timerHandler.postDelayed(this, 1000);
            }
        };
        timerHandler.post(timerRunnable);

        // when player clicks on the leaderboard to go to a players profile
        playersListView.setClickable(true);
        playersListView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(getActivity(), PlayerProfileActivity.class);
            Player player = new Player(playersArrayList.get(i).getUsername(), playersArrayList.get(i).getUid(),true);
            intent.putExtra("Player", player);
            startActivity(intent);
        });

        //options for changing leaderboard view
        Button totalButton = root.findViewById(R.id.total_score_button);
        Button highestButton = root.findViewById(R.id.highest_score_button);
        Button countButton = root.findViewById(R.id.qrcode_count_button);
        Button lowestButton = root.findViewById(R.id.lowest_score_button);
        TextView scoreType = root.findViewById(R.id.leaderboard_score_type_textview);
        totalButton.setOnClickListener(view -> {
            mode = 1;
            scoreType.setText("Total Score");
            playerListAdapter.setType(1);
            LeaderBoard.bubbleSort(playersArrayList, mode);
            playerListAdapter.notifyDataSetChanged();
        });
        highestButton.setOnClickListener(view -> {
            mode = 2;
            scoreType.setText("Highest Score");
            playerListAdapter.setType(2);
            LeaderBoard.bubbleSort(playersArrayList, mode);
            playerListAdapter.notifyDataSetChanged();

        });
        countButton.setOnClickListener(view -> {
            mode = 3;
            scoreType.setText("QR Codes");
            playerListAdapter.setType(3);
            LeaderBoard.bubbleSort(playersArrayList, mode);
            playerListAdapter.notifyDataSetChanged();
        });
        lowestButton.setOnClickListener(view -> {
            mode = 4;
            scoreType.setText("Lowest Score");
            playerListAdapter.setType(4);
            LeaderBoard.bubbleSort(playersArrayList, mode);
            playerListAdapter.notifyDataSetChanged();
        });



        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}