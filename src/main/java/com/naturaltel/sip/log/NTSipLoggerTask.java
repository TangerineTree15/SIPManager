package com.naturaltel.sip.log;

public abstract class NTSipLoggerTask {
    
    protected String threadName;
    
    public abstract String getLog();
    
    public NTSipLoggerTask() {
        this.threadName = Thread.currentThread().getName();
    }
}
