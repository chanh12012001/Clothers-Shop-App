package com.myclothershopapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.myclothershopapp.PrefManager;
import com.myclothershopapp.activities.ProductDetailsActivity;
import com.myclothershopapp.model.HorizontalProductScrollModel;

import java.util.List;

import static com.myclothershopapp.R.*;

public class HorizontalProductScrollAdapter extends RecyclerView.Adapter<HorizontalProductScrollAdapter.ViewHolder> {

    private List<HorizontalProductScrollModel> horizontalProductScrollModelList;
    private PrefManager prefManager;
    public HorizontalProductScrollAdapter(List<HorizontalProductScrollModel> horizontalProductScrollModelList, Context context) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
        prefManager = new PrefManager(context);
    }

    @NonNull
    @Override
    public HorizontalProductScrollAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout.horizontal_scroll_item_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalProductScrollAdapter.ViewHolder viewHolder, int position) {
        String resource = horizontalProductScrollModelList.get(position).getProduceImage();
        String title = horizontalProductScrollModelList.get(position).getProductTitle();
        String description = horizontalProductScrollModelList.get(position).getProductDescription();
        String price = horizontalProductScrollModelList.get(position).getProductPrice();
        String productId = horizontalProductScrollModelList.get(position).getProductID();

        viewHolder.setData(productId,resource,title,description,price);

    }

    @Override
    public int getItemCount() {
        if (horizontalProductScrollModelList.size() > 8) {
            return 8;
        } else {
            return horizontalProductScrollModelList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView productTitle;
        private TextView productDescription;
        private TextView productPrice,cuttedprice,discountedtextview;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            productImage = itemView.findViewById(id.h_s_product_image);
            productTitle = itemView.findViewById(id.h_s_product_title);
            productDescription = itemView.findViewById(id.h_s_product_description);
            productPrice = itemView.findViewById(id.h_s_product_price);
            cuttedprice  = itemView.findViewById(id.cuttedprice);
            discountedtextview = itemView.findViewById(id.discountTextView);


        }

        private void setData(final String productId, String resource, String title, String description, String price){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(drawable.placeholder_image)).into(productImage);
            productPrice.setText(price+" đồng");

            if (prefManager.getDiscountAvailable()){
                cuttedprice.setVisibility(View.GONE);
                int percentage =  Integer.parseInt(prefManager.getPercentageValue());
                cuttedprice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
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
                productPrice.setText(price+" đồng");

            }

            productDescription.setText(description);
            productTitle.setText(title);

            if (!title.equals("")) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent productDetailsIntent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                        productDetailsIntent.putExtra("PRODUCT_ID",productId);
                        itemView.getContext().startActivity(productDetailsIntent);
                    }
                });
            }
        }

    }
}
