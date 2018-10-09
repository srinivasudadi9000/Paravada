package com.example.adimn.myapplication;

/**
 * Created by Adimn on 16-07-2018.
 */

public class PurchaseListCon {

    private String image, amount, city,units, product_name, cat_name,customer_name;

    public PurchaseListCon(String img, String product_name, String cat_name, String amount, String city, String units,String name) {
        this.image = img;
        this.amount = amount;
        this.city = city;
        this.units=units;
        this.product_name = product_name;
        this.cat_name = cat_name;
        this.customer_name = name;


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


    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;

    }

}

