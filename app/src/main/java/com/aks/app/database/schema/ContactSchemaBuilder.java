package com.aks.app.database.schema;

import static com.aks.app.database.accessor.ContactAccessor.EMAIL;
import static com.aks.app.database.accessor.ContactAccessor.ID;
import static com.aks.app.database.accessor.ContactAccessor.LATTITUDE;
import static com.aks.app.database.accessor.ContactAccessor.LONGITUDE;
import static com.aks.app.database.accessor.ContactAccessor.NAME;
import static com.aks.app.database.accessor.ContactAccessor.OFFICE_PHONE;
import static com.aks.app.database.accessor.ContactAccessor.PHONE;

/**
 * Created by Aakash Singh on 24-11-2016.
 */

public class ContactSchemaBuilder {

    public static final String TABLE_NAME = "contact_table";

    public static final String CREATE_TABLE = "CREATE TABLE " +
            TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NAME + " VARCHAR(50) , " +
            EMAIL + " VARCHAR(50) , " +
            PHONE + " VARCHAR(50) , " +
            OFFICE_PHONE + " VARCHAR(50) ," +
            LATTITUDE + " VARCHAR(50) ," +
            LONGITUDE + " VARCHAR(50) ," +
            "UNIQUE(" + NAME + "));";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getCreateTable() {
        return CREATE_TABLE;
    }

    public static String getDropTable() {
        return DROP_TABLE;
    }
}
