package com.zhunussov.auadrop.di.module;

import com.zhunussov.auadrop.data.FileRepository;
import com.zhunussov.auadrop.data.UserRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Kuanysh Zhunussov on 10/8/18.
 * Copyright @2018 AuaDrop. All rights reserved.
 */
@Module
public class RepositoriesModule {

    @Provides
    @Singleton
    FileRepository provideFileRepository() {
        return new FileRepository();
    }

    @Provides
    @Singleton
    UserRepository provideUserRepository() {
        return new UserRepository();
    }

}
