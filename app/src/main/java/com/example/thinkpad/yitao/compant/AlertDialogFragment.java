package com.example.thinkpad.yitao.compant;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.example.thinkpad.yitao.R;

/**
 * Created by cjw on 2016/11/17.
 */
public class AlertDialogFragment extends DialogFragment{
    private static final String KEY_TITLE = "title";
    private static final String KEY_MESSAGE = "message";

    @SuppressWarnings("unused")
    public static AlertDialogFragment newInstance(int title) {
        AlertDialogFragment fragment = new AlertDialogFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    public static AlertDialogFragment newInstance(String msg) {
        AlertDialogFragment fragment = new AlertDialogFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_TITLE, R.string.username_pwd_rule);
        args.putString(KEY_MESSAGE, msg);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt(KEY_TITLE);
        String msg = getArguments().getString(KEY_MESSAGE);
        return new AlertDialog.Builder(getActivity(), getTheme())
                .setTitle(title)
                .setMessage(msg)
                .setNeutralButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
    }


    public void show(FragmentManager supportFragmentManager, String string) {

    }
}
