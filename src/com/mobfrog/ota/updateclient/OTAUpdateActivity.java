package com.mobfrog.ota.updateclient;

import android.app.Activity;
import android.os.Bundle;


public class OTAUpdateActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTheme(android.R.style.Theme_Translucent);
        Bundle extras = this.getIntent().getExtras();
        if(extras == null || extras.getParcelable("update_info") == null) {
            this.finish();
            return;
        }

        OTAUpdateInfo updateInfo = extras.getParcelable("update_info");
        OTAUpdateDialog updateDialog = new OTAUpdateDialog(this)
                .setUpdateLink(updateInfo.getUpdateUrl())
                .setTitle(updateInfo.getDialogTitle())
                .setForceUpdate(updateInfo.isForceUpdate())
                .setUpdateButtonText(updateInfo.getUpdateButtonText())
                .setSkipButtonText(updateInfo.getSkipButtonText())
                .setMessage(updateInfo.getRecentChanges())
                .useAppIcon(true)
                .build();
        updateDialog.show();
    }

}
