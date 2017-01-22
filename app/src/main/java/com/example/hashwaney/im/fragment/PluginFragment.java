package com.example.hashwaney.im.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.hashwaney.im.MainActivity;
import com.example.hashwaney.im.R;
import com.example.hashwaney.im.activity.LoginActivity;
import com.example.hashwaney.im.base.BaseFragment;
import com.example.hashwaney.im.presenter.impl.PluginPresenter;
import com.example.hashwaney.im.view.IPluginView;

/**
 * Created by HashWaney on 2017/1/21.
 */

public class PluginFragment
        extends BaseFragment
        implements View.OnClickListener, IPluginView
{

    private Button          mBtnLogout;
    private PluginPresenter mPluginPresenter;
    private ProgressDialog  mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)

    {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setCancelable(false);
        return inflater.inflate(R.layout.fragment_plugin, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtnLogout = (Button) view.findViewById(R.id.btn_logout);
        mBtnLogout.setOnClickListener(this);
        mPluginPresenter = new PluginPresenter(this);


    }

    @Override
    public void onClick(View v) {
        //显示进度条
        mProgressDialog.setMessage("正在退出....");
        mProgressDialog.show();
        //进行退出的逻辑
        mPluginPresenter.logOut();
    }

    @Override
    public void onLogout(boolean b, String s) {
        //隐藏进度条
        mProgressDialog.hide();

        if (b) {
            //跳转到登录界面
            MainActivity activity = (MainActivity) getActivity();
            activity.startActivity(LoginActivity.class, true);
        } else {

            Toast.makeText(getActivity(), "退出失败" + s, Toast.LENGTH_SHORT)
                 .show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mProgressDialog.dismiss();
    }
}
