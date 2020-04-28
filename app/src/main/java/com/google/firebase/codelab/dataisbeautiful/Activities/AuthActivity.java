package com.google.firebase.codelab.dataisbeautiful.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.google.firebase.codelab.dataisbeautiful.Fragments.SignInFragment;
import com.google.firebase.codelab.dataisbeautiful.R;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.auth_activity_fragment_container, new SignInFragment()).commit();
    }
}
