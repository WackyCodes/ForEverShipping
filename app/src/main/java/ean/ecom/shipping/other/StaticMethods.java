package ean.ecom.shipping.other;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by Shailendra (WackyCodes) on 21/08/2020 03:57
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class StaticMethods {

    public static void showToast(Context context, String msg){
        Toast.makeText( context, msg, Toast.LENGTH_SHORT ).show();
    }

    public static String readFileFromLocal( Context context, String fileName ){
        String msg = null;
        try {
            File file = new File( context
                    .getExternalFilesDir( Environment.getExternalStorageDirectory().getAbsolutePath() ), fileName + "/"+ fileName + ".txt");
            if (file.exists()){
                FileInputStream fileIS = new FileInputStream( file );
//            FileInputStream fileIS = openFileInput( fileName );
                InputStreamReader inputStreamReader = new InputStreamReader( fileIS );
                char[] inputBuffer = new char[100];
                int charRead;
                while(( charRead = inputStreamReader.read( inputBuffer )) > 0){
                    String readString = String.copyValueOf( inputBuffer, 0, charRead );
                    if (msg != null){
                        msg += readString;
                    }else{
                        msg = readString;
                    }
                }
                inputStreamReader.close();
            }

        }catch (Exception e){
            showToast( context, e.getMessage() );
        }finally{
            return msg;
        }
    }


}
