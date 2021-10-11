package com.moomen.chessrays.ui.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.anychart.AnyChartView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
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
import com.moomen.chessrays.model.ChartGame;
import com.moomen.chessrays.model.InstallNewGame;
import com.moomen.chessrays.model.OpeningHandling;
import com.moomen.chessrays.utils.PreferenceUtils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;

public class HomeFragment extends Fragment {
    private String gameType = "Classical";
    private float rate = 0;
    private TextView textViewName;
    private TextView textViewInstallNewGame;
    private Spinner spinnerGameTypes;
    private EditText editTextYourRate;
    private EditText editTextOpponentName;
    private EditText editTextOpponentRate;
    private Button buttonCreate;
    private ImageView buttonBack;
    private ImageView imageViewCreate;
    private ProgressBar progressBar;
    private CardView cardViewInstallGame;
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
    private LineChart lineChart;
    private String userUid;

    private AnyChartView anyChartView;

    private DatePickerDialog datePickerDialog;
    private ImageView imageViewDate;
    private EditText editTextDate;
    ArrayList<InstallNewGame> installNewGameArrayList1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        userUid = PreferenceUtils.getUid(getContext());

        anyChartView = root.findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(root.findViewById(R.id.progress_bar));

        lineChart = root.findViewById(R.id.barChart_id);
        lineChart.setScaleEnabled(false);
        imageViewCreate = root.findViewById(R.id.imageView_create_id);
        textViewName = root.findViewById(R.id.textView_user_name_id);
        textViewName.setText(PreferenceUtils.getName(getContext()));
        cardViewInstallGame = root.findViewById(R.id.cardView_install_new_game_id);
        textViewInstallNewGame = root.findViewById(R.id.textView_open_game_id);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        /*imageViewCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardViewInstallGame.getVisibility() == View.GONE)
                    cardViewInstallGame.setVisibility(View.VISIBLE);
                else
                    cardViewInstallGame.setVisibility(View.GONE);
            }
        });*/

        cardViewInstallGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                installNewGameBottomSheet();
            }
        });

        date = DateFormat.getDateInstance().format(Calendar.getInstance().getTime());

        cartesian = AnyChart.line();

        cartesian.animation(true);

        //cartesian.padding(2d, 2d, 2d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                // TODO ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        //cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        //cartesian.title("Trend of Sales of the Most Popular Products of ACME Corp.");

        //cartesian.yAxis(0).title("Number of Bottles Sold (thousands)");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);
        anyChartView.setChart(cartesian);
        getAllInstallGame();
        return root;
    }

    Cartesian cartesian;

    private void getAllInstallGame() {
        dataEntryArrayList = new ArrayList<>();
        dataEntryArrayListRevers = new ArrayList<>();
        firebaseFirestore.collection("InstallNewGame")
                .whereEqualTo("userId", userUid)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        ChartGame chartGame = queryDocumentSnapshot.toObject(ChartGame.class);
                        ArrayList<InstallNewGame> installNewGameArrayList = chartGame.getInstallNewGameArrayList();
                        float[] rateGameData = new float[4];
                        for (int i = 0; i < installNewGameArrayList.size(); i++) {
                            InstallNewGame installNewGame = installNewGameArrayList.get(i);
                            switch (installNewGame.getGameType()) {
                                case "Classical":
                                    rateGameData[0] = installNewGame.getRate();
                                    break;
                                case "Rapid":
                                    rateGameData[1] = installNewGame.getRate();
                                    break;
                                case "Blitz":
                                    rateGameData[2] = installNewGame.getRate();
                                    break;
                                case "Fischer Random":
                                    rateGameData[3] = installNewGame.getRate();
                                    break;
                            }
                        }
                        dataEntryArrayList.add(new CustomDataEntry(chartGame.getDate()
                                , rateGameData[0]
                                , rateGameData[1]
                                , rateGameData[2]
                                , rateGameData[3]));
                    }
                }
                fillChart();
            }
        });
    }

    private void fillChart() {
        Set set = Set.instantiate();
        int size = dataEntryArrayList.size() - 1;
        for (int i = size; i >= 0; i--) {
            dataEntryArrayListRevers.add(dataEntryArrayList.get(i));
        }
        set.data(dataEntryArrayListRevers);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");
        Mapping series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }");
        Mapping series4Mapping = set.mapAs("{ x: 'x', value: 'value4' }");

        Line series1 = cartesian.line(series1Mapping);
        series1.name("Classical");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series2 = cartesian.line(series2Mapping);
        series2.name("Rapid");
        series2.hovered().markers().enabled(true);
        series2.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series2.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series3 = cartesian.line(series3Mapping);
        series3.name("Blitz");
        series3.hovered().markers().enabled(true);
        series3.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series3.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series4 = cartesian.line(series4Mapping);
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

        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        anyChartView.setChart(cartesian);
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
                String opponentRateString = editTextOpponentRate.getText().toString().trim();
                float opponentRate = 0;
                if (!opponentRateString.isEmpty())
                    opponentRate = Float.parseFloat(editTextOpponentRate.getText().toString().trim());
                if (rated.isEmpty()) {
                    editTextYourRate.setError("Your rate or date is required!");
                    editTextYourRate.requestFocus();
                    editTextDate.requestFocus();
                    progressBar.setVisibility(View.GONE);
                } else {
                    rate = Float.parseFloat(editTextYourRate.getText().toString().trim());
                    date = editTextDate.getText().toString().trim();
                    installNewGame = new InstallNewGame(userUid, gameType, rate, date, opponentName, opponentRate, 0);
                    installNewGameArrayList1 = new ArrayList<>();
                    installNewGameArrayList1.add(installNewGame);
                    ChartGame chartGame = new ChartGame(date, userUid, installNewGameArrayList1);
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

    InstallNewGame installNewGame;


    private void installNewGame(ChartGame chartGame) {
        firebaseFirestore.collection("InstallNewGame").whereEqualTo("date", date).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

}