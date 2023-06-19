package ru.script_dev.zeta;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;
import java.util.stream.IntStream;

import ru.script_dev.zeta.helpers.DataHelper;
import ru.script_dev.zeta.helpers.ProductHelper;

public class PreviewActivity extends AppCompatActivity {

    private View navbarGesture;
    private ImageView cardImage;
    private TextView cardTitle;
    private TextView cardAbout;
    private TextView cardPrice;
    private TextView cartCount;
    private ImageView cartPlus;
    private ImageView cartMinus;
    private ConstraintLayout buttonAdd;
    private Button buttonBuy;
    private DataHelper dataCart;
    private ProductHelper.Product productCard;
    private List<Integer> productImages;
    private Integer cartAmount = 1;
    private Double priceOne = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preview);

        initViews();

        initCart();

        initButtons();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //Toast.makeText(this, "It is impossible to go back, use the navigation buttons.", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(PreviewActivity.this, ProductActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private void initViews() {
        navbarGesture = findViewById(R.id.navbarGesture);

        cardImage = findViewById(R.id.cardImage);
        cardTitle = findViewById(R.id.cardTitle);
        cardAbout = findViewById(R.id.cardAbout);
        cardPrice = findViewById(R.id.cardPrice);

        cartCount = findViewById(R.id.cartCount);
        cartPlus = findViewById(R.id.cartPlus);
        cartMinus = findViewById(R.id.cartMinus);

        buttonAdd = findViewById(R.id.buttonAdd);
        buttonBuy = findViewById(R.id.buttonBuy);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initCart() {
        ProductHelper dataChairs = new DataHelper(getApplicationContext(), "Chairs").getData();
        ProductHelper dataTables = new DataHelper(getApplicationContext(), "Tables").getData();

        dataCart = new DataHelper(getApplicationContext(), "Cart");

        String title = getIntent().getStringExtra("Title");

        if (productCard == null) productCard = getProduct(title, dataChairs);
        if (productCard == null) productCard = getProduct(title, dataTables);

        if (productCard != null) {
            priceOne = productCard.getPrice() - ((productCard.getPrice() * productCard.getDiscount()) / 100);

            cardImage.setImageResource(productImages.get(0));
            cardTitle.setText(productCard.getTitle());
            cardAbout.setText(productCard.getAbout());
            cardPrice.setText(getString(R.string.card_price, priceOne.toString()));

            cardImage.setOnClickListener(new View.OnClickListener() {
                private Integer index = 0;

                @Override
                public void onClick(View view) {
                    if (index < productImages.size()-1) index += 1;
                    else index = 0;
                    cardImage.setImageResource(productImages.get(index));
                }
            });
        }
    }

    private ProductHelper.Product getProduct(String title, ProductHelper data) {
        List<ProductHelper.Product> products = data.getProducts();

        Integer index = IntStream.range(0, products.size())
            .filter(i -> products.get(i).getTitle().equals(title))
            .findFirst().orElse(-1);

        if (index > -1) {
            productImages = data.getImage(index);
            return products.get(index);
        } else return null;
    }

    private void initButtons() {
        navbarGesture.setOnClickListener(view -> onBackPressed());

        setAmount("cartAll");

        cartPlus.setOnClickListener(view -> setAmount("cartPlus"));
        cartMinus.setOnClickListener(view -> setAmount("cartMinus"));

        buttonAdd.setOnClickListener(view -> addCarts());
        buttonBuy.setOnClickListener(view -> buyCarts());
    }

    @SuppressLint("DefaultLocale")
    private void setAmount(String action) {
        Toast toast = Toast.makeText(PreviewActivity.this, "", Toast.LENGTH_SHORT);

        switch (action) {
            case "cartPlus":
                if (cartAmount < 10) cartAmount += 1;
                else {
                    toast.setText("Вы не можете выбрать\nбольше 10 товаров за раз.");
                    toast.show();
                }
                break;
            case "cartMinus":
                if (cartAmount > 1) cartAmount -= 1;
                else {
                    toast.setText("Вы не можете выбрать\nменьше 1 товара.");
                    toast.show();
                }
                break;
            case "cartAll":
                long count = IntStream.range(0, dataCart.getData().getProducts().size())
                        .filter(i -> dataCart.getData().getProducts().get(i).getTitle().equals(productCard.getTitle())).count();
                cartAmount = (count > 0) ? Math.toIntExact(count) : 1;
                break;
        }

        cardPrice.setText(getString(R.string.card_price, String.format("%.2f", (priceOne * cartAmount))));
        cartCount.setText(String.format("%1$s", cartAmount));
    }

    private void addCarts() {
        Toast toast = Toast.makeText(PreviewActivity.this, "", Toast.LENGTH_SHORT);

        if ((cartAmount + dataCart.getData().getProducts().size())  <= 10) {
            ProductHelper cartProducts = dataCart.getData();

            for (int i = 0; i < cartAmount; i++) {
                cartProducts.setProduct(
                        productCard.getTitle(),
                        productCard.getAbout(),
                        productCard.getPrice(),
                        productCard.getDiscount()
                );
                cartProducts.setImage(productImages);
            }

            dataCart.setData(cartProducts);
            dataCart.saveData();

            toast.setText("Товар добавлен\nв корзину!");
        } else {
            toast.setText("Ваша корзина заполнена\nданным видом товара.");
        }

        toast.show();
        setAmount("cartAll");
    }

    private void buyCarts() {
        if (dataCart.getData().getProducts().size() > 0) {
			Intent intent = new Intent(PreviewActivity.this, CartActivity.class);
			startActivity(intent);
			overridePendingTransition(0, 0);
		} else Toast.makeText(PreviewActivity.this, "Ваша корзина пуста!\nДобавьте товар...", Toast.LENGTH_LONG).show();
    }
}
