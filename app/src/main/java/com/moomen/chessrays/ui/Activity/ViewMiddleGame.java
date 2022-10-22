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
import com.moomen.chessrays.ui.Fragment.MiddleFragment;
import com.moomen.chessrays.utils.MovesIdStack;
import com.moomen.chessrays.utils.PreferenceUtils;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.Calendar;

public class ViewMiddleGame extends AppCompatActivity {
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

    private CardView cardViewCreateMenu;

    private TextView textViewScore;
    private TextView textViewTotal;
    private TextView textViewFrequency;
    private TextView textViewEfficiency;

    private String date;
    private String middleGameColor = "";
    private String middleGameFirstMove = "";
    private String middleGameUid;
    private String middleGameId;
    private String middleGameUidDelete;
    private String userUid;
    private String gameId;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private BottomSheetDialog bottomSheetDialog;

    //----------------------
    private EditText editMiddleName;
    private EditText editTextGood;
    private EditText editTextEqual;
    private EditText editTextBad;
    private EditText editTextWin;
    private EditText editTextDraw;
    private EditText editTextLost;
    private Button buttonCreate;
    private TextView textViewMiddleName;
    //-----------------------
    private EditText editAddOpponent;
    private ConstraintLayout constraintLayoutAddOpponent;

    private String middleGameName;
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
    private float frequency = 0;
    private float efficiency = 0;
    private float occurance = 0;

    private float score = 0;
    private int totalWin = 0;
    private float totalDraw = 0;
    private int totalLoss = 0;
    private int sumTotalAllMiddleGame = 0;
    private int sumTotalThisMiddleGame = 0;
    private float frequencyThisMiddleGame = 0;

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

    private RecyclerView recyclerView;

    private ConstraintLayout constraintLayoutNoDataRecycle;
    private ConstraintLayout constraintLayoutLazyLoaderRecycle;
    private ConstraintLayout constraintLayoutLazyLoader;
    private ConstraintLayout constraintLayoutLazyLoader2;
    private ConstraintLayout constraintLayoutLazyLoader3;
    private LazyLoader lazyLoaderRecycle;
    private LazyLoader lazyLoader;
    private LazyLoader lazyLoader2;
    private LazyLoader lazyLoader3;
    private String accountType;
    private ImageView clearOpposition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_middle_game);
        recyclerView = findViewById(R.id.recyclerView_all_challenge_id);
        textViewOpposition = findViewById(R.id.textView_opposition_id);
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
        textViewMiddleName = findViewById(R.id.textView_middle_name);
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

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(MiddleFragment.MIDDLE_GAME_UID)
                && intent.hasExtra(MiddleFragment.GAME_UID)
                && intent.hasExtra(MiddleFragment.USER_UID)
                && intent.hasExtra(MiddleFragment.MIDDLE_GAME_NAME)) {
            middleGameUid = intent.getStringExtra(MiddleFragment.MIDDLE_GAME_UID);
            gameId = intent.getStringExtra(MiddleFragment.GAME_UID);
            userUid = intent.getStringExtra(MiddleFragment.USER_UID);
            textViewMiddleName.setText(MiddleFragment.MIDDLE_GAME_NAME);
        }

        constraintLayoutAddOpponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOpponentForSelectedGame(middleGameUid);
            }
        });

        cardViewCreateMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewMiddleBottomSheet();
            }
        });

        clearOpposition();
        getMiddleGameInfo();
        getAllMiddleGameInThisGame();
        calculateFrequencyEfficiencScore(middleGameUid);
    }

    private void clearOpposition(){
        clearOpposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ViewMiddleGame.this);
                dialogBuilder.setTitle("Reset opposition")
                        .setMessage("Are you sure you want to reset all opposition?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                firebaseFirestore.collection("MiddleGames")
                                        .document(middleGameUid)
                                        .update("countOpponent", 0
                                                , "opposition", 0
                                                , "totalOpponent", 0
                                                , "totalOpposition", 0).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        getMiddleGameInfo();
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
                firebaseFirestore.collection("MiddleGames")
                        .whereEqualTo("thisGameId", middleGameUid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

    private boolean isView = false;

    private void getMiddleGameInfo() {
        progressVisibility(true);
        firebaseFirestore.collection("MiddleGames").document(middleGameUid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Game middleGame = documentSnapshot.toObject(Game.class);
                assert middleGame != null;
                //
                String score = (middleGame.getScore() * 100) + "000000";
                if (middleGame.getScore() == 0 || middleGame.getScore() == 1)
                    textViewScore.setText(middleGame.getScore() * 100 + " %");
                else
                    textViewScore.setText(score.substring(0, 4) + " %");
                //
                String frequency = (middleGame.getFrequency() * 100) + "000000";
                if (middleGame.getFrequency() == 0 || middleGame.getFrequency() == 10)
                    textViewFrequency.setText(middleGame.getFrequency() * 100 + " %");
                else
                    textViewFrequency.setText(frequency.substring(0, 4) + " %");
                String efficiency = (middleGame.getEfficiency() * 100) + "000000";
                if (middleGame.getEfficiency() == 0 || middleGame.getEfficiency() == 10)
                    textViewEfficiency.setText(middleGame.getEfficiency() * 100 + " %");
                else
                    textViewEfficiency.setText(efficiency.substring(0, 4) + " %");
                //--------------
                String goodPercent = (middleGame.getGoodPercent() * 100) + "000000";
                if (middleGame.getGoodPercent() == 0 || middleGame.getGoodPercent() == 10)
                    textViewGoodPercent.setText(middleGame.getGoodPercent() * 100 + " %");
                else
                    textViewGoodPercent.setText(goodPercent.substring(0, 4) + " %");

                String equalPercent = (middleGame.getEqualPercent() * 100) + "000000";
                if (middleGame.getEqualPercent() == 0 || middleGame.getEqualPercent() == 10)
                    textViewEqualPercent.setText(middleGame.getEqualPercent() * 100 + " %");
                else
                    textViewEqualPercent.setText(equalPercent.substring(0, 4) + " %");
                //
                String badPercent = (middleGame.getBadPercent() * 100) + "000000";
                if (middleGame.getBadPercent() == 0 || middleGame.getBadPercent() == 10)
                    textViewBadPercent.setText(middleGame.getBadPercent() * 100 + " %");
                else
                    textViewBadPercent.setText(badPercent.substring(0, 4) + " %");
                //
                String winPercent = (middleGame.getWinPercent() * 100) + "000000";
                if (middleGame.getWinPercent() == 0 || middleGame.getWinPercent() == 10)
                    textViewWinPercent.setText(middleGame.getWinPercent() * 100 + " %");
                else
                    textViewWinPercent.setText(winPercent.substring(0, 4) + " %");
                ////
                String drawPercent = (middleGame.getDrawPercent() * 100) + "000000";
                if (middleGame.getDrawPercent() == 0 || middleGame.getDrawPercent() == 10)
                    textViewDrawPercent.setText(middleGame.getDrawPercent() * 100 + " %");
                else
                    textViewDrawPercent.setText(drawPercent.substring(0, 4) + " %");
                ////
                String lossPercent = (middleGame.getLossPercent() * 100) + "000000";
                if (middleGame.getGoodPercent() == 0 || middleGame.getLossPercent() == 10)
                    textViewLostPercent.setText(middleGame.getLossPercent() * 100 + " %");
                else
                    textViewLostPercent.setText(lossPercent.substring(0, 4) + " %");
                //----------
                textViewMiddleName.setText(middleGame.getGameName());
                textViewGood.setText(middleGame.getSumGoodTG() + "");
                textViewEqual.setText(middleGame.getSumEqualTG() + "");
                textViewBad.setText(middleGame.getSumBadTG() + "");
                textViewWin.setText(middleGame.getSumWinTG() + "");
                textViewDraw.setText(middleGame.getSumDrawTG() + "");
                textViewLost.setText(middleGame.getSumLostTG() + "");
                textViewTotal.setText(middleGame.getTotalRAG() + "");
                int opposition = (int) middleGame.getTotalOpposition();
                textViewOpposition.setText(opposition + "");
                progressVisibility(false);
            }
        });
    }

    private void getAllMiddleGameInThisGame() {
        Query query = FirebaseFirestore.getInstance().collection("MiddleGames")
                .whereEqualTo("thisGameId", middleGameUid)
                .orderBy("sortNum", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Game> options = new FirestoreRecyclerOptions.Builder<Game>()
                .setQuery(query, Game.class)
                .build();
        HandlingAdapter handlingAdapter = new HandlingAdapter(options);
        handlingAdapter.onItemSetOnClickListener(new HandlingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position, PopupMenu popupMenu) {
                Game middleGame = documentSnapshot.toObject(Game.class);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        isEdit = false;
                        isView = false;
                        switch (item.getItemId()) {
                            case R.id.popup_view_item_id:
                                middleGameUid = documentSnapshot.getId();
                                middleGameName = middleGame.getGameName();
                                StackGameModel stackGameModel = new StackGameModel(middleGameUid, middleGameName);
                                MovesIdStack.pushStack(stackGameModel);
                                calculateFrequencyEfficiencScore(middleGameUid);
                                getAllMiddleGameInThisGame();
                                getMiddleGameInfo();
                                isView = true;
                                currentOpponent = 0;
                                updateOpponent(middleGameUid);
                                break;
                            case R.id.popup_edit_item_id:
                                middleGameId = documentSnapshot.getId();
                                isEdit = true;
                                editMiddleGame(middleGameId);
                                break;
                            /*case R.id.popup_add_opponent_item_id:
                                middleGameId = documentSnapshot.getId();
                                addOpponentForSelectedGame(middleGameId);
                                break;*/
                            case R.id.popup_delete_item_id:
                                middleGameUidDelete = documentSnapshot.getId();
                                //firebaseFirestore.collection("MiddleGames").document(middleGameUid).delete();
                                deleteMiddle(middleGameUidDelete);
                                //MovesIdStack.popStack();
                                StackGameModel middleModel1 = MovesIdStack.peekStack();
                                middleGameName = middleModel1.getGameName();
                                calculateFrequencyEfficiencScore(middleGameUid);
                                getAllMiddleGameInThisGame();
                                getMiddleGameInfo();
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

    private void addOpponentForSelectedGame(String middleGameId) {
        countOpponent = 0;
        opposition = 0;
        sumOpponent = 0;
        currentOpponent = 0;
        totalOpponent = 0;
        totalOpposition = 0;
        sumTotalOpponent = 0;
        //Get number opponent for this game and when complete add new the opponent
        bottomSheetDialog = new BottomSheetDialog(ViewMiddleGame.this);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(ViewMiddleGame.this).inflate(R.layout.add_opponents_bottom_sheet, null);
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
                updateOpponent(middleGameId);
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

    private void updateOpponent(String middleGameId) {
        countOpponent = 0;
        opposition = 0;
        sumOpponent = 0;
        totalOpponent = 0;
        totalOpposition = 0;
        sumTotalOpponent = 0;
        firebaseFirestore.collection("MiddleGames").document(middleGameId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Game middleGame = task.getResult().toObject(Game.class);
                countOpponent = middleGame.getCountOpponent();
                opposition = middleGame.getOpposition();
                sumOpponent = countOpponent * opposition;
                //Update data when add
                if (currentOpponent > 0) {
                    sumOpponent = (countOpponent * opposition) + currentOpponent;
                    countOpponent = countOpponent + 1;
                    opposition = (int) (sumOpponent / countOpponent);
                }
                //Now get the opposition and number opponent for all game in this game
                firebaseFirestore.collection("MiddleGames").whereEqualTo("gameId", middleGameId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            Game middleGame = queryDocumentSnapshot.toObject(Game.class);
                            totalOpponent = totalOpponent + middleGame.getCountOpponent();
                            sumTotalOpponent = sumTotalOpponent + (middleGame.getCountOpponent() * middleGame.getOpposition());
                        }
                        totalOpponent = totalOpponent + countOpponent;
                        sumTotalOpponent = sumTotalOpponent + sumOpponent;

                        if (totalOpponent > 0)
                            totalOpposition = sumTotalOpponent / totalOpponent;
                        //Now update opponent and opposition
                        //If add new
                        firebaseFirestore.collection("MiddleGames").document(middleGameId)
                                .update("countOpponent", countOpponent
                                        , "opposition", opposition
                                        , "totalOpponent", totalOpponent
                                        , "totalOpposition", totalOpposition).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!isView) {
                                    bottomSheetDialog.dismiss();
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(ViewMiddleGame.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                                }
                                getMiddleGameInfo();
                            }
                        });
                    }
                });
            }
        });
    }

    private void editMiddleGame(String middleGameId) {
        firebaseFirestore.collection("MiddleGames").document(middleGameId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Game middleGame = documentSnapshot.toObject(Game.class);
                addNewMiddleBottomSheet();
                buttonCreate.setText("Save");
                editMiddleName.setText(middleGame.getGameName());
                editTextGood.setText(middleGame.getGood() + "");
                editTextEqual.setText(middleGame.getEqual() + "");
                editTextBad.setText(middleGame.getBad() + "");
                editTextWin.setText(middleGame.getTotalWin() + "");
                editTextDraw.setText(middleGame.getTotalDraw() + "");
                editTextLost.setText(middleGame.getTotalLoss() + "");
            }
        });
    }

    private void deleteMiddle(String middleGameUidDelete) {
        firebaseFirestore.collection("MiddleGames").whereEqualTo("thisGameId", middleGameUidDelete).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                firebaseFirestore.collection("MiddleGames").document(middleGameUidDelete).delete();
                //If last item refresh recycle and game info
                if (task.getResult().isEmpty()) {
                    getMiddleGameInfo();
                    getAllMiddleGameInThisGame();
                    calculateFrequencyEfficiencScore(middleGameUid);
                }
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    deleteMiddle(queryDocumentSnapshot.getId());
                }
            }
        });
    }

    private void addNewMiddleBottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(ViewMiddleGame.this);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(ViewMiddleGame.this).inflate(R.layout.create_handling_bottom_sheet_layout, null);
        editMiddleName = view.findViewById(R.id.editText_move_name_id);
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
                middleGameName = editMiddleName.getText().toString().trim();
                if (middleGameName.isEmpty()) {
                    editMiddleName.setError("The Challenger name is required!");
                    editMiddleName.requestFocus();
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
                sumTotalThisMiddleGame = 0;
                sumTotalAllMiddleGame = 0;
                frequencyThisMiddleGame = 0;
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
                    Game middleGame = new Game(accountType,"", userUid, gameId, middleGameUid
                            , middleGameName, middleGameColor, middleGameFirstMove, date + ""
                            , score, winEdit, drawEdit, lostEdit, good, goodPercent, equal, equalPercent
                            , bad, badPercent, win, winPercent, draw, drawPercent, loss, lossPercent, totalResult
                            , totalResult, sumTotalAllMiddleGame, result, totalResult, totalRTG, totalRAG
                            , frequency, efficiency, occurance, sumGoodTG, sumEqualTG, sumBadTG, sumWinTG
                            , sumDrawTG, sumLostTG, 0, 0, 0, 0, 20);
                    addNewMiddle(middleGame);
                } else
                    editDateMiddleGame();
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


    private void addNewMiddle(Game middleGame) {
        firebaseFirestore.collection("MiddleGames").add(middleGame).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    bottomSheetDialog.dismiss();
                    Toast.makeText(ViewMiddleGame.this, "Created", Toast.LENGTH_SHORT).show();
                    getMiddleGameInfo();
                    getAllMiddleGameInThisGame();
                    calculateFrequencyEfficiencScore(middleGameUid);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ViewMiddleGame.this, "Failed to publish, try again!", Toast.LENGTH_SHORT).show();
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

    private void calculateFrequencyEfficiencScore(String middleGameUid) {
        //Calculate total all game
        String gameId = MovesIdStack.getMoveIdStack().get(0).getGameId();
        if (isMainGame()) {
            calculateMainGame(middleGameUid);
        } else {
            calculateSubGame(gameId, middleGameUid);
        }
    }

    private void calculateMainGame(String middleGameUid) {
        firebaseFirestore.collection("MiddleGames")
                .whereEqualTo("userId", userUid)
                .whereEqualTo("accountType", accountType)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                resetValues();
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    Game game = queryDocumentSnapshot.toObject(Game.class);
                    String g = queryDocumentSnapshot.getId();
                    if (!g.equals(middleGameUid))
                        sumTotalAllMiddleGame = sumTotalAllMiddleGame + game.getTotalRAG();
                }
                //2- Get data for main opened game and calculate total all game in it.
                firebaseFirestore.collection("MiddleGames").document(middleGameUid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Game game = documentSnapshot.toObject(Game.class);
                        assert game != null;
                        filData(game);
                        filInitialDataCurrentGame(game);
                        CalculateTotalInThisGame(middleGameUid);
                    }
                });
            }
        });
    }

    private void calculateSubGame(String gameId, String middleGameUid) {
        firebaseFirestore.collection("MiddleGames").whereEqualTo("gameId", gameId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                resetValues();
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    Game game = queryDocumentSnapshot.toObject(Game.class);
                    sumTotalAllMiddleGame = sumTotalAllMiddleGame + game.getTotalRAG();
                }
                //get data for this game and calculate total all game in it.
                firebaseFirestore.collection("MiddleGames").document(middleGameUid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Game game = documentSnapshot.toObject(Game.class);
                        assert game != null;
                        filData(game);
                        filInitialDataCurrentGame(game);
                        CalculateTotalInThisGame(middleGameUid);
                    }
                });
            }
        });
    }

    private void CalculateTotalInThisGame(String middleGameId) {
        firebaseFirestore.collection("MiddleGames").whereEqualTo("thisGameId", middleGameId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            Game middleGame = queryDocumentSnapshot.toObject(Game.class);
                            filData(middleGame);
                            CalculateTotalInThisGame(queryDocumentSnapshot.getId());
                        }
                    } else //Calculate all results (frequancy, efficant, result) for this Middle
                        calculateAllResults();
                }
            }
        });
    }

    private void calculateAllResults() {
        if (isMainGame()) {
            if ((sumTotalThisMiddleGame + sumTotalAllMiddleGame) != 0)
                frequencyThisMiddleGame = (float) sumTotalThisMiddleGame / (float) (sumTotalThisMiddleGame + sumTotalAllMiddleGame);
        } else {
            if (sumTotalAllMiddleGame != 0)
                frequencyThisMiddleGame = (float) sumTotalThisMiddleGame / (float) sumTotalAllMiddleGame;
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
        updateDataMiddleGame();
    }

    @SuppressLint("SetTextI18n")
    private void updateDataMiddleGame() {
        firebaseFirestore.collection("MiddleGames").document(middleGameUid)
                .update("totalTG", sumTotalThisMiddleGame
                        , "totalFAG", sumTotalAllMiddleGame
                        , "frequency", frequencyThisMiddleGame
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
                getMiddleGameInfo();
                if (isMainGame())
                    updateMiddleGameInPreviousAdapter();
            }
        });
    }

    private void editDateMiddleGame() {
        firebaseFirestore.collection("MiddleGames").document(middleGameId)
                .update("middleName", middleGameName
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
                Toast.makeText(ViewMiddleGame.this, "Update", Toast.LENGTH_SHORT).show();
                calculateFrequencyEfficiencScore(middleGameUid);
                getAllMiddleGameInThisGame();
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


    private float updateFrequency = 0;

    private void updateMiddleGameInPreviousAdapter() {
        // Update Main Mistake Game In Previous Adapter And BackPressed
        updateFrequency = 0;
        firebaseFirestore.collection("MiddleGames")
                .whereEqualTo("userId", userUid)
                .whereEqualTo("accountType", accountType)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    Game game = queryDocumentSnapshot.toObject(Game.class);
                    if ((sumTotalAllMiddleGame + sumTotalThisMiddleGame) != 0)
                        updateFrequency = (float) game.getTotalTG() / (float) (sumTotalAllMiddleGame + sumTotalThisMiddleGame);
                    String gameId = queryDocumentSnapshot.getId();
                    firebaseFirestore.collection("MiddleGames").document(gameId)
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
        sumTotalThisMiddleGame = sumTotalThisMiddleGame + game.getTotal();
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

    @Override
    public void onBackPressed() {
        if (!MovesIdStack.moveIdStack.empty()) {
            MovesIdStack.popStack();
            StackGameModel middleModel = new StackGameModel();
            if (!MovesIdStack.moveIdStack.empty()) {
                middleModel = MovesIdStack.peekStack();
                middleGameUid = middleModel.getGameId();
                middleGameName = middleModel.getGameName();
                //getMiddleGameInfo();
                getAllMiddleGameInThisGame();
                calculateFrequencyEfficiencScore(middleGameUid);
                isView = true;
                currentOpponent = 0;
                updateOpponent(middleGameUid);
            } else
                super.onBackPressed();
        }
    }

    private void resetValues() {
        sumTotalAllMiddleGame = 0;
        sumTotalThisMiddleGame = 0;
        frequencyThisMiddleGame = 0;
        total = 0;
        totalRTG = 0;
        totalRAG = 0;
        totalResult = 0;
        totalWin = 0;
        totalDraw = 0;
        score = 0;
        efficiency = 0;
        occurance = 0;
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