package com.mobfrog.ota.updateclient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

/**
 * Created by Code4LifeVn on 2/17/2015.
 */
public class OTAUpdateDialog implements DialogInterface.OnClickListener {

    private AlertDialog.Builder mAlertDialogBuilder;
    private AlertDialog mDialog;
    private Context mContext;
    private String mTitle = "";
    private String mMessage = "";
    private String mUpdateButtonText = "";
    private String mSkipButtonText = "";
    private String updateLink = "";
    private boolean forceUpdate = false;
    private boolean useAppIcon = true;

    public OTAUpdateDialog(Context context) {
        this.mContext = context;
        this.mAlertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(context, Build.VERSION.SDK_INT < 11 ? android.R.style.Theme_Dialog : android.R.style.Theme_Holo_Light_Dialog));
    }

    public OTAUpdateDialog setUpdateLink(String link) {
        this.updateLink = link;
        return this;
    }

    public OTAUpdateDialog setTitle(String title) {
        this.mTitle = title;
        return this;
    }

    public OTAUpdateDialog setMessage(String message) {
        this.mMessage = message;
        return this;
    }

    public OTAUpdateDialog setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
        return this;
    }

    public OTAUpdateDialog useAppIcon(boolean useAppIcon) {
        this.useAppIcon = useAppIcon;
        return this;
    }

    public OTAUpdateDialog setUpdateButtonText(String text) {
        this.mUpdateButtonText = text;
        return this;
    }

    public OTAUpdateDialog setSkipButtonText(String text) {
        this.mSkipButtonText = text;
        return this;
    }

    public OTAUpdateDialog build() {
        if(this.useAppIcon) {
            this.mAlertDialogBuilder.setIcon(this.mContext.getApplicationInfo().icon);
        }
        this.mAlertDialogBuilder.setTitle(this.mTitle);
        this.mAlertDialogBuilder.setMessage(this.mMessage);
        this.mAlertDialogBuilder.setCancelable(false);
        this.mAlertDialogBuilder.setNegativeButton(this.mUpdateButtonText, this);
        if(!this.forceUpdate) {
            this.mAlertDialogBuilder.setCancelable(true);
            this.mAlertDialogBuilder.setPositiveButton(this.mSkipButtonText, this);
        }
        this.mDialog = this.mAlertDialogBuilder.create();
        return this;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case AlertDialog.BUTTON_NEGATIVE:
                if(TextUtils.isEmpty(this.updateLink)) {
                    Toast.makeText(mContext, "Update link is empty", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }

                if(this.updateLink.endsWith(".apk")) {
                    //TODO: Download APK
                    return;
                }

                if(updateLink.startsWith("market://")) {

                    return;
                }

                if(updateLink.startsWith("http://") || updateLink.startsWith("https://")) {

                }
                break;
            case AlertDialog.BUTTON_POSITIVE:
                dialog.dismiss();
                break;
        }
    }

    public void show() {
        this.mDialog.show();
    }

    public void hide() {
        this.mDialog.dismiss();
    }

}
