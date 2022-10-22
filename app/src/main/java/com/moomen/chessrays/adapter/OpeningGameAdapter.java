package com.moomen.chessrays.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.moomen.chessrays.R;
import com.moomen.chessrays.model.Game;

public class OpeningGameAdapter extends FirestoreRecyclerAdapter<Game, OpeningGameAdapter.ViewHolder> {

    private OnItemClickListener listener;


    public OpeningGameAdapter(@NonNull FirestoreRecyclerOptions<Game> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull OpeningGameAdapter.ViewHolder holder, int position, @NonNull Game model) {
        holder.color.setText(model.getColor());
        holder.firstMove.setText(model.getFirstMove());
        holder.date.setText(model.getDate());
    }

    @NonNull
    @Override
    public OpeningGameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.opening_game_item, parent, false);
        return new OpeningGameAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView color;
        TextView firstMove;
        TextView date;
        Button buttonView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            color = itemView.findViewById(R.id.textView_color_id);
            firstMove = itemView.findViewById(R.id.textView_first_move_id);
            date = itemView.findViewById(R.id.textView_score_id);
            buttonView = itemView.findViewById(R.id.button_view_id);
            buttonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION && listener != null) {
                        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(getAdapterPosition());
                        listener.onItemClick(documentSnapshot, getAdapterPosition());
                    }
                }
            });

        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void onItemSetOnClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


}
