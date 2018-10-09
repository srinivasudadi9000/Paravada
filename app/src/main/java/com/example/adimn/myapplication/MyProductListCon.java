package com.example.adimn.myapplication;

/**
 * Created by pari on 07-06-2018.
 */

public class MyProductListCon {

    private String image, quantity, price, product_desc,units, product_name, cat_name,date,product_id;

    public MyProductListCon( String image, String product_name, String cat_name, String quantity, String price, String product_desc, String units, String date,String product_id) {
        this.image = image;
        this.quantity = quantity;
        this.price = price;
        this.product_desc = product_desc;
        this.units=units;
        this.product_name = product_name;
        this.cat_name = cat_name;
        this.date=date;
        this.product_id=product_id;

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

    public String getProduct_desc() {
        return product_desc;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return product_desc;
    }

    public  void setProduct_desc(String product_desc){this.product_desc=product_desc;}

    public String getUnits(){return units;}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

