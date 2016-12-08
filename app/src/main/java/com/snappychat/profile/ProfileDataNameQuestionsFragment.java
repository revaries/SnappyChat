package com.snappychat.profile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.snappychat.R;
import com.snappychat.model.User;


public class ProfileDataNameQuestionsFragment extends Fragment {



    private OnFragmentInteractionListener mListener;

    private EditText firstName;
    private EditText lastName;
    private EditText nickName;
    private TextView warning;
    private Button next;
    private Button save;

    private User NameUser;
    private String operationToPerform;
    public ProfileDataNameQuestionsFragment() {

    }


    public static ProfileDataNameQuestionsFragment newInstance(String param1, String param2) {
        ProfileDataNameQuestionsFragment fragment = new ProfileDataNameQuestionsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("Inside Fragment one","Inside Fragment one");
        View view =  inflater.inflate(R.layout.fragment_profile_data_name_questions, container, false);

        firstName = (EditText) view.findViewById(R.id.first_name);
        lastName = (EditText) view.findViewById(R.id.last_name);
        nickName = (EditText) view.findViewById(R.id.nick_name);
        next = (Button) view.findViewById(R.id.name_question_next);
        warning = (TextView) view.findViewById(R.id.name_warning);
        save = (Button) view.findViewById(R.id.name_question_save);

        NameUser = ((ProfileDataCollectorActivity)getActivity()).getUserObject();
        operationToPerform = ((ProfileDataCollectorActivity)getActivity()).getOperation();

        InitialCellValues();

        firstName.setOnFocusChangeListener(cellcleaner);
        lastName.setOnFocusChangeListener(cellcleaner);
        nickName.setOnFocusChangeListener(cellcleaner);



        if (operationToPerform.equals(ProfileDataCollectorActivity.EDIT))
        {
            save.setVisibility(View.VISIBLE);
            next.setVisibility(View.INVISIBLE);
        }
        else if (operationToPerform.equals(ProfileDataCollectorActivity.NEW)) {
            save.setVisibility(View.INVISIBLE);
            next.setVisibility(View.VISIBLE);
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ValidateNameFields()) {
                    NameUser.setFirstName(firstName.getText().toString());
                    NameUser.setLastName(lastName.getText().toString());
                    NameUser.setNickName(nickName.getText().toString());
                    ((ProfileDataCollectorActivity)getActivity()).nextpagehandler("name");
                    }
                }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ValidateNameFields())
                {
                    NameUser.setFirstName(firstName.getText().toString());
                    NameUser.setLastName(lastName.getText().toString());
                    NameUser.setNickName(nickName.getText().toString());
                    ((ProfileDataCollectorActivity)getActivity()).savePageHandeler();

                }
            }
        });

        return view;
    }


    View.OnFocusChangeListener cellcleaner = new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View view, boolean b) {
            if (view == firstName)
            {
                if (firstName.getText().toString().equals("First Name")) {
                        firstName.setText("");
                }
            }
            else if (view == lastName)
            {
                if (lastName.getText().toString().equals("Last Name")){
                    lastName.setText("");
                }
            }
            else
            {
                if (nickName.getText().toString().equals("Nick Name"))
                nickName.setText("");
            }
        }
        };

    private void InitialCellValues()
    {
        if (NameUser.getFirstName()==null)
            firstName.setText("First Name");
        else
            firstName.setText(NameUser.getFirstName());
        if (NameUser.getLastName()==null)
            lastName.setText("Last Name");
        else
            lastName.setText(NameUser.getLastName());
        if (NameUser.getNickName()==null)
            nickName.setText("Nick Name");
        else
            nickName.setText(NameUser.getNickName());
    }
    private boolean ValidateNameFields()
    {
        boolean flag = true;
        String fstname = firstName.getText().toString();
        String lstname = lastName.getText().toString();
        String nicname = nickName.getText().toString();
        if (fstname.length() == 0 || fstname.equals("First Name"))
        {
            warning.setText("Please Fill All the Fields");
            firstName.setBackground(getResources().getDrawable(R.drawable.edittext_warning));
            flag = false;
        }
        else
        {
            firstName.setBackground(getResources().getDrawable(R.drawable.edittext_underline));
        }
        if (lstname.length() == 0 || lstname.equals("Last Name"))
        {
            warning.setText("Please Fill All the Fields");
            lastName.setBackground(getResources().getDrawable(R.drawable.edittext_warning));
            flag = false;
        }
        else
        {
            lastName.setBackground(getResources().getDrawable(R.drawable.edittext_underline));
        }
        if (nicname.length() == 0 || nicname.equals("Nick Name"))
        {
            warning.setText("Please Fill All the Fields");
            nickName.setBackground(getResources().getDrawable(R.drawable.edittext_warning));
            flag = false;
        }
        else
        {
            nickName.setBackground(getResources().getDrawable(R.drawable.edittext_underline));
        }
        if (flag)
        {
            warning.setText("");
        }

        return flag;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
