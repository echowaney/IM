package com.example.hashwaney.im.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hashwaney.im.R;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by HashWaney on 2017/1/25.
 */

public class ConversationAdapter
        extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder>
{
    //    @Override
    //    public int getItemViewType(int position) {
    //        return super.getItemViewType(position);
    //    }
    //拿到会话数据
    private List<EMConversation> emConversationList;

    public ConversationAdapter(List<EMConversation> emConversationList) {this.emConversationList = emConversationList;}


    @Override
    public ConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //只有一种视图 如果有多种视图 需要去复写一个方法 getItemViewType'
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.list_item_conversation, parent, false);
        //将view绑定到viewholder上

        ConversationViewHolder holder = new ConversationViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(ConversationViewHolder holder, int position) {
        //将数据与视图的绑定
        EMConversation    emConversation = emConversationList.get(position);
        final String      userName       = emConversation.getUserName(); //拿到和你聊天的用户名
        EMMessage         lastMessage    = emConversation.getLastMessage();  //拿到最后一条消息
        long              msgTime        = lastMessage.getMsgTime();
        int               unreadMsgCount = emConversation.getUnreadMsgCount();
        EMTextMessageBody emMessageBody  = (EMTextMessageBody) lastMessage.getBody();//拿到消息的内容

        //视图与数据的绑定
        holder.mTvMsg.setText(emMessageBody.getMessage());
        holder.mTvTime.setText(DateUtils.getTimestampString(new Date(msgTime)));
        holder.mTvUsername.setText(userName);
        if (unreadMsgCount>99){
            //显示视图
            holder.mTvUnread.setVisibility(View.VISIBLE);

            holder.mTvUnread.setText("99+");
        }else if (unreadMsgCount>0){
            holder.mTvUnread.setVisibility(View.VISIBLE);
            holder.mTvUnread.setText(unreadMsgCount+"");
        }else {
            holder.mTvUnread.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener !=null){
                    mOnItemClickListener.onItemClick(userName);

                }
            }
        });

    }

    //接口回调 设置条目的点击事件 跳转到聊天界面
    public interface OnItemClickListener {
        void  onItemClick(String username);

    }
    private OnItemClickListener mOnItemClickListener;
    public  void setOnIntemClick(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener=onItemClickListener;

    }


    @Override
    public int getItemCount() {
        return emConversationList == null
               ? 0
               : emConversationList.size();
    }

    //    TODO 如果说将viewholder声明为静态的会怎样
    static class ConversationViewHolder
            extends RecyclerView.ViewHolder
    {

        TextView mTvTime;
        TextView mTvUsername;
        TextView mTvMsg;
        TextView mTvUnread;

        public ConversationViewHolder(View itemView) {
            super(itemView);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
            mTvUsername = (TextView) itemView.findViewById(R.id.tv_username);
            mTvMsg = (TextView) itemView.findViewById(R.id.tv_msg);
            mTvUnread = (TextView) itemView.findViewById(R.id.tv_uread);
        }

    }
}
