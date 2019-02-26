package ainullov.kamil.com.testassignment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Приложение без дополнительной задержки отображает картинку в течении 1.5 секунды
        synchronized (this) {
            try {
                wait(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
