package com.example.choiceproperties.Views.Fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.choiceproperties.CallBack.CallBack;
import com.example.choiceproperties.Constant.Constant;
import com.example.choiceproperties.Models.Customer;
import com.example.choiceproperties.Models.Plots;
import com.example.choiceproperties.R;
import com.example.choiceproperties.Views.dialog.ProgressDialogClass;
import com.example.choiceproperties.repository.LeedRepository;
import com.example.choiceproperties.repository.UserRepository;
import com.example.choiceproperties.repository.impl.LeedRepositoryImpl;
import com.example.choiceproperties.repository.impl.UserRepositoryImpl;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Fragment_Sale_Plots extends Fragment implements View.OnClickListener {

    EditText inputCustomerName, inputSalePrice, inputDepositAmount, inputAgentName;
    RadioGroup GroupDeposite, GroupComission;
    RadioButton Rdeposite, Rcomission, radioCash, radioPaid;
    Button btnAdd;
    Spinner spinnerPlotNumber;
    String Sdeposite, Scomission;

    ProgressDialogClass progressDialogClass;
    UserRepository userRepository;
    DatabaseReference ref;
    LeedRepository leedRepository;
    ArrayList<Plots> plotsArrayList;
    ArrayList<String> plotsNumbersList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.layout_sale_plots, container, false);

        userRepository = new UserRepositoryImpl(getActivity());
        leedRepository = new LeedRepositoryImpl();
        progressDialogClass = new ProgressDialogClass(getActivity());

        plotsArrayList = new ArrayList<>();
        plotsNumbersList = new ArrayList<>();

        ref = Constant.PLOT_TABLE_REF;

        spinnerPlotNumber = (Spinner) view.findViewById(R.id.plot_number);
        inputCustomerName = (EditText) view.findViewById(R.id.customer_name);
        inputSalePrice = (EditText) view.findViewById(R.id.plot_salling_price);
        inputDepositAmount = (EditText) view.findViewById(R.id.deposit_amount);
        inputAgentName = (EditText) view.findViewById(R.id.agent_name);

        GroupDeposite = (RadioGroup) view.findViewById(R.id.group_installment_type);
        GroupComission = (RadioGroup) view.findViewById(R.id.group_agent_comission);
        radioCash = (RadioButton) view.findViewById(R.id.cash);
        radioPaid = (RadioButton) view.findViewById(R.id.comissionpaid);

        btnAdd = (Button) view.findViewById(R.id.add_button);

        btnAdd.setOnClickListener(this);
        GroupDeposite.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                Rdeposite = (RadioButton) view.findViewById(checkedId);
                Sdeposite = Rdeposite.getText().toString();

            }
        });
        if (GroupDeposite.getCheckedRadioButtonId() != -1) {
            Sdeposite = ((RadioButton) GroupDeposite.getChildAt(GroupDeposite.indexOfChild(GroupDeposite.findViewById(GroupDeposite.getCheckedRadioButtonId())))).getText().toString();

        }
        GroupComission.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                Rcomission = (RadioButton) view.findViewById(checkedId);
                Scomission = Rcomission.getText().toString();

            }
        });
        if (GroupComission.getCheckedRadioButtonId() != -1) {
            Scomission = ((RadioButton) GroupComission.getChildAt(GroupComission.indexOfChild(GroupComission.findViewById(GroupComission.getCheckedRadioButtonId())))).getText().toString();
        }

        ReadPplots();
        return view;

    }

    private void ReadPplots() {
        plotsNumbersList.clear();
        plotsArrayList.clear();
        userRepository.readPlots(new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    plotsArrayList = (ArrayList<Plots>) object;

                    for (Plots plot : plotsArrayList) {
                        plotsNumbersList.add(plot.getPlotnumber());
                    }

                    ArrayAdapter<String> plotNumbersAdapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item, plotsNumbersList);
                    plotNumbersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerPlotNumber.setAdapter(plotNumbersAdapter);
                }
            }

            @Override
            public void onError(Object object) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        try {

            if (v == btnAdd) {
                progressDialogClass.showDialog(this.getString(R.string.loading), this.getString(R.string.PLEASE_WAIT));
                String plotnumber = spinnerPlotNumber.getSelectedItem().toString();

                userRepository.readPlotsByPlotNumber(plotnumber, new CallBack() {
                    @Override
                    public void onSuccess(Object object) {
                        if (object != null) {
                            Plots plots1 = (Plots) object;
                            String plotId = plots1.getPloteId();

                            final Plots plots = fillUserModel(plots1.getPlotarea());
                            ref.child(plotId).removeValue();
                            SalePlot(plots);
                        }
                    }

                    @Override
                    public void onError(Object object) {

                    }
                });

            }


        } catch (Exception e) {
        }

    }

    private void SalePlot(Plots plots) {
        userRepository.salePlot(plots, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                Toast.makeText(getContext(), "Plot Sold Successfully", Toast.LENGTH_SHORT).show();

                inputCustomerName.setText("");
                inputSalePrice.setText("");
                inputDepositAmount.setText("");
                inputAgentName.setText("");
                radioCash.setChecked(true);
                radioPaid.setChecked(true);
                progressDialogClass.dismissDialog();

            }

            @Override
            public void onError(Object object) {
                progressDialogClass.dismissDialog();
            }
        });

    }


    private Plots fillUserModel(String area) {
        Plots plots = new Plots();

        int salePrice = Integer.parseInt(inputSalePrice.getText().toString());
        int depositeAmount = Integer.parseInt(inputDepositAmount.getText().toString());
        int remaining = salePrice - depositeAmount;

        plots.setPlotnumber(spinnerPlotNumber.getSelectedItem().toString());
        plots.setCustomerNmae(inputCustomerName.getText().toString());
        plots.setPlotPrice(inputSalePrice.getText().toString());
        plots.setDepositAmount(inputDepositAmount.getText().toString());
        plots.setDepositType(Sdeposite);
        plots.setRemainingAmount(String.valueOf(remaining));
        plots.setPayedAmount(inputDepositAmount.getText().toString());
        plots.setAgentName(inputAgentName.getText().toString());
        plots.setComissionStatus(Scomission);
        plots.setPlotarea(area);
        plots.setStatus(Constant.STATUS_PLOT_SOLD);

        plots.setPloteId(Constant.SOLD_PLOT_TABLE_REF.push().getKey());

        return plots;
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}