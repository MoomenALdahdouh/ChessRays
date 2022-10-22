package com.moomen.chessrays.ui.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
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
import com.moomen.chessrays.adapter.OpeningGamesAdapter;
import com.moomen.chessrays.model.DashboardViewModel;
import com.moomen.chessrays.model.Game;
import com.moomen.chessrays.model.StackGameModel;
import com.moomen.chessrays.ui.Activity.IntroActivity;
import com.moomen.chessrays.ui.Activity.MainActivity;
import com.moomen.chessrays.ui.Activity.SplashActivity;
import com.moomen.chessrays.ui.Activity.ViewOpeningGame;
import com.moomen.chessrays.utils.MovesIdStack;
import com.moomen.chessrays.utils.PreferenceUtils;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.Calendar;

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
    private String userUid;
    private String gameUid;
    private String openingGameUid;

    private ConstraintLayout constraintLayoutNoData;
    private ConstraintLayout constraintLayoutLazyLoader;
    private LazyLoader lazyLoader;
    private ProgressBar progressBar1;
    private String accountType;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        root = inflater.inflate(R.layout.fragment_opening, container, false);
        setHasOptionsMenu(true);

        progressBar1 = root.findViewById(R.id.progressBar);
        progressBar1.setVisibility(View.GONE);
        lazyLoader = root.findViewById(R.id.lazyLoader_id);
        constraintLayoutLazyLoader = root.findViewById(R.id.constraintLayout_lazyLoader_id);
        constraintLayoutNoData = root.findViewById(R.id.constraintLayout_no_data_id);
        preparProgress();

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        accountType = PreferenceUtils.getAccountType(getContext());

        checkIfHaveData();
        getAllOpeningGames();
        return root;
    }

    private void checkIfHaveData() {
        constraintLayoutLazyLoader.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseFirestore.getInstance().collection("OpeningGames")
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

    private void preparProgress() {
        LazyLoader loader = new LazyLoader(getContext(), 30, 20, ContextCompat.getColor(getContext(), R.color.loader_selected),
                ContextCompat.getColor(getContext(), R.color.loader_selected),
                ContextCompat.getColor(getContext(), R.color.loader_selected));
        loader.setAnimDuration(500);
        loader.setFirstDelayDuration(100);
        loader.setSecondDelayDuration(200);
        loader.setInterpolator(new LinearInterpolator());
        lazyLoader.addView(loader);
    }

    private boolean isEdit = false;
    private EditText firstMoveEditText;

    @SuppressLint("SimpleDateFormat")
    public void openingGameBootomSheet() {
        bottomSheetDialog = new BottomSheetDialog(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.create_opening_game, root.findViewById(R.id.opening_game_layout_id));
        progressBar = view.findViewById(R.id.progressBar_id);
        progressBar.setVisibility(View.GONE);
        Button create = (Button) view.findViewById(R.id.button_create_opening_game_id);
        Spinner spinnerColor = view.findViewById(R.id.spinner_color_id);
        spinnerCreate(spinnerColor);
        firstMoveEditText = view.findViewById(R.id.editText_first_move_id);
        //firstMoveEditText.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        date = DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
        if (isEdit) {
            create.setText("Edit");
            firstMoveEditText.setText(firstMove);
        }
        ImageView back = view.findViewById(R.id.image_back_id);
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
                if (!isEdit)
                    createNewOpeningGame();
                else
                    editDataOpening();
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

    private void editDataOpening() {
        firebaseFirestore.collection("OpeningGames")
                .document(openingGameUid)
                .update("color",color
                        ,"firstMove",firstMove).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                editDataOpeningChild();
                Toast.makeText(getContext(),"Edited",Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            }
        });
    }

    private void editDataOpeningChild() {
        firebaseFirestore.collection("OpeningGames").whereEqualTo("gameId", openingGameUid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.getResult().isEmpty()) {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        String gameId = queryDocumentSnapshot.getId();
                        firebaseFirestore.collection("OpeningGames")
                                .document(gameId)
                                .update("color",color,"firstMove",firstMove);
                    }
                    color = "";
                    firstMove = "";
                    isEdit = false;
                }
            }
        });
    }

    private ArrayAdapter<CharSequence> adapterSpinner;

    private void spinnerCreate(Spinner spinner) {
        if (color.equals("White"))
            adapterSpinner = ArrayAdapter.createFromResource(getContext(), R.array.color, R.layout.support_simple_spinner_dropdown_item);
        else
            adapterSpinner = ArrayAdapter.createFromResource(getContext(), R.array.colorInverse, R.layout.support_simple_spinner_dropdown_item);
        adapterSpinner.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //spinner.getSelectedItem().toString();
                if (color.equals("White")) {
                    switch (position) {
                        case 0:
                            color = "White";
                            break;
                        case 1:
                            color = "Black";
                            break;
                    }
                } else {
                    switch (position) {
                        case 0:
                            color = "Black";
                            break;
                        case 1:
                            color = "White";
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (!isEdit)
                    color = "White";
            }
        });
    }

    private void createNewOpeningGame() {
        userUid = PreferenceUtils.getUid(getContext());
        Game game = new Game(accountType,userUid, "", "", ""
                , "", color, firstMove, date + "", 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                , 0, 0, 0, 0, 0, 0, 0, 0, 20);
        firebaseFirestore.collection("OpeningGames").add(game).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
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
        Query query = FirebaseFirestore.getInstance()
                .collection("OpeningGames")
                .whereEqualTo("userId", userUid)
                .whereEqualTo("accountType", accountType);
        FirestoreRecyclerOptions<Game> options = new FirestoreRecyclerOptions.Builder<Game>()
                .setQuery(query, Game.class)
                .build();
        OpeningGamesAdapter openingGameAdapter = new OpeningGamesAdapter(options);
        //click on item in the recycler view
        openingGameAdapter.onItemSetOnClickListener(new OpeningGamesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position, PopupMenu popupMenu) {
                openingGameUid = documentSnapshot.getId();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        isEdit = false;
                        Game game = documentSnapshot.toObject(Game.class);
                        switch (item.getItemId()) {
                            case R.id.popup_view_item_id:
                                StackGameModel stackGameModel = new StackGameModel(openingGameUid, "");
                                MovesIdStack.pushStack(stackGameModel);
                                Intent intent = new Intent(getContext(), ViewOpeningGame.class);
                                intent.putExtra(OPENING_GAME_UID, openingGameUid);
                                intent.putExtra(OPENING_GAME_MOVE, game.getFirstMove());
                                intent.putExtra(GAME_UID, openingGameUid);
                                intent.putExtra(USER_UID, userUid);
                                startActivity(intent);
                                break;
                            case R.id.popup_edit_item_id:
                                isEdit = true;
                                color = "";
                                editOpening();
                                break;
                            case R.id.popup_delete_item_id:
                                firebaseFirestore.collection("OpeningGames").whereEqualTo("gameId", openingGameUid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (!task.getResult().isEmpty()) {
                                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                String gameId = queryDocumentSnapshot.getId();
                                                firebaseFirestore.collection("OpeningGames").document(gameId).delete();
                                            }
                                        }
                                        firebaseFirestore.collection("OpeningGames").document(openingGameUid).delete();
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
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView_all_opening_game_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(openingGameAdapter);
        recyclerView.setHasFixedSize(true);
        openingGameAdapter.startListening();
        checkIfHaveData();
    }

    private void editOpening() {
        firebaseFirestore.collection("OpeningGames").document(openingGameUid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                Game game = task.getResult().toObject(Game.class);
                color = game.getColor();
                firstMove = game.getFirstMove();
                openingGameBootomSheet();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.opening_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            color = "";
            firstMove = "";
            openingGameBootomSheet();
        }
        return super.onOptionsItemSelected(item);
    }
}