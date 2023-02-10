package com.abuubaida921.esupermarkt.paymentgateway;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.abuubaida921.esupermarkt.Config;
import com.abuubaida921.esupermarkt.R;
import com.abuubaida921.esupermarkt.activity.AllOrderActivity;
import com.abuubaida921.esupermarkt.activity.PaymentHistoryActivity;
import com.abuubaida921.esupermarkt.model.CartItemModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RazorpayPaymentActivity extends Activity implements PaymentResultListener {

    String PaymentID, deliveryAddress;
    String to_pay;
    int final_to_pay;
    long order_created_at;
    String userID;
    ArrayList<CartItemModel> cartList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razorpay_payment);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        Checkout.preload(getApplicationContext());

        userID = FirebaseAuth.getInstance().getUid();
        to_pay = getIntent().getStringExtra("amount");
        deliveryAddress = getIntent().getStringExtra("selected_address");
        order_created_at = System.currentTimeMillis();
        final_to_pay = (int) (100 * Double.parseDouble(to_pay));
        loadCartData();
    }

    private void loadCartData() {

        //call for updating cart data
        FirebaseDatabase.getInstance().getReference("cart").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CartItemModel cartItemModel = dataSnapshot.getValue(CartItemModel.class);
                    cartList.add(cartItemModel);
                }
                if (cartList.size() > 0) {
                    startRazorpayPayment();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void startRazorpayPayment() {
        Checkout checkout = new Checkout();
        checkout.setImage(R.mipmap.ic_launcher);
        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();
            options.put(Config.RazorPayName, getApplicationContext().getPackageManager().getApplicationLabel(getApplicationContext().getApplicationInfo()).toString());
            options.put(Config.RazorPayDesc, "Dummy Payment");
            options.put(Config.RazorPaySMSHash, true);
            options.put(Config.RazorPayAllowRotation, false);

            options.put(Config.StripeCurrency, "USD");
            options.put(Config.StripeAmount, final_to_pay);

            JSONObject prefilled = new JSONObject();
            prefilled.put(Config.RazorPayEmail, "admin@gmail.com");
            prefilled.put(Config.RazorPayContact, "01712121212");

            options.put(Config.RazorPayPrefill, prefilled);
            checkout.open(activity, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        PaymentID = s;
        saveOrderAndPaymentDetailsToDatabase(System.currentTimeMillis());
        Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void saveOrderAndPaymentDetailsToDatabase(long paidAt) {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("orders");
        DatabaseReference paymentRef = FirebaseDatabase.getInstance().getReference("payments");
        String paymentID = paymentRef.push().getKey();
        String orderID = orderRef.push().getKey();

        HashMap<String, Object> order_status = new HashMap<>();
        order_status.put("created_at", order_created_at);
        order_status.put("order_status", "Waiting for Payment");

        HashMap<String, Object> pay_status = new HashMap<>();
        pay_status.put("created_at", paidAt);
        pay_status.put("order_status", "Payment Successful");

        HashMap<String, Object> order_status_map = new HashMap<>();
        order_status_map.put(order_created_at + "", order_status);
        order_status_map.put(paidAt + "", pay_status);

        HashMap<String, Object> map = new HashMap<>();
        map.put("order_id", orderID);
        map.put("payment_id", paymentID);
        map.put("order_total_amount", to_pay);
        map.put("order_items", cartList);
        map.put("delivery_address", deliveryAddress);
        map.put("user_id", userID);
        map.put("order_status", order_status_map);
        orderRef.child(orderID).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                HashMap<String, Object> paymentMap = new HashMap<>();
                paymentMap.put("order_id", orderID);
                paymentMap.put("payment_id", paymentID);
                paymentMap.put("paid_at", paidAt);
                paymentMap.put("txn_id", PaymentID);
                paymentMap.put("amount", to_pay);
                paymentMap.put("user_id", userID);
                paymentRef.child(paymentID).setValue(paymentMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseDatabase.getInstance().getReference("cart").child(userID).removeValue();
                        Toast.makeText(RazorpayPaymentActivity.this, "Order Placed", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(RazorpayPaymentActivity.this, AllOrderActivity.class));
                        finish();
                    }
                });
            }
        });
    }
}