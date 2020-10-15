package ean.ecom.shipping.notification;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ean.ecom.shipping.R;

public class NotificationFragment extends Fragment {

    public NotificationFragment() {
    }

    public static NotificationAdaptor notificationAdaptor;
    private RecyclerView recyclerNotification;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_notification, container, false );

        recyclerNotification = view.findViewById( R.id.notification_recycler );

        LinearLayoutManager layoutManager = new LinearLayoutManager( getContext() );
        layoutManager.setOrientation( RecyclerView.VERTICAL );
        recyclerNotification.setLayoutManager( layoutManager );

        notificationAdaptor = new NotificationAdaptor(  );
        recyclerNotification.setAdapter( notificationAdaptor );
        notificationAdaptor.notifyDataSetChanged();

        return view;
    }


}