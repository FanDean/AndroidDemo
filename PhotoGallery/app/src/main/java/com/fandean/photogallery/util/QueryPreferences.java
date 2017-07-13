package com.fandean.photogallery.util;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by fan on 17-7-12.
 */

public class QueryPreferences {
    private static final String PREF_SEARCH_QUERY = "searchQuery";
    private static final String PREF_LAST_RESULT_ID = "lastResultId";

    public static String getStoredQuery(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_SEARCH_QUERY,null);
    }

    public static void setPrefSearchQuery(Context context,String query){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_SEARCH_QUERY,query)
                .apply();
    }

    public static String getLastResultId(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_LAST_RESULT_ID,null);
    }

    public static void setLastResultId(Context context,String query){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_LAST_RESULT_ID,query)
                .apply();
    }
}
