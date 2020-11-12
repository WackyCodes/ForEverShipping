package ean.ecom.shipping.launching.fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.BindingAdapter;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import ean.ecom.shipping.MainActivity;
import ean.ecom.shipping.R;
import ean.ecom.shipping.databinding.FragmentSignInBinding;
import ean.ecom.shipping.launching.CheckUserPermission;
import ean.ecom.shipping.launching.UserPermissionM;
import ean.ecom.shipping.launching.WelcomeActivity;
import io.grpc.util.TransmitStatusRuntimeExceptionInterceptor;

import static ean.ecom.shipping.other.StaticMethods.writeFileInLocal;
import static ean.ecom.shipping.other.StaticValues.USER_ACCOUNT;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment implements CheckUserPermission.CheckIsUserRegistered, CheckUserPermission.CheckAdminPermission.OnAdminCheckFinisher{


    public SignInFragment() {
        // Required empty public constructor
    }

    private EditText emailEditText;
    private LinearLayout logInLayout;
    private LinearLayout sigUpLayout;

    private ProgressDialog dialog;
    private LogInViewModel logInViewModel;
    private CheckUserPermission.CheckAdminPermission checkAdminPermission;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_sign_in, container, false );
        dialog = new ProgressDialog( getContext() );
        dialog.setTitle( "Please Wait..." );

        logInViewModel  = new LogInViewModel( getContext(), this );
        checkAdminPermission = new UserPermissionM();
        // Data Binding...
//        FragmentSignInBinding activityMainBinding = DataBindingUtil.bind( view ); // = DataBindingUtil.inflate( inflater, R.layout.fragment_sign_in, container,false );
        FragmentSignInBinding activityMainBinding = FragmentSignInBinding.bind( view ); // = DataBindingUtil.inflate( inflater, R.layout.fragment_sign_in, container,false );
        activityMainBinding.setViewModel( logInViewModel );
        activityMainBinding.executePendingBindings();

        // Assign View...
//        View view = activityMainBinding.getRoot();

        emailEditText = view.findViewById( R.id.sign_in_email );
        logInLayout = view.findViewById( R.id.login_layout );
        sigUpLayout = view.findViewById( R.id.sign_up_layout );

        setTextChanged(emailEditText);

        return view;

    }


    private void setTextChanged(EditText editText ){
        editText.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().length() == 10){
                    showDialog();
                    checkUserRegister( charSequence.toString() );
                }
//                if (!TextUtils.isEmpty(charSequence.toString()) && Patterns.EMAIL_ADDRESS.matcher(charSequence.toString()).matches()){
////                    Toast.makeText( getContext(), "Email...!", Toast.LENGTH_SHORT ).show();
//                    checkUserRegister( charSequence.toString() );
//                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        } );
    }

    private void checkUserRegister( String email){
        new UserPermissionM().onUserRegisterCheckListener( this, email);
    }

    private void onAdminPermissionCheckStart(String mobile, String email) {
        checkAdminPermission.checkAdminPermission( this, mobile, email );
    }

    @BindingAdapter({"toastMessage"})
    public static void setToastMessage(View view, String message) {
        if (view != null && message != null) {
            Toast.makeText( view.getContext(), message, Toast.LENGTH_SHORT ).show();
        }
    }

    private void showToast(String msg){
        try{
            Toast.makeText( getContext(), msg, Toast.LENGTH_SHORT ).show();
        }catch(Exception e){

        }
    }

    @Override
    public void onUserRegisteredChecked(boolean isRegistered, String email) {
        if ( isRegistered ){
            logInLayout.setVisibility( View.VISIBLE );
            sigUpLayout.setVisibility( View.GONE );
        }else{
            sigUpLayout.setVisibility( View.VISIBLE );
            logInLayout.setVisibility( View.GONE );
        }

        if (email != null){
            logInViewModel.setUserEmail( email );
        }
        dismissDialog();
    }

    @Override
    public void onNotRegisteredUser() {
        emailEditText.setError( "Mobile Not Registered!" );
        dismissDialog();
    }

    @Override
    public void onSignInResponse(boolean isSuccess, String userMobile) {
//        dismissDialog();
        if (isSuccess){
            writeFileInLocal( getContext(), "mobile", userMobile );
            onAdminPermissionCheckStart( userMobile, null );
        }else{
            dismissDialog();
            showToast( userMobile ); // //userMobile = Error Message
        }
    }

    @Override
    public void onSignUpResponse(boolean isSuccess, String userMobile, String authID) {
//        dismissDialog();
        if (isSuccess){
            writeFileInLocal( getContext(), "mobile", userMobile );
            onAdminPermissionCheckStart( userMobile, null );
        }else{
            dismissDialog();
            showToast( userMobile ); //userMobile = Error Message
        }
    }

    @Override
    public void showDialog(){
        if (dialog != null)
            dialog.show();
    }

    @Override
    public void dismissDialog(){
        if (dialog.isShowing()){
            dialog.dismiss();
        }
    }

    @Override
    public void onAdminPermissionCheckFinish(boolean permission) {
        dismissDialog();
        if (permission){
            // Check Successfully!
            try {
                // TODO : Add Check -> To Check whether User information updated or not...!
                if (USER_ACCOUNT.getUser_city_code() != null){
                    writeFileInLocal( getContext(), "citycode", USER_ACCOUNT.getUser_city_code()  );
                }else{
                    writeFileInLocal( getContext(), "citycode", "BHOPAL" );
                }

                startActivity( new Intent( getActivity(), MainActivity.class ) );
                getActivity().finish();
            }catch (NullPointerException e){
                Log.d( "SignInFragment", "Activity Null : "+ e.getMessage() );
            }
        }else{
            Toast.makeText( getActivity(), "Sorry! you don't have permission to use this App. Your Account has been blocked! Please Contact to your Boss!",
                    Toast.LENGTH_SHORT ).show();
        }
    }



}
