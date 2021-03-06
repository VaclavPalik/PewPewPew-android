package com.github.vaclavpalik.pewpewpew.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.vaclavpalik.pewpewpew.MainActivity;
import com.github.vaclavpalik.pewpewpew.R;
import com.github.vaclavpalik.pewpewpew.model.Game;
import com.github.vaclavpalik.pewpewpew.model.Player;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    // Buttons
    static private Button gameButton;
    static private Button upgradeButton;
    static private Button aboutButton;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        gameButton = (Button) view.findViewById(R.id.button_game);
        upgradeButton = (Button) view.findViewById(R.id.button_upgrades);
        aboutButton = (Button) view.findViewById(R.id.button_about);
        gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getInstance().setActiveFragment(MainActivity.getInstance().getGameFragment());
                gameButton.setClickable(false);
                gameButton.setEnabled(false);
                upgradeButton.setClickable(true);
                upgradeButton.setEnabled(true);
                aboutButton.setClickable(true);
                aboutButton.setEnabled(true);
            }
        });
        upgradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getInstance().setActiveFragment(MainActivity.getInstance().getUpgradeFragment());
                gameButton.setClickable(true);
                gameButton.setEnabled(true);
                upgradeButton.setClickable(false);
                upgradeButton.setEnabled(false);
                aboutButton.setClickable(true);
                aboutButton.setEnabled(true);
            }
        });
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getInstance().setActiveFragment(MainActivity.getInstance().getAboutFragment());
                gameButton.setClickable(true);
                gameButton.setEnabled(true);
                upgradeButton.setClickable(true);
                upgradeButton.setEnabled(true);
                aboutButton.setClickable(false);
                aboutButton.setEnabled(false);
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
