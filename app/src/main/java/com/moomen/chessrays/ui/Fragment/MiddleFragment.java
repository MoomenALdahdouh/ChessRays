package com.moomen.chessrays.ui.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.moomen.chessrays.R;
import com.moomen.chessrays.adapter.HandlingAdapter;
import com.moomen.chessrays.adapter.MiddleGameAdapter;
import com.moomen.chessrays.model.MiddleGame;
import com.moomen.chessrays.model.MiddleGameCalculate;
import com.moomen.chessrays.model.NotificationsViewModel;
import com.moomen.chessrays.model.OpeningGame;
import com.moomen.chessrays.model.OpeningModel;
import com.moomen.chessrays.ui.Activity.ViewOpeningGame;
import com.moomen.chessrays.utils.MovesIdStack;
import com.moomen.chessrays.utils.PreferenceUtils;

import java.text.DateFormat;
import java.util.Calendar;

public class MiddleFragment extends Fragment {
    public static final String GAME_UID = "GAME_UID";
    private CardView cardViewCreateGame;
    private TextView middleGameTextView;
    private ImageView createGameImageView;
    private ProgressBar progressBar;
    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetDialog bottomSheetDialogVisitGame;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;

    public static final String OPENING_MOVE_UID = "OPENING_MOVE_UID";
    public static final String OPENING_MOVE_NAME = "OPENING_MOVE_NAME";
    public static final String OPENING_UID = "OPENING_UID";
    public static final String USER_UID = "USER_UID";
    private static final String VIEW_OPENING_GAME_UID = "VIEW_OPENING_GAME_UID";
    private static final String VIEW_OPENING_GAME_NAME = "VIEW_OPENING_GAME_NAME";

    private TextView textViewColor;
    private TextView textViewFirstMove;
    private TextView textViewDate;
    private TextView textViewGood;
    private TextView textViewEqual;
    private TextView textViewBad;
    private TextView textViewWin;
    private TextView textViewDraw;
    private TextView textViewLost;
    private ImageView buttonBack;

    private ImageView imageViewAddMove;
    private TextView textViewAddMove;
    private CardView cardViewCreateMenu;

    private TextView textViewScore;
    private TextView textViewTotal;
    private TextView textViewFrequency;
    private TextView textViewEfficiency;

    private String date;

    private String userId;

    //----------------------
    private EditText editTextGood;
    private EditText editTextEqual;
    private EditText editTextBad;
    private EditText editTextWin;
    private EditText editTextDraw;
    private EditText editTextLost;
    private Button buttonCreate;

    private int good = 0;
    private int equal = 0;
    private int bad = 0;
    private int win = 0;
    private int draw = 0;
    private int loss = 0;
    private int result = 0;
    private int totalResult = 0;
    private int total = 0;
    private float frequency = 0;
    private float efficiency = 0;
    private int occurance = 0;
    private int occuranceAllMiddle = 0;

    private float score = 0;
    private int totalWin = 0;
    private int totalDraw = 0;
    private int totalLoss = 0;
    private int totalGood = 0;
    private int totalEqual = 0;
    private int totalBad = 0;


    private int value = 0;
    private boolean isEdit = false;

    private Spinner middleSpinnerBoSh;
    private Spinner middleSubTypeSpinnerBoSh;
    private Spinner middleTypeSpinner;
    private ArrayAdapter<CharSequence> adapterSpinnerMiddle;
    private ArrayAdapter<CharSequence> adapterSpinnerMiddleBoSh;
    private ArrayAdapter<CharSequence> adapterSpinnerTypeMiddleBoSh;

    private LinearLayout includeMiddle;
    private String middleType = "Strategic";
    private String middleTypeMainSpinner = "All";
    private String middleSubType = "Open Position";

    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_middle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        textViewScore = view.findViewById(R.id.textView_game_score_id);
        textViewTotal = view.findViewById(R.id.textView_total_id);
        textViewFrequency = view.findViewById(R.id.textView_frequenc_id);
        textViewEfficiency = view.findViewById(R.id.textView_efficiency_id);

        textViewGood = view.findViewById(R.id.editText_handling_good_id);
        textViewEqual = view.findViewById(R.id.editText_handling_equal_id);
        textViewBad = view.findViewById(R.id.editText_handling_bad_id);
        textViewWin = view.findViewById(R.id.editText_result_win_id);
        textViewDraw = view.findViewById(R.id.editText_result_draw_id);
        textViewLost = view.findViewById(R.id.editText_result_lost_id);

        recyclerView = view.findViewById(R.id.recyclerView_middle_id);
        cardViewCreateGame = view.findViewById(R.id.cardView_create_id);
        middleTypeSpinner = view.findViewById(R.id.spinner_middle_type_view);
        includeMiddle = view.findViewById(R.id.view_middle_result_handling_include);
        includeMiddle.setVisibility(View.GONE);
        adapterSpinnerMiddle = ArrayAdapter.createFromResource(getContext(), R.array.middleView, R.layout.support_simple_spinner_dropdown_item);
        spinnerCreate(middleTypeSpinner, adapterSpinnerMiddle);
        middleTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                middleTypeMainSpinner = middleTypeSpinner.getSelectedItem().toString();
                getAllMiddleGame();
                if (position == 0) {
                    includeMiddle.setVisibility(View.GONE);
                } else {
                    includeMiddle.setVisibility(View.VISIBLE);
                    getInfoMiddleForThisType();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cardViewCreateGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                middleGameBottomSheet();
            }
        });
    }

    private void getInfoMiddleForThisType() {
        firebaseFirestore.collection("MiddleGameCalculate").whereEqualTo("middleType", middleTypeMainSpinner).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.getResult().isEmpty()) {
                    MiddleGameCalculate middleGame = task.getResult().getDocuments().get(0).toObject(MiddleGameCalculate.class);
                    textViewScore.setText(middleGame.getScore() * 100 + " %");
                    textViewTotal.setText(middleGame.getOccurance() + "");
                    textViewFrequency.setText(middleGame.getFrequency() * 100 + " %");
                    textViewEfficiency.setText(middleGame.getEfficiency() * 100 + " %");
                    textViewGood.setText(middleGame.getTotalGood() + "");
                    textViewEqual.setText(middleGame.getTotalEqual() + "");
                    textViewBad.setText(middleGame.getTotalBad() + "");
                    textViewWin.setText(middleGame.getTotalWin() + "");
                    textViewDraw.setText(middleGame.getTotalDraw() + "");
                    textViewLost.setText(middleGame.getTotalLoss() + "");
                } else {
                    textViewScore.setText("0.0 %");
                    textViewTotal.setText("0");
                    textViewFrequency.setText("0.0 %");
                    textViewEfficiency.setText("0.0 %");
                    textViewGood.setText("0");
                    textViewEqual.setText("0");
                    textViewBad.setText("0");
                    textViewWin.setText("0");
                    textViewDraw.setText("0");
                    textViewLost.setText("0");
                }
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    public void middleGameBottomSheet() {
        resetValue();
        bottomSheetDialog = new BottomSheetDialog(getContext());
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.create_handling_middle_bottom_sheet_layout, null);
        editTextGood = view.findViewById(R.id.editText_handling_good_id);
        editTextEqual = view.findViewById(R.id.editText_handling_equal_id);
        editTextBad = view.findViewById(R.id.editText_handling_bad_id);
        editTextWin = view.findViewById(R.id.editText_result_win_id);
        editTextDraw = view.findViewById(R.id.editText_result_draw_id);
        editTextLost = view.findViewById(R.id.editText_result_lost_id);
        progressBar = view.findViewById(R.id.progressBar_id);
        progressBar.setVisibility(View.GONE);
        buttonBack = view.findViewById(R.id.image_back_id);
        buttonCreate = view.findViewById(R.id.button_create_handling_id);
        middleSpinnerBoSh = view.findViewById(R.id.spinner_middle);
        adapterSpinnerMiddleBoSh = ArrayAdapter.createFromResource(getContext(), R.array.middle, R.layout.support_simple_spinner_dropdown_item);
        spinnerCreate(middleSpinnerBoSh, adapterSpinnerMiddleBoSh);
        middleSubTypeSpinnerBoSh = view.findViewById(R.id.spinner_middle_type);
        adapterSpinnerTypeMiddleBoSh = ArrayAdapter.createFromResource(getContext(), R.array.middleStrategicType, R.layout.support_simple_spinner_dropdown_item);
        spinnerCreate(middleSubTypeSpinnerBoSh, adapterSpinnerTypeMiddleBoSh);
        middleSpinnerBoSh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                middleType = middleSpinnerBoSh.getSelectedItem().toString();
                switch (position) {
                    case 0:
                        middleSubType = "Open Position";
                        middleSubTypeSpinnerBoSh.setVisibility(View.VISIBLE);
                        adapterSpinnerTypeMiddleBoSh = ArrayAdapter.createFromResource(getContext(), R.array.middleStrategicType, R.layout.support_simple_spinner_dropdown_item);
                        break;
                    case 1:
                        middleSubType = "Attacking the king";
                        middleSubTypeSpinnerBoSh.setVisibility(View.VISIBLE);
                        adapterSpinnerTypeMiddleBoSh = ArrayAdapter.createFromResource(getContext(), R.array.middleTacticalType, R.layout.support_simple_spinner_dropdown_item);
                        break;
                    case 2:
                        middleSubTypeSpinnerBoSh.setVisibility(View.GONE);
                        middleSubType = "";
                        break;
                }
                spinnerCreate(middleSubTypeSpinnerBoSh, adapterSpinnerTypeMiddleBoSh);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                middleType = "Strategic";
                middleSubType = "Open Position";
            }
        });
        middleSubTypeSpinnerBoSh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                middleSubType = middleSubTypeSpinnerBoSh.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                good = getValueEditText(editTextGood);
                equal = getValueEditText(editTextEqual);
                bad = getValueEditText(editTextBad);
                win = getValueEditText(editTextWin);
                draw = getValueEditText(editTextDraw);
                loss = getValueEditText(editTextLost);

                date = DateFormat.getDateInstance().format(Calendar.getInstance().getTime());

                total = good + equal + bad;

                userId = PreferenceUtils.getUid(getContext());
                if (isEdit) {
                    editMiddleGame();
                } else {
                    MiddleGame middleGame = new MiddleGame(userId, middleType, middleSubType, date, score
                            , win, draw, loss, totalWin, totalDraw, totalLoss, good, equal, bad, totalGood
                            , totalEqual, totalBad, total, result, totalResult, frequency, efficiency, occurance, occuranceAllMiddle);
                    addNewMiddle(middleGame);
                }
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    private void addNewMiddle(MiddleGame middleGame) {
        firebaseFirestore.collection("MiddleGame").add(middleGame).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                progressBar.setVisibility(View.GONE);
                bottomSheetDialog.dismiss();
                //sumOccuranceForThisType();
                //calculateAllResultsForAllType(middleType);
                addNewMiddleCalculate();
            }
        });
    }

    private void addNewMiddleCalculate() {
        firebaseFirestore.collection("MiddleGameCalculate").whereEqualTo("middleType", middleType).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult().isEmpty()) {
                    //Is exist
                    //create empty middle
                    MiddleGameCalculate middleGameCalculate = new MiddleGameCalculate(userId, middleType
                            , totalWin, totalDraw, totalLoss, totalGood, totalEqual, totalBad, score, frequency
                            , efficiency, occurance, occuranceAllMiddle);
                    //now update middle and fill data
                    firebaseFirestore.collection("MiddleGameCalculate").add(middleGameCalculate).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            String gameId = task.getResult().getId();
                            firebaseFirestore.collection("MiddleGameCalculate").document(gameId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    MiddleGameCalculate middleGameCalculate = task.getResult().toObject(MiddleGameCalculate.class);
                                    updateMiddleCalculate(middleGameCalculate, gameId);
                                }
                            });
                        }
                    });
                } else {
                    MiddleGameCalculate middleGameCalculate = task.getResult().getDocuments().get(0).toObject(MiddleGameCalculate.class);
                    String gameId = task.getResult().getDocuments().get(0).getId();
                    updateMiddleCalculate(middleGameCalculate, gameId);
                }

            }
        });
    }
    private void updateMiddleCalculate(MiddleGameCalculate middleGameCalculate, String gameId) {
        //update
        occurance = 0;
        occuranceAllMiddle = 0;
        totalWin = 0;
        totalDraw = 0;
        totalLoss = 0;
        totalGood = 0;
        totalEqual = 0;
        totalBad = 0;
        occurance = total + middleGameCalculate.getOccurance();
        totalGood = good + middleGameCalculate.getTotalGood();
        totalEqual = equal + middleGameCalculate.getTotalEqual();
        totalBad = bad + middleGameCalculate.getTotalBad();
        totalWin = win + middleGameCalculate.getTotalWin();
        totalDraw = draw + middleGameCalculate.getTotalDraw();
        totalLoss = loss + middleGameCalculate.getTotalLoss();
        //Sum all occ on all type to calculate frequancy
        firebaseFirestore.collection("MiddleGameCalculate").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                occuranceAllMiddle = 0;
                frequency = 0;
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    MiddleGameCalculate middleGame = queryDocumentSnapshot.toObject(MiddleGameCalculate.class);
                    occuranceAllMiddle = occuranceAllMiddle + middleGame.getOccurance();
                }
                occuranceAllMiddle += total;
                frequency = (float) occurance / (float) occuranceAllMiddle;
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //update all data for this type
                firebaseFirestore.collection("MiddleGameCalculate").document(gameId)
                        .update("occurance", occurance
                                , "occuranceAllMiddle", occuranceAllMiddle
                                , "frequency", frequency
                                , "totalGood", totalGood
                                , "totalEqual", totalEqual
                                , "totalBad", totalBad
                                , "totalWin", totalWin
                                , "totalDraw", totalDraw
                                , "totalLoss", totalLoss).addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //update freq and occ for all type
                        firebaseFirestore.collection("MiddleGameCalculate").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                    MiddleGameCalculate middleGameCalculate1 = queryDocumentSnapshot.toObject(MiddleGameCalculate.class);
                                    frequency = (float) middleGameCalculate1.getOccurance() / (float) occuranceAllMiddle;
                                    firebaseFirestore.collection("MiddleGameCalculate")
                                            .document(queryDocumentSnapshot.getId())
                                            .update("occuranceAllMiddle", occuranceAllMiddle
                                                    , "frequency", frequency);
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    //-----------------------------------------------------------------------------------------------
    private void sumOccuranceForThisType() {
        occurance = 0;
        occuranceAllMiddle = 0;
        totalWin = 0;
        totalDraw = 0;
        totalLoss = 0;
        totalGood = 0;
        totalEqual = 0;
        totalBad = 0;
        firebaseFirestore.collection("MiddleGame").whereEqualTo("middleType", middleType).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    MiddleGame middleGame = queryDocumentSnapshot.toObject(MiddleGame.class);
                    occurance = occurance + middleGame.getTotal();
                    totalGood = totalGood + middleGame.getGood();
                    totalEqual = totalEqual + middleGame.getEqual();
                    totalBad = totalBad + middleGame.getBad();
                    totalWin = totalWin + middleGame.getWin();
                    totalDraw = totalDraw + middleGame.getDraw();
                    totalLoss = totalLoss + middleGame.getLoss();
                }
                //updateOccuranceForThisType();
                //sumOccuranceAllMiddle();
            }
        });
    }

    private void sumOccuranceAllMiddle() {
        occurance = 0;
        occuranceAllMiddle = 0;
        totalWin = 0;
        totalDraw = 0;
        totalLoss = 0;
        totalGood = 0;
        totalEqual = 0;
        totalBad = 0;
        firebaseFirestore.collection("MiddleGame").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    MiddleGame middleGame = queryDocumentSnapshot.toObject(MiddleGame.class);
                    occuranceAllMiddle = occuranceAllMiddle + middleGame.getTotal();
                }
                //updateOccuranceAllMiddle();
            }
        });
    }

    private void updateOccuranceAllMiddle() {
        firebaseFirestore.collection("MiddleGame").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    String middleId = queryDocumentSnapshot.getId();
                    firebaseFirestore.collection("MiddleGame").document(middleId).update("occuranceAllMiddle", occuranceAllMiddle);
                }
            }
        });
    }

    private void updateOccuranceForThisType() {
        firebaseFirestore.collection("MiddleGame").whereEqualTo("middleType", middleType).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    String middleId = queryDocumentSnapshot.getId();
                    firebaseFirestore
                            .collection("MiddleGame")
                            .document(middleId)
                            .update("occurance", occurance
                                    , "totalGood", totalGood
                                    , "totalGood", totalGood,
                                    "totalEqual", totalEqual
                                    , "totalBad", totalBad
                                    , "totalWin", totalWin
                                    , "totalDraw", totalDraw
                                    , "totalLoss", totalLoss).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            getInfoMiddleForThisType();
                        }
                    });
                }
            }
        });
    }

    private void calculateAllResultsForAllType(String type) {
        firebaseFirestore.collection("MiddleGame").whereEqualTo(middleType, type).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    MiddleGame middleGame = queryDocumentSnapshot.toObject(MiddleGame.class);
                    occurance = occurance + middleGame.getTotal();
                }
            }
        });
        calculateAllResultsForAllSubType();
    }

    private void calculateAllResultsForAllSubType() {
    }

    private void editMiddleGame() {
    }

    Query query;

    private void getAllMiddleGame() {
        if (middleTypeMainSpinner.equals("All"))
            query = FirebaseFirestore.getInstance().collection("MiddleGame")
                    .orderBy("date", Query.Direction.DESCENDING);
        else
            query = FirebaseFirestore.getInstance().collection("MiddleGame")
                    .whereEqualTo("middleType", middleTypeMainSpinner);
        FirestoreRecyclerOptions<MiddleGame> options = new FirestoreRecyclerOptions.Builder<MiddleGame>()
                .setQuery(query, MiddleGame.class)
                .build();
        MiddleGameAdapter middleGameAdapter = new MiddleGameAdapter(options);
        middleGameAdapter.onItemSetOnClickListener(new MiddleGameAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position, PopupMenu popupMenu) {
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.popup_view_item_id:
                                break;
                            case R.id.popup_edit_item_id:
                                break;
                            case R.id.popup_delete_item_id:
                                break;
                        }
                        return true;
                    }
                });
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(middleGameAdapter);
        middleGameAdapter.startListening();
    }

    private int getValueEditText(EditText editText) {
        if (!editText.getText().toString().trim().isEmpty())
            value = Integer.parseInt(editText.getText().toString().trim());
        else
            value = 0;
        return value;
    }

    private void spinnerCreate(Spinner spinner, ArrayAdapter<CharSequence> adapterSpinner) {
        adapterSpinner.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);
    }

    private void resetValue() {
        total = 0;
        totalResult = 0;
        win = 0;
        draw = 0;
        loss = 0;
        totalWin = 0;
        totalDraw = 0;
        totalLoss = 0;
        good = 0;
        bad = 0;
        equal = 0;
        totalGood = 0;
        totalEqual = 0;
        totalBad = 0;
        score = 0;
        efficiency = 0;
        occurance = 0;
        frequency = 0;
        occurance = 0;
        occuranceAllMiddle = 0;
    }
}