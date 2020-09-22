package ean.ecom.ean_shipping.launching.fragment;


import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ean.ecom.ean_shipping.R;
import ean.ecom.ean_shipping.databinding.FragmentSignInBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {


    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Data Binding...
        FragmentSignInBinding activityMainBinding = DataBindingUtil.inflate( inflater, R.layout.fragment_sign_in, container,false );
        activityMainBinding.setViewModel(new LogInViewModel());
        activityMainBinding.executePendingBindings();

        // Assign View...
        View view = activityMainBinding.getRoot();





        return view;

    }

}
