package dev.edu.ngochandev.paymentservice.controllers;

import dev.edu.ngochandev.common.dtos.req.AdvancedFilterRequestDto;
import dev.edu.ngochandev.common.dtos.res.PageResponseDto;
import dev.edu.ngochandev.common.dtos.res.SuccessResponseDto;
import dev.edu.ngochandev.common.i18n.Translator;
import dev.edu.ngochandev.paymentservice.dtos.req.CreateProductRequestDto;
import dev.edu.ngochandev.paymentservice.dtos.req.UpdateProductRequestDto;
import dev.edu.ngochandev.paymentservice.dtos.res.ProductResponse;
import dev.edu.ngochandev.paymentservice.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final Translator translator;
    private final ProductService productService;

    @PostMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<PageResponseDto<ProductResponse>> getProducts(@RequestBody AdvancedFilterRequestDto filter){
        return SuccessResponseDto.<PageResponseDto<ProductResponse>>builder()
                .status(HttpStatus.OK.value())
                .message(translator.translate("product.list.success"))
                .data(productService.getProducts(filter))
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponseDto<ProductResponse> createProduct(@RequestBody CreateProductRequestDto req) {
        return SuccessResponseDto.<ProductResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message(translator.translate("product.create.success"))
                .data(productService.createProduct(req))
                .build();
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<ProductResponse> updateProduct(@RequestBody UpdateProductRequestDto req) {
        return SuccessResponseDto.<ProductResponse>builder()
                .status(HttpStatus.OK.value())
                .message(translator.translate("product.update.success"))
                .data(productService.updateProduct(req))
                .build();
    }
}
