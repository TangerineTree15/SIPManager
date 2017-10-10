/*
 * @(#)MonitCheckAliveConfiguration.java 2015年5月27日
 *
 * Copyright (C) 2015 Naturaltel Communication Co., LTD.
 * All right reserved.
 */
package com.naturaltel.config.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "COMMANDS")
@XmlAccessorType(XmlAccessType.FIELD)
public class UdpCmds {

    @XmlElement(name = "ACK")
    public String ackCmd;

    @XmlElement(name = "PING")
    public String pingCmd;

    @XmlElement(name = "RELOAD_CITY_CELL")
    public String reloadCityCellCmd;

    @XmlElement(name = "RELOAD_CONFIG")
    public String reloadConfigCmd;

    public String getReloadConfigCmd() {
        return reloadConfigCmd;
    }

    public void setReloadConfigCmd(String reloadConfigCmd) {
        this.reloadConfigCmd = reloadConfigCmd;
    }

    public String getAckCmd() {
        return ackCmd;
    }

    public void setAckCmd(String ackCmd) {
        this.ackCmd = ackCmd;
    }

    public String getPingCmd() {
        return pingCmd;
    }

    public void setPingCmd(String pingCmd) {
        this.pingCmd = pingCmd;
    }

    public String getReloadCityCellCmd() {
        return reloadCityCellCmd;
    }

    public void setReloadCityCellCmd(String reloadCityCellCmd) {
        this.reloadCityCellCmd = reloadCityCellCmd;
    }

    @Override
    public String toString() {
        return "UdpCmds{" + "ackCmd=" + ackCmd + ", pingCmd=" + pingCmd + ", reloadCityCellCmd=" + reloadCityCellCmd + ", reloadConfigCmd=" + reloadConfigCmd + '}';
    }

}
