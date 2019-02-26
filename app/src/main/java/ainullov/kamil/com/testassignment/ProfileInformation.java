package ainullov.kamil.com.testassignment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.support.constraint.Constraints.TAG;

public class ProfileInformation extends AppCompatActivity {
    private TextView tvName;
    private TextView tvSurname;
    private TextView tvEmail;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_information);

        Intent intent = getIntent();
        String cookie = intent.getStringExtra("cookie");


        tvName = (TextView) findViewById(R.id.tvName);
        tvSurname = (TextView) findViewById(R.id.tvSurname);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "{\"loginType\":\"email\",\"login\":\"admin@mail.ru\",\"password\":\"123456\"}");
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("http://185.27.193.111:8082/ckpt-core-web-1.0/platform/api/user/logout")
                        .post(body)
                        .addHeader("cache-control", "no-cache")
                        .addHeader("Postman-Token", "07b68f91-573e-4674-a6d5-758e9f4e415a")
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "onFailure");
                        Toast.makeText(getApplicationContext(), "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.e(TAG, response.toString());
                        finish();
                    }
                });
            }
        });


        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"loginType\":\"email\",\"login\":\"admin@mail.ru\",\"password\":\"123456\"}");

        final Request request = new Request.Builder()
                .url("http://185.27.193.111:8082/ckpt-core-web-1.0/platform/api/user/logged")
                .post(body)


                .header("Cookie", "JSESSIONID=" + cookie)
                .addHeader("Content-Type", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("Postman-Token", "806d8a06-a779-470f-a327-4f356745ea49")
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
                final JSONObject jsonObject;
                String jsonData = response.body().string();
                try {
                    jsonObject = new JSONObject(jsonData);
                    final JSONObject jsonObject1 = jsonObject.getJSONObject("fields");
                    Log.e(TAG, jsonData);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                tvEmail.setText("email: "+jsonObject.getString("email"));
                                tvName.setText("name: "+jsonObject1.getString("name"));
                                tvSurname.setText("surname: "+jsonObject1.getString("surname"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
