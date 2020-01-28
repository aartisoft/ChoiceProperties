package com.example.choiceproperties.repository;

import com.example.choiceproperties.CallBack.CallBack;
import com.example.choiceproperties.Models.Customer;
import com.example.choiceproperties.Models.Plots;
import com.example.choiceproperties.Models.User;

public interface UserRepository {

    void createUserData(final User userModel, final CallBack callback);

    void readUserByUserId(final String regId, final CallBack callBack);

    void createCustomer(final Customer customer, final CallBack callback);

    void createPlot(final Plots plots, final CallBack callback);

    void readPlots(final CallBack callback);

    void readPlotsByPlotNumber(final String plotNumber, final CallBack callBack);

    void salePlot(final Plots plots, final CallBack callback);

    void readSoldOutPlots(final CallBack callback);

    void readVisitedCustomers(final CallBack callback);

    void createAdminData(final User userModel, final CallBack callback);
}