package com.codepath.simpletodo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.codepath.simpletodo.R.id.lvItems;
import static com.codepath.simpletodo.R.id.spinner;

/**
 * Created by culgnol on 8/13/17.
 */

public class EditItemDialogFrament extends DialogFragment {
    private EditText mEditText;
    private Button mButtonSave;
    private int itemPosition;

    private NumberPicker numberPicker;
    private ArrayAdapter<String> spinAdapter;

    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText, int position, int remindDays);
    }


    public EditItemDialogFrament() {

    }

    public static EditItemDialogFrament newInstance(String item_body, int item_pos, int days) {
        EditItemDialogFrament frag = new EditItemDialogFrament();
        Bundle args = new Bundle();
        args.putString("item_body", item_body);
        args.putInt("item_pos", item_pos);
        args.putInt("item_days", days);
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
        mButtonSave = (Button) view.findViewById(R.id.btnSave);
        // Fetch arguments from bundle and set title
        String item_body = getArguments().getString("item_body", "Ooops....");
        itemPosition = getArguments().getInt("item_pos");
        //getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        mEditText.setText(item_body);
        mEditText.requestFocus();
//        getDialog().getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mButtonSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                EditNameDialogListener listener = (EditNameDialogListener) getActivity();
                listener.onFinishEditDialog(mEditText.getText().toString(), itemPosition, numberPicker.getValue());
                dismiss();
            }
        });

        numberPicker = (NumberPicker) view.findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(7);
        numberPicker.setMinValue(0);
        numberPicker.setWrapSelectorWheel(true);
//        spinAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, days);
//        spinner.setAdapter(spinAdapter);

        numberPicker.setValue(getArguments().getInt("item_days", 1));

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
}
