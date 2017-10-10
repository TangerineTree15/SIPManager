package com.naturaltel.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.PropertyConfigurator;

import com.naturaltel.cache.Config;
import com.naturaltel.config.entity.CityCellInfo;
import com.naturaltel.config.entity.RootConfig;
import org.apache.log4j.Logger;

public class ConfigLoader {

    private static final Logger mLogger = Logger.getLogger("debug");

    //For test
    public static void main(String[] args) throws Exception {
//        Config.setXmlConfigPath("C:/Users/Jacky/FET_VoWiFi_EMR/Code/sipgw/src/main/java/conf/ntsip_config.xml");
        ConfigLoader.loadConfiguration();
    }

    private static final String DEFAULT_XML_FILE_PATH = "conf/ntsip_config.xml";
    private static final String DEFAULT_SIPSTACK_PROP_FILE_PATH = "conf/sipStack.properties";
    private static final String DEFAULT_LOG4J_PROP_FILE_PATH = "conf/log4j.properties";

    public static void loadConfiguration() throws Exception {
        String path = null;
        InputStream is = null;
        try {
            if (Config.getXmlConfigPath() != null && !"".equals(Config.getXmlConfigPath())) {
                path = Config.getXmlConfigPath();
                is = new FileInputStream(new File(path));
            } else {
                path = DEFAULT_XML_FILE_PATH;
                is = ConfigLoader.class.getClassLoader().getResourceAsStream(path);
            }

            JAXBContext jaxbContext = JAXBContext.newInstance(RootConfig.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            RootConfig rootConfig = (RootConfig) unmarshaller.unmarshal(is);

            Config.setSipInfo(rootConfig.getSipInfo());
            Config.setDbInfo(rootConfig.getDbInfo());
            Config.setUdpServerInfo(rootConfig.getUdpServerInfo());
            Config.setCellNetworkInfo(rootConfig.getCellNetworkInfo());
            Config.setSipStackFilePath(rootConfig.getSipStackFilePath());
            Config.setPsapInfo(rootConfig.getPsapInfo());
            Config.setLog4jPropFilePath(rootConfig.getLog4jPropFilePath());
            Config.setEmergencyCodes(rootConfig.getEmergencyCodes());
            Config.setSosUri(rootConfig.getSosUri());
            is.close();

            //load sip stack
            Properties sipStack = new Properties();
            String sipStackFilePath = rootConfig.getSipStackFilePath();
            if (sipStackFilePath != null && !"".equals(sipStackFilePath)) {
                path = rootConfig.getSipStackFilePath();
                is = new FileInputStream(new File(path));
            } else {
                path = DEFAULT_SIPSTACK_PROP_FILE_PATH;
                is = ConfigLoader.class.getClassLoader().getResourceAsStream(path);
            }
            sipStack.load(is);
            Config.setSipStack(sipStack);
            is.close();

            //initialize log4j TODO
            Properties log4jProp = new Properties();
            String log4jPropFilePath = rootConfig.getLog4jPropFilePath();
            if (rootConfig.getLog4jPropFilePath() != null && !"".equals(log4jPropFilePath)) {
                path = rootConfig.getLog4jPropFilePath();
                is = new FileInputStream(new File(path));
            } else {
                path = DEFAULT_LOG4J_PROP_FILE_PATH;
                is = ConfigLoader.class.getClassLoader().getResourceAsStream(path);
            }
            log4jProp.load(is);
            PropertyConfigurator.configure(log4jProp);

            mLogger.info("");
            mLogger.info(rootConfig);
            reloadCityCellInfo();
        } catch (JAXBException e) {

            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;

        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public static void reloadAllConfiguration() throws Exception {
        loadConfiguration();
        mLogger.info("Reloaded LRF configuration...");

    }

    private static void reloadCityCell(Connection conn) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        HashMap<String, ArrayList<CityCellInfo>> cellIdMap = new HashMap<String, ArrayList<CityCellInfo>>();
        CityCellInfo vo = null;
        try {
            String sql = "select CELLID_MIN,CELLID_MAX,CITY_CELLID_CODE from CITY_CELLID_INFO ";
            String cellIdPrefixTwoDgt = null;
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                vo = new CityCellInfo();
                vo.setCityCode(rs.getString("CITY_CELLID_CODE"));
                vo.setCellIdMin(rs.getString("CELLID_MIN"));
                vo.setCellIdMax(rs.getString("CELLID_MAX"));

                cellIdPrefixTwoDgt = rs.getString("CELLID_MIN").substring(0, 1);
                if (!cellIdMap.containsKey(cellIdPrefixTwoDgt)) {
                    ArrayList<CityCellInfo> cellList = new ArrayList<CityCellInfo>();

                    cellList.add(vo);
                    cellIdMap.put(cellIdPrefixTwoDgt, cellList);
                } else {
                    ArrayList<CityCellInfo> cellList = cellIdMap.get(cellIdPrefixTwoDgt);
                    cellList.add(vo);
                }
            }

            Config.setCityCellMap(cellIdMap);
        } catch (Exception e) {
            throw e;

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                rs = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                pstmt = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private static void reloadLacCity(Connection conn) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        HashMap<String, String> lacMap = new HashMap<String, String>();
        try {
            String sql = "select CITY_CELLID_CODE,LAC_CODE from CITY_LAC_MAPPING";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                if (!lacMap.containsKey(rs.getString("LAC_CODE"))) {
                    lacMap.put(rs.getString("LAC_CODE"), rs.getString("CITY_CELLID_CODE"));
                }
            }

            Config.setLacCityMap(lacMap);

        } catch (Exception e) {
            throw e;

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                rs = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                pstmt = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private static void reloadCityNea(Connection conn) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        HashMap<String, String> neaMap = new HashMap<String, String>();

        try {
            String sql = "select CITY_CELLID_CODE,NEA_CODE from CITY_NEA_MAPPING";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                if (!neaMap.containsKey(rs.getString("CITY_CELLID_CODE"))) {
                    neaMap.put(rs.getString("CITY_CELLID_CODE"), rs.getString("NEA_CODE"));
                }
            }

            Config.setCityNeaMap(neaMap);

        } catch (Exception e) {
            throw e;

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                rs = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                pstmt = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void reloadCityCellInfo() throws Exception {
        Connection conn = null;

        try {
            Class.forName(Config.getDbInfo().getDriverClass()).newInstance();
            conn = DriverManager.getConnection(Config.getDbInfo().getConnectionUrl(),
                    Config.getDbInfo().getUserId(),
                    Config.getDbInfo().getUserPassword());

            reloadCityCell(conn);
            reloadLacCity(conn);
            reloadCityNea(conn);

        } catch (SQLException e) {
            //TODO alarm
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {

            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
