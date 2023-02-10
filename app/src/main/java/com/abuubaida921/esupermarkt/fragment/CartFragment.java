package com.abuubaida921.esupermarkt.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abuubaida921.esupermarkt.Config;
import com.abuubaida921.esupermarkt.R;
import com.abuubaida921.esupermarkt.activity.AllOrderActivity;
import com.abuubaida921.esupermarkt.activity.PaymentHistoryActivity;
import com.abuubaida921.esupermarkt.adapter.CartItemAdapter;
import com.abuubaida921.esupermarkt.adapter.DeliveryAddressItemSelectAdapter;
import com.abuubaida921.esupermarkt.adapter.PaymentMethodListItemAdapter;
import com.abuubaida921.esupermarkt.adapter.PromoCodeItemSelectAdapter;
import com.abuubaida921.esupermarkt.model.CartItemModel;
import com.abuubaida921.esupermarkt.model.DeliveryAddressModel;
import com.abuubaida921.esupermarkt.model.PaymentMethodListModel;
import com.abuubaida921.esupermarkt.model.PromoCodeModel;
import com.abuubaida921.esupermarkt.paymentgateway.RazorpayPaymentActivity;
import com.abuubaida921.esupermarkt.paymentgateway.StripePaymentActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class CartFragment extends Fragment {

    ProgressBar cart_progressBar;
    RecyclerView cart_recycler;
    String userID;
    ArrayList<CartItemModel> cartList = new ArrayList<>();

    CartItemAdapter cartItemAdapter;

    TextView cart_total_price, selected_address, tv;
    BottomSheetDialog bottomSheetDialog;
    double total_price;
    Button goto_checkout_btn;
    DeliveryAddressItemSelectAdapter deliveryAddressItemSelectAdapter;
    PromoCodeItemSelectAdapter promoCodeItemSelectAdapter;
    PaymentMethodListItemAdapter paymentMethodListItemAdapter;

    long order_created_at;

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
//        paymentSheet = new PaymentSheet(this, this::onPaymentResult);

        userID = FirebaseAuth.getInstance().getUid();
        goto_checkout_btn = view.findViewById(R.id.goto_checkout_btn);
        cart_total_price = view.findViewById(R.id.cart_total_price);
        cart_progressBar = view.findViewById(R.id.cart_progressBar);
        cart_recycler = view.findViewById(R.id.cart_recycler);
        cart_recycler.setHasFixedSize(true);
        cart_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        goto_checkout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomThemeDialogueTheme);
                View bottomSheetView = LayoutInflater.from(getActivity()).inflate(R.layout.checkout_bottom_sheet_layout, container.findViewById(R.id.checkout_bottom_sheet_layout));
                RelativeLayout d_address = bottomSheetView.findViewById(R.id.d_address);
                RelativeLayout p_code = bottomSheetView.findViewById(R.id.p_code);
                RelativeLayout payment = bottomSheetView.findViewById(R.id.payment);
                tv = bottomSheetView.findViewById(R.id.checkout_total_cost);
                selected_address = bottomSheetView.findViewById(R.id.selected_address);
                TextView selected_payment_method = bottomSheetView.findViewById(R.id.selected_payment_method);
                TextView selected_coupon = bottomSheetView.findViewById(R.id.selected_coupon);
                ImageView p_method_icon = bottomSheetView.findViewById(R.id.p_method_icon);
                tv.setText(String.format("%.2f", total_price));

                d_address.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.OrderCustomAlertDialog);
                        ViewGroup viewGroup = container.findViewById(android.R.id.content);
                        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.order_customview_for_select, viewGroup, false);
                        builder.setView(dialogView);
                        final AlertDialog alertDialog = builder.create();

                        RecyclerView delivery_address_recycler;
//                        String userID = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
                        ArrayList<DeliveryAddressModel> deliveryAddressModelArrayList = new ArrayList<>();
                        delivery_address_recycler = dialogView.findViewById(R.id.recycler_view);
                        delivery_address_recycler.setHasFixedSize(true);
                        delivery_address_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
                        FirebaseDatabase.getInstance().getReference("delivery_address").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                deliveryAddressModelArrayList.clear();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    DeliveryAddressModel deliveryAddressModel = dataSnapshot.getValue(DeliveryAddressModel.class);
                                    if (deliveryAddressModel.getUser_id() != null && deliveryAddressModel.getUser_id().equals(userID)) {
                                        deliveryAddressModelArrayList.add(deliveryAddressModel);
                                    }
                                    deliveryAddressItemSelectAdapter = new DeliveryAddressItemSelectAdapter(getActivity(), deliveryAddressModelArrayList, alertDialog, selected_address);
                                    delivery_address_recycler.setAdapter(deliveryAddressItemSelectAdapter);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        alertDialog.show();
                    }
                });

                p_code.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.OrderCustomAlertDialog);
                        ViewGroup viewGroup = container.findViewById(android.R.id.content);
                        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.order_customview_for_select, viewGroup, false);
                        builder.setView(dialogView);
                        final AlertDialog alertDialog = builder.create();

                        RecyclerView recyclerView;

                        ArrayList<PromoCodeModel> promoCodeModelArrayList = new ArrayList<>();
                        recyclerView = dialogView.findViewById(R.id.recycler_view);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        FirebaseDatabase.getInstance().getReference("promo_codes").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                promoCodeModelArrayList.clear();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    PromoCodeModel promoCodeModel = dataSnapshot.getValue(PromoCodeModel.class);
                                    promoCodeModelArrayList.add(promoCodeModel);

                                    promoCodeItemSelectAdapter = new PromoCodeItemSelectAdapter(getActivity(), promoCodeModelArrayList, alertDialog, selected_coupon, total_price, tv);
                                    recyclerView.setAdapter(promoCodeItemSelectAdapter);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        alertDialog.show();
                    }
                });

                payment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.OrderCustomAlertDialog);
                        ViewGroup viewGroup = container.findViewById(android.R.id.content);
                        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.order_customview_for_select, viewGroup, false);
                        builder.setView(dialogView);
                        final AlertDialog alertDialog = builder.create();

                        RecyclerView recyclerView;

                        ArrayList<PaymentMethodListModel> paymentMethodListModels = new ArrayList<>();
                        recyclerView = dialogView.findViewById(R.id.recycler_view);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                        paymentMethodListModels.add(new PaymentMethodListModel("cod", "Cash on delivery", Config.codIconUrl));
                        paymentMethodListModels.add(new PaymentMethodListModel("razorpay", "Pay using Razorpay", Config.razorPayIconUrl));
                        paymentMethodListModels.add(new PaymentMethodListModel("stripe", "Pay using Stripe", Config.stripeIconUrl));

                        paymentMethodListItemAdapter = new PaymentMethodListItemAdapter(getActivity(), paymentMethodListModels, alertDialog, selected_payment_method, p_method_icon);
                        recyclerView.setAdapter(paymentMethodListItemAdapter);
                        alertDialog.show();

                    }
                });

                bottomSheetView.findViewById(R.id.place_order_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selected_address.getText().toString().startsWith("Select")) {
                            Toast.makeText(getContext(), "Please Select Delivery Address", Toast.LENGTH_LONG).show();
                        } else if (TextUtils.isEmpty(selected_payment_method.getText().toString())) {
                            Toast.makeText(getContext(), "Please Select Payment Method", Toast.LENGTH_LONG).show();
                        } else {
                            order_created_at = System.currentTimeMillis();
                            if (selected_payment_method.getText().toString().toLowerCase().equals("cod")) {
                                saveOrderAndPaymentDetailsToDatabase();
                            } else if (selected_payment_method.getText().toString().toLowerCase().equals("razorpay")) {
                                Intent intent = new Intent(getActivity(), RazorpayPaymentActivity.class);
                                intent.putExtra("amount", tv.getText().toString());
                                intent.putExtra("selected_address", selected_address.getText().toString());
                                getContext().startActivity(intent);
                            } else if (selected_payment_method.getText().toString().toLowerCase().equals("stripe")) {
                                Intent intent = new Intent(getActivity(), StripePaymentActivity.class);
                                intent.putExtra("amount", tv.getText().toString());
                                intent.putExtra("selected_address", selected_address.getText().toString());
                                getContext().startActivity(intent);
                            }
                            bottomSheetDialog.dismiss();
                        }

                    }
                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });

        readCartItem(userID);
        return view;
    }

    private void showOrderPlaced(ViewGroup container, View v) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.OrderCustomAlertDialog);
        ViewGroup viewGroup = container.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.order_customview, viewGroup, false);
        TextView buttonOk = dialogView.findViewById(R.id.buttonOk);
        Button track_order_btn = dialogView.findViewById(R.id.track_order_btn);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        track_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrderFailed(container, v);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void showOrderFailed(ViewGroup container, View v) {

        final AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity(), R.style.OrderCustomAlertDialog);
        ViewGroup viewGroup1 = container.findViewById(android.R.id.content);
        View dialogView1 = LayoutInflater.from(v.getContext()).inflate(R.layout.order_failed_customview, viewGroup1, false);
        TextView buttonOk1 = dialogView1.findViewById(R.id.buttonOk);
        Button try_again_btn = dialogView1.findViewById(R.id.try_again_btn);
        builder1.setView(dialogView1);
        final AlertDialog alertDialog1 = builder1.create();
        buttonOk1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
            }
        });
        try_again_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Coming soon", Toast.LENGTH_LONG).show();
                alertDialog1.dismiss();
            }
        });
        alertDialog1.show();
    }

    private void readCartItem(String uid) {
        /**/
        //call for first time loading cart data
        FirebaseDatabase.getInstance().getReference("cart").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                total_price = 0;
                cart_total_price.setVisibility(View.GONE);
                goto_checkout_btn.setVisibility(View.GONE);
                cart_progressBar.setVisibility(View.VISIBLE);
                cartList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CartItemModel cartItemModel = dataSnapshot.getValue(CartItemModel.class);

                    cartList.add(cartItemModel);
                    cartItemAdapter = new CartItemAdapter(getActivity(), cartList, userID);
                    cart_recycler.setAdapter(cartItemAdapter);
                }
                cart_progressBar.setVisibility(View.GONE);
                if (cartList.size() > 0) {
                    cart_total_price.setVisibility(View.VISIBLE);
                    goto_checkout_btn.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //call for updating cart data
        FirebaseDatabase.getInstance().getReference("cart").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                total_price = 0;
                cart_total_price.setVisibility(View.GONE);
                goto_checkout_btn.setVisibility(View.GONE);
                cart_progressBar.setVisibility(View.VISIBLE);
                cartList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CartItemModel cartItemModel = dataSnapshot.getValue(CartItemModel.class);

                    cartList.add(cartItemModel);
                    total_price += (cartItemModel.getAdded_unit() * (cartItemModel.getUnit_price()));
                    cart_total_price.setText("Total: $" + String.format("%.2f", total_price));
                }
                if (cartList.size() > 0) {
                    if (cartItemAdapter == null) {
                        cartItemAdapter = new CartItemAdapter(getActivity(), cartList, userID);
                        cart_recycler.setAdapter(cartItemAdapter);
                    }
                    cartItemAdapter.notifyDataSetChanged();
                    cart_total_price.setVisibility(View.VISIBLE);
                    goto_checkout_btn.setVisibility(View.VISIBLE);
                }
                cart_progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void saveOrderAndPaymentDetailsToDatabase() {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("orders");
        String orderID = orderRef.push().getKey();

        HashMap<String, Object> order_status = new HashMap<>();
        order_status.put("created_at", order_created_at);
        order_status.put("order_status", "Waiting for Payment");

        HashMap<String, Object> order_status_map = new HashMap<>();
        order_status_map.put(order_created_at + "", order_status);

        HashMap<String, Object> map = new HashMap<>();
        map.put("order_id", orderID);
        map.put("order_total_amount", tv.getText().toString());
        map.put("order_items", cartList);
        map.put("delivery_address", selected_address.getText().toString());
        map.put("user_id", userID);
        map.put("order_status", order_status_map);
        orderRef.child(orderID).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                FirebaseDatabase.getInstance().getReference("cart").child(userID).removeValue();
                Toast.makeText(getContext(), "Order Placed", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getActivity(), AllOrderActivity.class));
            }
        });
    }
}