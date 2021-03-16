package com.example.mobileapps;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListFragment extends Fragment {

    private SharedViewModel viewModel;

    SQLiteDatabase transactionDB;
    TransactionAdapter adapter;
    ImageButton filterBtn;
    FloatingActionButton flActBtn;
    RecyclerView rvRecyclerView;
    //SharedViewModel viewModel;

    public ListFragment() {
        super(R.layout.fragment_list);
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransactionDBHelper dbHelper = new TransactionDBHelper(getActivity());
        transactionDB = dbHelper.getWritableDatabase();

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        adapter = new TransactionAdapter(getActivity(), getAllItems());
        rvRecyclerView = view.findViewById(R.id.rvRecyclerView);
        rvRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rvRecyclerView.setAdapter(this.adapter);
        rvRecyclerView.addItemDecoration(new MarginItemDecoration(10));
        flActBtn = view.findViewById(R.id.floatingActionButton);

        filterBtn = view.findViewById(R.id.ibFilterBtn);
        filterBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final FragmentManager fm = getActivity().getSupportFragmentManager();
                final FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.flFrameLayout, new SetListFilters());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        flActBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final FragmentManager fm = getActivity().getSupportFragmentManager();
                final FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.flFrameLayout, new AddItemFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        //addRandomTransactionItem()
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getCurrentCursor().observe(getViewLifecycleOwner(), new Observer<Cursor>() {
                    @Override
                    public void onChanged(Cursor cursor) {
                        TransactionAdapter newAdapter = new TransactionAdapter(getActivity(), cursor);
                        rvRecyclerView.setAdapter(newAdapter);
                    }
                });
    }

    private Cursor getAllItems(){
        return transactionDB.query(
                TransactionList.TransactionEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                "createdAt DESC, "+TransactionList.TransactionEntry._ID + " DESC");
    }
}
