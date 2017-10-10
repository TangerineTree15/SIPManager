package com.naturaltel.config.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CELL_ID_INFO")
@XmlAccessorType(XmlAccessType.FIELD)
public class CellNetworkInfo {

    @XmlAttribute(name = "cell_msg_header_1")
    private String headerName1;

    @XmlAttribute(name = "cell_msg_header_2")
    private String headerName2;

    @XmlAttribute(name = "cell_msg_header_3")
    private String headerName3;

    @XmlAttribute(name = "cell_msg_header_4")
    private String headerName4;



    @XmlAttribute(name = "cell_id_attr_2G")
    private String cellIdParamKey2G;

    @XmlAttribute(name = "cell_id_attr_utran")
    private String cellIdParamKeyUtran;

    @XmlAttribute(name = "access_type_4G")
    private String accessType4G;

    @XmlAttribute(name = "access_type_3G")
    private String accessType3G;

    @XmlAttribute(name = "access_type_2G")
    private String accessType2G;

    public String getCellIdParamKey2G() {
        return cellIdParamKey2G;
    }

    public void setCellIdParamKey2G(String cellIdParamKey2G) {
        this.cellIdParamKey2G = cellIdParamKey2G;
    }

    public String getAccessType4G() {
        return accessType4G;
    }

    public void setAccessType4G(String accessType4G) {
        this.accessType4G = accessType4G;
    }

    public String getAccessType3G() {
        return accessType3G;
    }

    public void setAccessType3G(String accessType3G) {
        this.accessType3G = accessType3G;
    }

    public String getAccessType2G() {
        return accessType2G;
    }

    public void setAccessType2G(String accessType2G) {
        this.accessType2G = accessType2G;
    }

    
    
    
    
    public String getCellIdParamKeyUtran() {
        return cellIdParamKeyUtran;
    }

    public void setCellIdParamKeyUtran(String cellIdParamKeyUtran) {
        this.cellIdParamKeyUtran = cellIdParamKeyUtran;
    }

    public String getHeaderName1() {
        return headerName1;
    }

    public void setHeaderName1(String headerName1) {
        this.headerName1 = headerName1;
    }

    public String getHeaderName2() {
        return headerName2;
    }

    public void setHeaderName2(String headerName2) {
        this.headerName2 = headerName2;
    }

    public String getHeaderName3() {
        return headerName3;
    }

    public void setHeaderName3(String headerName3) {
        this.headerName3 = headerName3;
    }

    public String getHeaderName4() {
        return headerName4;
    }

    public void setHeaderName4(String headerName4) {
        this.headerName4 = headerName4;
    }

    @Override
    public String toString() {
        return "CellNetworkInfo{" + "headerName1=" + headerName1 + ", headerName2=" + headerName2 + ", headerName3=" + headerName3 + ", headerName4=" + headerName4 + ", cellIdParamKey2G=" + cellIdParamKey2G + ", cellIdParamKeyUtran=" + cellIdParamKeyUtran + ", accessType4G=" + accessType4G + ", accessType3G=" + accessType3G + ", accessType2G=" + accessType2G + '}';
    }





    
    
}
