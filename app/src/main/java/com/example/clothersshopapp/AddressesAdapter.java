package com.example.clothersshopapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static com.example.clothersshopapp.DeliveryActivity.SELECT_ADDRESS;
import static com.example.clothersshopapp.MyAccountFragment.MANAGE_ADDRESS;
import static com.example.clothersshopapp.MyAddressesActivity.refreshItem;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.Viewholder> {

    private List<AddressesModel> addressesModelList;
    private int MODE;
    private int preSelectedPosition = -1;

    public AddressesAdapter(List<AddressesModel> addressesModelList, int MODE) {
        this.addressesModelList = addressesModelList;
        this.MODE = MODE;
    }

    @NonNull
    @Override
    public AddressesAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.addresses_item_layout,viewGroup,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressesAdapter.Viewholder viewholder, int position) {
        String name = addressesModelList.get(position).getFullname();
        String address = addressesModelList.get(position).getAddress();
        String pincode = addressesModelList.get(position).getPincode();
        Boolean selected = addressesModelList.get(position).getSelected();

        viewholder.setData(name, address, pincode, selected, position);
    }

    @Override
    public int getItemCount() {
        return addressesModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        private TextView fullname;
        private TextView address;
        private TextView pincode;
        private ImageView icon;
        private LinearLayout optionContainer;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            fullname = itemView.findViewById(R.id.tv_name_address_item);
            address = itemView.findViewById(R.id.tv_address_item);
            pincode = itemView.findViewById(R.id.tv_pincode_address_item);
            icon = itemView.findViewById(R.id.icon_view_address_item);
            optionContainer = itemView.findViewById(R.id.option_container);
        }

        private void setData(String userName, String userAddress, String userPincode, Boolean selecterd, int position) {
            fullname.setText(userName);
            address.setText(userAddress);
            pincode.setText(userPincode);

            if (MODE == SELECT_ADDRESS) {
                icon.setImageResource(R.drawable.ic_checkmark);
                if (selecterd) {
                    icon.setVisibility(View.VISIBLE);
                    preSelectedPosition = position;
                } else {
                    icon.setVisibility(View.GONE);
                }
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        if (preSelectedPosition != position) {
                            addressesModelList.get(position).setSelected(true);
                            addressesModelList.get(preSelectedPosition).setSelected(false);
                            refreshItem(preSelectedPosition, position);
                            preSelectedPosition = position;
                        }
                    }
                });
            } else if (MODE == MANAGE_ADDRESS) {
                optionContainer.setVisibility(View.GONE);
                icon.setImageResource(R.drawable.ic_menu_vertical);
                icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        optionContainer.setVisibility(View.VISIBLE);
                        refreshItem(preSelectedPosition,preSelectedPosition);
                        preSelectedPosition = position;
                    }
                });
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refreshItem(preSelectedPosition,preSelectedPosition);
                        preSelectedPosition = -1;
                    }
                });
            }
        }
    }
}
