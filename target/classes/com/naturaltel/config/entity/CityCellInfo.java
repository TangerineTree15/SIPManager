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
public class CityCellInfo {

    private String cellIdMin;
    private String cellIdMax;
    private String cityCode;

    public String getCellIdMin() {
        return cellIdMin;
    }

    public void setCellIdMin(String cellIdMin) {
        this.cellIdMin = cellIdMin;
    }

    public String getCellIdMax() {
        return cellIdMax;
    }

    public void setCellIdMax(String cellIdMax) {
        this.cellIdMax = cellIdMax;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    @Override
    public String toString() {
        return "CityCellInfo{" + "cellIdMin=" + cellIdMin + ", cellIdMax=" + cellIdMax + ", cityCode=" + cityCode + '}';
    }

}
