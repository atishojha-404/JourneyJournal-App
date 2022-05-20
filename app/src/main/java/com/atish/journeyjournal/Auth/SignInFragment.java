package com.atish.journeyjournal.Auth;

import static com.atish.journeyjournal.Auth.RegisterActivity.onResetPasswordFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.atish.journeyjournal.MainActivity;
import com.atish.journeyjournal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInFragment extends Fragment {

    public SignInFragment() {
        // Required empty public constructor
    }

    private TextView needAnAccount, forgotPassword;
    private FrameLayout parentFrameLayout;

    private EditText email, password;
    private ImageButton close;
    private CheckBox rememberme;
    private Button signInBtn;
    private ProgressBar progressBar;

    private SharedPreferences sharedPreferences;

    private FirebaseAuth firebaseAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        sharedPreferences = requireActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        parentFrameLayout = requireActivity().findViewById(R.id.register_fragment);

        needAnAccount = view.findViewById(R.id.orsign_up);
        forgotPassword = view.findViewById(R.id.forgotpass);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        close = view.findViewById(R.id.close);
        rememberme = view.findViewById(R.id.rememberme);
        signInBtn = view.findViewById(R.id.signinbtn);
        progressBar = view.findViewById(R.id.signin_progess);

        firebaseAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        needAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignUpFragment());
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onResetPasswordFragment = true;
                setFragment(new ResetPasswordFragment());
            }
        });

//        to check email edit text
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//              no use for now
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {
//              no use for now
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                no use for now
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {
//              no use for now
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailAndPassword();
            }
        });

        if(sharedPreferences.getBoolean("rememberme", false)){
            Intent mainIntent = new Intent(getActivity(), MainActivity.class);
            Toast.makeText(getActivity(), "User Logged In", Toast.LENGTH_SHORT).show();
            startActivity(mainIntent);
            getActivity().finish();

//            To make email and password pre saved
//            String registeredEmail = sharedPreferences.getString("email", "");
//            String registeredPassword = sharedPreferences.getString("password", "");
//            email.setText(registeredEmail);
//            password.setText(registeredPassword);
        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
    }


    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_form_right, R.anim.slideout_from_left);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    @SuppressLint("ResourceAsColor")
    private void checkInputs() {
        if(!TextUtils.isEmpty(email.getText())){
            if(!TextUtils.isEmpty(password.getText()) && password.length() >= 6){
//               change signin button
                signInBtn.setEnabled(true);
                signInBtn.setTextColor(getResources().getColor(R.color.btntxt));
            }else {
                signInBtn.setEnabled(false);
                signInBtn.setTextColor(getResources().getColor(R.color.btntxtdisable));
            }
        }else {
            signInBtn.setEnabled(false);
            signInBtn.setTextColor(getResources().getColor(R.color.btntxtdisable));
        }
    }

    @SuppressLint("ResourceAsColor")
    private void checkEmailAndPassword() {
        if (Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            if(password.length() >= 6){

//              if every thing goes well than change these
                progressBar.setVisibility(View.VISIBLE);
                signInBtn.setEnabled(false);
                signInBtn.setTextColor(getResources().getColor(R.color.btntxtdisable));

                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    if(rememberme.isChecked()){
                                        sharedPreferences.edit().putBoolean("rememberme", true).commit();
                                    }

                                    Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                                    Toast.makeText(getActivity(), "User Logged In", Toast.LENGTH_SHORT).show();
                                    startActivity(mainIntent);
                                    getActivity().finish();
                                }else{
//                                    if every thing doesn't go well than change these
                                    progressBar.setVisibility(View.INVISIBLE);
                                    signInBtn.setEnabled(true);
                                    signInBtn.setTextColor(getResources().getColor(R.color.btntxt));
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }else{
                Toast.makeText(getActivity(), "Incorrect email or password!", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getActivity(), "Incorrect email or password!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAlertDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(requireActivity());
        dialog.setTitle("Exit application");
        dialog.setMessage("Are you sure?");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requireActivity().finish();
            }
        });
        dialog.setNegativeButton("Cancel", null);
        dialog.setCancelable(false);
        dialog.show();
    }
}