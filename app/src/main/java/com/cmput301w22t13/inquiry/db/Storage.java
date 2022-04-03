package com.cmput301w22t13.inquiry.db;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Storage {
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageRef = storage.getReference();

    /**
     * Store a file
     * @param fileRef the path to file, including the file name and extension
     * @param data file data in byte array
     * @return UploadTask - the task that will upload the file, useful for monitoring progress
     */
    public UploadTask storeFileBytes(String fileRef, byte[] data) {
        StorageReference fileRefStorage = storageRef.child(fileRef);
        return fileRefStorage.putBytes(data);
    }


}
