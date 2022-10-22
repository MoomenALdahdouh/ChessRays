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
import com.moomen.chessrays.adapter.ThemesGamesAdapter;
import com.moomen.chessrays.model.DashboardViewModel;
import com.moomen.chessrays.model.Game;
import com.moomen.chessrays.model.StackGameModel;
import com.moomen.chessrays.ui.Activity.ViewThemesGame;
import com.moomen.chessrays.utils.MovesIdStack;
import com.moomen.chessrays.utils.PreferenceUtils;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.Calendar;

public class ThemesFragment extends Fragment {

    public static final String MISTAKES_GAME_UID = "MISTAKES_GAME_UID";
    public static final String MISTAKES_GAME_NAME = "MISTAKES_GAME_NAME";
    public static final String USER_UID = "USER_UID";
    public static final String GAME_UID = "GAME_UID";
    private CardView cardViewCreateGame;
    private TextView themeGameTextView;
    private ImageView createGameImageView;
    private ProgressBar progressBar;
    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetDialog bottomSheetDialogVisitGame;

    private DashboardViewModel dashboardViewModel;
    private View root;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;

    private String color = "White";
    private String firstMove = "";
    private String themeType = "";
    private String date;
    private String userUid;
    private String gameUid;
    private String themeGameUid;

    private ConstraintLayout constraintLayoutLazyLoader;
    private ConstraintLayout constraintLayoutNoData;
    private LazyLoader lazyLoader;
    private String accountType;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        root = inflater.inflate(R.layout.fragment_themes, container, false);
        setHasOptionsMenu(true);

        lazyLoader = root.findViewById(R.id.lazyLoader_id);
        constraintLayoutLazyLoader = root.findViewById(R.id.constraintLayout_lazyLoader_id);
        constraintLayoutNoData = root.findViewById(R.id.constraintLayout_no_data_id);
        preparProgress();
        accountType = PreferenceUtils.getAccountType(getContext());
        date = DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userUid = PreferenceUtils.getUid(getContext());

        firebaseFirestore.collection("ThemesGames")
                .whereEqualTo("userId", userUid)
                .whereEqualTo("accountType", accountType)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult().isEmpty()) {
                    createDefaultMiddleGame("Theme types", 1,1);
                } else {
                    constraintLayoutLazyLoader.setVisibility(View.GONE);
                    getAllThemesGames();
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
                FirebaseFirestore.getInstance().collection("ThemesGames")
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

    @SuppressLint("SimpleDateFormat")
    public void themeGameBottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.create_themes_game, root.findViewById(R.id.theme_game_layout_id));
        progressBar = view.findViewById(R.id.progressBar_id);
        progressBar.setVisibility(View.GONE);
        Button create = (Button) view.findViewById(R.id.button_create_theme_game_id);
        Spinner spinnerColor = view.findViewById(R.id.spinner_color_id);
        spinnerCreate(spinnerColor);
        EditText themeTypeEditText = view.findViewById(R.id.editText_first_move_id);
        //date = Calendar.getInstance().getTime();
        ImageView back = view.findViewById(R.id.image_back_id);
        String userId = PreferenceUtils.getID(getContext());
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                themeType = themeTypeEditText.getText().toString().trim();
                if (themeType.isEmpty()) {
                    themeTypeEditText.setError("The theme type is required!");
                    themeTypeEditText.requestFocus();
                    return;
                }
                //getUserUid(userId);
                createNewThemesGame();
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

    private void createNewThemesGame() {
        Game game = new Game(accountType,userUid, "", "", ""
                , themeType, "", "", date + "", 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0,0,0,0,0,35);
        firebaseFirestore.collection("ThemesGames").add(game).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    //themeGameUid = task.getResult().getId();
                    //Intent intent = new Intent(getContext(), ViewThemesGame.class);
                    //intent.putExtra(MISTAKES_GAME_UID, themeGameUid);
                    //intent.putExtra(USER_UID, userUid);
                    //startActivity(intent);
                    Toast.makeText(getContext(), "Created", Toast.LENGTH_SHORT).show();
                    bottomSheetDialog.dismiss();
                    getAllThemesGames();
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

    private void getAllThemesGames() {
        Query query = FirebaseFirestore.getInstance()
                .collection("ThemesGames")
                .whereEqualTo("userId", userUid)
                .whereEqualTo("accountType", accountType)
                .orderBy("sortNum", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Game> options = new FirestoreRecyclerOptions.Builder<Game>()
                .setQuery(query, Game.class)
                .build();
        ThemesGamesAdapter themeGameAdapter = new ThemesGamesAdapter(options);
        //click on item in the recycler view
        themeGameAdapter.onItemSetOnClickListener(new ThemesGamesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position, PopupMenu popupMenu) {
                themeGameUid = documentSnapshot.getId();
                //getThemesGameUid(gameUid);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Game game = documentSnapshot.toObject(Game.class);
                        switch (item.getItemId()) {
                            case R.id.popup_view_item_id:
                                themeGameUid = documentSnapshot.getId();
                                //Create default sub middle in this middle if not created before
                                firebaseFirestore.collection("ThemesGames").whereEqualTo("thisGameId", themeGameUid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.getResult().isEmpty())
                                            checkMiddleTypeAndCreate();
                                    }
                                });

                                //Bush this game data in stack and intent to view
                                StackGameModel stackGameModel = new StackGameModel(themeGameUid, "");
                                MovesIdStack.pushStack(stackGameModel);
                                Intent intent = new Intent(getContext(), ViewThemesGame.class);
                                intent.putExtra(MISTAKES_GAME_UID, themeGameUid);
                                intent.putExtra(MISTAKES_GAME_NAME, game.getGameName());
                                intent.putExtra(GAME_UID, themeGameUid);
                                intent.putExtra(USER_UID, userUid);
                                startActivity(intent);
                                break;
                            case R.id.popup_delete_item_id:
                                firebaseFirestore.collection("ThemesGames").whereEqualTo("gameId", themeGameUid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (!task.getResult().isEmpty()) {
                                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                String gameId = queryDocumentSnapshot.getId();
                                                firebaseFirestore.collection("ThemesGames").document(gameId).delete();
                                            }
                                        }
                                        firebaseFirestore.collection("ThemesGames").document(themeGameUid).delete();
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
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView_all_theme_game_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(themeGameAdapter);
        recyclerView.setHasFixedSize(true);
        themeGameAdapter.startListening();
        checkIfHaveData();
    }


    private void checkMiddleTypeAndCreate() {
        createDefaultMiddleGameForThisGame("Attack", 1);
        createDefaultMiddleGameForThisGame("Defense", 2);
        createDefaultMiddleGameForThisGame("Opposite Castles", 3);
        createDefaultMiddleGameForThisGame("Two bishops (mine)", 4);
        createDefaultMiddleGameForThisGame("Two bishops (opponent)", 5);
        createDefaultMiddleGameForThisGame("Bishop vs Knight", 6);
        createDefaultMiddleGameForThisGame("Knight vs Bishop", 7);
        createDefaultMiddleGameForThisGame("Space (mine)", 8);
        createDefaultMiddleGameForThisGame("Space (opponent)", 9);
        createDefaultMiddleGameForThisGame("Strong center (mine)", 10);
        createDefaultMiddleGameForThisGame("Strong center (opponent)", 11);
        createDefaultMiddleGameForThisGame("Minority Attack (mine)", 12);
        createDefaultMiddleGameForThisGame("Minority Attack (opponent)", 13);
        createDefaultMiddleGameForThisGame("Passed Pawn (mine)", 14);
        createDefaultMiddleGameForThisGame("Passed Pawn (opponent)", 15);
        createDefaultMiddleGameForThisGame("Isolani (mine)", 16);
        createDefaultMiddleGameForThisGame("Isolani (opponent)", 17);
        createDefaultMiddleGameForThisGame("Isolated pawn (mine)", 18);
        createDefaultMiddleGameForThisGame("Isolated pawn (opponent)", 19);
        createDefaultMiddleGameForThisGame("Backward pawn (mine)", 20);
        createDefaultMiddleGameForThisGame("Backward pawn (opponent)", 21);
        createDefaultMiddleGameForThisGame("Doubled pawns (mine)", 22);
        createDefaultMiddleGameForThisGame("Doubled pawns (opponent)", 23);
    }

    private void createDefaultMiddleGameForThisGame(String gameName, int sortNum) {
        Game game = new Game(accountType,"", userUid, themeGameUid, themeGameUid
                , gameName, "", "", date + "", 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0, 0,sortNum);
        firebaseFirestore.collection("ThemesGames").add(game);
    }

    private void createDefaultMiddleGame(String gameName, int tag, int sortNum) {
        Game game = new Game(accountType,userUid, "", "", ""
                , gameName, "", "", date + "", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0, 0,sortNum);
        if (tag == 0)
            firebaseFirestore.collection("ThemesGames").add(game);
        else
            firebaseFirestore.collection("ThemesGames").add(game).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    getAllThemesGames();
                }
            });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.themes_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings)
            themeGameBottomSheet();
        return super.onOptionsItemSelected(item);
    }
}