package com.aks.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aks.app.R;
import com.aks.app.adapter.viewholder.RecyclerItemViewHolder;
import com.aks.app.database.model.Contact;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Contact> contactList;

    public RecyclerAdapter(List<Contact> list) {
        this.contactList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_row, parent, false);
        return RecyclerItemViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        RecyclerItemViewHolder recyclerItemViewHolder = (RecyclerItemViewHolder) viewHolder;
        //String itemText = mItemList.get(position);
        Contact contact = contactList.get(position);
        recyclerItemViewHolder.setName(contact.getName());
        recyclerItemViewHolder.setEmail(contact.getEmail());
        recyclerItemViewHolder.setPhone(contact.getPhone());
        recyclerItemViewHolder.setOffice(contact.getOfficePhone());
    }

    @Override
    public int getItemCount() {
        return contactList == null ? 0 : contactList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
