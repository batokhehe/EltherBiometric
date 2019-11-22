package com.eltherbiometric.data;

import com.eltherbiometric.data.model.LoggedInUser;
import com.eltherbiometric.utils.Config;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {
        if(username.equals(Config.UserName) && password.equals(Config.Password)){
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            Config.Name);
            return new Result.Success<>(fakeUser);
        }
        return new Result.Error(new IOException("Error logging in"));
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
