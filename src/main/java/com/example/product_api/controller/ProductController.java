package com.example.product_api.controller;

import com.example.product_api.dto.ProductRequestDTO;
import com.example.product_api.dto.ProductResponseDTO;
import com.example.product_api.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /*@GetMapping()
    public List<ProductResponseDTO> getAllProducts(){
        return productService.getAllProducts();
    }*/
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getProducts(
            @RequestParam(required = false) String category) {

        if (category != null) {
            return ResponseEntity.ok(productService.getProductsByCategory(category));
        }

        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> addProduct(
            @Valid @RequestBody ProductRequestDTO productDTO) {

        ProductResponseDTO savedProduct = productService.save(productDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    /*@GetMapping("{id}")
    public ProductResponseDTO getProduct(@PathVariable Long id){
        return productService.getProduct(id);
    }*/

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable Long id) {

        ProductResponseDTO product = productService.getProduct(id);

        return ResponseEntity.ok(product);
    }

    /*@DeleteMapping("{id}")
    public void deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
    }

    @PutMapping("{id}")
    public ProductResponseDTO updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO product){
        return productService.updateProduct(id, product);
    }*/
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDTO productDTO) {

        ProductResponseDTO updatedProduct = productService.updateProduct(id, productDTO);

        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(@PathVariable String category){
        List<ProductResponseDTO> products = productService.getProductsByCategory(category);

        return ResponseEntity.ok(products);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<ProductResponseDTO>> getProductsPage(
            @RequestParam int page,
            @RequestParam int size) {

        return ResponseEntity.ok(
                productService.getProducts(page, size)
        );
    }
}
