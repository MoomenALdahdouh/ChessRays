package com.moomen.chessrays.ui.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agrawalsuneet.dotsloader.loaders.LazyLoader;
import com.anychart.AnyChartView;
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
import com.moomen.chessrays.adapter.OpeningGamesAdapter;
import com.moomen.chessrays.adapter.RatingAdapter;
import com.moomen.chessrays.model.ChartGame;
import com.moomen.chessrays.model.Game;
import com.moomen.chessrays.model.InstallNewGame;
import com.moomen.chessrays.model.MistakeInMoves;
import com.moomen.chessrays.model.StackGameModel;
import com.moomen.chessrays.ui.Activity.ViewOpeningGame;
import com.moomen.chessrays.utils.MovesIdStack;
import com.moomen.chessrays.utils.PreferenceUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.graphics.vector.Stroke;

import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment {
    private String gameType = "Classical";
    private float rate = 0;
    private TextView textViewInstallNewGame;
    private Spinner spinnerGameTypes;
    private EditText editTextYourRate;
    private EditText editTextOpponentName;
    private EditText editTextOpponentRate;
    private Button buttonCreate;
    private ImageView buttonBack;
    private ImageView imageViewCreate;
    private ProgressBar progressBar;

    private BottomSheetDialog bottomSheetDialog;

    private float currentDay;
    private float currentMonth;
    private float currentYear;
    private String date;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;

    private ArrayList<DataEntry> dataEntryArrayList;
    private ArrayList<DataEntry> dataEntryArrayListRevers;
    private Cartesian cartesian;
    private String userUid;

    private AnyChartView anyChartView;
    private RecyclerView recyclerViewAllInstallGame;
    private DatePickerDialog datePickerDialog;
    private ImageView imageViewDate;
    private EditText editTextDate;
    private ArrayList<InstallNewGame> installNewGameArrayList1;

    private TextView scoringOpeningTextView, scoringMiddleTextView, scoringEndTextView;
    private TextView openingTextView;
    private TextView middleTextView;
    private TextView endTextView;
    private TextView averageEffciencyTextView;
    private TextView averageScoringTextView;
    private RatingAdapter ratingAdapter;

    private float scoring = 0;
    private float effciency = 0;
    private float averageScoring = 0;
    private float averageEffciency = 0;
    private float totalScoring = 0;
    private float allTotalScoring = 0;
    private float totalEffciency = 0;
    private float allTotalEffciency = 0;
    private int totalGoodBadEqual = 0;
    private int totalWinDrawLost = 0;
    private int totalSumGood = 0;
    private int totalSumEqual = 0;
    private int totalSumBad = 0;
    private int totalSumWin = 0;
    private int totalSumDraw = 0;
    private int totalSumLost = 0;
    private InstallNewGame installNewGame;
    private Set set;
    private Line series1, series2, series3, series4;
    private Mapping series1Mapping, series2Mapping, series3Mapping, series4Mapping;

    private ConstraintLayout constraintLayoutNoData;
    private ConstraintLayout constraintLayoutLazyLoader;
    private LazyLoader lazyLoader;

    private Button classicalButton, rapidButton, blitzButton, fischerButton;
    private String accountType = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        recyclerViewAllInstallGame = view.findViewById(R.id.recyclerView_all_install_game_id);
        anyChartView = view.findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(view.findViewById(R.id.progress_bar));
        imageViewCreate = view.findViewById(R.id.imageView_create_id);
        scoringOpeningTextView = view.findViewById(R.id.textView_score_opening_id);
        scoringMiddleTextView = view.findViewById(R.id.textView_score_middle_id);
        scoringEndTextView = view.findViewById(R.id.textView_score_end_id);
        averageEffciencyTextView = view.findViewById(R.id.textView_average_effciency_id);
        averageScoringTextView = view.findViewById(R.id.textView_average_scoring_id);
        openingTextView = view.findViewById(R.id.textView_opening_id);
        middleTextView = view.findViewById(R.id.textView_middle_id);
        endTextView = view.findViewById(R.id.textView_end_id);
        lazyLoader = view.findViewById(R.id.lazyLoader_id);
        constraintLayoutLazyLoader = view.findViewById(R.id.constraintLayout_lazyLoader_id);
        constraintLayoutNoData = view.findViewById(R.id.constraintLayout_no_data_id);
        constraintLayoutNoData.setVisibility(View.GONE);
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userUid = PreferenceUtils.getUid(getContext());
        date = DateFormat.getDateInstance().format(Calendar.getInstance().getTime());

        accountType = PreferenceUtils.getAccountType(getContext());
        classicalButton = view.findViewById(R.id.button_classical_account);
        rapidButton = view.findViewById(R.id.button_rapid_account);
        blitzButton = view.findViewById(R.id.button_blitz_account);
        fischerButton = view.findViewById(R.id.button_ficher_account);

        changeColorButtonAccount();
        buttonOnClick(classicalButton);
        buttonOnClick(rapidButton);
        buttonOnClick(blitzButton);
        buttonOnClick(fischerButton);

        preparProgress();
        prepirChart();
        getAllInstallGame();
        calculateResults("OpeningGames");
        calculateResults("MiddleGames");
        calculateResults("EndGames");
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void changeColorButtonAccount() {
        if (accountType.equals("Classical")) {
            classicalButton.setBackground(getResources().getDrawable(R.drawable.bg_button_4));
            rapidButton.setBackground(getResources().getDrawable(R.drawable.bg_button_2));
            blitzButton.setBackground(getResources().getDrawable(R.drawable.bg_button_2));
            fischerButton.setBackground(getResources().getDrawable(R.drawable.bg_button_2));
        } else if (accountType.equals("Rapid")) {
            classicalButton.setBackground(getResources().getDrawable(R.drawable.bg_button_2));
            rapidButton.setBackground(getResources().getDrawable(R.drawable.bg_button_4));
            blitzButton.setBackground(getResources().getDrawable(R.drawable.bg_button_2));
            fischerButton.setBackground(getResources().getDrawable(R.drawable.bg_button_2));
        } else if (accountType.equals("Blitz")) {
            classicalButton.setBackground(getResources().getDrawable(R.drawable.bg_button_2));
            rapidButton.setBackground(getResources().getDrawable(R.drawable.bg_button_2));
            blitzButton.setBackground(getResources().getDrawable(R.drawable.bg_button_4));
            fischerButton.setBackground(getResources().getDrawable(R.drawable.bg_button_2));
        } else if (accountType.equals("Fischer")) {
            classicalButton.setBackground(getResources().getDrawable(R.drawable.bg_button_2));
            rapidButton.setBackground(getResources().getDrawable(R.drawable.bg_button_2));
            blitzButton.setBackground(getResources().getDrawable(R.drawable.bg_button_2));
            fischerButton.setBackground(getResources().getDrawable(R.drawable.bg_button_4));
        }
    }

    private void buttonOnClick(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAccount(button);
            }
        });
    }

    private void changeAccount(Button button) {
        switch (button.getId()) {
            case R.id.button_classical_account:
                accountType = "Classical";
                break;
            case R.id.button_rapid_account:
                accountType = "Rapid";
                break;
            case R.id.button_blitz_account:
                accountType = "Blitz";
                break;
            case R.id.button_ficher_account:
                accountType = "Fischer";
                break;
        }
        PreferenceUtils.saveAccountType(accountType, getContext());
        changeColorButtonAccount();
        resetAverageValues();
        getAllInstallGame();
        calculateResults("OpeningGames");
        calculateResults("MiddleGames");
        calculateResults("EndGames");
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

    private void prepirChart() {
        cartesian = AnyChart.line();
        cartesian.animation(true);
        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);
        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        set = Set.instantiate();

        series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");
        series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }");
        series4Mapping = set.mapAs("{ x: 'x', value: 'value4' }");

        series1 = cartesian.line(series1Mapping);
        series1.name("Classical");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);

        series2 = cartesian.line(series2Mapping);
        series2.name("Rapid");
        series2.hovered().markers().enabled(true);
        series2.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);

        series3 = cartesian.line(series3Mapping);
        series3.name("Blitz");
        series3.hovered().markers().enabled(true);
        series3.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);

        series4 = cartesian.line(series4Mapping);
        series4.name("Fischer Random");
        series4.hovered().markers().enabled(true);
        series4.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series4.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        anyChartView.setChart(cartesian);

    }

    private void fillRecycleAllInstallGame() {
        Query query = FirebaseFirestore.getInstance()
                .collection("InstallNewGame")
                .whereEqualTo("userId", userUid);
        FirestoreRecyclerOptions<InstallNewGame> options = new FirestoreRecyclerOptions.Builder<InstallNewGame>()
                .setQuery(query, InstallNewGame.class)
                .build();
        ratingAdapter = new RatingAdapter(options);
        //click on item in the recycler view
        ratingAdapter.onItemSetOnClickListener(new RatingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position, PopupMenu popupMenu) {
                String openingGameUid = documentSnapshot.getId();
                //getOpeningGameUid(gameUid);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Game game = documentSnapshot.toObject(Game.class);
                        switch (item.getItemId()) {
                            case R.id.popup_view_item_id:

                                break;
                            case R.id.popup_delete_item_id:

                                break;
                        }
                        return true;
                    }
                });
            }
        });
        recyclerViewAllInstallGame.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewAllInstallGame.setAdapter(ratingAdapter);
        recyclerViewAllInstallGame.setHasFixedSize(true);
        ratingAdapter.startListening();
    }

    private TextView textViewEffciency;
    private TextView textViewScoring;
    private int count = 0;

    private void calculateResults(String gameType) {
        firebaseFirestore.collection(gameType)
                .whereEqualTo("userId", userUid)
                .whereEqualTo("accountType", accountType)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                constraintLayoutLazyLoader.setVisibility(View.GONE);
                resetValue();
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    Game game = queryDocumentSnapshot.toObject(Game.class);
                    totalSumGood += game.getSumGoodTG();
                    totalSumEqual += game.getSumEqualTG();
                    totalSumBad += game.getSumBadTG();
                    totalGoodBadEqual = totalGoodBadEqual + game.getSumGoodTG() + game.getSumEqualTG() + game.getSumBadTG();

                    totalSumWin += game.getSumWinTG();
                    totalSumDraw += game.getSumDrawTG();
                    totalSumLost += game.getSumLostTG();
                    totalWinDrawLost = totalWinDrawLost + game.getSumWinTG() + game.getSumDrawTG() + game.getSumLostTG();
                }
                if (totalGoodBadEqual != 0)
                    effciency = ((float) (totalSumGood + totalSumEqual * 0.5)) / (float) totalGoodBadEqual;

                if (totalWinDrawLost != 0)
                    scoring = ((float) (totalSumWin + totalSumDraw * 0.5)) / (float) totalWinDrawLost;

                count++;

                totalEffciency = totalEffciency + (float) (totalSumGood + totalSumEqual * 0.5);
                allTotalEffciency = allTotalEffciency + (float) totalGoodBadEqual;

                totalScoring = totalScoring + (float) (totalSumWin + totalSumDraw * 0.5);
                allTotalScoring = allTotalScoring + (float) totalWinDrawLost;


                filTextView(gameType, effciency, scoring);
            }
        });
    }

    private void filTextView(String gameType, float effciency, float scoring) {
        if (gameType.equals("OpeningGames")) {
            textViewEffciency = openingTextView;
            textViewScoring = scoringOpeningTextView;
        } else if (gameType.equals("MiddleGames")) {
            textViewEffciency = middleTextView;
            textViewScoring = scoringMiddleTextView;
        } else if (gameType.equals("EndGames")) {
            textViewEffciency = endTextView;
            textViewScoring = scoringEndTextView;
        }
        String scoringString = (scoring * 100) + "00000";
        if (scoring == 0 || scoring == 1)
            textViewScoring.setText(scoring * 100 + " %");
        else if (scoring > 1)
            textViewScoring.setText(scoringString.substring(0, 5) + " %");
        else
            textViewScoring.setText(scoringString.substring(0, 4) + " %");
        //
        String effciencyString = (effciency * 100) + "00000";
        if (effciency == 0 || effciency == 1)
            textViewEffciency.setText(effciency * 100 + " %");
        else if (effciency > 1)
            textViewEffciency.setText(effciencyString.substring(0, 5) + " %");
        else
            textViewEffciency.setText(effciencyString.substring(0, 4) + " %");

        if (count <= 3) {
            if (allTotalEffciency != 0)
                averageEffciency = totalEffciency / allTotalEffciency;
            String averageEff = (averageEffciency * 100) + "000000";
            averageEffciencyTextView.setText(averageEff.substring(0, 4) + "%");
            if (allTotalScoring != 0)
                averageScoring = totalScoring / allTotalScoring;
            String averageSco = (averageScoring * 100) + "000000";
            averageScoringTextView.setText(averageSco.substring(0, 4) + "%");
        }
    }

    private void resetValue() {
        totalSumGood = 0;
        totalSumEqual = 0;
        totalSumBad = 0;
        totalGoodBadEqual = 0;
        totalSumWin = 0;
        totalSumDraw = 0;
        totalSumLost = 0;
        totalWinDrawLost = 0;
        scoring = 0;
        effciency = 0;
    }

    float prevClass = 0;
    float prevRapid = 0;
    float prevBlitz = 0;
    float prevFischer = 0;

    private void getAllInstallGame() {
        index = 0;
        dataEntryArrayList = new ArrayList<>();
        dataEntryArrayListRevers = new ArrayList<>();
        firebaseFirestore.collection("InstallNewGame")
                .whereEqualTo("userId", userUid)
                .whereEqualTo("accountType", accountType)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        constraintLayoutNoData.setVisibility(View.GONE);
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            ChartGame chartGame = queryDocumentSnapshot.toObject(ChartGame.class);
                            ArrayList<InstallNewGame> installNewGameArrayList = chartGame.getInstallNewGameArrayList();
                            index = index + installNewGameArrayList.size();
                            float[] rateGameData = new float[4];
                            for (int i = 0; i < installNewGameArrayList.size(); i++) {
                                InstallNewGame installNewGame = installNewGameArrayList.get(i);
                                switch (installNewGame.getGameType()) {
                                    case "Classical":
                                        rateGameData[0] = installNewGame.getRate();
                                        prevClass = installNewGame.getRate();
                                        break;
                                    case "Rapid":
                                        rateGameData[1] = installNewGame.getRate();
                                        prevRapid = installNewGame.getRate();
                                        break;
                                    case "Blitz":
                                        rateGameData[2] = installNewGame.getRate();
                                        prevBlitz = installNewGame.getRate();
                                        break;
                                    case "Fischer Random":
                                        rateGameData[3] = installNewGame.getRate();
                                        prevFischer = installNewGame.getRate();
                                        break;
                                }
                            }
                            if (rateGameData[0] == 0)
                                rateGameData[0] = prevClass;
                            if (rateGameData[1] == 0)
                                rateGameData[1] = prevRapid;
                            if (rateGameData[2] == 0)
                                rateGameData[2] = prevBlitz;
                            if (rateGameData[3] == 0)
                                rateGameData[3] = prevFischer;
                            dataEntryArrayList.add(new CustomDataEntry(chartGame.getDate()
                                    , rateGameData[0]
                                    , rateGameData[1]
                                    , rateGameData[2]
                                    , rateGameData[3]));
                        }

                    } else {
                        constraintLayoutNoData.setVisibility(View.VISIBLE);
                        dataEntryArrayList.add(new CustomDataEntry(""
                                , null
                                , null
                                , null
                                , null));
                    }

                    try {
                        fillChart();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    private void fillChart() throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd h:m");
        for (int i = 0; i < dataEntryArrayList.size() - 1; i++) {
            for (int j = 0; j < (dataEntryArrayList.size() - i - 1); j++) {
                DataEntry dE1 = dataEntryArrayList.get(j);
                DataEntry dE2 = dataEntryArrayList.get(j + 1);
                String da1 = String.valueOf(dE1.getValue("x"));
                String da2 = String.valueOf(dE2.getValue("x"));
                Date d1 = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(da1);
                Date d2 = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(da2);
                if (d1.compareTo(d2) > 0) {
                    dataEntryArrayList.set(j, dE2);
                    dataEntryArrayList.set(j + 1, dE1);
                }
            }
        }
        set.data(dataEntryArrayList);
    }

    private void spinnerCreate() {
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(getContext(), R.array.gameType, R.layout.support_simple_spinner_dropdown_item);
        adapterSpinner.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerGameTypes.setAdapter(adapterSpinner);
        spinnerGameTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                switch (position) {
                    case 0:
                        gameType = "Classical";
                        break;
                    case 1:
                        gameType = "Rapid";
                        break;
                    case 2:
                        gameType = "Blitz";
                        break;
                    case 3:
                        gameType = "Fischer Random";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                gameType = "Classical";
            }
        });
    }

    int index = 0;

    private void installNewGameBottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(getContext());
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.install_new_game_bottom_sheet_layout, null);
        spinnerGameTypes = view.findViewById(R.id.spinner_game_type_id);
        editTextYourRate = view.findViewById(R.id.editText_your_rate_id);
        editTextOpponentName = view.findViewById(R.id.editText_opponent_name_id);
        editTextOpponentRate = view.findViewById(R.id.editText_opponent_rate_id);
        buttonCreate = view.findViewById(R.id.button_install_new_game_id);
        buttonBack = view.findViewById(R.id.image_back_id);
        progressBar = view.findViewById(R.id.progressBar_id);
        progressBar.setVisibility(View.GONE);
        imageViewDate = view.findViewById(R.id.imageView_date);
        editTextDate = view.findViewById(R.id.editText_date);
        spinnerCreate();
        imageViewDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(getContext());
                Calendar calendar = Calendar.getInstance();
                datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
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

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String rated = editTextYourRate.getText().toString().trim();
                String opponentName = editTextOpponentName.getText().toString().trim();
                float opponentRate = 0;
                date = editTextDate.getText().toString().trim();
                if (rated.isEmpty() || date.isEmpty()) {
                    //editTextYourRate.setError("Your rate or date is required!");
                    //editTextYourRate.requestFocus();
                    //editTextDate.requestFocus();
                    Toast.makeText(getContext(), "Must input all data", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                } else {
                    rate = Float.parseFloat(editTextYourRate.getText().toString().trim());
                    index = 1 + (index * 2);
                    installNewGame = new InstallNewGame(userUid, gameType, rate, date, opponentName, opponentRate, index);
                    installNewGameArrayList1 = new ArrayList<>();
                    installNewGameArrayList1.add(installNewGame);
                    ChartGame chartGame = new ChartGame(accountType, date, userUid, installNewGameArrayList1);
                    installNewGame(chartGame);
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

    private void installNewGame(ChartGame chartGame) {
        firebaseFirestore.collection("InstallNewGame")
                .whereEqualTo("date", date)
                .whereEqualTo("accountType", accountType)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //if exist the same date add on the array this game also
                if (!task.getResult().isEmpty()) {
                    ChartGame chartGame = task.getResult().getDocuments().get(0).toObject(ChartGame.class);
                    assert chartGame != null;
                    installNewGameArrayList1 = chartGame.getInstallNewGameArrayList();
                    installNewGameArrayList1.add(installNewGame);
                    firebaseFirestore.collection("InstallNewGame")
                            .document(task.getResult().getDocuments().get(0).getId())
                            .update("installNewGameArrayList", installNewGameArrayList1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Created", Toast.LENGTH_SHORT).show();
                            bottomSheetDialog.dismiss();
                            getAllInstallGame();
                        }
                    });
                    //if not exist create new
                } else {
                    firebaseFirestore.collection("InstallNewGame").add(chartGame).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Created", Toast.LENGTH_SHORT).show();
                            getAllInstallGame();
                            bottomSheetDialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    private class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value, Number value2, Number value3, Number value4) {
            super(x, value);
            setValue("value2", value2);
            setValue("value3", value3);
            setValue("value4", value4);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_rating)
            installNewGameBottomSheet();
        else if (item.getItemId() == R.id.reset_rating)
            resetConfirmDialog();
        return super.onOptionsItemSelected(item);
    }

    private void resetConfirmDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle("Reset ratings")
                .setMessage("Are you sure you want to reset all ratings?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        clearRatingsOnThisGame();
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

    private void clearRatingsOnThisGame() {
        firebaseFirestore.collection("InstallNewGame")
                .whereEqualTo("userId", userUid)
                .whereEqualTo("accountType", accountType)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        constraintLayoutNoData.setVisibility(View.GONE);
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            firebaseFirestore.collection("InstallNewGame").document(queryDocumentSnapshot.getId()).delete();
                        }
                        getAllInstallGame();
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        resetAverageValues();
    }

    private void resetAverageValues() {
        averageScoring = 0;
        totalScoring = 0;
        allTotalScoring = 0;
        averageEffciency = 0;
        totalEffciency = 0;
        allTotalEffciency = 0;
        count = 0;
    }
}

class SortComparator implements Comparator<DataEntry> {

    // Function to compare
    public int compare(DataEntry m1, DataEntry m2) {
        int date1 = Integer.parseInt(String.valueOf(m1.getValue("x")).split("/").toString().trim());
        int date2 = Integer.parseInt(Arrays.toString(String.valueOf(m2.getValue("x")).split("/")));
        return Integer.compare(date1, date2);
    }
}