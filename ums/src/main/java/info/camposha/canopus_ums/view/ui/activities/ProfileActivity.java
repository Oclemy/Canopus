package info.camposha.canopus_ums.view.ui.activities;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.google.android.material.tabs.TabLayout;

import info.camposha.canopus_ums.R;
import info.camposha.canopus_ums.common.UserUtils;
import info.camposha.canopus_ums.data.model.entity.User;
import info.camposha.canopus_ums.databinding.ActivityProfileBinding;
import info.camposha.canopus_ums.view.ui.base.AccountBaseActivity;

import static info.camposha.canopus_ums.common.UserUtils.sendToActivity;

public class ProfileActivity extends AccountBaseActivity {
    private User receivedUser;
    private ActivityProfileBinding b;


    private void handleEvents() {
        b.tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    b.basicLayout.setVisibility(View.VISIBLE);
                    b.accountLayout.setVisibility(View.GONE);
                } else if (tab.getPosition() == 1) {
                    b.basicLayout.setVisibility(View.GONE);
                    b.accountLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        b.editAccount.setOnClickListener(view -> {
            if(receivedUser != null){
                sendToActivity(ProfileActivity.this,receivedUser,AccountActivity.class);
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        receivedUser = UserUtils.receive(getIntent(), this);

        if (receivedUser != null) {
            b.setUser(receivedUser);
        } else {
            b.setUser(new User());
        }
    }

    /**
     * Let's override our onCreate() method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        this.handleEvents();
    }
}
//end
