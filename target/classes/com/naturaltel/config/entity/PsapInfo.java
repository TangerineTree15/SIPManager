package com.naturaltel.config.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "PSAP_INFO")
@XmlAccessorType(XmlAccessType.FIELD)
public class PsapInfo {

    @XmlAttribute(name = "operator_code_4g_fet")
    private String operatorCodeFet4g;
    
    @XmlAttribute(name = "operator_code_3g_fet")
    private String operatorCodeFet3g;
    
    @XmlAttribute(name = "operator_code_taiwan")
    private String operatorCodeTw;
    
    @XmlAttribute(name = "add_prefix_countrycode")
    private String addPrefixCountryCode;
    
    @XmlAttribute(name = "country_code_taiwan")
    private String countryCodeTw;
        
    @XmlAttribute(name = "default_4g_psap")
    private String defaultPsap4g;
    
    @XmlAttribute(name = "default_4g_112_psap")
    private String defaultPsap4g112;
    
    @XmlAttribute(name = "default_3g_psap")
    private String defaultPsap3g;
    
    @XmlAttribute(name = "default_3g_112_psap")
    private String defaultPsap3g112;

    public String getOperatorCodeFet4g() {
        return operatorCodeFet4g;
    }

    public void setOperatorCodeFet4g(String operatorCodeFet4g) {
        this.operatorCodeFet4g = operatorCodeFet4g;
    }

    public String getOperatorCodeFet3g() {
        return operatorCodeFet3g;
    }

    public void setOperatorCodeFet3g(String operatorCodeFet3g) {
        this.operatorCodeFet3g = operatorCodeFet3g;
    }

    public String getOperatorCodeTw() {
        return operatorCodeTw;
    }

    public void setOperatorCodeTw(String operatorCodeTw) {
        this.operatorCodeTw = operatorCodeTw;
    }

    public String getAddPrefixCountryCode() {
        return addPrefixCountryCode;
    }

    public void setAddPrefixCountryCode(String addPrefixCountryCode) {
        this.addPrefixCountryCode = addPrefixCountryCode;
    }

    public String getCountryCodeTw() {
        return countryCodeTw;
    }

    public void setCountryCodeTw(String countryCodeTw) {
        this.countryCodeTw = countryCodeTw;
    }

    public String getDefaultPsap4g() {
        return defaultPsap4g;
    }

    public void setDefaultPsap4g(String defaultPsap4g) {
        this.defaultPsap4g = defaultPsap4g;
    }

    public String getDefaultPsap4g112() {
        return defaultPsap4g112;
    }

    public void setDefaultPsap4g112(String defaultPsap4g112) {
        this.defaultPsap4g112 = defaultPsap4g112;
    }

    public String getDefaultPsap3g() {
        return defaultPsap3g;
    }

    public void setDefaultPsap3g(String defaultPsap3g) {
        this.defaultPsap3g = defaultPsap3g;
    }

    public String getDefaultPsap3g112() {
        return defaultPsap3g112;
    }

    public void setDefaultPsap3g112(String defaultPsap3g112) {
        this.defaultPsap3g112 = defaultPsap3g112;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PsapInfo [operatorCodeFet4g=");
        builder.append(operatorCodeFet4g);
        builder.append(", operatorCodeFet3g=");
        builder.append(operatorCodeFet3g);
        builder.append(", operatorCodeTw=");
        builder.append(operatorCodeTw);
        builder.append(", addPrefixCountryCode=");
        builder.append(addPrefixCountryCode);
        builder.append(", countryCodeTw=");
        builder.append(countryCodeTw);
        builder.append(", defaultPsap4g=");
        builder.append(defaultPsap4g);
        builder.append(", defaultPsap4g112=");
        builder.append(defaultPsap4g112);
        builder.append(", defaultPsap3g=");
        builder.append(defaultPsap3g);
        builder.append(", defaultPsap3g112=");
        builder.append(defaultPsap3g112);
        builder.append("]");
        return builder.toString();
    }


}
