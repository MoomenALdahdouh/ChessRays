package com.moomen.chessrays.ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.moomen.chessrays.ui.Fragment.OpeningFragment;
import com.moomen.chessrays.utils.MovesIdStack;
import com.moomen.chessrays.utils.PreferenceUtils;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.Calendar;

public class ViewOpeningGame extends AppCompatActivity {

    public static final String OPENING_MOVE_UID = "OPENING_MOVE_UID";
    public static final String OPENING_MOVE_NAME = "OPENING_MOVE_NAME";
    public static final String OPENING_UID = "OPENING_UID";
    public static final String USER_UID = "USER_UID";
    private TextView textViewColor;
    private TextView textViewFirstMove;
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
    //-----------------------
    private EditText editAddOpponent;
    private ConstraintLayout constraintLayoutAddOpponent;

    private String openingGameName;
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

    private ConstraintLayout constraintLayoutLazyLoader;
    private ConstraintLayout constraintLayoutLazyLoader2;
    private ConstraintLayout constraintLayoutLazyLoader3;
    private ConstraintLayout constraintLayoutLazyLoader4;

    private LazyLoader lazyLoader;
    private LazyLoader lazyLoader2;
    private LazyLoader lazyLoader3;
    private LazyLoader lazyLoader4;
    private String accountType;
    private ImageView clearOpposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_opening_game);

        textViewColor = findViewById(R.id.textView_color_id);
        textViewFirstMove = findViewById(R.id.textView_first_move_id);
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
        imageViewAddMove = findViewById(R.id.imageView_create_id);
        textViewAddMove = findViewById(R.id.textView_add_new_move_id);
        textViewOpeningName = findViewById(R.id.textView_opening_name);
        cardViewCreateMenu = findViewById(R.id.cardView_create_id);
        textViewScore = findViewById(R.id.textView_game_score_id);
        textViewTotal = findViewById(R.id.textView_total_id);
        textViewFrequency = findViewById(R.id.textView_frequenc_id);
        textViewEfficiency = findViewById(R.id.textView_efficiency_id);

        lazyLoader = findViewById(R.id.lazyLoader_id);
        lazyLoader2 = findViewById(R.id.lazyLoader2_id);
        lazyLoader3 = findViewById(R.id.lazyLoader3_id);
        lazyLoader4 = findViewById(R.id.lazyLoader4_id);
        constraintLayoutLazyLoader = findViewById(R.id.constraintLayout_lazyLoader_id);
        constraintLayoutLazyLoader2 = findViewById(R.id.constraintLayout_lazyLoader2_id);
        constraintLayoutLazyLoader3 = findViewById(R.id.constraintLayout_lazyLoader3_id);
        constraintLayoutLazyLoader4 = findViewById(R.id.constraintLayout_lazyLoader4_id);
        prepareProgress();

        constraintLayoutAddOpponent = findViewById(R.id.constraintLayout_add_opponent);
        accountType = PreferenceUtils.getAccountType(getApplicationContext());
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

        constraintLayoutAddOpponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOpponentForSelectedGame(openingGameUid);
            }
        });

        cardViewCreateMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //createChallengeBottomSheet();
                addNewOpeningBottomSheet();
            }
        });
        clearOpposition();
        getAllOpeningGameInThisGame();
        calculateFrequencyEfficiencScore(openingGameUid);
    }

    private void clearOpposition() {
        clearOpposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ViewOpeningGame.this);
                dialogBuilder.setTitle("Reset opposition")
                        .setMessage("Are you sure you want to reset all opposition?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                firebaseFirestore.collection("OpeningGames")
                                        .document(openingGameUid)
                                        .update("countOpponent", 0
                                                , "opposition", 0
                                                , "totalOpponent", 0
                                                , "totalOpposition", 0).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        getOpeningGameInfo();
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

    private void prepareProgress() {
        LazyLoader loader = new LazyLoader(this, 30, 20, ContextCompat.getColor(this, R.color.loader_selected),
                ContextCompat.getColor(this, R.color.loader_selected),
                ContextCompat.getColor(this, R.color.loader_selected));
        loader.setAnimDuration(500);
        loader.setFirstDelayDuration(100);
        loader.setSecondDelayDuration(200);
        loader.setInterpolator(new LinearInterpolator());

        LazyLoader loader2 = new LazyLoader(this, 30, 20, ContextCompat.getColor(this, R.color.loader_selected),
                ContextCompat.getColor(this, R.color.loader_selected),
                ContextCompat.getColor(this, R.color.loader_selected));
        loader2.setAnimDuration(500);
        loader2.setFirstDelayDuration(100);
        loader2.setSecondDelayDuration(200);
        loader2.setInterpolator(new LinearInterpolator());

        LazyLoader loader3 = new LazyLoader(this, 30, 20, ContextCompat.getColor(this, R.color.loader_selected),
                ContextCompat.getColor(this, R.color.loader_selected),
                ContextCompat.getColor(this, R.color.loader_selected));
        loader3.setAnimDuration(500);
        loader3.setFirstDelayDuration(100);
        loader3.setSecondDelayDuration(200);
        loader3.setInterpolator(new LinearInterpolator());

        LazyLoader loader4 = new LazyLoader(this, 30, 20, ContextCompat.getColor(this, R.color.loader_selected),
                ContextCompat.getColor(this, R.color.loader_selected),
                ContextCompat.getColor(this, R.color.loader_selected));
        loader4.setAnimDuration(500);
        loader4.setFirstDelayDuration(100);
        loader4.setSecondDelayDuration(200);
        loader4.setInterpolator(new LinearInterpolator());
        lazyLoader.addView(loader);
        lazyLoader2.addView(loader2);
        lazyLoader3.addView(loader3);
        lazyLoader4.addView(loader4);
    }

    private boolean isView = false;

    private void getOpeningGameInfo() {
        constraintLayoutLazyLoader.setVisibility(View.VISIBLE);
        constraintLayoutLazyLoader2.setVisibility(View.VISIBLE);
        constraintLayoutLazyLoader3.setVisibility(View.VISIBLE);
        constraintLayoutLazyLoader4.setVisibility(View.VISIBLE);
        firebaseFirestore.collection("OpeningGames").document(openingGameUid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Game game = task.getResult().toObject(Game.class);
                assert game != null;
                openingGameColor = game.getColor();
                openingGameFirstMove = game.getFirstMove();
                textViewColor.setText(openingGameColor);
                textViewFirstMove.setText(openingGameFirstMove);
                int opposition = (int) game.getTotalOpposition();
                textViewOpposition.setText(opposition + "");
                ////
                textViewTotal.setText(game.getTotalRAG() + "");
                //
                String score = (game.getScore() * 100) + "000000";
                if (game.getScore() == 0 || game.getScore() == 1) {
                    textViewScore.setText(game.getScore() * 100 + " %");
                } else {
                    textViewScore.setText(score.substring(0, 4) + " %");
                }
                //
                String frequency = (game.getFrequency() * 100) + "000000";
                if (game.getFrequency() == 0 || game.getFrequency() == 10)
                    textViewFrequency.setText(game.getFrequency() * 100 + " %");
                else
                    textViewFrequency.setText(frequency.substring(0, 4) + " %");
                //textViewFrequency.setText(frequency.substring(0, 5) + " %");
                String efficiency = (game.getEfficiency() * 100) + "000000";
                if (game.getEfficiency() == 0 || game.getEfficiency() == 10)
                    textViewEfficiency.setText(game.getEfficiency() * 100 + " %");
                else
                    textViewEfficiency.setText(efficiency.substring(0, 4) + " %");
                //
                String goodPercent = (game.getGoodPercent() * 100) + "000000";
                if (game.getGoodPercent() == 0 || game.getGoodPercent() == 10)
                    textViewGoodPercent.setText(game.getGoodPercent() * 100 + " %");
                else
                    textViewGoodPercent.setText(goodPercent.substring(0, 4) + " %");

                String equalPercent = (game.getEqualPercent() * 100) + "000000";
                if (game.getEqualPercent() == 0 || game.getEqualPercent() == 10)
                    textViewEqualPercent.setText(game.getEqualPercent() * 100 + " %");
                else
                    textViewEqualPercent.setText(equalPercent.substring(0, 4) + " %");
                //
                String badPercent = (game.getBadPercent() * 100) + "000000";
                if (game.getBadPercent() == 0 || game.getBadPercent() == 10)
                    textViewBadPercent.setText(game.getBadPercent() * 100 + " %");
                else
                    textViewBadPercent.setText(badPercent.substring(0, 4) + " %");
                //
                String winPercent = (game.getWinPercent() * 100) + "000000";
                if (game.getWinPercent() == 0 || game.getWinPercent() == 10)
                    textViewWinPercent.setText(game.getWinPercent() * 100 + " %");
                else
                    textViewWinPercent.setText(winPercent.substring(0, 4) + " %");
                ////
                String drawPercent = (game.getDrawPercent() * 100) + "000000";
                if (game.getDrawPercent() == 0 || game.getDrawPercent() == 10)
                    textViewDrawPercent.setText(game.getDrawPercent() * 100 + " %");
                else
                    textViewDrawPercent.setText(drawPercent.substring(0, 4) + " %");
                ////
                String lossPercent = (game.getLossPercent() * 100) + "000000";
                if (game.getGoodPercent() == 0 || game.getLossPercent() == 10)
                    textViewLostPercent.setText(game.getLossPercent() * 100 + " %");
                else
                    textViewLostPercent.setText(lossPercent.substring(0, 4) + " %");
                //
                textViewOpeningName.setText(game.getGameName());
                textViewGood.setText(game.getSumGoodTG() + "");
                textViewEqual.setText(game.getSumEqualTG() + "");
                textViewBad.setText(game.getSumBadTG() + "");
                textViewWin.setText(game.getSumWinTG() + "");
                textViewDraw.setText(game.getSumDrawTG() + "");
                textViewLost.setText(game.getSumLostTG() + "");
                constraintLayoutLazyLoader.setVisibility(View.GONE);
                constraintLayoutLazyLoader2.setVisibility(View.GONE);
                constraintLayoutLazyLoader3.setVisibility(View.GONE);
                constraintLayoutLazyLoader4.setVisibility(View.GONE);
            }
        });
    }

    private void getAllOpeningGameInThisGame() {
        Query query = FirebaseFirestore.getInstance().collection("OpeningGames")
                .whereEqualTo("thisGameId", openingGameUid)
                .orderBy("date", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Game> options = new FirestoreRecyclerOptions.Builder<Game>()
                .setQuery(query, Game.class)
                .build();
        HandlingAdapter handlingAdapter = new HandlingAdapter(options);
        handlingAdapter.onItemSetOnClickListener(new HandlingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position, PopupMenu popupMenu) {
                Game game = documentSnapshot.toObject(Game.class);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        isEdit = false;
                        isView = false;
                        switch (item.getItemId()) {
                            case R.id.popup_view_item_id:
                                openingGameUid = documentSnapshot.getId();
                                openingGameName = game.getGameName();
                                StackGameModel stackGameModel = new StackGameModel(openingGameUid, openingGameName);
                                MovesIdStack.pushStack(stackGameModel);
                                calculateFrequencyEfficiencScore(openingGameUid);
                                getAllOpeningGameInThisGame();
                                //getOpeningGameInfo();
                                isView = true;
                                currentOpponent = 0;
                                updateOpponent(openingGameUid);
                                break;
                            case R.id.popup_edit_item_id:
                                openingGameId = documentSnapshot.getId();
                                isEdit = true;
                                editOpeningGame(openingGameId);
                                break;
                            /*case R.id.popup_add_opponent_item_id:
                                openingGameId = documentSnapshot.getId();
                                addOpponentForSelectedGame(openingGameId);
                                break;*/
                            case R.id.popup_delete_item_id:
                                openingGameUidDelete = documentSnapshot.getId();
                                //firebaseFirestore.collection("OpeningGames").document(openingGameUid).delete();
                                deleteOpening(openingGameUidDelete);
                                //MovesIdStack.popStack();
                                StackGameModel openingModel1 = MovesIdStack.peekStack();
                                openingGameName = openingModel1.getGameName();
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

    private int countOpponent = 0;
    private int totalOpponent = 0;
    private int opposition = 0;
    private int totalOpposition = 0;
    private int sumOpponent = 0;
    private int sumTotalOpponent = 0;
    private int currentOpponent = 0;

    private void addOpponentForSelectedGame(String openingGameId) {
        countOpponent = 0;
        opposition = 0;
        sumOpponent = 0;
        currentOpponent = 0;
        totalOpponent = 0;
        totalOpposition = 0;
        sumTotalOpponent = 0;
        //Get number opponent for this game and when complete add new the opponent
        bottomSheetDialog = new BottomSheetDialog(ViewOpeningGame.this);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(ViewOpeningGame.this).inflate(R.layout.add_opponents_bottom_sheet, null);
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
                updateOpponent(openingGameId);
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

    private void updateOpponent(String openingGameId) {
        countOpponent = 0;
        opposition = 0;
        sumOpponent = 0;
        totalOpponent = 0;
        totalOpposition = 0;
        sumTotalOpponent = 0;
        firebaseFirestore.collection("OpeningGames").document(openingGameId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Game game = task.getResult().toObject(Game.class);
                countOpponent = game.getCountOpponent();
                opposition = game.getOpposition();
                sumOpponent = countOpponent * opposition;
                //Update data when add
                if (currentOpponent > 0) {
                    sumOpponent = (countOpponent * opposition) + currentOpponent;
                    countOpponent = countOpponent + 1;
                    opposition = (int) (sumOpponent / countOpponent);
                }
                //Now get the opposition and number opponent for all game in this game
                firebaseFirestore.collection("OpeningGames").whereEqualTo("gameId", openingGameId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            Game game = queryDocumentSnapshot.toObject(Game.class);
                            totalOpponent = totalOpponent + game.getCountOpponent();
                            sumTotalOpponent = sumTotalOpponent + (game.getCountOpponent() * game.getOpposition());
                        }
                        totalOpponent = totalOpponent + countOpponent;
                        sumTotalOpponent = sumTotalOpponent + sumOpponent;

                        if (totalOpponent > 0)
                            totalOpposition = sumTotalOpponent / totalOpponent;
                        //Now update opponent and opposition
                        //If add new
                        firebaseFirestore.collection("OpeningGames").document(openingGameId)
                                .update("countOpponent", countOpponent
                                        , "opposition", opposition
                                        , "totalOpponent", totalOpponent
                                        , "totalOpposition", totalOpposition).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!isView) {
                                    bottomSheetDialog.dismiss();
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(ViewOpeningGame.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                                }
                                getOpeningGameInfo();
                            }
                        });
                    }
                });
            }
        });
    }

    private void editOpeningGame(String openingGameId) {
        firebaseFirestore.collection("OpeningGames").document(openingGameId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Game game = documentSnapshot.toObject(Game.class);
                addNewOpeningBottomSheet();
                buttonCreate.setText("Save");
                editOpeningName.setText(game.getGameName());
                editTextGood.setText(game.getGood() + "");
                editTextEqual.setText(game.getEqual() + "");
                editTextBad.setText(game.getBad() + "");
                editTextWin.setText(game.getTotalWin() + "");
                editTextDraw.setText(game.getTotalDraw() + "");
                editTextLost.setText(game.getTotalLoss() + "");
            }
        });
    }

    private void deleteOpening(String openingGameUidDelete) {
        firebaseFirestore.collection("OpeningGames").whereEqualTo("thisGameId", openingGameUidDelete).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                firebaseFirestore.collection("OpeningGames").document(openingGameUidDelete).delete();
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
                    Game game = new Game(accountType, "", userUid, gameId, openingGameUid
                            , openingGameName, openingGameColor, openingGameFirstMove, date + ""
                            , score, winEdit, drawEdit, lostEdit, good, goodPercent, equal, equalPercent
                            , bad, badPercent, win, winPercent, draw, drawPercent, loss, lossPercent, totalResult
                            , totalResult, sumTotalAllOpeningGame, result, totalResult, totalRTG, totalRAG
                            , frequency, efficiency, occurance, sumGoodTG, sumEqualTG, sumBadTG, sumWinTG, sumDrawTG
                            , sumLostTG, 0, 0, 0, 0, 20);
                    addNewOpening(game);
                } else
                    editDataOpeningGame();
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


    private void addNewOpening(Game game) {
        firebaseFirestore.collection("OpeningGames").add(game).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    bottomSheetDialog.dismiss();
                    Toast.makeText(ViewOpeningGame.this, "Created", Toast.LENGTH_SHORT).show();
                    //getOpeningGameInfo();
                    getAllOpeningGameInThisGame();
                    /*if (MovesIdStack.getMoveIdStack().size() == 1) {
                        updateTotalFAG();
                    } else*/
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

    private void updateTotalFAG() {
        firebaseFirestore.collection("OpeningGames").document(openingGameUid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                Game game = task.getResult().toObject(Game.class);
                firebaseFirestore.collection("OpeningGames").document(openingGameUid).update("totalFAG", game.getTotalFAG() + totalResult).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        calculateFrequencyEfficiencScore(openingGameUid);
                    }
                });
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

    private float oldTotalTG = 0;
    private float newTotalTG = 0;
    private float oldTotal = 0;

    private void calculateFrequencyEfficiencScore(String openingGameUid) {
        //Calculate total all game
        String gameId = MovesIdStack.getMoveIdStack().get(0).getGameId();
        if (isMainGame()) {
            calculateMainOpeningGame(openingGameUid);
        } else {
            calculateSubGame(gameId, openingGameUid);
        }
    }

    private void calculateSubGame(String gameId, String openingGameUid) {
        firebaseFirestore.collection("OpeningGames").whereEqualTo("gameId", gameId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                resetValues();
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    Game game = queryDocumentSnapshot.toObject(Game.class);
                    sumTotalAllOpeningGame = sumTotalAllOpeningGame + game.getTotal();
                }
                //get data for this game and calculate total all game in it.
                firebaseFirestore.collection("OpeningGames").document(openingGameUid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Game game = documentSnapshot.toObject(Game.class);
                        assert game != null;
                        filData(game);
                        filInitialDataCurrentGame(game);
                        CalculateTotalInThisGame(openingGameUid);
                    }
                });
            }
        });
    }

    private String gameColor = "";

    private void calculateMainOpeningGame(String openingGameUid) {
        //1- Get game info. to know what color it is and get all main games which have the same color.
        firebaseFirestore.collection("OpeningGames").document(openingGameUid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Game game = task.getResult().toObject(Game.class);
                gameColor = game.getColor();
                assert game != null;
                firebaseFirestore.collection("OpeningGames")
                        .whereEqualTo("userId", userUid)
                        .whereEqualTo("color", gameColor)
                        .whereEqualTo("accountType", accountType)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        resetValues();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            Game game = queryDocumentSnapshot.toObject(Game.class);
                            String g = queryDocumentSnapshot.getId();
                            if (!g.equals(openingGameUid))
                                sumTotalAllOpeningGame = sumTotalAllOpeningGame + game.getTotalRAG();
                        }
                        //2- Get data for main opened game and calculate total all game in it.
                        firebaseFirestore.collection("OpeningGames").document(openingGameUid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Game game = documentSnapshot.toObject(Game.class);
                                assert game != null;
                                filData(game);
                                filInitialDataCurrentGame(game);
                                CalculateTotalInThisGame(openingGameUid);
                            }
                        });
                    }
                });
            }
        });
    }

    private void CalculateTotalInThisGame(String openingGameId) {
        firebaseFirestore.collection("OpeningGames").whereEqualTo("thisGameId", openingGameId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            Game game = queryDocumentSnapshot.toObject(Game.class);
                            filData(game);
                            CalculateTotalInThisGame(queryDocumentSnapshot.getId());
                        }
                    } else //Calculate all results (frequancy, efficant, result) for this Opening
                        calculateAllResults();
                }
            }
        });
    }

    private void filData(Game game) {
        sumTotalThisOpeningGame = sumTotalThisOpeningGame + game.getTotal();
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

    private void filInitialDataCurrentGame(Game game) {
        good = game.getGood();
        equal = game.getEqual();
        bad = game.getBad();
        //
        winEdit = game.getTotalWin();
        drawEdit = game.getTotalDraw();
        lostEdit = game.getTotalLoss();
    }

    private void calculateAllResults() {
        //
        if (isMainGame()) {
            if ((sumTotalThisOpeningGame + sumTotalAllOpeningGame) != 0)
                frequencyThisOpeningGame = (float) sumTotalThisOpeningGame / (float) (sumTotalThisOpeningGame + sumTotalAllOpeningGame);
        } else {
            if (sumTotalAllOpeningGame != 0)
                frequencyThisOpeningGame = (float) sumTotalThisOpeningGame / (float) sumTotalAllOpeningGame;
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
            goodPercent = (float) sumGoodTG / totalHandel;
            equalPercent = (float) sumEqualTG / totalHandel;
            badPercent = (float) sumBadTG / totalHandel;
        }
        if (totalRAG != 0) {
            winPercent = (float) sumWinTG / (float) totalRAG;
            drawPercent = (float) sumDrawTG / (float) totalRAG;
            lossPercent = (float) sumLostTG / (float) totalRAG;
        }
        updateDataOpeningGame();
    }

    @SuppressLint("SetTextI18n")
    private void updateDataOpeningGame() {
        firebaseFirestore.collection("OpeningGames").document(openingGameUid)
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
                        , "sumLostTG", sumLostTG
                        , "goodPercent", goodPercent
                        , "equalPercent", equalPercent
                        , "badPercent", badPercent
                        , "winPercent", winPercent
                        , "drawPercent", drawPercent
                        , "lossPercent", lossPercent).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                getOpeningGameInfo();
                if (isMainGame()) {
                    updateMainOpeningGameInPreviousAdapter();
                }
            }
        });
    }

    private boolean isCalculate = false;

    private void editDataOpeningGame() {
        firebaseFirestore.collection("OpeningGames").document(openingGameId)
                .update("openingName", openingGameName
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

    private void updateMainOpeningGameInPreviousAdapter() {
        // Update Main opening Game In Previous Adapter And BackPressed
        updateFrequency = 0;
        firebaseFirestore.collection("OpeningGames")
                .whereEqualTo("userId", userUid)
                .whereEqualTo("color", gameColor)
                .whereEqualTo("accountType", accountType).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    Game game = queryDocumentSnapshot.toObject(Game.class);
                    if ((sumTotalThisOpeningGame + sumTotalAllOpeningGame) != 0)
                        updateFrequency = (float) game.getTotalTG() / (float) (sumTotalThisOpeningGame + sumTotalAllOpeningGame);
                    String gameId = queryDocumentSnapshot.getId();
                    firebaseFirestore.collection("OpeningGames").document(gameId)
                            .update("frequency", updateFrequency);
                }
            }
        });
    }

    float scoreUpdate = 0;

    private void updateGameInAdapter() {
        scoreUpdate = 0;
        firebaseFirestore.collection("OpeningGames").whereEqualTo("thisGameId", openingGameUid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            Game game = queryDocumentSnapshot.toObject(Game.class);
                            if (totalRAG != 0)
                                scoreUpdate = ((float) (game.getTotalWin() + game.getTotalDraw() * 0.5)) / (float) totalRAG;
                            String gameId = queryDocumentSnapshot.getId();
                            firebaseFirestore.collection("OpeningGames").document(gameId)
                                    .update("score", scoreUpdate);
                        }
                        getAllOpeningGameInThisGame();
                    }
                }
            }
        });
    }

    int count = 0;

    @Override
    public void onBackPressed() {
        if (!MovesIdStack.moveIdStack.empty()) {
            StackGameModel opening = MovesIdStack.peekStack();
            MovesIdStack.popStack();
            StackGameModel openingModel = new StackGameModel();
            if (!MovesIdStack.moveIdStack.empty()) {
                count = 0;
                openingModel = MovesIdStack.peekStack();
                openingGameUid = openingModel.getGameId();
                openingGameName = openingModel.getGameName();
                //getOpeningGameInfo();
                getAllOpeningGameInThisGame();
                calculateFrequencyEfficiencScore(openingGameUid);
                isView = true;
                currentOpponent = 0;
                updateOpponent(openingGameUid);

            } else {
                if (count > 0) {
                    count = 0;
                    super.onBackPressed();
                } else {
                    Toast.makeText(ViewOpeningGame.this, "Calculation in progress, please wait 1sec.", Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            count++;
                            StackGameModel stackGameModel = new StackGameModel(opening.getGameId(), opening.getGameName());
                            MovesIdStack.pushStack(stackGameModel);
                        }
                    }, 1000);
                }
            }
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