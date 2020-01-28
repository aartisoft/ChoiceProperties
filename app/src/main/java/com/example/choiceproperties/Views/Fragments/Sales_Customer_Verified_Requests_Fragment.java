package com.example.choiceproperties.Views.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choiceproperties.CallBack.CallBack;
import com.example.choiceproperties.Constant.Constant;
import com.example.choiceproperties.Models.Requests;
import com.example.choiceproperties.R;
import com.example.choiceproperties.Views.Adapters.Sales_Customer_Requests_Adapter;
import com.example.choiceproperties.Views.Adapters.Sales_Customer_Verified_Requests_Adapter;
import com.example.choiceproperties.Views.dialog.ProgressDialogClass;
import com.example.choiceproperties.interfaces.OnFragmentInteractionListener;
import com.example.choiceproperties.repository.LeedRepository;
import com.example.choiceproperties.repository.impl.LeedRepositoryImpl;
import com.example.choiceproperties.utilities.Utility;

import java.util.ArrayList;
import java.util.List;

public class Sales_Customer_Verified_Requests_Fragment extends Fragment implements AdapterView.OnItemSelectedListener {
    // NOTE: Removed Some unwanted Boiler Plate Codes
    private OnFragmentInteractionListener mListener;
    private LeedRepository leedRepository;
    RecyclerView listView;
    Sales_Customer_Verified_Requests_Adapter adapter;
    private List<Requests> requestList;
    ProgressDialogClass progressDialogClass;

    public Sales_Customer_Verified_Requests_Fragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sales_customer_verified_requests, container, false);
        // NOTE : We are calling the onFragmentInteraction() declared in the MainActivity
        // ie we are sending "Fragment 1" as title parameter when fragment1 is activated
        if (mListener != null) {
            mListener.onFragmentInteraction("Verified");

            leedRepository = new LeedRepositoryImpl();
            progressDialogClass = new ProgressDialogClass(getActivity());
            requestList = new ArrayList<>();
            listView = (RecyclerView) view.findViewById(R.id.recycler_view_users);

            readRequests();
        }

        return view;
    }

    private void readRequests() {
        requestList.clear();
        progressDialogClass.showDialog(this.getString(R.string.loading),this.getString(R.string.PLEASE_WAIT));
        leedRepository.readRequestsByStatus(Constant.STATUS_REQUEST_VERIFIED, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    requestList = (ArrayList<Requests>) object;

                }

                adapter = new Sales_Customer_Verified_Requests_Adapter(getActivity(), requestList, false);
                //adding adapter to recyclerview
                listView.setAdapter(adapter);
                listView.setHasFixedSize(true);
                listView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter.notifyDataSetChanged();
                progressDialogClass.dismissDialog();
            }

            @Override
            public void onError(Object object) {
                Utility.showLongMessage(getActivity(), getString(R.string.server_error));
                progressDialogClass.dismissDialog();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            // NOTE: This is the part that usually gives you the error
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}