package com.mtsealove.github.boxlinker_driver.Restful;

public class ResLogin {
    String Name, Phone;

    public ResLogin(String name, String phone) {
        Name = name;
        Phone = phone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
