package com.zhunussov.auadrop.ui.main;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.ScanMode;
import com.zhunussov.auadrop.App;
import com.zhunussov.auadrop.R;
import com.zhunussov.auadrop.model.Mode;
import com.zhunussov.auadrop.view.AuaProgressView;

import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.commands.Back;
import ru.terrakok.cicerone.commands.Command;
import ru.terrakok.cicerone.commands.Forward;

/**
 * Created by Kuanysh Zhunussov on 10/8/18.
 * Copyright @2018 AuaDrop. All rights reserved.
 */
public class MainView extends MvpAppCompatActivity implements MainContract.View {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 13;

    private CodeScanner codeScanner;

    @InjectPresenter
    MainPresenter presenter;

    @Inject
    NavigatorHolder navigatorHolder;

    @BindView(R.id.scanner_view)
    CodeScannerView codeScannerView;

    @BindView(R.id.progress)
    AuaProgressView progressView;

    @BindView(R.id.rlProgress)
    RelativeLayout progressWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        App.appComponent().inject(this);

        setupCodeScanner();

        handleActionIfAvailable();

        navigatorHolder.setNavigator(new Navigator() {
            @Override
            public void applyCommands(Command[] commands) {
                if (commands[0] instanceof Back) {
                    finish();
                } else if (commands[0] instanceof Forward) {
                    startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
                    finish();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (codeScanner != null)
            codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (codeScanner != null)
            codeScanner.releaseResources();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupCodeScanner();
            } else {
                // Permission denied.
            }
        }
    }


    @Override
    public void showProgress(int percentage) {
        progressWindow.setVisibility(View.VISIBLE);
        progressView.setProgressAnimated(percentage);
    }

    @Override
    public void showAs(Mode mode) {
        if (mode == Mode.UPLOAD) {

        } else {

        }
    }

    @Override
    public void hideProgress() {
        progressWindow.setVisibility(View.INVISIBLE);
        progressView.setProgress(0);
    }

    @Override
    public void showShareDialog(Uri uri) {

    }

    private void setupCodeScanner() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_REQUEST_CODE);
                return;
            }
        }
        codeScanner = new CodeScanner(this, codeScannerView);

        codeScanner.setCamera(CodeScanner.CAMERA_BACK);
        codeScanner.setFormats(CodeScanner.ALL_FORMATS);
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.setAutoFocusMode(AutoFocusMode.SAFE);
        codeScanner.setScanMode(ScanMode.SINGLE);
        codeScanner.setAutoFocusEnabled(true);

        // Callbacks
        codeScanner.setDecodeCallback(result -> {
            presenter.onQRCodeScanned(result.getText());
        });
    }

    private void handleActionIfAvailable() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
//                handleSendText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) {
                presenter.onFilesAvailableToUpload(Collections.singletonList(intent.getParcelableExtra(Intent.EXTRA_STREAM)));
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
//                handleSendMultipleImages(intent); // Handle multiple images being sent
            }
        }
    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
        }
    }

    void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            // Update UI to reflect multiple images being shared
        }
    }
}
