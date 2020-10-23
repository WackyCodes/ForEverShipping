package ean.ecom.shipping.main.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import ean.ecom.shipping.OnFragmentSetListener;
import ean.ecom.shipping.R;
import ean.ecom.shipping.database.DBQuery;

import static ean.ecom.shipping.SetFragmentActivity.FRAGMENT_NOTIFICATION_ORDERS;
import static ean.ecom.shipping.SetFragmentActivity.FRAGMENT_ORDER_VIEW;

public class OrderViewActivity extends AppCompatActivity implements OnFragmentSetListener {
//    public static final int FRAGMENT_NOTIFICATION_ORDERS = 2;
//    public static final int FRAGMENT_ORDER_VIEW = 3;

    private FrameLayout frameLayout;
    private FragmentManager activityFragmentManager;

    private int modelIndex;
    private int modelType;
    private CurrentOrderListModel orderModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_order_view );

        modelIndex = getIntent().getIntExtra( "MODEL_INDEX", -1);
        modelType = getIntent().getIntExtra( "MODEL_TYPE", -1);

        frameLayout = findViewById( R.id.activity_frame_layout );
        activityFragmentManager = getSupportFragmentManager();

        Toolbar toolbar = findViewById( R.id.appToolbar );
        setSupportActionBar( toolbar );
        try {
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar().setTitle( "4Ever Mall" );
        }catch (NullPointerException ignored){ }

        switch (modelType){
            case FRAGMENT_NOTIFICATION_ORDERS:
                try {
                    orderModel = DBQuery.orderNotificationList.get( modelIndex );
                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }
                break;
            case FRAGMENT_ORDER_VIEW:
                orderModel = DBQuery.currentOrderListModelList.get( modelIndex );
                break;
            default:
                // Show toast not found...!
                break;
        }

        if (orderModel != null){
            // Set Data...
            setActivityFragmentManager( new OrderViewFragment( this, orderModel ) );
        }else{
            // Discard...
            showToast( "Data not found! Try again.!" );
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        orderModel = null;
    }

    private void setActivityFragmentManager(Fragment fragment){
        FragmentTransaction fragmentTransaction = activityFragmentManager.beginTransaction();
        fragmentTransaction.add( frameLayout.getId(), fragment );
        fragmentTransaction.commit();
    }


    @Override
    public void setTitle(String title) {
        getSupportActionBar().setDisplayShowTitleEnabled( true );
        getSupportActionBar().setTitle( title );
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText( this, msg, Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void onBackPressed(int From, String backTitle) {
        if (backTitle!=null){
            setTitle(backTitle);
        }
        onBackPressed();
    }

    @Override
    public void setNextFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = activityFragmentManager.beginTransaction();
        fragmentTransaction.add( frameLayout.getId(), fragment );
        fragmentTransaction.setCustomAnimations( R.anim.slide_from_left, R.anim.slide_out_from_right,
                R.anim.slide_from_right, R.anim.slide_out_from_left);
        fragmentTransaction.addToBackStack( null );
        fragmentTransaction.commit();
    }


}