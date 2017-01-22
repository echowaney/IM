package com.example.hashwaney.im.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hashwaney.im.R;
import com.example.hashwaney.im.util.StringUtils;

import java.util.List;

/**

 这是联系人列表的adapter

 */
public class ContactFragmentAdapter
        extends RecyclerView.Adapter<ContactFragmentAdapter.ContactViewHolder>
{

    private List<String> contacts;
    //    private Context      sContext;

    public ContactFragmentAdapter(List<String> contacts) {
        this.contacts = contacts;
        //        this.sContext = context;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //        View view = LayoutInflater.from(sContext)
        //                                     .inflate(R.layout.contact_item_view, parent);
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.contact_item_view, parent,false);
        ContactViewHolder holder = new ContactViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {

        String username = contacts.get(position);
        String search   = StringUtils.getSearchTitle(username);
        holder.mTv_search.setText(search);
        holder.mTv_name.setText(username);
        if (position == 0) {
            holder.mTv_search.setVisibility(View.VISIBLE);
            //截取字符串


        } else {
            //判断上一个跟下一个是否是同一个字符,如果想,那么就不显示了,
            String currentUser = contacts.get(position);
            String preUser     = contacts.get(position - 1);
            boolean equals     = StringUtils.getSearchTitle(currentUser)
                                            .equals(StringUtils.getSearchTitle(preUser));
            if (equals){
                holder.mTv_search.setVisibility(View.GONE);

            }else {
                holder.mTv_search.setVisibility(View.VISIBLE);
            }
        }


    }

    @Override
    public int getItemCount() {
        return contacts == null
               ? 0
               : contacts.size();
    }

    class ContactViewHolder
            extends RecyclerView.ViewHolder
    {


        private final TextView mTv_search;
        private final TextView mTv_name;

        public ContactViewHolder(View itemView) {
            super(itemView);
            mTv_search = (TextView) itemView.findViewById(R.id.tv_search);
            mTv_name = (TextView) itemView.findViewById(R.id.user_name);

        }
    }

}
