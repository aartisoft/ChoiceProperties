package com.example.choiceproperties.Views.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choiceproperties.Constant.Constant;
import com.example.choiceproperties.Models.Plots;
import com.example.choiceproperties.R;
import com.example.choiceproperties.Views.Activities.Update_Sold_Out_Plots_Activity;
import com.example.choiceproperties.Views.dialog.ProgressDialogClass;
import com.example.choiceproperties.repository.LeedRepository;

import java.util.ArrayList;
import java.util.List;

public class Reports_Adapter extends RecyclerView.Adapter<Reports_Adapter.ViewHolder> {

    private static List<Plots> searchArrayList;
    private Context context;
    private boolean isFromRequest;
    ProgressDialogClass progressDialogClass;
    LeedRepository leedRepository;

    public Reports_Adapter(Context context, List<Plots> userArrayList) {
        this.context = context;
        this.searchArrayList = userArrayList;
        this.isFromRequest = isFromRequest;
    }


    @Override
    public Reports_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reports_adapter_layout, parent, false);
        Reports_Adapter.ViewHolder viewHolder = new ViewHolder(v);
        //  context = parent.getContext();
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final Reports_Adapter.ViewHolder holder, int position) {
        final Plots plots = searchArrayList.get(position);

        if (plots.getPlotnumber() != null) {
            holder.txtCustomerName.setText(": " + searchArrayList.get(position).getPlotnumber());
        } else {
            holder.txtCustomerName.setText("Null");
        }
        if (plots.getCustomerNmae() != null) {
            holder.txtAddress.setText(": " + searchArrayList.get(position).getCustomerNmae());
        } else {
            holder.txtAddress.setText("Null");
        }
        if (plots.getPlotPrice() != null) {
            holder.txtNumber.setText(": " + searchArrayList.get(position).getPlotPrice());
        } else {
            holder.txtNumber.setText("Null");
        }
        if (plots.getAgentName() != null) {
            holder.txtStatus.setText(": " + searchArrayList.get(position).getAgentName());
        } else {
            holder.txtStatus.setText("Null");
        }

//        holder.txtbilldate.setText(": "+ Utility.convertMilliSecondsToFormatedDate(searchArrayList.get(position).getCreatedDateTimeLong(), GLOBAL_DATE_FORMATE));

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(holder.card_view.getContext());
                dialog.setContentView(R.layout.dialogeditnotice);

                Button btnYes = (Button) dialog.findViewById(R.id.dialogButtoncancle);
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return searchArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtCustomerName, txtAddress, txtNumber, txtStatus;
        CardView card_view;
        LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);


            txtCustomerName = (TextView) itemView.findViewById(R.id.txt_txt_customer_name_value);
            txtAddress = (TextView) itemView.findViewById(R.id.txt_address_value);
            txtNumber = (TextView) itemView.findViewById(R.id.txt_number_value);
            txtStatus = (TextView) itemView.findViewById(R.id.txt_status_value);
            card_view = (CardView) itemView.findViewById(R.id.card_view);
//            layout = (LinearLayout) itemView.findViewById(R.id.layoutdetails);

        }
    }

    public void reload(ArrayList<Plots> leedsModelArrayList) {
        searchArrayList.clear();
        searchArrayList.addAll(leedsModelArrayList);
        notifyDataSetChanged();
    }

}