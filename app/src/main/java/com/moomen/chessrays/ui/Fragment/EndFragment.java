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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
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
import com.moomen.chessrays.adapter.EndGamesAdapter;
import com.moomen.chessrays.model.DashboardViewModel;
import com.moomen.chessrays.model.Game;
import com.moomen.chessrays.model.StackGameModel;
import com.moomen.chessrays.ui.Activity.ViewEndGame;
import com.moomen.chessrays.utils.MovesIdStack;
import com.moomen.chessrays.utils.PreferenceUtils;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.Calendar;

public class EndFragment extends Fragment {
    public static final String END_GAME_UID = "END_GAME_UID";
    public static final String END_GAME_NAME = "END_GAME_NAME";
    public static final String USER_UID = "USER_UID";
    public static final String GAME_UID = "GAME_UID";
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
    private String endGameUid;

    private ConstraintLayout constraintLayoutLazyLoader;
    private ConstraintLayout constraintLayoutNoData;
    private LazyLoader lazyLoader;
    private String accountType;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_end, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userUid = PreferenceUtils.getUid(getContext());
        date = DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
        lazyLoader = view.findViewById(R.id.lazyLoader_id);
        constraintLayoutLazyLoader = view.findViewById(R.id.constraintLayout_lazyLoader_id);
        constraintLayoutNoData = view.findViewById(R.id.constraintLayout_no_data_id);
        preparProgress();
        accountType = PreferenceUtils.getAccountType(getContext());
        recyclerView = view.findViewById(R.id.recyclerView_all_game_id);
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("EndGames")
                .whereEqualTo("userId", userUid)
                .whereEqualTo("accountType", accountType)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult().isEmpty()) {
                    createDefaultEndGame("Pawns", 0,1);
                    createDefaultEndGame("Knights", 0,2);
                    createDefaultEndGame("Bishops", 0,3);
                    createDefaultEndGame("Bishops vs Knights", 0,4);
                    createDefaultEndGame("Rooks", 0,5);
                    createDefaultEndGame("Queens", 0,6);
                    createDefaultEndGame("Mixed Endgames", 1,7);
                } else {
                    constraintLayoutLazyLoader.setVisibility(View.GONE);
                    getAllEndGames();
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
                FirebaseFirestore.getInstance().collection("EndGames")
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
    public void endGameBottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.create_end_game, null);
        progressBar = view.findViewById(R.id.progressBar_id);
        progressBar.setVisibility(View.GONE);
        Button create = (Button) view.findViewById(R.id.button_create_game_id);

        EditText endNameEditText = view.findViewById(R.id.textView_name);

        //date = Calendar.getInstance().getTime();
        ImageView back = view.findViewById(R.id.image_back_id);
        String userId = PreferenceUtils.getID(getContext());
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                gameName = endNameEditText.getText().toString().trim();
                if (gameName.isEmpty()) {
                    endNameEditText.setError("The first move is required!");
                    endNameEditText.requestFocus();
                    return;
                }
                createNewEndGame();
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

    private void createNewEndGame() {
        Game endGame = new Game(accountType,userUid, "", "", ""
                , gameName, "", "", date + "", 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0,0,0,0,0,0,0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0, 0, 20);
        firebaseFirestore.collection("EndGames").add(endGame).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Created", Toast.LENGTH_SHORT).show();
                    bottomSheetDialog.dismiss();
                    getAllEndGames();
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

    private void getAllEndGames() {
        Query query = FirebaseFirestore.getInstance()
                .collection("EndGames")
                .whereEqualTo("userId", userUid)
                .whereEqualTo("accountType", accountType)
                .orderBy("sortNum",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Game> options = new FirestoreRecyclerOptions.Builder<Game>()
                .setQuery(query, Game.class)
                .build();
        EndGamesAdapter endGameAdapter = new EndGamesAdapter(options);
        //click on item in the recycler view
        endGameAdapter.onItemSetOnClickListener(new EndGamesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position, PopupMenu popupMenu) {
                endGameUid = documentSnapshot.getId();
                //getEndGameUid(gameUid);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Game endGame = documentSnapshot.toObject(Game.class);
                        switch (item.getItemId()) {
                            case R.id.popup_view_item_id:
                                endGameUid = documentSnapshot.getId();
                                //Create default sub middle in this middle if not created before
                                firebaseFirestore.collection("EndGames").whereEqualTo("thisGameId", endGameUid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.getResult().isEmpty())
                                            checkEndGameTypeAndCreate(endGame.getGameName());
                                    }
                                });
                                //Bush this game data in stack and intent to view
                                StackGameModel stackGameModel = new StackGameModel(endGameUid, "");
                                MovesIdStack.pushStack(stackGameModel);
                                Intent intent = new Intent(getContext(), ViewEndGame.class);
                                intent.putExtra(END_GAME_UID, endGameUid);
                                intent.putExtra(END_GAME_NAME, endGame.getGameName());
                                intent.putExtra(GAME_UID, endGameUid);
                                intent.putExtra(USER_UID, userUid);
                                startActivity(intent);
                                break;
                            case R.id.popup_delete_item_id:
                                firebaseFirestore.collection("EndGames").whereEqualTo("gameId", endGameUid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (!task.getResult().isEmpty()) {
                                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                String gameId = queryDocumentSnapshot.getId();
                                                firebaseFirestore.collection("EndGames").document(gameId).delete();
                                            }
                                        }
                                        firebaseFirestore.collection("EndGames").document(endGameUid).delete();
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
        recyclerView.setAdapter(endGameAdapter);
        recyclerView.setHasFixedSize(true);
        endGameAdapter.startListening();
        checkIfHaveData();
    }

    private void createDefaultEndGame(String gameName, int tag, int sortNum) {
        Game middleGame = new Game(accountType,userUid, "", "", ""
                , gameName, "", "", date + "", 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0, 0, sortNum);
        if (tag == 0)
            firebaseFirestore.collection("EndGames").add(middleGame);
        else
            firebaseFirestore.collection("EndGames").add(middleGame).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    getAllEndGames();
                }
            });
    }

    private void checkEndGameTypeAndCreate(String gameName) {
        switch (gameName) {
            case "Pawns":
                createDefaultEndGameForThisGame("Races", 1);
                createDefaultEndGameForThisGame("Maneuvering", 2);
                break;
            case "Knights":
                createDefaultEndGameForThisGame("Knight vs Pawns", 1);
                createDefaultEndGameForThisGame("Knights & Pawns", 2);
                break;
            case "Bishops":
                createDefaultEndGameForThisGame("Bishop vs Pawns", 1);
                createDefaultEndGameForThisGame("Opposite color Bishops", 2);
                createDefaultEndGameForThisGame("Same color Bishops", 3);
                break;
            case "Bishops vs Knights":
                createDefaultEndGameForThisGame("Pawns in one area", 1);
                createDefaultEndGameForThisGame("Pawns in center and a wing", 2);
                createDefaultEndGameForThisGame("Pawns  on two wings", 3);
                break;
            case "Rooks":
                createDefaultEndGameForThisGame("Rook vs Pawns", 1);
                createDefaultEndGameForThisGame("Rooks and Pawns", 2);
                createDefaultEndGameForThisGame("Rook vs Bishop", 3);
                createDefaultEndGameForThisGame("Rook vs Knight", 4);
                createDefaultEndGameForThisGame("Rook vs Two minor pieces", 5);
                break;
            case "Queens":
                createDefaultEndGameForThisGame("Queen vs Pawns", 1);
                createDefaultEndGameForThisGame("Queens and Pawns", 2);
                createDefaultEndGameForThisGame("Queen vs Two Rooks", 3);
                createDefaultEndGameForThisGame("Queen vs Rook", 4);
                createDefaultEndGameForThisGame("Queen vs Rook and knight", 5);
                createDefaultEndGameForThisGame("Queen vs Rook and Bishop", 6);
                break;
        }
    }

    private void createDefaultEndGameForThisGame(String gameName, int sortNum) {
        Game endGame = new Game(accountType,"", userUid, endGameUid, endGameUid
                , gameName, "", "", date + "", 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0, 0, sortNum);
        firebaseFirestore.collection("EndGames").add(endGame);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.end_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings)
            endGameBottomSheet();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllEndGames();
    }
}