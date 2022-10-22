package com.moomen.chessrays.ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
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
import com.moomen.chessrays.model.Game;
import com.moomen.chessrays.model.OpeningHandling;
import com.moomen.chessrays.model.StackGameModel;
import com.moomen.chessrays.utils.MovesIdStack;
import com.moomen.chessrays.utils.PreferenceUtils;

import java.text.DateFormat;
import java.util.Calendar;

public class ViewOpeningChallenge extends AppCompatActivity {

    public static final String OPENING_MOVE_UID = "OPENING_MOVE_UID";
    public static final String OPENING_MOVE_NAME = "OPENING_MOVE_NAME";
    public static final String OPENING_CHALLENGE_UID = "OPENING_CHALLENGE_UID";
    public static final String OPENING_CHALLENGE_NAME = "OPENING_CHALLENGE_NAME";
    public static final String OPENING_COLOR = "OPENING_COLOR";
    public static final String OPENING_FIRST_MOVE = "OPENING_FIRST_MOVE";
    public static final String OPENING_UID = "OPENING_UID";
    public static final String USER_UID = "USER_UID";

    private MovesIdStack movesIdStack;

    private TextView textViewFirstPlayer;
    private EditText editTextSecondPlayer;
    private EditText editTextGood;
    private EditText editMoveName;
    private EditText editTextEqual;
    private EditText editTextBad;
    private EditText editTextWin;
    private EditText editTextDraw;
    private EditText editTextLoss;
    private Button buttonCreate;
    private ImageView buttonBack;
    private ProgressBar progressBar;
    private ImageView imageViewAddMove;
    private TextView textViewHandlingMenu;
    private CardView cardViewCreateMenu;
    private TextView textViewMoveName;
    private String moveName;
    private String firstPlayerName;
    private String secondPlayerName;
    private String date;
    private int good = 0;
    private int equal = 0;
    private int bad = 0;
    private int win = 1;
    private float draw = 0;
    private int loss = 0;
    private int result = 1;
    private int total = 0;
    private float frequency = 0;
    private float efficiency = 0;
    private int occurance = 0;
    private int scoreOpeningGame = 0;
    private int allMovesInThisOpening = 0;
    private int sumTotalAllOpeningGame;
    private int sumTotalThisOpeningGame;
    private float frequencyThisOpeningGame;
    private float efficiencyThisOpeningGame;
    private int sumOccuranceThisOpeningGame;

    private float score = 0;
    private int totalWin = 0;
    private float totalDraw = 0;
    private int totalLoss = 0;
    private int handlingCount = 0;

    private int sumTotalAllHandling = 0;

    private String userUid;
    private String openingChallengeUid;
    private String openingMoveUid;
    private String openingMoveName;
    private String openingGameColor = "";
    private String openingGameFirstMove = "";
    private String openingGameUid;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private String openingHandlingUid;
    private BottomSheetDialog bottomSheetDialog;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private EditText editTextChallengeName;
    private Button buttonCreateChallenge;

    private int value = 0;
    private String accountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_opening_challenge);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        imageViewAddMove = findViewById(R.id.imageView_create_id);
        textViewHandlingMenu = findViewById(R.id.textView_add_new_move_id);
        cardViewCreateMenu = findViewById(R.id.cardView_create_id);
        cardViewCreateMenu.setVisibility(View.GONE);
        textViewMoveName = findViewById(R.id.textView_challenge_name_id);
        accountType = PreferenceUtils.getAccountType(getApplicationContext());
        imageViewAddMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardViewCreateMenu.getVisibility() == View.VISIBLE)
                    cardViewCreateMenu.setVisibility(View.GONE);
                else
                    cardViewCreateMenu.setVisibility(View.VISIBLE);
            }
        });

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(ViewOpeningGame.OPENING_MOVE_UID) && intent.hasExtra(ViewOpeningGame.USER_UID)
                && intent.hasExtra(ViewOpeningGame.OPENING_MOVE_NAME) && intent.hasExtra(ViewOpeningGame.OPENING_UID)) {
            openingMoveUid = intent.getStringExtra(ViewOpeningGame.OPENING_MOVE_UID);
            openingMoveName = intent.getStringExtra(ViewOpeningGame.OPENING_MOVE_NAME);
            openingGameUid = intent.getStringExtra(ViewOpeningGame.OPENING_UID);
            userUid = intent.getStringExtra(ViewOpeningGame.USER_UID);
            textViewMoveName.setText(openingMoveName);
            getAllMovesOnThisMove();
            //getAllHandlingForThisChallenge();
        }

        textViewHandlingMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //createHandlingBottomSheet();
                addNewMoveBottomSheet();
                cardViewCreateMenu.setVisibility(View.GONE);
            }
        });

    }

    private void addNewMoveBottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(ViewOpeningChallenge.this);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(ViewOpeningChallenge.this).inflate(R.layout.create_handling_bottom_sheet_layout, null);
        editMoveName = view.findViewById(R.id.editText_move_name_id);
        editTextGood = view.findViewById(R.id.editText_handling_good_id);
        editTextEqual = view.findViewById(R.id.editText_handling_equal_id);
        editTextBad = view.findViewById(R.id.editText_handling_bad_id);
        editTextWin = view.findViewById(R.id.editText_result_win_id);
        editTextDraw = view.findViewById(R.id.editText_result_draw_id);
        progressBar = view.findViewById(R.id.progressBar_id);
        progressBar.setVisibility(View.GONE);
        buttonBack = view.findViewById(R.id.image_back_id);
        buttonCreate = view.findViewById(R.id.button_create_handling_id);
        radioGroup = view.findViewById(R.id.radioGroup_id);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                moveName = editMoveName.getText().toString().trim();
                if (moveName.isEmpty()) {
                    editMoveName.setError("The Challenger name is required!");
                    editMoveName.requestFocus();
                    return;
                }

                good = getValueEditText(editTextGood);
                equal = getValueEditText(editTextEqual);
                bad = getValueEditText(editTextBad);
                radioButtonCheckOnClick(view);

                date = DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
                total = good + equal + bad;
                occurance = good + equal;
                sumTotalThisOpeningGame = 0;
                sumTotalAllOpeningGame = 0;
                frequencyThisOpeningGame = 0;
                efficiencyThisOpeningGame = 0;
                sumOccuranceThisOpeningGame = 0;

                OpeningHandling openingHandling = new OpeningHandling(userUid, "", openingGameUid, openingGameColor
                        , openingGameFirstMove, openingMoveUid, "", moveName, ""
                        , date + "", good, equal, bad, win, draw, loss, result, total, frequency, efficiency, occurance);

                addNewMove(openingHandling);
                //calculateFrequencyAndCreateHandling();
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

    private void addNewMove(OpeningHandling openingHandling) {
        firebaseFirestore.collection("OpeningMoves").add(openingHandling).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    bottomSheetDialog.dismiss();
                    Toast.makeText(ViewOpeningChallenge.this, "Created", Toast.LENGTH_SHORT).show();
                    getAllMovesOnThisMove();
                    calculateTotalFrequencyEfficiency();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ViewOpeningChallenge.this, "Failed to publish, try again!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private int getValueEditText(EditText editText) {
        if (!editText.getText().toString().trim().isEmpty())
            value = Integer.parseInt(editText.getText().toString().trim());
        else
            value = 0;
        return value;
    }

    @SuppressLint("NonConstantResourceId")
    public void radioButtonCheckOnClick(View view) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = view.findViewById(radioId);
        result = 3;
        switch (radioId) {
            case R.id.radioButton_win_id:
                win = 1;
                draw = 0;
                loss = 0;
                result = 2;
                break;
            case R.id.radioButton_draw_id:
                win = 0;
                draw = (float) 0.5;
                loss = 0;
                result = 1;
                break;
            case R.id.radioButton_loss_id:
                win = 0;
                draw = 0;
                loss = 0;
                result = 0;
                break;
        }
    }

    private void calculateTotalFrequencyEfficiency() {
        firebaseFirestore.collection("OpeningMoves").whereEqualTo("openingGameId", openingGameUid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        sumTotalThisOpeningGame = 0;
                        sumTotalAllOpeningGame = 0;
                        frequencyThisOpeningGame = 0;
                        efficiencyThisOpeningGame = 0;
                        sumOccuranceThisOpeningGame = 0;
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            OpeningHandling openingHandling = queryDocumentSnapshot.toObject(OpeningHandling.class);
                            sumTotalThisOpeningGame = sumTotalThisOpeningGame + openingHandling.getTotal();
                            sumOccuranceThisOpeningGame = sumOccuranceThisOpeningGame + openingHandling.getOccurance();
                            totalWin = totalWin + openingHandling.getWin();
                            totalDraw = totalDraw + openingHandling.getDraw();
                            totalLoss = totalLoss + openingHandling.getLoss();
                            if (openingHandling.getResult() != 3)
                                allMovesInThisOpening++;
                        }
                        score = (totalWin + totalDraw + totalLoss) / allMovesInThisOpening;
                        if ((score + "").equals("NaN"))
                            score = 0;
                        efficiencyThisOpeningGame = (float) 100 * ((float) sumOccuranceThisOpeningGame / 2) / sumTotalThisOpeningGame;
                        if ((efficiencyThisOpeningGame + "").equals("NaN"))
                            efficiencyThisOpeningGame = 0;
                        calculateFrequency();
                    }
                }
            }
        });
    }

    private void calculateFrequency() {
        firebaseFirestore.collection("OpeningMoves")
                .whereEqualTo("userId", userUid)
                .whereEqualTo("accountType", accountType)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            OpeningHandling openingHandling = queryDocumentSnapshot.toObject(OpeningHandling.class);
                            sumTotalAllOpeningGame = sumTotalAllOpeningGame + openingHandling.getTotal();
                        }
                        frequencyThisOpeningGame = (float) sumTotalThisOpeningGame / sumTotalAllOpeningGame;
                        if ((frequencyThisOpeningGame + "").equals("NaN"))
                            frequencyThisOpeningGame = 0;
                        updateDataOpeningGame();
                    }
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateDataOpeningGame() {
        firebaseFirestore.collection("Game").document(openingGameUid)
                .update("total", sumTotalThisOpeningGame
                        , "frequency", frequencyThisOpeningGame
                        , "efficiency", efficiencyThisOpeningGame
                        , "score", score);
    }

    private void getAllMovesOnThisMove() {
        Query query = FirebaseFirestore.getInstance().collection("OpeningMoves")
                .whereEqualTo("challengeId", openingMoveUid)
                .orderBy("date", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Game> options = new FirestoreRecyclerOptions.Builder<Game>()
                .setQuery(query, Game.class)
                .build();
        HandlingAdapter handlingAdapter = new HandlingAdapter(options);
        handlingAdapter.onItemSetOnClickListener(new HandlingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position, PopupMenu popupMenu) {
                openingMoveUid = documentSnapshot.getId();
                OpeningHandling openingHandling = documentSnapshot.toObject(OpeningHandling.class);
                openingMoveName = openingHandling.getFirstPlayerName();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.popup_view_item_id:
                                StackGameModel stackGameModel = new StackGameModel(openingMoveUid, openingMoveName);
                                MovesIdStack.pushStack(stackGameModel);
                                Intent intent = new Intent(getApplicationContext(), ViewOpeningChallenge.class);
                                intent.putExtra(OPENING_MOVE_UID, openingMoveUid);
                                intent.putExtra(OPENING_UID, openingGameUid);
                                intent.putExtra(USER_UID, userUid);
                                intent.putExtra(OPENING_MOVE_NAME, openingMoveName);
                                startActivity(intent);
                                break;
                            case R.id.popup_delete_item_id:
                                firebaseFirestore.collection("OpeningMoves").document(openingMoveUid).delete();
                                getAllMovesOnThisMove();
                                break;

                        }
                        return true;
                    }
                });
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerView_all_moves_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(handlingAdapter);
        recyclerView.setHasFixedSize(true);
        handlingAdapter.startListening();
    }
    @Override
    public void onBackPressed() {
        if (!MovesIdStack.moveIdStack.empty()) {
            MovesIdStack.popStack();
            StackGameModel stackGameModel = new StackGameModel();
            if (!MovesIdStack.moveIdStack.empty()) {
                stackGameModel = MovesIdStack.popStack();
                openingMoveUid = stackGameModel.getGameId();
                openingMoveName = stackGameModel.getGameName();
            }
        }
        super.onBackPressed();
    }
}