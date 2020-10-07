package info.camposha.canopus_ums.common;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import info.camposha.canopus_ums.data.model.entity.User;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserUtils {

    private static Retrofit retrofit;
    public static String SEARCH_STRING = "";
    public static ArrayList<User> MEM_CACHE=new ArrayList<>();

    /**
     * This method will return us our Retrofit instance which we can use to initiate
      HTTP calls.
     */
    public static Retrofit getClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(UserConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return  retrofit;
    }

    public static void show(Context c,String message){
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
    }
    public static boolean validate(EditText... editTexts){
        EditText nameTxt = editTexts[0];
        EditText bioTxt = editTexts[1];
        EditText countryTxt = editTexts[2];

        if(nameTxt.getText() == null || nameTxt.getText().toString().isEmpty()){
            nameTxt.setError("Name is Required Please!");
            return false;
        }
        if(bioTxt.getText() == null || bioTxt.getText().toString().isEmpty()){
            bioTxt.setError("Bio is Required Please!");
            return false;
        }
        if(countryTxt.getText() == null || countryTxt.getText().toString().isEmpty()){
            countryTxt.setError("Country is Required Please!");
            return false;
        }
        return true;

    }

    public static void clearEditTexts(EditText... editTexts){
        for (EditText editText:editTexts) {
            editText.setText("");
        }
    }
    public static String valOf(EditText editText){
        if (editText==null){
            return "";
        }
        return editText.getText().toString();
    }

    public static void openActivity(Context c,Class clazz){
        Intent intent = new Intent(c, clazz);
        c.startActivity(intent);
    }
    /**
     * This method will allow us send a serialized user objec  to a specified
     *  activity
     */
    public static void sendToActivity(Context c, User user,
                                      Class clazz){
        Intent i=new Intent(c,clazz);
        i.putExtra("_KEY", user);
        c.startActivity(i);
    }

    /**
     * This method will allow us receive a serialized scientist, deserialize it and return it,.
     */
    public  static User receive(Intent intent, Context c){
        try {
            return (User) intent.getSerializableExtra("_KEY");
        }catch (Exception e){
            e.printStackTrace();
            show(c,"ERROR: "+e.getMessage());
        }
        return null;
    }

    /**
     * This method will allow us convert a string into a java.util.Date object and
     *  return it.
     */
    public static Date giveMeDate(String stringDate){
        try {
            SimpleDateFormat sdf=new SimpleDateFormat(UserConstants.DATE_FORMAT);
            return sdf.parse(stringDate);
        }catch (ParseException e){
            e.printStackTrace();
            return null;
        }
    }

    public static Boolean itemExists(String key, List<User> users){
        for (User user : users){
            if (user.getId().equals(key)){
                return true;
            }
        }
        return false;
    }


}
//end

