package com.example.choiceproperties.Constant;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

public class Constant {
    /************************************** Firebase Storage reference constants ***************************************************************************/
    private static final FirebaseDatabase DATABASE = FirebaseDatabase.getInstance();
    public static final DatabaseReference USER_TABLE_REF = DATABASE.getReference("user");
    public static final DatabaseReference ADMIN_TABLE_REF = DATABASE.getReference("Admin");
    public static final DatabaseReference LEEDS_TABLE_REF = DATABASE.getReference("leeds");
    public static final DatabaseReference REQUESTS_TABLE_REF = DATABASE.getReference("Request");
    public static final DatabaseReference CUSTOMERS_TABLE_REF = DATABASE.getReference("Customers");
    public static final DatabaseReference PLOT_TABLE_REF = DATABASE.getReference("Plots");
    public static final DatabaseReference SOLD_PLOT_TABLE_REF = DATABASE.getReference("SoldPlots");


    /************************************** Firebase Authentication reference constants ***************************************************************************/
    public static final FirebaseAuth AUTH = FirebaseAuth.getInstance();
    /************************************** Calender Constatns ***************************************************************************/
    public static final Calendar cal = Calendar.getInstance();
    public static final int DAY = cal.get(Calendar.DAY_OF_MONTH);
    public static final int MONTH = cal.get(Calendar.MONTH);
    public static final int YEAR = cal.get(Calendar.YEAR);
    public static String TWO_DIGIT_LIMIT = "%02d";
    public static String FOUR_DIGIT_LIMIT = "%04d";
    public static final String SUCCESS = "Success";
    public static final String GLOBAL_DATE_FORMATE = "dd MMM yyyy";
    public static final String CALANDER_DATE_FORMATE = "dd/MM/yy";

    public static final String SALES_PREFIX = "AG-";
    public static final String CUSTOMER_PREFIX = "CM-";
    public static final String EMAIL_POSTFIX = "@smartloan.com";

    //********************************************STATUS FLEADS*****************************

    public static final String STORAGE_PATH = "STORAGE_PATH";
    public static final String BITMAP_IMG = "BITMAP_IMG";
    public static final String LEED_ID = "LEED_ID";
    public static final String IMAGE_COUNT = "IMAGE_COUNT";
    public static final String TOTAL_IMAGE_COUNT = "TOTAL_IMAGE_COUNT";

    public static final FirebaseStorage STORAGE = FirebaseStorage.getInstance();
    public static final StorageReference STORAGE_REFERENCE = STORAGE.getReference();

    public static final String DOCUMENTS_PATH = "images/documents";
    public static final String CUSROMER_PROFILE_PATH = "images/customer";
    public static final String IMAGE_URI_LIST = "IMAGE_URI_LIST";

    public static final String ROLE_SALES = "SALES";
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_DEACTIVE = "DEACTIVE";

    public static final String STATUS_REQUEST_GENERATED = "GENERATED";
    public static final String STATUS_REQUEST_VERIFIED = "VERIFIED";
    public static final String STATUS_REQUEST_VISITED = "VISITED";

    public static final String STATUS_PLOT_AVAILABLE = "AVAILABLE";
    public static final String STATUS_PLOT_SOLD = "SOLD";

    public static final String PLOTS = "Plots";
}
