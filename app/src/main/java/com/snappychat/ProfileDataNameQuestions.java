package com.snappychat;

import android.content.Context;
import android.net.Uri;
import android.opengl.EGLDisplay;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.snappychat.model.User;

import org.w3c.dom.Text;


public class ProfileDataNameQuestions extends Fragment {



    private OnFragmentInteractionListener mListener;

    private EditText FirstName;
    private EditText LastName;
    private EditText NickName;
    private TextView Warning;
    private Button Next;

    private User user;

    public ProfileDataNameQuestions() {

    }


    public static ProfileDataNameQuestions newInstance(String param1, String param2) {
        ProfileDataNameQuestions fragment = new ProfileDataNameQuestions();
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

        FirstName = (EditText) view.findViewById(R.id.first_name);
        LastName = (EditText) view.findViewById(R.id.last_name);
        NickName = (EditText) view.findViewById(R.id.nick_name);
        Next = (Button) view.findViewById(R.id.name_question_next);
        Warning = (TextView) view.findViewById(R.id.name_warning);

        user = ((ProfileDataCollector)getActivity()).getUserObject();

        InitialCellValues();

        FirstName.setOnFocusChangeListener(cellcleaner);
        LastName.setOnFocusChangeListener(cellcleaner);
        NickName.setOnFocusChangeListener(cellcleaner);

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ValidateNameFields()) {
                    user.setFirstName(FirstName.getText().toString());
                    user.setLastName(LastName.getText().toString());
                    user.setNickName(NickName.getText().toString());
                    ((ProfileDataCollector)getActivity()).nextpagehandler("name");
                    }
                }
        });

        return view;
    }


    View.OnFocusChangeListener cellcleaner = new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View view, boolean b) {
            if (view == FirstName)
            {
                if (FirstName.getText().toString()=="First Name") {
                        FirstName.setText("");
                }
            }
            else if (view == LastName)
            {
                if (LastName.getText().toString()=="Last Name"){
                    LastName.setText("");
                }
            }
            else
            {
                if (LastName.getText().toString()=="Nick Name")
                NickName.setText("");
            }
        }
        };

    private void InitialCellValues()
    {
        if (user.getFirstName()==null)
            FirstName.setText("First Name");
        else
            FirstName.setText(user.getFirstName());
        if (user.getLastName()==null)
            LastName.setText("Last Name");
        else
            LastName.setText(user.getLastName());
        if (user.getNickName()==null)
            NickName.setText("Nick Name");
        else
            NickName.setText(user.getNickName());
    }
    private boolean ValidateNameFields()
    {
        boolean flag = true;
        String fstname = FirstName.getText().toString();
        String lstname = LastName.getText().toString();
        String nicname = NickName.getText().toString();
        if (fstname.length() == 0 || fstname == "First Name")
        {
            Warning.setText("Please Fill All the Fields");
            FirstName.setBackground(getResources().getDrawable(R.drawable.edittext_warning));
            flag = false;
        }
        else
        {
            FirstName.setBackground(getResources().getDrawable(R.drawable.edittext_underline));
        }
        if (lstname.length() == 0 || lstname == "Last Name")
        {
            Warning.setText("Please Fill All the Fields");
            LastName.setBackground(getResources().getDrawable(R.drawable.edittext_warning));
            flag = false;
        }
        else
        {
            LastName.setBackground(getResources().getDrawable(R.drawable.edittext_underline));
        }
        if (nicname.length() == 0 || nicname == "Nick Name")
        {
            Warning.setText("Please Fill All the Fields");
            NickName.setBackground(getResources().getDrawable(R.drawable.edittext_warning));
            flag = false;
        }
        else
        {
            NickName.setBackground(getResources().getDrawable(R.drawable.edittext_underline));
        }
        if (flag)
        {
            Warning.setText("");
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
