package com.snappychat.profile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.snappychat.R;
import com.snappychat.model.User;


public class ProfileDataProfQuestionsFragment extends Fragment {

    private EditText userLocation;
    private EditText userProfession;
    private Button next;
    private Button back;
    private Button save;
    private User locationUser;
    private TextView warning;
    private OnFragmentInteractionListener mListener;
    private String operationToPerform;

    public ProfileDataProfQuestionsFragment() {

    }


    public static ProfileDataProfQuestionsFragment newInstance(String param1, String param2) {
        ProfileDataProfQuestionsFragment fragment = new ProfileDataProfQuestionsFragment();
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
        View view =  inflater.inflate(R.layout.fragment_proile_data_prof_loc, container, false);

        userLocation = (EditText)view.findViewById(R.id.location);
        userProfession = (EditText)view.findViewById(R.id.profession);
        next = (Button)view.findViewById(R.id.proffesion_question_next);
        back = (Button) view.findViewById(R.id.proffesion_question_back);
        save = (Button) view.findViewById(R.id.proffesion_question_save);
        warning = (TextView) view.findViewById(R.id.location_warning);
        locationUser = ((ProfileDataCollectorActivity)getActivity()).getUserObject();
        operationToPerform = ((ProfileDataCollectorActivity)getActivity()).getOperation();
        setInitialValues();

        userLocation.setOnFocusChangeListener(cellCleaner);
        userProfession.setOnFocusChangeListener(cellCleaner);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ValidateCells()) {
                    locationUser.setLocation(userLocation.getText().toString());
                    locationUser.setProfession(userLocation.getText().toString());
                    ((ProfileDataCollectorActivity) getActivity()).nextpagehandler("profession");
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ProfileDataCollectorActivity)getActivity()).prevpagehandler("profession");
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ValidateCells()) {
                    locationUser.setLocation(userLocation.getText().toString());
                    locationUser.setProfession(userProfession.getText().toString());
                    ((ProfileDataCollectorActivity) getActivity()).savePageHandeler();
                }
            }
        });

        if (operationToPerform.equals(ProfileDataCollectorActivity.EDIT))
        {
            next.setVisibility(View.INVISIBLE);
            back.setVisibility(View.INVISIBLE);
            save.setVisibility(View.VISIBLE);
        }
        else if (operationToPerform.equals(ProfileDataCollectorActivity.NEW))
        {
            next.setVisibility(View.VISIBLE);
            back.setVisibility(View.VISIBLE);
            save.setVisibility(View.INVISIBLE);
        }

        return view;
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

    private void setInitialValues()
    {
        if (locationUser.getLocation()==null)
        {
            userLocation.setText("Location");
        }
        else
        {
            userLocation.setText(locationUser.getLocation());
        }
        if (locationUser.getProfession()==null)
        {
            userProfession.setText("Profession");
        }
        else
        {
            userProfession.setText(locationUser.getProfession());
        }
    }
    private View.OnFocusChangeListener cellCleaner = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (view == userLocation)
            {
                if ((userLocation.getText().toString()).equals("")||(userLocation.getText().toString()).equals("Location"))
                {
                    userLocation.setText("");
                }
            }
            else
            {
                if ((userProfession.getText().toString()).equals("")||(userProfession.getText().toString()).equals("Profession"))
                {
                    userProfession.setText("");
                }
            }
        }
    };

    private boolean ValidateCells()
    {
        boolean flag = true;
        String stringProfession = userProfession.getText().toString();
        String stringLocation = userLocation.getText().toString();
        if (stringProfession.length() == 0 || stringProfession.equals("Profession")) {
            warning.setText("Please Fill All the Fields");
            userProfession.setBackground(getResources().getDrawable(R.drawable.edittext_warning));
            flag = false;
        } else {
            userProfession.setBackground(getResources().getDrawable(R.drawable.edittext_underline));
        }
        if (stringLocation.length() == 0 || stringLocation.equals("Location")) {
            warning.setText("Please Fill All the Fields");
            userLocation.setBackground(getResources().getDrawable(R.drawable.edittext_warning));
            flag = false;
        } else {
            userLocation.setBackground(getResources().getDrawable(R.drawable.edittext_underline));
        }
        if (flag) {
            warning.setText("");
        }
        return flag;
    }
}
