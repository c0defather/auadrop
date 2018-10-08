package com.zhunussov.auadrop.data;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.Single;
import io.reactivex.subjects.SingleSubject;

/**
 * Created by Kuanysh Zhunussov on 10/8/18.
 * Copyright @2018 AuaDrop. All rights reserved.
 */
public class UserRepository {

    private FirebaseAuth auth;

    public UserRepository() {
        auth = FirebaseAuth.getInstance();
    }

    public Single<AuthResult> signInAnonymously() {
        SingleSubject<AuthResult> subject = SingleSubject.create();

        auth.signInAnonymously()
                .addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                subject.onSuccess(task.getResult());
            } else {
                subject.onError(new Throwable("Failed to sign in anonymously"));
            }
        });

        return subject;
    }
}
