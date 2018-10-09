package com.example.adimn.myapplication;

import android.app.Activity;
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
 * Created by pari on 07-06-2018.
 */

public class MyProductListAdapter extends BaseAdapter
{

    Context mcontext;
    List<MyProductListCon> productListCon;
    List<MyProductListCon> arraylist;
    LayoutInflater inflator;
    ImageView  img_logo;
    TextView tv_productname,tv_categoryname, tv_qty, tv_price,tv_description,tv_units;

    customButtonListener customlistener;
    public MyProductListAdapter(Context context, List<MyProductListCon> productListCon)
    {
        this.mcontext = context;
        this.productListCon=productListCon;
        arraylist = new ArrayList<>();
        arraylist.addAll(productListCon);
        inflator = LayoutInflater.from(mcontext);
    }

    public interface customButtonListener{
        public void onButtonClickListener(int position, View view);
    }
    public void setCustomButtonListener(customButtonListener listener){
        this.customlistener = listener;

}
    public static class  ViewHolder
    {
        TextView tv_productname, tv_categoryname, tv_qty, tv_price, tv_units, tv_description,tv_date;
        ImageView img_logo,img_edit,img_avaliable;
    }

    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public Object getItem(int i) {
        return productListCon.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup)
    {

        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflator.inflate(R.layout.list_product_single_item, null);
            // Locate the TextViews in listview_item.xml
            holder.tv_productname = (TextView) view.findViewById(R.id.tv_productname);
            holder.tv_categoryname = (TextView) view.findViewById(R.id.tv_categoryname);
            //holder.tv_qty = (TextView) view.findViewById(R.id.tv_qty);
            holder.tv_price = (TextView) view.findViewById(R.id.tv_price);
            holder.tv_units = (TextView) view.findViewById(R.id.tv_units);
            holder.tv_description = (TextView) view.findViewById(R.id.tv_description);
            holder.tv_date = (TextView) view.findViewById(R.id.tv_date);
            holder.img_logo = (ImageView)view.findViewById(R.id.img_logo);
            holder.img_edit = (ImageView)view.findViewById(R.id.img_edit);
            holder.img_avaliable = (ImageView)view.findViewById(R.id.img_avaliable);


            final View finalView = view;
            holder.img_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (customlistener !=null){
                        customlistener.onButtonClickListener(position,finalView);
                    }
                }
            });
            holder.img_avaliable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (customlistener !=null){
                        customlistener.onButtonClickListener(position,finalView);
                    }
                }
            });
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.tv_productname.setText(productListCon.get(position).getProduct_name());
        holder.tv_categoryname.setText(productListCon.get(position).getCat_name());
        //holder.tv_qty.setText(productListCon.get(position).getQuantity());
        holder.tv_price.setText(productListCon.get(position).getPrice());
        holder.tv_units.setText(productListCon.get(position).getUnits());
        holder.tv_description.setText(productListCon.get(position).getDescription());
        holder.tv_date.setText(productListCon.get(position).getDate());
        Picasso.with(mcontext).load(Uri.parse(productListCon.get(position).getImage().toString())).into(holder.img_logo);
        // holder.iv_shoutsicon.setBackgroundResource(eventsActivity.get(position).getIv_shoutsicon());
        System.out.println("images is"+productListCon);
        return view;
    }
}

