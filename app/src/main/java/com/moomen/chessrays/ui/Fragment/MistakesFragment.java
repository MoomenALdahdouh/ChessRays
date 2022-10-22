package com.moomen.chessrays.ui.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agrawalsuneet.dotsloader.loaders.LazyLoader;
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
import com.moomen.chessrays.adapter.MistakesGamesAdapter;
import com.moomen.chessrays.model.DashboardViewModel;
import com.moomen.chessrays.model.Game;
import com.moomen.chessrays.model.StackGameModel;
import com.moomen.chessrays.ui.Activity.ViewMistakeGame;
import com.moomen.chessrays.utils.MovesIdStack;
import com.moomen.chessrays.utils.PreferenceUtils;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.Calendar;

public class MistakesFragment extends Fragment {

    public static final String MISTAKES_GAME_UID = "MISTAKES_GAME_UID";
    public static final String MISTAKES_GAME_NAME = "MISTAKES_GAME_NAME";
    public static final String USER_UID = "USER_UID";
    public static final String GAME_UID = "GAME_UID";

    private ProgressBar progressBar;
    private BottomSheetDialog bottomSheetDialog;

    private DashboardViewModel dashboardViewModel;
    private View root;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;

    int sumTotalAllMistakesGame  = 0;
    int sumTotalThisMistakesGame  = 0;

    private String color = "White";
    private String firstMove = "";
    private String mistakeType = "";
    private String date;
    private String userUid;
    private String gameUid;
    private String mistakeGameUid;
    private float total;

    private ConstraintLayout constraintLayoutLazyLoader;
    private ConstraintLayout constraintLayoutNoData;
    private LazyLoader lazyLoader;
    private String accountType;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        root = inflater.inflate(R.layout.fragment_mistakes, container, false);
        setHasOptionsMenu(true);

        lazyLoader = root.findViewById(R.id.lazyLoader_id);
        constraintLayoutLazyLoader = root.findViewById(R.id.constraintLayout_lazyLoader_id);
        constraintLayoutNoData = root.findViewById(R.id.constraintLayout_no_data_id);
        preparProgress();

        date = DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userUid = PreferenceUtils.getUid(getContext());
        accountType = PreferenceUtils.getAccountType(getContext());
        firebaseFirestore.collection("MistakesGames")
                .whereEqualTo("userId", userUid)
                .whereEqualTo("accountType", accountType)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult().isEmpty()) {
                    createDefaultMiddleGame("Opening Mistakes", 0,1);
                    createDefaultMiddleGame("Strategical Mistakes", 0,2);
                    createDefaultMiddleGame("Dynamical Mistakes", 0,3);
                    createDefaultMiddleGame("Tactics", 0,4);
                    createDefaultMiddleGame("Endgame", 0,5);
                    createDefaultMiddleGame("Psychological and other types", 1,6);
                } else {
                    constraintLayoutLazyLoader.setVisibility(View.GONE);
                    getAllMistakesGames();
                }
            }
        });
        checkIfHaveData();
        return root;
    }
    private void checkIfHaveData() {
        constraintLayoutLazyLoader.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseFirestore.getInstance().collection("MistakesGames")
                        .whereEqualTo("userId", userUid)
                        .whereEqualTo("accountType", accountType)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        constraintLayoutLazyLoader.setVisibility(View.GONE);
                        if (task.getResult().isEmpty())
                            constraintLayoutNoData.setVisibility(View.VISIBLE);
                        else
                            constraintLayoutNoData.setVisibility(View.GONE);
                    }
                });
            }
        }, 2000);
    }
    private void preparProgress(){
        LazyLoader loader = new LazyLoader(getContext(), 30, 20, ContextCompat.getColor(getContext(), R.color.loader_selected),
                ContextCompat.getColor(getContext(), R.color.loader_selected),
                ContextCompat.getColor(getContext(), R.color.loader_selected));
        loader.setAnimDuration(500);
        loader.setFirstDelayDuration(100);
        loader.setSecondDelayDuration(200);
        loader.setInterpolator(new LinearInterpolator());
        lazyLoader.addView(loader);
    }

    private void calculateTotalInMistakes() {
        FirebaseFirestore.getInstance().collection("MistakesGames").whereEqualTo("userId", userUid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    Game game = queryDocumentSnapshot.toObject(Game.class);
                    sumTotalAllMistakesGame = sumTotalAllMistakesGame + game.getTotal();
                }
                FirebaseFirestore.getInstance().collection("MistakesGames").whereEqualTo("thisGameId", "mistakeId").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            Game game = queryDocumentSnapshot.toObject(Game.class);
                            sumTotalThisMistakesGame = sumTotalThisMistakesGame + game.getTotal();
                        }
                    }
                });
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    public void mistakeGameBottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.create_mistakes_game, root.findViewById(R.id.mistake_game_layout_id));
        progressBar = view.findViewById(R.id.progressBar_id);
        progressBar.setVisibility(View.GONE);
        Button create = (Button) view.findViewById(R.id.button_create_mistake_game_id);
        Spinner spinnerColor = view.findViewById(R.id.spinner_color_id);
        spinnerCreate(spinnerColor);
        EditText mistakeTypeEditText = view.findViewById(R.id.editText_first_move_id);
        //date = Calendar.getInstance().getTime();
        ImageView back = view.findViewById(R.id.image_back_id);
        String userId = PreferenceUtils.getID(getContext());
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                mistakeType = mistakeTypeEditText.getText().toString().trim();
                if (mistakeType.isEmpty()) {
                    mistakeTypeEditText.setError("The mistake type is required!");
                    mistakeTypeEditText.requestFocus();
                    return;
                }
                //getUserUid(userId);
                createNewMistakesGame();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    private void spinnerCreate(Spinner spinner) {
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(getContext(), R.array.color, R.layout.support_simple_spinner_dropdown_item);
        adapterSpinner.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        color = "White";
                        break;
                    case 1:
                        color = "Black";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                color = "White";
            }
        });
    }

    private void createNewMistakesGame() {
        Game game = new Game(accountType,userUid, userUid, "", ""
                , mistakeType, "", "", date + "", 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0,0,0,0,0,35);
        firebaseFirestore.collection("MistakesGames").add(game).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    //mistakeGameUid = task.getResult().getId();
                    //Intent intent = new Intent(getContext(), ViewMistakesGame.class);
                    //intent.putExtra(MISTAKES_GAME_UID, mistakeGameUid);
                    //intent.putExtra(USER_UID, userUid);
                    //startActivity(intent);
                    Toast.makeText(getContext(), "Created", Toast.LENGTH_SHORT).show();
                    bottomSheetDialog.dismiss();
                    getAllMistakesGames();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed to publish, try again!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getAllMistakesGames() {
        Query query = FirebaseFirestore.getInstance()
                .collection("MistakesGames")
                .whereEqualTo("userId", userUid)
                .whereEqualTo("accountType", accountType)
                .orderBy("sortNum", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Game> options = new FirestoreRecyclerOptions.Builder<Game>()
                .setQuery(query, Game.class)
                .build();
        MistakesGamesAdapter mistakeGameAdapter = new MistakesGamesAdapter(options);
        //mistakeGameAdapter.setMistakeFragment(true);
        //click on item in the recycler view
        mistakeGameAdapter.onItemSetOnClickListener(new MistakesGamesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position, PopupMenu popupMenu) {
                mistakeGameUid = documentSnapshot.getId();
                //getMistakesGameUid(gameUid);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Game game = documentSnapshot.toObject(Game.class);
                        switch (item.getItemId()) {
                            case R.id.popup_view_item_id:
                                mistakeGameUid = documentSnapshot.getId();
                                //Create default sub middle in this middle if not created before
                                firebaseFirestore.collection("MistakesGames").whereEqualTo("thisGameId", mistakeGameUid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.getResult().isEmpty())
                                            checkMiddleTypeAndCreate(game.getGameName());
                                    }
                                });

                                //Bush this game data in stack and intent to view
                                StackGameModel stackGameModel = new StackGameModel(mistakeGameUid, "");
                                MovesIdStack.pushStack(stackGameModel);
                                Intent intent = new Intent(getContext(), ViewMistakeGame.class);
                                intent.putExtra(MISTAKES_GAME_UID, mistakeGameUid);
                                intent.putExtra(MISTAKES_GAME_NAME, game.getGameName());
                                intent.putExtra(GAME_UID, mistakeGameUid);
                                intent.putExtra(USER_UID, userUid);
                                startActivity(intent);
                                break;
                            case R.id.popup_delete_item_id:
                                firebaseFirestore.collection("MistakesGames").whereEqualTo("gameId", mistakeGameUid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (!task.getResult().isEmpty()) {
                                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                String gameId = queryDocumentSnapshot.getId();
                                                firebaseFirestore.collection("MistakesGames").document(gameId).delete();
                                            }
                                        }
                                        firebaseFirestore.collection("MistakesGames").document(mistakeGameUid).delete();
                                        checkIfHaveData();
                                    }
                                });
                                break;
                        }
                        return true;
                    }
                });
            }
        });
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView_all_mistake_game_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mistakeGameAdapter);
        recyclerView.setHasFixedSize(true);
        mistakeGameAdapter.startListening();
        checkIfHaveData();
    }


    private void checkMiddleTypeAndCreate(String gameName) {
        switch (gameName) {
            case "Opening Mistakes":
                createDefaultMiddleGameForThisGame("Unprepared",1);
                createDefaultMiddleGameForThisGame("Ignorance of opening",2);
                createDefaultMiddleGameForThisGame("Ignorance of typical opening idea or failing to apply",3);
                createDefaultMiddleGameForThisGame("Ignorance of typical opening tactic or failing to apply",4);
                createDefaultMiddleGameForThisGame("Risky material grabbing",5);
                createDefaultMiddleGameForThisGame("Failing to punish Risky material grabbing",6);
                createDefaultMiddleGameForThisGame("Bad development",7);
                createDefaultMiddleGameForThisGame("Failing to punish Bad development",8);
                break;
            case "Strategical Mistakes":
                createDefaultMiddleGameForThisGame("Missed: Pawn break or pawn push",1);
                createDefaultMiddleGameForThisGame("Prophylaxis",2);
                createDefaultMiddleGameForThisGame("Bad exchange",3);
                createDefaultMiddleGameForThisGame("Wrong evaluation",4);
                createDefaultMiddleGameForThisGame("Ignorance of strategical theme or failing to apply",5);
                createDefaultMiddleGameForThisGame("Missed chance to capitalize on material advantage",6);
                break;
            case "Dynamical Mistakes":
                createDefaultMiddleGameForThisGame("Passivity",1);
                createDefaultMiddleGameForThisGame("Missed Forced Move",2);
                createDefaultMiddleGameForThisGame("Bad conduct of the attack",3);
                createDefaultMiddleGameForThisGame("Failing to punish a bad conducted attack",4);
                createDefaultMiddleGameForThisGame("Bad conduct of the defense",5);
                createDefaultMiddleGameForThisGame("Failing to punish a bad conducted defense",6);
                createDefaultMiddleGameForThisGame("Risky approach",7);
                createDefaultMiddleGameForThisGame("Complicated for no reason in strategical won position",8);
                createDefaultMiddleGameForThisGame("Didn't search for dynamics in bad strategical position",9);
                createDefaultMiddleGameForThisGame("Weak calculation",10);
                break;
            case "Tactics":
                createDefaultMiddleGameForThisGame("Blunder",1);
                createDefaultMiddleGameForThisGame("Double attack",2);
                createDefaultMiddleGameForThisGame("Discovery",3);
                createDefaultMiddleGameForThisGame("Pin",4);
                createDefaultMiddleGameForThisGame("Skewer",5);
                createDefaultMiddleGameForThisGame("Trapped piece",6);
                createDefaultMiddleGameForThisGame("Overloaded piece",7);
                createDefaultMiddleGameForThisGame("Chasing material",8);
                createDefaultMiddleGameForThisGame("Deflection",9);
                createDefaultMiddleGameForThisGame("Decoy",10);
                createDefaultMiddleGameForThisGame("Interception",11);
                createDefaultMiddleGameForThisGame("Obstruction",12);
                createDefaultMiddleGameForThisGame("Clearance (Square, line, diagonal)",13);
                createDefaultMiddleGameForThisGame("X-ray",14);
                createDefaultMiddleGameForThisGame("Under promotion",15);
                createDefaultMiddleGameForThisGame("Tempo gain",16);
                createDefaultMiddleGameForThisGame("Zwischenzug",17);
                createDefaultMiddleGameForThisGame("Zwugzwang",18);
                createDefaultMiddleGameForThisGame("Desperato",19);
                break;
            case "Endgame":
                createDefaultMiddleGameForThisGame("Ignorance of endgame",1);
                createDefaultMiddleGameForThisGame("Didn't activate the King",2);
                createDefaultMiddleGameForThisGame("Didn't activate the Rook",3);
                createDefaultMiddleGameForThisGame("Passive play",4);
                createDefaultMiddleGameForThisGame("Didn't do Prophylaxis",5);
                createDefaultMiddleGameForThisGame("Didn't play for creating a passed pawn",6);
                createDefaultMiddleGameForThisGame("Didn't try to make use of a passed pawn",7);
                createDefaultMiddleGameForThisGame("Should undermine opponent's structure",8);
                createDefaultMiddleGameForThisGame("Didn't use all pieces",9);
                createDefaultMiddleGameForThisGame("Didn't look for opponent's threats",10);
                createDefaultMiddleGameForThisGame("Didn't look for opponent's weaknesses",11);
                createDefaultMiddleGameForThisGame("Overestimated opponent's weaknesses",12);
                createDefaultMiddleGameForThisGame("Didn't play for two weaknesses",13);
                createDefaultMiddleGameForThisGame("Didn't open the position for the two bishops",14);
                createDefaultMiddleGameForThisGame("Didn't do the right exchange",15);
                createDefaultMiddleGameForThisGame("Allowed zugzwang",16);
                createDefaultMiddleGameForThisGame("Allowed a fortress",17);
                createDefaultMiddleGameForThisGame("Allowed opponent to trap or restrict my pieces",18);
                createDefaultMiddleGameForThisGame("Didn't play for transforming the advantage",19);
                break;
            case "Psychological and other types":
                createDefaultMiddleGameForThisGame("Afraid to sacrifice for compensation",1);
                createDefaultMiddleGameForThisGame("Bad time management",2);
                createDefaultMiddleGameForThisGame("Underestimating the opponent",3);
                createDefaultMiddleGameForThisGame("Visualization ",4);
                createDefaultMiddleGameForThisGame("Lost patience in \"boring\" position",5);
                createDefaultMiddleGameForThisGame("Quiting in non-losing position",6);
                break;
        }
    }

    private void createDefaultMiddleGameForThisGame(String gameName, int sortNum) {
        Game game = new Game(accountType,"", userUid, mistakeGameUid, mistakeGameUid
                , gameName, "", "", date + "", 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0, 0,sortNum);
        firebaseFirestore.collection("MistakesGames").add(game);
    }

    private void createDefaultMiddleGame(String gameName, int tag, int sortNum) {
        Game game = new Game(accountType,userUid, "", "", ""
                , gameName, "", "", date + "", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0, 0,sortNum);
        if (tag == 0)
            firebaseFirestore.collection("MistakesGames").add(game);
        else
            firebaseFirestore.collection("MistakesGames").add(game).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    getAllMistakesGames();
                }
            });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.mistakes_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings)
            mistakeGameBottomSheet();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllMistakesGames();
    }
}