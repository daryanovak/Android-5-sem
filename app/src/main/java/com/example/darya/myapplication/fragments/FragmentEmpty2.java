package com.example.darya.myapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.darya.myapplication.R;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentEmpty2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentEmpty2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentEmpty2 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_empty2, container, false);
    }

}
