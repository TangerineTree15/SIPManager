package com.naturaltel.sip.gw;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.sip.ClientTransaction;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.ListeningPoint;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.SipListener;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionAlreadyExistsException;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.TransactionUnavailableException;
import javax.sip.address.AddressFactory;
import javax.sip.address.URI;
import javax.sip.header.CallIdHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ReasonHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Message;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

import org.apache.log4j.Logger;

import com.naturaltel.cache.Config;
import com.naturaltel.config.InternalException;
import com.naturaltel.config.entity.CellId;
import com.naturaltel.config.entity.CellNetworkInfo;
import com.naturaltel.config.entity.CityCellInfo;
import com.naturaltel.config.entity.PsapInfo;
import com.naturaltel.dao.entity.SipLogVo;
import com.naturaltel.sip.gw.entity.CallInfo;
import com.naturaltel.sip.gw.entity.CellInfo;
import com.naturaltel.sip.log.CdrLoggerTask;

import gov.nist.javax.sip.address.SipUri;
import gov.nist.javax.sip.address.TelURLImpl;
import gov.nist.javax.sip.header.ExtensionHeaderImpl;
import gov.nist.javax.sip.header.ims.PAccessNetworkInfo;
import gov.nist.javax.sip.header.ims.PAssertedIdentityHeader;

public class SipManager implements SipListener {

    private static SipManager instance;

    private SipFactory sipFactory;
    private static MessageFactory messageFactory;
    private static HeaderFactory headerFactory;
    private static AddressFactory addressFactory;
    private ListeningPoint listeningPoint;
    private SipStack sipStack;
    private SipProvider sipProvider;

    private Map<String, CallState> callStateMap;
    private Map<String, CallInfo> callInfoMap;
    private Map<String, ListenerAndFilterPair> listeners;

    private CdrLoggerTask mCdrLogTask;

    private Logger logger = Logger.getLogger("debug");
    private Logger cdrLogger = Logger.getLogger("cdr");

    private class ListenerAndFilterPair {

        private StanzaListener listener;
        private StanzaFilter filter;

        public StanzaListener getListener() {
            return listener;
        }

        public StanzaFilter getFilter() {
            return filter;
        }

        public void setListener(StanzaListener listener) {
            this.listener = listener;
        }

        public void setFilter(StanzaFilter filter) {
            this.filter = filter;
        }
    }

    private SipManager() throws Exception {
        sipFactory = SipFactory.getInstance();
//        sipFactory.setPathName(Config.getInstance().sip_path_name);
        messageFactory = sipFactory.createMessageFactory();
        headerFactory = sipFactory.createHeaderFactory();
        addressFactory = sipFactory.createAddressFactory();
        sipStack = sipFactory.createSipStack(Config.getSipStack());

        InetAddress address = InetAddress.getByName(Config.getSipInfo().getAddress());
        listeningPoint = sipStack.createListeningPoint(address.getHostAddress(), Config.getSipInfo().getPort(), Config.getSipInfo().getTransport());
        sipProvider = sipStack.createSipProvider(listeningPoint);
        sipProvider.setAutomaticDialogSupportEnabled(false);
        sipProvider.addSipListener(this);

        // The call id pair with call state represent the life cycle of the call
        callStateMap = new ConcurrentHashMap<String, CallState>();

        callInfoMap = new ConcurrentHashMap<String, CallInfo>();

        listeners = new ConcurrentHashMap<String, ListenerAndFilterPair>();
    }

    public static final SipManager getInstance() throws Exception {
        if (instance == null) {
            instance = new SipManager();
        }
        return instance;
    }

    public void addListener(StanzaListener listener, StanzaFilter filter) {
        String key = listener.getClass().getName();
        if (!listeners.containsKey(key)) {
            ListenerAndFilterPair pair = new ListenerAndFilterPair();
            pair.setListener(listener);
            pair.setFilter(filter);
            listeners.put(key, pair);
        }
    }

    public void addGenericListener(StanzaListener listener) {
        addListener(listener, new StanzaFilter() {

            public boolean accept(Message message) {
                return true;
            }

        });
    }

    public final void start() throws SipException {
        if (sipStack != null) {
            sipStack.start();
        }
    }

    public final void stop() {
        sipStack.stop();
    }

    public boolean containsCall(String callId) {
        return callStateMap.containsKey(callId);
    }

    public CallState getCallState(String callId) {
        if (containsCall(callId)) {
            return callStateMap.get(callId);
        }
        return null;
    }

    public void changeCallState(String callId, CallFlow callFlow) throws CallFlowException {
        if (containsCall(callId)) {
            CallState callState = callStateMap.get(callId);
            callState = callState.next(callFlow);
            callStateMap.put(callId, callState);
        }
    }

    public void addCall(String callId) {
        if (!containsCall(callId)) {
            callStateMap.put(callId, CallState.NONE);
        }
    }

    public void removeCall(String callId) {
        if (containsCall(callId)) {
            callStateMap.remove(callId);
        }
        if (callInfoMap.containsKey(callId)) {
            callInfoMap.remove(callId);
        }
    }

    public CallInfo getCallInfo(String callId) {
        if (callInfoMap.containsKey(callId)) {
            return callInfoMap.get(callId);
        }
        return null;
    }

    public void setCallInfo(CallInfo callInfo) {
        String callId = callInfo.getCallId();
        if (!callInfoMap.containsKey(callId)) {
            callInfoMap.put(callId, callInfo);
        }
    }

    public final String getNewCallId() {
        return ((CallIdHeader) sipProvider.getNewCallId()).getCallId();
    }

    public CallIdHeader getNewCallIdHeader() {
        return (CallIdHeader) sipProvider.getNewCallId();
    }

    /**
     * For client to send stateful request.
     *
     * @param request
     * @return
     * @throws TransactionAlreadyExistsException
     * @throws TransactionUnavailableException
     */
    public final ClientTransaction getNewClientTransaction(Request request) throws TransactionAlreadyExistsException, TransactionUnavailableException {
        return sipProvider.getNewClientTransaction(request);
    }

    /**
     * For server to send stateful response to the request.
     *
     * @param request
     * @return
     * @throws TransactionAlreadyExistsException
     * @throws TransactionUnavailableException
     */
    public final ServerTransaction getNewServerTransaction(Request request) throws TransactionAlreadyExistsException, TransactionUnavailableException {
        return sipProvider.getNewServerTransaction(request);
    }

    /**
     * Send response stateless.
     *
     * @param response
     * @throws SipException
     */
    public final void sendResponseToClient(Response response) throws SipException {
        sipProvider.sendResponse(response);
    }

    /**
     * Send request stateless.
     *
     * @param request
     * @throws SipException
     */
    public final void sendRequestToServer(Request request) throws SipException {
        sipProvider.sendRequest(request);
    }

    public void processDialogTerminated(DialogTerminatedEvent event) {
        logger.info(event.getSource());

    }

    public void processIOException(IOExceptionEvent event) {
        logger.info(event.getSource());

    }

    public void processRequest(RequestEvent event) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                final Request req = event.getRequest();
                ServerTransaction st = event.getServerTransaction();
                final String callId = ((CallIdHeader) req.getHeader(CallIdHeader.NAME)).getCallId();
                final String logHeader = "[callId=" + callId + "]";
                Date eventTime = new Date();
                CallInfo callInfo = null;
                try {
                    logger.info(logHeader + "New coming:" + req.toString());

//            acquireSem();
                    if (containsCall(callId)) {
                        callInfo = getCallInfo(callId);
                    } else {
                        addCall(callId);
                        callInfo = parseCallInfo(logHeader, req);
                        setCallInfo(callInfo);
                    }
                    callInfo.setEventTime(eventTime);
                    CallState callState = getCallState(callId);
                    String mAMsisdn = callInfo.getCaller();
                    String mBMsisdn = callInfo.getCallee();
//                    if (CallState.NONE.equals(callState)) {
                    mCdrLogTask = new CdrLoggerTask(mAMsisdn, mBMsisdn, callId);
                    mCdrLogTask.setStartDateTime();
//                    }
                    for (String key : listeners.keySet()) {
                        ListenerAndFilterPair pair = listeners.get(key);
                        StanzaListener listener = pair.getListener();
                        StanzaFilter filter = pair.getFilter();
                        if (filter != null && filter.accept(req)) {
                            final String method = req.getMethod();
                            mCdrLogTask.setEvent(method);
                            logger.debug("SIP Event: " + method);
                            if (Request.ACK.equals(method)) {
                                listener.doAck(st, req, callInfo, mCdrLogTask);
                            } else if (Request.BYE.equals(method)) {
                                listener.doBye(st, req, callInfo, mCdrLogTask);
                            } else if (Request.CANCEL.equals(method)) {
                                listener.doCancel(st, req, callInfo, mCdrLogTask);
                            } else if (Request.INFO.equals(method)) {
                                listener.doInfo(st, req, callInfo, mCdrLogTask);
                            } else if (Request.MESSAGE.equals(method)) {
                                listener.doMessage(st, req, callInfo, mCdrLogTask);
                            } else if (Request.INVITE.equals(method)) {
                                listener.doInvite(st, req, callInfo, mCdrLogTask);
                            } else if (Request.NOTIFY.equals(method)) {
                                listener.doNotify(st, req, callInfo, mCdrLogTask);
                            } else if (Request.OPTIONS.equals(method)) {
                                listener.doOptions(st, req, callInfo, mCdrLogTask);
                            } else if (Request.PRACK.equals(method)) {
                                listener.doPrack(st, req, callInfo, mCdrLogTask);
                            } else if (Request.PUBLISH.equals(method)) {
                                listener.doPublish(st, req, callInfo, mCdrLogTask);
                            } else if (Request.REFER.equals(method)) {
                                listener.doRefer(st, req, callInfo, mCdrLogTask);
                            } else if (Request.REGISTER.equals(method)) {
                                listener.doRegister(st, req, callInfo, mCdrLogTask);
                            } else if (Request.SUBSCRIBE.equals(method)) {
                                listener.doSubscribe(st, req, callInfo, mCdrLogTask);
                            } else if (Request.UPDATE.equals(method)) {
                                listener.doUpdate(st, req, callInfo, mCdrLogTask);
                            }
                        }
                    }
                } catch (Exception e) {
                    try {

                        Response errorResponse = messageFactory.createResponse(Response.SERVICE_UNAVAILABLE, req);
                        if (!Request.ACK.equals(req.getMethod())) {
                            ReasonHeader reasonHeader = headerFactory.createReasonHeader("SIP", Response.SERVICE_UNAVAILABLE, "service unavailable");
                            errorResponse.setHeader(reasonHeader);
                            sendResponseToClient(errorResponse);
                        }

                        logger.error("", e);
                    } catch (Exception e1) {
                        logger.error("", e);
                    }
                    logger.error(req.toString(), e);
                } finally {
                }

            }
        }).start();;

    }

    public void processResponse(ResponseEvent event) {
        Response resp = event.getResponse();
        ClientTransaction ct = event.getClientTransaction();
        for (String key : listeners.keySet()) {
            try {
                ListenerAndFilterPair pair = listeners.get(key);
                StanzaListener listener = pair.getListener();
                StanzaFilter filter = pair.getFilter();
                if (filter != null && filter.accept(resp)) {
                    final int statusCode = resp.getStatusCode();
                    final String callId = ((CallIdHeader) resp.getHeader(CallIdHeader.NAME)).getCallId();
                    if (containsCall(callId)) {
                        CallInfo callInfo = getCallInfo(callId);
                        if (statusCode < 200) {
                            listener.onProvisional(ct, resp, callInfo);
                        } else if (statusCode < 300) {
                            listener.onSuccess(ct, resp, callInfo);
                        } else if (statusCode < 400) {
                            listener.onRedirection(ct, resp, callInfo);
                        } else if (statusCode < 500) {
                            listener.onClientError(ct, resp, callInfo);
                        } else if (statusCode < 600) {
                            listener.onServerError(ct, resp, callInfo);
                        } else if (statusCode < 700) {
                            listener.onGlobalFailure(ct, resp, callInfo);
                        }
                    }
                }
            } catch (Exception e) {
                logger.error(event.getResponse().toString(), e);
            }

        }

    }

    private CallInfo parseCallInfo(String logHeader, Request aRequest) throws Exception {
        CallInfo result = new CallInfo();
        String mAMsisdn = null;
        String mBMsisdn = null;
        String mCallId = null;
        String source = null;

        try {
            //Get CallID.
            mCallId = ((CallIdHeader) aRequest.getHeader(CallIdHeader.NAME)).getCallId();

            //Get B party msisdn.
            URI bUri = aRequest.getRequestURI();

            //TODO RequestURI format is urn:service:sos.112, we need the parse '112'(words after sos.)
            if (isSosUri(bUri)) {
                logger.debug(logHeader + "bUri=" + bUri + " is isSosUri ");

                mBMsisdn = getSosTel(bUri);
            } else {
                if (bUri.isSipURI()) {
                    logger.debug(logHeader + "bUri=" + bUri + " is isSipURI ");

                    mBMsisdn = ((SipUri) bUri).getUser();

                } else {
                    logger.debug(logHeader + "bUri=" + bUri + " is isTelURI ");

                    mBMsisdn = ((TelURLImpl) bUri).getPhoneNumber();
                }
            }

            //Get A party msisdn.
            PAssertedIdentityHeader pAssertedIdentityHeader = ((PAssertedIdentityHeader) aRequest.getHeader(PAssertedIdentityHeader.NAME));
            URI aUri = null;
            if (pAssertedIdentityHeader == null) {
                aUri = ((FromHeader) aRequest.getHeader(FromHeader.NAME)).getAddress().getURI();
            } else {
                aUri = pAssertedIdentityHeader.getAddress().getURI();
            }
            if (aUri.isSipURI()) {
                mAMsisdn = ((SipUri) aUri).getUser();
            } else {
                mAMsisdn = ((TelURLImpl) aUri).getPhoneNumber();
            }

            ListIterator listIter = aRequest.getHeaders(ViaHeader.NAME);
            while (listIter.hasNext()) {
                ViaHeader viaHeader = (ViaHeader) listIter.next();
                if ("udp".equalsIgnoreCase(viaHeader.getTransport())) {
                    source = viaHeader.getHost() + ":" + viaHeader.getPort();
                }
                logger.debug(logHeader + "Source(ViaHeader): " + source);
            }
            logger.debug(logHeader + "A party:" + mAMsisdn + ", B party:" + mBMsisdn + ",source:" + source);

        } catch (Exception e) {
            logger.error("", e);
//            throw e;
        } finally {
            result.setCallId(mCallId);
            result.setCaller(mAMsisdn);
            result.setCallee(mBMsisdn);
            result.setSource(source);
        }

        return result;
    }

    public CellInfo parseCellInfo(String logHeader, Request req) throws InternalException {
        CellNetworkInfo cellNetworkInfo = Config.getCellNetworkInfo();
        CellInfo cellInfo = new CellInfo();
        String headerName = null;
        String cellIdAttrName = null;
        String accessTypeName = null;

        String cellId = null;
        try {
            /*
             headerName = cellNetworkInfo.getHeaderName1();
             PNetworkInfo pNetworkHeader = (PNetworkInfo) req.getHeader(headerName);
             PAccessNetworkInfo pAccessNetworkInfo = null;
             if (pNetworkHeader == null
             || pNetworkHeader.getHeaderValue() == null
             || "".equals(pNetworkHeader.getHeaderValue().trim())) {
             headerName = cellNetworkInfo.getHeaderName2();
             pNetworkHeader = (PNetworkInfo) req.getHeader(headerName);
             }
             if (pNetworkHeader == null
             || pNetworkHeader.getHeaderValue() == null
             || "".equals(pNetworkHeader.getHeaderValue().trim())) {
             headerName = cellNetworkInfo.getHeaderName3();
             pNetworkHeader = (PNetworkInfo) req.getHeader(headerName);
             }
             if (pNetworkHeader == null
             || pNetworkHeader.getHeaderValue() == null
             || "".equals(pNetworkHeader.getHeaderValue().trim())) {
             headerName = cellNetworkInfo.getHeaderName4();
             pNetworkHeader = (PNetworkInfo) req.getHeader(headerName);
             }
             */

            //TODO
            headerName = cellNetworkInfo.getHeaderName1();
            ExtensionHeaderImpl extensionHeaderImpl = (ExtensionHeaderImpl) req.getHeader(headerName);
            PAccessNetworkInfo pAccessNetworkInfo = null;
            if (extensionHeaderImpl == null
                    || extensionHeaderImpl.getHeaderValue() == null
                    || "".equals(extensionHeaderImpl.getHeaderValue().trim())) {
                headerName = cellNetworkInfo.getHeaderName2();
                extensionHeaderImpl = (ExtensionHeaderImpl) req.getHeader(headerName);
            }
            if (extensionHeaderImpl == null
                    || extensionHeaderImpl.getHeaderValue() == null
                    || "".equals(extensionHeaderImpl.getHeaderValue().trim())) {
                headerName = cellNetworkInfo.getHeaderName3();
                extensionHeaderImpl = (ExtensionHeaderImpl) req.getHeader(headerName);
            }
            if (extensionHeaderImpl == null
                    || extensionHeaderImpl.getHeaderValue() == null
                    || "".equals(extensionHeaderImpl.getHeaderValue().trim())) {
                headerName = cellNetworkInfo.getHeaderName4();
                pAccessNetworkInfo = (PAccessNetworkInfo) req.getHeader(headerName);
            }

            String rawData = null;
            if (extensionHeaderImpl != null) {
                rawData = extensionHeaderImpl.getHeaderValue();
            } else if (pAccessNetworkInfo != null) {
                rawData = pAccessNetworkInfo.getHeaderValue();
            }

            /*
             if (pNetworkHeader.getAccessType() != null) {
             accessTypeName = pNetworkHeader.getAccessType();
             }

             if (pNetworkHeader.getUtranCellID3GPP() != null) {
             cellIdAttrName = cellNetworkInfo.getCellIdParamKeyUtran();
             cellId = pNetworkHeader.getUtranCellID3GPP();
             } else if (pNetworkHeader.getCGI3GPP() != null) {
             cellIdAttrName = cellNetworkInfo.getCellIdParamKey2G();
             cellId = pNetworkHeader.getCGI3GPP();
             }
             */
            if (rawData != null) {
                String[] rawDataArray = rawData.split(";");
                for (int i = 0; i < rawDataArray.length; i++) {
                    String param = rawDataArray[i];

                    //to find matched accessType
                    if (param.contains(cellNetworkInfo.getAccessType4G())) {
                        accessTypeName = cellNetworkInfo.getAccessType4G();

                    } else if (param.contains(cellNetworkInfo.getAccessType3G())) {
                        accessTypeName = cellNetworkInfo.getAccessType3G();

                    } else if (param.contains(cellNetworkInfo.getAccessType2G())) {
                        accessTypeName = cellNetworkInfo.getAccessType2G();

                    }

                    //to find matched cellAttribute
                    if (param.contains(cellNetworkInfo.getCellIdParamKeyUtran())) {
                        cellIdAttrName = cellNetworkInfo.getCellIdParamKeyUtran();
                        cellId = param.substring(param.indexOf("=") + 1);
                    } else if (param.contains(cellNetworkInfo.getCellIdParamKey2G())) {
                        cellIdAttrName = cellNetworkInfo.getCellIdParamKey2G();
                        cellId = param.substring(param.indexOf("=") + 1);
                    }

                    /*
                     if (param.contains(cellNetworkInfo.getCellIdParamKey4G())) {
                     cellIdAttrName = cellNetworkInfo.getCellIdParamKey4G();
                     cellId = param.substring(param.indexOf("=") + 1);
                     } else if (param.contains(cellNetworkInfo.getCellIdParamKey3G())) {
                     cellIdAttrName = cellNetworkInfo.getCellIdParamKey3G();
                     cellId = param.substring(param.indexOf("=") + 1);
                     }
                     */
                }
            }

        } catch (Exception e) {
            logger.error("", e);
            throw new InternalException(e.getMessage());
        } finally {
            cellInfo.setHeaderName(headerName);
            cellInfo.setCellIdAttrName(cellIdAttrName);
            cellInfo.setCellId(cellId);
            cellInfo.setAccessTypeName(accessTypeName);
        }
        return cellInfo;
    }

    public void processTimeout(TimeoutEvent event) {
        if (event.isServerTransaction()) {
            try {
                ServerTransaction st = event.getServerTransaction();
                CallIdHeader callIdHeader = (CallIdHeader) st.getRequest().getHeader(CallIdHeader.NAME);
                String callId = callIdHeader.getCallId();
                if (containsCall(callId)) {
                    CallInfo callInfo = getCallInfo(callId);
                    changeCallState(callId, CallFlow.ERROR);
                    mCdrLogTask = new CdrLoggerTask(callInfo.getCaller(), callInfo.getCallee(), callId);
                    mCdrLogTask.setEndDateTime();
                    terminate(callId, mCdrLogTask);
                }
            } catch (Exception e) {
                logger.error(event.getServerTransaction().getRequest().toString(), e);
            }
        } else {
            try {
                ClientTransaction ct = event.getClientTransaction();
                CallIdHeader callIdHeader = (CallIdHeader) ct.getRequest().getHeader(CallIdHeader.NAME);
                String callId = callIdHeader.getCallId();
                if (containsCall(callId)) {
                    CallInfo callInfo = getCallInfo(callId);
                    changeCallState(callId, CallFlow.ERROR);
                    mCdrLogTask = new CdrLoggerTask(callInfo.getCaller(), callInfo.getCallee(), callId);
                    mCdrLogTask.setEndDateTime();
                    terminate(callId, mCdrLogTask);
                }
            } catch (Exception e) {
                logger.error(event.getClientTransaction().getRequest().toString(), e);
            }
        }

    }

    public void processTransactionTerminated(TransactionTerminatedEvent event) {
        logger.info(event.getSource());

    }

    public CellId getCellId(CallInfo callInfo, CellInfo cellInfo) {
        CellId result = null;

        try {

//            logger.info(new StringBuffer().append(callInfo).append("the utran_cellid_3gpp is ").append(cellInfo.getCellId()).toString());
            PsapInfo psapInfo = Config.getPsapInfo();

            if (cellInfo.getCellId().startsWith(psapInfo.getOperatorCodeTw() + psapInfo.getOperatorCodeFet4g()) || cellInfo.getCellId().startsWith(psapInfo.getOperatorCodeTw() + psapInfo.getOperatorCodeFet3g())) {//only for TW to retrieve the psap
                if (Config.getCellNetworkInfo().getAccessType4G().equalsIgnoreCase(cellInfo.getAccessTypeName())) {

                    String lastSevenDgts = cellInfo.getCellId().substring(cellInfo.getCellId().length() - 7);

                    String cellIdX = String.valueOf(Integer.parseInt(lastSevenDgts.substring(0, 5), 16));
                    String cellIdXFormated = "000000".substring(0, 6 - cellIdX.length()) + cellIdX;
                    logger.info(new StringBuffer().append(callInfo).append("the cellIdX is ").append(cellIdX).append(", cellIdXFormated is ").append(cellIdXFormated).toString());
                    result = new CellId();
                    result.setCellIdFirstPart(cellIdX);
                    result.setCellIdFirstPartFormated(cellIdXFormated);

                } else if (Config.getCellNetworkInfo().getAccessType3G().equalsIgnoreCase(cellInfo.getAccessTypeName()) || Config.getCellNetworkInfo().getAccessType2G().equalsIgnoreCase(cellInfo.getAccessTypeName())) {
                    String lastFourDgts = cellInfo.getCellId().substring(cellInfo.getCellId().length() - 4);
                    String cellIdX = String.valueOf(Integer.parseInt(lastFourDgts, 16));
                    String cellIdXFormated = "00000".substring(0, 5 - cellIdX.length()) + cellIdX;
                    logger.info(new StringBuffer().append(callInfo).append("the cellIdX is ").append(cellIdX).append(", cellIdXFormated is ").append(cellIdXFormated).toString());
                    result = new CellId();
                    result.setCellIdFirstPart(cellIdX);
                    result.setCellIdFirstPartFormated(cellIdXFormated);
                }

            }

        } catch (Exception e) {
            logger.error(new StringBuffer().append(callInfo).toString(), e);
        }
        return result;

    }

    private boolean treatedAsEmergency(CallInfo callInfo) {
        boolean result = false;
        if (callInfo.getCallee().equalsIgnoreCase(Config.getSosUri().getCode()) && Config.getSosUri().getCode().equalsIgnoreCase(Config.getSosUri().getTreatAs())) {
            result = true;
        }
        return result;
    }

    private String getDefaultPSAP(CallInfo callInfo, CellInfo cellInfo) {
        String result = null;
        String psapStr = null;
        StringBuffer psap = new StringBuffer();
        StringBuffer defaultPsap = new StringBuffer();
        try {
            PsapInfo psapInfo = Config.getPsapInfo();

            if ("Y".equalsIgnoreCase(psapInfo.getAddPrefixCountryCode())) {
                psap.append(psapInfo.getCountryCodeTw());
                defaultPsap.append(psapInfo.getCountryCodeTw());

            }

            if (treatedAsEmergency(callInfo)) {
                defaultPsap.append(psapInfo.getDefaultPsap4g112());
            } else {
                defaultPsap.append(psapInfo.getDefaultPsap4g());
            }

            if (Config.getCellNetworkInfo().getAccessType4G().equalsIgnoreCase(cellInfo.getAccessTypeName())) {

                if (treatedAsEmergency(callInfo)) {
                    psapStr = psapInfo.getDefaultPsap4g112();

                } else {
                    psapStr = psapInfo.getDefaultPsap4g();
                }

            } else if (Config.getCellNetworkInfo().getAccessType3G().equalsIgnoreCase(cellInfo.getAccessTypeName()) || Config.getCellNetworkInfo().getAccessType2G().equalsIgnoreCase(cellInfo.getAccessTypeName())) {
                if (treatedAsEmergency(callInfo)) {
                    psapStr = psapInfo.getDefaultPsap3g112();

                } else {
                    psapStr = psapInfo.getDefaultPsap3g();

                }
            }
//            }

            if (psapStr != null) {
                result = psap.append(psapStr).toString();
            } else {
                result = defaultPsap.toString();
            }

            if (callInfo.getCallee().equalsIgnoreCase(Config.getSosUri().getCode())) {
                if (Config.getSosUri().getCode().equalsIgnoreCase(Config.getSosUri().getTreatAs())) {
                    result = result.replace("%B", callInfo.getCallee());
                } else {
                    result = result.replace("%B", Config.getSosUri().getTreatAs());
                }

            } else {
                result = result.replace("%B", callInfo.getCallee());
            }

        } catch (Exception e) {
            logger.error("", e);
            throw e;
        }
        return result;

    }

    private String getLacByCellInfo(CellInfo cellInfo) {
        String result = null;
//        if (cellInfo.getCellIdAttrName().equalsIgnoreCase(Config.getCellNetworkInfo().getCellIdParamKey3G())) {

        String lacHex = String.valueOf(Integer.parseInt(cellInfo.getCellId().substring(5, 9), 16));
        result = "0000".substring(0, 4 - lacHex.length()) + lacHex;
//        }

        return result;
    }

    private String getNeaByCity(String cityCode) {
        String result = null;
        result = Config.getCityNeaMap().get(cityCode);

        return result;
    }

    public String getPsap(String logHeader, CallInfo callInfo, CellInfo cellInfo) throws InternalException {

        String result = null;

        try {

            try {
                result = getDefaultPSAP(callInfo, cellInfo);
            } catch (Exception e) {
                String warnStr = "the configured default or the calculated psap is null";
                InternalException ex = new InternalException(warnStr);
                logger.error(new StringBuffer().append(callInfo).append(warnStr).toString(), ex);
                throw ex;
            }
            if (cellInfo != null) {
                if (cellInfo.getCellId() != null) {

                    CellId cellId = getCellId(callInfo, cellInfo);

                    if (cellId != null) {
                        String mBMsisdn = callInfo.getCallee();
                        PsapInfo psapInfo = Config.getPsapInfo();

                        String cityCode = null;
                        cityCode = getCityCode(callInfo, cellInfo, cellId);

                        //for 112
                        if (treatedAsEmergency(callInfo)) {
                            if (cityCode != null) {
                                String nea = getNeaByCity(cityCode);
                                if (nea != null) {
                                    StringBuffer psap = new StringBuffer();
                                    if ("Y".equalsIgnoreCase(psapInfo.getAddPrefixCountryCode())) {
                                        psap.append(psapInfo.getCountryCodeTw());
                                    }
                                    psap.append(mBMsisdn).append(nea).append(cellId.getCellIdFirstPartFormated());
                                    result = psap.toString();

                                }
                            }
                            // for 110 & 119 & 112(treated as 110/119)
                        } else {
                            if (cityCode != null) {

                                String oper_fet = null;
                                StringBuffer psap = new StringBuffer();

                                if (cellInfo.getAccessTypeName().equalsIgnoreCase(Config.getCellNetworkInfo().getAccessType4G())) {
                                    oper_fet = psapInfo.getOperatorCodeFet4g();
                                } else if (cellInfo.getAccessTypeName().equalsIgnoreCase(Config.getCellNetworkInfo().getAccessType3G()) || cellInfo.getAccessTypeName().equalsIgnoreCase(Config.getCellNetworkInfo().getAccessType2G())) {
                                    oper_fet = psapInfo.getOperatorCodeFet3g();

                                }

                                if ("Y".equalsIgnoreCase(psapInfo.getAddPrefixCountryCode())) {
                                    psap.append(psapInfo.getCountryCodeTw());
                                }

                                if (mBMsisdn.equalsIgnoreCase(Config.getSosUri().getCode())) {
                                    psap.append(Config.getSosUri().getTreatAs()).append(cityCode).append(oper_fet).append(cellId.getCellIdFirstPartFormated());

                                } else {
                                    psap.append(mBMsisdn).append(cityCode).append(oper_fet).append(cellId.getCellIdFirstPartFormated());
                                }
                                result = psap.toString();
                            }

                        }

                    }
                }
            }
            logger.info(new StringBuffer().append(callInfo).append("the psap is ").append(result).toString());
            /*
             if (result == null || result.length() == 0) {

             String warnStr = "the configured default or the calculated psap is null";
             InternalException e = new InternalException(warnStr);
             mNtSipLog.debug(new DebugLoggerTask(Level.ERROR, new StringBuffer().append(callInfo).append(warnStr).toString(), e));

             throw e;
             } else {
             return result;
             }
             */
        } catch (InternalException e) {
            throw e;
        } catch (Exception e) {
            logger.error(new StringBuffer().append(callInfo).toString(), e);
//            throw new InternalException(e.toString());
        }
        if (result == null || result.length() == 0) {

            String warnStr = "the configured default or the calculated psap is null";
            InternalException e = new InternalException(warnStr);
            logger.error(new StringBuffer().append(callInfo).append(warnStr).toString(), e);
            throw e;
        } else {
            return result;
        }

    }

    public int getLocaleType(String cellId) {
        int result = SipLogVo.LOCALE_TYPE_UNKNOWN;
        if (cellId != null) {
            if (cellId.startsWith(Config.getPsapInfo().getOperatorCodeTw())) {
                result = SipLogVo.LOCALE_TYPE_DOMESTIC;
            } else {
                result = SipLogVo.LOCALE_TYPE_ABROAD;
            }
        }
        return result;
    }

    private String getCityCode(CallInfo callInfo, CellInfo cellInfo, CellId cellId) {
        String result = null;

        String accessType="unknown 3G or 4G";
        try {

            if (cellInfo.getAccessTypeName().equalsIgnoreCase(Config.getCellNetworkInfo().getAccessType3G()) || cellInfo.getAccessTypeName().equalsIgnoreCase(Config.getCellNetworkInfo().getAccessType2G())) {
                String Lac = getLacByCellInfo(cellInfo);
                result = Config.getLacCityMap().get(Lac);
                accessType="3G";
            } else if (cellInfo.getAccessTypeName().equalsIgnoreCase(Config.getCellNetworkInfo().getAccessType4G())) {
                accessType="4G";
                String cell = cellId.getCellIdFirstPartFormated();
                String cellIdPrefixTwoDgt = cell.substring(0, 1);

                if (Config.getCityCellMap().containsKey(cellIdPrefixTwoDgt)) {
                    CityCellInfo maxCellIdMin = null;
                    ArrayList<CityCellInfo> cellList = Config.getCityCellMap().get(cellIdPrefixTwoDgt);
                    for (CityCellInfo vo : cellList) {
                        if (cell.compareTo(vo.getCellIdMin()) >= 0 && vo.getCellIdMax().compareTo(cell) >= 0) {
                            if (maxCellIdMin == null) {
                                maxCellIdMin = vo;
                            } else {
                                if (vo.getCellIdMin().compareTo(maxCellIdMin.getCellIdMin()) > 0) {
                                    maxCellIdMin = vo;
                                }
                            }

                        }

                    }

                    if (maxCellIdMin != null) {
                        result = maxCellIdMin.getCityCode();
                    }

                }
            }

            /*
             if (inputType.equalsIgnoreCase(TYPE_LAC)) {
             result = Config.getLacCityMap().get(inputValue);

             } else if (inputType.equalsIgnoreCase(TYPE_CELLID)) {
             String cellIdPrefixTwoDgt = inputValue.substring(0, 1);

             if (Config.getCityCellMap().containsKey(cellIdPrefixTwoDgt)) {
             CityCellInfo maxCellIdMin = null;
             ArrayList<CityCellInfo> cellList = Config.getCityCellMap().get(cellIdPrefixTwoDgt);
             for (CityCellInfo vo : cellList) {
             if (inputValue.compareTo(vo.getCellIdMin()) >= 0 && vo.getCellIdMax().compareTo(inputValue) >= 0) {
             if (maxCellIdMin == null) {
             maxCellIdMin = vo;
             } else {
             if (vo.getCellIdMin().compareTo(maxCellIdMin.getCellIdMin()) > 0) {
             maxCellIdMin = vo;
             }
             }

             }

             }

             if (maxCellIdMin != null) {
             result = maxCellIdMin.getCityCode();
             }

             }
             }
             */
        } catch (Exception e) {
            logger.error(new StringBuffer().append(callInfo).toString(), e);

        }
        logger.info(new StringBuffer().append(callInfo).append("the city code is ").append(result).toString());
        
        //todo alarm
        if (result==null || result.length()==0){
            logger.error("Can not find City for "+accessType+" cell="+cellId.getCellIdFirstPartFormated());
        }
        return result;
    }

    public boolean isSosUri(URI uri) {

        String uriStr = uri.toString().trim();

        if (Config.getSosUri().getUrnFormat() != null && Config.getSosUri().getUrnFormat().equalsIgnoreCase("Y")) {
            return uriStr.equals(Config.getSosUri().getUri());
        } else {
            if (uri.isSipURI()) {
                return ((SipUri) uri).getUser().equals(Config.getSosUri().getUri());
            } else {
                return ((TelURLImpl) uri).getPhoneNumber().equals(Config.getSosUri().getUri());
            }

//            return uriStr.startsWith(Config.getSosUri().getUri());
        }

    }

    public boolean isSosUri(String uri) {
        String uriStr = uri.toString().trim();
        return uriStr.equals(Config.getSosUri().getUri());
    }

    public String getSosTel(URI uri) {
        if (isSosUri(uri)) {
            return Config.getSosUri().getCode();
        }
        return null;
    }

    public void terminate(String callId, CdrLoggerTask mCdrLogTask) {
        CallState callState = getCallState(callId);
        String logHeader = "[callId=" + callId + "]";

        logger.debug(logHeader + "callState: " + callState);
        if (CallState.TERMINATED.equals(callState)) {
//            cdrLogger.info(mCdrLogTask);
            removeCall(callId);
        }

    }

    public MessageFactory getMessageFactory() {
        return messageFactory;
    }

    public HeaderFactory getHeaderFactory() {
        return headerFactory;
    }

    public AddressFactory getAddressFactory() {
        return addressFactory;
    }

}
