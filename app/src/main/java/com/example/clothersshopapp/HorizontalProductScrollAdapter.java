package com.example.clothersshopapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HorizontalProductScrollAdapter extends RecyclerView.Adapter<HorizontalProductScrollAdapter.ViewHolder> {

    private List<HorizontalProductScrollModel> horizontalProductScrollModelList;

    public HorizontalProductScrollAdapter(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }

    @NonNull
    @Override
    public HorizontalProductScrollAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_scroll_item_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalProductScrollAdapter.ViewHolder viewHolder, int position) {
        int resource = horizontalProductScrollModelList.get(position).getProductImage();
        String name = horizontalProductScrollModelList.get(position).getProductName();
        String price = horizontalProductScrollModelList.get(position).getProductPrice();
        String location = horizontalProductScrollModelList.get(position).getProductLocation();

        viewHolder.setProductImage(resource);
        viewHolder.setProductName(name);
        viewHolder.setProductPrice(price);
        viewHolder.setProductLocation(location);
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
        private TextView productName;
        private TextView productPrice;
        private TextView productLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.iv_horizontal_product);
            productName = itemView.findViewById(R.id.tv_name_horizontal_product);
            productPrice = itemView.findViewById(R.id.tv_price_horizontal_product);
            productLocation = itemView.findViewById(R.id.tv_location_horizontal_product);
        }

        private void setProductImage(int resource) {
            productImage.setImageResource(resource);
        }

        private void setProductName(String name) {
            productName.setText(name);
        }

        private void setProductPrice(String price) {
            productPrice.setText(price);
        }

        private void setProductLocation(String location) {
            productLocation.setText(location);
        }
    }
}
