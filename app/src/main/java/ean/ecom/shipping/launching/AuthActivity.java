package ean.ecom.shipping.launching;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

import ean.ecom.shipping.R;
import ean.ecom.shipping.launching.fragment.SignInFragment;

public class AuthActivity extends AppCompatActivity {

    private FrameLayout authFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_auth );

        authFrameLayout = findViewById( R.id.auth_frame_layout );

        // Set Fragment..
        setFragment( new SignInFragment() );

    }

    private void setFragment (Fragment fragment){
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace( authFrameLayout.getId(), fragment );
        fragmentTransaction.commit();
    }

}