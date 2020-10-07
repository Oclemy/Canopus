package info.camposha.canopus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import info.camposha.canopus_ums.data.model.entity.User;
import info.camposha.canopus_ums.view.ui.activities.AccountActivity;

public class MainActivity extends AppCompatActivity {

    private void handleEvents() {
        findViewById(R.id.openBtn).setOnClickListener(view -> {
            startActivityForResult(new Intent(this, AccountActivity.class),1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                if(data != null){
                    //Use the below code to obtain results:
                    Object result=data.getSerializableExtra("LOGIN_RESULT");
//                    Object result=  data.getSerializableExtra("REGISTRATION_RESULT");
//                    Object result=  data.getSerializableExtra("UPDATE_RESULT");
                    //String deleteResult = data.getStringExtra("DELETE_RESULT");

                    if(result != null){
                        User user= (User) result;
                        ((TextView)findViewById(R.id.emailTV)).setText(user.getEmail());
                        ((TextView)findViewById(R.id.passwordTV)).setText(user.getPassword());
                    }else{
                        Toast.makeText(this, "Operation was Unsuccessful", Toast.LENGTH_SHORT).show();
                    }


                }else{
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * Let's override our onCreate() method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.handleEvents();
    }
}
//end
