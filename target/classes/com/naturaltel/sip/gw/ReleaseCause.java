package com.naturaltel.sip.gw;

import java.util.HashMap;
import javax.sip.message.Response;

public class ReleaseCause {

    public final static String INVITE_SUCCESS = "NORMAL_END";
    public final static String CANCEL_END = "CANCEL_END";
    public final static String INTERNAL_ERROR = "INTERNAL_ERROR";
    public final static String BADREQUEST_ERROR = "BADREQUEST_ERROR";

//  //ISDN & SS7 release cause
//    001       unassigned number (in DB) 
//    002       no route to transit network 
//    003       no route to destination 
//    004       send special infomation tone 
//    005       misdialed trunk prefix 
//    016       normal call clearing
//    017       user busy 
//    018       no user response 
//    019       no answer (alerted) 
//    020       circuit blocked
//    021       call rejected 
//    022       number changed 
//    027       destination out of order 
//    028       Address incomplete 
//    029       facility rejected 
//    031       normal unspecified 
//    034       no circuit available 
//    038       network out of order 
//    041       Temporary failure 
//    042       Switch equipment congestion 
//    043       user information discarded 
//    044       request circuit unavailable 
//    047       resources unavailable 
//    #049       TG release for redirecting
//    050       request facility not subscrib 
//    #051       client reset circuit 
//    #052       ISUPSG itself reset circuit 
//    #053       ISUPSG itself release circuit 
//    #054      all link down , add 20120328 for SaveCom
//    055       incoming calls barred CUG 
//    057       bearer cap not authorized 
//    058       bearer cap not available 
//    063       Service unavailable 
//    065       bearer cap not implemented 
//    069       request facility not implement 
//    070       only digital bear cap is avail
//    079       service not implemented 
//    081       invalid call reference value 
//    082       channel id not exist
//    087       Called Not member of CUG 
//    088       incompatible destination 
//    091       invalid transit selection 
//    095       invalid message unspecified 
//    096       mandatory element is missing 
//    097       msg type is non-existent
//    099       Param. non-existant discard 
//    100       invalid parameter contents 
//    102       timeout recovery 
//    103       Param. non-existant pass along 
//    111       protocol error unspecified 
//    127       interworking unspecified 
//
//
//    //following cause were defined by ourself ---------------
//    131       H324M Fail
//    132       UII check Fail
//    133       Password Check Fail
//
//    //MS specified 
//    140    Max Call Duration Timeout
//    141    B-party disappear in switch mode
//    142    B-party not match in switch mode
//
//    //Announcement Detected
//    151       無回應
//    152       關機或沒有回應
//    153       空號
//    154       暫停使用
//    155       暫時不接聽電話
//    156       轉接到語音信箱
//    157       忙線中 
//    158       被過慮 new add 2004-12-06
//    159       故障
//
//    //manual released 
//    201       manual released by NaturalBuilder
//    202       manual released by Diag Tool
//    203       released by NaturalBuilder shutdown
//    204       released by redirect request
    private static ReleaseCause mReleaseCause;
    private static final HashMap<Integer, Integer> mISDNtoSIPHashMap = new HashMap<Integer, Integer>();
    private static final HashMap<Integer, Integer> mSIPtoISDNHashMap = new HashMap<Integer, Integer>();

    private ReleaseCause() {
        mISDNtoSIPHashMap.put(1, Response.NOT_FOUND);
        mISDNtoSIPHashMap.put(2, Response.NOT_FOUND);
        mISDNtoSIPHashMap.put(3, Response.NOT_FOUND);
        mISDNtoSIPHashMap.put(17, Response.BUSY_HERE);
        mISDNtoSIPHashMap.put(18, Response.REQUEST_TIMEOUT);
        mISDNtoSIPHashMap.put(19, Response.TEMPORARILY_UNAVAILABLE);
        mISDNtoSIPHashMap.put(20, Response.TEMPORARILY_UNAVAILABLE);
        mISDNtoSIPHashMap.put(21, Response.FORBIDDEN);
        mISDNtoSIPHashMap.put(22, Response.GONE);
        mISDNtoSIPHashMap.put(23, Response.GONE);
        mISDNtoSIPHashMap.put(26, Response.NOT_FOUND);
        mISDNtoSIPHashMap.put(27, Response.BAD_GATEWAY);
        mISDNtoSIPHashMap.put(28, Response.ADDRESS_INCOMPLETE);
        mISDNtoSIPHashMap.put(29, Response.NOT_IMPLEMENTED);
        mISDNtoSIPHashMap.put(31, Response.TEMPORARILY_UNAVAILABLE);
        mISDNtoSIPHashMap.put(34, Response.SERVICE_UNAVAILABLE);
        mISDNtoSIPHashMap.put(38, Response.SERVICE_UNAVAILABLE);
        mISDNtoSIPHashMap.put(41, Response.SERVICE_UNAVAILABLE);
        mISDNtoSIPHashMap.put(42, Response.SERVICE_UNAVAILABLE);
        mISDNtoSIPHashMap.put(47, Response.SERVICE_UNAVAILABLE);
        mISDNtoSIPHashMap.put(55, Response.FORBIDDEN);
        mISDNtoSIPHashMap.put(57, Response.FORBIDDEN);
        mISDNtoSIPHashMap.put(58, Response.SERVICE_UNAVAILABLE);
        mISDNtoSIPHashMap.put(65, Response.NOT_ACCEPTABLE_HERE);
        mISDNtoSIPHashMap.put(70, Response.NOT_ACCEPTABLE_HERE);
        mISDNtoSIPHashMap.put(79, Response.NOT_IMPLEMENTED);
        mISDNtoSIPHashMap.put(87, Response.FORBIDDEN);
        mISDNtoSIPHashMap.put(88, Response.SERVICE_UNAVAILABLE);
        mISDNtoSIPHashMap.put(102, Response.SERVER_TIMEOUT);
        mISDNtoSIPHashMap.put(111, Response.SERVER_INTERNAL_ERROR);
        mISDNtoSIPHashMap.put(127, Response.SERVER_INTERNAL_ERROR);

        mSIPtoISDNHashMap.put(Response.BAD_REQUEST, 41);
        mSIPtoISDNHashMap.put(Response.UNAUTHORIZED, 21);
        mSIPtoISDNHashMap.put(Response.PAYMENT_REQUIRED, 21);
        mSIPtoISDNHashMap.put(Response.FORBIDDEN, 21);
        mSIPtoISDNHashMap.put(Response.NOT_FOUND, 1);
        mSIPtoISDNHashMap.put(Response.METHOD_NOT_ALLOWED, 63);
        mSIPtoISDNHashMap.put(Response.NOT_ACCEPTABLE, 79);
        mSIPtoISDNHashMap.put(Response.PROXY_AUTHENTICATION_REQUIRED, 21);
        mSIPtoISDNHashMap.put(Response.REQUEST_TIMEOUT, 102);
        mSIPtoISDNHashMap.put(Response.GONE, 22);
        mSIPtoISDNHashMap.put(Response.REQUEST_ENTITY_TOO_LARGE, 127);
        mSIPtoISDNHashMap.put(Response.REQUEST_URI_TOO_LONG, 127);
        mSIPtoISDNHashMap.put(Response.UNSUPPORTED_MEDIA_TYPE, 79);
        mSIPtoISDNHashMap.put(Response.UNSUPPORTED_URI_SCHEME, 127);
        mSIPtoISDNHashMap.put(Response.BAD_EXTENSION, 127);
        mSIPtoISDNHashMap.put(Response.EXTENSION_REQUIRED, 127);
        mSIPtoISDNHashMap.put(Response.INTERVAL_TOO_BRIEF, 127);
        mSIPtoISDNHashMap.put(Response.TEMPORARILY_UNAVAILABLE, 18);
        mSIPtoISDNHashMap.put(Response.CALL_OR_TRANSACTION_DOES_NOT_EXIST, 41);
        mSIPtoISDNHashMap.put(Response.LOOP_DETECTED, 25);
        mSIPtoISDNHashMap.put(Response.TOO_MANY_HOPS, 25);
        mSIPtoISDNHashMap.put(Response.ADDRESS_INCOMPLETE, 28);
        mSIPtoISDNHashMap.put(Response.AMBIGUOUS, 1);
        mSIPtoISDNHashMap.put(Response.BUSY_HERE, 17);
        mSIPtoISDNHashMap.put(Response.REQUEST_TERMINATED, 127);
        mSIPtoISDNHashMap.put(Response.NOT_ACCEPTABLE_HERE, 79);
        mSIPtoISDNHashMap.put(Response.SERVER_INTERNAL_ERROR, 41);
        mSIPtoISDNHashMap.put(Response.NOT_IMPLEMENTED, 79);
        mSIPtoISDNHashMap.put(Response.BAD_GATEWAY, 38);
        mSIPtoISDNHashMap.put(Response.SERVICE_UNAVAILABLE, 41);
        mSIPtoISDNHashMap.put(Response.SERVER_TIMEOUT, 102);
        mSIPtoISDNHashMap.put(Response.VERSION_NOT_SUPPORTED, 127);
        mSIPtoISDNHashMap.put(Response.MESSAGE_TOO_LARGE, 127);
        mSIPtoISDNHashMap.put(Response.BUSY_EVERYWHERE, 17);
        mSIPtoISDNHashMap.put(Response.DECLINE, 21);
        mSIPtoISDNHashMap.put(Response.DOES_NOT_EXIST_ANYWHERE, 1);
        mSIPtoISDNHashMap.put(Response.SESSION_NOT_ACCEPTABLE, 79);
    }

    final public static void init() {
        if (mReleaseCause == null) {
            mReleaseCause = new ReleaseCause();
        }
    }

    final public static int getSipResponseCodeByISDN(int isdn) {
        Integer obj = mISDNtoSIPHashMap.get(isdn);
        if (obj == null) {
            return -1;
        } else {
            return obj.intValue();
        }
    }

    final public static int getISDNBySipResponseCode(int code) {

        Integer obj = mSIPtoISDNHashMap.get(code);
        if (obj == null) {
            return -1;
        } else {
            return obj.intValue();
        }
    }

}
