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
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.moomen.chessrays.adapter.ChallengeAdapter;
import com.moomen.chessrays.adapter.HandlingAdapter;
import com.moomen.chessrays.model.OpeningModel;
import com.moomen.chessrays.model.OpeningChallenge;
import com.moomen.chessrays.model.OpeningGame;
import com.moomen.chessrays.model.OpeningHandling;
import com.moomen.chessrays.ui.Fragment.OpeningFragment;
import com.moomen.chessrays.utils.MovesIdStack;
import com.moomen.chessrays.utils.PreferenceUtils;

import java.text.DateFormat;
import java.util.Calendar;

public class ViewOpeningGame extends AppCompatActivity {

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
    private ProgressBar progressBar;
    private ImageView buttonBack;

    private ImageView imageViewAddMove;
    private TextView textViewAddMove;
    private CardView cardViewCreateMenu;

    private TextView textViewScore;
    private TextView textViewTotal;
    private TextView textViewFrequency;
    private TextView textViewEfficiency;

    private String challengeName = "";
    private String date;

    private String openingGameColor = "";
    private String openingGameFirstMove = "";
    private String openingGameUid;
    private String openingGameId;
    private String openingGameUidDelete;
    private String userUid;
    private String gameId;
    private String openingMoveUid;
    private String openingMoveName;

    private String openingChallengeUid;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private BottomSheetDialog bottomSheetDialog;
    private FirebaseUser firebaseUser;

    private int challengeCount = 0;

    //----------------------
    private EditText editOpeningName;
    private EditText editTextGood;
    private EditText editTextEqual;
    private EditText editTextBad;
    private EditText editTextWin;
    private EditText editTextDraw;
    private EditText editTextLost;
    private Button buttonCreate;
    private TextView textViewOpeningName;

    private String openingGameName;
    private int good = 0;
    private int equal = 0;
    private int bad = 0;
    private int winEdit = 0;
    private int drawEdit = 0;
    private int lostEdit = 0;
    private int win = 0;
    private float draw = 0;
    private int loss = 0;
    private int result = 3;
    private int totalResult = 0;
    private int totalRTG = 0;
    private int totalRAG = 0;
    private int total = 0;
    private int totalFAG = 0;
    private int totalSG = 0;
    private float frequency = 0;
    private float efficiency = 0;
    private float occurance = 0;

    private float score = 0;
    private int totalWin = 0;
    private float totalDraw = 0;
    private int totalLoss = 0;
    private int allMovesInThisOpening = 0;

    private int scoreOpeningGame = 0;
    private int sumTotalAllOpeningGame = 0;
    private int sumTotalThisOpeningGame = 0;
    private float frequencyThisOpeningGame = 0;
    private float efficiencyThisOpeningGame = 0;
    private int sumOccuranceThisOpeningGame = 0;

    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private int value = 0;
    private boolean isEdit = false;

    private int sumGoodTG = 0;
    private int sumEqualTG = 0;
    private int sumBadTG = 0;
    private int sumWinTG = 0;
    private int sumDrawTG = 0;
    private int sumLostTG = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_opening_game);

        textViewColor = findViewById(R.id.textView_color_id);
        textViewFirstMove = findViewById(R.id.textView_first_move_id);
        textViewDate = findViewById(R.id.textView_date_id);

        textViewGood = findViewById(R.id.editText_handling_good_id);
        textViewEqual = findViewById(R.id.editText_handling_equal_id);
        textViewBad = findViewById(R.id.editText_handling_bad_id);
        textViewWin = findViewById(R.id.editText_result_win_id);
        textViewDraw = findViewById(R.id.editText_result_draw_id);
        textViewLost = findViewById(R.id.editText_result_lost_id);

        imageViewAddMove = findViewById(R.id.imageView_create_id);
        textViewAddMove = findViewById(R.id.textView_add_new_move_id);
        textViewOpeningName = findViewById(R.id.textView_opening_name);
        cardViewCreateMenu = findViewById(R.id.cardView_create_id);
        textViewScore = findViewById(R.id.textView_game_score_id);
        textViewTotal = findViewById(R.id.textView_total_id);
        textViewFrequency = findViewById(R.id.textView_frequenc_id);
        textViewEfficiency = findViewById(R.id.textView_efficiency_id);


        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(OpeningFragment.OPENING_GAME_UID)
                && intent.hasExtra(OpeningFragment.GAME_UID)
                && intent.hasExtra(OpeningFragment.USER_UID)
                && intent.hasExtra(OpeningFragment.OPENING_GAME_MOVE)) {
            openingGameUid = intent.getStringExtra(OpeningFragment.OPENING_GAME_UID);
            gameId = intent.getStringExtra(OpeningFragment.GAME_UID);
            userUid = intent.getStringExtra(OpeningFragment.USER_UID);
            textViewOpeningName.setText(OpeningFragment.OPENING_GAME_MOVE);
        }

        /*imageViewAddMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardViewCreateMenu.getVisibility() == View.VISIBLE)
                    cardViewCreateMenu.setVisibility(View.GONE);
                else
                    cardViewCreateMenu.setVisibility(View.VISIBLE);
            }
        });*/
        cardViewCreateMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //createChallengeBottomSheet();
                addNewOpeningBottomSheet();
            }
        });

        getOpeningGameInfo();
        getAllOpeningGameInThisGame();
        calculateFrequencyEfficiencScore(openingGameUid);
    }

    private void getOpeningGameInfo() {
        firebaseFirestore.collection("OpeningGame").document(openingGameUid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                OpeningGame openingGame = documentSnapshot.toObject(OpeningGame.class);
                assert openingGame != null;
                openingGameColor = openingGame.getColor();
                openingGameFirstMove = openingGame.getFirstMove();
                textViewColor.setText(openingGameColor);
                textViewFirstMove.setText(openingGameFirstMove);
                textViewDate.setText("88.5 %");
                //
                String score = (openingGame.getScore() * 100) + "000000";
                if (openingGame.getScore() == 0 || openingGame.getScore() == 1)
                    textViewScore.setText(openingGame.getScore() * 100 + " %");
                else
                    textViewScore.setText(score.substring(0, 4) + " %");
                ////
                textViewTotal.setText(openingGame.getTotalRAG() + "");
                //
                String frequency = (openingGame.getFrequency() * 100) + "000000";
                if (openingGame.getFrequency() == 0 || openingGame.getFrequency() == 10)
                    textViewFrequency.setText(openingGame.getFrequency() * 100 + " %");
                else
                    textViewFrequency.setText(frequency.substring(0, 4) + " %");
                //textViewFrequency.setText(frequency.substring(0, 5) + " %");
                String efficiency = (openingGame.getEfficiency() * 100) + "000000";
                if (openingGame.getEfficiency() == 0 || openingGame.getEfficiency() == 10)
                    textViewEfficiency.setText(openingGame.getEfficiency() * 100 + " %");
                else
                    textViewEfficiency.setText(efficiency.substring(0, 4) + " %");
                //textViewEfficiency.setText(efficiency.substring(0, 5) + " %");
                textViewOpeningName.setText(openingGame.getOpeningName());
                textViewGood.setText(openingGame.getSumGoodTG() + "");
                textViewEqual.setText(openingGame.getSumEqualTG() + "");
                textViewBad.setText(openingGame.getSumBadTG() + "");
                textViewWin.setText(openingGame.getSumWinTG() + "");
                textViewDraw.setText(openingGame.getSumDrawTG() + "");
                textViewLost.setText(openingGame.getSumLostTG() + "");
            }
        });
    }

    private void getAllOpeningGameInThisGame() {
        Query query = FirebaseFirestore.getInstance().collection("OpeningGame")
                .whereEqualTo("openingGameId", openingGameUid)
                .orderBy("date", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<OpeningGame> options = new FirestoreRecyclerOptions.Builder<OpeningGame>()
                .setQuery(query, OpeningGame.class)
                .build();
        HandlingAdapter handlingAdapter = new HandlingAdapter(options);
        handlingAdapter.onItemSetOnClickListener(new HandlingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position, PopupMenu popupMenu) {
                OpeningGame openingGame = documentSnapshot.toObject(OpeningGame.class);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        isEdit = false;
                        switch (item.getItemId()) {
                            case R.id.popup_view_item_id:
                                openingGameUid = documentSnapshot.getId();
                                openingGameName = openingGame.getOpeningName();
                                OpeningModel openingModel = new OpeningModel(openingGameUid, openingGameName);
                                MovesIdStack.pushStack(openingModel);
                                calculateFrequencyEfficiencScore(openingGameUid);
                                getAllOpeningGameInThisGame();
                                getOpeningGameInfo();
                                break;
                            case R.id.popup_edit_item_id:
                                openingGameId = documentSnapshot.getId();
                                isEdit = true;
                                editOpeningGame(openingGameId);
                                break;
                            case R.id.popup_delete_item_id:
                                openingGameUidDelete = documentSnapshot.getId();
                                //firebaseFirestore.collection("OpeningGame").document(openingGameUid).delete();
                                deleteOpening(openingGameUidDelete);
                                //MovesIdStack.popStack();
                                OpeningModel openingModel1 = MovesIdStack.peekStack();
                                openingGameName = openingModel1.getOpeningName();
                                calculateFrequencyEfficiencScore(openingGameUid);
                                getAllOpeningGameInThisGame();
                                getOpeningGameInfo();
                                break;
                        }
                        return true;
                    }
                });
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerView_all_challenge_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(handlingAdapter);
        handlingAdapter.startListening();
    }

    private void editOpeningGame(String openingGameId) {
        firebaseFirestore.collection("OpeningGame").document(openingGameId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                OpeningGame openingGame = documentSnapshot.toObject(OpeningGame.class);
                addNewOpeningBottomSheet();
                buttonCreate.setText("Save");
                editOpeningName.setText(openingGame.getOpeningName());
                editTextGood.setText(openingGame.getGood() + "");
                editTextEqual.setText(openingGame.getEqual() + "");
                editTextBad.setText(openingGame.getBad() + "");
                editTextWin.setText(openingGame.getTotalWin() + "");
                editTextDraw.setText(openingGame.getTotalDraw() + "");
                editTextLost.setText(openingGame.getTotalLoss() + "");
            }
        });
    }

    private void deleteOpening(String openingGameUidDelete) {
        firebaseFirestore.collection("OpeningGame").whereEqualTo("openingGameId", openingGameUidDelete).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                firebaseFirestore.collection("OpeningGame").document(openingGameUidDelete).delete();
                //If last item refresh recycle and game info
                if (task.getResult().isEmpty()) {
                    getOpeningGameInfo();
                    getAllOpeningGameInThisGame();
                    calculateFrequencyEfficiencScore(openingGameUid);
                }
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    deleteOpening(queryDocumentSnapshot.getId());
                }
            }
        });
    }

    private void addNewOpeningBottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(ViewOpeningGame.this);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(ViewOpeningGame.this).inflate(R.layout.create_handling_bottom_sheet_layout, null);
        editOpeningName = view.findViewById(R.id.editText_move_name_id);
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
        radioGroup = view.findViewById(R.id.radioGroup_id);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                openingGameName = editOpeningName.getText().toString().trim();
                if (openingGameName.isEmpty()) {
                    editOpeningName.setError("The Challenger name is required!");
                    editOpeningName.requestFocus();
                    return;
                }

                good = getValueEditText(editTextGood);
                equal = getValueEditText(editTextEqual);
                bad = getValueEditText(editTextBad);
                winEdit = getValueEditText(editTextWin);
                drawEdit = getValueEditText(editTextDraw);
                lostEdit = getValueEditText(editTextLost);
                radioButtonCheckOnClick(view);

                date = DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
                sumTotalThisOpeningGame = 0;
                sumTotalAllOpeningGame = 0;
                frequencyThisOpeningGame = 0;
                efficiencyThisOpeningGame = 0;
                sumOccuranceThisOpeningGame = 0;
                //
                total = good + equal + bad;
                occurance = good + ((float) equal / 2);
                efficiency = (occurance / (float) total);
                //
                totalResult = winEdit + drawEdit + lostEdit;
                if (totalResult != 0) {
                    score = (float) ((winEdit) + (drawEdit * 0.5)) / (float) totalResult;
                } else
                    score = 0;

                userUid = PreferenceUtils.getUid(getApplicationContext());
                if (!isEdit) {
                    OpeningGame openingGame = new OpeningGame("", userUid, gameId, openingGameUid
                            , openingGameName, openingGameColor, openingGameFirstMove, date + ""
                            , score, winEdit, drawEdit, lostEdit, good, equal, bad, win, draw, loss, total
                            , sumTotalThisOpeningGame, sumTotalAllOpeningGame, result, totalResult, totalRTG, totalRAG
                            , frequency, efficiency, occurance, sumGoodTG, sumEqualTG, sumBadTG, sumWinTG, sumDrawTG, sumLostTG);
                    addNewOpening(openingGame);
                } else
                    editDateOpeningGame();
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


    private void addNewOpening(OpeningGame openingGame) {
        firebaseFirestore.collection("OpeningGame").add(openingGame).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    bottomSheetDialog.dismiss();
                    Toast.makeText(ViewOpeningGame.this, "Created", Toast.LENGTH_SHORT).show();
                    getOpeningGameInfo();
                    getAllOpeningGameInThisGame();
                    calculateFrequencyEfficiencScore(openingGameUid);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ViewOpeningGame.this, "Failed to publish, try again!", Toast.LENGTH_SHORT).show();
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

    private void calculateFrequencyEfficiencScore(String openingGameUid) {
        //Calculate total all game
        String gameId = MovesIdStack.getMoveIdModlesStack().get(0).getOpeningId();
        firebaseFirestore.collection("OpeningGame").whereEqualTo("gameId", gameId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                resetValues();
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    OpeningGame openingGame = queryDocumentSnapshot.toObject(OpeningGame.class);
                    sumTotalAllOpeningGame = sumTotalAllOpeningGame + openingGame.getTotal();
                }
                //get data for this game and calculate total all game in it.
                firebaseFirestore.collection("OpeningGame").document(openingGameUid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        OpeningGame openingGame = documentSnapshot.toObject(OpeningGame.class);
                        assert openingGame != null;
                        sumTotalThisOpeningGame = sumTotalThisOpeningGame + openingGame.getTotal();
                        totalRTG = openingGame.getTotalResult();
                        totalWin = openingGame.getTotalWin();
                        totalDraw = openingGame.getTotalDraw();
                        //
                        sumGoodTG = openingGame.getGood();
                        sumEqualTG = openingGame.getEqual();
                        sumBadTG = openingGame.getBad();
                        //
                        sumWinTG = openingGame.getTotalWin();
                        sumDrawTG = openingGame.getTotalDraw();
                        sumLostTG = openingGame.getTotalLoss();
                        //
                        good = openingGame.getGood();
                        equal = openingGame.getEqual();
                        bad = openingGame.getBad();
                        //
                        winEdit = openingGame.getTotalWin();
                        drawEdit = openingGame.getTotalDraw();
                        lostEdit = openingGame.getTotalLoss();
                        CalculateTotalInThisGame(openingGameUid);
                    }
                });
            }
        });
    }

    private void CalculateTotalInThisGame(String openingGameId) {
        firebaseFirestore.collection("OpeningGame").whereEqualTo("openingGameId", openingGameId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            OpeningGame openingGame = queryDocumentSnapshot.toObject(OpeningGame.class);
                            sumTotalThisOpeningGame = sumTotalThisOpeningGame + openingGame.getTotal();
                            totalRTG = totalRTG + openingGame.getTotalResult();
                            totalWin = totalWin + openingGame.getTotalWin();
                            totalDraw = totalDraw + openingGame.getTotalDraw();
                            //
                            sumGoodTG += openingGame.getGood();
                            sumEqualTG += openingGame.getEqual();
                            sumBadTG += openingGame.getBad();
                            //
                            sumWinTG += openingGame.getTotalWin();
                            sumDrawTG += openingGame.getTotalDraw();
                            sumLostTG += openingGame.getTotalLoss();
                            CalculateTotalInThisGame(queryDocumentSnapshot.getId());
                        }
                    } else //Calculate all results (frequancy, efficant, result) for this Opening
                        calculateAllResults();
                }
            }
        });
    }

    private void calculateAllResults() {
        //
        if (sumTotalAllOpeningGame != 0 && sumTotalThisOpeningGame != 0)
            frequencyThisOpeningGame = (float) sumTotalThisOpeningGame / (float) sumTotalAllOpeningGame;
        //
        if (totalWin != 0 && totalRTG != 0)
            score = ((float) (totalWin + totalDraw * 0.5)) / (float) totalRTG;
        //
        totalRAG = sumWinTG + sumDrawTG + sumLostTG;
        //
        float sumResults = sumGoodTG + sumEqualTG + sumBadTG;
        float occorance = sumGoodTG + (float) sumEqualTG / 2;
        if (occorance != 0 && sumResults != 0)
            efficiency = occorance / (sumResults);
        //
        updateDataOpeningGame();
    }

    @SuppressLint("SetTextI18n")
    private void updateDataOpeningGame() {
        firebaseFirestore.collection("OpeningGame").document(openingGameUid)
                .update("totalTG", sumTotalThisOpeningGame
                        , "totalFAG", sumTotalAllOpeningGame
                        , "frequency", frequencyThisOpeningGame
                        , "score", score
                        , "efficiency", efficiency
                        , "totalRAG", totalRAG
                        , "sumGoodTG", sumGoodTG
                        , "sumEqualTG", sumEqualTG
                        , "sumBadTG", sumBadTG
                        , "sumWinTG", sumWinTG
                        , "sumDrawTG", sumDrawTG
                        , "sumLostTG", sumLostTG).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                getOpeningGameInfo();
            }
        });
    }

    private void editDateOpeningGame() {
        firebaseFirestore.collection("OpeningGame").document(openingGameId)
                .update("openingName", openingGameName
                        , "good", good
                        , "equal", equal
                        , "bad", bad
                        , "totalWin", winEdit
                        , "totalDraw", drawEdit
                        , "totalLoss", lostEdit
                        , "totalResult", totalResult
                        , "total", total).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                bottomSheetDialog.dismiss();
                Toast.makeText(ViewOpeningGame.this, "Update", Toast.LENGTH_SHORT).show();
                calculateFrequencyEfficiencScore(openingGameUid);
                getAllOpeningGameInThisGame();
            }
        });
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


    @Override
    protected void onPause() {
        super.onPause();
        //getOpeningGameInfo();
    }


    @Override
    protected void onResume() {
        super.onResume();
       /* getOpeningGameInfo();
        getAllOpeningGameInThisGame();
        calculateFrequencyEfficiencScore();*/
    }

    @Override
    public void onBackPressed() {
        if (!MovesIdStack.moveIdModlesStack.empty()) {
            MovesIdStack.popStack();
            OpeningModel openingModel = new OpeningModel();
            if (!MovesIdStack.moveIdModlesStack.empty()) {
                openingModel = MovesIdStack.peekStack();
                openingGameUid = openingModel.getOpeningId();
                openingGameName = openingModel.getOpeningName();
                getOpeningGameInfo();
                getAllOpeningGameInThisGame();
                calculateFrequencyEfficiencScore(openingGameUid);
            } else
                super.onBackPressed();
        }
    }

    private void resetValues() {
        sumTotalAllOpeningGame = 0;
        sumTotalThisOpeningGame = 0;
        frequencyThisOpeningGame = 0;
        total = 0;
        totalRTG = 0;
        totalRAG = 0;
        totalResult = 0;
        totalWin = 0;
        totalDraw = 0;
        score = 0;
        efficiency = 0;
        occurance = 0;
        efficiencyThisOpeningGame = 0;
        good = 0;
        bad = 0;
        equal = 0;
        sumGoodTG = 0;
        sumEqualTG = 0;
        sumBadTG = 0;
        sumWinTG = 0;
        sumDrawTG = 0;
        sumLostTG = 0;
    }
}