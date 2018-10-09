package com.example.adimn.myapplication;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PurchaseListAdapter extends BaseAdapter {
    Context mcontext;
    List<PurchaseListCon> purchaseListCon;
    List<PurchaseListCon> arraylist;
    LayoutInflater inflator;
    /*ImageView  img_logo;
    TextView tv_productname,tv_categoryname, tv_qty, tv_price,tv_description,tv_units;
*/

    public PurchaseListAdapter(Context context, List<PurchaseListCon> purchaseListCon) {
        this.mcontext = context;
        this.purchaseListCon = purchaseListCon;
        arraylist = new ArrayList<>();
        arraylist.addAll(purchaseListCon);
        inflator = LayoutInflater.from(mcontext);

    }

    public class ViewHolder {
        TextView tv_productname, tv_category, tv_price, tv_units, tv_name,tv_city;
        ImageView img_logo;
    }

    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public Object getItem(int i) {
        return purchaseListCon.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        final PurchaseListAdapter.ViewHolder holder;
        if (view == null) {
            holder = new PurchaseListAdapter.ViewHolder();
            view = inflator.inflate(R.layout.activity_purchases_list_single, null);
            // Locate the TextViews in listview_item.xml
            holder.tv_productname = (TextView) view.findViewById(R.id.tv_productname);
            holder.tv_category = (TextView) view.findViewById(R.id.tv_categoryname);
            //holder.tv_qty = (TextView) view.findViewById(R.id.tv_qty);
            holder.tv_price = (TextView) view.findViewById(R.id.tv_price);
            holder.tv_units = (TextView) view.findViewById(R.id.tv_units);
            holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            holder.tv_city=(TextView)view.findViewById(R.id.tv_city);
            holder.img_logo = (ImageView) view.findViewById(R.id.img_logo);
            view.setTag(holder);
        } else {
            holder = (PurchaseListAdapter.ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.tv_productname.setText(purchaseListCon.get(position).getProduct_name());
        holder.tv_category.setText(purchaseListCon.get(position).getCat_name());
        //holder.tv_qty.setText(productListCon.get(position).getQuantity());
        holder.tv_price.setText(purchaseListCon.get(position).getAmount());
        holder.tv_units.setText(purchaseListCon.get(position).getUnits());
        holder.tv_name.setText(purchaseListCon.get(position).getCustomer_name());
        holder.tv_city.setText(purchaseListCon.get(position).getCity());
        View convertView;
        inflator = LayoutInflater.from(mcontext);
        convertView = inflator.inflate(R.layout.activity_purchases_list_single, viewGroup, false);
        ImageView iv_pro = (ImageView) view.findViewById(R.id.img_logo);
        Picasso.with(mcontext).load(Uri.parse(purchaseListCon.get(position).getImage())).into(holder.img_logo);
        // System.out.println("image is "+orderListCon.get(position).getImage());
        //Picasso.with(mcontext).load(Uri.parse(productListCon.get(position).getImage())).into(holder.iv_pro);
        // Picasso.with(mcontext).load(arraylist.get(position).getImage()).into((Target) holder.iv_pro);


        return view;
    }

   /* public  MyViewHolder MyProductListAdapter(@NonNull ViewGroup parent, int viewtype) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_product_single_item,parent,false);
        return new MyViewHolder(itemView);
    }

    public void onBindViewHolder(@NonNull MyProductListAdapter.MyViewHolder holder,final int position){


    }*/
}

