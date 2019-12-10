package com.mtsealove.github.boxlinker_driver.Restful;

public class ReqTakeOrder {
    String OrderID, Phone;
    boolean Start;

    public ReqTakeOrder(String orderID, String phone, boolean start) {
        OrderID = orderID;
        Phone = phone;
        Start = start;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public boolean isStart() {
        return Start;
    }

    public void setStart(boolean start) {
        Start = start;
    }
}
