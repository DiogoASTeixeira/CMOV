package com.feup.acme_cafe_terminal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;


public class ResultFrag extends Fragment{

    View curView;
    TextView order_id;
    TextView result_text;
    ImageView error_img;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        curView = view;
        order_id = view.findViewById(R.id.order_id);
        result_text = view.findViewById(R.id.textview_second);
        error_img = view.findViewById(R.id.error_sign);
        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ResultFrag.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        int order_num = ((MainActivity) getActivity()).getOrderId();
        order_id.setText(String.format("%03d",order_num));
        if (order_num < 0)
        {
            error_img.setVisibility(View.VISIBLE);
            order_id.setVisibility(View.GONE);
            result_text.setText(getString(R.string.fail_second_fragment));
        }
        else
        {
            error_img.setVisibility(View.GONE);
            order_id.setVisibility(View.VISIBLE);
            result_text.setText(getString(R.string.succ_second_fragment));
        }


        if (((MainActivity) getActivity()).hasReadCodeOnce())
            NavHostFragment.findNavController(ResultFrag.this)
                    .navigate(R.id.action_SecondFragment_to_FirstFragment);
    }
}