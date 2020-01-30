package com.example.choiceproperties.Views.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choiceproperties.Models.Customer;
import com.example.choiceproperties.Models.Requests;
import com.example.choiceproperties.R;
import com.example.choiceproperties.Views.dialog.ProgressDialogClass;
import com.example.choiceproperties.repository.LeedRepository;

import java.util.List;

public class Sales_Customer_Visited_Requests_Adapter extends RecyclerView.Adapter<Sales_Customer_Visited_Requests_Adapter.ViewHolder> {

    private static List<Customer> searchArrayList;
    private Context context;
    private boolean isFromRequest;
    ProgressDialogClass progressDialogClass;
    LeedRepository leedRepository;

    public Sales_Customer_Visited_Requests_Adapter(Context context, List<Customer> userArrayList, boolean isFromRequest) {
        this.context = context;
        this.searchArrayList = userArrayList;
        this.isFromRequest = isFromRequest;
    }

    @Override
    public Sales_Customer_Visited_Requests_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sales_visited_requests_adapter_layout, parent, false);
        Sales_Customer_Visited_Requests_Adapter.ViewHolder viewHolder = new ViewHolder(v);
        //  context = parent.getContext();
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final Sales_Customer_Visited_Requests_Adapter.ViewHolder holder, int position) {
        final Customer request = searchArrayList.get(position);

        if (request.getName() != null) {
            holder.txtCustomerName.setText(": " + searchArrayList.get(position).getName());
        } else {
            holder.txtCustomerName.setText(": Null");
        }
        if (request.getAddress() != null) {
            holder.txtAddress.setText(": " + searchArrayList.get(position).getAddress());
        }else {
            holder.txtAddress.setText(": Null");
        }
        if (request.getMobile() != null) {
            holder.txtNumber.setText(": " + searchArrayList.get(position).getMobile());
        }else {
            holder.txtNumber.setText(": Null");
        }
        if (request.getStatus() != null) {
            holder.txtStatus.setText(": " + searchArrayList.get(position).getStatus());
        }else {
            holder.txtStatus.setText(": Null");
        }
        if (request.getAttendedBy() != null) {
            holder.txtSalesPerson.setText(": " + searchArrayList.get(position).getAttendedBy());
        }else {
            holder.txtSalesPerson.setText(": Null");
        }


//        holder.txtbilldate.setText(": "+ Utility.convertMilliSecondsToFormatedDate(searchArrayList.get(position).getCreatedDateTimeLong(), GLOBAL_DATE_FORMATE));

    }


    @Override
    public int getItemCount() {
        return searchArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtCustomerName, txtAddress, txtNumber, txtStatus,txtSalesPerson;
        CardView card_view;
        LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);


            txtCustomerName = (TextView) itemView.findViewById(R.id.txt_txt_customer_name_value);
            txtAddress = (TextView) itemView.findViewById(R.id.txt_address_value);
            txtNumber = (TextView) itemView.findViewById(R.id.txt_number_value);
            txtStatus = (TextView) itemView.findViewById(R.id.txt_status_value);
            card_view = (CardView) itemView.findViewById(R.id.card_view);
            txtSalesPerson = (TextView) itemView.findViewById(R.id.txt_attended_value);
//            layout = (LinearLayout) itemView.findViewById(R.id.layoutdetails);

        }
    }

}