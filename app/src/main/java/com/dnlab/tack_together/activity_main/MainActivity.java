package com.dnlab.tack_together.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.dnlab.tack_together.R;
import com.dnlab.tack_together.retrofit.RetrofitBuilder;
import com.dnlab.tack_together.service.StompClientSingleton;

import retrofit2.Retrofit;
import ua.naiksoftware.stomp.StompClient;

public class MainActivity extends AppCompatActivity {
    public static String TAG = "[" + MainActivity.class.getSimpleName() + "] ";
    private Retrofit retrofit;
    private static MainActivity instance;

    private Button signupButton;
    private Button loginButton;

    private ActivityResultLauncher<Intent> loginLauncher;

    private String accessToken = null;
    private String refreshToken = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        instance = this;
//        getHashKey();

        retrofit = RetrofitBuilder.getInstance().getRetrofit();


        //임시 stomp 테스트
        StompClient stompClient = StompClientSingleton.getInstance(getApplicationContext()).getStompClient();
//        stompClient.send("send할 주소", jsonObject.toJSONString()).subscribe();
//
//
//
//        stompClient.topic("subscribe할 주소").subscribe(topicMessage -> {
//                    JSONParser parser=new JSONParser();
//                    Object obj=parser.parse(topicMessage.getPayload());

//
//        signupButton = findViewById(R.id.signupButton);
//        signupButton.setOnClickListener(v -> {
//            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
//            startActivityForResult(intent, 101);
//        });
//
//        loginButton = findViewById(R.id.to_login_button);
//
//        loginLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
//                (result -> {
//                    if (result.getResultCode() == RESULT_OK) {
//                        assert result.getData() != null;
//                        Bundle bundle = result.getData().getExtras();
//                        Intent intent = bundle.getParcelable("INTENT");
//                        accessToken = intent.getStringExtra("ACCESS_TOKEN");
//                        refreshToken = intent.getStringExtra("REFRESH_TOKEN");
//                    }
//                }));
//
//        loginButton.setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//            loginLauncher.launch(intent);
//        });

    }

//    public Retrofit getRetrofit() {
//        return retrofit;
//    }
//
//    public static MainActivity getInstance() {
//        return instance;
//    }
//
//    private void getHashKey() {
//        PackageInfo packageInfo = null;
//        try {
//            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        if (packageInfo == null) {
//            Log.e("KeyHash", "KeyHash:null");
//        }
//
//        for (Signature signature : packageInfo.signatures) {
//            try {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.e("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            } catch (NoSuchAlgorithmException e) {
//                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
//            }
//        }
//    }
}