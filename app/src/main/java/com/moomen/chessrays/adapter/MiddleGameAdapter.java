package com.moomen.chessrays.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.moomen.chessrays.R;
import com.moomen.chessrays.model.MiddleGame;

public class MiddleGameAdapter extends FirestoreRecyclerAdapter<MiddleGame, MiddleGameAdapter.ViewHolder> {

    private OnItemClickListener listener;


    public MiddleGameAdapter(@NonNull FirestoreRecyclerOptions<MiddleGame> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull MiddleGameAdapter.ViewHolder holder, int position, @NonNull MiddleGame model) {


        holder.type.setText(model.getMiddleType());
        holder.subType.setText(model.getMiddleSubType());
        holder.total.setText(model.getTotal() + "");
        String score = (model.getScore() * 100) + "00000";
        if (model.getScore() == 0 || model.getScore() == 1)
            holder.score.setText(model.getScore() * 100 + " %");
        else
            holder.score.setText(score.substring(0, 4) + " %");

        String frequency = (model.getFrequency() * 100) + "00000";
        if (model.getFrequency() == 0 || model.getFrequency() == 1)
            holder.frequencyValue.setText(model.getFrequency() * 100 + " %");
        else
            holder.frequencyValue.setText(frequency.substring(0, 4) + " %");

        String efficiency = (model.getEfficiency() * 100) + "00000";
        if (model.getEfficiency() == 0 || model.getEfficiency() == 1)
            holder.efficiencyValue.setText(model.getEfficiency() * 100 + " %");
        else
            holder.efficiencyValue.setText(efficiency.substring(0, 4) + " %");

        holder.goodValue.setText(model.getGood() + "");
        holder.equalValue.setText(model.getEqual() + "");
        holder.badValue.setText(model.getBad() + "");
        holder.winValue.setText(model.getWin() + "");
        holder.drawValue.setText(model.getDraw() + "");
        holder.lostValue.setText(model.getLoss() + "");

        /*if (model.getGood() == 0 && model.getBad() == 0 && model.getEqual() == 0) {
            holder.linearLayoutHandling.setVisibility(View.GONE);
        } else
            holder.linearLayoutHandling.setVisibility(View.VISIBLE);

        if (model.getTotalWin() == 0 && model.getTotalDraw() == 0 && model.getTotalLoss() == 0) {
            holder.linearLayoutResult.setVisibility(View.GONE);
        } else
            holder.linearLayoutResult.setVisibility(View.VISIBLE);*/
    }

    @NonNull
    @Override
    public MiddleGameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.middle_game_item, parent, false);
        return new MiddleGameAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView type;
        TextView subType;
        TextView score;
        TextView goodValue;
        TextView equalValue;
        TextView badValue;
        TextView winValue;
        TextView drawValue;
        TextView lostValue;
        TextView efficiencyValue;
        TextView total;
        TextView frequencyValue;
        LinearLayout linearLayoutHandling;
        LinearLayout linearLayoutResult;
        ImageButton imageButtonPopupMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.textView_middle_type_id);
            subType = itemView.findViewById(R.id.textView_sub_type_id);
            score = itemView.findViewById(R.id.textView_score_id);
            goodValue = itemView.findViewById(R.id.textView_good_value_id);
            equalValue = itemView.findViewById(R.id.textView_equal_value_id);
            badValue = itemView.findViewById(R.id.textView_bad_value_id);
            winValue = itemView.findViewById(R.id.textView_win_value_id);
            drawValue = itemView.findViewById(R.id.textView_draw_value_id);
            lostValue = itemView.findViewById(R.id.textView_lost_value_id);
            total = itemView.findViewById(R.id.textView_total_id);
            efficiencyValue = itemView.findViewById(R.id.textView_frequency_id);
            frequencyValue = itemView.findViewById(R.id.textView_efficiency_id);
            linearLayoutHandling = itemView.findViewById(R.id.linearLayout_handling_move_id);
            linearLayoutResult = itemView.findViewById(R.id.linearLayout_result_id);
            imageButtonPopupMenu = itemView.findViewById(R.id.imageButton_menu_id);
            imageButtonPopupMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                    popupMenu.inflate(R.menu.popup_menu);
                    popupMenu.show();
                    if (getAdapterPosition() != RecyclerView.NO_POSITION && listener != null) {
                        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(getAdapterPosition());
                        listener.onItemClick(documentSnapshot, getAdapterPosition(), popupMenu);
                    }
                }
            });
            /*buttonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION && listener != null) {
                        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(getAdapterPosition());
                        listener.onItemClick(documentSnapshot, getAdapterPosition());
                    }
                }
            });*/

        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position, PopupMenu popupMenu);
    }

    public void onItemSetOnClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


}
