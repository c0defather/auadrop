package com.zhunussov.auadrop.di;

import com.zhunussov.auadrop.di.module.AppModule;
import com.zhunussov.auadrop.di.module.NavigationModule;
import com.zhunussov.auadrop.di.module.RepositoriesModule;
import com.zhunussov.auadrop.ui.main.MainPresenter;
import com.zhunussov.auadrop.ui.main.MainView;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Kuanysh Zhunussov on 10/8/18.
 * Copyright @2018 AuaDrop. All rights reserved.
 */
@Singleton
@Component(modules = {AppModule.class, NavigationModule.class, RepositoriesModule.class})
public interface AppComponent {
    void inject(MainPresenter presenter);
    void inject(MainView view);
}
