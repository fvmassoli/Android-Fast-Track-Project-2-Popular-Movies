package com.example.android.fvmpopularmovieapp.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.android.fvmpopularmovieapp.R;
import butterknife.Bind;
import butterknife.ButterKnife;


public class RemoveFavoriteMovieDialog extends DialogFragment {

    @Bind(R.id.icon)
    ImageView icon;
    @Bind(R.id.message)
    TextView message;

    public RemoveFavoriteMovieDialog(){

    }

    public static RemoveFavoriteMovieDialog newInstance(){
        RemoveFavoriteMovieDialog removeFavoriteMovieDialog = new RemoveFavoriteMovieDialog();
        return removeFavoriteMovieDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.dialog_layout, null);
        ButterKnife.bind(this, rootView);

        icon.setImageResource(R.drawable.ic_star_border_yellow_24dp);
        message.setText(R.string.removed_from_favorites);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomTheme_Dialog);
        builder
                .setView(rootView)
                .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        RemoveFavoriteMovieDialog.this.getDialog().cancel();
                    }
                });

        builder.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    RemoveFavoriteMovieDialog.this.getDialog().dismiss();
                }
                return true;
            }
        });

        final AlertDialog alert = builder.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button btnNegative = alert.getButton(Dialog.BUTTON_NEGATIVE);
                btnNegative.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
            }
        });

        return alert;
    }
}
