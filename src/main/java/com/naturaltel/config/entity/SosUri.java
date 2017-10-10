package com.naturaltel.config.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "SOS_URI")
@XmlAccessorType(XmlAccessType.FIELD)
public class SosUri {

    @XmlValue
    private String uri;

    @XmlAttribute(name = "code")
    private String code;

    @XmlAttribute(name = "urn_format")
    private String urnFormat;
    
    @XmlAttribute(name = "treatAs")
    private String treatAs;

    public String getTreatAs() {
        return treatAs;
    }

    public void setTreatAs(String treatAs) {
        this.treatAs = treatAs;
    }
    
    
    
    
    

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrnFormat() {
        return urnFormat;
    }

    public void setUrnFormat(String urnFormat) {
        this.urnFormat = urnFormat;
    }

    @Override
    public String toString() {
        return "SosUri{" + "uri=" + uri + ", code=" + code + ", urnFormat=" + urnFormat + ", treatAs=" + treatAs + '}';
    }


    
    
    



}
