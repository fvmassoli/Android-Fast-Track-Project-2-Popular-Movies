package com.example.android.fvmpopularmovieapp.utilities;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.android.fvmpopularmovieapp.frgament_main_grid.PosterGridRecyclerViewAdapter;


public final class UtilMethods {

    public static Typeface getTypeface(Context context, int type){
        if(type == 0)
            return Typeface.createFromAsset(context.getAssets(), "fonts/aller_lt.ttf");
        else
            return Typeface.createFromAsset(context.getAssets(), "fonts/aller_rg.ttf");
    }

    public static boolean isWifiConnected(Context context) {
        final ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.getState() == NetworkInfo.State.CONNECTED;
    }

    public static int setGridColumnNumber(Context context) {
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            return 5;
        else
            return 3;
    }

}
