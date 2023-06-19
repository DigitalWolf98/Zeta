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
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import ru.script_dev.zeta.helpers.DataHelper;
import ru.script_dev.zeta.helpers.ProductHelper;

public class ProductActivity extends AppCompatActivity {

    private DataHelper dataFavorite;
    private DataHelper dataChairs;
    private DataHelper dataTables;
    private ConstraintLayout discountCard;
    private LinearLayout chipButtons;
    private GridLayout gridLayout;
    private View navbarButtons;
    private String selectedChip = "All";
    private String selectedMenu = "Home";
    private GestureDetector gestureDetector;
    private String selectedCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product);

        initViews();

        initDiscount();

        initCategory();

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

        Intent intent = new Intent(ProductActivity.this, SigninActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private void initViews() {
        dataFavorite = new DataHelper(getApplicationContext(), "Favorite");
        dataChairs = new DataHelper(getApplicationContext(), "Chairs");
        dataTables = new DataHelper(getApplicationContext(), "Tables");

        discountCard = findViewById(R.id.discountCard);
        chipButtons = findViewById(R.id.chipButtons);
        gridLayout = findViewById(R.id.gridLayout);
        navbarButtons = findViewById(R.id.navbarButtons);
    }

    private void initDiscount() {
        if (selectedChip == "All") discountCard.setVisibility(View.GONE);
        else {
            discountCard.setVisibility(View.VISIBLE);

            ProductHelper productHelper = null;

            ImageView image = discountCard.findViewById(R.id.discountImage);
            TextView title = discountCard.findViewById(R.id.discountTitle);
            TextView price = discountCard.findViewById(R.id.discountText);

            switch (selectedChip) {
                case "Стулья":
                    productHelper = dataChairs.getData();
                    break;
                case "Столы":
                    productHelper = dataTables.getData();
                    break;
            }

            Integer random = new Random().nextInt(productHelper.getProducts().size());
            ProductHelper.Product product = productHelper.getProduct(random);
            if (product.getDiscount() > 0) {
                image.setImageResource(productHelper.getImage(random).get(1));
                title.setText(getString(R.string.card_product, selectedChip, product.getTitle()));
                price.setText(getString(R.string.card_discount, product.getDiscount().toString()));
            } else initDiscount();
        }
    }

    private void initCategory() {
        int count = chipButtons.getChildCount();

        int color1 = getColor(R.color.gray_1);
        int color2 = getColor(R.color.gray_3);

        for (int i = 0; i < count; i++) {
            Button button = (Button) chipButtons.getChildAt(i);
            button.setTextColor(color1);
            button.setOnClickListener(view -> {
                for (int j = 0; j < count; j++) {
                    Button btn = (Button) chipButtons.getChildAt(j);
                    btn.setTextColor(color1);
                }
                button.setTextColor(color2);

                selectedChip = button.getText().toString();

                initDiscount();
                initProducts();
            });
        }
    }

    private void initProducts() {
        List<ProductHelper.Product> favoriteProducts = dataFavorite.getData().getProducts();

        ProductHelper chairsHelper = dataChairs.getData();
        ProductHelper tablesHelper = dataTables.getData();

        gridLayout.removeAllViews();
        LayoutInflater layoutInflater = getLayoutInflater();

        initGesture();
        switch (selectedChip) {
            case "All":
                initCards(layoutInflater, chairsHelper, favoriteProducts);
                initCards(layoutInflater, tablesHelper, favoriteProducts);
                break;
            case "Стулья":
                initCards(layoutInflater, chairsHelper, favoriteProducts);
                break;
            case "Столы":
                initCards(layoutInflater, tablesHelper, favoriteProducts);
                break;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initCards(
            LayoutInflater layoutInflater,
            ProductHelper productHelper,
            List<ProductHelper.Product> favoriteProducts
    ) {
        productHelper.getProducts().forEach(product -> {
            Integer index = productHelper.getIndex(product);

            Integer index1 = IntStream.range(0, favoriteProducts.size())
                .filter(i -> favoriteProducts.get(i).getTitle().equals(product.getTitle()))
                .findFirst().orElse(-1);

            View cardView = layoutInflater.inflate(R.layout.product, null);

            if (index1 > -1) {
                ImageView favImage = cardView.findViewById(R.id.cardFavorite);
                favImage.setImageResource(R.drawable.icon_favorite_on);
            }

            ImageView imageView = cardView.findViewById(R.id.cardImage);
            imageView.setImageResource(productHelper.getImage(index).get(0));

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
        gestureDetector = new GestureDetector(ProductActivity.this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent event) {
                ProductHelper dataProducts = dataFavorite.getData();

                Integer index = IntStream.range(0, dataProducts.getProducts().size())
                    .filter(i -> dataProducts.getProducts().get(i).getTitle().equals(selectedCart))
                    .findFirst().orElse(-1);

                if (index == -1) {
                    ProductHelper productChairs = dataChairs.getData();
                    ProductHelper productTables = dataTables.getData();

                    Boolean init = initFavorite(dataProducts, productChairs);
                    if (!init) initFavorite(dataProducts, productTables);

                    Toast.makeText(ProductActivity.this, "Товар добавлен\nв избарнное!", Toast.LENGTH_SHORT).show();
                }

                dataFavorite.setData(dataProducts);
                dataFavorite.saveData();

                initProducts();
                return super.onDoubleTap(event);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent event) {
                Intent intent = new Intent(ProductActivity.this, PreviewActivity.class);

                intent.putExtra("Title", selectedCart);
                startActivity(intent);
                overridePendingTransition(0, 0);

                return super.onSingleTapConfirmed(event);
            }

            private boolean initFavorite(ProductHelper data, ProductHelper products) {
                for (int i = 0; i < products.getProducts().size(); i++) {
                    ProductHelper.Product product = products.getProducts().get(i);

                    if (selectedCart.equals(product.getTitle())) {
                        data.setProduct(
                                product.getTitle(),
                                product.getAbout(),
                                product.getPrice(),
                                product.getDiscount()
                        );
                        data.setImage(products.getImage(i));
                        return true;
                    }
                }
                return false;
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
                    Intent intent = new Intent(ProductActivity.this, element.second);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
            });
        });
    }
}
