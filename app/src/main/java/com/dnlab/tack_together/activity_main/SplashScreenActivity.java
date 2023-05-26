package com.dnlab.tack_together.activity_main;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.dnlab.tack_together.R;
import com.dnlab.tack_together.api.dto.auth.LoginResponseDTO;
import com.dnlab.tack_together.api.dto.auth.RefreshTokenRequestDTO;
import com.dnlab.tack_together.api.dto.auth.TestTokenResponseDTO;
import com.dnlab.tack_together.common.jwt.TokenManager;
import com.dnlab.tack_together.common.jwt.TokenManagerImpl;
import com.dnlab.tack_together.retrofit.AuthorizationAPI;
import com.dnlab.tack_together.retrofit.RetrofitBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN_TIMEOUT = 3000; // 3 seconds
    private final String TAG = "SplashScreenActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            RetrofitBuilder retrofitBuilder = RetrofitBuilder.getInstance(getApplicationContext());
            AuthorizationAPI authorizationAPI = retrofitBuilder.getRetrofit().create(AuthorizationAPI.class);
            Call<TestTokenResponseDTO> call = authorizationAPI.testAuthentication();
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<TestTokenResponseDTO> call, Response<TestTokenResponseDTO> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (response.body().isAuthorized()) {
                            startMainActivity();
                        } else {
                            tryAuthenticateWithRefreshToken();
                        }
                    } else {
                        tryAuthenticateWithRefreshToken();
                    }
                }

                @Override
                public void onFailure(Call<TestTokenResponseDTO> call, Throwable t) {
                    tryAuthenticateWithRefreshToken();
                }
            });
        }, SPLASH_SCREEN_TIMEOUT);
    }

    private void tryAuthenticateWithRefreshToken() {
        TokenManager tokenManager = new TokenManagerImpl(getApplicationContext());
        String refreshToken = tokenManager.getRefreshToken();
        if (refreshToken == null) {
            startAuthenticationActivity();
        }

        AuthorizationAPI authorizationAPI = RetrofitBuilder
                .getInstance(getApplicationContext())
                .getRetrofit()
                .create(AuthorizationAPI.class);

        RefreshTokenRequestDTO refreshTokenRequestDTO = new RefreshTokenRequestDTO(refreshToken);
        Call<LoginResponseDTO> call = authorizationAPI.refreshToken(refreshTokenRequestDTO);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<LoginResponseDTO> call, Response<LoginResponseDTO> response) {
                if (response.isSuccessful()) {
                    LoginResponseDTO loginResponseDTO = response.body();
                    assert loginResponseDTO != null;
                    tokenManager.storeAccessToken(loginResponseDTO.getAccessToken());
                    tokenManager.storeRefreshToken(loginResponseDTO.getRefreshToken());
                    startMainActivity();
                } else {
                    startAuthenticationActivity();
                }
            }

            @Override
            public void onFailure(Call<LoginResponseDTO> call, Throwable t) {
                startAuthenticationActivity();
            }
        });
    }

    private void startMainActivity() {
        Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void startAuthenticationActivity() {
        Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SplashScreenActivity.this, AuthenticationActivity.class);
        startActivity(intent);
        finish();
    }
}