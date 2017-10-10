package com.naturaltel.config.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Configuration")
@XmlAccessorType(XmlAccessType.FIELD)
public class RootConfig {

    @XmlElement(name = "NTSIP")
    private SipInfo sipInfo;
    
    @XmlElement(name = "NTSIPSTACK_PROPERTIES")
    private String sipStackFilePath;
    
    @XmlElement(name = "DATABASE_PROPERTIES")
    private DBInfo dbInfo;
    
    @XmlElement(name = "CELL_ID_INFO")
    private CellNetworkInfo cellNetworkInfo;
    
    @XmlElement(name = "UDP_SERVER_INFO")
    private UdpServerInfo udpServerInfo;
    
    @XmlElement(name = "PSAP_INFO")
    private PsapInfo psapInfo;
    
    @XmlElement(name = "LOG4J_PROPERTIES")
    private String log4jPropFilePath;
    
    @XmlElement(name = "EMERGENCY_CODES")
    private EmergencyCodes emergencyCodes;
    
    @XmlElement(name = "SOS_URI")
    private SosUri sosUri;
    
    public SipInfo getSipInfo() {
        return sipInfo;
    }

    public void setSipInfo(SipInfo sipInfo) {
        this.sipInfo = sipInfo;
    }

    public String getSipStackFilePath() {
        return sipStackFilePath;
    }

    public void setSipStackFilePath(String sipStackFilePath) {
        this.sipStackFilePath = sipStackFilePath;
    }

    public DBInfo getDbInfo() {
        return dbInfo;
    }

    public void setDbInfo(DBInfo dbInfo) {
        this.dbInfo = dbInfo;
    }

    public CellNetworkInfo getCellNetworkInfo() {
        return cellNetworkInfo;
    }

    public void setCellNetworkInfo(CellNetworkInfo cellNetworkInfo) {
        this.cellNetworkInfo = cellNetworkInfo;
    }

    public UdpServerInfo getUdpServerInfo() {
        return udpServerInfo;
    }

    public void setUdpServerInfo(UdpServerInfo udpServerInfo) {
        this.udpServerInfo = udpServerInfo;
    }
    
    public PsapInfo getPsapInfo() {
        return psapInfo;
    }

    public void setPsapInfo(PsapInfo psapInfo) {
        this.psapInfo = psapInfo;
    }

    public String getLog4jPropFilePath() {
        return log4jPropFilePath;
    }

    public void setLog4jPropFilePath(String log4jPropFilePath) {
        this.log4jPropFilePath = log4jPropFilePath;
    }

    public EmergencyCodes getEmergencyCodes() {
        return emergencyCodes;
    }

    public void setEmergencyCodes(EmergencyCodes emergencyCodes) {
        this.emergencyCodes = emergencyCodes;
    }

    public SosUri getSosUri() {
        return sosUri;
    }

    public void setSosUri(SosUri sosUri) {
        this.sosUri = sosUri;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("RootConfig [sipInfo=");
        builder.append(sipInfo);
        builder.append(", sipStackFilePath=");
        builder.append(sipStackFilePath);
        builder.append(", dbInfo=");
        builder.append(dbInfo);
        builder.append(", cellNetworkInfo=");
        builder.append(cellNetworkInfo);
        builder.append(", udpServerInfo=");
        builder.append(udpServerInfo);
        builder.append(", psapInfo=");
        builder.append(psapInfo);
        builder.append(", log4jPropFilePath=");
        builder.append(log4jPropFilePath);
        builder.append(", emergencyCodes=");
        builder.append(emergencyCodes);
        builder.append(", sosUri=");
        builder.append(sosUri);
        builder.append("]");
        return builder.toString();
    }


    
}
