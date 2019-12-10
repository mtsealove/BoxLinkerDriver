package com.mtsealove.github.boxlinker_driver.Restful;

public class ResOrder {
    String OrderID, StdAddr, DstAddr, DstPhone, StdPhone, ImagePath, ArrPrdtTm, CorpNm;
    int Size;
    double Weight;
    boolean Start;

    public ResOrder(String orderID, String stdAddr, String dstAddr, String dstPhone, String stdPhone, String imagePath, String arrPrdtTm, String corpNm, int size, double weight, boolean start) {
        OrderID = orderID;
        StdAddr = stdAddr;
        DstAddr = dstAddr;
        DstPhone = dstPhone;
        StdPhone = stdPhone;
        ImagePath = imagePath;
        ArrPrdtTm = arrPrdtTm;
        CorpNm = corpNm;
        Size = size;
        Weight = weight;
        Start = start;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getStdAddr() {
        return StdAddr;
    }

    public void setStdAddr(String stdAddr) {
        StdAddr = stdAddr;
    }

    public String getDstAddr() {
        return DstAddr;
    }

    public void setDstAddr(String dstAddr) {
        DstAddr = dstAddr;
    }

    public int getSize() {
        return Size;
    }

    public void setSize(int size) {
        Size = size;
    }

    public double getWeight() {
        return Weight;
    }

    public void setWeight(double weight) {
        Weight = weight;
    }

    public boolean isStart() {
        return Start;
    }

    public void setStart(boolean start) {
        Start = start;
    }

    public String getDstPhone() {
        return DstPhone;
    }

    public void setDstPhone(String dstPhone) {
        DstPhone = dstPhone;
    }

    public String getStdPhone() {
        return StdPhone;
    }

    public void setStdPhone(String stdPhone) {
        StdPhone = stdPhone;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public String getArrPrdtTm() {
        return ArrPrdtTm;
    }

    public void setArrPrdtTm(String arrPrdtTm) {
        ArrPrdtTm = arrPrdtTm;
    }

    public String getCorpNm() {
        return CorpNm;
    }

    public void setCorpNm(String corpNm) {
        CorpNm = corpNm;
    }
}
