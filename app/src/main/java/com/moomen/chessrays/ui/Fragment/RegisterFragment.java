package com.moomen.chessrays.ui.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.moomen.chessrays.R;
import com.moomen.chessrays.model.User;
import com.moomen.chessrays.ui.Activity.IntroActivity;
import com.moomen.chessrays.ui.Activity.MainActivity;
import com.moomen.chessrays.utils.PreferenceUtils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class RegisterFragment extends Fragment {

    private Button startButton;
    private EditText nameEditText;
    private ProgressBar progressBar;

    private FirebaseFirestore firebaseFirestore;

    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_register, container, false);


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
                String date = DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
                String userId = name + date.trim();
                ArrayList<String> userIDArrayList = new ArrayList<>();
                for (int i = 0; i < userId.length(); i++) {
                    userIDArrayList.add(String.valueOf(userId.charAt(i)));
                }
                Collections.shuffle(userIDArrayList);
                userId = "";
                for (int i = 0; i < userIDArrayList.size(); i++) {
                    if (!userIDArrayList.get(i).equals(" "))
                        userId = userId + userIDArrayList.get(i);
                }
                //Check empty edit text
                if (name.isEmpty()) {
                    nameEditText.setError("Your Name is required!");
                    nameEditText.requestFocus();
                    progressBar.setVisibility(View.GONE);
                } else {
                    User user = new User(name, date, userId);
                    postNameOnFirebase(user);
                }
            }
        });

        return view;
    }

    private void postNameOnFirebase(User user) {
        Query query = FirebaseFirestore.getInstance().collection("Users").whereEqualTo("name", user.getName());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty()) {
                        firebaseFirestore.collection("Users").add(user).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()) {
                                    String userUid = task.getResult().getId();
                                    PreferenceUtils.saveName(user.getName(), getContext());
                                    PreferenceUtils.saveID(user.getUserID(), getContext());
                                    PreferenceUtils.saveUid(userUid, getContext());
                                    Toast.makeText(getContext(), "Welcome!", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    startActivity(new Intent(getContext(), MainActivity.class));
                                    getActivity().finish();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Failed to start, try again!", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "This name was used! use a new name", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        });

    }

}