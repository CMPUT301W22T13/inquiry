package com.cmput301w22t13.inquiry.db;
/**
 *  Callback to fetch user data from firestore database
 */


import java.util.Map;

public interface onProfileDataListener {
    void getProfileData(Map<String, Object> data);
}
