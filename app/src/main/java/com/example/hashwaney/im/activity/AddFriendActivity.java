package com.example.hashwaney.im.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hashwaney.bean.User;
import com.example.hashwaney.im.R;
import com.example.hashwaney.im.adapter.AddFriendAdapter;
import com.example.hashwaney.im.base.BaseActivity;
import com.example.hashwaney.im.db.DBUtils;
import com.example.hashwaney.im.presenter.IAddFriendPresenter;
import com.example.hashwaney.im.presenter.impl.AddFriendPresenter;
import com.example.hashwaney.im.view.IAddFriendView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AddFriendActivity
        extends BaseActivity
        implements SearchView.OnQueryTextListener,
                   IAddFriendView,
                   AddFriendAdapter.onAddFriendClickListener
{

    private IAddFriendPresenter mAddFriendPresenter;

    @InjectView(R.id.toolbar)
    Toolbar      mToolbar;
    @InjectView(R.id.iv_nodata)
    ImageView    mIvNodata;
    @InjectView(R.id.recycleview)
    RecyclerView mRecycleview;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.inject(this);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAddFriendPresenter = new AddFriendPresenter(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //布局
        getMenuInflater().inflate(R.menu.add_friend_menu, menu);

        //视图
        MenuItem   item       = menu.findItem(R.id.search);
        mSearchView = (SearchView) item.getActionView();
        /**
         * 给searchview设置提示
         *
         */
        mSearchView.setQueryHint("用户名");
        mSearchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }


        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (TextUtils.isEmpty(query)) {
            Toast.makeText(this, "输入框不能为空", Toast.LENGTH_SHORT)
                 .show();
            return false;
        }
        //添加好友
        mAddFriendPresenter.onSearchFriend(query);

        InputMethodManager methodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
     methodManager.hideSoftInputFromWindow(mSearchView.getWindowToken(),0);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!TextUtils.isEmpty(newText)) {

            showToast(newText);

        }
        return true;

    }

    @Override
    public void onSearchSuccess(String currentUser, boolean b, String msg, List<User> list) {
        if (b) {
            //显示recycleview
            mRecycleview.setVisibility(View.VISIBLE);
            mIvNodata.setVisibility(View.GONE);
            //显示数据
            mRecycleview.setLayoutManager(new LinearLayoutManager(this));
            List<String>     contactList = DBUtils.getContact(currentUser);
            AddFriendAdapter adapter     = new AddFriendAdapter(list, contactList);
            mRecycleview.setAdapter(adapter);

            adapter.setOnAddFriendClickListener(this);

        } else {
            //隐藏recycleview
            mRecycleview.setVisibility(View.GONE);
            mIvNodata.setVisibility(View.VISIBLE);

        }


    }

    @Override
    public void onAddFriendFormService(String username, boolean b, String msg) {

        if (b) {
          //如果添加成功
            showToast("添加"+username+"成功");

        } else {
            showToast("添加"+username+"失败"+msg);
        }


    }

    //点击添加按钮的回调
    @Override
    public void onAddFriendClick(String username) {
        mAddFriendPresenter.onAddFriend(username);


    }
}
