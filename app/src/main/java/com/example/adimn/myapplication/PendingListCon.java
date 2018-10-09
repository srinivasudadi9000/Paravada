package com.example.adimn.myapplication;

/**
 * Created by Adimn on 11-07-2018.
 */

public class PendingListCon {


    private String image, amount, units, customer_city, product_name, cat_name, customer_name,pid,mobile;

    public PendingListCon(String img, String product_name, String cat_name, String amount,  String units, String city, String name,String pid,String mobile) {
        this.image = img;
        this.amount = amount;
        this.units = units;
        //this.customer_city = city;
        this.product_name = product_name;
        this.cat_name = cat_name;
        this.customer_name = name;
        this.pid=pid;
        this.mobile=mobile;
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

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }


    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /* public String getCustomer_city() {
                return customer_city;
            }

            public void setCustomer_city(String customer_city) {
                this.customer_city = customer_city;
            }
        */
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
