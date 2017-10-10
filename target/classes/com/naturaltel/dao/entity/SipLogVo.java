package com.naturaltel.dao.entity;

import java.sql.Timestamp;

public class SipLogVo {
    
    public static final int LOCALE_TYPE_DOMESTIC = 1;
    public static final int LOCALE_TYPE_ABROAD = 2;
    public static final int LOCALE_TYPE_UNKNOWN = 3;

    private Timestamp logTime;
    
    private String sourceDomain;
    
    private String localHostName;
    
    private String eventName;
    
    private String eventTime;
    
    private String partyA;
    
    private String partyB;
    
    private String cellId;
    
    private int localeType;
    
    private String partyPsap;
    
    private String releaseCause;
    
    private int responseCode;
    
    private String pHeader;
    
    private String pHeaderAttr;
    
    private String callId;

        private String pHeaderAccess;

        
        
    
    
    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }
    
    
    

    public Timestamp getLogTime() {
        return logTime;
    }

    public void setLogTime(Timestamp logTime) {
        this.logTime = logTime;
    }

    public String getSourceDomain() {
        return sourceDomain;
    }

    public void setSourceDomain(String sourceDomain) {
        this.sourceDomain = sourceDomain;
    }

    public String getLocalHostName() {
        return localHostName;
    }

    public void setLocalHostName(String localHostName) {
        this.localHostName = localHostName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getPartyA() {
        return partyA;
    }

    public void setPartyA(String partyA) {
        this.partyA = partyA;
    }

    public String getPartyB() {
        return partyB;
    }

    public void setPartyB(String partyB) {
        this.partyB = partyB;
    }

    public String getCellId() {
        return cellId;
    }

    public void setCellId(String cellId) {
        this.cellId = cellId;
    }

    public int getLocaleType() {
        return localeType;
    }

    public void setLocaleType(int localeType) {
        this.localeType = localeType;
    }

    public String getPartyPsap() {
        return partyPsap;
    }

    public void setPartyPsap(String partyPsap) {
        this.partyPsap = partyPsap;
    }

    public String getReleaseCause() {
        return releaseCause;
    }

    public void setReleaseCause(String releaseCause) {
        this.releaseCause = releaseCause;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getPHeader() {
        return pHeader;
    }

    public void setPHeader(String pHeader) {
        this.pHeader = pHeader;
    }

    public String getPHeaderAttr() {
        return pHeaderAttr;
    }

    public void setPHeaderAttr(String pHeaderAttr) {
        this.pHeaderAttr = pHeaderAttr;
    }

    public String getpHeaderAccess() {
        return pHeaderAccess;
    }

    public void setpHeaderAccess(String pHeaderAccess) {
        this.pHeaderAccess = pHeaderAccess;
    }

    @Override
    public String toString() {
        return "SipLogVo{" + "logTime=" + logTime + ", sourceDomain=" + sourceDomain + ", localHostName=" + localHostName + ", eventName=" + eventName + ", eventTime=" + eventTime + ", partyA=" + partyA + ", partyB=" + partyB + ", cellId=" + cellId + ", localeType=" + localeType + ", partyPsap=" + partyPsap + ", releaseCause=" + releaseCause + ", responseCode=" + responseCode + ", pHeader=" + pHeader + ", pHeaderAttr=" + pHeaderAttr + ", callId=" + callId + ", pHeaderAccess=" + pHeaderAccess + '}';
    }

    
    


    
}
