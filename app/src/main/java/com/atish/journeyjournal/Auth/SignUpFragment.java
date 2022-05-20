package com.atish.journeyjournal.Auth;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.atish.journeyjournal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class SignUpFragment extends Fragment {


    public SignUpFragment() {
        // Required empty public constructor
    }

    //    define variables
    private TextView alreadyHaveAnAccount;
    private FrameLayout parentFrameLayout;

    private EditText email, fullName, password, confirmPassword;
    private ImageButton close;
    private Button signUp;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        parentFrameLayout = getActivity().findViewById(R.id.register_fragment);

        alreadyHaveAnAccount = view.findViewById(R.id.orsign_in);
        email = view.findViewById(R.id.email);
        fullName = view.findViewById(R.id.full_name);
        password = view.findViewById(R.id.password);
        confirmPassword = view.findViewById(R.id.confirmPassword);
        close = view.findViewById(R.id.close);
        signUp = view.findViewById(R.id.signup);
        progressBar = view.findViewById(R.id.signup_progess);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        what to do if user clicked on already have an account
        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
        });

//        to check email edit text
        email.addTextChangedListener(new TextWatcher() {
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
//          no use for now
            }
        });

//        to check fullname edit text
        fullName.addTextChangedListener(new TextWatcher() {
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
//                no use for now
            }
        });

//        to check password edit text
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

//        to check confirm password edit text
        confirmPassword.addTextChangedListener(new TextWatcher() {
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
//                no use for now
            }
        });

//        what to o if user clicked in signup button
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                send data to firebase
                checkEmailAndPassword();

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
    }


    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    @SuppressLint("ResourceAsColor")
    private void checkInputs() {
//        check if email is empty or not
        if (!TextUtils.isEmpty(email.getText())) {
//            check if full name is empty or not
            if (!TextUtils.isEmpty(fullName.getText())) {
//                check if password have length 6 or not
                if (!TextUtils.isEmpty(password.getText()) && password.length() >= 6) {
//                    check confirm password is empty or not
                    if (!TextUtils.isEmpty(confirmPassword.getText())) {
//                        change signup button
                        signUp.setEnabled(true);
                        signUp.setTextColor(getResources().getColor(R.color.btntxt));
                    } else {
                        signUp.setEnabled(false);
                        signUp.setTextColor(getResources().getColor(R.color.btntxtdisable));
                    }

                } else {
                    signUp.setEnabled(false);
                    signUp.setTextColor(getResources().getColor(R.color.btntxtdisable));
                }

            } else {
                signUp.setEnabled(false);
                signUp.setTextColor(getResources().getColor(R.color.btntxtdisable));
            }
        } else {
            signUp.setEnabled(false);
            signUp.setTextColor(getResources().getColor(R.color.btntxtdisable));
        }
    }

    @SuppressLint("ResourceAsColor")
    private void checkEmailAndPassword() {
//        check if given email is valid or not
        if (Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
//            check if given password is equals to confirm password
            if (password.getText().toString().equals(confirmPassword.getText().toString())) {

//                change progress bar  and signup button
                progressBar.setVisibility(View.VISIBLE);
                signUp.setEnabled(false);
                signUp.setTextColor(R.color.btntxtdisable);

//                connect with firebase
                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    Map<Object, String> userdata = new HashMap<>();
                                    userdata.put("fullname", fullName.getText().toString());
                                    userdata.put("password", password.getText().toString());
                                    userdata.put("email", email.getText().toString());
                                    firebaseFirestore.collection("USERS")
                                            .add(userdata)
                                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                                    if (task.isSuccessful()) {

//                                    if every thing is good then return to login page
                                                        Intent mainIntent = new Intent(getActivity(), RegisterActivity.class);
                                                        Toast.makeText(getActivity(), "User successfully registered", Toast.LENGTH_SHORT).show();
                                                        startActivity(mainIntent);
                                                        getActivity().finish();

                                                        FirebaseUser usr = firebaseAuth.getCurrentUser();
                                                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                                                .setDisplayName(fullName.getText().toString())
                                                                .build();
                                                        usr.updateProfile(request);

                                                    } else {
//                                    if every thing doesn't go well than change these
                                                        progressBar.setVisibility(View.INVISIBLE);
                                                        signUp.setEnabled(true);
                                                        signUp.setTextColor(getResources().getColor(R.color.btntxt));
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                } else {
//                                    if every thing doesn't go well than change these
                                    progressBar.setVisibility(View.INVISIBLE);
                                    signUp.setEnabled(true);
                                    signUp.setTextColor(getResources().getColor(R.color.btntxt));
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            } else {
//                if password not matched
                confirmPassword.setError("Password doesn't matched!");
            }
        } else {
//            if email address is not valid
            email.setError("Invalid Email!");
        }
    }

    private void showAlertDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Exit application");
        dialog.setMessage("Are you sure?");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getActivity().finish();
            }
        });
        dialog.setNegativeButton("Cancel", null);
        dialog.setCancelable(false);
        dialog.show();
    }
}