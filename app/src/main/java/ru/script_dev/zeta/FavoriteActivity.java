package ru.script_dev.zeta;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import ru.script_dev.zeta.helpers.DataHelper;
import ru.script_dev.zeta.helpers.ProductHelper;

public class FavoriteActivity extends AppCompatActivity {

    private DataHelper dataFavorite;
    private Button chipButton1;
    private Button chipButton2;
    private GridLayout gridLayout;
    private View navbarButtons;
    private GestureDetector gestureDetector;
    private String selectedCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_favorite);

        initViews();

        initActions();

        initProducts();

        initNavbar();
    }

    @Override
    public void onResume() {
        super.onResume();
        initProducts();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //Toast.makeText(this, "It is impossible to go back, use the navigation buttons.", Toast.LENGTH_SHORT).show();

        dataFavorite.saveData();

        Intent intent = new Intent(FavoriteActivity.this, SigninActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private void initViews() {
        dataFavorite = new DataHelper(getApplicationContext(), "Favorite");

        chipButton1 = findViewById(R.id.chipButton1);
        chipButton2 = findViewById(R.id.chipButton2);
        gridLayout = findViewById(R.id.gridLayout);
        navbarButtons = findViewById(R.id.navbarButtons);
    }

    private void initActions() {
        chipButton1.setOnClickListener(view -> {
            if (dataFavorite.getData().getProducts().size() > 0) {
                Random random = new Random();

                List<Class<? extends AppCompatActivity>> pages = new ArrayList<>();
                pages.add(TrueActivity.class);
                pages.add(FalseActivity.class);

                chipButton2.callOnClick();

                Intent intent = new Intent(FavoriteActivity.this, pages.get(random.nextInt(pages.size())));
                startActivity(intent);
                overridePendingTransition(0, 0);
            } else Toast.makeText(FavoriteActivity.this, "Доабвьте минимум 1 товар в избаранное.", Toast.LENGTH_SHORT).show();
        });

        chipButton2.setOnClickListener(view -> {
            ProductHelper productCart = new ProductHelper();

            dataFavorite.setData(productCart);
            dataFavorite.saveData();

            initProducts();
        });
    }

    private void initProducts() {
        ProductHelper favorite = dataFavorite.getData();

        gridLayout.removeAllViews();
        LayoutInflater layoutInflater = getLayoutInflater();

        initGesture();
        initCards(layoutInflater, favorite);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initCards(
            LayoutInflater inflater,
            ProductHelper favorite
    ) {
        favorite.getProducts().forEach(product -> {
            Integer index = favorite.getIndex(product);

            View cardView = inflater.inflate(R.layout.product, null);

            ImageView favImage = cardView.findViewById(R.id.cardFavorite);
            favImage.setImageResource(R.drawable.icon_favorite_on);

            ImageView imageView = cardView.findViewById(R.id.cardImage);
            imageView.setImageResource(favorite.getImage(index).get(0));

            TextView cardTitle = cardView.findViewById(R.id.cardTitle);
            cardTitle.setText(product.getTitle().replaceFirst(" ", "\n"));

            TextView cardPrice = cardView.findViewById(R.id.cardPrice);
            cardPrice.setText("?₸".replace("?", product.getPrice().toString()));

            cardView.setOnTouchListener((view, event) -> {
                selectedCart = product.getTitle().replace("\n", " ");
                return gestureDetector.onTouchEvent(event);
            });

            gridLayout.addView(cardView);
        });
    }

    private void initGesture() {
        gestureDetector = new GestureDetector(FavoriteActivity.this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent event) {
                List<ProductHelper.Product> dataProducts = dataFavorite.getData().getProducts();
                List<List<Integer>> dataImages = dataFavorite.getData().getImages();

                dataFavorite = new DataHelper(getApplicationContext(), "Favorite");
                ProductHelper productHelper = new ProductHelper();

                Integer index = IntStream.range(0, dataProducts.size())
                        .filter(i -> dataProducts.get(i).getTitle().equals(selectedCart))
                        .findFirst().orElse(-1);

                for (int i = 0; i < dataProducts.size(); i++) {
                    if (index != i) {
                        ProductHelper.Product product = dataProducts.get(i);
                        productHelper.setProduct(
                            product.getTitle(),
                            product.getAbout(),
                            product.getPrice(),
                            product.getDiscount()
                        );
                        productHelper.setImage(dataImages.get(i));
                    }
                }

                dataFavorite.setData(productHelper);

                initProducts();
                return super.onDoubleTap(event);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent event) {
                Intent intent = new Intent(FavoriteActivity.this, PreviewActivity.class);

                intent.putExtra("Title", selectedCart);
                startActivity(intent);
                overridePendingTransition(0, 0);

                return super.onSingleTapConfirmed(event);
            }
        });
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
                    dataFavorite.saveData();

                    Intent intent = new Intent(FavoriteActivity.this, element.second);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
            });
        });
    }
}
