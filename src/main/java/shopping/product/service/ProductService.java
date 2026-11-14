package shopping.product.service;

import java.util.List;
import org.springframework.stereotype.Service;
import shopping.product.domain.Product;
import shopping.product.exception.ProductNotFoundException;
import shopping.product.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product create(String name, int price, String imageUrl) {
        Product product = new Product(null, name, price, imageUrl);
        return productRepository.save(product);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다."));
    }

    public Product update(Long id, String name, int price, String imageUrl) {
        findById(id);
        Product product = new Product(id, name, price, imageUrl);
        return productRepository.save(product);
    }

    public void delete(Long id) {
        findById(id);
        productRepository.deleteById(id);
    }
}
