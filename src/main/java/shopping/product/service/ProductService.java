package shopping.product.service;

import java.util.List;
import org.springframework.stereotype.Service;
import shopping.product.domain.Product;
import shopping.product.domain.ProductNameValidator;
import shopping.product.exception.ProductNotFoundException;
import shopping.product.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductNameValidator productNameValidator;

    public ProductService(
            ProductRepository productRepository,
            ProductNameValidator productNameValidator) {
        this.productRepository = productRepository;
        this.productNameValidator = productNameValidator;
    }

    public Product create(String name, int price, String imageUrl) {
        productNameValidator.validate(name);
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
        productNameValidator.validate(name);
        findById(id);
        Product product = new Product(id, name, price, imageUrl);
        return productRepository.save(product);
    }

    public void delete(Long id) {
        findById(id);
        productRepository.deleteById(id);
    }
}
