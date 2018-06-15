package com.sinovoice.hcicloud.nettysocketiofirst.vo;

public class Notice {

    private int wjyyNum;
    private int fsrNum;
    private int fwtdNum;
    private String ctiCode;
    private String tipUrl;

    public String getCtiCode() {
        return ctiCode;
    }

    public void setCtiCode(String ctiCode) {
        this.ctiCode = ctiCode;
    }

    public int getWjyyNum() {
        return wjyyNum;
    }

    public void setWjyyNum(int wjyyNum) {
        this.wjyyNum = wjyyNum;
    }

    public int getFsrNum() {
        return fsrNum;
    }

    public void setFsrNum(int fsrNum) {
        this.fsrNum = fsrNum;
    }

    public int getFwtdNum() {
        return fwtdNum;
    }

    public void setFwtdNum(int fwtdNum) {
        this.fwtdNum = fwtdNum;
    }

    public String getTipUrl() {
        return tipUrl;
    }

    public void setTipUrl(String tipUrl) {
        this.tipUrl = tipUrl;
    }

    @Override
    public String toString() {
        return "Notice{" +
                "wjyyNum=" + wjyyNum +
                ", fsrNum=" + fsrNum +
                ", fwtdNum=" + fwtdNum +
                ", ctiCode='" + ctiCode + '\'' +
                ", tipUrl='" + tipUrl + '\'' +
                '}';
    }
}
