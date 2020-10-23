package ean.ecom.shipping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import ean.ecom.shipping.notification.NotificationFragment;

import static ean.ecom.shipping.other.StaticValues.NOTIFICATION_TYPE_ORDERS;

public class SetFragmentActivity extends AppCompatActivity implements OnFragmentSetListener {

    public static final int FRAGMENT_NOTIFICATION_OTHERS = 1;
    public static final int FRAGMENT_NOTIFICATION_ORDERS = 2;
    public static final int FRAGMENT_ORDER_VIEW = 3;

    public FrameLayout frameLayout;
    public static FragmentManager activityFragmentManager;

    private int fragmentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_set_fragment );
        frameLayout = findViewById( R.id.activity_frame_layout );
        activityFragmentManager = getSupportFragmentManager();

        fragmentID = getIntent().getIntExtra( "FRAGMENT_CODE", -1 );

        Toolbar toolbar = findViewById( R.id.appToolbar );
        setSupportActionBar( toolbar );
        try {
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar().setTitle( "4Ever Mall" );
        }catch (NullPointerException ignored){ }

        setFrameLayout(fragmentID);
    }

    private void setFrameLayout(int fragmentID){
        switch (fragmentID){
            case FRAGMENT_NOTIFICATION_ORDERS:
                setActivityFragmentManager( new NotificationFragment( this, NOTIFICATION_TYPE_ORDERS ) );
                break;
            case FRAGMENT_ORDER_VIEW:
            default:
                break;
        }
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
    public void setNextFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = activityFragmentManager.beginTransaction();
        fragmentTransaction.add( frameLayout.getId(), fragment );
        fragmentTransaction.setCustomAnimations( R.anim.slide_from_left, R.anim.slide_out_from_right,
                R.anim.slide_from_right, R.anim.slide_out_from_left);
        fragmentTransaction.addToBackStack( null );
        fragmentTransaction.commit();
    }

    @Override
    public void setTitle(String title) {
        getSupportActionBar().setTitle( title );
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText( this, msg, Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void onBackPressed(int From, String backTitle) {
        if (backTitle != null){
            setTitle( backTitle );
        }
        onBackPressed();
    }


}