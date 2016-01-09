package com.my.app.appgodo;

import android.app.Application;

import org.apache.http.client.HttpClient;


import java.net.CookieManager;

/**
 * Created by Mark on 2015-12-21.
 */
public class myApplication extends Application {
    private String regGCMId; /*GCM ID*/
    private String regAppId; /*GCM ID*/
    private HttpClient httpclient;  //멤버변수로 선언
    private CookieManager cookieManager;
    private String orderData =""; /*주문정보 데이터*/
    private String logData = ""; /*통계 데이터*/

    public String getOrderData() {
        return orderData;
    }

    public void setOrderData(String orderData) {
        this.orderData = orderData;
    }

    public String getLogData() {
        return logData;
    }

    public void setLogData(String logData) {
        this.logData = logData;
    }

    public HttpClient getHttpclient() {
        return httpclient;
    }

    public void setHttpclient(HttpClient httpclient) {
        this.httpclient = httpclient;
    }

    public CookieManager getCookieManager() {
        return cookieManager;
    }

    public void setCookieManager(CookieManager cookieManager) {
        this.cookieManager = cookieManager;
    }

    public String getRegGCMId() {
        return regGCMId;
    }

    public void setRegGCMId(String regGCMId) {
        this.regGCMId = regGCMId;
    }

    public String getRegAppId() {
        return regAppId;
    }

    public void setRegAppId(String regAppId) {
        this.regAppId = regAppId;
    }
}
