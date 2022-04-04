package com.cmput301w22t13.inquiry.ui.map;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.cmput301w22t13.inquiry.classes.QRCode;
import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryBounds;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MapViewModel extends ViewModel {

    public MapViewModel() { }

    public interface NearbyPointHandler {
        void handlePoint(QRCode qr);
    }

    public interface NearbyPointCompleteHandler {
        void onComplete();
    }

    public void getNearbyPoints(double lat, double lng, NearbyPointHandler l, NearbyPointCompleteHandler lc) {

        final GeoLocation center = new GeoLocation(lat, lng);
        final double radiusInM = 30 * 1000;

        // Each item in 'bounds' represents a startAt/endAt pair. We have to issue
        // a separate query for each pair. There can be up to 9 pairs of bounds
        // depending on overlap, but in most cases there are 4.
        List<GeoQueryBounds> bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInM);
        final List<Task<QuerySnapshot>> tasks = new ArrayList<>();
        for (GeoQueryBounds b : bounds) {
            Query q = FirebaseFirestore.getInstance().collection("qr_codes")
                    .orderBy("geohash")
                    .startAt(b.startHash)
                    .endAt(b.endHash);

            tasks.add(q.get());
        }

        // Collect all the query results together into a single list
        Tasks.whenAllComplete(tasks)
                .addOnCompleteListener(t -> {
                    for (Task<QuerySnapshot> task : tasks) {
                        QuerySnapshot snap = task.getResult();
                        for (DocumentSnapshot doc : snap.getDocuments()) {
                            try {
                                double lat1 = doc.getDouble("lat");
                                double lng1 = doc.getDouble("lng");
                                String hash = doc.getString("hash");
                                int score = doc.get("score", int.class);
                                GeoLocation docLocation = new GeoLocation(lat1, lng1);

                                // We have to filter out a few false positives due to GeoHash
                                // accuracy, but most will match
                                double distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center);
                                if (distanceInM <= radiusInM) {
                                    l.handlePoint(new QRCode(hash, score, new LatLng(docLocation.latitude, docLocation.longitude)));
                                }

                            } catch (NullPointerException e) {
                                Log.v("NO_LOCATION", e.getMessage());
                            }

                        }
                    }

                    lc.onComplete();
                });
    }

}
