package info.camposha.canopus_ums.view.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.google.android.material.tabs.TabLayout;

import info.camposha.canopus_ums.R;
import info.camposha.canopus_ums.common.UserUtils;
import info.camposha.canopus_ums.data.model.entity.User;
import info.camposha.canopus_ums.databinding.ActivityAccountBinding;
import info.camposha.canopus_ums.view.ui.base.AccountBaseActivity;

import static info.camposha.canopus_ums.common.UserConstants.SUCCEEDED;
import static info.camposha.canopus_ums.common.UserUtils.valOf;

public class AccountActivity extends AccountBaseActivity {
    private User receivedUser;
    private ActivityAccountBinding b;
    private Intent result = new Intent();


    private void handleEvents() {
        b.tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    b.loginLayout.setVisibility(View.VISIBLE);
                    b.registerLayout.setVisibility(View.GONE);
                } else if (tab.getPosition() == 1) {
                    b.loginLayout.setVisibility(View.GONE);
                    b.registerLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        b.deleteAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (receivedUser != null) {
                    delete(receivedUser);
                }
            }
        });
        b.postBtn.setOnClickListener(view -> {
            if (receivedUser != null) {
                validateThenPOST(true);
            } else {
                validateThenPOST(false);
            }

        });
        b.loginBtn.setOnClickListener(view -> {
            login();
        });
    }

    private void login() {
        if (validate(b.loginEmailTxt, b.loginPasswordTxt, b.loginEmailTxt)) {
            getViewModel().login(valOf(b.loginEmailTxt), valOf(b.loginPasswordTxt)).observe(this, r -> {
                if (makeRequest(r, "USER SIGN IN") == SUCCEEDED) {
                    clearEditTexts(b.loginEmailTxt, b.loginPasswordTxt);
                    result.putExtra("LOGIN_RESULT", r.getUser());
                    setResult(Activity.RESULT_OK, result);
                    finish();
                }
            });
        }
    }

    private void createAccount(User u) {
        getViewModel().createAccount(u).observe(this, r -> {
            if (makeRequest(r, "USER PUBLISHING") == SUCCEEDED) {
                clearEditTexts(b.nameTxt, b.emailTxt, b.passwordTxt, b.securityTxt);
                result.putExtra("REGISTRATION_RESULT", r.getUser());
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });
    }


    private void update(User u) {
        getViewModel().update(u).observe(this, r -> {
            if (makeRequest(r, "USER UPDATE") == SUCCEEDED) {
                result.putExtra("UPDATE_RESULT", r.getUser());
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });
    }

    private void delete(User u) {
        getViewModel().delete(u).observe(this, r -> {
            if (makeRequest(r, "ACCOUNT DELETE") == SUCCEEDED) {
                result.putExtra("DELETE_RESULT", "SUCCESS");
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });
    }

    private void validateThenPOST(boolean isForUpdate) {
        if (validate(b.nameTxt, b.emailTxt, b.passwordTxt, b.securityTxt)) {
            User u;
            if (isForUpdate) {
                u = receivedUser;
            } else {
                u = new User();
            }
            u.setName(valOf(b.nameTxt));
            u.setEmail(valOf(b.emailTxt));
            u.setPassword(valOf(b.passwordTxt));
            u.setSecurityQuestion(valOf(b.securityTxt));

            if (isForUpdate) {
                update(u);
            } else {
                createAccount(u);
            }

        } else {
            show("Please fill up all Fields First");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        receivedUser = UserUtils.receive(getIntent(), this);

        if (receivedUser != null) {
            b.setUser(receivedUser);
            b.postBtn.setText("UPDATE");
            b.deleteAccountBtn.setVisibility(View.VISIBLE);
        } else {
            b.setUser(new User());
            b.postBtn.setText("Create Account");
            b.deleteAccountBtn.setVisibility(View.GONE);
        }
    }

    /**
     * Let's override our onCreate() method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_account);

        this.handleEvents();
    }
}
//end
