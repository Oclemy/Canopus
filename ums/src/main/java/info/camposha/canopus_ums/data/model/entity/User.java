package info.camposha.canopus_ums.data.model.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Let's Create our User class to represent a single User.
 * It will implement java.io.Serializable interface, a marker interface that will allow
 * our
 * class to support serialization and deserialization.
 */
public class User implements Serializable {
    /**
     * Let' now come define instance fields for this class. We decorate them with
     *
     * @SerializedName attribute. Through this we are specifying the keys in our json data.
     */
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("security_question")
    private String securityQuestion;
    @SerializedName("registeration_date")
    private String registerationDate;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("meta")
    private String meta;

    /**
     * Let's now come define our getter and setter methods.
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRegisterationDate() {
        return registerationDate;
    }

    public void setRegisterationDate(String registerationDate) {
        this.registerationDate = registerationDate;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    @Override
    public String toString() {
        return getName();
    }
}
//end

