package com.abuubaida921.esupermarkt.paymentgateway;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StripePaymentActivity extends AppCompatActivity {

    String CustomerID, PaymentID, EphemeralKey, ClientSecret;
    PaymentSheet paymentSheet;
    String deliveryAddress;
    String to_pay, final_to_pay;
    long order_created_at;
    String userID;
    ArrayList<CartItemModel> cartList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe_payment);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        paymentSheet = new PaymentSheet(this, this::onPaymentResult);

        userID = FirebaseAuth.getInstance().getUid();
        to_pay = getIntent().getStringExtra("amount");
        deliveryAddress = getIntent().getStringExtra("selected_address");
        order_created_at = System.currentTimeMillis();
        int d = (int) (Double.parseDouble(to_pay) * 100);
        final_to_pay = String.valueOf(Math.ceil(d));
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
                    startStripePayment();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void startStripePayment() {

        PaymentConfiguration.init(this, Config._StripePublishableKey);

        StringRequest request = new StringRequest(Request.Method.POST, Config.StripeCustomerURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    CustomerID = jsonObject.getString(Config.ID);
                    getEphemeralKey();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put(Config.StripeAuthorization, Config.StripeAuthorizationBearer + Config._StripeSecretKey);
                return header;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            saveOrderAndPaymentDetailsToDatabase(System.currentTimeMillis());
            Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void getEphemeralKey() {
        StringRequest request = new StringRequest(Request.Method.POST, Config.StripeEphemeralURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    EphemeralKey = jsonObject.getString(Config.ID);
                    getClientSecret();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put(Config.StripeAuthorization, Config.StripeAuthorizationBearer + Config._StripeSecretKey);
                header.put(Config.StripeVersionTXT, Config.StripeVersion);
                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put(Config.StripeCustomer, CustomerID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void getClientSecret() {
        StringRequest request = new StringRequest(Request.Method.POST, Config.StripePaymentURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    ClientSecret = jsonObject.getString(Config.StripeClientSecret);
                    PaymentID = jsonObject.getString(Config.ID);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                paymentSheet.presentWithPaymentIntent(ClientSecret, new PaymentSheet.Configuration(getApplicationContext().getPackageManager().getApplicationLabel(getApplicationContext().getApplicationInfo()).toString(), new PaymentSheet.CustomerConfiguration(CustomerID, EphemeralKey)));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put(Config.StripeAuthorization, Config.StripeAuthorizationBearer + Config._StripeSecretKey);
                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put(Config.StripeCustomer, CustomerID);
                params.put(Config.StripeAmount, final_to_pay.replaceAll("\\.\\d+$", ""));
                params.put(Config.StripeCurrency, "USD");
                params.put(Config.StripeAutoPayment, "true");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
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
                        Toast.makeText(StripePaymentActivity.this, "Order Placed", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(StripePaymentActivity.this, AllOrderActivity.class));
                        finish();
                    }
                });
            }
        });
    }
}