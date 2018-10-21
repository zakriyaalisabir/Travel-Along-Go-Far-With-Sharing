package com.zakriyaalisabir.travelalonggofarwithsharing;

public class UserInfo {

    public String name;
    public String email;
    public String phone;
    public String cnic;
    public String city;
    public String password;
    public String carName;
    public String carModel;
    public String carNumber;


    UserInfo() {
    }

    public UserInfo(String name, String email, String phone, String cnic, String city, String password, String carName, String carModel, String carNumber) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.cnic = cnic;
        this.city = city;
        this.password = password;
        this.carName = carName;
        this.carModel = carModel;
        this.carNumber = carNumber;
    }

}
