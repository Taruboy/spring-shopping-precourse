package shopping.product.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import shopping.product.domain.Product;

public class ProductRepository {
    private final Map<Long, Product> products = new HashMap<>();
    private long nextId = 1L;

    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    public Product save(Product product) {
        if (!products.containsKey(product.getId())) {
            return saveAsNew(product);
        }
        products.put(product.getId(), product);
        return product;
    }

    private Product saveAsNew(Product product) {
        Product savedProduct = product.withId(nextId);
        nextId += 1;
        products.put(savedProduct.getId(), savedProduct);
        return savedProduct;
    }

    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(products.get(id));
    }

    public void deleteById(Long id) {
        products.remove(id);
    }
}
