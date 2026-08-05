package com.example.product_api.service;

import com.example.product_api.dto.ProductRequestDTO;
import com.example.product_api.dto.ProductResponseDTO;
import com.example.product_api.entity.Product;
import com.example.product_api.exception.ProductNotFoundException;
import com.example.product_api.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponseDTO> getAllProducts(){
        return productRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    /*public Product save(Product product){
        return productRepository.save(product);
    }*/

    public ProductResponseDTO save(ProductRequestDTO dto){

        Product product = convertToEntity(dto);

        Product savedProduct = productRepository.save(product);

        return convertToDTO(savedProduct);
    }

    public ProductResponseDTO getProduct(Long id){
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        return convertToDTO(product);
    }

    public void deleteProduct(Long id){
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

        productRepository.delete(product);
    }

    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto){

        Product existingProduct = productRepository.findById(id).orElseThrow( ()-> new ProductNotFoundException(id));

            existingProduct.setName(dto.getName());
            existingProduct.setPrice(dto.getPrice());
            existingProduct.setCategory(dto.getCategory());

        Product updatedProduct = productRepository.save(existingProduct);

        return convertToDTO(updatedProduct);
    }


    private ProductResponseDTO convertToDTO(Product product){
        return new ProductResponseDTO(product.getId(), product.getName(), product.getPrice(), product.getCategory());
    }

    private Product convertToEntity(ProductRequestDTO dto){
        Product product = new Product();

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setCategory(dto.getCategory());

        return product;
    }


    public List<ProductResponseDTO> getProductsByCategory(String category){
    return productRepository.findByCategory(category)
            .stream()
            .map(this::convertToDTO)
            .toList();
    }

    public Page<ProductResponseDTO> getProducts(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return productRepository.findAll(pageable)
                .map(this::convertToDTO);
    }
}
