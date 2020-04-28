package com.google.firebase.codelab.dataisbeautiful.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.codelab.dataisbeautiful.Activities.MainActivity;
import com.google.firebase.codelab.dataisbeautiful.R;

public class SignUpFragment extends Fragment implements View.OnClickListener{

    private final String TAG = "SignUpFragment";

    private EditText mEmailEt, mPasswordEt, mConfirmPasswordEt;
    private MultiAutoCompleteTextView mCountryEt;
    private Button mSubmitBtn;
    private TextView mSignInBtn;
    private RadioButton mRadioBtnMale, mRadioBtnFemale;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        initializeViews(view);
        return view;
    }

    private void initializeViews(View layoutView){
        mEmailEt = layoutView.findViewById(R.id.sign_up_email);
        mPasswordEt = layoutView.findViewById(R.id.sign_up_password);
        mConfirmPasswordEt = layoutView.findViewById(R.id.sign_up_confirm_password);
        mSubmitBtn = layoutView.findViewById(R.id.sign_up_submit_btn);
        mSignInBtn = layoutView.findViewById(R.id.sign_up_have_an_account_btn);
        mRadioBtnMale = layoutView.findViewById(R.id.sign_up_radio_btn_male);
        mRadioBtnFemale = layoutView.findViewById(R.id.sign_up_radio_btn_female);

        mSignInBtn.setOnClickListener(this);
        mSubmitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.sign_up_submit_btn: {
                createAnAccount();
            } break;
            case R.id.sign_up_have_an_account_btn: {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.auth_activity_fragment_container, new SignInFragment());
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.commit();
            }
        }
    }

    private void createAnAccount(){
        final String email = mEmailEt.getText().toString();
        final String password = mPasswordEt.getText().toString();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: Success");
                            startActivity(new Intent(getActivity(), MainActivity.class));
                            getActivity().finish();
                        } else {
                            Log.w(TAG, "onComplete: Failure", task.getException());
                        }
                    }
                });
    }
}
