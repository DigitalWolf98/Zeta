package ru.script_dev.zeta;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import ru.script_dev.zeta.helpers.MailHelper;
import ru.script_dev.zeta.helpers.UserHelper;

public class TrueActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_true);

        initViews();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //Toast.makeText(this, "It is impossible to go back, use the navigation buttons.", Toast.LENGTH_SHORT).show();
    }

    private void initViews() {
        Button button = findViewById(R.id.button);

        String mail = UserHelper.getMail(getApplicationContext());

        String title = String.format("Обработка заказа в %1$s", getString(R.string.app_name));
        String[] message = {
                "Поздравляем, Ваш заказ обработан!",
                "",
                "Номер заказа:",
                String.format("№-%1$s", generateRandom(10)),
                "",
                "Мы свяжимся с Вами в ближайшее время,",
                "для уточнения деталей доставки и платежа!",
        };
        MailHelper.sendMail(mail, title, String.join("\n", message));

        button.setOnClickListener(view -> {
            Intent intent = new Intent(TrueActivity.this, ProductActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });
    }

    private static String generateRandom(int length) {
        StringBuilder stringBuilder = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt("0123456789".length());
            char randomChar = "0123456789".charAt(index);
            stringBuilder.append(randomChar);
        }

        return stringBuilder.toString();
    }
}
