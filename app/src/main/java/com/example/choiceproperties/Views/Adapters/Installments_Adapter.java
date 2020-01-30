package com.example.choiceproperties.Views.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choiceproperties.Models.Plots;
import com.example.choiceproperties.R;
import com.example.choiceproperties.Views.dialog.ProgressDialogClass;
import com.example.choiceproperties.repository.LeedRepository;
import com.example.choiceproperties.utilities.Utility;

import java.util.ArrayList;
import java.util.List;

import static com.example.choiceproperties.Constant.Constant.GLOBAL_DATE_FORMATE;

public class Installments_Adapter extends RecyclerView.Adapter<Installments_Adapter.ViewHolder> {

    private static ArrayList<String> searchArrayList;
    private Context context;

    ProgressDialogClass progressDialogClass;
    LeedRepository leedRepository;

    public Installments_Adapter(Context context, ArrayList<String> userArrayList) {
        this.context = context;
        this.searchArrayList = userArrayList;
    }




    @Override
    public Installments_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.installments_adapter_layout, parent, false);
        Installments_Adapter.ViewHolder viewHolder = new ViewHolder(v);
        //  context = parent.getContext();
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final Installments_Adapter.ViewHolder holder, final int position) {
        final String plots = searchArrayList.get(position);

       holder.txtInstallment.setText(plots);


    }


    @Override
    public int getItemCount() {
        return searchArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtInstallment;

        public ViewHolder(View itemView) {
            super(itemView);


            txtInstallment = (TextView) itemView.findViewById(R.id.txt_installments);


        }
    }

    public void reload(ArrayList<String> leedsModelArrayList) {
        searchArrayList.clear();
        searchArrayList.addAll(leedsModelArrayList);
        notifyDataSetChanged();
    }

}