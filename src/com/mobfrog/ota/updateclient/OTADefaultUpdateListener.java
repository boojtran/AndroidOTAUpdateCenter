package com.mobfrog.ota.updateclient;

import android.content.Context;
import android.text.Html;

/**
 * Created by Code4LifeVn on 2/17/2015.
 */
public class OTADefaultUpdateListener implements OTAAbstractUpdateListener {
    private OTAUpdateOptions options;
    private Context context;

    public OTADefaultUpdateListener(Context context, OTAUpdateOptions options) {
        this.context = context;
        this.options = options;
    }

    @Override
    public void onReceivedUpdateInfo(OTAUpdateInfo updateInfo) {
        OTAUpdateOptions.NoticeType noticeType = options.getNoticeType();
        if(OTAUpdateOptions.NoticeType.DIALOG == noticeType) {
            OTAUpdateDialog updateDialog = new OTAUpdateDialog(this.context)
                    .setTitle("Update Notice")
                    .setMessage(Html.fromHtml(updateInfo.getRecentChanges()).toString())
                    .setUpdateLink(updateInfo.getUpdateUrl())
                    .setUpdateButtonText("Update")
                    .setSkipButtonText("Skip")
                    .setForceUpdate(true)
                    .build();
            updateDialog.show();
            return;
        }
        if(OTAUpdateOptions.NoticeType.NOTIFICATION == this.options.getNoticeType()) {
            //Make notification
            return;
        }
    }

    @Override
    public void onPrepareChecking() {

    }
}
