package com.example.choiceproperties.Views.Adapters;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choiceproperties.Constant.Constant;
import com.example.choiceproperties.Models.Plots;
import com.example.choiceproperties.R;
import com.example.choiceproperties.Views.Activities.Update_Sold_Out_Plots_Activity;
import com.example.choiceproperties.Views.dialog.ProgressDialogClass;
import com.example.choiceproperties.repository.LeedRepository;
import com.example.choiceproperties.utilities.Utility;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale;
import static com.example.choiceproperties.Constant.Constant.GLOBAL_DATE_FORMATE;

public class Reports_Adapter extends RecyclerView.Adapter<Reports_Adapter.ViewHolder> {

    private static List<Plots> searchArrayList;
    private Context context;
    private boolean isFromRequest;
    ProgressDialogClass progressDialogClass;
    LeedRepository leedRepository;
    Installments_Adapter adapter;
    ArrayList<String> install;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;

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
    public void onBindViewHolder(final Reports_Adapter.ViewHolder holder, final int position) {
        final Plots plots = searchArrayList.get(position);
        install = new ArrayList<>();

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



        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(holder.card_view.getContext());
                dialog.setContentView(R.layout.dialogeditnotice);

                Button btnYes = (Button) dialog.findViewById(R.id.dialogButtoncancle);
                ImageView imgPDF = (ImageView) dialog.findViewById(R.id.imgPDF);
                RecyclerView recycleInstallment = (RecyclerView) dialog.findViewById(R.id.recycle_installments);

                TextView txtCustomerName = (TextView) dialog.findViewById(R.id.txt_customer_name_value);
                TextView txtTotalAmount = (TextView) dialog.findViewById(R.id.txt_total_amount_value);
                TextView txtSoldDate = (TextView) dialog.findViewById(R.id.txt_sold_date_value);
                TextView txtPaidAmount = (TextView) dialog.findViewById(R.id.txt_paid_amount_value);
                TextView txtPendingAmount = (TextView) dialog.findViewById(R.id.txt_panding_amount_value);
                TextView txtAgentName = (TextView) dialog.findViewById(R.id.txt_agent_name_value);
                TextView txtPlotNumber = (TextView) dialog.findViewById(R.id.txt_plot_number_value);

                txtPlotNumber.setText(": " + plots.getPlotnumber());
                txtCustomerName.setText(": " + plots.getCustomerNmae());
                txtTotalAmount.setText(": " + plots.getPlotPrice());
                txtPaidAmount.setText(": " + plots.getPayedAmount());
                txtPendingAmount.setText(": " + plots.getRemainingAmount());
                txtAgentName.setText(": " + plots.getAgentName());
                txtSoldDate.setText(": " + Utility.convertMilliSecondsToFormatedDate(searchArrayList.get(position).getCreatedDateTimeLong(), GLOBAL_DATE_FORMATE));

                if (plots.getInstallments() != null) {
//            holder.txtStatus.setText(": " + searchArrayList.get(position).getAgentName());
                    install = plots.getInstallments();
                    adapter = new Installments_Adapter(holder.card_view.getContext(), install);
                    //adding adapter to recyclerview
                    recycleInstallment.setAdapter(adapter);
                    recycleInstallment.setHasFixedSize(true);
                    recycleInstallment.setLayoutManager(new LinearLayoutManager(holder.card_view.getContext()));
                    adapter.notifyDataSetChanged();
                }

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                imgPDF.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        createPdfWrapper();
                    }

                    private void createPdfWrapper() {

                            try {
                                createPdf();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                    }

                    private void createPdf() throws FileNotFoundException {

                        Document doc = new Document();
                        try {
                            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PLOTS DATABASE/CUSTOMER REPORTS/";

                            File dir = new File(path);
                            if (!dir.exists())
                                dir.mkdirs();

                            Log.d("PDFCreator", "PDF Path: " + path);

                            File file = new File(dir, plots.getCustomerNmae() + ".pdf");
                            FileOutputStream fOut = new FileOutputStream(file);

                            PdfWriter.getInstance(doc, fOut);

                            doc.open();
                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                            String formattedDate = df.format(c);

                            Paragraph address = new Paragraph("details");
                            Paragraph Date = new Paragraph("Date: "+formattedDate);
                            /* You can also SET FONT and SIZE like this */
                            Font paraFont2 = new Font(Font.FontFamily.HELVETICA);
                            paraFont2.setSize(11);
                            address.setAlignment(Paragraph.ALIGN_CENTER);
                            address.setFont(paraFont2);
                            doc.add(address);

                            Paragraph blankspace = new Paragraph("\n");
                            doc.add(blankspace);


                            Font paraFonto = new Font(Font.FontFamily.HELVETICA);
                            paraFonto.setSize(11);
                            Date.setAlignment(Paragraph.ALIGN_RIGHT);
                            Date.setFont(paraFonto);
                            doc.add(Date);

                            Paragraph blankspace0 = new Paragraph("\n");
                            doc.add(blankspace0);
                            doc.add(blankspace0);
                            Phrase phrase5 = new Phrase();
                            PdfPCell phraseCell5 = new PdfPCell();
                            phraseCell5.addElement(phrase5);
                            PdfPTable phraseTable5 = new PdfPTable(2);
                            phraseTable5.setWidthPercentage(100);
                            phraseTable5.setWidths(new int[]{50, 50});
                            phraseTable5.setHorizontalAlignment(Element.ALIGN_CENTER);

                            phraseTable5.addCell("CUSTOMER NAME");
                            phraseTable5.addCell(plots.getCustomerNmae());

                            phrase5.setFont(paraFont2);

                            Phrase phraseTableWrapper5 = new Phrase();
                            phraseTableWrapper5.add(phraseTable5);
                            doc.add(phraseTableWrapper5);
/////////////////////////////////////////////////////////////////////////
                            Phrase phrase6 = new Phrase();
                            PdfPCell phraseCell6 = new PdfPCell();
                            phraseCell6.addElement(phrase6);
                            PdfPTable phraseTable6 = new PdfPTable(2);
                            phraseTable6.setWidthPercentage(100);
                            phraseTable6.setWidths(new int[]{50, 50});
                            phraseTable6.setHorizontalAlignment(Element.ALIGN_CENTER);

                            phraseTable6.addCell("PLOT NUMBER");
                            phraseTable6.addCell(plots.getPlotnumber());

                            phrase6.setFont(paraFont2);

                            Phrase phraseTableWrapper6 = new Phrase();
                            phraseTableWrapper6.add(phraseTable6);
                            doc.add(phraseTableWrapper6);
/////////////////////////////////////////////////////////////////////////
                            Phrase phrase7 = new Phrase();
                            PdfPCell phraseCell7 = new PdfPCell();
                            phraseCell7.addElement(phrase7);
                            PdfPTable phraseTable7 = new PdfPTable(2);
                            phraseTable7.setWidthPercentage(100);
                            phraseTable7.setWidths(new int[]{50, 50});
                            phraseTable7.setHorizontalAlignment(Element.ALIGN_CENTER);

                            phraseTable7.addCell("PLOT AREA");
                            phraseTable7.addCell(plots.getPlotarea());

                            phrase7.setFont(paraFont2);

                            Phrase phraseTableWrapper7 = new Phrase();
                            phraseTableWrapper7.add(phraseTable7);
                            doc.add(phraseTableWrapper7);
/////////////////////////////////////////////////////////////////////////
                            Phrase phrase8 = new Phrase();
                            PdfPCell phraseCell8 = new PdfPCell();
                            phraseCell8.addElement(phrase8);
                            PdfPTable phraseTable8 = new PdfPTable(2);
                            phraseTable8.setWidthPercentage(100);
                            phraseTable8.setWidths(new int[]{50, 50});
                            phraseTable8.setHorizontalAlignment(Element.ALIGN_CENTER);

                            phraseTable8.addCell("PLOT PRICE");
                            phraseTable8.addCell(plots.getPlotPrice());

                            phrase8.setFont(paraFont2);

                            Phrase phraseTableWrapper8 = new Phrase();
                            phraseTableWrapper8.add(phraseTable8);
                            doc.add(phraseTableWrapper8);
/////////////////////////////////////////////////////////////////////////
                            Phrase phrase9 = new Phrase();
                            PdfPCell phraseCell9 = new PdfPCell();
                            phraseCell9.addElement(phrase9);
                            PdfPTable phraseTable9 = new PdfPTable(2);
                            phraseTable9.setWidthPercentage(100);
                            phraseTable9.setWidths(new int[]{50, 50});
                            phraseTable9.setHorizontalAlignment(Element.ALIGN_CENTER);

                            phraseTable9.addCell("SOLD DATE");
                            phraseTable9.addCell( Utility.convertMilliSecondsToFormatedDate(searchArrayList.get(position).getCreatedDateTimeLong(), GLOBAL_DATE_FORMATE));

                            phrase9.setFont(paraFont2);

                            Phrase phraseTableWrapper9 = new Phrase();
                            phraseTableWrapper9.add(phraseTable9);
                            doc.add(phraseTableWrapper9);
/////////////////////////////////////////////////////////////////////////
                            Phrase phrase1 = new Phrase();
                            PdfPCell phraseCell1 = new PdfPCell();
                            phraseCell1.addElement(phrase1);
                            PdfPTable phraseTable1 = new PdfPTable(2);
                            phraseTable1.setWidthPercentage(100);
                            phraseTable1.setWidths(new int[]{50, 50});
                            phraseTable1.setHorizontalAlignment(Element.ALIGN_CENTER);

                            phraseTable1.addCell("PAID AMOUNT");
                            phraseTable1.addCell(plots.getPayedAmount());

                            phrase1.setFont(paraFont2);

                            Phrase phraseTableWrapper1 = new Phrase();
                            phraseTableWrapper1.add(phraseTable1);
                            doc.add(phraseTableWrapper1);
/////////////////////////////////////////////////////////////////////////
                            Phrase phrase2 = new Phrase();
                            PdfPCell phraseCell2 = new PdfPCell();
                            phraseCell2.addElement(phrase2);
                            PdfPTable phraseTable2 = new PdfPTable(2);
                            phraseTable2.setWidthPercentage(100);
                            phraseTable2.setWidths(new int[]{50, 50});
                            phraseTable2.setHorizontalAlignment(Element.ALIGN_CENTER);

                            phraseTable2.addCell("REMAINING AMOUNT");
                            phraseTable2.addCell(plots.getRemainingAmount());

                            phrase2.setFont(paraFont2);

                            Phrase phraseTableWrapper2 = new Phrase();
                            phraseTableWrapper2.add(phraseTable2);
                            doc.add(phraseTableWrapper2);
/////////////////////////////////////////////////////////////////////////
                            Phrase phrase3 = new Phrase();
                            PdfPCell phraseCell3 = new PdfPCell();
                            phraseCell3.addElement(phrase3);
                            PdfPTable phraseTable3 = new PdfPTable(2);
                            phraseTable3.setWidthPercentage(100);
                            phraseTable3.setWidths(new int[]{50, 50});
                            phraseTable3.setHorizontalAlignment(Element.ALIGN_CENTER);

                            phraseTable3.addCell("AGENT NAME");
                            phraseTable3.addCell(plots.getAgentName());

                            phrase3.setFont(paraFont2);

                            Phrase phraseTableWrapper3 = new Phrase();
                            phraseTableWrapper3.add(phraseTable3);
                            doc.add(phraseTableWrapper3);
//////////////////////////////////////////////////////////////////////////////////
                            Phrase phrase4 = new Phrase();
                            PdfPCell phraseCell4 = new PdfPCell();
                            phraseCell4.addElement(phrase4);
                            PdfPTable phraseTable4 = new PdfPTable(2);
                            phraseTable4.setWidthPercentage(100);
                            phraseTable4.setWidths(new int[]{50, 50});
                            phraseTable4.setHorizontalAlignment(Element.ALIGN_CENTER);

                            phraseTable4.addCell("COMISSION STATUS");
                            phraseTable4.addCell(plots.getComissionStatus());

                            phrase4.setFont(paraFont2);

                            Phrase phraseTableWrapper4 = new Phrase();
                            phraseTableWrapper4.add(phraseTable4);
                            doc.add(phraseTableWrapper4);
//////////////////////////////////////////////////////////////////////////////////
                            Phrase phrase12 = new Phrase();
                            PdfPCell phraseCel = new PdfPCell();
                            phraseCel.addElement(phrase12);
                            PdfPTable phraseTable = new PdfPTable(1);
                            phraseTable.setWidthPercentage(100);
                            phraseTable.setWidths(new int[]{100});
                            phraseTable.setHorizontalAlignment(Element.ALIGN_CENTER);

                            phraseTable.addCell("INSTALLMENTS");

                            phrase12.setFont(paraFont2);

                            Phrase phraseTableWrapper11 = new Phrase();
                            phraseTableWrapper11.add(phraseTable);
                            doc.add(phraseTableWrapper11);

                            for (int i = 0; i < plots.getInstallments().size(); i++) {

                                Phrase phrase = new Phrase();
                                PdfPCell phraseCell = new PdfPCell();
                                phraseCell.addElement(phrase);
                                PdfPTable phraseTablen = new PdfPTable(1);
                                phraseTablen.setWidthPercentage(100);
                                phraseTablen.setWidths(new int[]{100});
                                phraseTablen.setHorizontalAlignment(Element.ALIGN_CENTER);

                                phraseTablen.addCell(plots.getInstallments().get(i));


                                phrase.setFont(paraFont2);

                                Phrase phraseTableWrapper = new Phrase();
                                phraseTableWrapper.add(phraseTablen);
                                doc.add(phraseTableWrapper);
                            }

//////////////////////////////////////////////////////////////////////////////////

                            Phrase phrase = new Phrase();
                            PdfPCell phraseCell = new PdfPCell();
                            phraseCell.addElement(phrase);
                            PdfPTable phraseTables = new PdfPTable(2);
                            phraseTables.setWidthPercentage(100);
                            phraseTables.setWidths(new int[]{50, 50});
                            phraseTables.setHorizontalAlignment(Element.ALIGN_CENTER);

                            phrase4.setFont(paraFont2);

                            Phrase phraseTableWrapper = new Phrase();
                            phraseTableWrapper.add(phraseTables);
                            doc.add(phraseTableWrapper);
//////////////////////////////////////////////////////////////////////////////////
                            Toast.makeText(context, "PDF Generated", Toast.LENGTH_SHORT).show();

                        } catch (DocumentException de) {
                            Log.e("PDFCreator", "DocumentException:" + de);
                        } catch (IOException e) {
                            Log.e("PDFCreator", "ioException:" + e);
                        } finally {
                            doc.close();
                        }

                        openPdf1();
                    }


                    void openPdf1() {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PLOTS DATABASE/CUSTOMER REPORTS/";
                        File file = new File(path, plots.getCustomerNmae() + ".pdf");
                        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                        Intent j = Intent.createChooser(intent, "Choose an application to open with:");
                        context.startActivity(j);
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
//        RecyclerView recycleInstallments;

        public ViewHolder(View itemView) {
            super(itemView);


            txtCustomerName = (TextView) itemView.findViewById(R.id.txt_txt_customer_name_value);
            txtAddress = (TextView) itemView.findViewById(R.id.txt_address_value);
            txtNumber = (TextView) itemView.findViewById(R.id.txt_number_value);
            txtStatus = (TextView) itemView.findViewById(R.id.txt_status_value);
            card_view = (CardView) itemView.findViewById(R.id.card_view);
//            recycleInstallments = (RecyclerView) itemView.findViewById(R.id.recycle_installments);
//            layout = (LinearLayout) itemView.findViewById(R.id.layoutdetails);

        }
    }

    public void reload(ArrayList<Plots> leedsModelArrayList) {
        searchArrayList.clear();
        searchArrayList.addAll(leedsModelArrayList);
        notifyDataSetChanged();
    }

}