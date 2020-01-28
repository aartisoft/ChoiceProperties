package com.example.choiceproperties.repository.impl;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.example.choiceproperties.CallBack.CallBack;
import com.example.choiceproperties.Constant.Constant;
import com.example.choiceproperties.Exception.ExceptionUtil;
import com.example.choiceproperties.Models.Customer;
import com.example.choiceproperties.Models.Plots;
import com.example.choiceproperties.Models.User;
import com.example.choiceproperties.repository.FirebaseTemplateRepository;
import com.example.choiceproperties.repository.UserRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import static com.example.choiceproperties.Constant.Constant.EMAIL_POSTFIX;


public class UserRepositoryImpl extends FirebaseTemplateRepository implements UserRepository {
    private Activity _activity;

    public UserRepositoryImpl(final Activity activity) {
        _activity = activity;
    }

    public UserRepositoryImpl() {

    }


    @Override
    public void createUserData(User user, final CallBack callBack) {
        DatabaseReference databaseReference = Constant.USER_TABLE_REF.child(user.getUserId());
        fireBaseCreate(databaseReference, user, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                callBack.onSuccess(object);
            }

            @Override
            public void onError(Object object) {
                callBack.onError(object);
            }
        });
    }


    @Override
    public void readUserByUserId(String userId, final CallBack callBack) {
        final Query query = Constant.USER_TABLE_REF.child(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    if (dataSnapshot.getValue() != null) {
                        try {
                            if (dataSnapshot.hasChildren()) {
                                callBack.onSuccess(dataSnapshot.getValue(User.class));
                            } else {
                                callBack.onError(null);
                            }
                        } catch (Exception e) {
                            ExceptionUtil.logException(e);
                        }
                    } else
                        callBack.onError(null);
                } else
                    callBack.onError(null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callBack.onError(databaseError);
            }
        });
    }

    @Override
    public void createCustomer(Customer customer, final CallBack callBack) {
        DatabaseReference databaseReference = Constant.CUSTOMERS_TABLE_REF.child(customer.getCustomerId());
        fireBaseCreate(databaseReference, customer, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                callBack.onSuccess(object);
            }

            @Override
            public void onError(Object object) {
                callBack.onError(object);
            }
        });
    }

    @Override
    public void createPlot(Plots plots, final CallBack callBack) {
        DatabaseReference databaseReference = Constant.PLOT_TABLE_REF.child(plots.getPloteId());
        fireBaseCreate(databaseReference, plots, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                callBack.onSuccess(object);
            }

            @Override
            public void onError(Object object) {
                callBack.onError(object);
            }
        });
    }


    @Override
    public void readPlots(final CallBack callBack) {
        final Query query = Constant.PLOT_TABLE_REF;
        fireBaseNotifyChange(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                        ArrayList<Plots> leedsModelArrayList = new ArrayList<>();
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                            Plots plots = suggestionSnapshot.getValue(Plots.class);
                            leedsModelArrayList.add(plots);
                        }
                        callBack.onSuccess(leedsModelArrayList);
                    } else {
                        callBack.onSuccess(null);
                    }
                }
            }

            @Override
            public void onError(Object object) {
                callBack.onError(object);
            }
        });
    }

    @Override
    public void salePlot(Plots plots, final CallBack callBack) {
        DatabaseReference databaseReference = Constant.SOLD_PLOT_TABLE_REF.child(plots.getPloteId());
        fireBaseCreate(databaseReference, plots, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                callBack.onSuccess(object);
            }

            @Override
            public void onError(Object object) {
                callBack.onError(object);
            }
        });
    }

    @Override
    public void readPlotsByPlotNumber(String plotNumber, final CallBack callBack) {
        final Query query = Constant.PLOT_TABLE_REF.orderByChild("plotnumber").equalTo(plotNumber);
        fireBaseNotifyChange(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                        Plots plots = new Plots();
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                             plots = suggestionSnapshot.getValue(Plots.class);

                        }
                        callBack.onSuccess(plots);
                    } else {
                        callBack.onSuccess(null);
                    }
                }
            }

            @Override
            public void onError(Object object) {
                callBack.onError(object);
            }
        });
    }

    @Override
    public void readSoldOutPlots(final CallBack callBack) {
        final Query query = Constant.SOLD_PLOT_TABLE_REF;
        fireBaseNotifyChange(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                        ArrayList<Plots> leedsModelArrayList = new ArrayList<>();
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                            Plots plots = suggestionSnapshot.getValue(Plots.class);
                            leedsModelArrayList.add(plots);
                        }
                        callBack.onSuccess(leedsModelArrayList);
                    } else {
                        callBack.onSuccess(null);
                    }
                }
            }

            @Override
            public void onError(Object object) {
                callBack.onError(object);
            }
        });
    }

    @Override
    public void readVisitedCustomers(final CallBack callBack) {
        final Query query = Constant.CUSTOMERS_TABLE_REF;
        fireBaseNotifyChange(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                        ArrayList<Customer> leedsModelArrayList = new ArrayList<>();
                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                            Customer customer = suggestionSnapshot.getValue(Customer.class);
                            leedsModelArrayList.add(customer);
                        }
                        callBack.onSuccess(leedsModelArrayList);
                    } else {
                        callBack.onSuccess(null);
                    }
                }
            }

            @Override
            public void onError(Object object) {
                callBack.onError(object);
            }
        });
    }

    @Override
    public void createAdminData(User user, final CallBack callBack) {
        DatabaseReference databaseReference = Constant.ADMIN_TABLE_REF.child(user.getUserId());
        fireBaseCreate(databaseReference, user, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                callBack.onSuccess(object);
            }

            @Override
            public void onError(Object object) {
                callBack.onError(object);
            }
        });
    }

}