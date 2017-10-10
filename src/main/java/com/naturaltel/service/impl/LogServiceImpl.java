package com.naturaltel.service.impl;

import com.naturaltel.dao.SipLogDao;
import com.naturaltel.dao.entity.SipLogVo;
import com.naturaltel.service.LogService;

public class LogServiceImpl implements LogService {

    @Override
    public void insertSipEvent(SipLogVo sipLogVo) {
        SipLogDao sipDao = SipLogDao.getInstance();
        sipDao.insert(sipLogVo);
    }

}
