package com.example.mobileapps;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class AddItemFragment extends Fragment {
    private ImageButton btnRevenue;
    private ImageButton btnExpense;
    private ImageButton btnNeutralExpense;
    private TextView etEtrAmount;
    private TextView etEtrDate;
    private TextView etEtrNote;
    private TextView tvErrorDesc;
    //private AutoCompleteTextView acTextView;
    private Spinner spinnerCategory;
    private Button btnSbmt;
    private String type;
    SQLiteDatabase transactionDB;
    TransactionAdapter adapter;

    public AddItemFragment() {
        type = "empty";
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransactionDBHelper dbHelper = new TransactionDBHelper(getActivity());
        transactionDB = dbHelper.getWritableDatabase();
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_additem, container, false);

        TransactionDBHelper dbHelper = new TransactionDBHelper(getActivity());
        transactionDB = dbHelper.getWritableDatabase();
        setUpViews(view);

        btnSbmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkOnClickSubmit()) {
                    float amountHelp = Float.parseFloat(etEtrAmount.getText().toString());
                    String note =  etEtrNote.getText().toString();
                    if (note==null) {
                        addTransactionItem(type, amountHelp, spinnerCategory.getSelectedItem().toString(), etEtrDate.getText().toString());
                    }else{
                        addTransactionItem(type, amountHelp, spinnerCategory.getSelectedItem().toString(), etEtrDate.getText().toString(), note);
                    }
                }
            }
        });
        setImgBtnOnClick();
        //acTextView.setAdapter(new ArrayAdapter<CategoryEnum>(getActivity(), android.R.layout.simple_list_item_1, CategoryEnum.values()));

        Spinner mySpinner = (Spinner) view.findViewById(R.id.spinnerCategory);

        mySpinner.setAdapter(new ArrayAdapter<CategoryEnum>(getContext(), android.R.layout.simple_spinner_item, CategoryEnum.values()));

        return view;
    }

    private void addTransactionItem(String type, Float amountNew, String category, String createdAt, String note){

            transactionDB.execSQL(String.format("INSERT INTO transactionList (type, amount, category, createdAt, note) VALUES ('%s', '%s', '%s', '%s', '%s');", type,amountNew,category,createdAt,note));


        final FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();
    }
    private void addTransactionItem(String type, Float amountNew, String category, String createdAt){
            transactionDB.execSQL(String.format("INSERT INTO transactionList (type, amount, category, createdAt) VALUES ('%s', '%s', '%s', '%s');", type,amountNew,category,createdAt));


        final FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();
    }

    private void setImgBtnOnClick() {
        btnRevenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRevenue.setBackgroundResource(R.drawable.rounded_imagebutton_clicked);
                btnExpense.setBackgroundResource(R.drawable.rounded_imagebutton);
                btnNeutralExpense.setBackgroundResource(R.drawable.rounded_imagebutton);
                type = "r"; // Revenue
            }
        });

        btnExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnExpense.setBackgroundResource(R.drawable.rounded_imagebutton_clicked);
                btnRevenue.setBackgroundResource(R.drawable.rounded_imagebutton);
                btnNeutralExpense.setBackgroundResource(R.drawable.rounded_imagebutton);
                type = "e"; // Expense
            }
        });

        btnNeutralExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnNeutralExpense.setBackgroundResource(R.drawable.rounded_imagebutton_clicked);
                btnExpense.setBackgroundResource(R.drawable.rounded_imagebutton);
                btnRevenue.setBackgroundResource(R.drawable.rounded_imagebutton);
                type = "ne";
            }
        });
    }

    private boolean checkOnClickSubmit() {
        tvErrorDesc.setText("");
        if (type != "empty") {
            if (contains(spinnerCategory.getSelectedItem().toString())) {
                if (etEtrAmount.getText().toString() != "" && etEtrDate.getText().toString() != "") {
                    return true;
                } else {
                    tvErrorDesc.setText("Error with amount or date!");
                }
            } else {
                tvErrorDesc.setText("Error with your category!");
            }
        } else {
            tvErrorDesc.setText("Please select a type first!");
        }
        return false;
    }

    private void setUpViews(View view) {
        btnRevenue = view.findViewById(R.id.btnRevenue);
        btnExpense = view.findViewById(R.id.btnExpense);
        btnNeutralExpense = view.findViewById(R.id.btnNeutralExpense);
        etEtrAmount = view.findViewById(R.id.etEnterAmount);
        //etEtrCtgry        = view.findViewById(R.id.etEnterCategory)
        etEtrDate = view.findViewById(R.id.etEnterDate);
        etEtrDate.setText(getCurrentTime());
        etEtrNote = view.findViewById(R.id.etEnterNote);
        btnSbmt = view.findViewById(R.id.btnSbmt);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        //acTextView = view.findViewById(R.id.acTv);
        tvErrorDesc = view.findViewById(R.id.tvErrorDesciption);
    }

    private String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return formatter.format(date);
    }

    public static boolean contains(String test) {
        for (CategoryEnum c : CategoryEnum.values()) {
            if (c.name().equals(test)) {
                return true;
            }
        }
        return false;
    }

}
