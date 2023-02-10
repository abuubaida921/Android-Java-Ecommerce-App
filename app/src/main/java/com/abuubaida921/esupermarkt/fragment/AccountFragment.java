package com.abuubaida921.esupermarkt.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.abuubaida921.esupermarkt.R;
import com.abuubaida921.esupermarkt.activity.AboutActivity;
import com.abuubaida921.esupermarkt.activity.AllOrderActivity;
import com.abuubaida921.esupermarkt.activity.ChangePasswordActivity;
import com.abuubaida921.esupermarkt.activity.DeliveryAddressesActivity;
import com.abuubaida921.esupermarkt.activity.FaqActivity;
import com.abuubaida921.esupermarkt.activity.LoginActivity;
import com.abuubaida921.esupermarkt.activity.PaymentHistoryActivity;
import com.abuubaida921.esupermarkt.activity.PromoCodeActivity;
import com.google.firebase.auth.FirebaseAuth;

public class AccountFragment extends Fragment {

    RelativeLayout showOrders, changePass, showDeliveryAddress, showPaymentMethod, showPromoCode, showFaq, showAboutUs;
    Button logout_btn;

    public AccountFragment() {
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
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        logout_btn = view.findViewById(R.id.logout_btn);
        showOrders = view.findViewById(R.id.showOrders);
        changePass = view.findViewById(R.id.changePass);
        showDeliveryAddress = view.findViewById(R.id.showDeliveryAddress);
        showPaymentMethod = view.findViewById(R.id.showPaymentMethod);
        showPromoCode = view.findViewById(R.id.showPromoCode);
        showFaq = view.findViewById(R.id.showFaq);
        showAboutUs = view.findViewById(R.id.showAboutUs);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });
        showOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AllOrderActivity.class));
            }
        });
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
            }
        });
        showDeliveryAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DeliveryAddressesActivity.class));
            }
        });
        showPaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PaymentHistoryActivity.class));
            }
        });
        showPromoCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PromoCodeActivity.class));
            }
        });
        showFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FaqActivity.class));
            }
        });
        showAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AboutActivity.class));
            }
        });
        return view;
    }
}