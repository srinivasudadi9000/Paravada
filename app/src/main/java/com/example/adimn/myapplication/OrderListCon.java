package com.example.adimn.myapplication;

/**
 * Created by Adimn on 11-07-2018.
 */

public class OrderListCon {

    private String image, amount, city,units, product_id, product_name, cat_name,customer_name,date,mobile;

    public OrderListCon(String img, String product_name, String cat_name, String amount, String city, String units, String pid,String name,String date,String mobile) {
        this.image = img;
        this.amount = amount;
        this.city = city;
        this.units=units;
        this.product_id = pid;
        this.product_name = product_name;
        this.cat_name = cat_name;
        this.customer_name = name;
        this.date = date;
        this.mobile = mobile;


    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;


    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;

    }

}

