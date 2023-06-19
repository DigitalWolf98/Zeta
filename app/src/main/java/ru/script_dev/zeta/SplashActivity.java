package ru.script_dev.zeta;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import ru.script_dev.zeta.helpers.DataHelper;
import ru.script_dev.zeta.helpers.ProductHelper;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private Integer SPLASH_TIMEOUT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> new ConnectDatabaseTask().execute(), SPLASH_TIMEOUT);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //Toast.makeText(this, "It is impossible to go back, use the navigation buttons.", Toast.LENGTH_SHORT).show();
    }

    private class ConnectDatabaseTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            //DatabaseHelper dbHelper = new DatabaseHelper();

            // Setup chairs data
            DataHelper dataChairs = new DataHelper(getApplicationContext(), "Chairs");
            ProductHelper productChairs;

            if (dataChairs.getData() == null) {
                productChairs = new ProductHelper();

                productChairs.setProduct("Орда", getString(R.string.chair_about), 98500.0, 35);
                productChairs.setImage(
                        R.drawable.chairs_a_1, R.drawable.chairs_a_2, R.drawable.chairs_a_3, R.drawable.chairs_a_4,
                        R.drawable.chairs_a_5, R.drawable.chairs_a_6, R.drawable.chairs_a_7, R.drawable.chairs_a_8
                );
                productChairs.setProduct("Бэтмен", getString(R.string.chair_about), 123700.0, 22);
                productChairs.setImage(
                        R.drawable.chairs_b_1, R.drawable.chairs_b_2, R.drawable.chairs_b_3, R.drawable.chairs_b_4,
                        R.drawable.chairs_b_5, R.drawable.chairs_b_6, R.drawable.chairs_b_7, R.drawable.chairs_b_8
                );
                productChairs.setProduct("Катана", getString(R.string.chair_about), 135200.0, 10);
                productChairs.setImage(
                        R.drawable.chairs_c_1, R.drawable.chairs_c_2, R.drawable.chairs_c_3, R.drawable.chairs_c_4,
                        R.drawable.chairs_c_5, R.drawable.chairs_c_6, R.drawable.chairs_c_7, R.drawable.chairs_c_8
                );
                productChairs.setProduct("Контроль", getString(R.string.chair_about), 81900.0, 0);
                productChairs.setImage(
                        R.drawable.chairs_d_1, R.drawable.chairs_d_2, R.drawable.chairs_d_3, R.drawable.chairs_d_4,
                        R.drawable.chairs_d_5, R.drawable.chairs_d_6, R.drawable.chairs_d_7, R.drawable.chairs_d_8
                );

                dataChairs.setData(productChairs);
                dataChairs.saveData();
            } else dataChairs.extractData();

            // Setup tables data
            DataHelper dataTables = new DataHelper(getApplicationContext(), "Tables");
            ProductHelper productTables;

            if (dataTables.getData() == null) {
                productTables = new ProductHelper();

                productTables.setProduct("Ассасин", getString(R.string.table_about), 87600.0, 33);
                productTables.setImage(
                        R.drawable.tables_a_1, R.drawable.tables_a_2, R.drawable.tables_a_3, R.drawable.tables_a_4,
                        R.drawable.tables_a_5, R.drawable.tables_a_6, R.drawable.tables_a_7, R.drawable.tables_a_8
                );
                productTables.setProduct("Киберпанк", getString(R.string.table_about), 65900.0, 10);
                productTables.setImage(
                        R.drawable.tables_b_1, R.drawable.tables_b_2, R.drawable.tables_b_3, R.drawable.tables_b_4,
                        R.drawable.tables_b_5, R.drawable.tables_b_6, R.drawable.tables_b_7, R.drawable.tables_b_8
                );
                productTables.setProduct("Облака", getString(R.string.table_about), 93200.0, 0);
                productTables.setImage(
                        R.drawable.tables_c_1, R.drawable.tables_c_2, R.drawable.tables_c_3, R.drawable.tables_c_4,
                        R.drawable.tables_c_5, R.drawable.tables_c_6, R.drawable.tables_c_7, R.drawable.tables_c_8
                );
                productTables.setProduct("Храбрость", getString(R.string.table_about), 72800.0, 8);
                productTables.setImage(
                        R.drawable.tables_d_1, R.drawable.tables_d_2, R.drawable.tables_d_3, R.drawable.tables_d_4,
                        R.drawable.tables_d_5, R.drawable.tables_d_6, R.drawable.tables_d_7, R.drawable.tables_d_8
                );

                dataTables.setData(productTables);
                dataTables.saveData();
            } else dataTables.extractData();

            // Setup cart data
            DataHelper dataCart = new DataHelper(getApplicationContext(), "Cart");
            ProductHelper cartProducts;

            if (dataCart.getData() == null) {
                cartProducts = new ProductHelper();

                dataCart.setData(cartProducts);
                dataCart.saveData();
            } else dataCart.extractData();

            // Setup favorite data
            DataHelper dataFavorite = new DataHelper(getApplicationContext(), "Favorite");
            ProductHelper favoriteProducts;

            if (dataFavorite.getData() == null) {
                favoriteProducts = new ProductHelper();

                dataFavorite.setData(favoriteProducts);
                dataFavorite.saveData();
            } else dataFavorite.extractData();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Intent intent = new Intent(SplashActivity.this, SignupActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        }
    }
}
