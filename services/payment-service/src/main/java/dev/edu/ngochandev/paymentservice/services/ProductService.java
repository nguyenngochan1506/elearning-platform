package dev.edu.ngochandev.paymentservice.services;

import dev.edu.ngochandev.common.dtos.req.AdvancedFilterRequestDto;
import dev.edu.ngochandev.common.dtos.res.PageResponseDto;
import dev.edu.ngochandev.paymentservice.dtos.req.CreateProductRequestDto;
import dev.edu.ngochandev.paymentservice.dtos.req.UpdateProductRequestDto;
import dev.edu.ngochandev.paymentservice.dtos.res.ProductResponse;

public interface ProductService {

    ProductResponse createProduct(CreateProductRequestDto req);

    ProductResponse updateProduct(UpdateProductRequestDto req);

    PageResponseDto<ProductResponse> getProducts(AdvancedFilterRequestDto filter);
}
