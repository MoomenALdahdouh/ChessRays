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
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
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
import com.moomen.chessrays.adapter.MiddleGamesAdapter;
import com.moomen.chessrays.model.DashboardViewModel;
import com.moomen.chessrays.model.Game;
import com.moomen.chessrays.model.StackGameModel;
import com.moomen.chessrays.ui.Activity.ViewMiddleGame;
import com.moomen.chessrays.utils.MovesIdStack;
import com.moomen.chessrays.utils.PreferenceUtils;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.Calendar;

public class MiddleFragment extends Fragment {
    public static final String MIDDLE_GAME_UID = "MIDDLE_GAME_UID";
    public static final String MIDDLE_GAME_NAME = "MIDDLE_GAME_NAME";
    public static final String USER_UID = "USER_UID";
    public static final String GAME_UID = "GAME_UID";

    private ConstraintLayout constraintLayoutLazyLoader;
    private ConstraintLayout constraintLayoutNoData;
    private LazyLoader lazyLoader;
    private ProgressBar progressBar;
    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetDialog bottomSheetDialogVisitGame;
    private RecyclerView recyclerView;
    private DashboardViewModel dashboardViewModel;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;

    private String gameName = "";
    private String date;
    private String userUid;
    private String gameUid;
    private String middleGameUid;
    private String accountType;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_middle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        recyclerView = view.findViewById(R.id.recyclerView_all_middle_game_id);
        lazyLoader = view.findViewById(R.id.lazyLoader_id);
        constraintLayoutLazyLoader = view.findViewById(R.id.constraintLayout_lazyLoader_id);
        constraintLayoutNoData = view.findViewById(R.id.constraintLayout_no_data_id);
        constraintLayoutNoData.setVisibility(View.GONE);
        preparProgress();

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        date = DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
        userUid = PreferenceUtils.getUid(getContext());
        accountType = PreferenceUtils.getAccountType(getContext());
        firebaseFirestore.collection("MiddleGames")
                .whereEqualTo("userId", userUid)
                .whereEqualTo("accountType", accountType)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult().isEmpty()) {
                    createDefaultMiddleGame("Strategic", 0, 1);
                    createDefaultMiddleGame("Tactical", 0, 2);
                    createDefaultMiddleGame("Chaos", 1, 3);
                } else {
                    constraintLayoutLazyLoader.setVisibility(View.GONE);
                    getAllMiddleGames();
                }
            }
        });
        checkIfHaveData();
    }

    private void checkIfHaveData() {
        constraintLayoutLazyLoader.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseFirestore.getInstance().collection("MiddleGames")
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
    public void middleGameBottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.create_middle_game, null);
        progressBar = view.findViewById(R.id.progressBar_id);
        progressBar.setVisibility(View.GONE);
        Button create = (Button) view.findViewById(R.id.button_create_middle_game_id);

        EditText middleNameEditText = view.findViewById(R.id.textView_middle_name);
        ImageView back = view.findViewById(R.id.image_back_id);
        String userId = PreferenceUtils.getID(getContext());
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                gameName = middleNameEditText.getText().toString().trim();
                if (gameName.isEmpty()) {
                    middleNameEditText.setError("The first move is required!");
                    middleNameEditText.requestFocus();
                    return;
                }
                createNewMiddleGame();
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

    private void createNewMiddleGame() {
        Game middleGame = new Game(accountType,userUid, "", "", ""
                , gameName, "", "", date + "", 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0, 0, 20);
        firebaseFirestore.collection("MiddleGames").add(middleGame).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Created", Toast.LENGTH_SHORT).show();
                    bottomSheetDialog.dismiss();
                    getAllMiddleGames();
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

    private void getAllMiddleGames() {
        Query query = FirebaseFirestore.getInstance()
                .collection("MiddleGames")
                .whereEqualTo("userId", userUid)
                .whereEqualTo("accountType", accountType)
                .orderBy("sortNum", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Game> options = new FirestoreRecyclerOptions.Builder<Game>()
                .setQuery(query, Game.class)
                .build();
        MiddleGamesAdapter middleGameAdapter = new MiddleGamesAdapter(options);

        //click on item in the recycler view
        middleGameAdapter.onItemSetOnClickListener(new MiddleGamesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position, PopupMenu popupMenu) {
                middleGameUid = documentSnapshot.getId();
                //getMiddleGameUid(gameUid);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Game middleGame = documentSnapshot.toObject(Game.class);
                        switch (item.getItemId()) {
                            case R.id.popup_view_item_id:
                                middleGameUid = documentSnapshot.getId();
                                //Create default sub middle in this middle if not created before
                                firebaseFirestore.collection("MiddleGames").whereEqualTo("thisGameId", middleGameUid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.getResult().isEmpty())
                                            checkMiddleTypeAndCreate(middleGame.getGameName());
                                    }
                                });

                                //Bush this game data in stack and intent to view
                                StackGameModel stackGameModel = new StackGameModel(middleGameUid, "");
                                MovesIdStack.pushStack(stackGameModel);
                                Intent intent = new Intent(getContext(), ViewMiddleGame.class);
                                intent.putExtra(MIDDLE_GAME_UID, middleGameUid);
                                intent.putExtra(MIDDLE_GAME_NAME, middleGame.getGameName());
                                intent.putExtra(GAME_UID, middleGameUid);
                                intent.putExtra(USER_UID, userUid);
                                startActivity(intent);
                                break;
                            case R.id.popup_delete_item_id:
                                firebaseFirestore.collection("MiddleGames").whereEqualTo("gameId", middleGameUid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (!task.getResult().isEmpty()) {
                                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                String gameId = queryDocumentSnapshot.getId();
                                                firebaseFirestore.collection("MiddleGames").document(gameId).delete();
                                            }
                                        }
                                        firebaseFirestore.collection("MiddleGames").document(middleGameUid).delete();
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(middleGameAdapter);
        recyclerView.setHasFixedSize(true);
        middleGameAdapter.startListening();
        checkIfHaveData();
    }

    private void checkMiddleTypeAndCreate(String gameName) {
        switch (gameName) {
            case "Strategic":
                createDefaultMiddleGameForThisGame("Open Position", 1);
                createDefaultMiddleGameForThisGame("Semi-open Position", 2);
                createDefaultMiddleGameForThisGame("Closed Position", 3);
                break;
            case "Tactical":
                createDefaultMiddleGameForThisGame("Attacking the king", 1);
                createDefaultMiddleGameForThisGame("Defending the king", 2);
                createDefaultMiddleGameForThisGame("Combinative game", 3);
                break;
            case "Chaos":
                createDefaultMiddleGameForThisGame("Chaos", 1);
                break;
        }
    }

    private void createDefaultMiddleGameForThisGame(String gameName, int sortNum) {
        Game middleGame = new Game(accountType,"", userUid, middleGameUid, middleGameUid
                , gameName, "", "", date + "", 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0, 0, sortNum);
        firebaseFirestore.collection("MiddleGames").add(middleGame);
    }

    private void createDefaultMiddleGame(String gameName, int tag, int sortNum) {
        Game middleGame = new Game(accountType,userUid, "", "", ""
                , gameName, "", "", date + "", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0, 0, sortNum);
        if (tag == 0)
            firebaseFirestore.collection("MiddleGames").add(middleGame);
        else
            firebaseFirestore.collection("MiddleGames").add(middleGame).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    getAllMiddleGames();
                }
            });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.middle_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings)
            middleGameBottomSheet();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllMiddleGames();
    }
}