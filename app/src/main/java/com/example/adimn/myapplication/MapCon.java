package com.example.adimn.myapplication;

/**
 * Created by Adimn on 02-08-2018.
 */

public class MapCon {

    private String image,product_name, cat_name, units , price, product_desc;


    public MapCon(String image, String product_name, String cat_name, String units,String price, String product_desc) {
        this.image = image;
        this.product_name = product_name;
        this.cat_name = cat_name;
        this.units=units;
        this.price = price;
        this.product_desc = product_desc;



    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
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
}

