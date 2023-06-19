package ru.script_dev.zeta.helpers;

import java.util.ArrayList;
import java.util.List;

public class FavoriteHelper {

    private final List<ProductHelper.Product> products;

    public FavoriteHelper() {
        this.products = new ArrayList<>();
    }

    public void setProduct(ProductHelper.Product data) {
        this.products.add(data);
    }

    public ProductHelper.Product getProduct(Integer index) {
        return this.products.get(index);
    }

    public List<ProductHelper.Product> getProducts() {
        return this.products;
    }

    public void clearProducts() {
        this.products.clear();
    }

    public Integer getIndex(ProductHelper.Product data) {
        return this.products.indexOf(data);
    }
}