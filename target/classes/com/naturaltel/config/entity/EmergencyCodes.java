package com.naturaltel.config.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "EMERGENCY_CODES")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmergencyCodes {

    @XmlAttribute(name = "country_code_prefix")
    private String countryCodePrefix;
    
    @XmlElement(name = "EMERGENCY_CODE")
    private List<String> codes;

    public String getCountryCodePrefix() {
        return countryCodePrefix;
    }

    public List<String> getCodes() {
        return codes;
    }

    public void setCountryCodePrefix(String countryCodePrefix) {
        this.countryCodePrefix = countryCodePrefix;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }

    @Override
    public String toString() {
        return "EmergencyCodes [countryCodePrefix=" + countryCodePrefix + ", codes=" + codes + "]";
    }
    
}
