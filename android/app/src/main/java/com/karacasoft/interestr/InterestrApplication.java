package com.karacasoft.interestr;

import android.app.Application;

import com.karacasoft.interestr.network.InterestrAPI;
import com.karacasoft.interestr.network.InterestrAPIImpl;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by karacasoft on 01.11.2017.
 */

public class InterestrApplication extends Application {

    private InterestrAPI api;

    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoaderConfiguration config = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(config);
    }

    public InterestrAPI getApi() {
        if(api == null) {
            api = new InterestrAPIImpl(this);
        }
        return api;
    }
}
