package com.softdesign.devintensive.data.network;

import android.content.Context;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;

/**
 * Created by shmakova on 16.07.16.
 */

public class PicassoCache {
    private Context mContext;
    private Picasso mPicassoInstance;

    public PicassoCache(Context context) {
        OkHttp3Downloader okHttp3Downloader = new OkHttp3Downloader(context, Integer.MAX_VALUE);
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(okHttp3Downloader);
        mPicassoInstance = builder.build();
        Picasso.setSingletonInstance(mPicassoInstance);
        mContext = context;
    }

    public Picasso getPicassoInstance() {
        if (mPicassoInstance == null) {
            new PicassoCache(mContext);
        }

        return mPicassoInstance;
    }
}
