package com.aks.app.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

import com.aks.app.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapDemoActivity extends FragmentActivity implements OnMapReadyCallback {

	private MarkerOptions options;
	private LatLng position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_two);
		
		// Receiving latitude from MainActivity screen
		double latitude = getIntent().getDoubleExtra("lat", 0);
		
		// Receiving longitude from MainActivity screen
		double longitude = getIntent().getDoubleExtra("lng", 0);
		
		position = new LatLng(latitude, longitude);
		
		// Instantiating MarkerOptions class
		options = new MarkerOptions();
		
		// Setting position for the MarkerOptions
		options.position(position);
		
		// Setting title for the MarkerOptions
		options.title("Position");
		
		// Setting snippet for the MarkerOptions
		options.snippet("Latitude:"+latitude+",Longitude:"+longitude);
		
		// Getting Reference to SupportMapFragment of activity_map.xml
		SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);

		// Getting reference to google map
		fm.getMapAsync(this);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {


		// Adding Marker on the Google Map
		googleMap.addMarker(options);

		// Creating CameraUpdate object for position
		CameraUpdate updatePosition = CameraUpdateFactory.newLatLng(position);

		// Creating CameraUpdate object for zoom
		CameraUpdate updateZoom = CameraUpdateFactory.zoomBy(4);

		// Updating the camera position to the user input latitude and longitude
		googleMap.moveCamera(updatePosition);

		// Applying zoom to the marker position
		googleMap.animateCamera(updateZoom);
	}
}
