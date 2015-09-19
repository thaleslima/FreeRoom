package com.ciandt.app.freeroom.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ciandt.app.freeroom.R;
import com.ciandt.app.freeroom.constants.Constants;

/**
 * Created by thales on 9/18/15.
 */
public class ConfirmDialog extends DialogFragment {

    public interface ConfirmDialogListener{
        void onDialogOkClick();
    }

    ConfirmDialogListener mListener;

    private EditText mPassword;
    private Button mButtonOk;
    private Button mButtonCancel;

    public static ConfirmDialog newInstance() {
        ConfirmDialog frag = new ConfirmDialog();
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflator = getActivity().getLayoutInflater();
        View view = inflator.inflate(R.layout.fragment_confirm_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        mPassword = (EditText) view.findViewById(R.id.edit_text_password);
        mButtonOk = (Button) view.findViewById(R.id.button_ok);
        mButtonCancel = (Button) view.findViewById(R.id.button_cancel);

        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog.this.dismiss();
            }
        });
        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmClick();
            }
        });

        mPassword.requestFocus();

        return builder.create();
    }

    private void confirmClick() {
        if (Constants.Admin.PASSWORD.equals(mPassword.getText().toString())) {
            mListener.onDialogOkClick();
            dismiss();
        } else {
            mPassword.setError(getActivity().getString(R.string.dialog_password_error));
        }
    }

    public void show(FragmentManager manager, String tag, ConfirmDialogListener listener) {
        super.show(manager, tag);
        mListener = listener;
    }
}

