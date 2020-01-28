package com.example.choiceproperties.Views.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.choiceproperties.CallBack.CallBack;
import com.example.choiceproperties.Constant.Constant;
import com.example.choiceproperties.Exception.ExceptionUtil;
import com.example.choiceproperties.Models.User;
import com.example.choiceproperties.R;
import com.example.choiceproperties.Views.dialog.ProgressDialogClass;
import com.example.choiceproperties.preferences.AppSharedPreference;
import com.example.choiceproperties.repository.UserRepository;
import com.example.choiceproperties.repository.impl.UserRepositoryImpl;
import com.example.choiceproperties.utilities.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.concurrent.TimeUnit;

import static com.example.choiceproperties.Constant.Constant.SALES_PREFIX;

public class Registeractivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener, View.OnTouchListener, View.OnClickListener {
    TextView txtlogin, txttc;
    Button btlogin, btnotp;
    EditText etname, etmobile, etPassword, etotp;
    private UserRepository userRepository;
    private ProgressDialogClass progressDialogClass;
    private AppSharedPreference appSharedPreference;


    private String verificationId;
    private FirebaseAuth mAuth;
//    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        userRepository = new UserRepositoryImpl(this);
        progressDialogClass = new ProgressDialogClass(this);
        appSharedPreference = new AppSharedPreference(this);
        mAuth = FirebaseAuth.getInstance();

        btlogin = (Button) findViewById(R.id.buttonsubmit);
        btnotp = (Button) findViewById(R.id.buttongenerateotp);
        etname = (EditText) findViewById(R.id.edittextname);
        etmobile = (EditText) findViewById(R.id.edittextmobile);
        etPassword = (EditText) findViewById(R.id.edittextpassword);
        etotp = (EditText) findViewById(R.id.edittextenterotp);


        txttc = (TextView) findViewById(R.id.txttermsandconditions);
        txtlogin = (TextView) findViewById(R.id.textViewLogin);
        setClickListners();
        setTouchListner();
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private void setClickListners() {
        btlogin.setOnClickListener(this);
        txttc.setOnClickListener(this);
        txtlogin.setOnClickListener(this);
        btnotp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonsubmit:
                String code = etotp.getText().toString();
//                validateAndCreateUser();
                verifyCode(code);
                break;
            case R.id.txttermsandconditions:
//                Intent intent = new Intent(Registeractivity.this, TermsCondition_Activity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.backslide_in, R.anim.backslide_out);
                break;
            case R.id.buttongenerateotp:
                String phonenumber = "+91" + etmobile.getText().toString();
                sendVerificationCode(phonenumber);
                break;
            case R.id.textViewLogin:
                Intent intent1 = new Intent(Registeractivity.this, LoginScreen.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.backslide_in, R.anim.backslide_out);
                break;
        }
    }

    private void setTouchListner() {

        etname.setOnTouchListener(this);
        etmobile.setOnTouchListener(this);
        etotp.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Animation zoomOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
        view.startAnimation(zoomOutAnimation);
        return false;
    }

    private void validateAndCreateUser() {
        User user = fillUserModel();
        if (validate(user))
            createUser(user);
    }

    private User fillUserModel() {
        User user = new User();
        user.setUserId(Constant.USER_TABLE_REF.push().getKey());
        user.setUserName(etname.getText().toString());
        user.setMobileNumber(etmobile.getText().toString());
        user.setPassword(etPassword.getText().toString());
        user.setRole(Constant.ROLE_SALES);
        user.setStatus(Constant.STATUS_DEACTIVE);
        user.setAgentId(Utility.generateAgentId(SALES_PREFIX));

        return user;
    }

    private boolean validate(User user) {
        String validationMessage;
        boolean isValid = true;
        try {
            if (Utility.isEmptyOrNull(user.getUserName())) {
                validationMessage = "User Nmae Should not be empty";
                etname.setError(validationMessage);
                isValid = false;
            }
            if (Utility.isEmptyOrNull(user.getMobileNumber())) {
                validationMessage = getString(R.string.MOBILE_NUMBER_SHOULD_NOT_BE_EMPTY);
                etmobile.setError(validationMessage);
                isValid = false;
            } else if (!Utility.isValidMobileNumber(user.getMobileNumber())) {
                validationMessage = getMessage(R.string.INVALID_MOBILE_NUMBER);
                etmobile.setError(validationMessage);
                isValid = false;
            }
            if (Utility.isEmptyOrNull(user.getPassword())) {
                validationMessage = getString(R.string.PASSWORD_SHOULD_NOT_BE_EMPTY);
                etPassword.setError(validationMessage);
                isValid = false;
            }
        } catch (Exception e) {
            isValid = false;
            ExceptionUtil.logException(e);
        }
        return isValid;
    }


    private String getMessage(int id) {
        return getString(id);
    }

    private void createUser(final User user) {
        progressDialogClass.showDialog(getMessage(R.string.loading), getMessage(R.string.PLEASE_WAIT));
        userRepository.createAdminData(user, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                addUserDataToPreferences(user);
                loginToApp();
            }

            @Override
            public void onError(Object object) {
                Utility.showMessage(Registeractivity.this, getMessage(R.string.registration_fail));
                progressDialogClass.dismissDialog();
            }
        });
    }

    private void addUserDataToPreferences(User user) {
        appSharedPreference.addUserDetails(user);
        appSharedPreference.createUserLoginSession();
    }

    private void loginToApp() {
        Toast.makeText(Registeractivity.this, "User Register Successfully", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(Registeractivity.this, LoginScreen.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private void sendVerificationCode(String number) {
//        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                etotp.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Registeractivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            validateAndCreateUser();
//                            User user = fillUserModel();
//                            Users user = new Users();
//                            user.setName(username);
//                            user.setMobilenumber(phonenumber);
//                            user.setAgentId(Utility.generateAgentId(AGENT_PREFIX));
//                            String uploadId = mDatabase.push().getKey();
//                            user.setUserid(uploadId);
//                            mDatabase.child(uploadId).setValue(user);
//                            appSharedPreference.addUserDetails(user);
//                            appSharedPreference.createUserLoginSession();
//                            Intent intent = new Intent(Registeractivity.this, MainActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//                            startActivity(intent);

                        } else {
                            Toast.makeText(Registeractivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}//end of activity
