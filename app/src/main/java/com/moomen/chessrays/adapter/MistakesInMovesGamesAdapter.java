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
import com.moomen.chessrays.model.MistakeInMoves;

public class MistakesInMovesGamesAdapter extends FirestoreRecyclerAdapter<MistakeInMoves, MistakesInMovesGamesAdapter.ViewHolder> {

    private OnItemClickListener listener;

    public MistakesInMovesGamesAdapter(@NonNull FirestoreRecyclerOptions<MistakeInMoves> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull MistakesInMovesGamesAdapter.ViewHolder holder, int position, @NonNull MistakeInMoves model) {
        holder.move.setText(model.getNumberOfMove()+"");
        holder.mistake.setText(model.getNumberOfMistake()+"");
    }

    @NonNull
    @Override
    public MistakesInMovesGamesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.mistake_in_move_item, parent, false);
        return new MistakesInMovesGamesAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView move;
        TextView mistake;
        ImageButton imageButtonPopupMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            move = itemView.findViewById(R.id.textView_move_number_id);
            mistake = itemView.findViewById(R.id.textView_mistake_number_id);
            imageButtonPopupMenu = itemView.findViewById(R.id.imageButton_menu_id);
            imageButtonPopupMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                    popupMenu.inflate(R.menu.popup_mistake_move_menu);
                    popupMenu.show();
                    if (getAdapterPosition() != RecyclerView.NO_POSITION && listener != null) {
                        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(getAdapterPosition());
                        listener.onItemClick(documentSnapshot, getAdapterPosition(), popupMenu);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position, PopupMenu popupMenu);
    }

    public void onItemSetOnClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


}
