package com.mobfrog.ota.updateclient;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.AndroidRuntimeException;
import android.util.Log;

/**
 * Created by Code4LifeVn on 2/17/2015.
 */
public class OTAUpdateManager {

    private Context context;

    public OTAUpdateManager(Context context) {
        this.context = context;
    }

    public void check(OTAUpdateOptions options) throws NullPointerException, AndroidRuntimeException {
        this.check(options, null);
    }

    public void check(OTAUpdateOptions options, OTAAbstractUpdateListener listener) throws NullPointerException, AndroidRuntimeException {
        if(this.context == null) {
            Log.e("OTAUpdateCenter", "Context is null -> Skip check for update");
            return;
        }

        if(options == null) {
            Log.e("OTAUpdateCenter", "Options is null -> Skip check for update");
            return;
        }

        if(listener == null) {
            listener = new OTADefaultUpdateListener(this.context, options);
        }

        if(options.shouldCheckUpdate()) {
            new OTACheckUpdateAsyncTask(this.context, this.getAPIToken(), listener).execute(options);
        } else {
            Log.d("OTAUpdateCenter", "Skip check for update");
        }
    }

    private String getAPIToken() {
        return this.getMetadata(this.context, "com.mobfrog.ota.CLIENT_KEY");
    }

    private String getMetadata(Context context, String name) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                return appInfo.metaData.getString(name);
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
        return "";
    }
}
