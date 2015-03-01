package com.mobfrog.ota.updateclient;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Code4LifeVn on 2/17/2015.
 */
public class OTAUpdateInfo implements Parcelable {

    private String appId = "";
    private String versionName = "";
    private String fileSize = "";
    private String developer = "";
    private String appName = "";
    private String updateTime = "";
    private String recentChanges = "";
    private String updateUrl = "";
    private String dialogTitle = "";
    private String updateButtonText = "";
    private String skipButtonText = "";
    private String resultMessage = "";
    private boolean forceUpdate = false;
    private int resultCode = 0;
    private int versionCode = 0;

    public boolean isForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    public void setSkipButtonText(String skipButtonText) {
        this.skipButtonText = skipButtonText;
    }

    public String getSkipButtonText() {
        return skipButtonText;
    }

    public void setUpdateButtonText(String updateButtonText) {
        this.updateButtonText = updateButtonText;
    }

    public String getUpdateButtonText() {
        return updateButtonText;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getRecentChanges() {
        return recentChanges;
    }

    public void setRecentChanges(String recentChanges) {
        this.recentChanges = recentChanges;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.appId);
        dest.writeString(this.versionName);
        dest.writeString(this.fileSize);
        dest.writeString(this.developer);
        dest.writeString(this.appName);
        dest.writeString(this.updateTime);
        dest.writeString(this.recentChanges);
        dest.writeString(this.updateUrl);
        dest.writeString(this.dialogTitle);
        dest.writeString(this.updateButtonText);
        dest.writeString(this.skipButtonText);
        dest.writeString(this.resultMessage);
        dest.writeByte(forceUpdate ? (byte) 1 : (byte) 0);
        dest.writeInt(this.resultCode);
        dest.writeInt(this.versionCode);
    }

    public OTAUpdateInfo() {
    }

    private OTAUpdateInfo(Parcel in) {
        this.appId = in.readString();
        this.versionName = in.readString();
        this.fileSize = in.readString();
        this.developer = in.readString();
        this.appName = in.readString();
        this.updateTime = in.readString();
        this.recentChanges = in.readString();
        this.updateUrl = in.readString();
        this.dialogTitle = in.readString();
        this.updateButtonText = in.readString();
        this.skipButtonText = in.readString();
        this.resultMessage = in.readString();
        this.forceUpdate = in.readByte() != 0;
        this.resultCode = in.readInt();
        this.versionCode = in.readInt();
    }

    public static final Creator<OTAUpdateInfo> CREATOR = new Creator<OTAUpdateInfo>() {
        public OTAUpdateInfo createFromParcel(Parcel source) {
            return new OTAUpdateInfo(source);
        }

        public OTAUpdateInfo[] newArray(int size) {
            return new OTAUpdateInfo[size];
        }
    };
}
