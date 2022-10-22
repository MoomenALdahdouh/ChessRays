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
import com.moomen.chessrays.model.Game;

public class HandlingAdapter extends FirestoreRecyclerAdapter<Game, HandlingAdapter.ViewHolder> {

    private OnItemClickListener listener;


    public HandlingAdapter(@NonNull FirestoreRecyclerOptions<Game> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull HandlingAdapter.ViewHolder holder, int position, @NonNull Game model) {


        holder.moveName.setText(model.getGameName());
        String score = (model.getScore() * 100) + "00000";
        if (model.getScore() == 0 || model.getScore() == 1)
            holder.date.setText(model.getScore() * 100 + " %");
        else
            holder.date.setText(score.substring(0, 4) + " %");

        holder.goodValue.setText(model.getGood() + "");
        holder.equalValue.setText(model.getEqual() + "");
        holder.badValue.setText(model.getBad() + "");
        holder.winValue.setText(model.getTotalWin() + "");
        holder.drawValue.setText(model.getTotalDraw() + "");
        holder.lostValue.setText(model.getTotalLoss() + "");
        holder.result.setVisibility(View.VISIBLE);
        /*switch (model.getResult()) {
            case 0:
                holder.result.setText("Loss");
                break;
            case 1:
                holder.result.setText("Draw");
                break;
            case 2:
                holder.result.setText("Win");
                break;
            case 3:
                holder.result.setVisibility(View.GONE);
                break;
        }*/
        holder.result.setVisibility(View.GONE);
        if (model.getGood() == 0 && model.getBad() == 0 && model.getEqual() == 0) {
            holder.linearLayoutHandling.setVisibility(View.GONE);
        } else
            holder.linearLayoutHandling.setVisibility(View.VISIBLE);

        if (model.getTotalWin() == 0 && model.getTotalDraw() == 0 && model.getTotalLoss() == 0) {
            holder.linearLayoutResult.setVisibility(View.GONE);
        } else
            holder.linearLayoutResult.setVisibility(View.VISIBLE);
    }

    @NonNull
    @Override
    public HandlingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.handling_item, parent, false);
        return new HandlingAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView moveName;
        TextView date;
        TextView goodValue;
        TextView equalValue;
        TextView badValue;
        TextView winValue;
        TextView drawValue;
        TextView lostValue;
        /*TextView totalValue;
        TextView frequencyValue;
        TextView efficiencyValue;*/
        TextView result;
        LinearLayout linearLayoutHandling;
        LinearLayout linearLayoutResult;
        ImageButton imageButtonPopupMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            moveName = itemView.findViewById(R.id.textView_move_name_id);
            date = itemView.findViewById(R.id.textView_date_id);
            goodValue = itemView.findViewById(R.id.textView_good_value_id);
            equalValue = itemView.findViewById(R.id.textView_equal_value_id);
            badValue = itemView.findViewById(R.id.textView_bad_value_id);
            result = itemView.findViewById(R.id.textView_result_id);
            winValue = itemView.findViewById(R.id.textView_win_value_id);
            drawValue = itemView.findViewById(R.id.textView_draw_value_id);
            lostValue = itemView.findViewById(R.id.textView_lost_value_id);
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
