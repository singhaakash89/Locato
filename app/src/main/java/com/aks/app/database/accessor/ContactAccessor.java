package com.aks.app.database.accessor;

/**
 * Created by Aakash Singh on 24-11-2016.
 */

public class ContactAccessor {
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    public static final String OFFICE_PHONE = "office_phone";
    public static final String LATTITUDE = "lattitude";
    public static final String LONGITUDE = "longitude";

    public static String getID() {
        return ID;
    }

    public static String getNAME() {
        return NAME;
    }

    public static String getEMAIL() {
        return EMAIL;
    }

    public static String getPHONE() {
        return PHONE;
    }

    public static String getOfficePhone() {
        return OFFICE_PHONE;
    }

    public static String getLATTITUDE() {
        return LATTITUDE;
    }

    public static String getLONGITUDE() {
        return LONGITUDE;
    }

    public static String[] getTableProjection() {
        String[] projection = new String[]{
                getID(),
                getNAME(),
                getEMAIL(),
                getPHONE(),
                getOfficePhone(),
                getLATTITUDE(),
                getLONGITUDE()};
        return projection;
    }

}
