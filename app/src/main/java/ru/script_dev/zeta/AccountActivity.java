package ru.script_dev.zeta;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableStringBuilder;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.script_dev.zeta.helpers.TextHelper;
import ru.script_dev.zeta.helpers.UserHelper;

public class AccountActivity extends AppCompatActivity {
	private ImageView imageView;
	private Uri selectedImage;
	private String temp = "temp_zeta.jpg";
	private int requestSelect = 1;
	private int requestWrite = 2;
	private LinearLayout fieldView1;
	private LinearLayout fieldView2;
	private LinearLayout fieldView3;
	private LinearLayout fieldView4;
	private Button saveButton;
	private TextView redirectButton;
	private LinearLayout navbarButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_account);

		initViews();

		fieldView4.setVisibility(View.GONE);
		LinearLayout parent = (LinearLayout) fieldView4.getParent();
		parent.getChildAt(0).setVisibility(View.GONE);
		saveButton.setVisibility(View.GONE);

		SpannableStringBuilder redirectText = new SpannableStringBuilder();
		redirectText.append(TextHelper.getColoredString(getString(R.string.button_profile_0), false, getColor(R.color.tertiary)));
		redirectText.append(TextHelper.getColoredString("Разработчиком", true, getColor(R.color.primary)));
		redirectButton.setText(redirectText);

		redirectButton.setOnClickListener(view -> {
			Intent intent = new Intent(AccountActivity.this, ProfileActivity.class);
			startActivity(intent);
			overridePendingTransition(0, 0);
		});

		initAvatar();

		initFields();

		initButtons();

		initNavbar();
    }

	private void initViews() {
		imageView = findViewById(R.id.imageView);

		fieldView1 = findViewById(R.id.fieldView1);
		fieldView2 = findViewById(R.id.fieldView2);
		fieldView3 = findViewById(R.id.fieldView3);
		fieldView4 = findViewById(R.id.fieldView4);

		saveButton = findViewById(R.id.buttonView1);
		redirectButton = findViewById(R.id.buttonView2);
		navbarButtons = findViewById(R.id.navbarButtons);
	}

	@SuppressLint("IntentReset")
	private void initAvatar() {
		readImage();

		imageView.setOnClickListener(view -> {
			Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			intent.setType("image/*");
			intent.putExtra("crop", true);
			startActivityForResult(intent, requestSelect);
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == requestSelect && resultCode == RESULT_OK && data != null) {
			selectedImage = data.getData();
			requestPermission();
		}
	}

	private void requestPermission() {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
			saveImage();
		} else {
			ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE }, requestWrite);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		if (requestCode == requestWrite && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			saveImage();
		}
	}

	private void saveImage() {
		if (selectedImage != null) {
			try {
				InputStream inputStream = getContentResolver().openInputStream(selectedImage);
				File appFolder = getExternalFilesDir(null);
				File tempFile = new File(appFolder, temp);
				OutputStream outputStream = new FileOutputStream(tempFile);
				byte[] buffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}
				inputStream.close();
				outputStream.close();
				readImage();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void readImage() {
		File appFolder = getExternalFilesDir(null);
		File tempFile = new File(appFolder, temp);
		if (tempFile.exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
			imageView.setForeground(null);
			imageView.setImageBitmap(bitmap);
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	private void initFields() {
		List<Pair<String, Pair<LinearLayout, String>>> groups = new ArrayList<>();

		groups.add(Pair.create("Name", Pair.create(fieldView1, UserHelper.getName(getApplicationContext()))));
		groups.add(Pair.create("Mail", Pair.create(fieldView2, UserHelper.getMail(getApplicationContext()))));
		groups.add(Pair.create("Password", Pair.create(fieldView3, UserHelper.getPass(getApplicationContext()))));
		groups.add(Pair.create("Confirm", Pair.create(fieldView4, UserHelper.getPass(getApplicationContext()))));

		groups.forEach(element -> {
			EditText field = (EditText) element.second.first.getChildAt(0);
			ImageView pen = (ImageView) element.second.first.getChildAt(1);

			field.setText(element.second.second);

			element.second.first.setOnClickListener(view -> {
				pen.setForegroundTintList(ColorStateList.valueOf((getColor(R.color.primary))));

				field.requestFocus();
				field.setSelection(field.getText().length());

				InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				manager.showSoftInput(field, InputMethodManager.SHOW_IMPLICIT);

				switch (element.first) {
					case "Password":
						fieldView4.setVisibility(View.VISIBLE);
						LinearLayout parent = (LinearLayout) fieldView4.getParent();
						parent.getChildAt(0).setVisibility(View.VISIBLE);
					default:
						saveButton.setVisibility(View.VISIBLE);
						break;
				}
			});

			field.setOnTouchListener((view, motionEvent) -> {
				element.second.first.callOnClick();
				return false;
			});
		});
	}

	private void initButtons() {
		EditText loginField = findViewById(R.id.editText1);
		EditText mailFiled = findViewById(R.id.editText2);
		EditText passwordField = findViewById(R.id.editText3);
		EditText confirmField = findViewById(R.id.editText4);

		saveButton.setOnClickListener(view -> {
			String login = loginField.getText().toString().trim();
			String mail = mailFiled.getText().toString().trim();
			String password = passwordField.getText().toString().trim();
			String confirm = confirmField.getText().toString().trim();

			Toast toast = Toast.makeText(AccountActivity.this, "", Toast.LENGTH_SHORT);

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

				List<LinearLayout> fields = new ArrayList<>();
				fields.add(fieldView1);
				fields.add(fieldView2);
				fields.add(fieldView3);
				fields.add(fieldView4);

				fields.forEach(field -> {
					field.getChildAt(0).clearFocus();
					field.getChildAt(1).setForegroundTintList(ColorStateList.valueOf((getColor(R.color.tertiary))));
				});

				fieldView4.setVisibility(View.GONE);
				LinearLayout parent = (LinearLayout) fieldView4.getParent();
				parent.getChildAt(0).setVisibility(View.GONE);
				saveButton.setVisibility(View.GONE);

				toast.setText("Ваши данные обновлены!");
				toast.show();
			}
		});
	}

	private boolean isEmail(String mail) {
		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(mail);
		return matcher.matches();
	}

	private void initNavbar() {
		List<Pair<Integer, Class<? extends AppCompatActivity>>> menu = new ArrayList<>();

		menu.add(Pair.create(R.id.navbarButton1, ProductActivity.class));
		menu.add(Pair.create(R.id.navbarButton2, CartActivity.class));
		menu.add(Pair.create(R.id.navbarButton3, FavoriteActivity.class));
		menu.add(Pair.create(R.id.navbarButton4, AccountActivity.class));

		menu.forEach(element -> {
			LinearLayout button = navbarButtons.findViewById(element.first);

			if (element.second.isInstance(this))
				button.getChildAt(1).setBackgroundTintList(getColorStateList(R.color.tertiary));
			else button.getChildAt(1).setBackgroundTintList(getColorStateList(R.color.gray_1));

			button.setOnClickListener(view -> {
				if (!element.second.isInstance(this)) {
					Intent intent = new Intent(AccountActivity.this, element.second);
					startActivity(intent);
					overridePendingTransition(0, 0);
				}
			});
		});
	}
}
