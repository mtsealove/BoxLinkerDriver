package com.mtsealove.github.boxlinker_driver.Restful;

public class ReqOrderStatus {
    String OrderID;
    int Status;

    public ReqOrderStatus(String orderID, int status) {
        OrderID = orderID;
        Status = status;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}
