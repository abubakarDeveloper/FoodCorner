package ab_developer.com.foodcorner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseLoginActivity extends AppCompatActivity {

    Button btnPhone;
    Button btnEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login);

        btnPhone = findViewById(R.id.btn_phone);
        btnEmail = findViewById(R.id.btn_email);
        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseLoginActivity.this, PhoneLoginActivity.class);
                startActivity(intent);
            }
        });
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseLoginActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
