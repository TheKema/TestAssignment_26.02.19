package ainullov.kamil.com.testassignment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.support.constraint.Constraints.TAG;

public class MainActivity extends AppCompatActivity {

    private EditText etLogin;
    private EditText etPass;
    private Button btnSignIn;
    private String cookie;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etLogin = (EditText) findViewById(R.id.etLogin);
        etPass = (EditText) findViewById(R.id.etPass);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etLogin.length() != 0 && etPass.length() != 0) {

                    CookieManager cookieManager = new CookieManager();
                    cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

                    OkHttpClient client = new OkHttpClient.Builder()
                            .cookieJar(new CookieJar() {
                                final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

                                @Override
                                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                                    cookieStore.put(url, cookies);
                                    //Получаю JSESSIONID
                                    cookie = cookies.get(0).value();
                                }

                                @Override
                                public List<Cookie> loadForRequest(HttpUrl url) {
                                    List<Cookie> cookies = cookieStore.get(url);
                                    return cookies != null ? cookies : new ArrayList<Cookie>();
                                }
                            })
                            .build();


                    MediaType mediaType = MediaType.parse("application/json");
                    RequestBody body = RequestBody.create(mediaType, "{\"loginType\":\"email\",\"login\":\"admin@mail.ru\",\"password\":\"123456\"}");
                    Request request = new Request.Builder()
                            .url("http://185.27.193.111:8082/ckpt-core-web-1.0/platform/api/user/login")
                            .post(body)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("cache-control", "no-cache")
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e(TAG, "onFailure");
                            Toast.makeText(getApplicationContext(), "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Log.d(TAG, response.toString());

                            Intent intent = new Intent(getApplicationContext(), ProfileInformation.class);
                            intent.putExtra("response", response.toString());
                            intent.putExtra("cookie", cookie);
                            startActivity(intent);
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "Заполните поля!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
