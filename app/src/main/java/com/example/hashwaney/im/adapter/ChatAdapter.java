package com.example.hashwaney.im.adapter;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hashwaney.im.R;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by HashWaney on 2017/1/24.
 */

public class ChatAdapter
        extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>
{

    // TODO 数据源
    private List<EMMessage> emMessagesLists;


    public ChatAdapter(List<EMMessage> emMessagesLists) {
        this.emMessagesLists = emMessagesLists;
    }

    @Override
    public int getItemViewType(int position) {
        EMMessage        emMessage = emMessagesLists.get(position);
        EMMessage.Direct direct    = emMessage.direct();
        return direct == EMMessage.Direct.RECEIVE
               ? 0
               : 1;
    }


    /**
     * 该方法调用之前,系统会先走getItemViewType这个方法获取布局的类型.
     * 拿到类型之后.就应该填充相应的布局
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //        TODO 视图
        View mView = null;
        switch (viewType) {
            case 0:
                mView = LayoutInflater.from(parent.getContext())
                                      .inflate(R.layout.list_chat_receive, parent, false);
                break;
            case 1:
                mView = LayoutInflater.from(parent.getContext())
                                      .inflate(R.layout.list_chat_send, parent, false);

                break;
        }
        ChatViewHolder holder = new ChatViewHolder(mView);
        return holder;
    }

    //视图和数据的绑定了
    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        EMMessage emMessage = emMessagesLists.get(position);
        long      msgTime   = emMessage.getMsgTime();
        //
        holder.mTvTime.setText(DateUtils.getTimestampString(new Date(msgTime)));
        //        com.hyphenate.chat.EMMessageBody; 返回来的是json字符串，因此可以返回文本信息内容
        //显示聊天内容
        EMTextMessageBody messageBody = (EMTextMessageBody) emMessage.getBody();
        Log.d("result", "onBindViewHolder: " + messageBody.getMessage());
        holder.mTvMsg.setText(messageBody.getMessage());

        if (position == 0) {
            //第一条消息时间肯定是要显示的m
            holder.mTvTime.setVisibility(View.VISIBLE);
        } else {
            EMMessage preMessage = emMessagesLists.get(position - 1);
            long      preMsgTime = preMessage.getMsgTime();
            if (DateUtils.isCloseEnough(preMsgTime, msgTime)) {

                holder.mTvTime.setVisibility(View.GONE);
            } else {
                holder.mTvTime.setVisibility(View.VISIBLE);

            }

        }
        //发送的逻辑判断
        if (emMessage.direct() == EMMessage.Direct.SEND) {
            //判断消息发送的状态
            EMMessage.Status status = emMessage.status();
            switch (status) {
                case FAIL:      //发送失败
                    //给imageview设置一个失败的背景
                    holder.mIvStatus.setVisibility(View.VISIBLE);
                    holder.mIvStatus.setBackgroundResource(R.mipmap.msg_error);

                    break;
                case SUCCESS:   //发送成功
                    //让状态图标不可见
                    holder.mIvStatus.setVisibility(View.GONE);

                    break;
                case INPROGRESS:  //正在发送
                    //播放一个帧动画
                    holder.mIvStatus.setVisibility(View.VISIBLE);
                    holder.mIvStatus.setBackgroundResource(R.drawable.loading);
                    AnimationDrawable animation = (AnimationDrawable) holder.mIvStatus.getDrawable();   //声明一个帧动画对象
                    //                    animation.start();
                    //                    animation.addFrame(parent.getContext),200);
                    //那么就要关心一下这个帧动画什么时候停止
                    if (animation.isRunning()) {
                        animation.stop();

                    }
                    animation.start();

                    break;
            }


        }

    }

    @Override
    public int getItemCount() {
        return emMessagesLists == null
               ? 0
               : emMessagesLists.size();
    }

    class ChatViewHolder
            extends RecyclerView.ViewHolder
    {


        ImageView mIvStatus;
        TextView  mTvMsg;
        TextView  mTvTime;

        public ChatViewHolder(View itemView) {
            super(itemView);
            mIvStatus = (ImageView) itemView.findViewById(R.id.iv_status);
            mTvMsg = (TextView) itemView.findViewById(R.id.tv_msg);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_time);


        }
    }


}
