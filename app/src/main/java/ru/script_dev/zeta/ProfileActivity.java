package ru.script_dev.zeta;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Pair;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import ru.script_dev.zeta.helpers.TextHelper;

public class ProfileActivity extends AppCompatActivity {

    private TextView redirectButton;
	private LinearLayout navbarButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        initViews();

		SpannableStringBuilder redirectText = new SpannableStringBuilder();
		redirectText.append(TextHelper.getColoredString(getString(R.string.button_profile_1), false, getColor(R.color.tertiary)));
		redirectText.append(TextHelper.getColoredString("Мой профиль", true, getColor(R.color.primary)));
		redirectButton.setText(redirectText);

		redirectButton.setOnClickListener(view -> {
			Intent intent = new Intent(ProfileActivity.this, AccountActivity.class);
			startActivity(intent);
			overridePendingTransition(0, 0);
		});

        initNavbar();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //Toast.makeText(this, "It is impossible to go back, use the navigation buttons.", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ProfileActivity.this, SigninActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private void initViews() {
		redirectButton = findViewById(R.id.buttonView1);
        navbarButtons = findViewById(R.id.navbarButtons);
    }

    private void initNavbar() {
        List<Pair<Integer, Class<? extends AppCompatActivity>>> menu = new ArrayList<>();

        menu.add(Pair.create(R.id.navbarButton1, ProductActivity.class));
        menu.add(Pair.create(R.id.navbarButton2, CartActivity.class));
        menu.add(Pair.create(R.id.navbarButton3, FavoriteActivity.class));
        menu.add(Pair.create(R.id.navbarButton4, ProfileActivity.class));

        menu.forEach(element -> {
            LinearLayout button = navbarButtons.findViewById(element.first);

            if (element.second.isInstance(this))
                button.getChildAt(1).setBackgroundTintList(getColorStateList(R.color.tertiary));
            else button.getChildAt(1).setBackgroundTintList(getColorStateList(R.color.gray_1));

            button.setOnClickListener(view -> {
                if (!element.second.isInstance(this)) {
                    Intent intent = new Intent(ProfileActivity.this, element.second);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
            });
        });
    }
}
