package ru.script_dev.zeta;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.script_dev.zeta.helpers.MailHelper;
import ru.script_dev.zeta.helpers.TextHelper;
import ru.script_dev.zeta.helpers.UserHelper;

public class SigninActivity extends AppCompatActivity {

    private EditText loginField;
    private EditText passwordField;
    private View forgotButton;
    private Button signinButton;
    private View googleButton;
    private TextView redirectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signin);

        initViews();

        String text = getString(R.string.field_placeholder, getString(R.string.field_enter), getString(R.string.label_login));
        loginField.setHint(text);
        text = getString(R.string.field_placeholder, getString(R.string.field_enter), getString(R.string.label_password));
        passwordField.setHint(text);

        forgotButton.setOnClickListener(view -> {
            Intent intent = new Intent(SigninActivity.this, ForgotActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        signinButton.setOnClickListener(view -> {
            String savedLogin = UserHelper.getName(getApplicationContext());
            String savedMail = UserHelper.getMail(getApplicationContext());
            String savedPassword = UserHelper.getPass(getApplicationContext());

            String login = loginField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            Date currentDate = new Date();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
            String formattedDate = dateFormat.format(currentDate);

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
            String formattedTime = timeFormat.format(currentDate);

            if (!savedLogin.equals(login) || !savedPassword.equals(password)) {
                Toast.makeText(SigninActivity.this, "Данный аккаунт не найден в базе!", Toast.LENGTH_SHORT).show();
            } else {
                String title = String.format("Авторизация в %1$s", getString(R.string.app_name));
                String[] message = {
                        "Мы рады видеть Вас снова!",
                        "Вы успешно авторизовались в нашем приложении.",
                        "",
                        "Последний вход:",
                        String.format("Дата входа: %1$s", formattedDate),
                        String.format("Время входа: %1$s", formattedTime),
                        "",
                        "Если это были не Вы, то настоятельно просим Вас,",
                        "ответить на данное письмо как можно скорее.",
                        "Мы свяжимся с Вами в ближайшее время!"
                };
                MailHelper.sendMail(savedMail, title, String.join("\n", message));

                Intent intent = new Intent(SigninActivity.this, ProductActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        googleButton.setOnClickListener(view -> {
            Toast.makeText(this, getString(R.string.toast_service), Toast.LENGTH_LONG).show();
        });

        SpannableStringBuilder redirectText = new SpannableStringBuilder();
        redirectText.append(TextHelper.getColoredString(getString(R.string.button_account_0), false, getColor(R.color.tertiary)));
        redirectText.append(TextHelper.getColoredString(getString(R.string.button_signup), true, getColor(R.color.primary)));
        redirectButton.setText(redirectText);

        redirectButton.setOnClickListener(view -> {
            Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //Toast.makeText(this, "It is impossible to go back, use the navigation buttons.", Toast.LENGTH_SHORT).show();
    }

    private void initViews() {
        loginField = findViewById(R.id.field1);
        passwordField = findViewById(R.id.field2);

        forgotButton = findViewById(R.id.button1);
        signinButton = findViewById(R.id.button2);
        googleButton = findViewById(R.id.button3);
        redirectButton = findViewById(R.id.button4);
    }
}
