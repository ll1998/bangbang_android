package com.autowheel.bangbang.ui.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.autowheel.bangbang.base.BaseViewBindingActivity;
import com.autowheel.bangbang.databinding.ActivityLoginBinding;
import com.autowheel.bangbang.model.DataManager;
import com.autowheel.bangbang.model.network.RetrofitHelper;
import com.autowheel.bangbang.model.network.bean.GeneralResponseBean;
import com.autowheel.bangbang.model.network.bean.LoginBean;
import com.autowheel.bangbang.ui.MainActivity;
import com.autowheel.bangbang.utils.DeviceUtilKt;
import com.autowheel.bangbang.utils.ToastyUtilKt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Xily on 2020/3/26.
 */
public class LoginActivity extends BaseViewBindingActivity<ActivityLoginBinding> {

    @NotNull
    @Override
    public ActivityLoginBinding initViewBinding() {
        return ActivityLoginBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initViews(@Nullable Bundle savedInstanceState) {
        DeviceUtilKt.setStatusBarUpper(this);
        DeviceUtilKt.setDarkStatusIcon(this, false);
        getViewBinding().btnLogin.setOnClickListener(v -> {
            login();
        });
        getViewBinding().tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, VerifyActivity.class));
        });
        getViewBinding().tvForgetPassword.setOnClickListener(v -> {

        });
    }

    private void login() {
        String username = getViewBinding().etUsername.getText().toString();
        String password = getViewBinding().etPassword.getText().toString();
        if (username.isEmpty() || password.isEmpty()) {
            ToastyUtilKt.toastError("用户名或密码不能为空!");
        } else {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("登陆中...");
            progressDialog.show();
            RetrofitHelper.getApiService().login(username, password).enqueue(new Callback<GeneralResponseBean<LoginBean>>() {
                @Override
                public void onResponse(Call<GeneralResponseBean<LoginBean>> call, Response<GeneralResponseBean<LoginBean>> response) {
                    progressDialog.dismiss();
                    GeneralResponseBean<LoginBean> loginResponseBean = response.body();
                    if (loginResponseBean.getCode() == 0) {
                        String token = loginResponseBean.getData().getToken();
                        DataManager.INSTANCE.setToken(token);
                        ToastyUtilKt.toastSuccess("登陆成功!");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        ToastyUtilKt.toastError(loginResponseBean.getMsg());
                    }
                }


                @Override
                public void onFailure(Call<GeneralResponseBean<LoginBean>> call, Throwable t) {
                    progressDialog.dismiss();
                    ToastyUtilKt.toastError("网络请求出错!");
                }
            });
        }
    }
}
