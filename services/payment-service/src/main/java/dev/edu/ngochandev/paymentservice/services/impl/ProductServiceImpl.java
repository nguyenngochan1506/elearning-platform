package dev.edu.ngochandev.paymentservice.services.impl;

import dev.edu.ngochandev.common.dtos.req.AdvancedFilterRequestDto;
import dev.edu.ngochandev.common.dtos.res.PageResponseDto;
import dev.edu.ngochandev.common.exceptions.ResourceNotFoundException;
import dev.edu.ngochandev.paymentservice.commons.MyUtils;
import dev.edu.ngochandev.paymentservice.commons.enums.CurrencyType;
import dev.edu.ngochandev.paymentservice.dtos.req.CreateProductRequestDto;
import dev.edu.ngochandev.paymentservice.dtos.req.UpdateProductRequestDto;
import dev.edu.ngochandev.paymentservice.dtos.res.ProductResponse;
import dev.edu.ngochandev.paymentservice.entities.ItemEntity;
import dev.edu.ngochandev.paymentservice.entities.ProductEntity;
import dev.edu.ngochandev.paymentservice.mappers.ProductMapper;
import dev.edu.ngochandev.paymentservice.repositories.ItemRepository;
import dev.edu.ngochandev.paymentservice.repositories.ProductRepository;
import dev.edu.ngochandev.paymentservice.services.ProductService;
import dev.edu.ngochandev.paymentservice.specs.ProductSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j(topic = "PRODUCT-SERVICE")
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ItemRepository itemRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponse createProduct(CreateProductRequestDto req) {
//        TODO: check if the items are owned the user or organization or super_admin
        List<ItemEntity> items = itemRepository.findAllByItemUuidIn(req.getItemUuids());
        if(items.size() != req.getItemUuids().size()) {
            throw new ResourceNotFoundException("error.item.not_found");
        }
        ProductEntity product = new ProductEntity();
        product.setName(req.getName());
        product.setDescription(req.getDescription());
        product.setThumbnail(req.getThumbnail());
        product.setPrice(CurrencyType.toStoredAmount(req.getPrice(), req.getCurrency()));
        product.setCurrency(req.getCurrency());
        product.setItems(new HashSet<>(items));

//        TODO: replace with actual user and organization
        product.setOrganizationUuid("system");
        product.setCreatedBy(-1L);

        product = productRepository.save(product);
        return productMapper.toResponseDto(product);
    }

    @Override
    public ProductResponse updateProduct(UpdateProductRequestDto req) {
//        TODO: check if the items are owned the user or organization or super_admin

        ProductEntity product = productRepository.findByUuid(req.getUuid())
                .orElseThrow(() -> new ResourceNotFoundException("error.product.not_found"));

        product.setName(req.getName() != null ? req.getName() : product.getName());
        product.setDescription(req.getDescription() != null ? req.getDescription() : product.getDescription());
        product.setThumbnail(req.getThumbnail() != null ? req.getThumbnail() : product.getThumbnail());
        product.setPrice(req.getPrice() != null ? CurrencyType.toStoredAmount(req.getPrice(), req.getCurrency() != null ? req.getCurrency() : product.getCurrency()) : product.getPrice());
        product.setCurrency(req.getCurrency() != null ? req.getCurrency() : product.getCurrency());
        product.setIsActive(req.getIsActive() != null ? req.getIsActive() : product.getIsActive());
        if(!req.getItemUuids().isEmpty()){
            List<ItemEntity> items = itemRepository.findAllByItemUuidIn(req.getItemUuids());
            if(items.size() != req.getItemUuids().size()) {
                throw new ResourceNotFoundException("error.item.not_found");
            }
            product.setItems(new HashSet<>(items));
        }else{
            product.setItems(null);
        }
        product = productRepository.save(product);

        return productMapper.toResponseDto(product);
    }

    @Override
    public PageResponseDto<ProductResponse> getProducts(AdvancedFilterRequestDto filter) {
        Pageable pageable = MyUtils.createPageable(filter);

        Specification<ProductEntity> spec = new ProductSpecification(filter.getFilters(), filter.getSearch());

        Page<ProductEntity> pageOfProducts = productRepository.findAll(spec, pageable);

        List<ProductResponse> products = pageOfProducts.getContent().stream().map(productMapper::toResponseDto).toList();

        return PageResponseDto.<ProductResponse>builder()
                .items(products)
                .currentPage(filter.getPage())
                .totalPages(pageOfProducts.getTotalPages())
                .totalElements(pageOfProducts.getTotalElements())
                .build();
    }
}
