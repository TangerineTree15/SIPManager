package com.naturaltel.sip.gw;

public class CallFlowException extends Exception {


    /**
     * 
     */
    private static final long serialVersionUID = -5198930341376768768L;

    public CallFlowException() {
        super();
    }

    public CallFlowException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

    public CallFlowException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public CallFlowException(String arg0) {
        super(arg0);
    }

    public CallFlowException(Throwable arg0) {
        super(arg0);
    }

}
