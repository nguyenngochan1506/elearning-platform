package dev.edu.ngochandev.paymentservice.controllers;

import dev.edu.ngochandev.common.dtos.res.SuccessResponseDto;
import dev.edu.ngochandev.common.i18n.Translator;
import dev.edu.ngochandev.paymentservice.dtos.req.CreateProductRequestDto;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponseDto<ProductResponse> createProduct(@RequestBody CreateProductRequestDto req) {
        return SuccessResponseDto.<ProductResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message(translator.translate("product.create.success"))
                .data(productService.createProduct(req))
                .build();
    }
}
