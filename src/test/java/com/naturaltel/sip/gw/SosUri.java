package com.naturaltel.sip.gw;

import com.naturaltel.cache.Config;

import gov.nist.javax.sip.address.SipUri;

public class SosUri extends SipUri {

    private static final long serialVersionUID = 8496818192825691259L;

    public SosUri() {
        scheme = Config.getSosUri().getUri();
    }

    @Override
    public String encode() {
        return encode(new StringBuilder()).toString();
    }

    @Override
    public StringBuilder encode(StringBuilder buffer) {
        buffer.append(scheme);
//        if (authority != null)
//            authority.encode(buffer);
        if (!uriParms.isEmpty()) {
            buffer.append(SEMICOLON);
            uriParms.encode(buffer);
        }
        if (!qheaders.isEmpty()) {
            buffer.append(QUESTION);
            qheaders.encode(buffer);
        }
        return buffer;
    }

    @Override
    public String toString() {
        return this.encode();
    }


}
