package com.zhunussov.auadrop.data;

import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by Kuanysh Zhunussov on 10/7/18.
 * Copyright @2018 AuaDrop. All rights reserved.
 */
public class FileRepository {

    private static final String TAG = "Repository";

    private StorageReference reference;
    private FirebaseStorage storage;

    public FileRepository() {
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();
    }

    public Observable<Integer> uploadFile(Uri file, String filename) {
        final BehaviorSubject<Integer> subject = BehaviorSubject.create();

        StorageReference fileRef = reference.child("test/" + filename);
        UploadTask uploadTask = fileRef.putFile(file);

        uploadTask.addOnFailureListener(subject::onError)
                .addOnSuccessListener(taskSnapshot -> subject.onComplete())
                .addOnProgressListener(taskSnapshot -> subject.onNext((int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount())));

        return subject;
    }

    public Observable<Integer> downloadFile(String filename) {
        final BehaviorSubject<Integer> subject = BehaviorSubject.create();

        StorageReference fileRef = reference.child("test/" + filename);

        File localFile = new File(Environment.getExternalStorageDirectory(), filename + ".zip");

        fileRef.getFile(localFile)
                .addOnSuccessListener(taskSnapshot -> subject.onComplete())
                .addOnFailureListener(subject::onError)
                .addOnProgressListener(taskSnapshot -> subject.onNext((int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount())));

        return subject;
    }
}
