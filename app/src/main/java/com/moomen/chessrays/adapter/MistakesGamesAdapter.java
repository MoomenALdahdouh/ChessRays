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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.moomen.chessrays.R;
import com.moomen.chessrays.model.Game;

public class MistakesGamesAdapter extends FirestoreRecyclerAdapter<Game, MistakesGamesAdapter.ViewHolder> {

    private OnItemClickListener listener;
    private boolean isMistakeFragment = false;

    public MistakesGamesAdapter(@NonNull FirestoreRecyclerOptions<Game> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull MistakesGamesAdapter.ViewHolder holder, int position, @NonNull Game model) {
        holder.name.setText(model.getGameName());
        holder.date.setText(model.getDate());
        holder.occurence.setText(model.getTotalTG() + "");
        String frequency = (model.getFrequency() * 100) + "000000";
        if (model.getFrequency() == 0 || model.getFrequency() == 10)
            holder.frequency.setText(model.getFrequency() * 100 + " %");
        else
            holder.frequency.setText(frequency.substring(0, 4) + " %");
    }

    @NonNull
    @Override
    public MistakesGamesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.mistake_item, parent, false);
        return new MistakesGamesAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView date;
        TextView frequency;
        TextView occurence;
        ImageButton imageButtonPopupMenu;
        ConstraintLayout constraintLayoutFrequency;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView_name_id);
            date = itemView.findViewById(R.id.textView_date_id);
            frequency = itemView.findViewById(R.id.textView_frequency_id);
            occurence = itemView.findViewById(R.id.textView_total_id);
            constraintLayoutFrequency = itemView.findViewById(R.id.constraintLayout_frequency_id);
            if (isMistakeFragment)
                constraintLayoutFrequency.setVisibility(View.GONE);
            imageButtonPopupMenu = itemView.findViewById(R.id.imageButton_menu_id);
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
