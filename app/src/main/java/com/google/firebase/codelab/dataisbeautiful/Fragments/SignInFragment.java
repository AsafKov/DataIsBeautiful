package com.google.firebase.codelab.dataisbeautiful.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.codelab.dataisbeautiful.Activities.MainActivity;
import com.google.firebase.codelab.dataisbeautiful.R;

public class SignInFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "SignInFragment";

    private EditText mEmailEt, mPasswordEt;
    private Button mSubmitBtn;
    private TextView mRegisterBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        initializeViews(view);
        return view;
    }

    private void initializeViews(View layoutView) {
        mEmailEt = layoutView.findViewById(R.id.sign_in_email);
        mPasswordEt = layoutView.findViewById(R.id.sign_in_password);
        mSubmitBtn = layoutView.findViewById(R.id.sign_in_submit_btn);
        mRegisterBtn = layoutView.findViewById(R.id.sign_in_register_btn);

        mSubmitBtn.setOnClickListener(this);
        mRegisterBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_submit_btn: {
                signIn();
            }
            break;
            case R.id.sign_in_register_btn: {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.auth_activity_fragment_container, new SignUpFragment());
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            default:
                return;
        }
    }

    private void signIn() {
        final String email = mEmailEt.getText().toString();
        final String password = mPasswordEt.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: success");
                            startActivity(new Intent(getActivity(), MainActivity.class));
                            getActivity().finish();
                        } else {
                            Log.w(TAG, "onComplete: failure", task.getException());
                        }
                    }
                });
    }
}
