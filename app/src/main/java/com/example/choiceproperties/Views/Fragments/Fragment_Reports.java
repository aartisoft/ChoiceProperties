package com.example.choiceproperties.Views.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choiceproperties.CallBack.CallBack;
import com.example.choiceproperties.Exception.ExceptionUtil;
import com.example.choiceproperties.Models.Plots;
import com.example.choiceproperties.R;
import com.example.choiceproperties.Views.Adapters.Reports_Adapter;
import com.example.choiceproperties.interfaces.OnFragmentInteractionListener;
import com.example.choiceproperties.repository.LeedRepository;
import com.example.choiceproperties.repository.UserRepository;
import com.example.choiceproperties.repository.impl.LeedRepositoryImpl;
import com.example.choiceproperties.repository.impl.UserRepositoryImpl;
import com.example.choiceproperties.utilities.Utility;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
import java.util.Locale;

import static com.example.choiceproperties.Constant.Constant.CALANDER_DATE_FORMATE;


public class Fragment_Reports extends Fragment {
    int fromYear, fromMonth, fromDay;
    int toYear, toMonth, toDay;
    long fromDate, toDate;
    private RecyclerView catalogRecycler;
    private ArrayList<Plots> SoldOutPlotList;
    private ArrayList<Plots> AvailablePlotList;
    Reports_Adapter adapter;

    private OnFragmentInteractionListener mListener;
    private EditText edittextfromdate, edittexttodate;
    TextView TotalPlots, SoldoutPlots, RemainingPlots;
    LeedRepository leedRepository;
    UserRepository userRepository;
    private ProgressDialog progressDialog;
    ImageView imgPDF;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mListener != null) {
            mListener.onFragmentInteraction("Reports");
        }
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        if (isNetworkAvailable()) {

        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        leedRepository = new LeedRepositoryImpl();
        userRepository = new UserRepositoryImpl();
        progressDialog = new ProgressDialog(getContext());

        SoldOutPlotList = new ArrayList<>();
        AvailablePlotList = new ArrayList<>();

        catalogRecycler = (RecyclerView) view.findViewById(R.id.catalog_recycle);
        catalogRecycler.setHasFixedSize(true);
        catalogRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        edittextfromdate = (EditText) view.findViewById(R.id.edittextfromdate);
        edittexttodate = (EditText) view.findViewById(R.id.edittexttodate);
        TotalPlots = (TextView) view.findViewById(R.id.text_view_total_plots_count);
        SoldoutPlots = (TextView) view.findViewById(R.id.text_view_sold_plots_count);
        RemainingPlots = (TextView) view.findViewById(R.id.text_view_remaining_plots_count);
        imgPDF = (ImageView) view.findViewById(R.id.imgPDF);

        getAllSoldPlots();

        setFromDateClickListner();
        setToDateClickListner();

        imgPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    createPdfWrapper();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    private void getAllSoldPlots() {
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        userRepository.readSoldOutPlots(new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    SoldOutPlotList = (ArrayList<Plots>) object;
                    getAvailablePlots();

                    progressDialog.dismiss();
                }
            }

            @Override
            public void onError(Object object) {
                progressDialog.dismiss();
            }
        });

    }

    private void getAvailablePlots() {
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        userRepository.readPlots(new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    AvailablePlotList = (ArrayList<Plots>) object;
                    serAdapter(SoldOutPlotList);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onError(Object object) {
                progressDialog.dismiss();
            }
        });

    }

    private void serAdapter(ArrayList<Plots> leedsModels) {
        setReports(leedsModels);
        if (leedsModels != null) {
            if (adapter == null) {
                adapter = new Reports_Adapter(getActivity(), leedsModels);
                catalogRecycler.setAdapter(adapter);
            } else {
                ArrayList<Plots> leedsModelArrayList = new ArrayList<>();
                leedsModelArrayList.addAll(leedsModels);
                adapter.reload(leedsModelArrayList);
            }
        }
    }

    private void filterByDate(ArrayList<Plots> leedsModelArrayList) {
        try {
            ArrayList<Plots> filterArrayList = new ArrayList<>();
            if (leedsModelArrayList != null) {
                if (fromDate > 0) {
                    for (Plots leedsModel : leedsModelArrayList) {
                        if (leedsModel.getCreatedDateTimeLong() >= fromDate && leedsModel.getCreatedDateTimeLong() <= toDate) {
                            filterArrayList.add(leedsModel);
                        }
                    }
                } else {
                    for (Plots leedsModel : leedsModelArrayList) {
                        if (leedsModel.getCreatedDateTimeLong() <= toDate) {
                            filterArrayList.add(leedsModel);
                        }
                    }
                }
            }
            serAdapter(filterArrayList);
        } catch (Exception e) {
            ExceptionUtil.logException(e);
        }
    }

    private void setReports(ArrayList<Plots> leedsModelArrayList) {
        int soldoutCount = 0, availableCount = 0;
        int totalPayout = 0;
        if (leedsModelArrayList != null) {
            soldoutCount = SoldOutPlotList.size();
            availableCount = AvailablePlotList.size();
            totalPayout = soldoutCount + availableCount;

            TotalPlots.setText(String.valueOf(totalPayout));
            SoldoutPlots.setText(String.valueOf(soldoutCount));
            RemainingPlots.setText(String.valueOf(availableCount));
        } else {
            TotalPlots.setText("0");
            SoldoutPlots.setText("0");
            RemainingPlots.setText("0");
        }
    }


    private void setFromCurrentDate() {
        Calendar mcurrentDate = Calendar.getInstance();
        fromYear = mcurrentDate.get(Calendar.YEAR);
        fromMonth = mcurrentDate.get(Calendar.MONTH);
        fromDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
    }

    private void setFromDateClickListner() {
        setFromCurrentDate();
        edittextfromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        SimpleDateFormat sdf = new SimpleDateFormat(CALANDER_DATE_FORMATE, Locale.FRANCE);
                        String formatedDate = sdf.format(myCalendar.getTime());
                        edittextfromdate.setText(formatedDate);
                        fromDay = selectedday;
                        fromMonth = selectedmonth;
                        fromYear = selectedyear;
                        fromDate = Utility.convertFormatedDateToMilliSeconds(formatedDate, CALANDER_DATE_FORMATE);
                        filterByDate(SoldOutPlotList);
                    }
                }, fromYear, fromMonth, fromDay);
                mDatePicker.show();
            }
        });
    }

    private void setToCurrentDate() {
        toDate = System.currentTimeMillis();
        Calendar mcurrentDate = Calendar.getInstance();
        toYear = mcurrentDate.get(Calendar.YEAR);
        toMonth = mcurrentDate.get(Calendar.MONTH);
        toDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
    }

    private void setToDateClickListner() {
        setToCurrentDate();
        edittexttodate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        SimpleDateFormat sdf = new SimpleDateFormat(CALANDER_DATE_FORMATE, Locale.FRANCE);
                        edittexttodate.setText(sdf.format(myCalendar.getTime()));
                        toDay = selectedday;
                        toMonth = selectedmonth;
                        toYear = selectedyear;
                        toDate = myCalendar.getTimeInMillis();
                        filterByDate(SoldOutPlotList);
                    }
                }, toYear, toMonth, toDay);
                mDatePicker.show();
            }
        });
    }

    private void createPdfWrapper() throws FileNotFoundException, DocumentException {

        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("You need to allow access to Storage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            });
                    return;
                }


                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        } else {
            createPdf();
        }
    }

    private void createPdf() throws FileNotFoundException, DocumentException {

        Document doc = new Document();
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PLOTS DATABASE/TOTAL REPORT/";

            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            Log.d("PDFCreator", "PDF Path: " + path);

            File file = new File(dir, "SoldPlots_Report" + ".pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(doc, fOut);

            doc.open();
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(c);

            Paragraph address = new Paragraph("SOLD PLOTS");
            Paragraph Date = new Paragraph("Date: " + formattedDate);
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
            PdfPTable phraseTable5 = new PdfPTable(4);
            phraseTable5.setWidthPercentage(100);
            phraseTable5.setWidths(new int[]{30, 20, 20, 30});
            phraseTable5.setHorizontalAlignment(Element.ALIGN_CENTER);

            phraseTable5.addCell("CUSTOMER NAME");
            phraseTable5.addCell("PLOT NUMBER");
            phraseTable5.addCell("PLOT PRICE");
            phraseTable5.addCell("RECEIVED AMOUNT");

            phrase5.setFont(paraFont2);

            Phrase phraseTableWrapper5 = new Phrase();
            phraseTableWrapper5.add(phraseTable5);
            doc.add(phraseTableWrapper5);

            for (int i = 0; i < SoldOutPlotList.size(); i++) {

                Phrase phrase = new Phrase();
                PdfPCell phraseCell = new PdfPCell();
                phraseCell.addElement(phrase);
                PdfPTable phraseTable = new PdfPTable(4);
                phraseTable.setWidthPercentage(100);
                phraseTable.setWidths(new int[]{30, 20, 20, 30});
                phraseTable.setHorizontalAlignment(Element.ALIGN_CENTER);

                phraseTable.addCell(SoldOutPlotList.get(i).getCustomerNmae());
                phraseTable.addCell(SoldOutPlotList.get(i).getPlotnumber());
                phraseTable.addCell(SoldOutPlotList.get(i).getPlotPrice());
                phraseTable.addCell(SoldOutPlotList.get(i).getPayedAmount());

                phrase.setFont(paraFont2);

                Phrase phraseTableWrapper = new Phrase();
                phraseTableWrapper.add(phraseTable);
                doc.add(phraseTableWrapper);
            }

            Toast.makeText(getContext(), "PDF Generated", Toast.LENGTH_SHORT).show();

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
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PLOTS DATABASE/TOTAL REPORT/";
        File file = new File(path, "SoldPlots_Report.pdf");
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        Intent j = Intent.createChooser(intent, "Choose an application to open with:");
        startActivity(j);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
