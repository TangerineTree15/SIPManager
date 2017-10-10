package com.naturaltel.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.naturaltel.config.ConfigLoader;
import com.naturaltel.dao.entity.SipLogVo;

import org.apache.log4j.Logger;

public class SipLogDao {

    //for test
    public static void main(String[] args) {
        try {
            ConfigLoader.loadConfiguration();
            SipLogDao dao = SipLogDao.getInstance();
            SipLogVo sipLogVo = new SipLogVo();
            sipLogVo.setCellId("cellId");
            sipLogVo.setEventName("eventName");
            sipLogVo.setEventTime("eventTime");
            sipLogVo.setLocaleType(SipLogVo.LOCALE_TYPE_DOMESTIC);
            sipLogVo.setLocalHostName("localHostName");
            sipLogVo.setPartyA("A");
            sipLogVo.setPartyB("B");
            sipLogVo.setPartyPsap("partyPsap");
            sipLogVo.setPHeader("pHeader");
            sipLogVo.setPHeaderAttr("pHeaderAttr");
            sipLogVo.setReleaseCause("releaseCause");
            sipLogVo.setResponseCode(123);
            sipLogVo.setSourceDomain("sourceDomain");
            dao.insert(sipLogVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Logger logger = Logger.getLogger("debug");
    private static SipLogDao instance;

    private DataSource ds;

    private SipLogDao() {
        try {
            ds = DataSourceFactory.getDataSource();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SipLogDao getInstance() {
        if (instance == null) {
            instance = new SipLogDao();
        }
        return instance;
    }

    public void insert(SipLogVo sipLogVo) {
//        System.out.println("insert");
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "insert into sip_log (SOURCE_DOMAIN,LOCAL_HOSTNAME,EVENT_NAME,EVENT_TIME,PARTY_A,PARTY_B,CELL_ID,ABROAD_TYPE,PARTY_PSAP,RELEASE_CAUSE,RESPONSE_CODE,P_HEADER,P_HEADER_ATTRIBUTE,P_HEADER_ACCESS,CALL_ID)"
                + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            conn = ds.getConnection();
            ps = conn.prepareStatement(sql);
            int idx = 0;
            ps.setString(++idx, sipLogVo.getSourceDomain());
            ps.setString(++idx, sipLogVo.getLocalHostName());
            ps.setString(++idx, sipLogVo.getEventName());
            ps.setString(++idx, sipLogVo.getEventTime());
            ps.setString(++idx, sipLogVo.getPartyA());
            ps.setString(++idx, sipLogVo.getPartyB());
            ps.setString(++idx, sipLogVo.getCellId());
            ps.setInt(++idx, sipLogVo.getLocaleType());
            ps.setString(++idx, sipLogVo.getPartyPsap());
            ps.setString(++idx, sipLogVo.getReleaseCause());
            ps.setInt(++idx, sipLogVo.getResponseCode());
            ps.setString(++idx, sipLogVo.getPHeader());
            ps.setString(++idx, sipLogVo.getPHeaderAttr());
            ps.setString(++idx, sipLogVo.getpHeaderAccess());
            ps.setString(++idx, sipLogVo.getCallId());

            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                logger.error("", e);

            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.error("", e);

            }
        }
    }

}
