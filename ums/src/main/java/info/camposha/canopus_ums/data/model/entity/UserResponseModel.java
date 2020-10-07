package info.camposha.canopus_ums.data.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Our json response will be mapped to this class.
 */
public class UserResponseModel {
    /**
     * Our UserResponseModel attributes
     */
    @SerializedName("code")
    private String code="-1";
    @SerializedName("message")
    private String message="UNKNOWN MESSAGE";
    @SerializedName("users")
    private List<User> users=new ArrayList<>();
    @SerializedName("user")
    private User user=null;


    /**
     * Generate Getter and Setters
     */
    public List<User> getResult() {
        return users;
    }

    public void setResult(List<User> users) {
        this.users = users;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
//end
