package com.feup.acme_cafe_terminal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.feup.acme_cafe_terminal.utils.*;
import com.google.android.material.snackbar.Snackbar;

public class OrderFrag extends Fragment{


    private RequestQueue queue;
    TextView text;
    Button button;
    ProgressBar spinner;

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

        text = view.findViewById(R.id.textview_first);
        button = view.findViewById(R.id.button_first);
        spinner = view.findViewById(R.id.spinner);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                spinner.setVisibility(View.VISIBLE);
                text.setVisibility(View.GONE);
                button.setEnabled(false);
                String urlProducts = "http://" + Constants.ip_address + ":3000/user/checkout";
                JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.POST, urlProducts,
                        ((MainActivity)getActivity()).getMyData(),
                        response -> {
                            ((MainActivity)getActivity()).setOrderId(response.optInt("orderId",420));
                            goToNextFrag();
                            spinner.setVisibility(View.GONE);
                            text.setVisibility(View.VISIBLE);
                        },
                        error -> {
                            ((MainActivity)getActivity()).setOrderId(-1);
                            goToNextFrag();
                            spinner.setVisibility(View.GONE);
                            text.setVisibility(View.VISIBLE);
                            button.setEnabled(true);
                            Snackbar.make(view, error.toString() , Snackbar.LENGTH_SHORT).show();
                        }
                ) {};
                queue.add(jsonObj);
            }
        });
    }

    public void goToNextFrag()
    {
        NavHostFragment.findNavController(OrderFrag.this)
                .navigate(R.id.action_FirstFragment_to_SecondFragment);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!((MainActivity)getActivity()).hasReadCodeOnce()) return;
        ((MainActivity)getActivity()).resetReadState();

        spinner.setVisibility(View.VISIBLE);
        text.setVisibility(View.GONE);
        button.setEnabled(false);

        JSONArray productArray = ((MainActivity)getActivity()).getProductData();

        String temp = "";

        try {
            JSONObject order = ((MainActivity)getActivity()).getMyData();

            JSONArray orderList = order.getJSONArray("products");

            temp = "\nVoucher: ";

            try {
                if (order.getJSONObject("voucher").isNull("id"))
                    temp += "No";
                else
                    temp += "Yes";
            } catch (JSONException|NullPointerException e) {
                temp += "Invalid/Error";
            }

            temp += "\n\nOrder:";

            float cost = 0;

            for (int j = 0; j < orderList.length(); j++) {
                JSONObject ordProd = orderList.getJSONObject(j);
                for (int i = 0; i < productArray.length(); i++) {
                    try {
                        JSONObject prod = productArray.getJSONObject(i);
                        if (prod.getString("id").equals(ordProd.getString("id"))) {
                            temp += "\n- " + prod.getString("name") + " x " + ordProd.getInt("count");
                            cost += prod.getDouble("value") * ordProd.getInt("count");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            temp += "\n\n\n\nTotal: " + String.format("%.02f €",cost);

            String urlProducts = "http://" + Constants.ip_address + ":3000/user/id/" + order.getString("UserId");
            String finalTemp = temp;
            JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.GET, urlProducts, new JSONObject(),
                    response -> {
                        try {
                            text.setText("User:\n- " + response.getString("name") + "\n" + finalTemp);
                            spinner.setVisibility(View.GONE);
                            text.setVisibility(View.VISIBLE);
                            button.setEnabled(true);
                        } catch (JSONException e) {
                            text.setText("Bad server response!");
                            spinner.setVisibility(View.GONE);
                            text.setVisibility(View.VISIBLE);
                            button.setEnabled(false);
                        }
                    },
                    error -> {
                        text.setText("Failed to aquire user info!");
                        spinner.setVisibility(View.GONE);
                        text.setVisibility(View.VISIBLE);
                        button.setEnabled(false);
                    }
            ) {};
            queue.add(jsonObj);


        } catch (JSONException|NullPointerException e) {
            e.printStackTrace();
            text.setText("Invalid QR code!");
            spinner.setVisibility(View.GONE);
            text.setVisibility(View.VISIBLE);
            button.setEnabled(false);
        }



    }
}