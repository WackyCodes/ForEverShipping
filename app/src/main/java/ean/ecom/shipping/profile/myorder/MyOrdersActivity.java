package ean.ecom.shipping.profile.myorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ean.ecom.shipping.R;
import ean.ecom.shipping.database.DBQuery;

import static ean.ecom.shipping.database.DBQuery.myOrdersList;
import static ean.ecom.shipping.other.StaticValues.USER_ACCOUNT;

public class MyOrdersActivity extends AppCompatActivity implements MyOrderListener{

    private RecyclerView recyclerViewOrder;
    private TextView textViewNoOrder;

    public static MyOrderAdaptor myOrderAdaptor;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_my_orders );
        dialog = new ProgressDialog( this );
        dialog.setTitle( "Please wait..." );
        dialog.setCancelable( false );


        Toolbar toolbar = findViewById( R.id.appToolbar );
        setSupportActionBar( toolbar );

        try {
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );
            getSupportActionBar().setTitle( "My Delivery" );
        }catch (NullPointerException ignored){ }

        recyclerViewOrder = findViewById( R.id.my_order_recycler );
        textViewNoOrder = findViewById( R.id.no_delivery_textView );
        textViewNoOrder.setVisibility( View.GONE );

        LinearLayoutManager layoutManager = new LinearLayoutManager( this );
        layoutManager.setOrientation( RecyclerView.VERTICAL );
        recyclerViewOrder.setLayoutManager( layoutManager );
        if (myOrdersList.size() == 0){
            dialog.show();
            DBQuery.getMyOrderList( this, USER_ACCOUNT.getUser_mobile());
        }

        myOrderAdaptor = new MyOrderAdaptor();
        recyclerViewOrder.setAdapter( myOrderAdaptor );

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }
        else return super.onOptionsItemSelected( item );
    }


    @Override
    public void onLoadProductResponse() {
        if (myOrdersList.size() == 0){
            textViewNoOrder.setVisibility( View.VISIBLE );
        }
        myOrderAdaptor.notifyDataSetChanged();
        dismissDialog();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText( this, msg, Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void showDialog() {
        dialog.show();
    }

    @Override
    public void dismissDialog() {
        dialog.dismiss();
    }


}