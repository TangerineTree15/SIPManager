package com.naturaltel.sip.log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CdrLoggerTask extends NTSipLoggerTask {

    private static String defaultCDRDateFormat = "yyyy-MM-dd HH:mm:ss:SSS";

    private String event;
    private String callId;

    private String from;
    private String to;
    private String startDateTime;
    private String endDateTime;
    private String cellId;
    private int localeType;
    private String psap;
    private String releaseCause;
    private int responseCode;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
    
    
    
    

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getReleaseCause() {
        return releaseCause;
    }

    public void setReleaseCause(String releaseCause) {
        this.releaseCause = releaseCause;
    }

    public String getPsap() {
        return psap;
    }

    public void setPsap(String psap) {
        this.psap = psap;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
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

    public CdrLoggerTask(String from, String to, String callId) {
        this.from = from;
        this.to = to;
        this.callId = callId;
    }

    public void setStartDateTime() {
        final SimpleDateFormat spDateFormat = new SimpleDateFormat(defaultCDRDateFormat);
        startDateTime = spDateFormat.format(new Date());
    }

    public void setEndDateTime() {
        final SimpleDateFormat spDateFormat = new SimpleDateFormat(defaultCDRDateFormat);
        endDateTime = spDateFormat.format(new Date());
    }

    @Override
    public String getLog() {
        return toString();
    }

    @Override
    public String toString() {
        return "CdrLoggerTask{" + "event=" + event + ", callId=" + callId + ", from=" + from + ", to=" + to + ", startDateTime=" + startDateTime + ", endDateTime=" + endDateTime + ", cellId=" + cellId + ", localeType=" + localeType + ", psap=" + psap + ", releaseCause=" + releaseCause + ", responseCode=" + responseCode + '}';
    }




}
