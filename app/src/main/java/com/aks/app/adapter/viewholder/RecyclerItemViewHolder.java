package com.aks.app.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aks.app.R;

public class RecyclerItemViewHolder extends RecyclerView.ViewHolder {

    private final TextView nameTV;
    private final TextView emailTV;
    private final TextView phone;
    private final TextView office;

    public RecyclerItemViewHolder(final View parent, TextView nameTV, TextView emailTV, TextView phone, TextView office) {
        super(parent);
        this.nameTV = nameTV;
        this.emailTV = emailTV;
        this.phone = phone;
        this.office = office;
    }

    public static RecyclerItemViewHolder newInstance(View parent) {
        TextView nameTV = (TextView) parent.findViewById(R.id.nameTextView);
        TextView emailTV = (TextView) parent.findViewById(R.id.emailTextView);
        TextView phone = (TextView) parent.findViewById(R.id.phoneTextView);
        TextView office = (TextView) parent.findViewById(R.id.officePhoneTextView);
        return new RecyclerItemViewHolder(parent, nameTV, emailTV, phone, office);
    }

//    public void setItemText(CharSequence text) {
//        mItemTextView.setText(text);
//        //mItemTextView.append(text);
//    }

    public void setName(CharSequence text) {
        if (null != text)
            nameTV.setText(text);
        //mItemTextView.append(text);
    }

    public void setEmail(CharSequence text) {
        if (null != text)
            emailTV.setText(text);
        //mItemTextView.append(text);
    }

    public void setPhone(CharSequence text) {
        if (null != text)
            phone.setText(text);
        //mItemTextView.append(text);
    }

    public void setOffice(CharSequence text) {
        if (null != text)
            office.setText(text);
        //mItemTextView.append(text);
    }

}
