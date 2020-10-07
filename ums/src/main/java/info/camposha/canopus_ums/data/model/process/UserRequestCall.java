package info.camposha.canopus_ums.data.model.process;

import java.util.List;

import info.camposha.canopus_ums.data.model.entity.User;
import info.camposha.canopus_ums.data.model.entity.UserResponseModel;

public class UserRequestCall {
    private int status;
    private String message;
    private List<User> users;
    private User user;
    private UserResponseModel userResponseModel;

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
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

    public UserResponseModel getUserResponseModel() {
        return userResponseModel;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUserResponseModel(UserResponseModel userResponseModel) {
        this.userResponseModel = userResponseModel;
    }

}
