package ean.ecom.shipping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

public class SetFragmentActivity extends AppCompatActivity implements OnFragmentSetListener {

    public FrameLayout frameLayout;
    public static FragmentManager activityFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_set_fragment );
        frameLayout = findViewById( R.id.activity_frame_layout );
        activityFragmentManager = getSupportFragmentManager();

        Toolbar toolbar = findViewById( R.id.appToolbar );
        setSupportActionBar( toolbar );
        try {
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar().setTitle( "4Ever Mall" );
        }catch (NullPointerException ignored){ }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setActivityFragmentManager(Fragment fragment){
        FragmentTransaction fragmentTransaction = activityFragmentManager.beginTransaction();
        fragmentTransaction.add( frameLayout.getId(), fragment );
        fragmentTransaction.commit();
    }

    @Override
    public void setTitle(String title) {
        getSupportActionBar().setTitle( title );
    }

    @Override
    public void onBackPressed(int From, String backTitle) {
        setTitle( backTitle );
    }


}