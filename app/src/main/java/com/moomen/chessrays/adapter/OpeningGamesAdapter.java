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

public class OpeningGamesAdapter extends FirestoreRecyclerAdapter<Game, OpeningGamesAdapter.ViewHolder> {

    private OnItemClickListener listener;


    public OpeningGamesAdapter(@NonNull FirestoreRecyclerOptions<Game> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull OpeningGamesAdapter.ViewHolder holder, int position, @NonNull Game model) {
        holder.firstMove.setText(model.getFirstMove());
        holder.color.setText(model.getColor());
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
            holder.frequency.setText(frequency.substring(0, 4) + " %");    }

    @NonNull
    @Override
    public OpeningGamesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.opening_game_item, parent, false);
        return new OpeningGamesAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView firstMove;
        TextView color;
        TextView score;
        TextView frequency;
        ImageButton imageButtonPopupMenu;
        ConstraintLayout constraintLayoutOpeningGameItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            firstMove = itemView.findViewById(R.id.textView_first_move_id);
            color = itemView.findViewById(R.id.textView_color_id);
            score = itemView.findViewById(R.id.textView_score_id);
            frequency = itemView.findViewById(R.id.textView_frequency_id);
            imageButtonPopupMenu = itemView.findViewById(R.id.image_button_view_id);
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
            /*constraintLayoutOpeningGameItem = itemView.findViewById(R.id.constraintLayout_opening_game_item_id);
            constraintLayoutOpeningGameItem.setOnClickListener(new View.OnClickListener() {
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
