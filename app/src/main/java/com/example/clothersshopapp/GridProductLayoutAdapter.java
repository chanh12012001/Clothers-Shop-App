package com.example.clothersshopapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class GridProductLayoutAdapter extends BaseAdapter {

    List<HorizontalProductScrollModel> horizontalProductScrollModelList;

    public GridProductLayoutAdapter(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }

    @Override
    public int getCount() {
        return horizontalProductScrollModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item_layout, null);
            view.setElevation(0);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productDetailsIntent = new Intent(parent.getContext(),ProductDetailsActivity.class);
                    parent.getContext().startActivity(productDetailsIntent);
                }
            });

            ImageView productImage = view.findViewById(R.id.iv_horizontal_product);
            TextView productName = view.findViewById(R.id.tv_name_horizontal_product);
            TextView productPrice = view.findViewById(R.id.tv_price_horizontal_product);
            TextView productLocation = view.findViewById(R.id.tv_location_horizontal_product);

            productImage.setImageResource(horizontalProductScrollModelList.get(position).getProductImage());
            productName.setText(horizontalProductScrollModelList.get(position).getProductName());
            productPrice.setText(horizontalProductScrollModelList.get(position).getProductPrice());
            productLocation.setText(horizontalProductScrollModelList.get(position).getProductLocation());
        } else {
            view = convertView;
        }
        return view;
    }
}