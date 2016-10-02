package com.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fs.lib.util.MyLogger;
import com.mobiguard.MainActivity;
import com.mobiguard.R;
import com.util.BUILD_TYPES;
import com.util.MyDatabaseHelper;
import com.util.Statics;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements TextWatcher {
    @Bind(R.id.editTextLoginPassword)
    EditText editTextLoginPassword;

    @Bind(R.id.editTextConfirmPassword)
    EditText editTextConfirmPassword;

    @Bind(R.id.buttonLoginPassword)
    Button buttonLoginPassword;

    @Bind(R.id.loginStatusTextView)
    TextView loginStatusTextView;

    private boolean isLogin = false;
    private MyDatabaseHelper db;
    private int attemptCount = 0;
    private int MAX_ATTEMPT = 3;
    private String salt = "secret";
    private UserHelper userHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        editTextConfirmPassword.addTextChangedListener(this);
        db = MyDatabaseHelper.getInstance(getApplicationContext());
        userHelper = UserHelper.getInstance(getApplicationContext());
        if (Statics.buildType == BUILD_TYPES.RELEASE) {
            MyLogger.debug("Release");
            checkIfUserPresent();
        } else {
            // Build type is Debug, so, make user logged in
            try {
                userHelper.createNewUser(MyDatabaseHelper.testPassword);
                UserHelper.makeUserLoggedIn(db);
                finish();
                startMainActivity();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkIfUserPresent() {

        try {
            Dao<User, Long> db = this.db.getDao(User.class);
            int users = db.queryForAll().size();
            if (users == 0) {
                // show signup
                editTextConfirmPassword.setVisibility(View.VISIBLE);
                isLogin = false;
                loginStatusTextView.setText("Set Master Passwords");
                buttonLoginPassword.setText("Sign Up");

            } else {
                editTextLoginPassword.addTextChangedListener(this);
                // show login
                isLogin = true;
                loginStatusTextView.setText("Enter Your Password");
                buttonLoginPassword.setText("Login");
            }
        } catch (Exception e) {
            MyLogger.show(getApplicationContext(), e.getMessage());
        }
    }

    public boolean isLoginCorrect() throws SQLException {
        String passwordLogin = UserHelper.getHashedPasswordAsString(editTextLoginPassword.getText().toString());
        QueryBuilder<User, Long> userDao = db.getUserDao().queryBuilder();
        User userInDb = userDao.where().eq("id", MyDatabaseHelper.defaultUserId).queryForFirst();
        boolean loginMatch = passwordLogin.matches(userInDb.getPassword());
        return loginMatch;
    }


    @OnClick(R.id.buttonLoginPassword)
    public void buttonLoginPassword() {
        // MyLogger.show(getApplicationContext(),"Button clicked");
        if (attemptCount == MAX_ATTEMPT) {
            // todo capture picture with front camera and store in phone.
            return;
        }

        if (isLogin) {
            try {
                if (isLoginCorrect()) {
                    UserHelper.makeUserLoggedIn(db);
                    startMainActivity();
                } else {
                    MyLogger.show(getApplicationContext(), "Wrong Passwords");
                    attemptCount++;
                }

            } catch (Exception e) {
                MyLogger.show(getApplicationContext(), e.getMessage());
            }
        } else {
            String passwordSignup = editTextLoginPassword.getText().toString();
            String passwordConfirm = editTextConfirmPassword.getText().toString();
            if (passwordSignup.matches("")) {
                MyLogger.show(getApplicationContext(), "Password can no be empty.");
                return;
            }
            if (passwordConfirm.matches("")) {
                MyLogger.show(getApplicationContext(), "Confirm Password can no be empty.");
                return;
            }
            if (isMatched()) {
                // signup
                try {
                    userHelper.createNewUser(passwordSignup);
                    startMainActivity();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                MyLogger.show(getApplicationContext(), "Confirm Passwords does not match.");
            }
        }
    }

    private void starLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void startMainActivity() {
        MyLogger.debug("Starting MainActivity.");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public boolean isMatched() {
        String passwordSignup = editTextLoginPassword.getText().toString();
        String passwordConfirm = editTextConfirmPassword.getText().toString();
        boolean isMatched = passwordSignup.matches(passwordConfirm);
        return isMatched;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (isLogin) {
            try {
                if (isLoginCorrect()) {
                    userHelper.createNewUser(editTextConfirmPassword.getText().toString());
                    userHelper.makeUserLoggedIn(db);
                    startMainActivity();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            if (isMatched()) {
                buttonLoginPassword();
            }

        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
