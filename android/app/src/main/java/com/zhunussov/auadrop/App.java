package com.zhunussov.auadrop;

import android.app.Application;

import com.zhunussov.auadrop.di.AppComponent;
import com.zhunussov.auadrop.di.DaggerAppComponent;
import com.zhunussov.auadrop.di.module.AppModule;
import com.zhunussov.auadrop.di.module.NavigationModule;
import com.zhunussov.auadrop.di.module.RepositoriesModule;

/**
 * Created by Kuanysh Zhunussov on 10/8/18.
 * Copyright @2018 AuaDrop. All rights reserved.
 */
public class App extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .navigationModule(new NavigationModule())
                .repositoriesModule(new RepositoriesModule())
                .build();
    }

    public static AppComponent appComponent() {
        return appComponent;
    }
}
