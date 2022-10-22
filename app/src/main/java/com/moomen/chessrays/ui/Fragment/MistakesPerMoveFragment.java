package com.moomen.chessrays.ui.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.LazyLoader;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.graphics.vector.Stroke;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.mikephil.charting.charts.LineChart;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.moomen.chessrays.adapter.MistakesInMovesGamesAdapter;
import com.moomen.chessrays.model.ChartGame;
import com.moomen.chessrays.model.Game;
import com.moomen.chessrays.model.InstallNewGame;
import com.moomen.chessrays.model.MistakeInMoves;
import com.moomen.chessrays.model.StackGameModel;
import com.moomen.chessrays.ui.Activity.ViewMiddleGame;
import com.moomen.chessrays.utils.MovesIdStack;
import com.moomen.chessrays.utils.PreferenceUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class MistakesPerMoveFragment extends Fragment {

    private ArrayList<DataEntry> dataEntryArrayList;
    private Cartesian cartesian;
    private AnyChartView anyChartView;

    private BottomSheetDialog bottomSheetDialog;
    private ProgressBar progressBar;
    private Button buttonCreate;
    private ImageView buttonBack;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;

    private String userUid;
    private RecyclerView recyclerView;
    private View view;

    private Set set;
    private Mapping series1Mapping;
    private Line series1;

    private ArrayList<MistakeInMoves> mistakeInMovesArrayList;
    private EditText editTextNumberOfMistake;
    private EditText editTextNumberOfMove;
    private int numberOfMistake = 0;
    private int numberOfMove = 0;
    private boolean isEdit = false;
    private String mistakeEditUid;
    private TextView titleBottomSheet;

    private ConstraintLayout constraintLayoutLazyLoader;
    private ConstraintLayout constraintLayoutNoData;
    private ConstraintLayout constraintLayoutNoDataChart;
    private LazyLoader lazyLoader;
    private String accountType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mistakes_per_move, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        recyclerView = view.findViewById(R.id.recyclerView_mistakes_move_id);
        anyChartView = view.findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(view.findViewById(R.id.progress_bar));
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userUid = PreferenceUtils.getUid(getContext());

        lazyLoader = view.findViewById(R.id.lazyLoader_id);
        constraintLayoutLazyLoader = view.findViewById(R.id.constraintLayout_lazyLoader_id);
        constraintLayoutNoData = view.findViewById(R.id.constraintLayout_no_data_id);
        constraintLayoutNoData.setVisibility(View.GONE);
        constraintLayoutNoDataChart = view.findViewById(R.id.constraintLayout_no_data_chart_id);
        constraintLayoutNoDataChart.setVisibility(View.GONE);
        preparProgress();

        accountType = PreferenceUtils.getAccountType(getContext());
        cartesian = AnyChart.line();
        cartesian.title("Mistakes in moves");
        cartesian.yAxis(0).title("Number of mistakes");
        cartesian.xAxis(0).title("Move");
        cartesian.animation(true);
        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);
        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        set = Set.instantiate();
        series1Mapping = set.mapAs("{ x: 'x' }");
        series1 = cartesian.line(series1Mapping);
        series1.name("Mistake");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);

        anyChartView.setChart(cartesian);

        createOneHundredMistakes();
        getAllMoveMistakes();
        getAllMistakeAndFillRecycle();
        checkIfHaveData();
    }

    private void checkIfHaveData() {
        constraintLayoutLazyLoader.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseFirestore.getInstance().collection("MistakesInMoves")
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

    private void createOneHundredMistakes() {
        constraintLayoutLazyLoader.setVisibility(View.VISIBLE);
        firebaseFirestore.collection("MistakesInMoves")
                .whereEqualTo("userId", userUid)
                .whereEqualTo("accountType", accountType)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult().isEmpty()) {
                    for (int i = 0; i < 100; i++) {
                        MistakeInMoves mistakeInMoves = new MistakeInMoves(i + 1, 0, userUid,accountType);
                        firebaseFirestore.collection("MistakesInMoves").add(mistakeInMoves);
                    }
                    constraintLayoutLazyLoader.setVisibility(View.GONE);
                    getAllMistakeAndFillRecycle();
                    getAllMoveMistakes();
                } else
                    constraintLayoutLazyLoader.setVisibility(View.GONE);
            }
        });
    }

    private void getAllMistakeAndFillRecycle() {
        Query query = FirebaseFirestore.getInstance()
                .collection("MistakesInMoves")
                .whereEqualTo("userId", userUid)
                .whereEqualTo("accountType", accountType)
                .orderBy("numberOfMove", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<MistakeInMoves> options = new FirestoreRecyclerOptions.Builder<MistakeInMoves>()
                .setQuery(query, MistakeInMoves.class)
                .build();
        MistakesInMovesGamesAdapter mistakesInMovesGamesAdapter = new MistakesInMovesGamesAdapter(options);
        //click on item in the recycler view
        mistakesInMovesGamesAdapter.onItemSetOnClickListener(new MistakesInMovesGamesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position, PopupMenu popupMenu) {
                isEdit = false;
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        MistakeInMoves mistakeInMoves = documentSnapshot.toObject(MistakeInMoves.class);
                        mistakeEditUid = documentSnapshot.getId();
                        if (item.getItemId() == R.id.popup_edit_item_id) {
                            isEdit = true;
                            addNewMistakeBottomSheet();
                            numberOfMistake = mistakeInMoves.getNumberOfMistake();
                            numberOfMove = mistakeInMoves.getNumberOfMove();
                            editTextNumberOfMistake.setText(numberOfMistake + "");
                            editTextNumberOfMove.setText(numberOfMove + "");
                        }
                        return true;
                    }
                });
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mistakesInMovesGamesAdapter);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mistakesInMovesGamesAdapter.startListening();
        checkIfHaveData();
    }

    private void getAllMoveMistakes() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dataEntryArrayList = new ArrayList<>();
                mistakeInMovesArrayList = new ArrayList<>();
                FirebaseFirestore.getInstance().collection("MistakesInMoves")
                        .whereEqualTo("userId", userUid)
                        .whereEqualTo("accountType", accountType)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                MistakeInMoves mistakeInMoves = queryDocumentSnapshot.toObject(MistakeInMoves.class);
                                mistakeInMovesArrayList.add(mistakeInMoves);
                            }
                        }else
                            constraintLayoutNoDataChart.setVisibility(View.VISIBLE);
                        Collections.sort(mistakeInMovesArrayList, new StockComparator());
                        for (int i = 0; i < mistakeInMovesArrayList.size(); i++) {
                            MistakeInMoves mistakeInMoves = mistakeInMovesArrayList.get(i);
                            dataEntryArrayList.add(new ValueDataEntry(mistakeInMoves.getNumberOfMove(), mistakeInMoves.getNumberOfMistake()));
                        }
                        set.data(dataEntryArrayList);
                        //checkIfHaveData();
                    }
                });
            }
        }, 2000);
    }

    private void addNewMistakeBottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(getContext());
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.create_mistake_move_bottom_sheet_layout, null);
        buttonCreate = view.findViewById(R.id.button_create_handling_id);
        titleBottomSheet = view.findViewById(R.id.textView6);
        buttonBack = view.findViewById(R.id.image_back_id);
        progressBar = view.findViewById(R.id.progressBar_id);
        progressBar.setVisibility(View.GONE);
        editTextNumberOfMistake = view.findViewById(R.id.editText_number_of_mistake_id);
        editTextNumberOfMove = view.findViewById(R.id.editText_number_of_move_id);
        TextView textView = view.findViewById(R.id.textView30);
        if (isEdit) {
            buttonCreate.setText("Save");
            titleBottomSheet.setText("Edit Mistake");
            textView.setVisibility(View.GONE);
            editTextNumberOfMove.setVisibility(View.GONE);
        }
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (!editTextNumberOfMistake.getText().toString().trim().isEmpty()) {
                    numberOfMistake = Integer.parseInt(editTextNumberOfMistake.getText().toString().trim());
                }
                if (!editTextNumberOfMove.getText().toString().trim().isEmpty()) {
                    numberOfMove = Integer.parseInt(editTextNumberOfMove.getText().toString().trim());
                }
                if (isEdit) {
                    firebaseFirestore.collection("MistakesInMoves").document(mistakeEditUid).update("numberOfMistake", numberOfMistake, "numberOfMove", numberOfMove).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getContext(), "Modified", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            update();
                        }
                    });
                } else {//Create new mistake
                    MistakeInMoves mistakeInMoves = new MistakeInMoves(numberOfMove, numberOfMistake, userUid,accountType);
                    //Check if has this mistake
                    firebaseFirestore.collection("MistakesInMoves")
                            .whereEqualTo("userId", userUid)
                            .whereEqualTo("accountType", accountType)
                            .whereEqualTo("numberOfMove",numberOfMove).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                if (task.getResult().isEmpty()){
                                    firebaseFirestore.collection("MistakesInMoves").add(mistakeInMoves).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(getContext(), "Created", Toast.LENGTH_LONG).show();
                                            update();
                                        }
                                    });
                                }else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getContext(),"This move already exist!",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                numberOfMistake = 0;
                numberOfMove = 0;
            }
        });
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    private void update() {
        numberOfMistake = 0;
        numberOfMove = 0;
        bottomSheetDialog.dismiss();
        getAllMistakeAndFillRecycle();
        getAllMoveMistakes();
    }

    private class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value) {
            super(x, value);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.mistakes_move_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            numberOfMistake = 0;
            numberOfMove = 0;
            addNewMistakeBottomSheet();
        }else if (item.getItemId() == R.id.action_refresh){
            getAllMoveMistakes();
            getAllMistakeAndFillRecycle();
            checkIfHaveData();
        }
        return super.onOptionsItemSelected(item);
    }
}

class StockComparator implements Comparator<MistakeInMoves> {

    // Function to compare
    public int compare(MistakeInMoves m1, MistakeInMoves m2) {
        return Integer.compare(m1.getNumberOfMove(), m2.getNumberOfMove());
    }
}