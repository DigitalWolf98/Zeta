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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.script_dev.zeta.helpers.MailHelper;
import ru.script_dev.zeta.helpers.TextHelper;
import ru.script_dev.zeta.helpers.UserHelper;

public class SignupActivity extends AppCompatActivity {

    private EditText loginField;
    private EditText mailFiled;
    private EditText passwordField;
    private EditText confirmField;
    private Button signupButton;
    private View googleButton;
    private TextView redirectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        initViews();

		String savedName = UserHelper.getName(getApplicationContext());
		String savedMail = UserHelper.getMail(getApplicationContext());
		String savedPass = UserHelper.getPass(getApplicationContext());

		if (!savedName.equals("") && !savedMail.equals("") && !savedPass.equals("")) {
			Intent intent = new Intent(SignupActivity.this, ProductActivity.class);
			startActivity(intent);
			overridePendingTransition(0, 0);
		} else {
			String text = getString(R.string.field_placeholder, getString(R.string.field_enter), getString(R.string.label_login));
			loginField.setHint(text);
			text = getString(R.string.field_placeholder, getString(R.string.field_enter), getString(R.string.label_mail).replace("а", "у"));
			mailFiled.setHint(text);
			text = getString(R.string.field_placeholder, getString(R.string.field_enter), getString(R.string.label_password));
			passwordField.setHint(text);
			text = getString(R.string.field_placeholder, getString(R.string.label_confirm), getString(R.string.label_password).replace("ь", "я"));
			confirmField.setHint(text);

			signupButton.setOnClickListener(view -> {
				String login = loginField.getText().toString().trim();
				String mail = mailFiled.getText().toString().trim();
				String password = passwordField.getText().toString().trim();
				String confirm = confirmField.getText().toString().trim();

				Toast toast = Toast.makeText(SignupActivity.this, "", Toast.LENGTH_SHORT);

				if (login.length() < 4) {
					toast.setText("Ваш логин должен быть более 4 символов!");
					toast.show();
				} else if (!isEmail(mail)) {
					toast.setText("Вы указали неверную почту!");
					toast.show();
				} else if (password.length() < 6) {
					toast.setText("Ваш пароль должен быть более 6 символов!");
					toast.show();
				} else if (!password.equals(confirm)) {
					toast.setText("Ваши пароли не совпадают!");
					toast.show();
				} else {
					UserHelper.saveUser(getApplicationContext(), login, mail, confirm);

					String title = String.format("Регистрация в %1$s", getString(R.string.app_name));
					String[] message = {
						"Мы рады видеть Вас нашим клиентом!",
						"Вы успешно зарегистрировались в нашем приложении.",
						"",
						"Ваши данные для входа:",
						String.format("Логин: %1$s", login),
						String.format("Пароль: %1$s", password),
						"",
						"Ожидаем скорейших Ваших заказов..."
					};
					MailHelper.sendMail(mail, title, String.join("\n", message));

					Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
					startActivity(intent);
					overridePendingTransition(0, 0);
				}
			});

			googleButton.setOnClickListener(view -> {
				Toast.makeText(SignupActivity.this, getString(R.string.toast_service), Toast.LENGTH_LONG).show();
			});
		}

		SpannableStringBuilder redirectText = new SpannableStringBuilder();
		redirectText.append(TextHelper.getColoredString(getString(R.string.button_account_1), false, getColor(R.color.tertiary)));
		redirectText.append(TextHelper.getColoredString(getString(R.string.button_signin), true, getColor(R.color.primary)));
		redirectButton.setText(redirectText);

		redirectButton.setOnClickListener(view -> {
			Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
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
        mailFiled = findViewById(R.id.field2);
        passwordField = findViewById(R.id.field3);
        confirmField = findViewById(R.id.field4);

        signupButton = findViewById(R.id.button1);
        googleButton = findViewById(R.id.button2);
        redirectButton = findViewById(R.id.button3);
    }

    private boolean isEmail(String mail) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(mail);
        return matcher.matches();
    }
}
