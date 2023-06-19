package ru.script_dev.zeta.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductHelper {

    public class Product {

        private String title;
        private String about;
        private Double price;
        private Integer discount;

        public String getTitle() {
            return this.title;
        }
        public void setTitle(String title) {
            this.title = title;
        }

        public String getAbout() {
            return this.about;
        }
        public void setAbout(String text) {
            this.about = text;
        }

        public Double getPrice() {
            return this.price;
        }
        public void setPrice(Double price) {
            this.price = price;
        }

        public Integer getDiscount() {
            return this.discount;
        }
        public void setDiscount(Integer discount) {
            this.discount = discount;
        }
    }

    private final List<Product> products;
    private final List<List<Integer>> images;

    public ProductHelper() {
        this.products = new ArrayList<>();
        this.images = new ArrayList<>();
    }

    public void setProduct(String title, String about, Double price, Integer discount) {
        Product product = new Product();

        product.setTitle(title);
        product.setAbout(about);
        product.setPrice(price);
        product.setDiscount(discount);

        this.products.add(product);
    }
    public void setImage(Integer... data) {
        this.setImage(Arrays.asList(data));
    }
    public void setImage(List<Integer> data) {
        this.images.add(data);
    }

    public Product getProduct(Integer index) {
        return this.products.get(index);
    }
    public List<Integer> getImage(Integer index) {
        return this.images.get(index);
    }

    public List<Product> getProducts() {
        return this.products;
    }
    public List<List<Integer>> getImages() {
        return this.images;
    }

    public void clearProducts() {
        this.products.clear();
    }
    public void clearImages() {
        this.images.clear();
    }
    public Integer getIndex(Product data) {
        return this.products.indexOf(data);
    }

    public void changeTitle(Integer index, String data) {
        this.products.get(index).setTitle(data);
    }
    public void changeAbout(Integer index, String data) {
        this.products.get(index).setAbout(data);
    }
    public void changePrice(Integer index, Double data) {
        this.products.get(index).setPrice(data);
    }
    public void changeDiscount(Integer index, Integer data) {
        this.products.get(index).setDiscount(data);
    }
}