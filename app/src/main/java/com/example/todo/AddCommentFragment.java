package com.example.todo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


interface AddCommentListener {
    void OnAddCommentButtonClick();
}

public class AddCommentFragment extends Fragment {

    private AddCommentListener mListener;
    private Button button;

//    private OnFragmentInteractionListener mListener;

    public AddCommentFragment() {
        // Required empty public constructor
    }

    public static AddCommentFragment newInstance() {
        AddCommentFragment fragment = new AddCommentFragment();
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_comment, container, false);
        button = v.findViewById(R.id.add_comment_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCommentFragment.this.onButtonPressed();
            }
        });
        return v;
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.OnAddCommentButtonClick();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddCommentListener) {
            mListener = (AddCommentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AddCommentListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
