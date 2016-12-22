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
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aks.app.R;
import com.aks.app.json_parser.AsyncTaskHandler;
import com.aks.app.json_parser.model.Contacts;
import com.aks.app.json_parser.model.Marker;
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

    private ListView listView;
    private MarkerOptions marker;
    private LatLng position;
    private static FragmentManager fragmentManager;
    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

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
        View rootView = null;
        TextView textView = null;
        switch (fragment_id) {
            case 0:
                rootView = inflater.inflate(R.layout.fragment_one, container, false);
                listView = (ListView) rootView.findViewById(R.id.listview);
                showContacts();
//                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
//                listView.setAdapter(arrayAdapter);
                break;

            case 1:
                ArrayList<Marker> markerArrayList = new ArrayList<>();
                Marker marker = null;
                ArrayList<Contacts> contactsArrayList = AsyncTaskHandler.getJSONArraylist();
                for (Contacts contacts : contactsArrayList) {
                    marker = new Marker();
                    marker.setLattitude(Double.parseDouble(contacts.getLatitude()));
                    marker.setLongitude(Double.parseDouble(contacts.getLongitude()));
                    markerArrayList.add(marker);
                }
                showMarker(inflater, container, rootView, savedInstanceState, markerArrayList);
                break;
        }
        return rootView;
    }

    private void showMarker(LayoutInflater inflater, ViewGroup container, View rootView, Bundle savedInstanceState, ArrayList<Marker> markerArrayList) {

        ArrayList<MarkerOptions> markerOptions = new ArrayList<>();

        rootView = inflater.inflate(R.layout.fragment_two, container, false);

        // Getting Reference to SupportMapFragment of activity_map.xml
        MapView mapView = (MapView) rootView.findViewById(R.id.map);

        mapView.onCreate(savedInstanceState);

        mapView.onResume();

        // latitude and longitude
        double latitude = 17.385044;

        double longitude = 78.486671;

        position = new LatLng(latitude, longitude);

        // Instantiating MarkerOptions class
        marker = new MarkerOptions();

        // Setting position for the MarkerOptions
        marker.position(position);

        // Setting title for the MarkerOptions
        marker.title("Position");

        // Setting snippet for the MarkerOptions
        marker.snippet("Latitude:" + latitude + ",Longitude:" + longitude);

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_RED));

        // Getting reference to google map
        mapView.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Adding Marker on the Google Map
        googleMap.addMarker(marker);

        // Creating CameraUpdate object for position
        CameraUpdate updatePosition = CameraUpdateFactory.newLatLng(position);

        // Creating CameraUpdate object for zoom
        CameraUpdate updateZoom = CameraUpdateFactory.zoomBy(4);

        // Updating the camera position to the user input latitude and longitude
        googleMap.moveCamera(updatePosition);

        // Applying zoom to the marker position
        googleMap.animateCamera(updateZoom);
    }

    /**
     * Show the contacts in the ListView.
     */
    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            List<String> list = getContactNames();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
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
    private List<String> getContactNames() {
        List<String> contacts = new ArrayList<>();
        // Get the ContentResolver
        ContentResolver cr = getActivity().getContentResolver();
        // Get the Cursor of all the contacts
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");

        // Move the cursor to first. Also check whether the cursor is empty or not.
        if (cursor.moveToFirst()) {
            // Iterate through the cursor
            do {
                // Get the contacts name
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                contacts.add(name);
            } while (cursor.moveToNext());
        }
        // Close the curosor
        cursor.close();

        return contacts;
    }
}

