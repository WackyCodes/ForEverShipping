package ean.ecom.shipping.other;

import com.google.firebase.firestore.GeoPoint;

import ean.ecom.shipping.profile.User;

/**
 * Created by Shailendra (WackyCodes) on 21/08/2020 03:52
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class StaticValues {

    public static String APP_VERSION = "em-1-01"; // ev-1-01

    public static final User USER_ACCOUNT = new User();

    public static final int STORAGE_PERMISSION = 1;
    /**  Order Status
     *          1. WAITING - ( For Accept )
     *          2. ACCEPTED - ( Preparing )
     *          3. PACKED - ( Waiting for Delivery ) READY_TO_DELIVERY
     *          4. PROCESS  - When Any Delivery Boy Accept to Delivering...
     *          5. PICKED - ( On Delivery ) OUT_FOR_DELIVERY...
     *          6. SUCCESS - Success Full Delivered..!
     *          7. CANCELLED -  When Order has been cancelled by user...
     *          8. FAILED -  when PayMode Online and payment has been failed...
     *          9. PENDING - when Payment is Pending...
     *
     */

    // Other Values....
    public static final int ID_UPDATE = 51;
    public static final int ID_DELETE = 52;
    public static final int ID_CLICK = 53;
    public static final int ID_MOVE = 54;
    public static final int ID_COPY = 55;

    // for Order View , local ID
    public static final int VIEW_ORDER_FROM_NOTIFICATION = 1;
    public static final int VIEW_ORDER_FROM_CURRENT_ORDERS = 2;
    public static final int VIEW_ORDER_FROM_COMPLETE_ORDERS = 3;

    public static String USER_MOBILE = "7566930540";

    public static final int PERMISSIONS_REQUEST_ENABLE_GPS = 101;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 102;
    public static final int ERROR_DIALOG_REQUEST = 103;
    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    public static final String mapServicePackage = "ean.ecom.shipping.service.LocationService";
    public static GeoPoint MY_GEO_POINTS;

    // Delivery Boy Directory !
    public static final String dMainCollection  = "";









}
