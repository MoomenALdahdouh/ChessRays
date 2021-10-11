package com.moomen.chessrays.ui.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.moomen.chessrays.adapter.OpeningGameAdapter;
import com.moomen.chessrays.adapter.OpeningGamesAdapter;
import com.moomen.chessrays.model.DashboardViewModel;
import com.moomen.chessrays.model.Game;
import com.moomen.chessrays.model.OpeningHandling;
import com.moomen.chessrays.model.OpeningModel;
import com.moomen.chessrays.model.OpeningGame;
import com.moomen.chessrays.model.User;
import com.moomen.chessrays.ui.Activity.ViewOpeningGame;
import com.moomen.chessrays.utils.MovesIdStack;
import com.moomen.chessrays.utils.PreferenceUtils;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

public class OpeningFragment extends Fragment {

    public static final String OPENING_GAME_UID = "OPENING_GAME_UID";
    public static final String OPENING_GAME_MOVE = "OPENING_GAME_MOVE";
    public static final String USER_UID = "USER_UID";
    public static final String GAME_UID = "GAME_UID";
    private CardView cardViewCreateGame;
    private TextView openingGameTextView;
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
    private String date;
    private Game game = new Game();
    private String userUid;
    private String gameUid;
    private String openingGameUid;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        root = inflater.inflate(R.layout.fragment_opening, container, false);

        cardViewCreateGame = root.findViewById(R.id.cardView_create_game_id);
        createGameImageView = root.findViewById(R.id.imageView_create_game_id);
        openingGameTextView = root.findViewById(R.id.textView_open_game_id);

        //onClickListener(createGameImageView);
        //onClickListener(openingGameTextView);
        cardViewCreateGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openingGame();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //getGames();
        getAllOpeningGames();
        return root;
    }

    @SuppressLint("NonConstantResourceId")
    private void onClickListener(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (view.getId()) {
                    case R.id.imageView_create_game_id:
                        if (cardViewCreateGame.getVisibility() == View.GONE) {
                            cardViewCreateGame.setVisibility(View.VISIBLE);
                        } else
                            cardViewCreateGame.setVisibility(View.GONE);
                        break;
                    case R.id.textView_open_game_id:
                        //startActivity(new Intent(getContext(), OpeningGame.class));
                        cardViewCreateGame.setVisibility(View.GONE);
                        openingGame();
                        break;
                }
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    public void openingGame() {
        bottomSheetDialog = new BottomSheetDialog(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.create_opening_game, root.findViewById(R.id.opening_game_layout_id));
        progressBar = view.findViewById(R.id.progressBar_id);
        progressBar.setVisibility(View.GONE);
        Button create = (Button) view.findViewById(R.id.button_create_opening_game_id);
        Spinner spinnerColor = view.findViewById(R.id.spinner_color_id);
        spinnerCreate(spinnerColor);
        EditText firstMoveEditText = view.findViewById(R.id.editText_first_move_id);
        date = DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
        //date = Calendar.getInstance().getTime();
        ImageView back = view.findViewById(R.id.image_back_id);
        String userId = PreferenceUtils.getID(getContext());
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                firstMove = firstMoveEditText.getText().toString().trim();
                if (firstMove.isEmpty()) {
                    firstMoveEditText.setError("The first move is required!");
                    firstMoveEditText.requestFocus();
                    return;
                }
                //getUserUid(userId);
                createNewOpeningGame();
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

    private void getUserUid(String userId) {
        firebaseFirestore.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        User user = d.toObject(User.class);
                        if (user.getUserID().equals(userId)) {
                            userUid = d.getId();
                            game = new Game(userUid, date + "", "opening", 0, 0, 0, 0);
                            //createNewGame(game);
                            break;
                        }
                    }
                }
            }
        });
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

    private void createNewGame() {
        userUid = PreferenceUtils.getUid(getContext());
        //userUid = firebaseAuth.getCurrentUser().getUid();
        game = new Game(userUid, date + "", "opening", 0, 0, 0, 0);
        //createNewGame(game);
        firebaseFirestore.collection("Games").add(game).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    gameUid = task.getResult().getId();
                    // OpeningGame openingGame = new OpeningGame(userUid, gameUid,"","", color, firstMove, date + "", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                    //createNewOpeningGame(openingGame);
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

    private void createNewOpeningGame() {
        userUid = PreferenceUtils.getUid(getContext());
        OpeningGame openingGame = new OpeningGame(userUid, "", "", ""
                , "", color, firstMove, date + "", 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0);
        firebaseFirestore.collection("OpeningGame").add(openingGame).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    //openingGameUid = task.getResult().getId();
                    //Intent intent = new Intent(getContext(), ViewOpeningGame.class);
                    //intent.putExtra(OPENING_GAME_UID, openingGameUid);
                    //intent.putExtra(USER_UID, userUid);
                    //startActivity(intent);
                    Toast.makeText(getContext(), "Created", Toast.LENGTH_SHORT).show();
                    bottomSheetDialog.dismiss();
                    getAllOpeningGames();
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

    private void getAllOpeningGames() {
        userUid = PreferenceUtils.getUid(getContext());
        Query query = FirebaseFirestore.getInstance().collection("OpeningGame").whereEqualTo("userId", userUid);
        FirestoreRecyclerOptions<OpeningGame> options = new FirestoreRecyclerOptions.Builder<OpeningGame>()
                .setQuery(query, OpeningGame.class)
                .build();
        OpeningGamesAdapter openingGameAdapter = new OpeningGamesAdapter(options);
        //click on item in the recycler view
        openingGameAdapter.onItemSetOnClickListener(new OpeningGamesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position, PopupMenu popupMenu) {
                String openingGameUid = documentSnapshot.getId();
                //getOpeningGameUid(gameUid);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        OpeningGame openingGame = documentSnapshot.toObject(OpeningGame.class);
                        switch (item.getItemId()) {
                            case R.id.popup_view_item_id:
                                OpeningModel openingModel = new OpeningModel(openingGameUid, "");
                                MovesIdStack.pushStack(openingModel);
                                Intent intent = new Intent(getContext(), ViewOpeningGame.class);
                                intent.putExtra(OPENING_GAME_UID, openingGameUid);
                                intent.putExtra(OPENING_GAME_MOVE, openingGame.getFirstMove());
                                intent.putExtra(GAME_UID, openingGameUid);
                                intent.putExtra(USER_UID, userUid);
                                startActivity(intent);
                                break;
                            case R.id.popup_delete_item_id:
                                firebaseFirestore.collection("OpeningGame").whereEqualTo("gameId", openingGameUid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (!task.getResult().isEmpty()) {
                                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                String gameId = queryDocumentSnapshot.getId();
                                                firebaseFirestore.collection("OpeningGame").document(gameId).delete();
                                            }
                                        }
                                        firebaseFirestore.collection("OpeningGame").document(openingGameUid).delete();
                                    }
                                });
                                break;
                        }
                        return true;
                    }
                });
            }
        });
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView_all_opening_game_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(openingGameAdapter);
        recyclerView.setHasFixedSize(true);
        openingGameAdapter.startListening();
    }

    private void getOpeningGameUid(String gameUid) {
        firebaseFirestore.collection("OpeningGame").whereEqualTo("gameId", gameUid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    openingGameUid = queryDocumentSnapshot.getId();
                }
                Intent intent = new Intent(getContext(), ViewOpeningGame.class);
                intent.putExtra(OPENING_GAME_UID, openingGameUid);
                intent.putExtra(USER_UID, userUid);
                startActivity(intent);
            }
        });
    }

    private void visitGameBottomSheet(String gameUid) {
        bottomSheetDialog = new BottomSheetDialog(getContext());
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.visit_game_bottom_sheet_layout, null);
        getAllGameForThisOpeningGame(gameUid, view);
        ImageView back = view.findViewById(R.id.image_back_id);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    private void getAllGameForThisOpeningGame(String gameUid, View view) {
        Query query = FirebaseFirestore.getInstance().collection("OpeningGame").whereEqualTo("gameId", gameUid)
                .orderBy("date", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<OpeningGame> options = new FirestoreRecyclerOptions.Builder<OpeningGame>()
                .setQuery(query, OpeningGame.class)
                .build();
        OpeningGameAdapter openingGameAdapter = new OpeningGameAdapter(options);
        openingGameAdapter.onItemSetOnClickListener(new OpeningGameAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_opening_game_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(openingGameAdapter);
        openingGameAdapter.startListening();
    }
}