package com.aks.app.json_parser;

import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.aks.app.json_parser.model.Contacts;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Aakash Singh on 21-12-2016.
 */

public class AsyncTaskHandler extends AsyncTask<Void, String, ArrayList<Contacts>> {

    private static AsyncTaskHandler asyncTaskHandler;
    private ProgressDialog progressDialog;
    private Context context;
    private static ArrayList<Contacts> contactsArrayList;

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

    public static ArrayList<Contacts> getJSONArraylist() {
        return contactsArrayList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("onPreExecute", "onPreExecute");
        progressDialog.setMessage("Fetching JSON data");
        progressDialog.show();
    }

    @Override
    protected ArrayList<Contacts> doInBackground(Void... params) {
        Log.d("doInBackground", "doInBackground");
        ObjectMapper objectMapper = new ObjectMapper();
        contactsArrayList = new ArrayList<>();
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

                    // looping through All Contacts
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

                        System.out.println("Contact No : " + i);
                        System.out.println("name : " + name + " ;");
                        System.out.println("email : " + email + " ;");
                        System.out.println("phone : " + phone + " ;");
                        System.out.println("officePhone : " + officePhone + " ;");
                        System.out.println("latitude : " + latitude + " ;");
                        System.out.println("longitude : " + longitude + " ;");
                        System.out.println("===================================");

                        Contacts contacts = new Contacts();
                        contacts.setName(name);
                        contacts.setEmail(email);
                        contacts.setPhone(phone);
                        contacts.setOfficePhone(officePhone);
                        contacts.setLatitude(latitude);
                        contacts.setLongitude(longitude);

                        // adding contact to contact list
                        contactsArrayList.add(contacts);

                        //de-referencing reference variable
                        //contacts = null;
                    }
                    publishProgress("Json list fetched...");
                    return contactsArrayList;


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contactsArrayList;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Log.d("onProgressUpdate", "onProgressUpdate");
        progressDialog.setMessage(values[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<Contacts> contactsArrayList) {
        Log.d("onPostExecute", "onPostExecute");
        progressDialog.dismiss();
        Collections.sort(contactsArrayList);
        Iterator<Contacts> iterator = contactsArrayList.iterator();
        while (iterator.hasNext()) {
            Contacts contacts = iterator.next();
            System.out.println("getName : " + contacts.getName() + " ;");
            System.out.println("getEmail : " + contacts.getEmail() + " ;");
            System.out.println("getPhone : " + contacts.getPhone() + " ;");
            System.out.println("getOfficePhone : " + contacts.getOfficePhone() + " ;");
            System.out.println("getLatitude : " + contacts.getLatitude() + " ;");
            System.out.println("getLongitude : " + contacts.getLongitude() + " ;");
            System.out.println("'''''''''''''''''''''''''''''''''''''''''''''''''''''''");
        }

        //storing in address book
        storeInAddressBook(contactsArrayList);
    }

    private void storeInAddressBook(ArrayList<Contacts> contactsArrayList) {
        Iterator<Contacts> iterator = contactsArrayList.iterator();
        String name = null;
        String email = null;
        String phone = null;
        String officePhone = null;
        while (iterator.hasNext()) {
            Contacts contacts = iterator.next();
            name = contacts.getName();
            email = contacts.getEmail();
            phone = contacts.getPhone();
            officePhone = contacts.getOfficePhone();

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
                Toast.makeText(context, "Contact is successfully added", Toast.LENGTH_SHORT).show();
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (OperationApplicationException e) {
                e.printStackTrace();
            }

//            //starting activity
//            // Creating an intent to open Android's Contacts List
//            Intent addressBook = new Intent(Intent.ACTION_VIEW, ContactsContract.Contacts.CONTENT_URI);
//
//            // Starting the activity
//            context.startActivity(addressBook);

        }

    }
}
