package com.moomen.chessrays.ui.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import com.google.firebase.firestore.CollectionReference;
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
import com.moomen.chessrays.adapter.MistakesGamesAdapter;
import com.moomen.chessrays.model.ChartGame;
import com.moomen.chessrays.model.Game;
import com.moomen.chessrays.model.InstallNewGame;
import com.moomen.chessrays.model.StackGameModel;
import com.moomen.chessrays.ui.Fragment.MistakesFragment;
import com.moomen.chessrays.utils.MovesIdStack;
import com.moomen.chessrays.utils.PreferenceUtils;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ViewMistakeGame extends AppCompatActivity {
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

    private TextView textViewDate;
    private TextView textViewTotal;
    private TextView textViewFrequency;
    private TextView textViewEfficiency;

    private String challengeName = "";
    private String date;

    private String mistakeGameUid;
    private String mistakeGameId;
    private String mistakeGameUidDelete;
    private String userUid;
    private String gameId;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private BottomSheetDialog bottomSheetDialog;
    private FirebaseUser firebaseUser;

    private int challengeCount = 0;

    //----------------------
    private EditText editMistakesName;
    private EditText editTextOccurence;
    private DatePickerDialog datePickerDialog;
    private ImageView imageViewDate;
    private EditText editTextDate;
    private Button buttonCreate;
    private TextView textViewMistakesName;
    //-----------------------
    private EditText editAddOpponent;
    private ConstraintLayout constraintLayoutAddOpponent;

    private String mistakeGameName;
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
    private int allMovesInThisMistakes = 0;

    private int scoreMistakesGame = 0;
    private int sumTotalAllMistakesGame = 0;
    private int sumTotalThisMistakesGame = 0;
    private float frequencyThisMistakesGame = 0;

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
    private float totalPass = 0;

    private ConstraintLayout constraintLayoutLazyLoader;
    private LazyLoader lazyLoader;
    private String accountType;
    private ImageView clearOpposition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_mistake_game);

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
        textViewMistakesName = findViewById(R.id.textView_mistake_name);
        cardViewCreateMenu = findViewById(R.id.cardView_create_id);
        textViewDate = findViewById(R.id.textView_game_date_id);
        textViewTotal = findViewById(R.id.textView_total_id);
        textViewFrequency = findViewById(R.id.textView_frequenc_id);
        textViewEfficiency = findViewById(R.id.textView_efficiency_id);
        accountType = PreferenceUtils.getAccountType(getApplicationContext());
        constraintLayoutAddOpponent = findViewById(R.id.constraintLayout_add_opponent);

        lazyLoader = findViewById(R.id.lazyLoader_id);
        constraintLayoutLazyLoader = findViewById(R.id.constraintLayout_lazyLoader_id);
        preparProgress(lazyLoader);

        date = DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(MistakesFragment.MISTAKES_GAME_UID)
                && intent.hasExtra(MistakesFragment.GAME_UID)
                && intent.hasExtra(MistakesFragment.USER_UID)
                && intent.hasExtra(MistakesFragment.MISTAKES_GAME_NAME)) {
            mistakeGameUid = intent.getStringExtra(MistakesFragment.MISTAKES_GAME_UID);
            gameId = intent.getStringExtra(MistakesFragment.GAME_UID);
            userUid = intent.getStringExtra(MistakesFragment.USER_UID);
            textViewMistakesName.setText(MistakesFragment.MISTAKES_GAME_NAME);
        }

        constraintLayoutAddOpponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOpponentForSelectedGame(mistakeGameUid);
            }
        });

        cardViewCreateMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //createChallengeBottomSheet();
                addNewMistakesBottomSheet();
            }
        });
        clearOpposition();
        getMistakesGameInfo();
        calculateFrequencyEfficiencScore(mistakeGameUid);
    }
    private void clearOpposition(){
        clearOpposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ViewMistakeGame.this);
                dialogBuilder.setTitle("Reset opposition")
                        .setMessage("Are you sure you want to reset all opposition?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                firebaseFirestore.collection("MistakesGames")
                                        .document(mistakeGameUid)
                                        .update("countOpponent", 0
                                                , "opposition", 0
                                                , "totalOpponent", 0
                                                , "totalOpposition", 0).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        getMistakesGameInfo();
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
    private boolean isView = false;

    private void getMistakesGameInfo() {
        constraintLayoutLazyLoader.setVisibility(View.VISIBLE);
        firebaseFirestore.collection("MistakesGames").document(mistakeGameUid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Game game = documentSnapshot.toObject(Game.class);
                assert game != null;
                int opposition = (int) game.getTotalOpposition();
                textViewOpposition.setText(opposition + "");
                ////
                textViewMistakesName.setText(game.getGameName());
                textViewTotal.setText(game.getTotalTG() + "");
                textViewDate.setText(game.getDate() + "");
                //
                String frequency = (game.getFrequency() * 100) + "000000";
                if (game.getFrequency() == 0 || game.getFrequency() == 100)
                    textViewFrequency.setText(game.getFrequency() * 100 + " %");
                else
                    textViewFrequency.setText(frequency.substring(0, 4) + " %");
                constraintLayoutLazyLoader.setVisibility(View.GONE);
            }
        });
    }

    private void getAllMistakesGameInThisGame() {
        Query query = FirebaseFirestore.getInstance().collection("MistakesGames")
                .whereEqualTo("thisGameId", mistakeGameUid)
                .orderBy("sortNum", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Game> options = new FirestoreRecyclerOptions.Builder<Game>()
                .setQuery(query, Game.class)
                .build();
        MistakesGamesAdapter handlingAdapter = new MistakesGamesAdapter(options);
        /*if (isPass) {
            handlingAdapter.setTotal(totalPass);
            handlingAdapter.setEdit(true);
            isPass = false;
        }*/
        handlingAdapter.onItemSetOnClickListener(new MistakesGamesAdapter.OnItemClickListener() {
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
                                mistakeGameUid = documentSnapshot.getId();
                                mistakeGameName = game.getGameName();
                                StackGameModel stackGameModel = new StackGameModel(mistakeGameUid, mistakeGameName);
                                MovesIdStack.pushStack(stackGameModel);
                                calculateFrequencyEfficiencScore(mistakeGameUid);
                                getAllMistakesGameInThisGame();
                                getMistakesGameInfo();
                                //--------opponent
                                isView = true;
                                currentOpponent = 0;
                                updateOpponent(mistakeGameUid);
                                break;
                            case R.id.popup_edit_item_id:
                                //---------
                                /*mistakeGameUid = documentSnapshot.getId();
                                mistakeGameName = game.getGameName();
                                StackGameModel stackGameModel1 = new StackGameModel(mistakeGameUid, mistakeGameName);
                                MovesIdStack.pushStack(stackGameModel1);
                                //calculateFrequencyEfficiencScore(mistakeGameUid);
                                getAllMistakesGameInThisGame();
                                getMistakesGameInfo();
                                isView = true;*/
                                //---------
                                mistakeGameId = documentSnapshot.getId();
                                isEdit = true;
                                oldTotal = game.getTotal();
                                oldTotalTG = game.getTotalTG();
                                editMistakesGame(mistakeGameId);
                                break;
                            /*case R.id.popup_add_opponent_item_id:
                                mistakeGameId = documentSnapshot.getId();
                                addOpponentForSelectedGame(mistakeGameId);
                                break;*/
                            case R.id.popup_delete_item_id:
                                mistakeGameUidDelete = documentSnapshot.getId();
                                //firebaseFirestore.collection("MistakesGames").document(mistakeGameUid).delete();
                                deleteMistakes(mistakeGameUidDelete);
                                //MovesIdStack.popStack();
                                StackGameModel mistakeModel1 = MovesIdStack.peekStack();
                                mistakeGameName = mistakeModel1.getGameName();
                                calculateFrequencyEfficiencScore(mistakeGameUid);
                                getAllMistakesGameInThisGame();
                                getMistakesGameInfo();
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

    private void addOpponentForSelectedGame(String mistakeGameId) {
        countOpponent = 0;
        opposition = 0;
        sumOpponent = 0;
        currentOpponent = 0;
        totalOpponent = 0;
        totalOpposition = 0;
        sumTotalOpponent = 0;
        //Get number opponent for this game and when complete add new the opponent
        bottomSheetDialog = new BottomSheetDialog(ViewMistakeGame.this);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(ViewMistakeGame.this).inflate(R.layout.add_opponents_bottom_sheet, null);
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
                updateOpponent(mistakeGameId);
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

    private void updateOpponent(String mistakeGameId) {
        countOpponent = 0;
        opposition = 0;
        sumOpponent = 0;
        totalOpponent = 0;
        totalOpposition = 0;
        sumTotalOpponent = 0;
        firebaseFirestore.collection("MistakesGames").document(mistakeGameId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                firebaseFirestore.collection("MistakesGames").whereEqualTo("gameId", mistakeGameId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                        firebaseFirestore.collection("MistakesGames").document(mistakeGameId)
                                .update("countOpponent", countOpponent
                                        , "opposition", opposition
                                        , "totalOpponent", totalOpponent
                                        , "totalOpposition", totalOpposition).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!isView) {
                                    bottomSheetDialog.dismiss();
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(ViewMistakeGame.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                                }
                                getMistakesGameInfo();
                            }
                        });
                    }
                });
            }
        });
    }

    private void editMistakesGame(String mistakeGameId) {
        firebaseFirestore.collection("MistakesGames").document(mistakeGameId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Game game = documentSnapshot.toObject(Game.class);
                addNewMistakesBottomSheet();
                buttonCreate.setText("Save");
                editMistakesName.setText(game.getGameName());
                editTextOccurence.setText(game.getTotal() + "");
                editTextDate.setText(game.getDate() + "");
            }
        });
    }

    private void deleteMistakes(String mistakeGameUidDelete) {
        firebaseFirestore.collection("MistakesGames").whereEqualTo("thisGameId", mistakeGameUidDelete).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                firebaseFirestore.collection("MistakesGames").document(mistakeGameUidDelete).delete();
                //If last item refresh recycle and game info
                if (task.getResult().isEmpty()) {
                    getMistakesGameInfo();
                    getAllMistakesGameInThisGame();
                    calculateFrequencyEfficiencScore(mistakeGameUid);
                }
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    deleteMistakes(queryDocumentSnapshot.getId());
                }
            }
        });
    }

    private float oldTotalTG = 0;
    private float newTotalTG = 0;
    private float oldTotal = 0;

    private void addNewMistakesBottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(ViewMistakeGame.this);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(ViewMistakeGame.this).inflate(R.layout.create_handling_mistake_bottom_sheet_layout, null);
        editMistakesName = view.findViewById(R.id.editText_name_the_mistake_id);
        editTextOccurence = view.findViewById(R.id.editText_occurence_id);
        imageViewDate = view.findViewById(R.id.imageView_date);
        editTextDate = view.findViewById(R.id.editText_date);
        progressBar = view.findViewById(R.id.progressBar_id);
        progressBar.setVisibility(View.GONE);
        buttonBack = view.findViewById(R.id.image_back_id);
        buttonCreate = view.findViewById(R.id.button_create_handling_id);
        radioGroup = view.findViewById(R.id.radioGroup_id);
        //---------------
        imageViewDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(ViewMistakeGame.this);
                Calendar calendar = Calendar.getInstance();
                datePickerDialog = new DatePickerDialog(ViewMistakeGame.this, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //date = dayOfMonth + "/" + month + "/" + year;
                        editTextDate.setText(dayOfMonth + "/" + month + "/" + year);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        //---------------
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                date = editTextDate.getText().toString().trim();
                mistakeGameName = editMistakesName.getText().toString().trim();
                if (mistakeGameName.isEmpty()) {
                    editMistakesName.setError("The Challenger name is required!");
                    editMistakesName.requestFocus();
                    return;
                }
                total = getValueEditText(editTextOccurence);
                sumTotalThisMistakesGame = 0;
                sumTotalAllMistakesGame = 0;
                frequencyThisMistakesGame = 0;
                //----------------------
                userUid = PreferenceUtils.getUid(getApplicationContext());
                if (!isEdit) {
                    Game game = new Game(accountType,"", userUid, gameId, mistakeGameUid
                            , mistakeGameName, "", "", date + ""
                            , 0, 0, 0, 0, 0, 0, 0
                            , 0, 0, 0, 0, 0, 0, 0, 0
                            , 0, total, total, 0, 0, 0, 0, 0
                            , 0, 0, 0, 0, 0, 0, 0, 0
                            , 0, 0, 0, 0, 0, 50);
                    addNewMistakes(game);
                } else {
                    newTotalTG = (oldTotalTG - oldTotal) + total;
                    editDataMistakesGame();
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


    private void addNewMistakes(Game game) {
        firebaseFirestore.collection("MistakesGames").add(game).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    bottomSheetDialog.dismiss();
                    Toast.makeText(ViewMistakeGame.this, "Created", Toast.LENGTH_SHORT).show();
                    getMistakesGameInfo();
                    getAllMistakesGameInThisGame();
                    calculateFrequencyEfficiencScore(mistakeGameUid);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ViewMistakeGame.this, "Failed to publish, try again!", Toast.LENGTH_SHORT).show();
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

    private void calculateFrequencyEfficiencScore(String mistakeGameUid) {
        //Calculate total all game
        String gameId = MovesIdStack.getMoveIdStack().get(0).getGameId();
        if (MovesIdStack.getMoveIdStack().size() == 1) {
            firebaseFirestore.collection("MistakesGames")
                    .whereEqualTo("userUid", userUid)
                    .whereEqualTo("accountType", accountType).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    resetValues();
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        Game game = queryDocumentSnapshot.toObject(Game.class);
                        sumTotalAllMistakesGame = sumTotalAllMistakesGame + game.getTotal();
                    }
                    //get data for this game and calculate total all game in it.
                    firebaseFirestore.collection("MistakesGames").document(mistakeGameUid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Game game = documentSnapshot.toObject(Game.class);
                            assert game != null;
                            sumTotalThisMistakesGame = sumTotalThisMistakesGame + game.getTotal();
                            CalculateTotalInThisGame(mistakeGameUid);
                        }
                    });
                }
            });
        } else {
            firebaseFirestore.collection("MistakesGames").whereEqualTo("gameId", gameId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    resetValues();
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        Game game = queryDocumentSnapshot.toObject(Game.class);
                        sumTotalAllMistakesGame = sumTotalAllMistakesGame + game.getTotal();
                    }
                    //get data for this game and calculate total all game in it.
                    firebaseFirestore.collection("MistakesGames").document(mistakeGameUid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Game game = documentSnapshot.toObject(Game.class);
                            assert game != null;
                            sumTotalThisMistakesGame = sumTotalThisMistakesGame + game.getTotal();
                            CalculateTotalInThisGame(mistakeGameUid);
                        }
                    });
                }
            });
        }
    }

    private void CalculateTotalInThisGame(String mistakeGameId) {
        firebaseFirestore.collection("MistakesGames").whereEqualTo("thisGameId", mistakeGameId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            Game game = queryDocumentSnapshot.toObject(Game.class);
                            sumTotalThisMistakesGame = sumTotalThisMistakesGame + game.getTotalTG();
                            //CalculateTotalInThisGame2(queryDocumentSnapshot.getId());
                        }
                    }
                    calculateAllResults();
                }
            }
        });
    }

    private void CalculateTotalInThisGame2(String mistakeGameId) {
        firebaseFirestore.collection("MistakesGames").whereEqualTo("thisGameId", mistakeGameId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            Game game = queryDocumentSnapshot.toObject(Game.class);
                            sumTotalThisMistakesGame = sumTotalThisMistakesGame + game.getTotal();
                            CalculateTotalInThisGame2(queryDocumentSnapshot.getId());
                        }
                    }
                }
            }
        });
    }

    private void calculateAllResults() {
        if (sumTotalAllMistakesGame != 0 && sumTotalThisMistakesGame != 0)
            frequencyThisMistakesGame = (float) sumTotalThisMistakesGame / (float) sumTotalAllMistakesGame;
        updateDataMistakesGame();
    }

    @SuppressLint("SetTextI18n")
    private void updateDataMistakesGame() {
        firebaseFirestore.collection("MistakesGames").document(mistakeGameUid)
                .update("totalTG", sumTotalThisMistakesGame
                        , "totalFAG", sumTotalAllMistakesGame
                        , "frequency", frequencyThisMistakesGame
                ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                getMistakesGameInfo();
                updateGameInAdapter();
            }
        });
    }

    float frequencyUpdate = 0;

    private void updateGameInAdapter() {
        frequencyUpdate = 0;
        firebaseFirestore.collection("MistakesGames").whereEqualTo("thisGameId", mistakeGameUid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            Game game = queryDocumentSnapshot.toObject(Game.class);
                            if (sumTotalThisMistakesGame != 0)
                                frequencyUpdate = (float) game.getTotalTG() / (float) sumTotalThisMistakesGame;
                            String gameId = queryDocumentSnapshot.getId();
                            firebaseFirestore.collection("MistakesGames").document(gameId)
                                    .update("frequency", frequencyUpdate);
                        }
                        getAllMistakesGameInThisGame();
                        updateMainMistakeGameInPreviousAdapter();
                    }
                }
            }
        });
    }
    //private boolean isPass = false;

    private void editDataMistakesGame() {
        firebaseFirestore.collection("MistakesGames").document(mistakeGameId)
                .update("gameName", mistakeGameName
                        , "date", date
                        , "total", total
                        , "totalTG", newTotalTG).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                bottomSheetDialog.dismiss();
                //isPass = true;
                Toast.makeText(ViewMistakeGame.this, "Update", Toast.LENGTH_SHORT).show();
                calculateFrequencyEfficiencScore(mistakeGameUid);
            }
        });
    }

    private float updateFrequency = 0;

    @Override
    public void onBackPressed() {
        if (!MovesIdStack.moveIdStack.empty()) {
            MovesIdStack.popStack();
            StackGameModel mistakeModel = new StackGameModel();
            if (!MovesIdStack.moveIdStack.empty()) {
                mistakeModel = MovesIdStack.peekStack();
                mistakeGameUid = mistakeModel.getGameId();
                mistakeGameName = mistakeModel.getGameName();
                getMistakesGameInfo();
                getAllMistakesGameInThisGame();
                calculateFrequencyEfficiencScore(mistakeGameUid);
                isView = true;
                currentOpponent = 0;
                updateOpponent(mistakeGameUid);
                if (MovesIdStack.getMoveIdStack().size() == 1) {
                    updateMainMistakeGameInPreviousAdapter();
                }
            } else
                super.onBackPressed();
        }
    }

    /*@Override
    protected void onPostResume() {
        super.onPostResume();
        if (MovesIdStack.getMoveIdStack().size() == 1) {
            updateMainMistakeGameInPreviousAdapter();
        }
    }*/

    private void updateMainMistakeGameInPreviousAdapter() {
        // Update Main Mistake Game In Previous Adapter And BackPressed
        updateFrequency = 0;
        firebaseFirestore.collection("MistakesGames")
                .whereEqualTo("userId", userUid)
                .whereEqualTo("accountType", accountType).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    Game game = queryDocumentSnapshot.toObject(Game.class);
                    if (sumTotalAllMistakesGame != 0)
                        updateFrequency = (float) game.getTotalTG() / (float) sumTotalAllMistakesGame;
                    String gameId = queryDocumentSnapshot.getId();
                    firebaseFirestore.collection("MistakesGames").document(gameId)
                            .update("frequency", updateFrequency);
                }
            }
        });
    }

    private void resetValues() {
        sumTotalAllMistakesGame = 0;
        sumTotalThisMistakesGame = 0;
        frequencyThisMistakesGame = 0;
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
}