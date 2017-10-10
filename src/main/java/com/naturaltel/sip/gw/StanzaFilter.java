package com.naturaltel.sip.gw;

import javax.sip.message.Message;

public interface StanzaFilter {

    public boolean accept(Message message);
    
}
