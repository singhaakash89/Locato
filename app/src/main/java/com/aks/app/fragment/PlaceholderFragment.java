package com.aks.app.fragment;

/**
 * Created by Aakash Singh on 21-12-2016.
 */


import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aks.app.R;
import com.aks.app.adapter.RecyclerAdapter;
import com.aks.app.database.StandardStorageHelper;
import com.aks.app.database.accessor.ContactAccessor;
import com.aks.app.database.model.Contact;
import com.aks.app.database.model.MarkerContacts;
import com.aks.app.database.schema.ContactSchemaBuilder;
import com.aks.app.json_parser.model.Marker;
import com.aks.app.toast_manager.ToastManager;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements OnMapReadyCallback {

    private boolean userScrolled = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private LinearLayoutManager mLayoutManager;
    private static RecyclerAdapter recyclerAdapter;
    private MarkerOptions marker;
    private List<MarkerContacts> markerArrayList;
    private LatLng position;
    private static FragmentManager fragmentManager;
    private ListView listView;
    private RecyclerView recyclerView;
    private RelativeLayout progressBarLayout;
    private View view;
    private List<Contact> contactList;
    private int count = 0;

    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "PlaceholderFragment$section_number";

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber, FragmentManager fm) {
        fragmentManager = fm;
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int fragment_id = getArguments().getInt(ARG_SECTION_NUMBER);
        switch (fragment_id) {
            case 0:
                view = inflater.inflate(R.layout.fragment_one, container, false);
                //initializing recyclerView
                progressBarLayout = initRecyclerView();
                //setup recycler view
                setupRecyclerView();
                //add scroll listener
                addScrollListener(progressBarLayout);
                //show 10 MarkerContacts
                //showContacts();
                break;

            case 1:
                markerArrayList = createMarkerList();
                Log.d("markerArrayList.size : ", "" + markerArrayList.size() + " ;");
                view = showMarker(inflater, container, savedInstanceState);
                break;
        }
        return view;
    }

    private RelativeLayout initRecyclerView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        progressBarLayout = (RelativeLayout) view.findViewById(R.id.progressBarLayout);
        Log.d("progressBarLayout : 1", " - " + progressBarLayout + " ;");
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        return progressBarLayout;
    }

    private void setupRecyclerView() {
        //list = createItemList();
        List<Contact> contactList = createContactList();
        recyclerAdapter = new RecyclerAdapter(contactList);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();
    }

    public static RecyclerAdapter getRecyclerAdapter() {
        return recyclerAdapter;
    }

    private void addScrollListener(final RelativeLayout progressBarLayout) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // If scroll state is touch scroll then set userScrolled
                // true
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // Here get the child count, item count and visibleitems
                // from layout manager
                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                // Now check if userScrolled is true and also check if
                // the item is end then update recycler view and set
                // userScrolled to false
                if (userScrolled && (visibleItemCount + pastVisiblesItems) == totalItemCount) {
                    userScrolled = false;
                    updateRecyclerView(progressBarLayout);
                }
            }
        });
    }

    // Method for repopulating recycler view
    private void updateRecyclerView(final RelativeLayout progressBarLayout) {
        // Show Progress Layout
        progressBarLayout.setVisibility(View.VISIBLE);
        // Handler to show refresh for a period of time you can use async task
        // while commnunicating server
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //fetching next 20 contacts
                getAllContact(20);
                // notify adapter
                recyclerAdapter.notifyDataSetChanged();
                // Toast for task completion
                ToastManager.getInstance().showSimpleToastShort("Contacts list updated");
                // After adding new data hide the view.
                progressBarLayout.setVisibility(View.GONE);
            }
        }, 2000);
    }

    public List<Contact> createContactList() {
        contactList = new ArrayList<>();
        contactList = getAllContact(20);
        return contactList;
    }

    /**
     * Show the contacts in the ListView.
     */
    private void showContacts() {
        Log.d("Inside showContacts", " Done");
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Inside showContacts : if", " Done");
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            Log.d("Inside showContacts : else", " Done");
            // Android version is lesser than 6.0 or the permission is already granted.
//            List<String> list = getContactNames();
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
//            listView.setAdapter(adapter);

            getAllContact(20);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(getActivity(), "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * Read the name of all the contacts.
     *
     * @return a list of names.
     */
    private List<Contact> getAllContact(int countUpdate) {
        Log.d("Inside getAllContact", " Done");
        Contact contact;
        String id = null;
        String name = null;
        String email = null;
        String homePhone = null;
        String phone = null;
        String officePhone = null;

        // Get the ContentResolver
        ContentResolver cr = getActivity().getContentResolver();
        //initialize cusor
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC" + " LIMIT 20 OFFSET " + count + "");
        if (cur.getCount() > 0) {
            Log.d("Inside if_cur_count>0", " Done");
            while (cur.moveToNext()) {
                Log.d("Inside while main cursor", " Done");
                id = null;
                name = null;
                email = null;
                homePhone = null;
                phone = null;
                officePhone = null;

                //Id
                id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));

                //Name
                name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                //Email
                Cursor eCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{id}, null);


                if (eCur.getCount() > 0) {
                    Log.d("Inside if_email_cur_count>0", " Done");
                    while (eCur.moveToNext()) {
                        email = eCur.getString(eCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    }
                    eCur.close();
                }

                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Log.d("Inside if_phone_cur_count>0", " Done");
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        Log.d("Inside phone while", " Done");
                        int type = pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        switch (type) {
                            case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                homePhone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                officePhone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                break;
                        }
                    }
                    pCur.close();
                }

                Log.d("name", "" + name + ";");
                Log.d("email", "" + email + ";");
                Log.d("phone", "" + phone + ";");
                Log.d("homePhone", "" + homePhone + ";");
                Log.d("office", "" + officePhone + ";");
                Log.d("============", "================================");
                Log.d("=============", "================================");
                Log.d("\n", "\n");

                //updating array list
                contact = new Contact();
                contact.setName(name);
                contact.setEmail(email);
                contact.setPhone(phone);
                contact.setOfficePhone(officePhone);
                contactList.add(contact);
            }
        }

        cur.close();

        count = count + countUpdate;

        //displaying list before populating
        for (Contact contact1 : contactList) {
            System.out.println("name : " + contact1.getName());
            System.out.println("email : " + contact1.getEmail());
            System.out.println("phone : " + contact1.getPhone());
            System.out.println("office : " + contact1.getOfficePhone());
            System.out.println("===============");
            System.out.println("\n\n");
        }

        System.out.println("contactList.size : " + contactList.size());
        return contactList;
    }

    public static List<MarkerContacts> createMarkerList() {
        List<MarkerContacts> markerContactsArrayList = new ArrayList<>();
        Cursor cursor = StandardStorageHelper.getInstance().queryFromDB(ContactSchemaBuilder.TABLE_NAME, ContactAccessor.getTableProjection());
        cursor.moveToFirst();
        if (cursor.getCount() > 0 && !cursor.isAfterLast()) {
            while (cursor.moveToNext()) {
                MarkerContacts markerContacts = new MarkerContacts();
                markerContacts.setName(cursor.getString(cursor.getColumnIndex(ContactAccessor.NAME)));
                markerContacts.setEmail(cursor.getString(cursor.getColumnIndex(ContactAccessor.EMAIL)));
                markerContacts.setPhone(cursor.getString(cursor.getColumnIndex(ContactAccessor.PHONE)));
                markerContacts.setOfficePhone(cursor.getString(cursor.getColumnIndex(ContactAccessor.OFFICE_PHONE)));
                markerContacts.setLatitude(cursor.getString(cursor.getColumnIndex(ContactAccessor.LATTITUDE)));
                markerContacts.setLongitude(cursor.getString(cursor.getColumnIndex(ContactAccessor.LONGITUDE)));

                markerContactsArrayList.add(markerContacts);
            }
            cursor.close();
        }
        return markerContactsArrayList;
    }

    public View showMarker(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_two, container, false);

        // Getting Reference to SupportMapFragment of activity_map.xml
        MapView mapView = (MapView) rootView.findViewById(R.id.map);

        mapView.onCreate(savedInstanceState);

        mapView.onResume();

        // Getting reference to google map
        mapView.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("Inside ", "onMapReady");

        Log.d("markerArrayList.size()", "" + markerArrayList.size() + " ;\n\n");
        for (MarkerContacts markerContacts : markerArrayList) {
            Log.d("markerContacts.getName()", "" + markerContacts.getName() + " ;");
            Log.d("markerContacts.getEmail()", "" + markerContacts.getEmail() + " ;");
            Log.d("markerContacts.getPhone()", "" + markerContacts.getPhone() + " ;");
            Log.d("markerContacts.getOfficePhone()", "" + markerContacts.getOfficePhone() + " ;");
            Log.d("markerContacts.getLatitude()", "" + markerContacts.getLatitude() + " ;");
            Log.d("markerContacts.getLongitude()", "" + markerContacts.getLongitude() + " ;");
            Log.d("============================", "============================");
            Log.d("\n", "\n");
        }

        for (MarkerContacts markerContacts : markerArrayList) {
            position = new LatLng(Double.valueOf(markerContacts.getLatitude()), Double.valueOf(markerContacts.getLongitude()));

            // Instantiating MarkerOptions class
            marker = new MarkerOptions();

            // Setting position for the MarkerOptions
            marker.position(position);

            // Setting title for the MarkerOptions
            marker.title(markerContacts.getName());

            // Setting snippet for the MarkerOptions

            //email is null
            if (null != markerContacts.getEmail())
                marker.snippet("Phone:" + markerContacts.getPhone() + ",Office:" + markerContacts.getOfficePhone());
            else if (null != markerContacts.getOfficePhone())
                marker.snippet("Email:" + markerContacts.getEmail() + ",Phone:" + markerContacts.getPhone());
            else if (null != markerContacts.getPhone())
                marker.snippet("Email:" + markerContacts.getEmail() + ",Office:" + markerContacts.getOfficePhone());

            // Changing marker icon
            marker.icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_RED));

            // Adding Marker on the Google Map
            googleMap.addMarker(marker);

//            // Creating CameraUpdate object for position
//            CameraUpdate updatePosition = CameraUpdateFactory.newLatLng(position);
//
//            // Creating CameraUpdate object for zoom
//            CameraUpdate updateZoom = CameraUpdateFactory.zoomBy(4);
//
//            // Updating the camera position to the user input latitude and longitude
//            googleMap.moveCamera(updatePosition);
//
//            // Applying zoom to the marker position
//            googleMap.animateCamera(updateZoom);

        }

        //''''''''''''''''''''''''''''''''''''''''''''''''

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        markerArrayList = createMarkerList();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

