package com.mobfrog.ota.updateclient;

import android.content.Context;
import android.os.AsyncTask;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Code4LifeVn on 2/17/2015.
 */
public class OTACheckUpdateAsyncTask extends AsyncTask<OTAUpdateOptions, Void, OTAUpdateInfo> {
    private OTAUpdateOptions options;
    private DefaultHttpClient httpClient = null;
    private Context mContext;
    private String token;
    private OTAAbstractUpdateListener listener;

    public OTACheckUpdateAsyncTask(Context mContext, String token, OTAAbstractUpdateListener listener) {
        this.mContext = mContext;
        this.listener = listener;
        this.token = token;

        BasicHttpParams httpParams = new BasicHttpParams();
        ConnManagerParams.setTimeout(httpParams, 30 * 1000);
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(10));
        ConnManagerParams.setMaxTotalConnections(httpParams, 10);

        HttpConnectionParams.setSoTimeout(httpParams, 30 * 1000);
        HttpConnectionParams.setConnectionTimeout(httpParams, 30 * 1000);
        HttpConnectionParams.setTcpNoDelay(httpParams, true);
        HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
        HttpClientParams.setRedirecting(httpParams, true);
        HttpProtocolParams.setUserAgent(httpParams, System.getProperty("http.agent"));

        this.httpClient = new DefaultHttpClient(httpParams);
        this.httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(5, true));
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(OTAUpdateInfo updateInfo) {
        if (updateInfo == null) {
            return;
        }
        this.options.calculateNextTime();
        if (this.listener != null) {
            this.listener.onReceivedUpdateInfo(updateInfo);
        }
    }

    @Override
    protected OTAUpdateInfo doInBackground(OTAUpdateOptions... params) {
        if (params == null || params.length == 0 || params[0] == null) {
            return null;
        }
        this.options = params[0];
        int versionCode = 0;
        try {
            versionCode = this.mContext.getPackageManager().getPackageInfo(this.mContext.getPackageName(), 0).versionCode;
        } catch (Exception exception) {
        }
        List<BasicNameValuePair> paramList = new LinkedList<BasicNameValuePair>();
        paramList.add(new BasicNameValuePair("auto_switch", String.valueOf(this.options.isAutoSwitchRepository())));
        paramList.add(new BasicNameValuePair("package_id", this.mContext.getPackageName()));
        paramList.add(new BasicNameValuePair("ctype", this.options.getRepoType().getType()));
        paramList.add(new BasicNameValuePair("version_code", String.valueOf(versionCode)));
        paramList.add(new BasicNameValuePair("lang", Locale.getDefault().getLanguage() + "-" + Locale.getDefault().getCountry()));
        paramList.add(new BasicNameValuePair("ctoken", this.token));

        String query = URLEncodedUtils.format(paramList, "UTF-8");
        String url = this.options.getBackendService();
        if (!url.endsWith("?")) {
            url = url.concat("?");
        }
        url = url.concat(query);
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse httpResponse = this.httpClient.execute(httpGet);
            String data = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.getInt("result_code") != 200) {
                return null;
            }
            return this.jsonToUpdateInfo(jsonObject);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    protected OTAUpdateInfo jsonToUpdateInfo(JSONObject jsonObject) {

        OTAUpdateInfo updateInfo = new OTAUpdateInfo();
        try {
            updateInfo.setResultCode(jsonObject.getInt("result_code"));
            updateInfo.setResultMessage(jsonObject.getString("result_message"));
            updateInfo.setAppName(jsonObject.getString("title"));
            updateInfo.setRecentChanges(jsonObject.getString("recent_changes"));
            updateInfo.setAppId(jsonObject.getString("app_id"));
            updateInfo.setVersionCode(jsonObject.getInt("version_code"));
            updateInfo.setVersionName(jsonObject.getString("version_name"));
            if (jsonObject.has("developer")) {
                updateInfo.setDeveloper(jsonObject.getString("developer"));
            }
            if (jsonObject.has("file_size")) {
                updateInfo.setFileSize(jsonObject.getString("file_size"));
            }
            if (jsonObject.has("update_time")) {
                updateInfo.setUpdateTime(jsonObject.getString("update_time"));
            }
            if (jsonObject.has("update_url")) {
                updateInfo.setUpdateUrl(jsonObject.getString("update_url"));
            }
            if (jsonObject.has("dialog_title")) {
                updateInfo.setDialogTitle(jsonObject.getString("dialog_title"));
            } else {
                updateInfo.setDialogTitle("vi".equalsIgnoreCase(Locale.getDefault().getDisplayLanguage()) ? "Thông báo cập nhật" : "Update Notice");
            }
            if(jsonObject.has("dialog_update_button_text")) {
                updateInfo.setUpdateButtonText(jsonObject.getString("dialog_update_button_text"));
            } else {
                updateInfo.setUpdateButtonText("vi".equalsIgnoreCase(Locale.getDefault().getDisplayLanguage()) ? "Cập nhật" : "Update");
            }
            if(jsonObject.has("dialog_skip_button_text")) {
                updateInfo.setSkipButtonText(jsonObject.getString("dialog_skip_button_text"));
            } else {
                updateInfo.setSkipButtonText("vi".equalsIgnoreCase(Locale.getDefault().getDisplayLanguage()) ? "Để sau" : "Skip");
            }
            if(jsonObject.has("dialog_force_update")) {
                updateInfo.setForceUpdate(jsonObject.getBoolean("dialog_force_update"));
            }
        } catch (Exception exception) {

        }
        return updateInfo;
    }

}
