package info.camposha.canopus_ums.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import info.camposha.canopus_ums.data.model.entity.User;
import info.camposha.canopus_ums.data.model.process.UserRequestCall;
import info.camposha.canopus_ums.data.repository.UsersRepository;

public class UsersViewModel extends AndroidViewModel {
    private final UsersRepository mRepository;

    public UsersViewModel(@NonNull Application application) {
        super(application);
        mRepository = new UsersRepository();
    }
    public MutableLiveData<UserRequestCall> createAccount(User user){
        return mRepository.register(user);
    }
    public MutableLiveData<UserRequestCall> update(User user){
        return mRepository.update(user);
    }
    public MutableLiveData<UserRequestCall> delete(User user){
        return mRepository.delete(user.getId());
    }
    public MutableLiveData<UserRequestCall> login(String email, String password){
        return mRepository.login(email,password);
    }
    public MutableLiveData<UserRequestCall> select() {
        return mRepository.select();
    }

    public MutableLiveData<UserRequestCall> search(String query, String start, String limit) {
        return mRepository.search(query,start,limit);
    }

}
