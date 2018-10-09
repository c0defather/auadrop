package com.zhunussov.auadrop.ui.main;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.zhunussov.auadrop.App;
import com.zhunussov.auadrop.data.FileRepository;
import com.zhunussov.auadrop.data.UserRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.terrakok.cicerone.Router;

/**
 * Created by Kuanysh Zhunussov on 10/8/18.
 * Copyright @2018 AuaDrop. All rights reserved.
 */
@InjectViewState
public class MainPresenter extends MvpPresenter<MainContract.View> implements MainContract.Presenter {

    @Inject
    Router router;

    @Inject
    FileRepository fileRepo;

    @Inject
    UserRepository userRepo;

    private List<Uri> filesUris;

    MainPresenter() {
        App.appComponent().inject(this);
    }
    @Override
    public void onFirstViewAttach() {
        userRepo.signInAnonymously();
    }

    @Override
    public void onQRCodeScanned(@NonNull String text) {
        if (filesUris == null || filesUris.size() == 0) {
            fileRepo.downloadFile(text)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Integer>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Integer integer) {
                            getViewState().showProgress(integer);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {
                            router.navigateTo(text + ".zip");
                        }
                    });
            return;
        }
        fileRepo.uploadFile(filesUris.get(0), text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        getViewState().showProgress(integer);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                        router.exit();
                    }
                });
    }

    @Override
    public void onFilesAvailableToUpload(@NonNull List<Uri> uris) {
        filesUris = new ArrayList<>(uris);
    }

    @Override
    public void onDestroy() {

    }
}
