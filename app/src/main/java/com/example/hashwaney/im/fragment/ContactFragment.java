package com.example.hashwaney.im.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hashwaney.im.R;
import com.example.hashwaney.im.base.BaseFragment;

/**
 * Created by HashWaney on 2017/1/21.
 */

public class ContactFragment
        extends BaseFragment
{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }
}
