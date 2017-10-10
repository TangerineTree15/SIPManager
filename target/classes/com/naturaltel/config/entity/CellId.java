/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naturaltel.config.entity;

/**
 *
 * @author LaLaTsai
 */
public class CellId {

    private String cellIdFirstPart;
    private String cellIdLastPart;
    private String cellIdFirstPartFormated;
    private String cellIdLastPartFormated;

    public String getCellIdFirstPart() {
        return cellIdFirstPart;
    }

    public void setCellIdFirstPart(String cellIdFirstPart) {
        this.cellIdFirstPart = cellIdFirstPart;
    }

    public String getCellIdLastPart() {
        return cellIdLastPart;
    }

    public void setCellIdLastPart(String cellIdLastPart) {
        this.cellIdLastPart = cellIdLastPart;
    }

    public String getCellIdFirstPartFormated() {
        return cellIdFirstPartFormated;
    }

    public void setCellIdFirstPartFormated(String cellIdFirstPartFormated) {
        this.cellIdFirstPartFormated = cellIdFirstPartFormated;
    }

    public String getCellIdLastPartFormated() {
        return cellIdLastPartFormated;
    }

    public void setCellIdLastPartFormated(String cellIdLastPartFormated) {
        this.cellIdLastPartFormated = cellIdLastPartFormated;
    }

    @Override
    public String toString() {
        return "CellId{" + "cellIdFirstPart=" + cellIdFirstPart + ", cellIdLastPart=" + cellIdLastPart + ", cellIdFirstPartFormated=" + cellIdFirstPartFormated + ", cellIdLastPartFormated=" + cellIdLastPartFormated + '}';
    }

    
    


}
