package com.mobfrog.ota.updateclient;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Code4LifeVn on 2/17/2015.
 */
public class OTAUpdateOptions {

    public static enum RepoType {
        GOOGLE_PLAY("gsf"), DEFAULT("mobfrog"), AMAZON("amazon");

        private String type;

        RepoType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static enum NoticeType {
        NOTIFICATION("notification"), DIALOG("dialog");

        private String type;
        NoticeType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    private boolean forceUpdate = false;
    private boolean autoSwitchRepository = false;
    private boolean checkUpdate = false;
    private String backendService = "";

    private Context context;
    private RepoType repoType;
    private NoticeType noticeType;
    private OTAUpdatePeriod updatePeriod;
    private static final String PREFS_NAME =  "ota_update";
    private static final String PREFS_KEY_NEXT_CHECK_UPDATE_TIME = "next_time";
    private SharedPreferences preferences;

    private OTAUpdateOptions(Builder builder) {
        this.context = builder.context;
        this.updatePeriod = builder.updatePeriod;
        this.backendService = builder.backendService;
        this.forceUpdate = builder.forceUpdate;
        this.autoSwitchRepository = builder.autoSwitchRepository;
        this.checkUpdate = builder.checkUpdate;
        this.repoType = builder.repoType;
        this.noticeType = builder.noticeType;
        this.preferences = this.context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public boolean shouldCheckUpdate() {
        boolean shouldCheckUpdate = false;

        if(this.checkUpdate) {
            shouldCheckUpdate = true;
            return shouldCheckUpdate;
        }

        if(context == null) {
            shouldCheckUpdate = false;
            return shouldCheckUpdate;
        }

        long now = System.currentTimeMillis();

        long nextTime = this.preferences.getLong(PREFS_KEY_NEXT_CHECK_UPDATE_TIME, -1L);

        if(nextTime == -1L) {
            shouldCheckUpdate = true;
            return shouldCheckUpdate;
        }

        long period = 0L;
        if(this.updatePeriod != null) {
            period = this.updatePeriod.getPeriodMillis();
        }

        if(period == 0L || (now + period >= nextTime)) { // It's time check for update
            shouldCheckUpdate = true;
        } else {
            shouldCheckUpdate = false;
        }

        return shouldCheckUpdate;
    }

    public void calculateNextTime() {
        SharedPreferences.Editor editor = this.preferences.edit();

        long now = System.currentTimeMillis();
        long period = 0;

        OTAUpdatePeriod otaUpdatePeriod = this.getUpdatePeriod();
        if(otaUpdatePeriod != null) {
            period = otaUpdatePeriod.getPeriodMillis();
        }

        long nextTime = now + period;
        editor.putLong(PREFS_KEY_NEXT_CHECK_UPDATE_TIME, nextTime).commit();

    }

    public NoticeType getNoticeType() {
        return noticeType;
    }

    public RepoType getRepoType() {
        return repoType;
    }

    public OTAUpdatePeriod getUpdatePeriod() {
        return updatePeriod;
    }

    public String getBackendService() {
        return backendService;
    }

    public boolean isForceUpdate() {
        return forceUpdate;
    }

    public boolean isAutoSwitchRepository() {
        return autoSwitchRepository;
    }

    public boolean isCheckUpdate() {
        return checkUpdate;
    }

    public Context getContext() {
        return context;
    }

    public static class Builder {
        private boolean forceUpdate = false;
        private boolean autoSwitchRepository = false;
        private boolean checkUpdate = false;
        private String backendService = "";

        private Context context;
        private RepoType repoType = RepoType.GOOGLE_PLAY;
        private NoticeType noticeType = NoticeType.DIALOG;
        private OTAUpdatePeriod updatePeriod = new OTAUpdatePeriod(OTAUpdatePeriod.EACH_TIME);

        public Builder(Context context) {
            this.context = context;
        }

        public Builder useNoticeType(NoticeType noticeType) {
            this.noticeType = noticeType;
            return this;
        }

        public Builder useRepository(RepoType repoType) {
            this.repoType = repoType;
            return this;
        }

        public Builder useDefaultBackendService() {
            this.backendService = "http://api.hascpi.com/ota-center/gsf-api";
            return this;
        }

        public Builder useCustomBackendService(String endpoint) {
            this.backendService = endpoint;
            return this;
        }

        public Builder autoSwitchRepositoryIfNotAvaiable(boolean autoSwitchRepository) {
            this.autoSwitchRepository = autoSwitchRepository;
            return this;
        }

        public Builder forceUpdate(boolean forceUpdate) {
            this.forceUpdate = forceUpdate;
            return this;
        }

        public Builder setCheckUpdate(boolean checkUpdate) {
            this.checkUpdate = checkUpdate;
            return this;
        }

        public OTAUpdateOptions build() {
            return new OTAUpdateOptions(this);
        }

    }
}
