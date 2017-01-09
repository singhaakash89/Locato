package com.aks.app.database.model;

import android.content.ContentValues;

import com.aks.app.database.accessor.ContactAccessor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Aakash Singh on 18-12-2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class MarkerContacts implements Comparable{
    private String name;
    private String email;
    private String phone;
    private String officePhone;
    private String latitude;
    private String longitude;

    public MarkerContacts() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public ContentValues getContentValues() {
        ContentValues row = new ContentValues();
        row.put(ContactAccessor.NAME,getName());
        row.put(ContactAccessor.EMAIL,getEmail());
        row.put(ContactAccessor.PHONE,getPhone());
        row.put(ContactAccessor.OFFICE_PHONE,getOfficePhone());
        row.put(ContactAccessor.LATTITUDE,getLatitude());
        row.put(ContactAccessor.LONGITUDE,getLongitude());
        return row;
    }

    @Override
    public int compareTo(Object another) {

        //for integer
        //int comparePhone = ((MarkerContacts) another).getPhone();

        //for String
        String compareName = ((MarkerContacts) another).getName();

        /* For Ascending order*/

        //for Integer
        //return this.getPhone() - comparePhone;

        //for String
        return this.getName().compareTo(compareName);

        /* For Descending order do like this */
        //return compareDate - this.date;
    }
}
