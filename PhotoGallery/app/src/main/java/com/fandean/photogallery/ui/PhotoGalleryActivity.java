package com.fandean.photogallery.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class PhotoGalleryActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new PhotoGalleryFragment();
    }

    public static Intent newIntent(Context context){
        return new Intent(context,PhotoGalleryActivity.class);
    }
}
