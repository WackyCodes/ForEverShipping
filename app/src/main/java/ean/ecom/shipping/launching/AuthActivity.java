package ean.ecom.shipping.launching;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.FrameLayout;

import ean.ecom.shipping.R;
import ean.ecom.shipping.launching.fragment.SignInFragment;
import ean.ecom.shipping.other.networkstate.MyReceiver;

public class AuthActivity extends AppCompatActivity {

    private FrameLayout authFrameLayout;

    private BroadcastReceiver broadcastReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_auth );

        authFrameLayout = findViewById( R.id.auth_frame_layout );

        // Set Fragment..
        setFragment( new SignInFragment() );

        broadcastReceiver = new MyReceiver( );

    }

    private void setFragment (Fragment fragment){
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace( authFrameLayout.getId(), fragment );
        fragmentTransaction.commit();
    }

    public void broadcastIntent() {
//        registerReceiver(broadcastReceiver, new IntentFilter( ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
//        unregisterReceiver(broadcastReceiver);
    }


}