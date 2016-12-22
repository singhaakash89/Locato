package com.aks.app.fragment;

/**
 * Created by Aakash Singh on 21-12-2016.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.aks.app.R;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private ListView listView;

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
    public static PlaceholderFragment newInstance(int sectionNumber) {
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
                ArrayList arrayList = new ArrayList();
                arrayList.add("Hi.. Aakash Singh_1");
                arrayList.add("Hi.. Aakash Singh");
                arrayList.add("Hi.. Aakash Singh");
                arrayList.add("Hi.. Aakash Singh");
                arrayList.add("Hi.. Aakash Singh");
                arrayList.add("Hi.. Aakash Singh");
                arrayList.add("Hi.. Aakash Singh");
                arrayList.add("Hi.. Aakash Singh");
                arrayList.add("Hi.. Aakash Singh");
                arrayList.add("Hi.. Aakash Singh");
                arrayList.add("Hi.. Aakash Singh");
                arrayList.add("Hi.. Aakash Singh");
                arrayList.add("Hi.. Aakash Singh");
                arrayList.add("Hi.. Aakash Singh");
                arrayList.add("Hi.. Aakash Singh");
                arrayList.add("Hi.. Aakash Singh");
                arrayList.add("Hi.. Aakash Singh");
                arrayList.add("Hi.. Aakash Singh");
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
                listView.setAdapter(arrayAdapter);
                break;

            case 1:
                rootView = inflater.inflate(R.layout.fragment_two, container, false);
                textView = (TextView) rootView.findViewById(R.id.section_label);
                //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
                textView.setText("Contacts Map");
                break;
        }
        return rootView;
    }
}

