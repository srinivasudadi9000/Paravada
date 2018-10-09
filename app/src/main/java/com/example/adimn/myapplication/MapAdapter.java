package com.example.adimn.myapplication;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adimn on 02-08-2018.
 */

public class MapAdapter extends BaseAdapter {

    Context mcontext;
    List<MapCon> mapCon;
    List<MapCon> arraylist;
    LayoutInflater inflator;
   // MyProductListAdapter.customButtonListener customlistener;
    public MapAdapter(Context context, List<MapCon> mapCon)
    {
        this.mcontext = context;
        this.mapCon = mapCon;
        arraylist = new ArrayList<>();
        arraylist.addAll(mapCon);
        inflator = LayoutInflater.from(mcontext);

    }

    public class ViewHolder {
        TextView tv_productname, tv_category,tv_units, tv_price,  tv_description;
        ImageView img_logo;
    }

    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public Object getItem(int i) {
        return mapCon.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        final MapAdapter.ViewHolder holder;
        if (view == null) {
            holder = new MapAdapter.ViewHolder();
            view = inflator.inflate(R.layout.activity_searchview_list_single, null);
            // Locate the TextViews in listview_item.xml
            holder.tv_productname = (TextView) view.findViewById(R.id.tv_productname);
            holder.tv_category = (TextView) view.findViewById(R.id.tv_category);
            //holder.tv_qty = (TextView) view.findViewById(R.id.tv_qty);

            holder.tv_units = (TextView) view.findViewById(R.id.tv_units);
            holder.tv_price = (TextView) view.findViewById(R.id.tv_price);

            holder.tv_description = (TextView) view.findViewById(R.id.tv_description);
           // holder.tv_city=(TextView)view.findViewById(R.id.tv_city);
            holder.img_logo = (ImageView) view.findViewById(R.id.img_logo);
            view.setTag(holder);
        } else {
            holder = (MapAdapter.ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.tv_productname.setText(mapCon.get(position).getProduct_name());
        holder.tv_category.setText(mapCon.get(position).getCat_name());
        //holder.tv_qty.setText(productListCon.get(position).getQuantity());

        holder.tv_units.setText(mapCon.get(position).getUnits());
        holder.tv_price.setText(mapCon.get(position).getPrice());

        holder.tv_description.setText(mapCon.get(position).getProduct_desc());
      //  holder.tv_city.setText(purchaseListCon.get(position).getCity());
        View convertView;
        inflator = LayoutInflater.from(mcontext);
        convertView = inflator.inflate(R.layout.activity_searchview_list_single, viewGroup, false);
        ImageView iv_pro = (ImageView) view.findViewById(R.id.img_logo);
        Picasso.with(mcontext).load(Uri.parse(mapCon.get(position).getImage())).into(holder.img_logo);
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

