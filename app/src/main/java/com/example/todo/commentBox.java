package com.example.todo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class commentBox extends Fragment {
    private static final String initialCommentKey = "initial";

    public commentBox() {
        // Required empty public constructor
    }

    public static commentBox newInstance() {
        return newInstance("");
    }
    public static commentBox newInstance(String intialComment) {
        commentBox fragment = new commentBox();
        Bundle args = new Bundle();
        args.putString(initialCommentKey, intialComment);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    ((TextView)getView().findViewById(R.id.comment)).setText(getArguments().getString(initialCommentKey));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_comment_box, container, false);
        ((TextView)v.findViewById(R.id.comment)).setText(getArguments().getString(initialCommentKey));
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
