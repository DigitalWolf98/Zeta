package ru.script_dev.zeta;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.script_dev.zeta.helpers.MailHelper;
import ru.script_dev.zeta.helpers.RecaptchaHelper;
import ru.script_dev.zeta.helpers.TextHelper;

public class ForgotActivity extends AppCompatActivity {

    private RecaptchaHelper recaptchaHelper;

    private EditText mailFiled;
    private CheckBox checkField;
    private View checkButton;
    private Button recoverButton;
    private View googleButton;
    private TextView redirectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot);

        initViews();

        String text = getString(R.string.field_placeholder, getString(R.string.field_enter), getString(R.string.label_mail).replace("а", "у"));
        mailFiled.setHint(text);

        checkButton.setOnClickListener(view -> {
            checkField.setChecked(false);

            RecaptchaHelper recaptchaHelper = new RecaptchaHelper(getApplicationContext());

            CompletableFuture.supplyAsync(() -> {
                try {
                    return recaptchaHelper.verifyRecaptcha();
                } catch (ExecutionException | InterruptedException exception) {
                    return false;
                }
            }).thenAccept(result -> {
                if (result) checkField.setChecked(true);
                else checkField.setChecked(false);
            });
        });

        recoverButton.setOnClickListener(view -> {
            String mail = mailFiled.getText().toString().trim();

            Toast toast = Toast.makeText(ForgotActivity.this, "", Toast.LENGTH_SHORT);

            Date currentDate = new Date();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
            String formattedDate = dateFormat.format(currentDate);

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
            String formattedTime = timeFormat.format(currentDate);

            if (!isEmail(mail)) {
                toast.setText("Вы указали неверную почту!");
                toast.show();
            } else if (!checkField.isChecked()) {
                toast.setText("Вы не прошли проверку!");
                toast.show();
            } else {
                String title = String.format("Восстановление в %1$s", getString(R.string.app_name));
                String[] message = {
                    "Мы сожалеем о случившемся!",
                    "Для восстановления данных,",
                    "пришлите нам как можно больше информации.",
                    "",
                    "Пример:",
                    String.format("Дата входа: %1$s", formattedDate),
                    String.format("Время входа: %1$s", formattedTime),
                    "Логин или пароль который Вы помните.",
                    "",
                    "Мы свяжимся с Вами в ближайшее время!",
                    "",
                    "Если это были не Вы, то просим Вас,",
                    "проигнорировать данное письмо.",
                };
                MailHelper.sendMail(mail, title, String.join("\n", message));

                Intent intent = new Intent(ForgotActivity.this, SigninActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        googleButton.setOnClickListener(view -> {
            Toast.makeText(ForgotActivity.this, getString(R.string.toast_service), Toast.LENGTH_LONG).show();
        });

        SpannableStringBuilder redirectText = new SpannableStringBuilder();
        redirectText.append(TextHelper.getColoredString(getString(R.string.button_account_1), false, getColor(R.color.tertiary)));
        redirectText.append(TextHelper.getColoredString(getString(R.string.button_signin), true, getColor(R.color.primary)));
        redirectButton.setText(redirectText);

        redirectButton.setOnClickListener(view -> {
            Intent intent = new Intent(ForgotActivity.this, SigninActivity.class);
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
        mailFiled = findViewById(R.id.field1);
        checkField = findViewById(R.id.field2);

        checkButton = findViewById(R.id.button1);
        recoverButton = findViewById(R.id.button2);
        googleButton = findViewById(R.id.button3);
        redirectButton = findViewById(R.id.button4);
    }

    private boolean isEmail(String mail) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(mail);
        return matcher.matches();
    }
}
