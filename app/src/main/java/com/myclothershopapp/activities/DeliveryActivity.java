package com.myclothershopapp.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.myclothershopapp.adapter.CartAdapter;
import com.myclothershopapp.DBqueries;
import com.myclothershopapp.MaterialCardTranslation;
import com.myclothershopapp.MyFirebaseMessaging;
import com.myclothershopapp.PrefManager;
import com.myclothershopapp.R;
import com.myclothershopapp.model.CartItemModel;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class DeliveryActivity extends AppCompatActivity implements View.OnClickListener,PaymentStatusListener, CartAdapter.TotalAmountinterface {

    private static final String TAG = "mytag";
    public static List<CartItemModel> cartItemModelList;
    private RecyclerView deliveryRecyclerView;
    public static CartAdapter cartAdapter;
    private Button changeOrAddNewAddressBtn;
    public static final int SELECT_ADDRESS = 0;
    private TextView totalAmount;
    private TextView fullname;
    private String name, mobileNo;
    private TextView fullAddress;
    private TextView pincode;
    private Button continueBtn;
    public static Dialog loadingDialog;
    private Dialog paymentMethodDialog;
    private ImageButton cod;
    private String paymentMethod = "PAYTM";
    private ConstraintLayout orderConfirmationLayout;
    private ImageButton continueShoppingBtn;
    private TextView orderId;
    private boolean continuecart;

    private boolean successResponse = false;
    public static boolean fromCart;
    private String order_id;
    public static boolean codOrderConfirmed = false;
    private MaterialCardTranslation codcard;
    private MaterialButton placeorder;
    private FirebaseFirestore firebaseFirestore;
    public static boolean getQtyIDs = true;
    private LinearLayout allmethodscards;
    private BottomDialog dialog;
    ImageView prescription;
    Button  skiporContinuebtn;
    private BroadcastReceiver mBroadcastReceiver;
    private Uri mFileUri;
    private Uri mDownloadUrl = null;
    TextView downloadurl;
    PrefManager prefManager;
    private boolean isPaid = false;
    private boolean isProcessing = false;
    private boolean isPaytmProduction = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        downloadurl = findViewById(R.id.downloadurl);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Delivery");
        prefManager = new PrefManager(this);

        deliveryRecyclerView = findViewById(R.id.delivery_recyclerview);
        changeOrAddNewAddressBtn = findViewById(R.id.change_or_add_address_btn);
        totalAmount = findViewById(R.id.total_cart_amount);
        fullname = findViewById(R.id.address_fullname);
        fullAddress = findViewById(R.id.address);
        pincode = findViewById(R.id.pincode);
        continueBtn = findViewById(R.id.cart_continue_btn);
        orderConfirmationLayout = findViewById(R.id.order_confirmation_layout);
        continueShoppingBtn = findViewById(R.id.continue_shopping_btn);
        orderId = findViewById(R.id.order_id);

        //////////////loading Dialog Start
        loadingDialog = new Dialog(DeliveryActivity.this);
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ////////////////loading Dialog End


        //////////////payment Dialog Start
        paymentMethodDialog = new Dialog(DeliveryActivity.this);
        paymentMethodDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        paymentMethodDialog.setContentView(R.layout.payment_method_dialog);
        paymentMethodDialog.setCancelable(true);
        paymentMethodDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        paymentMethodDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        codcard =  paymentMethodDialog.findViewById(R.id.cod);
        placeorder = paymentMethodDialog.findViewById(R.id.placeorder);
        allmethodscards = paymentMethodDialog.findViewById(R.id.allmethodscards);

        placeorder.setEnabled(false);
        placeorder.setAlpha(0.6f);

        ////////////////payment Dialog End
        firebaseFirestore = FirebaseFirestore.getInstance();
        getQtyIDs = true;

        codcard.setOnClickListener(this::onClick);
        placeorder.setOnClickListener(this::onClick);
        order_id = UUID.randomUUID().toString().substring(0, 16);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        deliveryRecyclerView.setLayoutManager(layoutManager);

        cartAdapter = new CartAdapter(cartItemModelList, totalAmount, false,this::totalcartamount);
        deliveryRecyclerView.setAdapter(cartAdapter);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.bottomdialog_for_imagepick, null);

        FloatingActionButton gallarybtn =  customView.findViewById(R.id.gallery);
        FloatingActionButton capturebtn =  customView.findViewById(R.id.camera);
        skiporContinuebtn = customView.findViewById(R.id.skip);
        prescription = customView.findViewById(R.id.prescription);
        gallarybtn.setOnClickListener(this::onClick);
        capturebtn.setOnClickListener(this::onClick);
        skiporContinuebtn.setOnClickListener(this::onClick);

        cartAdapter.notifyDataSetChanged();

        changeOrAddNewAddressBtn.setVisibility(View.VISIBLE);
        changeOrAddNewAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQtyIDs = false;
                Intent myAddressesIntent = new Intent(DeliveryActivity.this, MyAddressesActivity.class);
                myAddressesIntent.putExtra("MODE", SELECT_ADDRESS);
                startActivity(myAddressesIntent);
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean allProductsAvailable = true;
                Boolean isitMedicalProduct = false;
              for (CartItemModel cartItemModel : cartItemModelList){
                  if (cartItemModel.isQtyError()){
                      allProductsAvailable = false;
                      break;
                  }
                  if (cartItemModel.getType() == CartItemModel.CART_ITEM) {
                      if (!cartItemModel.isCOD()) {
                          codcard.setVisibility(View.GONE);
                          break;
                      } else {
                          codcard.setVisibility(View.VISIBLE);
                      }
                  }

                  if (cartItemModel.isIsitMedicalProduct()){
                      isitMedicalProduct = true;
                  }

              }
              if (allProductsAvailable){

                       if (!isitMedicalProduct){
                           paymentMethodDialog.show();
                       }else {

                           dialog = new BottomDialog.Builder(DeliveryActivity.this)
                                   .setTitle("Tải ảnh lên")
                                   .setCustomView(customView)
                                   .setCancelable(false)
                                   .show();
                       }
              }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        /////////////////accessing quantity start
        if (!getQtyIDs) {
            loadingDialog.show();
            for (int x = 0; x < cartItemModelList.size() - 1; x++) {
                for (int y = 0; y < cartItemModelList.get(x).getProductQuantity(); y++) {
                    final String quantityDocumentName = UUID.randomUUID().toString().substring(0, 20);
                    final Map<String, Object> timestamp = new HashMap<>();
                    timestamp.put("time", FieldValue.serverTimestamp());
                    final int finalX = x;
                    final int finalY = y;

                    firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(x).getProductID()).collection("QUANTITY").document(quantityDocumentName).set(timestamp)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                                            cartItemModelList.get(finalX).getQtyIDs().add(quantityDocumentName);
                                                            if (finalY + 1 == cartItemModelList.get(finalX).getProductQuantity()) {
                                                                firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(finalX).getProductID()).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).limit(cartItemModelList.get(finalX).getStockQuantity()).get()
                                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    List<String> serverQuantity = new ArrayList<>();

                                                                                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                                                        serverQuantity.add(queryDocumentSnapshot.getId());
                                                                                    }
                                                                                    long availableQty = 0;
                                                                                    boolean noLongerAvailable = true;
                                                                                    for (String qtyId : cartItemModelList.get(finalX).getQtyIDs()) {
                                                                                        cartItemModelList.get(finalX).setQtyError(false);
                                                                                        if (!serverQuantity.contains(qtyId)) {
                                                                                            if (noLongerAvailable) {
                                                                                                cartItemModelList.get(finalX).setInStock(false);
                                                                                            } else {
                                                                                                cartItemModelList.get(finalX).setQtyError(true);
                                                                                                cartItemModelList.get(finalX).setMaxQuantity(availableQty);
                                                                                                DynamicToast.make(DeliveryActivity.this, "Sorry ! All products may not be available in required quantity..", Toast.LENGTH_LONG).show();
                                                                                            }
                                                                                        } else {
                                                                                            availableQty++;
                                                                                            noLongerAvailable = false;
                                                                                        }
                                                                                    }
                                                                                    cartAdapter.notifyDataSetChanged();
                                                                                } else {
                                                                                    String error = task.getException().getMessage();
                                                                                    DynamicToast.make(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                                                                                }
                                                                                loadingDialog.dismiss();
                                                                            }
                                                                        });
                                                            }



                                    }else {
                                        loadingDialog.dismiss();
                                        String error = task.getException().getMessage();
                                        DynamicToast.make(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }

            }
        } else {
            getQtyIDs = true;
        }

        /////////////////accessing quantity end

        name = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getName();
        //gst = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getGst();

        mobileNo = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getMobileNo();
        if (DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAlternateMobileNo().equals("")) {
            fullname.setText(name + " - " + mobileNo);
        }else {
            fullname.setText(name + " - " + mobileNo + " or " + DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAlternateMobileNo());
        }
        String flatNo = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getFlatNo();
        String locality = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getLocality();
        String landmark = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getLandmark();
        String city = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getCity();
        String state = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getState();

        if (landmark.equals("")){
            fullAddress.setText(flatNo +" " + locality +" " + city +" " + state);
        }else {
            fullAddress.setText(flatNo +" " + locality +" " + landmark +" " + city +" " + state);
        }
        pincode.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getPincode());

        if (codOrderConfirmed) {
            showConfirmationLayout();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        loadingDialog.dismiss();

        if (getQtyIDs) {
            for (int x = 0; x < cartItemModelList.size() - 1; x++) {
                if (!successResponse) {
                    for (final String qtyID : cartItemModelList.get(x).getQtyIDs()) {
                        final int finalX = x;
                        final int finalX1 = x;
                        firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(x).getProductID()).collection("QUANTITY").document(qtyID).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
//                                        firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(finalX1).getProductID()).collection(cartItemModelList.get(finalX1).getProductColor()+"_"+cartItemModelList.get(finalX1).getProductSize()+"_qty").document(qtyID).delete()
//                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                    @Override
//                                                    public void onSuccess(Void aVoid) {
//
//                                        if (qtyID.equals(cartItemModelList.get(finalX).getQtyIDs().get(cartItemModelList.get(finalX).getQtyIDs().size() - 1))) {
//                                            cartItemModelList.get(finalX).getQtyIDs().clear();
//                                        }
//                                                    }
//                                                });
                                    }
                                });
                    }
                } else {
                    cartItemModelList.get(x).getQtyIDs().clear();
                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (successResponse) {
            finish();
            return;
        }
        super.onBackPressed();
    }

    private void showConfirmationLayout() {
        successResponse = true;
        codOrderConfirmed = false;
        getQtyIDs = false;
        for (int x = 0; x < cartItemModelList.size() - 1; x++) {

            for (String qtyID : cartItemModelList.get(x).getQtyIDs()) {
                firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(x).getProductID()).collection("QUANTITY").document(qtyID).update("user_ID", FirebaseAuth.getInstance().getUid());
                //firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(x).getProductID()).collection(cartItemModelList.get(x).getProductColor()+"_"+cartItemModelList.get(x).getProductSize()+"_qty").document(qtyID).update("user_ID", FirebaseAuth.getInstance().getUid());

            }
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Bundle bundle = new Bundle();
            RemoteMessage remoteMessage = new RemoteMessage(bundle);

            MyFirebaseMessaging.sendNotificationAPI26(this, remoteMessage,false,"E-Bike-Bazaar","Thank u so much for your Order!");
        }else {
            Bundle bundle = new Bundle();
            RemoteMessage remoteMessage = new RemoteMessage(bundle);
            MyFirebaseMessaging.sendNotification(this, remoteMessage,false,"E-Bike-Bazaar","Thank you so much for your Order!");

        }
        if (Main2Activity.main2Activity != null) {
            Main2Activity.main2Activity.finish();
            Main2Activity.main2Activity = null;
            Main2Activity.showCart = false;
        } else {
            Main2Activity.resetMain2Activity = true;
        }
        if (ProductDetailsActivity.productDetailsActivity != null) {
            ProductDetailsActivity.productDetailsActivity.finish();
            ProductDetailsActivity.productDetailsActivity = null;
        }

        if (fromCart) {
            loadingDialog.show();
            Map<String, Object> updateCartList = new HashMap<>();
            long cartListSize = 0;
            final List<Integer> indexList = new ArrayList<>();

            Log.d(TAG, "DBQuerys.cartlist.size():"+DBqueries.cartList.size());
            for (int x = 0; x < DBqueries.cartList.size(); x++) {
                if (!DBqueries.cartItemModelList.get(x).isInStock()) {
                    updateCartList.put("product_ID_" + cartListSize, DBqueries.cartItemModelList.get(x).getProductID());
                    cartListSize++;
                } else {
                    indexList.add(x);
                }
            }

            updateCartList.put("list_size", cartListSize);

            FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART")
                    .set(updateCartList).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Collections.sort(indexList);
                        for (int x = indexList.size() -1; x >=  0; x--) {
                            DBqueries.cartList.remove(indexList.get(x).intValue());
                            DBqueries.cartItemModelList.remove(indexList.get(x).intValue());
                        }
                        DBqueries.cartItemModelList.remove(DBqueries.cartItemModelList.size() - 1);
                    } else {
                        String error = task.getException().getMessage();
                        DynamicToast.make(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                    loadingDialog.dismiss();
                }
            });
        }
        continueBtn.setEnabled(false);
        changeOrAddNewAddressBtn.setEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        orderId.setText("Order ID " + order_id);
        orderConfirmationLayout.setVisibility(View.VISIBLE);
        continueShoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void placeorderDetails(){

        String userID = FirebaseAuth.getInstance().getUid();
        loadingDialog.show();

        int size = 0;
        for (CartItemModel cartItemModel : cartItemModelList) {

            if (cartItemModel.getType() == CartItemModel.CART_ITEM) {
                size++;

                Map<String,Object> orderDetails = new HashMap<>();
                orderDetails.put("ORDER ID",order_id);
                orderDetails.put("Prescriptionimage",downloadurl.getText().toString());
                orderDetails.put("Product Id",cartItemModel.getProductID());
                orderDetails.put("Product Image",cartItemModel.getProductImage());
                orderDetails.put("Product Title",cartItemModel.getProductTitle());
                orderDetails.put("User Id",userID);
                orderDetails.put("Product Quantity",cartItemModel.getProductQuantity());
                if (cartItemModel.getCuttedPrice() != null){
                    orderDetails.put("Cutted price",cartItemModel.getCuttedPrice());
                }else {
                    orderDetails.put("Cutted price","");
                }
                     if (prefManager.getDiscountAvailable()) {
                         int percentage = Integer.parseInt(prefManager.getPercentageValue());
                         String price = cartItemModel.getProductPrice();
                         Double productpricecal = (double) percentage / (double) 100.00;
                         Double productrealprice = (Double) (productpricecal * Integer.parseInt(price));
                         int realpriceint = (int) Math.round(productrealprice);
                         orderDetails.put("Product Price", Integer.parseInt(cartItemModel.getProductPrice()) - realpriceint);
                     }else{
                         orderDetails.put("Product Price",cartItemModel.getProductPrice());
                     }
                if (cartItemModel.getSelectedCoupenId() != null) {
                    orderDetails.put("Coupen Id", cartItemModel.getSelectedCoupenId());
                }else {
                    orderDetails.put("Coupen Id", "");
                }
                if (cartItemModel.getDiscountedPrice() != null) {
                    orderDetails.put("Discounted Price", cartItemModel.getDiscountedPrice());
                }else {
                    orderDetails.put("Discounted Price", "");
                }
                orderDetails.put("Ordered date ",FieldValue.serverTimestamp());
                orderDetails.put("Packed date ",FieldValue.serverTimestamp());
                orderDetails.put("Shipped date ",FieldValue.serverTimestamp());
                orderDetails.put("Delivered date ",FieldValue.serverTimestamp());
                orderDetails.put("Cancelled date ",FieldValue.serverTimestamp());
                if (paymentMethod.equals("cod")){
                    orderDetails.put("Order Status","Ordered");
                }else {
                    orderDetails.put("Order Status","Cancelled");
                }
                orderDetails.put("Payment Method",paymentMethod);
                orderDetails.put("Address",fullAddress.getText());
                orderDetails.put("FullName",fullname.getText());
                orderDetails.put("Pincode",pincode.getText());
                orderDetails.put("Free Coupens",cartItemModel.getFreeCoupens());
                orderDetails.put("Delivery Price",cartItemModel.getDeliveryPrice());

                orderDetails.put("Cancellation requested",false);

                firebaseFirestore.collection("ORDERS").document(order_id).collection("OrderItems").document(cartItemModel.getProductID())
                .set(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()){
                          String error = task.getException().getMessage();
                            DynamicToast.make(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                if (size == cartItemModelList.size()-1) {
                    Map<String, Object> orderDetailss = new HashMap<>();
                    orderDetailss.put("Total Items", cartItemModel.getTotalItems());
                    orderDetailss.put("Total Items Price", cartItemModel.getTotalItemPrice());
                    orderDetailss.put("Delivery Price", cartItemModel.getDeliveryPrice());
                    orderDetailss.put("Total Amount", cartItemModel.getTotalAmount());
                    orderDetailss.put("Saved Amount", cartItemModel.getSavedAmount());
                    orderDetailss.put("Payment Status", "not paid");
                    orderDetailss.put("orderdate",FieldValue.serverTimestamp());
                    orderDetailss.put("Order Status", "Ordered");
                    firebaseFirestore.collection("ORDERS").document(order_id)
                            .set(orderDetailss).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                               if (paymentMethod.equals("cod")){
                                    cod();
                                }
                            } else {
                                String error = task.getException().getMessage();
                                DynamicToast.make(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }    }
        }
    }

    private void cod(){
        getQtyIDs = false;
        paymentMethodDialog.dismiss();
        Map<String, Object> userOrder = new HashMap<>();
                            userOrder.put("order_id", order_id);
                            userOrder.put("time", FieldValue.serverTimestamp());
                            FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_ORDERS").document(order_id).set(userOrder)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                loadingDialog.dismiss();
                                    showConfirmationLayout();
                                            } else {
                                                DynamicToast.make(DeliveryActivity.this, "Đã xảy ra sự cố", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            Uri fileUri = data.getData();
            if (fileUri != null) {
                prescription.setImageURI(fileUri);
                skiporContinuebtn.setText("Continue");
                mFileUri = fileUri;

            }
        }else if (resultCode == ImagePicker.RESULT_ERROR) {
            DynamicToast.make(this, "Something Went wrong", Toast.LENGTH_SHORT).show();
            skiporContinuebtn.setText("SKIP");
        }
            else {
            skiporContinuebtn.setText("SKIP");
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.gallery:{
                getQtyIDs = false;
                ImagePicker.Companion.with(DeliveryActivity.this)
                        .compress(500)
                        .galleryOnly()//Final image size will be less than 1 MB(Optional)
                        .start();
                return;
            }
            case R.id.camera:{
                getQtyIDs = false;
                String path = getExternalFilesDir("/").getAbsolutePath();

                ImagePicker.Companion.with(DeliveryActivity.this)
                        .compress(500)
                        .saveDir(path + "/" + "perscription_"+System.currentTimeMillis())
                        .cameraOnly()//Final image size will be less than 1 MB(Optional)
                        .start();
                return;
            }
            case R.id.skip:{

                if (skiporContinuebtn.getText().equals("SKIP")){
                    dialog.dismiss();
                    paymentMethodDialog.show();
                    return;
                }else {
                    if (mFileUri!=null) {
                        uploadFromUri(mFileUri);
                    }
                }
            }


            case R.id.cod:{
                codcard.simulateClickElevation (null );
                codcard.setActivated(true);
                placeorder.setText("Place Order");

                codcard.setStrokeColor(getResources().getColor(R.color.colorPrimary));
                codcard.setStrokeWidth(2);

                placeorder.setAlpha(1f);
                placeorder.setEnabled(true);
                return;
            }

            case R.id.placeorder:{

                if (placeorder.isEnabled()){
                    for (int x=0;x<allmethodscards.getChildCount(); x++){

                        MaterialCardTranslation materialCardTranslation = (MaterialCardTranslation) allmethodscards.getChildAt(x);
                        if (materialCardTranslation.isActivated()){
                            paymentMethod = materialCardTranslation.getTag().toString();
                            placeorderDetails();

                            break;
                        }
                    }

                }else {
                    DynamicToast.make(DeliveryActivity.this, "Chọn phương thức thanh toán !", Toast.LENGTH_SHORT).show();
                }

                return;
            }

            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }

    }

    private void uploadFromUri(Uri fileUri) {
        Log.d(TAG, "uploadFromUri:src:" + fileUri.toString());
       TextView textView= loadingDialog.findViewById(R.id.textView34);
        textView.setText("Vui lòng đợi trong khi hình ảnh được tải lên !");
        loadingDialog.show();
        // Save the File URI
        mFileUri = fileUri;
        // Clear the last download, if any
        mDownloadUrl = null;

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference photoRef = mStorageRef.child("prescriptions")
                .child(fileUri.getLastPathSegment());
        photoRef.putFile(fileUri).
                addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    }
                })
                .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        // Forward any exceptions
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        Log.d(TAG, "uploadFromUri: upload success");

                        // Request the public download URL
                        return photoRef.getDownloadUrl();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(@NonNull Uri downloadUri) {

                        mDownloadUrl = downloadUri;
                        downloadurl.setText(downloadUri.toString());
                        loadingDialog.dismiss();
                        textView.setText("Đang tải..");
                        dialog.dismiss();
                        paymentMethodDialog.show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        DynamicToast.make(DeliveryActivity.this, ""+exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onTransactionCompleted(TransactionDetails transactionDetails) {
        // Transaction Completed
        Log.d("TransactionDetails", transactionDetails.toString());
        totalAmount.setText(transactionDetails.toString());
    }

    @Override
    public void onTransactionSuccess() {
        // Payment Success

        loadingDialog.show();
        Map<String,Object> updateStatus = new HashMap<>();
        updateStatus.put("Payment Status","Paid through Paytm ");
        updateStatus.put("Order Status","Ordered");
        firebaseFirestore.collection("ORDERS").document(order_id).update(updateStatus)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Map<String,Object> userOrder = new HashMap<>();
                            userOrder.put("order_id",order_id);
                            userOrder.put("time",FieldValue.serverTimestamp());
                            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_ORDERS").document(order_id).set(userOrder)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                loadingDialog.dismiss();
                                                showConfirmationLayout();
                                            }else {
                                                DynamicToast.make(DeliveryActivity.this, "Không cập nhật được orderList của người dùng ", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }else {
                            DynamicToast.make(DeliveryActivity.this, "Order Cancelled", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    @Override
    public void onTransactionSubmitted() {
        // Payment Pending
        loadingDialog.dismiss();
   //     paymentMethodDialog.show();
        DynamicToast.make(this, "Pending | Submitted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTransactionFailed() {
        // Payment Failed
        loadingDialog.dismiss();
        paymentMethodDialog.show();
        DynamicToast.make(this, "Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTransactionCancelled() {
        loadingDialog.dismiss();
        paymentMethodDialog.show();
        // Payment Cancelled by User
        DynamicToast.make(this, "Cancelled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAppNotFound() {
        paymentMethodDialog.show();
        DynamicToast.make(this, "App Not Found", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void totalcartamount(int ammount) {
        if (ammount>250){
            continuecart = true;
        }else {
            continuecart=false;
        }

    }

    private void updateStutus() {
        Map<String, Object> updateStatus = new HashMap<>();

        if (isPaid) {
            updateStatus.put("Order Status", "Ordered");
            updateStatus.put("Payment Status", "Paid With Paytm");

        }else {
            updateStatus.put("Order Status", "Pending");
            if (isProcessing){
                updateStatus.put("Payment Status", "Processing");
            }

        }
        //updateStatus.put("transaction_id", paytm_transaction_id);

        firebaseFirestore.collection("ORDERS").document(order_id).update(updateStatus)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> userOrder = new HashMap<>();
                            userOrder.put("order_id", order_id);
                            userOrder.put("time", FieldValue.serverTimestamp());
                            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_ORDERS").document(order_id).set(userOrder)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                showConfirmationLayout();
                                            } else {
                                                DynamicToast.make(DeliveryActivity.this, "Failed to update user's orderList", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            DynamicToast.make(DeliveryActivity.this, "Something Went Wrong Contact Us For More Details", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}
