package com.myclothershopapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.myclothershopapp.PrefManager;
import com.myclothershopapp.R;
import com.myclothershopapp.activities.ProductDetailsActivity;
import com.myclothershopapp.model.HorizontalProductScrollModel;

import java.util.List;

public class GridProductLayoutAdapter extends BaseAdapter {
    List<HorizontalProductScrollModel> horizontalProductScrollModelList;
    PrefManager prefManager;
    public GridProductLayoutAdapter(List<HorizontalProductScrollModel> horizontalProductScrollModelList, Context context) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
        prefManager = new PrefManager(context);
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = convertView;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_scroll_item_layout,null);
            view.setElevation(0);
            view.setBackgroundColor(Color.parseColor("#FFFFFF"));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productDetailsIntent = new Intent(parent.getContext(), ProductDetailsActivity.class);
                    productDetailsIntent.putExtra("PRODUCT_ID",horizontalProductScrollModelList.get(position).getProductID());
                    parent.getContext().startActivity(productDetailsIntent);
                }
            });

            ImageView productImage = view.findViewById(R.id.h_s_product_image);
            TextView productTitle = view.findViewById(R.id.h_s_product_title);
            TextView productDescription = view.findViewById(R.id.h_s_product_description);
            TextView productPrice = view.findViewById(R.id.h_s_product_price);
            TextView cuttedprice  = view.findViewById(R.id.cuttedprice);
            TextView discountedtextview = view.findViewById(R.id.discountTextView);

            Glide.with(parent.getContext()).load(horizontalProductScrollModelList.get(position).getProduceImage()).apply(new RequestOptions().placeholder(R.drawable.placeholder_image)).into(productImage);
            productTitle.setText(horizontalProductScrollModelList.get(position).getProductTitle());
            productDescription.setText(horizontalProductScrollModelList.get(position).getProductDescription());


            if (prefManager.getDiscountAvailable()){
                cuttedprice.setVisibility(View.GONE);
                int percentage =  Integer.parseInt(prefManager.getPercentageValue());
                cuttedprice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                String price = horizontalProductScrollModelList.get(position).getProductPrice();
                cuttedprice.setText(price+" đồng");

                Double productpricecal = (double) percentage / (double) 100.00;
                if (!price.equals("")) {
                    Double productrealprice = (Double) (productpricecal * Integer.parseInt(price));
                    int realpriceint = (int)Math.round(productrealprice);

                    productPrice.setText(( Integer.parseInt(price) - realpriceint) + " đồng");
                    Double p = Integer.parseInt(price) - productrealprice;
                    Double dif = Integer.parseInt(price) - p;
                    Double div = (double) dif / (double) Integer.parseInt(price);
                    int percentoff = (int) (div * 100);
                    discountedtextview.setText(percentoff + "%");
                    discountedtextview.setVisibility(View.GONE);
                }
            }else {
                cuttedprice.setVisibility(View.GONE);
                discountedtextview.setVisibility(View.GONE);
                productPrice.setText(horizontalProductScrollModelList.get(position).getProductPrice()+" đồng");
            }
        return view;
}
}
