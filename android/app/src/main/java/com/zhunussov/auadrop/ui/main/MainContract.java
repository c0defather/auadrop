package com.zhunussov.auadrop.ui.main;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.MvpView;
import com.zhunussov.auadrop.model.Mode;

import java.util.List;

/**
 * Created by Kuanysh Zhunussov on 10/7/18.
 * Copyright @2018 AuaDrop. All rights reserved.
 */
public interface MainContract {

    interface View extends MvpView {
        void showAs(Mode mode);
        void showProgress(int percentage);
        void hideProgress();
        void showShareDialog(Uri uri);
    }

    interface Presenter {
        void onQRCodeScanned(@NonNull String text);
        void onFilesAvailableToUpload(@Nullable List<Uri> uris);
    }
}
