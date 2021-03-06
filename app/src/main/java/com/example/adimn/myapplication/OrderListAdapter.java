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
 * Created by pari on 09-07-2018.
 */

public class OrderListAdapter extends BaseAdapter {

    Context mcontext;
    List<OrderListCon> orderListCon;
    List<OrderListCon> arraylist;
    LayoutInflater inflator;
    /*ImageView  img_logo;
    TextView tv_productname,tv_categoryname, tv_qty, tv_price,tv_description,tv_units;
*/
    public customButtonListener customListner;
    public OrderListAdapter(Context context, List<OrderListCon> orderListCon) {
        this.mcontext = context;
        this.orderListCon = orderListCon;
        arraylist = new ArrayList<OrderListCon>();
        arraylist.addAll(orderListCon);
        inflator = LayoutInflater.from(mcontext);

    }

    public interface customButtonListener{
        public void onButtonClickListener(int position, View view);
    }

    public void setCustomButtonListner(customButtonListener listner){
        this.customListner=listner;
    }

    public class ViewHolder {
        TextView tv_productname, tv_category, tv_price, tv_units, tv_name,tv_city,tv_date;
        ImageView img_logo,img_call,img_whatsapp;
    }

    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public Object getItem(int i) {
        return orderListCon.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflator.inflate(R.layout.activity_order_list_single, null);
            // Locate the TextViews in listview_item.xml
            holder.tv_productname = (TextView) view.findViewById(R.id.tv_productname);
            holder.tv_category = (TextView) view.findViewById(R.id.tv_categoryname);
            //holder.tv_qty = (TextView) view.findViewById(R.id.tv_qty);
            holder.tv_price = (TextView) view.findViewById(R.id.tv_price);
            holder.tv_units = (TextView) view.findViewById(R.id.tv_units);
            holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            holder.tv_city=(TextView)view.findViewById(R.id.tv_city);
            holder.tv_date=(TextView)view.findViewById(R.id.tv_date);
            holder.img_logo = (ImageView) view.findViewById(R.id.img_logo);
            holder.img_whatsapp=(ImageView)view.findViewById(R.id.img_whatsapp);
            holder.img_call=(ImageView)view.findViewById(R.id.img_call);

            final View finalView=view;
            holder.img_whatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (customListner != null){
                        customListner.onButtonClickListener(position,v);
                    }
                }
            });
            holder.img_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (customListner != null){
                        customListner.onButtonClickListener(position,v);
                    }
                }
            });
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.tv_productname.setText(orderListCon.get(position).getProduct_name());
        holder.tv_category.setText(orderListCon.get(position).getCat_name());
        //holder.tv_qty.setText(productListCon.get(position).getQuantity());
        holder.tv_price.setText(orderListCon.get(position).getAmount());
        holder.tv_units.setText(orderListCon.get(position).getUnits());
        holder.tv_name.setText(orderListCon.get(position).getCustomer_name());
        holder.tv_city.setText(orderListCon.get(position).getCity());
        holder.tv_date.setText(orderListCon.get(position).getDate());
        View convertView;
        inflator = LayoutInflater.from(mcontext);
        convertView = inflator.inflate(R.layout.activity_order_list_single, viewGroup, false);
        ImageView iv_pro = (ImageView) view.findViewById(R.id.img_logo);
        Picasso.with(mcontext).load(Uri.parse(orderListCon.get(position).getImage())).into(holder.img_logo);
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

