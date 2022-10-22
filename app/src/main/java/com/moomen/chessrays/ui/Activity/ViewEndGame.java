package com.moomen.chessrays.ui.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agrawalsuneet.dotsloader.loaders.LazyLoader;
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
import com.moomen.chessrays.adapter.HandlingAdapter;
import com.moomen.chessrays.model.Game;
import com.moomen.chessrays.model.StackGameModel;
import com.moomen.chessrays.ui.Fragment.EndFragment;
import com.moomen.chessrays.utils.MovesIdStack;
import com.moomen.chessrays.utils.PreferenceUtils;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.Calendar;

public class ViewEndGame extends AppCompatActivity {
    private TextView textViewOpposition;
    private TextView textViewGood;
    private TextView textViewGoodPercent;
    private TextView textViewEqual;
    private TextView textViewEqualPercent;
    private TextView textViewBad;
    private TextView textViewBadPercent;
    private TextView textViewWin;
    private TextView textViewWinPercent;
    private TextView textViewDraw;
    private TextView textViewDrawPercent;
    private TextView textViewLost;
    private TextView textViewLostPercent;
    private ProgressBar progressBar;
    private ImageView buttonBack;
    private RecyclerView recyclerView;

    private ImageView imageViewAddMove;
    private TextView textViewAddMove;
    private CardView cardViewCreateMenu;

    private TextView textViewScore;
    private TextView textViewTotal;
    private TextView textViewFrequency;
    private TextView textViewEfficiency;

    private String challengeName = "";
    private String date;

    private String endGameColor = "";
    private String endGameFirstMove = "";
    private String endGameUid;
    private String endGameId;
    private String endGameUidDelete;
    private String userUid;
    private String gameId;
    private String endMoveUid;
    private String endMoveName;

    private String endChallengeUid;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private BottomSheetDialog bottomSheetDialog;
    private FirebaseUser firebaseUser;

    private int challengeCount = 0;

    //----------------------
    private EditText editEndName;
    private EditText editTextGood;
    private EditText editTextEqual;
    private EditText editTextBad;
    private EditText editTextWin;
    private EditText editTextDraw;
    private EditText editTextLost;
    private Button buttonCreate;
    private TextView textViewEndName;
    //-----------------------
    private EditText editAddOpponent;
    private ConstraintLayout constraintLayoutAddOpponent;

    private String endGameName;
    private int good = 0;
    private float goodPercent = 0;
    private int equal = 0;
    private float equalPercent = 0;
    private int bad = 0;
    private float badPercent = 0;
    private int win = 0;
    private float winPercent = 0;
    private int draw = 0;
    private float drawPercent = 0;
    private int loss = 0;
    private float lossPercent = 0;
    private int winEdit = 0;
    private int drawEdit = 0;
    private int lostEdit = 0;
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
    private int allMovesInThisEnd = 0;

    private int scoreEndGame = 0;
    private int sumTotalAllEndGame = 0;
    private int sumTotalThisEndGame = 0;
    private float frequencyThisEndGame = 0;
    private float efficiencyThisEndGame = 0;
    private int sumOccuranceThisEndGame = 0;

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
    private ConstraintLayout constraintLayoutNoDataRecycle;
    private ConstraintLayout constraintLayoutLazyLoaderRecycle;
    private ConstraintLayout constraintLayoutLazyLoader;
    private ConstraintLayout constraintLayoutLazyLoader2;
    private ConstraintLayout constraintLayoutLazyLoader3;
    private LazyLoader lazyLoaderRecycle;
    private LazyLoader lazyLoader;
    private LazyLoader lazyLoader2;
    private LazyLoader lazyLoader3;

    private String accountType = "";
    private ImageView clearOpposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_end_game);

        textViewOpposition = findViewById(R.id.textView_opposition_id);
        recyclerView = findViewById(R.id.recyclerView_all_challenge_id);
        textViewGood = findViewById(R.id.editText_handling_good_id);
        textViewGoodPercent = findViewById(R.id.textView_good_percent_id);
        textViewEqual = findViewById(R.id.editText_handling_equal_id);
        textViewEqualPercent = findViewById(R.id.textView_equal_percent_id);
        textViewBad = findViewById(R.id.editText_handling_bad_id);
        textViewBadPercent = findViewById(R.id.textView_bad_percent_id);
        textViewWin = findViewById(R.id.editText_result_win_id);
        textViewWinPercent = findViewById(R.id.textView_win_percent_id);
        textViewDraw = findViewById(R.id.editText_result_draw_id);
        textViewDrawPercent = findViewById(R.id.textView_draw_percent_id);
        textViewLost = findViewById(R.id.editText_result_lost_id);
        textViewLostPercent = findViewById(R.id.textView_lost_percent_id);
        clearOpposition = findViewById(R.id.imageView_clear_opposition);
        imageViewAddMove = findViewById(R.id.imageView_create_id);
        textViewAddMove = findViewById(R.id.textView_add_new_move_id);
        textViewEndName = findViewById(R.id.textView_game_name);
        cardViewCreateMenu = findViewById(R.id.cardView_create_id);
        textViewScore = findViewById(R.id.textView_game_score_id);
        textViewTotal = findViewById(R.id.textView_total_id);
        textViewFrequency = findViewById(R.id.textView_frequenc_id);
        textViewEfficiency = findViewById(R.id.textView_efficiency_id);
        constraintLayoutAddOpponent = findViewById(R.id.constraintLayout_add_opponent);

        constraintLayoutNoDataRecycle = findViewById(R.id.constraintLayout_no_data_id);
        constraintLayoutNoDataRecycle.setVisibility(View.GONE);
        lazyLoaderRecycle = findViewById(R.id.lazyLoader_recycle_id);
        constraintLayoutLazyLoaderRecycle = findViewById(R.id.constraintLayout_lazyLoader_recycle_id);
        lazyLoader = findViewById(R.id.lazyLoader_id);
        constraintLayoutLazyLoader = findViewById(R.id.constraintLayout_lazyLoader_id);
        lazyLoader2 = findViewById(R.id.lazyLoader2_id);
        constraintLayoutLazyLoader2 = findViewById(R.id.constraintLayout_lazyLoader2_id);
        lazyLoader3 = findViewById(R.id.lazyLoader3_id);
        constraintLayoutLazyLoader3 = findViewById(R.id.constraintLayout_lazyLoader3_id);

        accountType = PreferenceUtils.getAccountType(getApplicationContext());

        preparProgress(lazyLoaderRecycle);
        preparProgress(lazyLoader);
        preparProgress(lazyLoader2);
        preparProgress(lazyLoader3);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(EndFragment.END_GAME_UID)
                && intent.hasExtra(EndFragment.GAME_UID)
                && intent.hasExtra(EndFragment.USER_UID)
                && intent.hasExtra(EndFragment.END_GAME_NAME)) {
            endGameUid = intent.getStringExtra(EndFragment.END_GAME_UID);
            gameId = intent.getStringExtra(EndFragment.GAME_UID);
            userUid = intent.getStringExtra(EndFragment.USER_UID);
            textViewEndName.setText(EndFragment.END_GAME_NAME);
        }

        constraintLayoutAddOpponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOpponentForSelectedGame(endGameUid);
            }
        });

        cardViewCreateMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //createChallengeBottomSheet();
                addNewEndBottomSheet();
            }
        });

        //getEndGameInfo();
        clearOpposition();
        getAllEndGameInThisGame();
        calculateFrequencyEfficiencScore(endGameUid);
    }
    private void clearOpposition(){
        clearOpposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ViewEndGame.this);
                dialogBuilder.setTitle("Reset opposition")
                        .setMessage("Are you sure you want to reset all opposition?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                firebaseFirestore.collection("EndGames")
                                        .document(endGameUid)
                                        .update("countOpponent", 0
                                                , "opposition", 0
                                                , "totalOpponent", 0
                                                , "totalOpposition", 0).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        getEndGameInfo();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    private void preparProgress(LazyLoader lazyLoader){
        LazyLoader loader = new LazyLoader(this, 30, 20, ContextCompat.getColor(this, R.color.loader_selected),
                ContextCompat.getColor(this, R.color.loader_selected),
                ContextCompat.getColor(this, R.color.loader_selected));
        loader.setAnimDuration(500);
        loader.setFirstDelayDuration(100);
        loader.setSecondDelayDuration(200);
        loader.setInterpolator(new LinearInterpolator());
        lazyLoader.addView(loader);
    }

    private void checkIfHaveData() {
        constraintLayoutLazyLoaderRecycle.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                firebaseFirestore.collection("EndGames")
                        .whereEqualTo("thisGameId", endGameUid)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        constraintLayoutLazyLoaderRecycle.setVisibility(View.GONE);
                        if (task.getResult().isEmpty()) {
                            constraintLayoutNoDataRecycle.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                        else {
                            constraintLayoutNoDataRecycle.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        }, 2000);
    }

    private boolean isView = false;

    private void progressVisibility(boolean visibility){
        if (visibility) {
            constraintLayoutLazyLoader.setVisibility(View.VISIBLE);
            constraintLayoutLazyLoader2.setVisibility(View.VISIBLE);
            constraintLayoutLazyLoader3.setVisibility(View.VISIBLE);
        }else{
            constraintLayoutLazyLoader.setVisibility(View.GONE);
            constraintLayoutLazyLoader2.setVisibility(View.GONE);
            constraintLayoutLazyLoader3.setVisibility(View.GONE);
        }
    }

    private void getEndGameInfo() {
        progressVisibility(true);
        firebaseFirestore.collection("EndGames").document(endGameUid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Game endGame = documentSnapshot.toObject(Game.class);
                assert endGame != null;
                String score = (endGame.getScore() * 100) + "000000";
                if (endGame.getScore() == 0 || endGame.getScore() == 1)
                    textViewScore.setText(endGame.getScore() * 100 + " %");
                else
                    textViewScore.setText(score.substring(0, 4) + " %");
                //
                String frequency = (endGame.getFrequency() * 100) + "000000";
                if (endGame.getFrequency() == 0 || endGame.getFrequency() == 10)
                    textViewFrequency.setText(endGame.getFrequency() * 100 + " %");
                else
                    textViewFrequency.setText(frequency.substring(0, 4) + " %");
                //
                String efficiency = (endGame.getEfficiency() * 100) + "000000";
                if (endGame.getEfficiency() == 0 || endGame.getEfficiency() == 10)
                    textViewEfficiency.setText(endGame.getEfficiency() * 100 + " %");
                else
                    textViewEfficiency.setText(efficiency.substring(0, 4) + " %");
                //--------------
                String goodPercent = (endGame.getGoodPercent() * 100) + "000000";
                if (endGame.getGoodPercent() == 0 || endGame.getGoodPercent() == 10)
                    textViewGoodPercent.setText(endGame.getGoodPercent() * 100 + " %");
                else
                    textViewGoodPercent.setText(goodPercent.substring(0, 4) + " %");

                String equalPercent = (endGame.getEqualPercent() * 100) + "000000";
                if (endGame.getEqualPercent() == 0 || endGame.getEqualPercent() == 10)
                    textViewEqualPercent.setText(endGame.getEqualPercent() * 100 + " %");
                else
                    textViewEqualPercent.setText(equalPercent.substring(0, 4) + " %");
                //
                String badPercent = (endGame.getBadPercent() * 100) + "000000";
                if (endGame.getBadPercent() == 0 || endGame.getBadPercent() == 10)
                    textViewBadPercent.setText(endGame.getBadPercent() * 100 + " %");
                else
                    textViewBadPercent.setText(badPercent.substring(0, 4) + " %");
                //
                String winPercent = (endGame.getWinPercent() * 100) + "000000";
                if (endGame.getWinPercent() == 0 || endGame.getWinPercent() == 10)
                    textViewWinPercent.setText(endGame.getWinPercent() * 100 + " %");
                else
                    textViewWinPercent.setText(winPercent.substring(0, 4) + " %");
                ////
                String drawPercent = (endGame.getDrawPercent() * 100) + "000000";
                if (endGame.getDrawPercent() == 0 || endGame.getDrawPercent() == 10)
                    textViewDrawPercent.setText(endGame.getDrawPercent() * 100 + " %");
                else
                    textViewDrawPercent.setText(drawPercent.substring(0, 4) + " %");
                ////
                String lossPercent = (endGame.getLossPercent() * 100) + "000000";
                if (endGame.getGoodPercent() == 0 || endGame.getLossPercent() == 10)
                    textViewLostPercent.setText(endGame.getLossPercent() * 100 + " %");
                else
                    textViewLostPercent.setText(lossPercent.substring(0, 4) + " %");
                //----------
                textViewEndName.setText(endGame.getGameName());
                textViewGood.setText(endGame.getSumGoodTG() + "");
                textViewEqual.setText(endGame.getSumEqualTG() + "");
                textViewBad.setText(endGame.getSumBadTG() + "");
                textViewWin.setText(endGame.getSumWinTG() + "");
                textViewDraw.setText(endGame.getSumDrawTG() + "");
                textViewLost.setText(endGame.getSumLostTG() + "");
                textViewTotal.setText(endGame.getTotalRAG() + "");
                int opposition = (int) endGame.getTotalOpposition();
                textViewOpposition.setText(opposition + "");
                progressVisibility(false);
            }
        });
    }

    private void getAllEndGameInThisGame() {
        Query query = FirebaseFirestore.getInstance().collection("EndGames")
                .whereEqualTo("thisGameId", endGameUid)
                .orderBy("sortNum", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Game> options = new FirestoreRecyclerOptions.Builder<Game>()
                .setQuery(query, Game.class)
                .build();
        HandlingAdapter handlingAdapter = new HandlingAdapter(options);
        handlingAdapter.onItemSetOnClickListener(new HandlingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position, PopupMenu popupMenu) {
                Game endGame = documentSnapshot.toObject(Game.class);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        isEdit = false;
                        isView = false;
                        switch (item.getItemId()) {
                            case R.id.popup_view_item_id:
                                endGameUid = documentSnapshot.getId();
                                endGameName = endGame.getGameName();
                                StackGameModel stackGameModel = new StackGameModel(endGameUid, endGameName);
                                MovesIdStack.pushStack(stackGameModel);
                                calculateFrequencyEfficiencScore(endGameUid);
                                getAllEndGameInThisGame();
                                //getEndGameInfo();
                                isView = true;
                                currentOpponent = 0;
                                updateOpponent(endGameUid);
                                break;
                            case R.id.popup_edit_item_id:
                                endGameId = documentSnapshot.getId();
                                isEdit = true;
                                editEndGame(endGameId);
                                break;
                            /*case R.id.popup_add_opponent_item_id:
                                endGameId = documentSnapshot.getId();
                                addOpponentForSelectedGame(endGameId);
                                break;*/
                            case R.id.popup_delete_item_id:
                                endGameUidDelete = documentSnapshot.getId();
                                //firebaseFirestore.collection("EndGames").document(endGameUid).delete();
                                deleteEnd(endGameUidDelete);
                                //MovesIdStack.popStack();
                                StackGameModel endModel1 = MovesIdStack.peekStack();
                                endGameName = endModel1.getGameName();
                                calculateFrequencyEfficiencScore(endGameUid);
                                getAllEndGameInThisGame();
                                getEndGameInfo();
                                break;
                        }
                        return true;
                    }
                });
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(handlingAdapter);
        handlingAdapter.startListening();
        checkIfHaveData();
    }

    private int countOpponent = 0;
    private int totalOpponent = 0;
    private int opposition = 0;
    private int totalOpposition = 0;
    private int sumOpponent = 0;
    private int sumTotalOpponent = 0;
    private int currentOpponent = 0;

    private void addOpponentForSelectedGame(String endGameId) {
        countOpponent = 0;
        opposition = 0;
        sumOpponent = 0;
        currentOpponent = 0;
        totalOpponent = 0;
        totalOpposition = 0;
        sumTotalOpponent = 0;
        //Get number opponent for this game and when complete add new the opponent
        bottomSheetDialog = new BottomSheetDialog(ViewEndGame.this);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(ViewEndGame.this).inflate(R.layout.add_opponents_bottom_sheet, null);
        editAddOpponent = view.findViewById(R.id.editText_opponent_rate_id);
        progressBar = view.findViewById(R.id.progressBar_id);
        progressBar.setVisibility(View.GONE);
        buttonCreate = view.findViewById(R.id.button_add_opponent);
        buttonBack = view.findViewById(R.id.image_back_id);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String opponentEdit = editAddOpponent.getText().toString().trim();
                if (!opponentEdit.isEmpty())
                    currentOpponent = Integer.parseInt(editAddOpponent.getText().toString().trim());
                isView = false;
                updateOpponent(endGameId);
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

    private void updateOpponent(String endGameId) {
        countOpponent = 0;
        opposition = 0;
        sumOpponent = 0;
        totalOpponent = 0;
        totalOpposition = 0;
        sumTotalOpponent = 0;
        firebaseFirestore.collection("EndGames").document(endGameId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Game endGame = task.getResult().toObject(Game.class);
                countOpponent = endGame.getCountOpponent();
                opposition = endGame.getOpposition();
                sumOpponent = countOpponent * opposition;
                //Update data when add
                if (currentOpponent > 0) {
                    sumOpponent = (countOpponent * opposition) + currentOpponent;
                    countOpponent = countOpponent + 1;
                    opposition = (int) (sumOpponent / countOpponent);
                }
                //Now get the opposition and number opponent for all game in this game
                firebaseFirestore.collection("EndGames").whereEqualTo("gameId", endGameId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            Game endGame = queryDocumentSnapshot.toObject(Game.class);
                            totalOpponent = totalOpponent + endGame.getCountOpponent();
                            sumTotalOpponent = sumTotalOpponent + (endGame.getCountOpponent() * endGame.getOpposition());
                        }
                        totalOpponent = totalOpponent + countOpponent;
                        sumTotalOpponent = sumTotalOpponent + sumOpponent;

                        if (totalOpponent > 0)
                            totalOpposition = sumTotalOpponent / totalOpponent;
                        //Now update opponent and opposition
                        //If add new
                        firebaseFirestore.collection("EndGames").document(endGameId)
                                .update("countOpponent", countOpponent
                                        , "opposition", opposition
                                        , "totalOpponent", totalOpponent
                                        , "totalOpposition", totalOpposition).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!isView) {
                                    bottomSheetDialog.dismiss();
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(ViewEndGame.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                                }
                                getEndGameInfo();
                            }
                        });
                    }
                });
            }
        });
    }

    private void editEndGame(String endGameId) {
        firebaseFirestore.collection("EndGames").document(endGameId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Game endGame = documentSnapshot.toObject(Game.class);
                addNewEndBottomSheet();
                buttonCreate.setText("Save");
                editEndName.setText(endGame.getGameName());
                editTextGood.setText(endGame.getGood() + "");
                editTextEqual.setText(endGame.getEqual() + "");
                editTextBad.setText(endGame.getBad() + "");
                editTextWin.setText(endGame.getTotalWin() + "");
                editTextDraw.setText(endGame.getTotalDraw() + "");
                editTextLost.setText(endGame.getTotalLoss() + "");
            }
        });
    }

    private void deleteEnd(String endGameUidDelete) {
        firebaseFirestore.collection("EndGames").whereEqualTo("thisGameId", endGameUidDelete).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                firebaseFirestore.collection("EndGames").document(endGameUidDelete).delete();
                //If last item refresh recycle and game info
                if (task.getResult().isEmpty()) {
                    getEndGameInfo();
                    getAllEndGameInThisGame();
                    calculateFrequencyEfficiencScore(endGameUid);
                }
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    deleteEnd(queryDocumentSnapshot.getId());
                }
            }
        });
    }

    private void addNewEndBottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(ViewEndGame.this);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(ViewEndGame.this).inflate(R.layout.create_handling_bottom_sheet_layout, null);
        editEndName = view.findViewById(R.id.editText_move_name_id);
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
                endGameName = editEndName.getText().toString().trim();
                if (endGameName.isEmpty()) {
                    editEndName.setError("The Challenger name is required!");
                    editEndName.requestFocus();
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
                sumTotalThisEndGame = 0;
                sumTotalAllEndGame = 0;
                frequencyThisEndGame = 0;
                efficiencyThisEndGame = 0;
                sumOccuranceThisEndGame = 0;
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
                    Game endGame = new Game(accountType,"", userUid, gameId, endGameUid
                            , endGameName, endGameColor, endGameFirstMove, date + ""
                            , score, winEdit, drawEdit, lostEdit, good, goodPercent, equal, equalPercent
                            , bad, badPercent, win, winPercent, draw, drawPercent, loss, lossPercent, totalResult
                            , totalResult, sumTotalAllEndGame, result, totalResult, totalRTG, totalRAG
                            , frequency, efficiency, occurance, sumGoodTG, sumEqualTG, sumBadTG, sumWinTG, sumDrawTG, sumLostTG, 0, 0, 0, 0, 20);
                    addNewEnd(endGame);
                } else
                    editDateEndGame();
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


    private void addNewEnd(Game endGame) {
        firebaseFirestore.collection("EndGames").add(endGame).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    bottomSheetDialog.dismiss();
                    Toast.makeText(ViewEndGame.this, "Created", Toast.LENGTH_SHORT).show();
                    getEndGameInfo();
                    getAllEndGameInThisGame();
                    calculateFrequencyEfficiencScore(endGameUid);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ViewEndGame.this, "Failed to publish, try again!", Toast.LENGTH_SHORT).show();
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

    private void calculateFrequencyEfficiencScore(String endGameUid) {
        //Calculate total all game
        String gameId = MovesIdStack.getMoveIdStack().get(0).getGameId();
        if (isMainGame()) {
            calculateMainEndGame(endGameUid);
        } else {
            calculateSubGame(gameId, endGameUid);
        }
    }

    private void calculateMainEndGame(String endGameUid) {
        firebaseFirestore.collection("EndGames")
                .whereEqualTo("userId", userUid)
                .whereEqualTo("accountType", accountType)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                resetValues();
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    Game game = queryDocumentSnapshot.toObject(Game.class);
                    String g = queryDocumentSnapshot.getId();
                    if (!g.equals(endGameUid))
                        sumTotalAllEndGame = sumTotalAllEndGame + game.getTotalRAG();
                }
                //2- Get data for main opened game and calculate total all game in it.
                firebaseFirestore.collection("EndGames").document(endGameUid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Game game = documentSnapshot.toObject(Game.class);
                        assert game != null;
                        filData(game);
                        filInitialDataCurrentGame(game);
                        CalculateTotalInThisGame(endGameUid);
                    }
                });
            }
        });
    }

    private void calculateSubGame(String gameId, String endGameUid) {
        firebaseFirestore.collection("EndGames").whereEqualTo("gameId", gameId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                resetValues();
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    Game endGame = queryDocumentSnapshot.toObject(Game.class);
                    sumTotalAllEndGame = sumTotalAllEndGame + endGame.getTotal();
                }
                //get data for this game and calculate total all game in it.
                firebaseFirestore.collection("EndGames").document(endGameUid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Game endGame = documentSnapshot.toObject(Game.class);
                        assert endGame != null;
                        filData(endGame);
                        filInitialDataCurrentGame(endGame);
                        CalculateTotalInThisGame(endGameUid);
                    }
                });
            }
        });
    }

    private void CalculateTotalInThisGame(String endGameId) {
        firebaseFirestore.collection("EndGames").whereEqualTo("thisGameId", endGameId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            Game endGame = queryDocumentSnapshot.toObject(Game.class);
                            filData(endGame);
                            CalculateTotalInThisGame(queryDocumentSnapshot.getId());
                        }
                    } else //Calculate all results (frequancy, efficant, result) for this End
                        calculateAllResults();
                }
            }
        });
    }

    private void calculateAllResults() {
        if (isMainGame()) {
            if ((sumTotalThisEndGame + sumTotalAllEndGame) != 0)
                frequencyThisEndGame = (float) sumTotalThisEndGame / (float) (sumTotalThisEndGame + sumTotalAllEndGame);
        } else {
            if (sumTotalAllEndGame != 0)
                frequencyThisEndGame = (float) sumTotalThisEndGame / (float) sumTotalAllEndGame;
        }
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
        float totalHandel = sumGoodTG + sumEqualTG + sumBadTG;
        if (totalHandel != 0) {
            goodPercent = (float) sumGoodTG / (float) totalHandel;
            equalPercent = (float) sumEqualTG / (float) totalHandel;
            badPercent = (float) sumBadTG / (float) totalHandel;
        }
        //
        if (totalRAG != 0) {
            winPercent = (float) sumWinTG / (float) totalRAG;
            drawPercent = (float) sumDrawTG / (float) totalRAG;
            lossPercent = (float) sumLostTG / (float) totalRAG;
        }
        updateDataEndGame();
    }

    @SuppressLint("SetTextI18n")
    private void updateDataEndGame() {
        firebaseFirestore.collection("EndGames").document(endGameUid)
                .update("totalTG", sumTotalThisEndGame
                        , "totalFAG", sumTotalAllEndGame
                        , "frequency", frequencyThisEndGame
                        , "score", score
                        , "efficiency", efficiency
                        , "totalRAG", totalRAG
                        , "sumGoodTG", sumGoodTG
                        , "sumEqualTG", sumEqualTG
                        , "sumBadTG", sumBadTG
                        , "sumWinTG", sumWinTG
                        , "sumDrawTG", sumDrawTG
                        , "sumLostTG", sumLostTG
                        , "goodPercent", goodPercent
                        , "equalPercent", equalPercent
                        , "badPercent", badPercent
                        , "winPercent", winPercent
                        , "drawPercent", drawPercent
                        , "lossPercent", lossPercent).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                getEndGameInfo();
                if (isMainGame())
                    updateEndGameInPreviousAdapter();
            }
        });
    }

    private void editDateEndGame() {
        firebaseFirestore.collection("EndGames").document(endGameId)
                .update("endName", endGameName
                        , "good", good
                        , "equal", equal
                        , "bad", bad
                        , "totalWin", winEdit
                        , "totalDraw", drawEdit
                        , "totalLoss", lostEdit
                        , "totalResult", totalResult
                        , "total", totalResult).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                bottomSheetDialog.dismiss();
                Toast.makeText(ViewEndGame.this, "Update", Toast.LENGTH_SHORT).show();
                calculateFrequencyEfficiencScore(endGameUid);
                getAllEndGameInThisGame();
            }
        });
    }

    private float updateFrequency = 0;

    private void updateEndGameInPreviousAdapter() {
        // Update Main Mistake Game In Previous Adapter And BackPressed
        updateFrequency = 0;
        firebaseFirestore.collection("EndGames")
                .whereEqualTo("userId", userUid)
                .whereEqualTo("accountType", accountType).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    Game game = queryDocumentSnapshot.toObject(Game.class);
                    if ((sumTotalAllEndGame + sumTotalThisEndGame) != 0)
                        updateFrequency = (float) game.getTotalTG() / (float) (sumTotalAllEndGame + sumTotalThisEndGame);
                    String gameId = queryDocumentSnapshot.getId();
                    firebaseFirestore.collection("EndGames").document(gameId)
                            .update("frequency", updateFrequency);
                }
            }
        });
    }

    private void filInitialDataCurrentGame(Game game) {
        good = game.getGood();
        equal = game.getEqual();
        bad = game.getBad();
        //
        winEdit = game.getTotalWin();
        drawEdit = game.getTotalDraw();
        lostEdit = game.getTotalLoss();
    }

    private void filData(Game game) {
        sumTotalThisEndGame = sumTotalThisEndGame + game.getTotal();
        totalRTG = totalRTG + game.getTotalResult();
        totalWin = totalWin + game.getTotalWin();
        totalDraw = totalDraw + game.getTotalDraw();
        //
        sumGoodTG += game.getGood();
        sumEqualTG += game.getEqual();
        sumBadTG += game.getBad();
        //
        sumWinTG += game.getTotalWin();
        sumDrawTG += game.getTotalDraw();
        sumLostTG += game.getTotalLoss();
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
                draw = 0;
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
    public void onBackPressed() {
        if (!MovesIdStack.moveIdStack.empty()) {
            MovesIdStack.popStack();
            StackGameModel endModel = new StackGameModel();
            if (!MovesIdStack.moveIdStack.empty()) {
                endModel = MovesIdStack.peekStack();
                endGameUid = endModel.getGameId();
                endGameName = endModel.getGameName();
                //getEndGameInfo();
                getAllEndGameInThisGame();
                calculateFrequencyEfficiencScore(endGameUid);
                isView = true;
                currentOpponent = 0;
                updateOpponent(endGameUid);
                /*if (isMainGame())
                    updateEndGameInPreviousAdapter();*/
            } else
                super.onBackPressed();
        }
    }

    private void resetValues() {
        sumTotalAllEndGame = 0;
        sumTotalThisEndGame = 0;
        frequencyThisEndGame = 0;
        total = 0;
        totalRTG = 0;
        totalRAG = 0;
        totalResult = 0;
        totalWin = 0;
        totalDraw = 0;
        score = 0;
        efficiency = 0;
        occurance = 0;
        efficiencyThisEndGame = 0;
        good = 0;
        bad = 0;
        equal = 0;
        sumGoodTG = 0;
        sumEqualTG = 0;
        sumBadTG = 0;
        sumWinTG = 0;
        sumDrawTG = 0;
        sumLostTG = 0;
        goodPercent = 0;
        equalPercent = 0;
        badPercent = 0;
        winPercent = 0;
        drawPercent = 0;
        lossPercent = 0;
    }

    private boolean isMainGame() {
        return MovesIdStack.getMoveIdStack().size() == 1;
    }
}