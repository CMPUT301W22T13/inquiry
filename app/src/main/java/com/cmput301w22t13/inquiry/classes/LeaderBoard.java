package com.cmput301w22t13.inquiry.classes;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LeaderBoard {
    //gets the players from a database and adds them to the ArrayList given in the argument
    public static void getPlayers(ArrayList<Player> playersArrayList){
        Task<QuerySnapshot> playersQuery = FirebaseFirestore.getInstance().collection("users").get();
        playersQuery.addOnCompleteListener(task -> {
            if (playersQuery.isSuccessful()) {
                QuerySnapshot queryResults = playersQuery.getResult();
                List<DocumentSnapshot> documents = queryResults.getDocuments();
                if (documents.size() != 0) {
                    for (DocumentSnapshot document : documents) {
                        Player newPlayer = new Player((String) document.get("username"),(String) document.get("id"),true);
                        playersArrayList.add(newPlayer);
                    }
                } else Log.i("LeaderboardFragment", "documents empty");
            } else Log.i("LeaderboardFragment", "query not successful");
        });
    }
    // sort an ArrayList of PLayers 4 different ways depending on what is wanted
    public static void bubbleSort(ArrayList<Player> a, int b){
        boolean sorted = false;
        Player temp;
        while(!sorted){
            sorted = true;
            for (int i = a.size()-1; i > 0; i--){
                if (b == 1){
                    if (a.get(i).getTotalScore() > a.get(i-1).getTotalScore()){
                        temp = a.get(i);
                        a.set(i,a.get(i-1));
                        a.set(i-1,temp);
                        sorted = false;
                    }
                }
                if (b == 2){
                    if (a.get(i).getHighestScore() > a.get(i-1).getHighestScore()){
                        temp = a.get(i);
                        a.set(i,a.get(i-1));
                        a.set(i-1,temp);
                        sorted = false;
                    }
                }
                if (b == 3){
                    if (a.get(i).getQRCodeCount() > a.get(i-1).getQRCodeCount()){
                        temp = a.get(i);
                        a.set(i,a.get(i-1));
                        a.set(i-1,temp);
                        sorted = false;
                    }
                }
                if (b == 4){
                    if (a.get(i).getLowestScore() < a.get(i-1).getLowestScore() && a.get(i).getLowestScore() != 0){
                        temp = a.get(i);
                        a.set(i,a.get(i-1));
                        a.set(i-1,temp);
                        sorted = false;
                    }
                }
            }
        }
    }
}
