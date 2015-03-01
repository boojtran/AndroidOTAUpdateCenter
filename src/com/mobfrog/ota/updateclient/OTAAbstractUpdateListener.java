package com.mobfrog.ota.updateclient;

/**
 * Created by Code4LifeVn on 2/17/2015.
 */
public interface OTAAbstractUpdateListener {
    void onReceivedUpdateInfo(OTAUpdateInfo updateInfo);
    void onPrepareChecking();
}
