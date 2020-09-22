package ean.ecom.ean_shipping.other;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ean.ecom.ean_shipping.R;

/**
 * Created by Shailendra (WackyCodes) on 21/08/2020 03:37
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class DialogsClass {

    public static AlertDialog.Builder alertDialog(Context c, @Nullable String title, @NonNull String body) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(c);
        if (title!=null)
            builder.setTitle(title);
        builder.setMessage(body);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder;
    }

    public static Dialog getMessageDialog(Context context, @NonNull String title, @NonNull String message){
//         Single Ok Button ...
        final Dialog dialog = new Dialog( context );
        dialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        dialog.setContentView( R.layout.dialog_message_ok_layout );
        dialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        dialog.setCancelable( false );
        Button okBtn = dialog.findViewById( R.id.dialog_ok_btn );
        TextView titleText = dialog.findViewById( R.id.dialog_title );
        TextView messageText = dialog.findViewById( R.id.dialog_message );
        titleText.setText( title );
        messageText.setText( message );
        okBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        } );
        return dialog;
    }


}
