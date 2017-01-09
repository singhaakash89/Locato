package com.aks.app.json_parser;

import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.aks.app.database.StandardStorageHelper;
import com.aks.app.database.model.MarkerContacts;
import com.aks.app.database.schema.ContactSchemaBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by Aakash Singh on 21-12-2016.
 */

public class AsyncTaskHandler extends AsyncTask<Void, String, ArrayList<MarkerContacts>> {

    private static AsyncTaskHandler asyncTaskHandler;
    private ProgressDialog progressDialog;
    private Context context;
    private static ArrayList<MarkerContacts> markerContactsArrayList;

    private AsyncTaskHandler(Context context, ProgressDialog progressDialog) {
        Log.d("AsyncTaskHandler - const", "AsyncTaskHandler - const");
        this.context = context;
        this.progressDialog = progressDialog;
    }

    public static void createInstance(Context context, ProgressDialog progressDialog) {
        Log.d("createInstance", "createInstance");
        asyncTaskHandler = new AsyncTaskHandler(context, progressDialog);
    }

    public static AsyncTaskHandler getInstance() {
        Log.d("getInstance", "getInstance");
        return asyncTaskHandler;
    }

    public static ArrayList<MarkerContacts> getJSONArraylist() {
        return markerContactsArrayList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("onPreExecute", "onPreExecute");
        progressDialog.setMessage("Fetching JSON data...");
        progressDialog.show();
    }

    @Override
    protected ArrayList<MarkerContacts> doInBackground(Void... params) {
        Log.d("doInBackground", "doInBackground");
        ObjectMapper objectMapper = new ObjectMapper();
        markerContactsArrayList = new ArrayList<>();
        try {
            HttpHandler httpHandler = new HttpHandler();
            String url = "http://api.androidhive.info/contacts/";
            String url_n1 = "http://private-b08d8d-nikitest.apiary-mock.com/contacts";
            String jsonString = httpHandler.makeServiceCall(url_n1);
            if (null != jsonString) {
                jsonString = jsonString.substring(1, (jsonString.length() - 2));
            }


            Log.d("Response from url: ", "" + jsonString);
            if (jsonString != null) {
                try {
                    //first approach
                    JSONObject jsonObj = new JSONObject(jsonString);
                    // Getting JSON Array node
                    JSONArray jsonArray = jsonObj.getJSONArray("contacts");

                    // looping through All MarkerContacts
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);

                        String name = c.getString("name");
                        String email = null;
                        String phone = null;
                        String officePhone = null;
                        try {
                            if (null != c.getString("email"))
                                email = c.getString("email");
                        } catch (Exception e) {
                            email = null;
                        }

                        try {
                            if (null != c.getString("phone"))
                                phone = c.getString("phone");
                        } catch (Exception e) {
                            phone = null;
                        }

                        try {
                            if (null != c.getString("officePhone"))
                                officePhone = c.getString("officePhone");
                        } catch (Exception e) {
                            officePhone = null;
                        }
                        String latitude = c.getString("latitude");
                        String longitude = c.getString("longitude");

                        System.out.println("Contact No : " + i+1);
                        System.out.println("name : " + name + " ;");
                        System.out.println("email : " + email + " ;");
                        System.out.println("phone : " + phone + " ;");
                        System.out.println("officePhone : " + officePhone + " ;");
                        System.out.println("latitude : " + latitude + " ;");
                        System.out.println("longitude : " + longitude + " ;");
                        System.out.println("===================================");

                        MarkerContacts markerContacts = new MarkerContacts();
                        markerContacts.setName(name);
                        markerContacts.setEmail(email);
                        markerContacts.setPhone(phone);
                        markerContacts.setOfficePhone(officePhone);
                        markerContacts.setLatitude(latitude);
                        markerContacts.setLongitude(longitude);

                        // adding contact to contact list
                        markerContactsArrayList.add(markerContacts);

                        //insert In DB
                        insertIntoDB(markerContacts);

                        //de-referencing reference variable
                        //markerContacts = null;
                    }
                    publishProgress("Json list fetched...");

                    //storing in address book
                    storeInAddressBook(markerContactsArrayList);
                    return markerContactsArrayList;


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return markerContactsArrayList;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Log.d("onProgressUpdate", "onProgressUpdate");
        progressDialog.setMessage(values[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<MarkerContacts> markerContactsArrayList) {
        Log.d("onPostExecute", "onPostExecute");
        progressDialog.dismiss();
        Toast.makeText(context, "Contacts are successfully saved", Toast.LENGTH_SHORT).show();
        Collections.sort(markerContactsArrayList);
        Iterator<MarkerContacts> iterator = markerContactsArrayList.iterator();
        while (iterator.hasNext()) {
            MarkerContacts markerContacts = iterator.next();
            System.out.println("getName : " + markerContacts.getName() + " ;");
            System.out.println("getEmail : " + markerContacts.getEmail() + " ;");
            System.out.println("getPhone : " + markerContacts.getPhone() + " ;");
            System.out.println("getOfficePhone : " + markerContacts.getOfficePhone() + " ;");
            System.out.println("getLatitude : " + markerContacts.getLatitude() + " ;");
            System.out.println("getLongitude : " + markerContacts.getLongitude() + " ;");
            System.out.println("'''''''''''''''''''''''''''''''''''''''''''''''''''''''");
        }
    }

    private long insertIntoDB(MarkerContacts markerContacts)
    {
        //Storing row to DB
        ContentValues contentValues = markerContacts.getContentValues();
        long id = StandardStorageHelper.getInstance().insertToDB(ContactSchemaBuilder.TABLE_NAME, contentValues);
        Log.d("Inserted row id : ", "" + id + " ;");
        return id;
    }

    private void storeInAddressBook(ArrayList<MarkerContacts> markerContactsArrayList) {
        Iterator<MarkerContacts> iterator = markerContactsArrayList.iterator();
        String name = null;
        String email = null;
        String phone = null;
        String officePhone = null;
        while (iterator.hasNext()) {
            MarkerContacts markerContacts = iterator.next();
            name = markerContacts.getName();
            email = markerContacts.getEmail();
            phone = markerContacts.getPhone();
            officePhone = markerContacts.getOfficePhone();

            //''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''

            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .build());

            //------------------------------------------------------ Names
            if (name != null) {
                ops.add(ContentProviderOperation.newInsert(
                        ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name).build());
            }

            //------------------------------------------------------ Email
            if (email != null) {
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
                        .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                        .build());
            }

            //------------------------------------------------------ Mobile Number
            if (phone != null) {
                ops.add(ContentProviderOperation.
                        newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone)
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                        .build());
            }

            //------------------------------------------------------ Home Numbers
            if (officePhone != null) {
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, officePhone)
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                        .build());
            }

            //''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''

            try {
                // Executing all the insert operations as a single database transaction
                context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (OperationApplicationException e) {
                e.printStackTrace();
            }

//            //starting activity
//            // Creating an intent to open Android's MarkerContacts List
//            Intent addressBook = new Intent(Intent.ACTION_VIEW, ContactsContract.MarkerContacts.CONTENT_URI);
//
//            // Starting the activity
//            context.startActivity(addressBook);

        }

    }
}
