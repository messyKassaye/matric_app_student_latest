package com.students.preparation.matric.students.modules.Students.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.students.preparation.matric.students.Constants;
import com.students.preparation.matric.students.R;
import com.students.preparation.matric.students.adapter.TeachersInboxAdapter;
import com.students.preparation.matric.students.model.TeachersModel;
import com.students.preparation.matric.students.model.Tutorials;

import java.util.ArrayList;


public class TeachersInboxFragment extends Fragment {
    private DatabaseReference databaseReference;
    private ArrayList<TeachersModel> arrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TeachersInboxAdapter adapter;
    private String school;

    public TeachersInboxFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_teachers_inbox, container, false);

        adapter = new TeachersInboxAdapter(getContext(),arrayList);
        recyclerView = view.findViewById(R.id.textInboxRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        school = prefs.getString(Constants.LOGGED_IN_USER_SCHOOL, null);
        databaseReference = FirebaseDatabase.getInstance()
                .getReference(Constants.DATABSE_TEACHERS_INBOX);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot tutorialSnapshot : dataSnapshot.getChildren()) {
                    TeachersModel uploadsModel = tutorialSnapshot.getValue(TeachersModel.class);
                    if (uploadsModel.getSchool().equalsIgnoreCase(school)){
                        arrayList.add(uploadsModel);
                    }
                }
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                if (arrayList.size()<=0){
                    Toast.makeText(
                            getContext(),
                            "There is no uploaded inbox at your school",
                            Toast.LENGTH_LONG)
                            .show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }


}
