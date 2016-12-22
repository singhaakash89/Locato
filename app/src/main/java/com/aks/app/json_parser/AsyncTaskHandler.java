package com.aks.app.json_parser;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

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

    private AsyncTaskHandler(ProgressDialog progressDialog) {
        Log.d("AsyncTaskHandler - const", "AsyncTaskHandler - const");
        this.progressDialog = progressDialog;
    }

    public static void createInstance(ProgressDialog progressDialog) {
        Log.d("createInstance", "createInstance");
        asyncTaskHandler = new AsyncTaskHandler(progressDialog);
    }

    public static AsyncTaskHandler getInstance() {
        Log.d("getInstance", "getInstance");
        return asyncTaskHandler;
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
        ArrayList<Contacts> contactsArrayList = new ArrayList<>();
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
                        String email = c.getString("email");
                        String phone = c.getString("phone");
                        String officePhone = c.getString("officePhone");
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
        System.out.println(contactsArrayList);
        System.out.println("1111111111111111");
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
    }
}
