package com.naturaltel.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import com.naturaltel.config.entity.CellNetworkInfo;
import com.naturaltel.config.entity.CityCellInfo;
import com.naturaltel.config.entity.DBInfo;
import com.naturaltel.config.entity.EmergencyCodes;
import com.naturaltel.config.entity.PsapInfo;
import com.naturaltel.config.entity.SipInfo;
import com.naturaltel.config.entity.SosUri;
import com.naturaltel.config.entity.UdpServerInfo;

public class Config {

    private static SipInfo sipInfo;

    private static String sipStackFilePath;

    private static String log4jPropFilePath;

    private static DBInfo dbInfo;

    private static CellNetworkInfo cellNetworkInfo;

    private static UdpServerInfo udpServerInfo;

    private static PsapInfo psapInfo;

    private static Properties sipStack;

    private static String xmlConfigPath;

    private static HashMap<String, ArrayList<CityCellInfo>> cityCellMap;

    private static HashMap<String, String> lacCityMap;
    private static HashMap<String, String> cityNeaMap;

    private static EmergencyCodes emergencyCodes;

    private static SosUri sosUri;

    public static SipInfo getSipInfo() {
        return sipInfo;
    }

    public static void setSipInfo(SipInfo sipInfo) {
        Config.sipInfo = sipInfo;
    }

    public static String getSipStackFilePath() {
        return sipStackFilePath;
    }

    public static void setSipStackFilePath(String sipStackFilePath) {
        Config.sipStackFilePath = sipStackFilePath;
    }

    public static DBInfo getDbInfo() {
        return dbInfo;
    }

    public static void setDbInfo(DBInfo dbInfo) {
        Config.dbInfo = dbInfo;
    }

    public static CellNetworkInfo getCellNetworkInfo() {
        return cellNetworkInfo;
    }

    public static void setCellNetworkInfo(CellNetworkInfo cellNetworkInfo) {
        Config.cellNetworkInfo = cellNetworkInfo;
    }

    public static UdpServerInfo getUdpServerInfo() {
        return udpServerInfo;
    }

    public static void setUdpServerInfo(UdpServerInfo udpServerInfo) {
        Config.udpServerInfo = udpServerInfo;
    }

    public static PsapInfo getPsapInfo() {
        return psapInfo;
    }

    public static void setPsapInfo(PsapInfo psapInfo) {
        Config.psapInfo = psapInfo;
    }

    public static Properties getSipStack() {
        return sipStack;
    }

    public static void setSipStack(Properties sipStack) {
        Config.sipStack = sipStack;
    }

    public static String getXmlConfigPath() {
        return xmlConfigPath;
    }

    public static void setXmlConfigPath(String xmlConfigPath) {
        Config.xmlConfigPath = xmlConfigPath;
    }

    public static HashMap<String, ArrayList<CityCellInfo>> getCityCellMap() {
        return cityCellMap;
    }

    public static void setCityCellMap(HashMap<String, ArrayList<CityCellInfo>> cityCellMap) {
        Config.cityCellMap = cityCellMap;
    }

    public static String getLog4jPropFilePath() {
        return log4jPropFilePath;
    }

    public static void setLog4jPropFilePath(String log4jPropFilePath) {
        Config.log4jPropFilePath = log4jPropFilePath;
    }

    public static EmergencyCodes getEmergencyCodes() {
        return emergencyCodes;
    }

    public static void setEmergencyCodes(EmergencyCodes emergencyCodes) {
        Config.emergencyCodes = emergencyCodes;
    }

    public static SosUri getSosUri() {
        return sosUri;
    }

    public static void setSosUri(SosUri sosUri) {
        Config.sosUri = sosUri;
    }

    public static HashMap<String, String> getLacCityMap() {
        return lacCityMap;
    }

    public static void setLacCityMap(HashMap<String, String> lacCityMap) {
        Config.lacCityMap = lacCityMap;
    }

    public static HashMap<String, String> getCityNeaMap() {
        return cityNeaMap;
    }

    public static void setCityNeaMap(HashMap<String, String> cityNeaMap) {
        Config.cityNeaMap = cityNeaMap;
    }
    
    
    

}
