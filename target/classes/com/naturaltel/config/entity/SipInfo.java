package com.naturaltel.config.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "NTSIP")
@XmlAccessorType(XmlAccessType.FIELD)
public class SipInfo {

    @XmlAttribute(name = "proxy_name")
    private String proxyName;
    
    @XmlAttribute(name = "address")
    private String address;
    
    @XmlAttribute(name = "port")
    private int port;

    @XmlAttribute(name = "transport")
    private String transport;
    
    @XmlAttribute(name = "domain_name")
    private String domainName;
    
    @XmlAttribute(name = "path_name")
    private String pathName;

    public String getProxyName() {
        return proxyName;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public String getTransport() {
        return transport;
    }

    public String getDomainName() {
        return domainName;
    }

    public String getPathName() {
        return pathName;
    }

    public void setProxyName(String proxyName) {
        this.proxyName = proxyName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SipInfo [proxyName=");
        builder.append(proxyName);
        builder.append(", address=");
        builder.append(address);
        builder.append(", port=");
        builder.append(port);
        builder.append(", transport=");
        builder.append(transport);
        builder.append(", domainName=");
        builder.append(domainName);
        builder.append(", pathName=");
        builder.append(pathName);
        builder.append("]");
        return builder.toString();
    }
    
}
