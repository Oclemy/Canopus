package info.camposha.canopus_ums.view.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.ogaclejapan.smarttablayout.utils.ViewPagerItem;
import com.ogaclejapan.smarttablayout.utils.ViewPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.ViewPagerItems;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

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


    }

    private void setupTabs() {
        ViewPagerItems pagerItems = new ViewPagerItems(this);
        LinkedHashSet<String> items = new LinkedHashSet<>();
        items.add("LOGIN");
        items.add("REGISTER");

        List<String> pages = new ArrayList<>(items);

        for (String currentPage : pages) {
            ViewPagerItem viewPagerItem = ViewPagerItem.of(currentPage, R.layout._content);
            pagerItems.add(viewPagerItem);
        }
        ViewPagerItemAdapter viewPagerItemAdapter = new ViewPagerItemAdapter(pagerItems);
        b.vp.setAdapter(viewPagerItemAdapter);
        b.tabs.setViewPager(b.vp);
        b.tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                View page = viewPagerItemAdapter.getPage(position);
                EditText loginEmailTxt = page.findViewById(R.id.loginEmailTxt);
                EditText loginPasswordTxt = page.findViewById(R.id.loginPasswordTxt);
                EditText emailTxt = page.findViewById(R.id.emailTxt);
                EditText passwordTxt = page.findViewById(R.id.passwordTxt);
                EditText nameTxt = page.findViewById(R.id.nameTxt);
                EditText securityTxt = page.findViewById(R.id.securityTxt);

                if (position == 0) {
                    page.findViewById(R.id.loginLayout).setVisibility(View.VISIBLE);
                    page.findViewById(R.id.registerLayout).setVisibility(View.GONE);
                } else if (position == 1) {
                    page.findViewById(R.id.loginLayout).setVisibility(View.GONE);
                    page.findViewById(R.id.registerLayout).setVisibility(View.VISIBLE);
                }

                page.findViewById(R.id.deleteAccountBtn).setOnClickListener((View.OnClickListener) view -> {
                    if (receivedUser != null) {
                        delete(receivedUser);
                    }
                });
                page.findViewById(R.id.postBtn).setOnClickListener(view -> {
                    if (receivedUser != null) {
                        validateThenPOST(true, nameTxt, emailTxt, passwordTxt, securityTxt);
                    } else {
                        validateThenPOST(false, nameTxt, emailTxt, passwordTxt, securityTxt);
                    }

                });
                page.findViewById(R.id.loginBtn).setOnClickListener(view -> {
                    login(loginEmailTxt, loginPasswordTxt);
                });

                if (receivedUser != null) {
                    b.setUser(receivedUser);
                    ((Button) page.findViewById(R.id.postBtn)).setText("UPDATE");
                    ((Button) page.findViewById(R.id.deleteAccountBtn)).setVisibility(View.VISIBLE);
                } else {
                    b.setUser(new User());
                    ((Button) page.findViewById(R.id.postBtn)).setText("Create Account");
                    ((Button) page.findViewById(R.id.deleteAccountBtn)).setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        new Handler().postDelayed(() -> b.vp.setCurrentItem(1), 200);
        new Handler().postDelayed(() -> b.vp.setCurrentItem(0), 200);
    }

    private void login(EditText... editText) {
        if (validate(editText[0], editText[1], editText[0])) {
            getViewModel().login(valOf(editText[0]), valOf(editText[1])).observe(this, r -> {
                if (makeRequest(r, "USER SIGN IN") == SUCCEEDED) {
                    clearEditTexts(editText[0], editText[1]);
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
                //clearEditTexts(b.nameTxt, b.emailTxt, b.passwordTxt, b.securityTxt);
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

    private void validateThenPOST(boolean isForUpdate, EditText... editTexts) {
        if (validate(editTexts[0], editTexts[1], editTexts[2], editTexts[3])) {
            User u;
            if (isForUpdate) {
                u = receivedUser;
            } else {
                u = new User();
            }
            u.setName(valOf(editTexts[0]));
            u.setEmail(valOf(editTexts[1]));
            u.setPassword(valOf(editTexts[2]));
            u.setSecurityQuestion(valOf(editTexts[3]));

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
        setupTabs();


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
