package ean.ecom.shipping.other.networkstate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.widget.Toast;

/**
 * Created by Shailendra (WackyCodes) on 23/09/2020 08:44
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class MyReceiver extends BroadcastReceiver {

    private static Object PendingResult;

    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean status = NetworkUtil.getConnectivityStatusString(context);

        if(status) {
//            status="No Internet Connection";
        }else{

        }

        while (!status){
            if (status) {
//                return;
                try {
                    this.wait( 2000 );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.getResultCode();
            }
        }
        Toast.makeText(context, "status", Toast.LENGTH_LONG).show();
    }


    private void checkConnectIon(){
        Intent i = new Intent();

//        registerReceiver(this, new IntentFilter( ConnectivityManager.CONNECTIVITY_ACTION));
    }


}
