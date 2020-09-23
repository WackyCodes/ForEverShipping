package ean.ecom.shipping.launching.fragment;


import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import ean.ecom.shipping.R;
import ean.ecom.shipping.databinding.FragmentSignInBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {


    public SignInFragment() {
        // Required empty public constructor
    }

    private EditText emailEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Data Binding...
        FragmentSignInBinding activityMainBinding = DataBindingUtil.inflate( inflater, R.layout.fragment_sign_in, container,false );
        activityMainBinding.setViewModel(new LogInViewModel());
        activityMainBinding.executePendingBindings();

        // Assign View...
        View view = activityMainBinding.getRoot();

        emailEditText = view.findViewById( R.id.sign_in_email );

        setTextChanged(emailEditText);

        return view;

    }

    private void setTextChanged( EditText editText ){
        editText.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 4){
                    Toast.makeText( getContext(), "Email...!", Toast.LENGTH_SHORT ).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        } );
    }


}
