package com.moomen.chessrays.ui.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moomen.chessrays.R;
import com.moomen.chessrays.ui.Activity.IntroActivity;
import com.moomen.chessrays.ui.Activity.MainActivity;
import com.moomen.chessrays.ui.Activity.SplashActivity;
import com.moomen.chessrays.utils.PreferenceUtils;

import org.jetbrains.annotations.NotNull;

public class SignOutFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_out, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        TextView name = view.findViewById(R.id.textView_user_name_id);
        name.setText(PreferenceUtils.getName(getContext()));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PreferenceUtils.saveName("", getContext());
                startActivity(new Intent(getContext(),IntroActivity.class));
                getActivity().finish();
            }
        }, 1000);
        super.onViewCreated(view, savedInstanceState);
    }
}