package com.moomen.chessrays.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.moomen.chessrays.R;
import com.moomen.chessrays.model.InstallNewGame;

public class RatingAdapter extends FirestoreRecyclerAdapter<InstallNewGame, RatingAdapter.ViewHolder> {

    private OnItemClickListener listener;

    public RatingAdapter(@NonNull FirestoreRecyclerOptions<InstallNewGame> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull RatingAdapter.ViewHolder holder, int position, @NonNull InstallNewGame model) {
        holder.type.setText(model.getGameType());
        holder.date.setText(model.getDate());
        String rate = (model.getRate() * 100) + "00000";
        if (model.getRate() == 0 || model.getRate() == 1)
            holder.rate.setText(model.getRate() * 100 + " %");
        else
            holder.rate.setText(rate.substring(0, 4) + " %");
    }

    @NonNull
    @Override
    public RatingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.install_game_item, parent, false);
        return new RatingAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView type;
        TextView date;
        TextView rate;
        ImageView imageViewMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.textView_game_type);
            date = itemView.findViewById(R.id.textView_date);
            rate = itemView.findViewById(R.id.textView_rate);
            imageViewMenu = itemView.findViewById(R.id.imageView_menu_id);
            imageViewMenu.setOnClickListener(new View.OnClickListener() {
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
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position, PopupMenu popupMenu);
    }

    public void onItemSetOnClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


}
