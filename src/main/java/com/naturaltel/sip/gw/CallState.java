package com.naturaltel.sip.gw;

public enum CallState {

    NONE {
                @Override
                protected CallState _next(CallFlow callFlow) throws CallFlowException {
                    if (callFlow.compareTo(CallFlow.INVITE) == 0) {
                        CallState callState = PROCEEDING;
                        callState.setCallFlowStateValue(callFlow);
                        return callState;

                    } else if (callFlow.compareTo(CallFlow.CANCEL) == 0 || callFlow.compareTo(CallFlow.ACK) == 0) {
                        CallState callState = TERMINATED;
                        callState.setCallFlowStateValue(callFlow);
                        return callState;
                    } else if (callFlow.compareTo(CallFlow.ERROR) == 0) {
                        CallState callState = TERMINATED;
                        callState.setCallFlowStateValue(callFlow);
                        return callState;
                    } else {
                        throw new CallFlowException(String.format("Call state: none, callflow:%s", callFlow.name()));
                    }

                }
            },
    PROCEEDING {
                @Override
                protected CallState _next(CallFlow callFlow) throws CallFlowException {
                    if (callFlow.compareTo(CallFlow.OK) == 0) {
                        CallState callState = TERMINATING;
                        callState.setCallFlowStateValue(callFlow);
                        return callState;
                    } else if (callFlow.compareTo(CallFlow.CANCEL) == 0) {
                        CallState callState = TERMINATING;
                        callState.setCallFlowStateValue(callFlow);
                        return callState;
                    } else if (callFlow.compareTo(CallFlow.ERROR) == 0) {
                        CallState callState = TERMINATING;
                        callState.setCallFlowStateValue(callFlow);
                        return callState;
                    } else {
                        throw new CallFlowException(String.format("Call state: proceeding, callflow:%s", callFlow.name()));
                    }

                }
            },
    TERMINATING {
                @Override
                protected CallState _next(CallFlow callFlow) throws CallFlowException {
                    if ( callFlow.compareTo(CallFlow.CANCEL) == 0) {
                        CallState callState = TERMINATING;
                        callState.setCallFlowStateValue(callFlow);
                        return callState;
                    } else if (callFlow.compareTo(CallFlow.OK) == 0 || callFlow.compareTo(CallFlow.ACK) == 0 || callFlow.compareTo(CallFlow.ERROR) == 0) {
                        CallState callState = TERMINATED;
                        callState.setCallFlowStateValue(callFlow);
                        return callState;
                    } else {
                        throw new CallFlowException(String.format("Call state: proceeding, callflow:%s", callFlow.name()));

                    }

                }
            },
    TERMINATED {
                @Override
                protected CallState _next(CallFlow callFlow) throws CallFlowException {
                    if (callFlow.compareTo(CallFlow.ERROR) == 0) {
                        CallState callState = TERMINATED;
                        callState.setCallFlowStateValue(callFlow);
                        return callState;
                    } else {
                        throw new CallFlowException(String.format("Call state: terminated, callflow:%s", callFlow.name()));
                    }
                }
            };

    private int code;

    public void setErrorCode(int code) {
        this.code = code;
    }

    public int getErrorCode() {
        return this.code;
    }

    private CallFlow flow;

    public void setCallFlowStateValue(CallFlow callFlow) {
        this.flow = callFlow;
    }

    public CallFlow getCallFlowStateValue() {
        return this.flow;
    }

    public CallState next(CallFlow callflow) throws CallFlowException {
        return _next(callflow);
    }

    protected abstract CallState _next(CallFlow callflow) throws CallFlowException;
}
