package com.feup.acme_cafe_terminal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;

import java.io.IOException;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.feup.acme_cafe_terminal.utils.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FirstFragment extends Fragment{


    private RequestQueue queue;
    TextView text;
    Button button;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        queue = Volley.newRequestQueue(getContext());
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        text = (TextView)view.findViewById(R.id.textview_first);
        button = view.findViewById(R.id.button_first);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                button.setEnabled(false);
                String urlProducts = "http://" + Constants.ip_address + ":3000/user/checkout";
                JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.POST, urlProducts,
                        ((MainActivity)getActivity()).getMyData(),
                        response -> {
                            goToNextFrag();
                        },
                        error -> {
                        }
                ) {};
                queue.add(jsonObj);
            }
        });
    }

    public void goToNextFrag()
    {
        NavHostFragment.findNavController(FirstFragment.this)
                .navigate(R.id.action_FirstFragment_to_SecondFragment);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!((MainActivity)getActivity()).hasReadCodeOnce()) return;
        ((MainActivity)getActivity()).resetReadState();

        JSONArray productArray = ((MainActivity)getActivity()).getProductData();

        String temp = "";

        try {
            JSONObject order = ((MainActivity)getActivity()).getMyData();
            JSONArray orderList = order.getJSONArray("products");

            temp = "\nVoucher: ";

            try {
                order.getString("voucher");
                temp += "Yes";
            } catch (JSONException|NullPointerException e) {
                temp += "No";
            }

            temp += "\nOrder:";

            for (int j = 0; j < orderList.length(); j++) {
                JSONObject ordProd = orderList.getJSONObject(j);
                for (int i = 0; i < productArray.length(); i++) {
                    try {
                        JSONObject prod = productArray.getJSONObject(i);
                        if (prod.getString("id").equals(ordProd.getString("id")))
                            temp += "\n" + prod.getString("name") + " x " + ordProd.getInt("count");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            String urlProducts = "http://" + Constants.ip_address + ":3000/user/id/" + order.getString("UserId");
            String finalTemp = temp;
            JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.GET, urlProducts, new JSONObject(),
                    response -> {
                        try {
                            text.setText("User: " + response.getString("name") + finalTemp);
                            button.setEnabled(true);
                        } catch (JSONException e) {
                            text.setText("Bad server response!");
                            button.setEnabled(false);
                        }
                    },
                    error -> {
                        text.setText("Failed to aquire user info!");
                        button.setEnabled(false);
                    }
            ) {};
            queue.add(jsonObj);


        } catch (JSONException|NullPointerException e) {
            e.printStackTrace();
            text.setText("Invalid QR code!");
            button.setEnabled(false);
        }



    }
}