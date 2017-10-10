package com.naturaltel.sip.gw.entity;

public class CellInfo {

    private String headerName;
    
    private String cellIdAttrName;
    
    private String accessTypeName;
    
    private String cellId;

    public String getAccessTypeName() {
        return accessTypeName;
    }

    public void setAccessTypeName(String accessTypeName) {
        this.accessTypeName = accessTypeName;
    }
    
    
    

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getCellIdAttrName() {
        return cellIdAttrName;
    }

    public void setCellIdAttrName(String cellIdAttrName) {
        this.cellIdAttrName = cellIdAttrName;
    }

    public String getCellId() {
        return cellId;
    }

    public void setCellId(String cellId) {
        this.cellId = cellId;
    }

    @Override
    public String toString() {
        return "CellInfo{" + "headerName=" + headerName + ", cellIdAttrName=" + cellIdAttrName + ", accessTypeName=" + accessTypeName + ", cellId=" + cellId + '}';
    }


    
    
    
    
}
