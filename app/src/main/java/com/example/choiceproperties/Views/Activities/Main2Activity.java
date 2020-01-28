package com.example.choiceproperties.Views.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.choiceproperties.R;
import com.example.choiceproperties.Views.Fragments.Admin_Sales__Tab_Fragment;
import com.example.choiceproperties.Views.Fragments.Fragment_Add_Customers;
import com.example.choiceproperties.Views.Fragments.Fragment_Add_Plots;
import com.example.choiceproperties.Views.Fragments.Fragment_Reports;
import com.example.choiceproperties.Views.Fragments.Fragment_Sale_Plots;
import com.example.choiceproperties.Views.Fragments.Fragment_Sold_Out_Plots;
import com.example.choiceproperties.Views.Fragments.Requests__Tab_Fragment;
import com.example.choiceproperties.Views.Fragments.Sales_Customer_Requests_Fragment;
import com.example.choiceproperties.interfaces.OnFragmentInteractionListener;
import com.example.choiceproperties.preferences.AppSharedPreference;
import com.google.firebase.auth.FirebaseAuth;
import com.shrikanthravi.customnavigationdrawer2.data.MenuItem;
import com.shrikanthravi.customnavigationdrawer2.widget.SNavigationDrawer;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity implements OnFragmentInteractionListener {

    SNavigationDrawer sNavigationDrawer;
    private AppSharedPreference appSharedPreference;
    int color1=0;
    Class fragmentClass;
    public static Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().hide();
        }

        Boolean per = isStoragePermissionGranted();
        if (per){
            //   Toast.makeText(this, "Storage Premission Granted", Toast.LENGTH_SHORT).show();
        }else {
//            Toast.makeText(this, "Storage Premission Required", Toast.LENGTH_SHORT).show();
        }

        appSharedPreference = new AppSharedPreference(getApplicationContext());
        sNavigationDrawer = findViewById(R.id.navigationDrawer);
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("Requests",R.color.Black));
//        menuItems.add(new MenuItem("Add Customers",R.color.Black));
        menuItems.add(new MenuItem("Add Plots",R.color.Black));
        menuItems.add(new MenuItem("Sale Plot",R.color.Black));
        menuItems.add(new MenuItem("Accept Sales Requests",R.color.Black));
        menuItems.add(new MenuItem("Sold Out Plot",R.color.Black));
        menuItems.add(new MenuItem("Reports",R.color.Black));
        menuItems.add(new MenuItem("Log Out",R.color.Black));
        sNavigationDrawer.setMenuItemList(menuItems);
        fragmentClass =  Requests__Tab_Fragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.frameLayout, fragment).commit();
        }


        sNavigationDrawer.setOnMenuItemClickListener(new SNavigationDrawer.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClicked(int position) {
                System.out.println("Position "+position);

                switch (position){
                    case 0:{
                        color1 = R.color.red;
                        fragmentClass = Requests__Tab_Fragment.class;
                        break;
                    }
//                    case 1:{
//                        color1 = R.color.orange;
//                        fragmentClass = Fragment_Add_Customers.class;
//
//                        break;
//                    }
                    case 1:{
                        color1 = R.color.green;
                        fragmentClass = Fragment_Add_Plots.class;
                        break;
                    }
                    case 2:{
                        color1 = R.color.green;
                        fragmentClass = Fragment_Sale_Plots.class;
                        break;
                    }
                    case 3:{
                        color1 = R.color.green;
                        fragmentClass = Admin_Sales__Tab_Fragment.class;
                        break;
                    }
                    case 4:{
                        color1 = R.color.green;
                        fragmentClass = Fragment_Sold_Out_Plots.class;
                        break;
                    }
                    case 5:{
                        color1 = R.color.green;
                        fragmentClass = Fragment_Reports.class;
                        break;
                    }
                    case 6:{
                        color1 = R.color.blue;
//                        FirebaseAuth.getInstance().signOut();
//                        finish();
//                        startActivity(new Intent(Main2Activity.this, LoginScreen.class));
                        clearDataWithSignOut();
                        break;
                    }

                }

                sNavigationDrawer.setDrawerListener(new SNavigationDrawer.DrawerListener() {

                    @Override
                    public void onDrawerOpened() {

                    }

                    @Override
                    public void onDrawerOpening(){

                    }

                    @Override
                    public void onDrawerClosing(){
                        System.out.println("Drawer closed");

                        try {
                            fragment = (Fragment) fragmentClass.newInstance();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (fragment != null) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.frameLayout, fragment).commit();

                        }
                    }

                    @Override
                    public void onDrawerClosed() {

                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        System.out.println("State "+newState);
                    }
                });
            }
        });

    }

    private void clearDataWithSignOut() {
        FirebaseAuth.getInstance().signOut();
        appSharedPreference.clear();
        logOut();
    }
    private void logOut() {
        Intent intent = new Intent(this, LoginScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //   Log.v(TAG,"Permission is granted");
                return true;
            } else {

                // Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    return true;
                }else {
                    return false;
                }
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            //  Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    @Override
    public void onFragmentInteraction(String title) {

    }
}
