package com.moomen.chessrays.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.moomen.chessrays.R;
import com.moomen.chessrays.model.Game;

public class MiddleGamesAdapter extends FirestoreRecyclerAdapter<Game, MiddleGamesAdapter.ViewHolder> {

    private OnItemClickListener listener;


    public MiddleGamesAdapter(@NonNull FirestoreRecyclerOptions<Game> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull MiddleGamesAdapter.ViewHolder holder, int position, @NonNull Game model) {
        holder.name.setText(model.getGameName());
        String score = (model.getScore() * 100) + "00000";
        if (model.getScore() == 0 || model.getScore() == 1)
            holder.score.setText(model.getScore() * 100 + " %");
        else
            holder.score.setText(score.substring(0, 4) + " %");
        //
        String frequency = (model.getFrequency() * 100) + "000000";
        if (model.getFrequency() == 0 || model.getFrequency() == 10)
            holder.frequency.setText(model.getFrequency() * 100 + " %");
        else
            holder.frequency.setText(frequency.substring(0, 4) + " %");
        //textViewFrequency.setText(frequency.substring(0, 5) + " %");
        String efficiency = (model.getEfficiency() * 100) + "000000";
        if (model.getEfficiency() == 0 || model.getEfficiency() == 10)
            holder.efficiency.setText(model.getEfficiency() * 100 + " %");
        else
            holder.efficiency.setText(efficiency.substring(0, 4) + " %");
    }

    @NonNull
    @Override
    public MiddleGamesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.middle_game_item, parent, false);
        return new MiddleGamesAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView score;
        TextView frequency;
        TextView efficiency;
        ImageButton imageButtonPopupMenu;
        ConstraintLayout constraintLayoutMiddleGameItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView_game_name);
            score = itemView.findViewById(R.id.textView_score_id);
            frequency = itemView.findViewById(R.id.textView_frequency_id);
            efficiency = itemView.findViewById(R.id.textView_efficiency_id);
            imageButtonPopupMenu = itemView.findViewById(R.id.image_button_view_id);
            imageButtonPopupMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                    popupMenu.inflate(R.menu.popup_menu_fragment);
                    popupMenu.show();
                    if (getAdapterPosition() != RecyclerView.NO_POSITION && listener != null) {
                        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(getAdapterPosition());
                        listener.onItemClick(documentSnapshot, getAdapterPosition(), popupMenu);
                    }
                }
            });
            /*constraintLayoutMiddleGameItem = itemView.findViewById(R.id.constraintLayout_opening_game_item_id);
            constraintLayoutMiddleGameItem.setOnClickListener(new View.OnClickListener() {
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
