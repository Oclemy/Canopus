package info.camposha.canopus_ums.view.ui.base;

import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.lifecycle.ViewModelProvider;

import info.camposha.canopus_ums.R;
import info.camposha.canopus_ums.common.UserConstants;
import info.camposha.canopus_ums.common.UserUtils;
import info.camposha.canopus_ums.data.model.process.UserRequestCall;
import info.camposha.canopus_ums.viewmodel.UsersViewModel;

import static info.camposha.canopus_ums.common.UserUtils.*;

public abstract class AccountBaseActivity extends AppCompatActivity {
    protected AccountBaseActivity a=this;

    protected UsersViewModel getViewModel() {
        return new ViewModelProvider(this).get(UsersViewModel.class);
    }
    protected void show(String message){
        UserUtils.show(this,message);
    }
    protected void openPage(Class clazz){
        openActivity(this,clazz);
    }

    protected boolean validate(EditText... editTexts){
        EditText ed1 = editTexts[0];
        EditText ed2 = editTexts[1];
        EditText ed3 = editTexts[2];

        if(ed1.getText() == null || ed1.getText().toString().isEmpty()){
            ed1.setError("This Field is Required Please!");
            return false;
        }
        if(ed2.getText() == null || ed2.getText().toString().isEmpty()){
            ed2.setError("This Field  is Required Please!");
            return false;
        }
        if(ed3.getText() == null || ed3.getText().toString().isEmpty()){
            ed3.setError("This Field is Required Please!");
            return false;
        }
        return true;

    }
    protected void clearEditTexts(EditText... editTexts){
        for (EditText editText:editTexts) {
            editText.setText("");
        }
    }

    /**
     * This is our progress and messages card to be re-used across several activities.
     * @param title - Title of card
     * @param msg - Message
     * @param isShowing
     * @param STATE
     */
    public void createStateCard(String title, String msg, boolean isShowing,
                                int STATE) {
        //state widgets
        LinearLayout sectionCard = findViewById(R.id.sectionLayout);
        ProgressBar pb = findViewById(R.id.pb);
        ImageView stateImg = findViewById(R.id.stateImg);
        TextView titleTV = findViewById(R.id.titleTV);
        TextView msgTV = findViewById(R.id.messageTV);
        AppCompatImageButton closeBtn = findViewById(R.id.closeBtn);

        Handler handler = new Handler();
        Runnable delayedHiding = () -> sectionCard.setVisibility(View.GONE);

        if (isShowing) {
            sectionCard.setVisibility(View.VISIBLE);
            titleTV.setText(title);
            msgTV.setText(msg);
            if (STATE == UserConstants.FAILED) {
                stateImg.setImageResource(R.drawable.error_32px);
                sectionCard.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                pb.setVisibility(View.GONE);
                stateImg.setVisibility(View.VISIBLE);
                handler.postDelayed(delayedHiding, 10000);
            } else if (STATE == UserConstants.IN_PROGRESS) {
                stateImg.setImageResource(R.drawable.loading_32px);
                sectionCard.setBackgroundColor(getResources().getColor(R.color.color_7));
                stateImg.setVisibility(View.GONE);
                pb.setVisibility(View.VISIBLE);
            } else if (STATE == UserConstants.SUCCEEDED) {
                sectionCard.setBackgroundColor(getResources().getColor(R.color.teal));
                stateImg.setImageResource(R.drawable.ok_checked_32px);
                pb.setVisibility(View.GONE);
                stateImg.setVisibility(View.VISIBLE);
                handler.postDelayed(delayedHiding, 10000);

            }

        } else {
            sectionCard.setVisibility(View.GONE);
        }

        closeBtn.setOnClickListener(v -> sectionCard.setVisibility(View.GONE));

    }

    public int makeRequest(UserRequestCall r, String OPERATION) {
        if (r == null) {
            createStateCard(OPERATION + " FAILED", "Null User RequestCall Received", true, UserConstants.FAILED);
        } else {
            if (r.getStatus() == UserConstants.IN_PROGRESS) {
                createStateCard(OPERATION + " IN PROGRESS", r.getMessage(), true, UserConstants.IN_PROGRESS);
            } else if (r.getStatus() == UserConstants.FAILED) {
                createStateCard("WHOOPS!", r.getMessage(), true, UserConstants.FAILED);
            } else if (r.getStatus() == UserConstants.SUCCEEDED) {
                createStateCard("CONGRATS!", r.getMessage(), true, UserConstants.SUCCEEDED);
            }
            return r.getStatus();
        }
        return -999;
    }


}
//end

