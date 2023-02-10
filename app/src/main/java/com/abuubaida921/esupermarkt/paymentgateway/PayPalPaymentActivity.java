package com.abuubaida921.esupermarkt.paymentgateway;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;

import android.app.Application;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.abuubaida921.esupermarkt.R;

public class PayPalPaymentActivity extends AppCompatActivity {

//    String YOUR_CLIENT_ID = "replace_with_your_key";
//    String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal_payment);
        Button payPalButton = findViewById(R.id.pp_btn);
        payPalButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
//                createOrder(); //this will trigger the checkout flow
            }
        });
    }

//    String encodeStringToBase64() {
//        String input = "<CLIENT_ID>:<CLIENT_SECRET>";
//        String encodedString = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            encodedString = Base64.getEncoder().encodeToString(input.getBytes());
//        }
//        return encodedString;
//    }
//
//    void getAccessToken() {
//        String AUTH = encodeStringToBase64();
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.addHeader("Accept", "application/json");
//        client.addHeader("Content-type", "application/x-www-form-urlencoded");
//        client.addHeader("Authorization", "Basic " + AUTH);
//        String jsonString = "grant_type=client_credentials";
//
//        HttpEntity entity = new StringEntity(jsonString, "utf-8");
//
//        client.post(this, "https://api-m.sandbox.paypal.com/v1/oauth2/token", entity, "application/x-www-form-urlencoded", new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
//                Log.e("RESPONSE", response);
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, String response) {
//                try {
//                    JSONObject jobj = new JSONObject(response);
//                    accessToken = jobj.getString("access_token");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//    void createOrder(){
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.addHeader("Accept", "application/json");
//        client.addHeader("Content-type", "application/json");
//        client.addHeader("Authorization", "Bearer " + accessToken);
//
//        String order = "{"
//                + "\"intent\": \"CAPTURE\","
//                + "\"purchase_units\": [\n" +
//                "      {\n" +
//                "        \"amount\": {\n" +
//                "          \"currency_code\": \"SGD\",\n" +
//                "          \"value\": \"1.00\"\n" +
//                "        }\n" +
//                "      }\n" +
//                "    ]}";
//        HttpEntity entity = new StringEntity(order, "utf-8");
//
//        client.post(this, "https://api-m.sandbox.paypal.com/v2/checkout/orders", entity, "application/json",new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
//                Log.e("RESPONSE", response);
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, String response) {
//                Log.i("RESPONSE", response);
//            }
//        });
//    }
//
//    @Override
//    public void onSuccess(int statusCode, Header[] headers, String response) {
//        try {
//            JSONArray links = new JSONObject(response).getJSONArray("links");
//
//            //iterate the array to get the approval link
//            for (int i = 0; i < links.length(); ++i) {
//                String rel = links.getJSONObject(i).getString("rel");
//                if (rel.equals("approve")){
//                    String link = linkObj.getString("href");
//                    //redirect to this link via CCT
//                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
//                    CustomTabsIntent customTabsIntent = builder.build();
//                    customTabsIntent.launchUrl(PayPalPaymentActivity.this, Uri.parse(link));
//                }
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }



}