package com.moomen.chessrays.ui.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.moomen.chessrays.R;
import com.moomen.chessrays.model.User;
import com.moomen.chessrays.ui.Activity.MainActivity;
import com.moomen.chessrays.utils.PreferenceUtils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;


public class LoginFragment extends Fragment {

    private View view;

    private Button startButton;
    private EditText nameEditText;
    private ProgressBar progressBar;

    private FirebaseFirestore firebaseFirestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);
        startButton = view.findViewById(R.id.button);
        nameEditText = view.findViewById(R.id.editTextTextPersonName);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        firebaseFirestore = FirebaseFirestore.getInstance();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String name = nameEditText.getText().toString().trim();
                //Check empty edit text
                if (name.isEmpty()) {
                    nameEditText.setError("Your Name is required!");
                    nameEditText.requestFocus();
                    progressBar.setVisibility(View.GONE);
                } else {
                    loginByName(name);
                }
            }
        });
        return view;
    }

    private void loginByName(String name) {
        Query query = FirebaseFirestore.getInstance().collection("Users").whereEqualTo("name", name);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        User user = new User();
                        String userUid = null;
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            user = queryDocumentSnapshot.toObject(User.class);
                            userUid = queryDocumentSnapshot.getId();
                        }
                        if (name.equals(user.getName())) {
                            PreferenceUtils.saveName(user.getName(), getContext());
                            PreferenceUtils.saveID(user.getUserID(), getContext());
                            PreferenceUtils.saveUid(userUid, getContext());
                            startActivity(new Intent(getContext(), MainActivity.class));
                            getActivity().finish();
                        } else {
                            Toast.makeText(getContext(), "Not Found! Create a new Account or check your name!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(getContext(), "Create new Account or write your name correct!", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(getContext(), "Create new Account or write your name correct!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }
}