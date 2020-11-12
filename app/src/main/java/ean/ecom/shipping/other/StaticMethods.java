package ean.ecom.shipping.other;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

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


    public static void writeFileInLocal(@NonNull Context context, String fileName, String textMsg){
        try {
//            FileOutputStream fileOS = openFileOutput( fileName, MODE_PRIVATE );
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter( fileOS );
//            outputStreamWriter.write( textMsg );
//            outputStreamWriter.close();
            File folder1 = new File(context.getExternalFilesDir( Environment.getExternalStorageDirectory().getAbsolutePath() ), fileName);
            folder1.mkdirs();
            File pdfFile = new File(folder1, fileName + ".txt");
//            InputStream inputStream = urlConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream( pdfFile );
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter( fileOutputStream );
            outputStreamWriter.write( textMsg );
            outputStreamWriter.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Timestamp getCurrentTimestamp(){
        return new Timestamp( Calendar.getInstance().getTime() );
    }

    public static String getCurrentDate(){
        //        Calendar calendar = Calendar.getInstance();
        Date date =  Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        //You can change "yyyyMMdd_HHmmss as per your requirement
        //        String crrDate = simpleDateFormat.format(new Date()) ;
        String crrDateDay = simpleDateFormat.format(new Date());
//                simpleDateFormat.format(new Date())+ " " + new SimpleDateFormat( "EEEE", Locale.ENGLISH).format( date.getTime() );
        return crrDateDay;
    }

    public static String getCurrentDay(){
        Date date =  Calendar.getInstance().getTime();
        String  currentDay = new SimpleDateFormat( "EEEE", Locale.ENGLISH).format( date.getTime());
        return currentDay;
    }

    public static String getCurrentTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String crrTime = simpleDateFormat.format(new Date()) ;
        return crrTime;
    }

    public static Timestamp getCrrTimestamp(){
        Date date =  Calendar.getInstance().getTime();
        return new Timestamp( date );
    }

    // yyyy/mm/dd hh:mm:ss

    public static String getScheduleTimeForOTP(){
        Date date =  Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/mm/dd hh:mm:ss", Locale.getDefault());

//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String currentTime = simpleDateFormat.format(new Date()) ;

//        String otpTime = currentTime.substring( 17 )

        return currentTime;

    }

    public static int getRandomOTP6(){
        Random random = new Random();
        int OTP_Number = random.nextInt(999999 - 111111) + 111111;
        return OTP_Number;
    }

    public static int getRandomOTP4(){
        Random random = new Random();
        int OTP_Number = random.nextInt(9999 - 1111) + 1111;
        return OTP_Number;
    }

    public static String getCorrectMobile(String mobile){
        if (mobile.length() == 10){
            return mobile;
        }else if (mobile.length()==11) {
            mobile = mobile.substring( 1 );
            return mobile;
        }else if (mobile.length()==12) {
            mobile = mobile.substring( 2 );
            return mobile;
        }else if (mobile.length()==13) {
            mobile = mobile.substring( 3 );
            return mobile;
        }else{
            return null;
        }
    }

    public static void gotoURL(Context context, String urlLink){
        Uri uri = Uri.parse( urlLink );
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity( intent );
    }

    // Responsive Price...
    public static String getResponsivePrice(String price){
        String returnPrice = "";
        int round = 0;
        char[] val = price.toCharArray();
        for (int i = val.length-1; i>=0; i-- ){
            round++;
            returnPrice = val[i] + returnPrice;
            // Put , or Not...
            if (round%3 == 0 && i!=0){
                returnPrice = "," + returnPrice;
            }else
            if ( i==1 && returnPrice.length() > 3) {
                if (price.length() < 8 && price.length() % 2 == 0) {
                    returnPrice = "," + returnPrice;
                }
            }
        }
        return returnPrice;
    }

    public static String getTimeFromNow( Date date ){

        SimpleDateFormat tempFormat = new SimpleDateFormat( "dd/MM/yyyy", Locale.getDefault());
        String timing =  tempFormat.format( date );
        try
        {
            SimpleDateFormat format = new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss", Locale.getDefault());
//            format.applyPattern( "yyyy/MM/ddXHH:mm:ss" );
//            Date past = format.parse(dateData + " " + timeData );

            Date fromDate = format.parse( format.format( date ) );
            Date now = format.parse( format.format( new Date() ) );
            long diff = now.getTime() - fromDate.getTime();
            /*
            1000 milliseconds = 1 second
            60 seconds = 1 minute
            60 minutes = 1 hour
            24 hours = 1 day
             */
            long seconds = diff / 1000;
            long minutes = diff / (60 * 1000);
            long hours = diff / (60 * 60 * 1000);
            long days = diff / (24 * 60 * 60 * 1000);

//            long seconds= TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
//            long minutes=TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
//            long hours=TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
//            long days=TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());

            if(seconds<=1)
            {
                timing = "Just now";
            }
            else if(seconds<60)
            {
                timing = seconds + " sec ago";
            }
            else if(minutes<60)
            {
                timing = minutes + " Min ago";
            }
            else if(hours<24)
            {
                timing = hours + " hr ago";
            }
            else if (days < 8)
            {
                timing = days + " days ago";
            }
        }
        catch (Exception j){
            j.printStackTrace();
        } finally {
            return timing;
        }
    }


    // get address Line
    public static String getAddressLine( String addressLine ){
        String link = null;
        String[] strings = addressLine.split( " " );
        for (String s : strings){
            if (link != null){
                link = link.trim() + "+" + s.trim();
            }else{
                link = s.trim();
            }
        }
        assert link != null;
        return link.trim();
    }



}
