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
import android.widget.TextView;
import com.example.android.fvmpopularmovieapp.R;
import com.example.android.fvmpopularmovieapp.data_model.Review;
import com.example.android.fvmpopularmovieapp.utilities.UtilMethods;
import butterknife.Bind;
import butterknife.ButterKnife;


public class ShowReviewDialog extends DialogFragment {

    public static final String REVIEW_AUTHOR  = "review_author";
    public static final String REVIEW_CONTENT = "review_content";

    private String mAuthor;
    private String mReview;

    @Bind(R.id.author)
    TextView tv_author;
    @Bind(R.id.review)
    TextView tv_review;

    public ShowReviewDialog(){

    }

    public static ShowReviewDialog newInstance(Review review){

        ShowReviewDialog showReviewDialog = new ShowReviewDialog();

        Bundle bundle = new Bundle();
        bundle.putString(REVIEW_AUTHOR, review.getAuthor());
        bundle.putString(REVIEW_CONTENT, review.getContent());
        showReviewDialog.setArguments(bundle);
        return showReviewDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        mAuthor  = bundle.getString(REVIEW_AUTHOR);
        mReview  = bundle.getString(REVIEW_CONTENT);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.dialog_review_layout, null);
        ButterKnife.bind(this, rootView);

        tv_author.setText(mAuthor);
        tv_review.setText(mReview.replace("\n\n", "\n"));

        tv_author.setTypeface(UtilMethods.getTypeface(getActivity(), 1));
        tv_review.setTypeface(UtilMethods.getTypeface(getActivity(), 0));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomTheme_Dialog);
        builder
                .setView(rootView)
                .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ShowReviewDialog.this.getDialog().cancel();
                    }
                });

        builder.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    ShowReviewDialog.this.getDialog().dismiss();
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
