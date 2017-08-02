package com.service.customer.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.service.customer.ui.contract.implement.FragmentViewImplement;

public class MineFragment extends FragmentViewImplement {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void findViewById() {
        
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {

    }

    @Override
    protected void setListener() {

    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
