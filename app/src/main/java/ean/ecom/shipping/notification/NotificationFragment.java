package ean.ecom.shipping.notification;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ean.ecom.shipping.OnFragmentSetListener;
import ean.ecom.shipping.R;

import static ean.ecom.shipping.other.StaticValues.NOTIFICATION_TYPE_ORDERS;
import static ean.ecom.shipping.other.StaticValues.NOTIFICATION_TYPE_OTHERS;

public class NotificationFragment extends Fragment implements OnFragmentSetListener {

    public NotificationFragment(OnFragmentSetListener onFragmentSetListener, int notificationType) {
        this.onFragmentSetListener = onFragmentSetListener;
        this.notificationType = notificationType;
    }
    private int notificationType;

    private OnFragmentSetListener onFragmentSetListener;
    public static NotificationAdaptor notificationAdaptor;
    public static NotificationAdaptor orderNotificationAdaptor;
    private RecyclerView recyclerNotification;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_notification, container, false );

        recyclerNotification = view.findViewById( R.id.notification_recycler );

        LinearLayoutManager layoutManager = new LinearLayoutManager( getContext() );
        layoutManager.setOrientation( RecyclerView.VERTICAL );
        recyclerNotification.setLayoutManager( layoutManager );

        if (notificationType == NOTIFICATION_TYPE_ORDERS){
            orderNotificationAdaptor = new NotificationAdaptor( this, NOTIFICATION_TYPE_ORDERS );
            recyclerNotification.setAdapter( orderNotificationAdaptor );
            orderNotificationAdaptor.notifyDataSetChanged();
        }else{
            notificationAdaptor = new NotificationAdaptor( this, NOTIFICATION_TYPE_OTHERS );
            recyclerNotification.setAdapter( notificationAdaptor );
            notificationAdaptor.notifyDataSetChanged();
        }

        return view;
    }

    @Override
    public void onResume() {
        if (notificationAdaptor!=null){
            notificationAdaptor.notifyDataSetChanged();
        }
        super.onResume();
    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public void showToast(String msg) {
        onFragmentSetListener.showToast( msg );
    }


    @Override
    public void onBackPressed(int From, String backTitle) {
        onFragmentSetListener.onBackPressed( From, backTitle );
    }

    @Override
    public void setNextFragment(Fragment fragment) {
        onFragmentSetListener.setNextFragment( fragment );
    }


}