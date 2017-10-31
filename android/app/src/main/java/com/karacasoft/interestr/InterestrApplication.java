package com.karacasoft.interestr;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by karacasoft on 01.11.2017.
 */

public class InterestrApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoaderConfiguration config = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(config);
    }
}
