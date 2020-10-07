package info.camposha.canopus_ums.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import info.camposha.canopus_ums.common.UserConstants;
import info.camposha.canopus_ums.common.UserUtils;
import info.camposha.canopus_ums.data.api.UserRestApi;
import info.camposha.canopus_ums.data.model.entity.UserResponseModel;
import info.camposha.canopus_ums.data.model.entity.User;
import info.camposha.canopus_ums.data.model.process.UserRequestCall;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersRepository {
    private MutableLiveData<UserRequestCall> mLiveData;

    public UsersRepository() {
        this.mLiveData = new MutableLiveData<>();
    }

    private UserRequestCall handleResponse(Response<UserResponseModel> response, UserRequestCall r) {
        if (response == null) {
            r.setStatus(UserConstants.FAILED);
            r.setMessage("Response is Null");
        } else if (response.body() == null) {
            r.setStatus(UserConstants.FAILED);
            r.setMessage("Response Body is Null");
        } else {
            UserResponseModel myUserResponseModel = response.body();

            if (myUserResponseModel.getCode() == null) {
                r.setStatus(UserConstants.FAILED);
                r.setMessage("NULL RESPONSE CODE");
            } else if (myUserResponseModel.getMessage() == null) {
                r.setStatus(UserConstants.FAILED);
                r.setMessage("NULL RESPONSE MESSAGE");
            } else {
                String myResponseCode = myUserResponseModel.getCode();
                String myResponseMessage = myUserResponseModel.getMessage();

                if (myResponseCode.equalsIgnoreCase("1")) {
                    r.setStatus(UserConstants.SUCCEEDED);
                    if (myUserResponseModel.getResult() != null) {
                        r.setUsers(myUserResponseModel.getResult());
                    }
                    if(myUserResponseModel.getUser() != null){
                        r.setUser(myUserResponseModel.getUser());
                    }
                } else {
                    r.setStatus(UserConstants.FAILED);
                }
                r.setMessage(myResponseMessage);


            }
            r.setUserResponseModel(myUserResponseModel);

        }
        return r;
    }

    private UserRequestCall handleFailure(String message, UserRequestCall userRequestCall) {
        Log.d("RETROFIT", "ERROR: " + message);
        userRequestCall.setStatus(UserConstants.FAILED);
        userRequestCall.setMessage(message);
        return userRequestCall;
    }

    public MutableLiveData<UserRequestCall> select() {
        UserRestApi api = UserUtils.getClient().create(UserRestApi.class);
        Call<UserResponseModel> retrievedData;
        retrievedData = api.getAll();

        UserRequestCall r = new UserRequestCall();
        r.setStatus(UserConstants.IN_PROGRESS);
        r.setMessage("Downloading Users Please Wait..");

        mLiveData.setValue(r);

        retrievedData.enqueue(new Callback<UserResponseModel>() {
            @Override
            public void onResponse(Call<UserResponseModel> call, Response<UserResponseModel> response) {
                mLiveData.postValue(handleResponse(response, r));
            }

            @Override
            public void onFailure(Call<UserResponseModel> call, Throwable t) {
                handleFailure(t.getMessage(), r);
            }
        });
        return mLiveData;
    }

    public MutableLiveData<UserRequestCall> register(User u) {
        MutableLiveData<UserRequestCall> mLiveData = new MutableLiveData<>();

        UserRestApi api = UserUtils.getClient().create(UserRestApi.class);
        Call<UserResponseModel> call = api.createAccount("CREATE_ACCOUNT",
                u.getName(), u.getEmail(), u.getPassword());

        UserRequestCall r = new UserRequestCall();
        r.setStatus(UserConstants.IN_PROGRESS);
        r.setMessage("Creating Your Account...Please wait..");
        mLiveData.setValue(r);
        call.enqueue(new Callback<UserResponseModel>() {
            @Override
            public void onResponse(Call<UserResponseModel> call, Response<UserResponseModel> response) {
                mLiveData.postValue(handleResponse(response, r));
            }

            @Override
            public void onFailure(Call<UserResponseModel> call, Throwable t) {
                mLiveData.postValue(handleFailure(t.getMessage(), r));
            }
        });
        return mLiveData;
    }

    public MutableLiveData<UserRequestCall> update(User q) {
        MutableLiveData<UserRequestCall> mLiveData = new MutableLiveData<>();

        UserRestApi api = UserUtils.getClient().create(UserRestApi.class);
        Call<UserResponseModel> call = api.update("UPDATE_ACCOUNT",
                q.getId(), q.getName(), q.getPassword());


        UserRequestCall r = new UserRequestCall();
        r.setStatus(UserConstants.IN_PROGRESS);
        r.setMessage("Updating User...Please wait..");
        mLiveData.setValue(r);
        call.enqueue(new Callback<UserResponseModel>() {
            @Override
            public void onResponse(Call<UserResponseModel> call, Response<UserResponseModel> response) {
                mLiveData.postValue(handleResponse(response, r));
            }

            @Override
            public void onFailure(Call<UserResponseModel> call, Throwable t) {
                mLiveData.postValue(handleFailure(t.getMessage(), r));
            }
        });
        return mLiveData;
    }

    public MutableLiveData<UserRequestCall> delete(String id) {
        MutableLiveData<UserRequestCall> mLiveData = new MutableLiveData<>();

        UserRestApi api = UserUtils.getClient().create(UserRestApi.class);
        Call<UserResponseModel> call = api.deleteAccount("DELETE_ACCOUNT", id);

        UserRequestCall r = new UserRequestCall();
        r.setStatus(UserConstants.IN_PROGRESS);
        r.setMessage("Deleting Account.. Please wait..");
        mLiveData.setValue(r);

        call.enqueue(new Callback<UserResponseModel>() {
            @Override
            public void onResponse(Call<UserResponseModel> call, Response<UserResponseModel> response) {
                mLiveData.postValue(handleResponse(response, r));
            }

            @Override
            public void onFailure(Call<UserResponseModel> call, Throwable t) {
                mLiveData.postValue(handleFailure(t.getMessage(), r));
            }
        });
        return mLiveData;
    }
    public MutableLiveData<UserRequestCall> login(String email, String password) {
        MutableLiveData<UserRequestCall> mLiveData = new MutableLiveData<>();

        UserRestApi api = UserUtils.getClient().create(UserRestApi.class);
        Call<UserResponseModel> call = api.login("LOGIN", email,password);

        UserRequestCall r = new UserRequestCall();
        r.setStatus(UserConstants.IN_PROGRESS);
        r.setMessage("Signing You In.. Please wait..");
        mLiveData.setValue(r);

        call.enqueue(new Callback<UserResponseModel>() {
            @Override
            public void onResponse(Call<UserResponseModel> call, Response<UserResponseModel> response) {
                mLiveData.postValue(handleResponse(response, r));
            }

            @Override
            public void onFailure(Call<UserResponseModel> call, Throwable t) {
                mLiveData.postValue(handleFailure(t.getMessage(), r));
            }
        });
        return mLiveData;
    }

    public MutableLiveData<UserRequestCall> search(String query, String start, String limit) {
        MutableLiveData<UserRequestCall> mLiveData = new MutableLiveData<>();

        UserRestApi api = UserUtils.getClient().create(UserRestApi.class);
        Call<UserResponseModel> call = api.search("SEARCH_USERS", query, start, limit);

        UserRequestCall r = new UserRequestCall();
        r.setStatus(UserConstants.IN_PROGRESS);
        r.setMessage("Fetching Users.. Please wait..");
        mLiveData.setValue(r);

        call.enqueue(new Callback<UserResponseModel>() {
            @Override
            public void onResponse(Call<UserResponseModel> call, Response<UserResponseModel> response) {
                mLiveData.postValue(handleResponse(response, r));
            }

            @Override
            public void onFailure(Call<UserResponseModel> call, Throwable t) {
                mLiveData.postValue(handleFailure(t.getMessage(), r));
            }
        });
        return mLiveData;
    }
}

//end