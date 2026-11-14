package shopping.product.domain;

public class Product {
    private final Long id;
    private final String name;
    private final int price;
    private final String imageUrl;

    public Product(Long id, String name, int price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getPrice() {
        return price;
    }

    public Product withId(Long id) {
        return new Product(id, this.name, this.price, this.imageUrl);
    }
}
