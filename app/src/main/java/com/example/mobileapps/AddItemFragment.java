package com.example.mobileapps;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddItemFragment extends Fragment {
    private Button btnRevenue;
    private Button btnExpense;
    private Button btnNeutralExpense;
    private TextView etEtrAmount;
    private TextView etEtrDate;
    private TextView etEtrNote;
    private TextView tvErrorDesc;
    private Spinner spinnerCategory;
    private Button btnSbmt;
    private String type;
    SQLiteDatabase transactionDB;

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

        setImgBtnOnClick();
        btnSbmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkOnClickSubmit()) {
                    if(btnNeutralExpense.getTag()=="pressed"){
                        switch(type){
                            case "r": type = "nr"; break;
                            case "e": type = "ne"; break;
                            default:break;
                        }
                    }
                    float amountHelp = Float.parseFloat(etEtrAmount.getText().toString());
                    String note =  etEtrNote.getText().toString();
                    if (note.equals("")) {
                        addTransactionItem(type, amountHelp, spinnerCategory.getSelectedItem().toString(), etEtrDate.getText().toString());
                    }else{
                        addTransactionItem(type, amountHelp, spinnerCategory.getSelectedItem().toString(), etEtrDate.getText().toString(), note);
                    }
                }
            }
        });

        Spinner mySpinner = (Spinner) view.findViewById(R.id.spinnerCategory);

        mySpinner.setAdapter(new ArrayAdapter<CategoryEnum>(getContext(), android.R.layout.simple_spinner_item, CategoryEnum.values()));

        return view;
    }

    private void addTransactionItem(String type, Float amountNew, String category, String createdAt, String note){
        transactionDB.execSQL(String.format("INSERT INTO transactionList (type, amount, category, createdAt, editedAt, note) VALUES ('%s', '%s', '%s', '%s', CURRENT_TIMESTAMP, '%s');", type, amountNew, category, createdAt, note));
        final FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();
    }
    private void addTransactionItem(String type, Float amountNew, String category, String createdAt){
        transactionDB.execSQL(String.format("INSERT INTO transactionList (type, amount, category, createdAt, editedAt) VALUES ('%s', '%s', '%s', '%s', CURRENT_TIMESTAMP);", type, amountNew, category, createdAt));
        final FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();
    }

    private void setImgBtnOnClick() {
        btnRevenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableOtherButtons(v);
            }
        });

        btnExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableOtherButtons(v);
            }
        });

        btnNeutralExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeButtonAppearance(btnNeutralExpense);}
        });
    }

    private void changeButtonAppearance(Button button) {
        if (button.getTag() == "notPressed") {
            button.setBackgroundResource(R.drawable.rounded_select_button_pressed);
            button.setTextColor(Color.WHITE);
            button.setTag("pressed");
        } else {
            button.setBackgroundResource(R.drawable.rounded_select_button);
            button.setTextColor(ContextCompat.getColor(getContext(), R.color.mainBlue));
            button.setTag("notPressed");
        }
    }

    private void disableOtherButtons(View view){
        if(view.getId()==R.id.btnRevenue){
            changeButtonAppearance(btnRevenue);
            if(btnRevenue.getTag()=="pressed"){
                type="r";
            }else{
                type="empty";
            }
            if(btnExpense.getTag()=="pressed"){
                changeButtonAppearance(btnExpense);
            }
        }
        if(view.getId()==R.id.btnExpense){
            changeButtonAppearance(btnExpense);
            if(btnExpense.getTag()=="pressed"){
                type="e";
            }else{
                type="empty";
            }
            if(btnRevenue.getTag()=="pressed"){
                changeButtonAppearance(btnRevenue);
            }
        }
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
        etEtrDate = view.findViewById(R.id.etEnterDate);
        etEtrDate.setText(getCurrentTime());
        etEtrNote = view.findViewById(R.id.etEnterNote);
        btnSbmt = view.findViewById(R.id.btnSbmt);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        tvErrorDesc = view.findViewById(R.id.tvErrorDesciption);
    }

    private String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
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
    //help

}
