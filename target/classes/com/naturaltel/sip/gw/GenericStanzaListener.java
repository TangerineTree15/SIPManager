package com.naturaltel.sip.gw;

import java.text.SimpleDateFormat;

import javax.sip.ClientTransaction;
import javax.sip.ServerTransaction;
import javax.sip.header.AcceptHeader;
import javax.sip.header.AllowHeader;
import javax.sip.header.CSeqHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

import org.apache.log4j.Logger;

import com.naturaltel.cache.Config;
import com.naturaltel.config.entity.EmergencyCodes;
import com.naturaltel.dao.entity.SipLogVo;
import com.naturaltel.service.LogService;
import com.naturaltel.service.impl.LogServiceImpl;
import com.naturaltel.sip.gw.entity.CallInfo;
import com.naturaltel.sip.gw.entity.CellInfo;
import com.naturaltel.sip.log.CdrLoggerTask;

import gov.nist.javax.sip.address.AddressImpl;
import gov.nist.javax.sip.address.TelURLImpl;
import gov.nist.javax.sip.address.TelephoneNumber;
import javax.sip.header.ReasonHeader;

public class GenericStanzaListener implements StanzaListener {

    private Logger logger = Logger.getLogger("debug");
    private Logger cdrLogger = Logger.getLogger("cdr");

    public void doAck(ServerTransaction st, Request req, CallInfo callInfo, CdrLoggerTask cdrLoggerTask) {
        final String callId = callInfo.getCallId();
        final String mAMsisdn = callInfo.getCaller();
        final String mBMsisdn = callInfo.getCallee();
        SipManager sipManager = null;
        try {
            sipManager = SipManager.getInstance();
            sipManager.changeCallState(callId, CallFlow.ACK);

        } catch (CallFlowException e) {

//            logger.error("", e);
        } catch (Exception e) {

//            logger.error("", e);
        } finally {
            cdrLogger.info(cdrLoggerTask.toString());

            if (sipManager != null) {
                sipManager.terminate(callId, cdrLoggerTask);
            }
        }

    }

    public void doBye(ServerTransaction st, Request req, CallInfo callInfo, CdrLoggerTask cdrLoggerTask) {
        logger.info(req);

    }

    public void doCancel(ServerTransaction st, Request req, CallInfo callInfo, CdrLoggerTask cdrLoggerTask) throws Exception {
        final String callId = callInfo.getCallId();
        final String logHeader = "[callId=" + callId + "]";

        final String mAMsisdn = callInfo.getCaller();
        final String mBMsisdn = callInfo.getCallee();
        final String source = callInfo.getSource();
        SipManager sipManager = null;
        MessageFactory messageFactory = null;
        CallState callState = null;
        SipLogVo sipLogVo = null;
        String releaseCause = null;
        int responseCode = 0;
        try {
            sipManager = SipManager.getInstance();
            messageFactory = sipManager.getMessageFactory();
            callState = sipManager.getCallState(callId);
            logger.info("callState.name(): " + callState.name());
            sipManager.changeCallState(callId, CallFlow.CANCEL);
//            if (st == null) {
//                st = sipManager.getNewServerTransaction(req);
//            }
            if (CallState.PROCEEDING.equals(callState)) {
                responseCode = Response.OK;
                Response okResponse = messageFactory.createResponse(responseCode, req);
//                st.sendResponse(okResponse);
                sipManager.sendResponseToClient(okResponse);
                //TODO not sure if this implementation is ok
                req.setMethod(Request.INVITE);
                Response requestTerminatedResponse = messageFactory.createResponse(Response.REQUEST_TERMINATED, req);
//                st.sendResponse(requestTerminatedResponse);
                sipManager.sendResponseToClient(requestTerminatedResponse);

            } else {
                responseCode = Response.CALL_OR_TRANSACTION_DOES_NOT_EXIST;
                Response notExistCallResponse = messageFactory.createResponse(responseCode, req);
//                st.sendResponse(notExistCallResponse);
                sipManager.sendResponseToClient(notExistCallResponse);
            }

            // TODO signaling log with cancel event 
            releaseCause = ReleaseCause.CANCEL_END;

        } catch (Exception e) {
            logger.error("", e);

            releaseCause = ReleaseCause.INTERNAL_ERROR;
            throw e;
        } finally {
            logger.error(logHeader + "Response:" + responseCode);
            cdrLoggerTask.setReleaseCause(releaseCause);
            cdrLogger.info(cdrLoggerTask.toString());

            sipLogVo = new SipLogVo();
            sipLogVo.setCellId(null);
            sipLogVo.setEventName(Request.CANCEL);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            sipLogVo.setEventTime(sdf.format(callInfo.getEventTime()));
            sipLogVo.setLocaleType(0);
            sipLogVo.setPartyA(mAMsisdn);
            sipLogVo.setPartyB(mBMsisdn);
            sipLogVo.setCallId(callId);

            sipLogVo.setPartyPsap(null);
            sipLogVo.setSourceDomain(source);
//            String localHostName = Config.getSipInfo().getAddress() + Config.getSipInfo().getPort();
            String localHostName = Config.getSipInfo().getAddress();

            sipLogVo.setLocalHostName(localHostName);
            sipLogVo.setResponseCode(responseCode);
            sipLogVo.setReleaseCause(releaseCause);
            LogService logService = new LogServiceImpl();
            logService.insertSipEvent(sipLogVo);
            logger.info(logHeader + sipLogVo.getEventName() + " log inserted");

            try {
                sipManager.changeCallState(callId, CallFlow.OK);
            } catch (CallFlowException e) {
                logger.error("", e);

            }

            if (sipManager != null) {
                sipManager.terminate(callId, cdrLoggerTask);
            }
        }
    }

    public void doInfo(ServerTransaction st, Request req, CallInfo callInfo, CdrLoggerTask cdrLoggerTask) {
        logger.info(req);

    }

    public void doMessage(ServerTransaction st, Request req, CallInfo callInfo, CdrLoggerTask cdrLoggerTask) {
        logger.info(req);

    }

    public void doInvite(ServerTransaction st, Request req, CallInfo callInfo, CdrLoggerTask cdrLoggerTask) throws Exception {
        final String callId = callInfo.getCallId();
        final String logHeader = "[callId=" + callId + "]";

        final String mAMsisdn = callInfo.getCaller();
        final String mBMsisdn = callInfo.getCallee();
        final String source = callInfo.getSource();
        SipManager sipManager = null;
        MessageFactory messageFactory = null;
        CallState callState = null;
        String cellId = null;
        SipLogVo sipLogVo = new SipLogVo();
        String releaseCause = ReleaseCause.INVITE_SUCCESS;
        int responseCode = 0;
        int localeType = 0;
        String psap = null;
        CellInfo cellInfo = null;

        try {
            sipManager = SipManager.getInstance();
            messageFactory = sipManager.getMessageFactory();
            sipManager.changeCallState(callId, CallFlow.INVITE);

            // check is request URI valid
            boolean isMatch = false;

            if (!sipManager.isSosUri(req.getRequestURI())) {
//            if (!sipManager.isSosUri(mBMsisdn)) {

                EmergencyCodes eCodes = Config.getEmergencyCodes();
                String countryCodePrefix = eCodes.getCountryCodePrefix();

                for (String code : eCodes.getCodes()) {
                    if ((mBMsisdn).startsWith(countryCodePrefix + code)) {
                        callInfo.setCallee(code);
                        cdrLoggerTask.setTo(code);
                        isMatch = true;
                        break;
                    }
                }

            } else {
                isMatch = true;
            }

            if (!isMatch) {
                sipManager.changeCallState(callId, CallFlow.ERROR);
                responseCode = Response.BAD_REQUEST;
                releaseCause = ReleaseCause.BADREQUEST_ERROR;
                Response resp = messageFactory.createResponse(responseCode, req);
//                    if (st == null) {
//                        st = sipManager.getNewServerTransaction(req);
//                    }
//                    st.sendResponse(resp);
                sipManager.sendResponseToClient(resp);
            } else {

                // TODO PARSING MSG TO GET CELL INFO
                cellInfo = sipManager.parseCellInfo(logHeader, req);
                cellId = cellInfo.getCellId();
//            PCellularNetworkInfoHeader pCellularNetworkInfoHeader = (PCellularNetworkInfoHeader) req.getHeader(cellNetworkInfo.getHeaderName());
//            String cellIdAttr = cellNetworkInfo.getCellIdParamKey4G();
//            cellId = pCellularNetworkInfoHeader.getParameter(cellIdAttr);
//            if(cellId == null || "".equals(cellId.trim())) {
//                cellIdAttr = cellNetworkInfo.getCellIdParamKey3G();
//                cellId = pCellularNetworkInfoHeader.getParameter(cellIdAttr);
//            }
                psap = sipManager.getPsap(logHeader, callInfo, cellInfo);

                localeType = sipManager.getLocaleType(cellId);

                // TODO response msg 300 multiple choices with state:proceeding check
                callState = sipManager.getCallState(callId);
                if (CallState.PROCEEDING.equals(callState)) {

                    HeaderFactory headerFactory = sipManager.getHeaderFactory();
                    responseCode = Response.MULTIPLE_CHOICES;
                    Response resp = messageFactory.createResponse(responseCode, req);
                    ContactHeader contactHeader = headerFactory.createContactHeader();
                    contactHeader.removeParameter("expires"); // ERT don't want the 'expires' like 'Contact: <tel:+886128102101200567>;expires=0'
//                com.naturaltel.config.entity.ContactHeader test = headerFactory.createHeader("Contact", "Contact");
//                Header contactHeader = headerFactory.createHeader("Contact", "Contact");
                    TelURLImpl telUri = new TelURLImpl();
                    telUri.setTelephoneNumber(new TelephoneNumber());
                    telUri.setPhoneNumber(psap);
                    AddressImpl address = new AddressImpl();
                    address.setURI(telUri);
                    contactHeader.setAddress(address);
                    resp.setHeader(contactHeader);
//                if (st == null) {
//                    st = sipManager.getNewServerTransaction(req);
//                }

//                    Thread.sleep(5000);
                    sipManager.changeCallState(callId, CallFlow.OK);
//                st.sendResponse(resp);
                    sipManager.sendResponseToClient(resp);
                    contactHeader = (ContactHeader) resp.getHeader(ContactHeader.NAME);
                    logger.info(logHeader + "resp: " + resp.toString());
                    logger.info(logHeader + "resp with contactHeader.getAddress().getURI().toString(): " + contactHeader.getAddress().getURI().toString());
                    releaseCause = ReleaseCause.INVITE_SUCCESS;

                } else {
                    responseCode = Response.REQUEST_TERMINATED;
                    releaseCause = ReleaseCause.CANCEL_END;
                }

            }
            cdrLoggerTask.setEndDateTime();
            cdrLoggerTask.setPsap(psap);
            cdrLoggerTask.setCellId(cellId);

        } catch (CallFlowException e) {
            logger.error("", e);

        } catch (Exception e) {
            logger.error("", e);

            releaseCause = ReleaseCause.INTERNAL_ERROR;
            responseCode = Response.SERVICE_UNAVAILABLE;
//            throw e;

            HeaderFactory headerFactory = sipManager.getHeaderFactory();
            Response errorResponse = messageFactory.createResponse(Response.SERVICE_UNAVAILABLE, req);
            if (!Request.ACK.equals(req.getMethod())) {
                ReasonHeader reasonHeader = headerFactory.createReasonHeader("SIP", Response.SERVICE_UNAVAILABLE, "service unavailable");
                errorResponse.setHeader(reasonHeader);
                sipManager.sendResponseToClient(errorResponse);
            }

        } finally {

            logger.info(logHeader + "Response:" + responseCode);
            cdrLoggerTask.setReleaseCause(releaseCause);
            cdrLoggerTask.setResponseCode(responseCode);
            cdrLoggerTask.setLocaleType(localeType);

            cdrLogger.info(cdrLoggerTask.toString());

            sipLogVo.setEventName(Request.INVITE);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            sipLogVo.setEventTime(sdf.format(callInfo.getEventTime()));
            sipLogVo.setCellId(cellId);
            sipLogVo.setLocaleType(localeType);
            sipLogVo.setPartyA(callInfo.getCaller());
            sipLogVo.setPartyB(callInfo.getCallee());
            sipLogVo.setCallId(callId);

            sipLogVo.setPartyPsap(psap);
            sipLogVo.setSourceDomain(source);
//            String localHostName = Config.getSipInfo().getAddress() + ":" + Config.getSipInfo().getPort();
            String localHostName = Config.getSipInfo().getAddress();

            if (cellInfo != null) {
                sipLogVo.setPHeader(cellInfo.getHeaderName());
                sipLogVo.setPHeaderAttr(cellInfo.getCellIdAttrName());
                sipLogVo.setpHeaderAccess(cellInfo.getAccessTypeName());
            }
            sipLogVo.setLocalHostName(localHostName);
            sipLogVo.setResponseCode(responseCode);
            sipLogVo.setReleaseCause(releaseCause);
            LogService logService = new LogServiceImpl();
            logService.insertSipEvent(sipLogVo);
            logger.debug(logHeader + sipLogVo.getEventName() + " log inserted");
            try {
                sipManager.changeCallState(callId, CallFlow.OK);
            } catch (CallFlowException e) {
                logger.error("", e);
            }

            if (sipManager != null) {
                sipManager.terminate(callId, cdrLoggerTask);
            }
        }
    }

    public void doNotify(ServerTransaction st, Request req, CallInfo callInfo, CdrLoggerTask cdrLoggerTask) {
        logger.info(req);

    }

    public void doOptions(ServerTransaction st, Request req, CallInfo callInfo, CdrLoggerTask cdrLoggerTask) {

        final String callId = callInfo.getCallId();
        final String logHeader = "[callId=" + callId + "]";

        final String source = callInfo.getSource();

        String releaseCause = ReleaseCause.INVITE_SUCCESS;
        int responseCode = Response.OK;
        try {

            SipManager sipManager = SipManager.getInstance();
            MessageFactory messageFactory = sipManager.getMessageFactory();
            HeaderFactory headerFactory = sipManager.getHeaderFactory();
            AllowHeader allowInvite = headerFactory.createAllowHeader(Request.INVITE);
            AllowHeader allowAck = headerFactory.createAllowHeader(Request.ACK);
//            AllowHeader allowBye = headerFactory.createAllowHeader(Request.BYE);
            AllowHeader allowCancel = headerFactory.createAllowHeader(Request.CANCEL);
//            AllowHeader allowInfo = headerFactory.createAllowHeader(Request.INFO);
            AllowHeader allowOptions = headerFactory.createAllowHeader(Request.OPTIONS);
            AcceptHeader acceptSDPHeader = headerFactory.createAcceptHeader("application", "sdp");
            Response resp = messageFactory.createResponse(responseCode, req);
            resp.addHeader(allowInvite);
            resp.addHeader(allowAck);
//            resp.addHeader(allowBye);
            resp.addHeader(allowCancel);
//            resp.addHeader(allowInfo);
            resp.addHeader(allowOptions);
            resp.addHeader(acceptSDPHeader);
            if (st == null) {
                st = sipManager.getNewServerTransaction(req);
            }
            st.sendResponse(resp);

            cdrLoggerTask.setEndDateTime();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.error("", e);

        } finally {

            logger.info(logHeader + "Response:" + responseCode);
            cdrLoggerTask.setReleaseCause(releaseCause);
            cdrLoggerTask.setResponseCode(responseCode);

            cdrLogger.info(cdrLoggerTask.toString());

            SipLogVo sipLogVo = new SipLogVo();
            sipLogVo.setEventName(Request.OPTIONS);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            sipLogVo.setEventTime(sdf.format(callInfo.getEventTime()));
            sipLogVo.setPartyA(callInfo.getCaller());
            sipLogVo.setPartyB(callInfo.getCallee());
            sipLogVo.setCallId(callId);
            sipLogVo.setLocaleType(0);

            sipLogVo.setSourceDomain(source);
//            String localHostName = Config.getSipInfo().getAddress() + ":" + Config.getSipInfo().getPort();
            String localHostName = Config.getSipInfo().getAddress();

            sipLogVo.setLocalHostName(localHostName);
            sipLogVo.setResponseCode(responseCode);
            sipLogVo.setReleaseCause(releaseCause);
            LogService logService = new LogServiceImpl();
            logService.insertSipEvent(sipLogVo);
            logger.debug(logHeader + sipLogVo.getEventName() + " log inserted");

        }
    }

    public void doPrack(ServerTransaction st, Request req, CallInfo callInfo, CdrLoggerTask cdrLoggerTask) {
        logger.info(req);

    }

    public void doPublish(ServerTransaction st, Request req, CallInfo callInfo, CdrLoggerTask cdrLoggerTask) {
        logger.info(req);

    }

    public void doRefer(ServerTransaction st, Request req, CallInfo callInfo, CdrLoggerTask cdrLoggerTask) {
        logger.info(req);

    }

    public void doRegister(ServerTransaction st, Request req, CallInfo callInfo, CdrLoggerTask cdrLoggerTask) {
        try {
            SipManager sipManager = SipManager.getInstance();
            MessageFactory messageFactory = sipManager.getMessageFactory();
            Response resp = messageFactory.createResponse(Response.OK, req);
            if (st == null) {
                st = sipManager.getNewServerTransaction(req);
            }
            st.sendResponse(resp);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void doSubscribe(ServerTransaction st, Request req, CallInfo callInfo, CdrLoggerTask cdrLoggerTask) {
        logger.info(req);

    }

    public void doUpdate(ServerTransaction st, Request req, CallInfo callInfo, CdrLoggerTask cdrLoggerTask) {
        logger.info(req);

    }

    public void onProvisional(ClientTransaction ct, Response resp, CallInfo callInfo) {
        // TODO Auto-generated method stub

    }

    public void onSuccess(ClientTransaction ct, Response resp, CallInfo callInfo) {
        final String mAMsisdn = callInfo.getCaller();
        final String mBMsisdn = callInfo.getCallee();
        final String callId = callInfo.getCallId();
        try {
            SipManager sipManager = SipManager.getInstance();
            final String method = ((CSeqHeader) resp.getHeader(CSeqHeader.NAME)).getMethod();
            if (method.equals(Request.INFO)) {

            } else if (method.equals(Request.INVITE)) {

                sipManager.changeCallState(callId, CallFlow.OK);

            } else {

                sipManager.changeCallState(callId, CallFlow.OK);
            }

        } catch (Exception e) {
            logger.error("", e);

        }
    }

    public void onRedirection(ClientTransaction ct, Response resp, CallInfo callInfo) {
        final String mAMsisdn = callInfo.getCaller();
        final String mBMsisdn = callInfo.getCallee();
        final String callId = callInfo.getCallId();
        logger.info(callInfo);
        try {
            SipManager sipManager = SipManager.getInstance();
            final String method = ((CSeqHeader) resp.getHeader(CSeqHeader.NAME)).getMethod();
            if (Request.INVITE.equals(method)) {
                sipManager.changeCallState(callId, CallFlow.ERROR);
            }
        } catch (CallFlowException e) {
            logger.error("", e);

        } catch (Exception e) {
            logger.error("", e);

        }

    }

    public void onClientError(ClientTransaction ct, Response resp, CallInfo callInfo) {
        final String mAMsisdn = callInfo.getCaller();
        final String mBMsisdn = callInfo.getCallee();
        final String callId = callInfo.getCallId();
        try {
            SipManager sipManager = SipManager.getInstance();
            final String method = ((CSeqHeader) resp.getHeader(CSeqHeader.NAME)).getMethod();
            if (Request.INVITE.equals(method)) {

                sipManager.changeCallState(callId, CallFlow.ERROR);
            }
        } catch (CallFlowException e) {

            logger.error("", e);
        } catch (Exception e) {
            logger.error("", e);

        }

    }

    public void onServerError(ClientTransaction ct, Response resp, CallInfo callInfo) {
        final String mAMsisdn = callInfo.getCaller();
        final String mBMsisdn = callInfo.getCallee();
        final String callId = callInfo.getCallId();
        try {
            SipManager sipManager = SipManager.getInstance();
            final String method = ((CSeqHeader) resp.getHeader(CSeqHeader.NAME)).getMethod();
            if (Request.INVITE.equals(method)) {
                sipManager.changeCallState(callId, CallFlow.ERROR);
            }
        } catch (CallFlowException e) {
            logger.error("", e);
        } catch (Exception e) {
            logger.error("", e);
        }

    }

    public void onGlobalFailure(ClientTransaction ct, Response resp, CallInfo callInfo) {
        final String mAMsisdn = callInfo.getCaller();
        final String mBMsisdn = callInfo.getCallee();
        final String callId = callInfo.getCallId();
        try {
            SipManager sipManager = SipManager.getInstance();
            final String method = ((CSeqHeader) resp.getHeader(CSeqHeader.NAME)).getMethod();
            if (Request.INVITE.equals(method)) {

                sipManager.changeCallState(callId, CallFlow.ERROR);
            }
        } catch (CallFlowException e) {
            logger.error("", e);
        } catch (Exception e) {
            logger.error("", e);
        }

    }

}
