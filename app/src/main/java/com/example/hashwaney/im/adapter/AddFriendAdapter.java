package com.example.hashwaney.im.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.hashwaney.bean.User;
import com.example.hashwaney.im.R;

import java.util.List;

/**
 * Created by HashWaney on 2017/1/23.
 */

public class AddFriendAdapter
        extends RecyclerView.Adapter<AddFriendAdapter.AddFriendViewHolder>
{
    private List<User> mUserList;
    private List<String> mContactList;
    public AddFriendAdapter(List<User> userList,List<String> contactList) {
        mUserList = userList;
        mContactList =contactList;

    }

    @Override
    public AddFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View                view = LayoutInflater.from(parent.getContext())
                                                    .inflate(R.layout.list_item_search, parent, false);
        AddFriendViewHolder holder =new AddFriendViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(AddFriendViewHolder holder, int position) {

        User         user      = mUserList.get(position);
        String       createdAt = user.getCreatedAt();
        final String username  = user.getUsername();
        holder.mTvTime.setText(createdAt);
        holder.mTvUsername.setText(username);

        //添加和已是好友的区分
        holder.mBtnAdd.setText(mContactList.contains(username)?"已是好友":"添加");
        if (mContactList.contains(username)){
            holder.mBtnAdd.setEnabled(false);

        }else {
            holder.mBtnAdd.setEnabled(true);
        }

        holder.mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnAddFriendClickListener!=null){
                    mOnAddFriendClickListener.onAddFriendClick(username);

                }
            }
        });

    }

    //定义一个接口用来回调添加功能
    public interface onAddFriendClickListener{

        void onAddFriendClick(String username);
    }
    private onAddFriendClickListener mOnAddFriendClickListener;
    public void setOnAddFriendClickListener(onAddFriendClickListener listener){
        mOnAddFriendClickListener =listener;

    }

    @Override
    public int getItemCount() {
        return mUserList==null?0:mUserList.size();
    }

    class AddFriendViewHolder extends RecyclerView.ViewHolder{

        TextView mTvTime;
        TextView mTvUsername;
        Button mBtnAdd;

        public AddFriendViewHolder(View itemView) {
            super(itemView);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
            mTvUsername = (TextView) itemView.findViewById(R.id.tv_username);
            mBtnAdd = (Button) itemView.findViewById(R.id.btn_add);
        }
    }
}
