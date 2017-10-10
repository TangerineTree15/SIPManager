package com.naturaltel.sip.gw;

import javax.sip.ClientTransaction;
import javax.sip.ServerTransaction;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.naturaltel.sip.gw.entity.CallInfo;
import com.naturaltel.sip.log.CdrLoggerTask;

public interface StanzaListener {
    
//    public void handleProcessTimeout(TimeoutEvent event);
//    
//    public void handleProcessTransactionTerminated(TransactionTerminatedEvent event);
//    
//    public void handleProcessDialogTerminated(DialogTerminatedEvent event);
//    
//    public void handleProcessIOException(IOExceptionEvent event);
    
    public void doAck(ServerTransaction st, Request req, CallInfo callInfo, CdrLoggerTask cdrLoggerTask);
    
    public void doBye(ServerTransaction st, Request req, CallInfo callInfo, CdrLoggerTask cdrLoggerTask);
    
    public void doCancel(ServerTransaction st, Request req, CallInfo callInfo, CdrLoggerTask cdrLoggerTask) throws Exception ;
    
    public void doInfo(ServerTransaction st, Request req, CallInfo callInfo, CdrLoggerTask cdrLoggerTask);
    
    public void doMessage(ServerTransaction st, Request req, CallInfo callInfo, CdrLoggerTask cdrLoggerTask);
    
    public void doInvite(ServerTransaction st, Request req, CallInfo callInfo, CdrLoggerTask cdrLoggerTask) throws Exception;
    
    public void doNotify(ServerTransaction st, Request req, CallInfo callInfo, CdrLoggerTask cdrLoggerTask);
    
    public void doOptions(ServerTransaction st, Request req, CallInfo callInfo, CdrLoggerTask cdrLoggerTask);
    
    public void doPrack(ServerTransaction st, Request req, CallInfo callInfo, CdrLoggerTask cdrLoggerTask);
    
    public void doPublish(ServerTransaction st, Request req, CallInfo callInfo, CdrLoggerTask cdrLoggerTask);
    
    public void doRefer(ServerTransaction st, Request req, CallInfo callInfo, CdrLoggerTask cdrLoggerTask);

    public void doRegister(ServerTransaction st, Request req, CallInfo callInfo, CdrLoggerTask cdrLoggerTask);
    
    public void doSubscribe(ServerTransaction st, Request req, CallInfo callInfo, CdrLoggerTask cdrLoggerTask);
    
    public void doUpdate(ServerTransaction st, Request req, CallInfo callInfo, CdrLoggerTask cdrLoggerTask);

    /**
     * (1xx): Request received and being processed.
     */
    public void onProvisional(ClientTransaction ct, Response resp, CallInfo callInfo);
    
    /**
     * (2xx): The action was successfully received, understood, and accepted.
     */
    public void onSuccess(ClientTransaction ct, Response resp, CallInfo callInfo);
    
    
    /**
     * (3xx): Further action needs to be taken (typically by sender) to complete the request.
     */
    public void onRedirection(ClientTransaction ct, Response resp, CallInfo callInfo);
    
    /**
     * (4xx): The request contains bad syntax or cannot be fulfilled at the server.
     */
    public void onClientError(ClientTransaction ct, Response resp, CallInfo callInfo);
    
    /**
     * (5xx): The server failed to fulfill an apparently valid request.
     */
    public void onServerError(ClientTransaction ct, Response resp, CallInfo callInfo);
    
    
    /**
     * (6xx): The request cannot be fulfilled at any server.
     */
    public void onGlobalFailure(ClientTransaction ct, Response resp, CallInfo callInfo);
}
