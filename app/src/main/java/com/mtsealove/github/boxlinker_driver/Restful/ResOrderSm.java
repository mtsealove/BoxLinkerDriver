package com.mtsealove.github.boxlinker_driver.Restful;

public class ResOrderSm {
    String OrderID, StdAddr, DstAddr, ArrPrdtTm;
    int Size;
    double Weight;
    boolean Start;

    public ResOrderSm(String orderID, String stdAddr, String dstAddr, String arrPrdtTm, int size, double weight, boolean start) {
        OrderID = orderID;
        StdAddr = stdAddr;
        DstAddr = dstAddr;
        ArrPrdtTm = arrPrdtTm;
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

    public String getArrPrdtTm() {
        return ArrPrdtTm;
    }

    public void setArrPrdtTm(String arrPrdtTm) {
        ArrPrdtTm = arrPrdtTm;
    }
}
