package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.req.UserModelReq;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.NetworkStatusChecker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends BaseActivity {
    static final String TAG = ConstantManager.TAG_PREFIX + "AuthActivity";
    @BindView(R.id.login_button)
    Button mSignIn;

    @BindView(R.id.remember_txt)
    TextView mRememberPassword;

    @BindView(R.id.login_email_et)
    EditText mLogin;

    @BindView(R.id.login_password_et)
    EditText mPassword;

    @BindView(R.id.main_coordinator_container)
    CoordinatorLayout mCoordinatorLayout;

    private DataManager mDataManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mDataManager = DataManager.getInstance();

        ButterKnife.bind(this);
        mLogin.setText("shmakova-nastya@yandex.ru");
        mPassword.setText("iliich");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @OnClick(R.id.remember_txt)
    public void onRememberPasswordClick(View view) {
        rememberPassword();
    }

    @OnClick(R.id.login_button)
    public void onSignInButtonClick(View view) {
        signIn();
    }

    /**
     * Показывает снэкбар с текстом
     * @param message
     */
    private void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Вспоминает пароль
     */
    private void rememberPassword() {
        Intent rememberIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://devintensive.softdesign-apps.ru/forgotpass"));
        startActivity(rememberIntent);
    }

    /**
     * Логин
     */
    private void loginSuccess(UserModelRes userModel) {
        showSnackBar(userModel.getData().getToken());
        mDataManager.getPreferenceManager().saveAuthToken(userModel.getData().getToken());
        mDataManager.getPreferenceManager().saveUserId(userModel.getData().getUser().getId());
        saveUserValues(userModel);

        Intent loginIntent = new Intent(this, UserListActivity.class);
        startActivity(loginIntent);
    }

    /**
     * Авторизует
     */
    private void signIn() {
        if (NetworkStatusChecker.isNetworkAvailable(this)) {
            Call<UserModelRes> call = mDataManager.loginUser(
                    new UserModelReq(mLogin.getText().toString(), mPassword.getText().toString()));
            call.enqueue(new Callback<UserModelRes>() {
                @Override
                public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                    if (response.code() == 200) {
                        loginSuccess(response.body());
                    } else if (response.code() == 403) {
                        showSnackBar("Неверный логин или пароль");
                    } else {
                        showSnackBar("Что-то пошло не так");
                    }
                }

                @Override
                public void onFailure(Call<UserModelRes> call, Throwable throwable) {

                }
            });
        } else {
            showSnackBar("Сеть на данный момент недоступна, попробуйте позже");
        }
    }

    /**
     * Сохраняет значения рейтинга/строк кода/проектов
     * @param userModel
     */
    private void saveUserValues(UserModelRes userModel) {
        int[] userValues = {
                userModel.getData().getUser().getProfileValues().getRating(),
                userModel.getData().getUser().getProfileValues().getLinesCode(),
                userModel.getData().getUser().getProfileValues().getProjects(),
        };

        mDataManager.getPreferenceManager().saveUserProfileValues(userValues);

        List<String> userData = new ArrayList<>();
        userData.add(userModel.getData().getUser().getContacts().getPhone());
        userData.add(userModel.getData().getUser().getContacts().getEmail());
        userData.add(userModel.getData().getUser().getContacts().getVk());
        userData.add(userModel.getData().getUser().getRepositories().getRepo().get(0).getGit());
        userData.add(userModel.getData().getUser().getPublicInfo().getBio());
        mDataManager.getPreferenceManager().saveUserProfileData(userData);

        String userName = userModel.getData().getUser().getSecondName()
                + " " + userModel.getData().getUser().getFirstName();
        mDataManager.getPreferenceManager().saveUserName(userName);

        String photoUrl =  userModel.getData().getUser().getPublicInfo().getPhoto();
        mDataManager.getPreferenceManager().saveUserPhoto(Uri.parse(photoUrl));

        String avatarUrl =  userModel.getData().getUser().getPublicInfo().getAvatar();
        mDataManager.getPreferenceManager().saveUserAvatar(Uri.parse(avatarUrl));
    }
}
