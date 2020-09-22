package ean.ecom.ean_shipping.database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Shailendra (WackyCodes) on 21/08/2020 03:40
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class DBQuery {

    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    // get Current User Reference ...
    public static FirebaseUser currentUser = firebaseAuth.getCurrentUser();

    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public  static StorageReference storageReference = FirebaseStorage.getInstance().getReference();


}
