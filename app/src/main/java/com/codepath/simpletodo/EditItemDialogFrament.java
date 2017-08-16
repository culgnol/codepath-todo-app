package com.codepath.simpletodo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

/**
 * Created by culgnol on 8/13/17.
 */

public class EditItemDialogFrament extends DialogFragment {
    private EditText mEditText;

    public EditItemDialogFrament() {

    }

    public static EditItemDialogFrament newInstance(String item_body) {
        EditItemDialogFrament frag = new EditItemDialogFrament();
        Bundle args = new Bundle();
        args.putString("item_body", item_body);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_item, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        mEditText = (EditText) view.findViewById(R.id.etEditItem);
        // Fetch arguments from bundle and set title
        String item_body = getArguments().getString("item_body", "Ooops....");
        //getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        mEditText.setText(item_body);
        mEditText.requestFocus();
//        getDialog().getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    // Used to fill screen
    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }

    public void onSubmit(View v) {
        this.dismiss();
    }
}
