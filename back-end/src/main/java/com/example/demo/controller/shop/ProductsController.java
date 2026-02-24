package com.example.demo.controller.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.shop.ProductsDTO;
import com.example.demo.entity.shop.Products;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.service.Shop.ProductsService;


@RestController
@RequestMapping("/api/products")
// @CrossOrigin(origins = "http://localhost:5173")
public class ProductsController {
    
    @Autowired
    private ProductsService productsService;


    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody ProductsDTO productsDTO){

        try {
            productsService.addProductWithStock(productsDTO);

            return ResponseEntity.ok().body("{\"message\":\"商品與庫存新增成功\"}");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("新增失敗: " +e.getMessage());
        }


    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id, @RequestBody ProductsDTO productsDTO){

        try {
            Products updatedProduct = productsService.updateProduct(id, productsDTO);

            return ResponseEntity.ok().body("{\"message\":\"商品修改成功\"}");
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        
        
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("系統發生錯誤 " );
        }


    }
    
}
