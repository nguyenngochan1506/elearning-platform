package dev.edu.ngochandev.paymentservice.mappers;

import dev.edu.ngochandev.paymentservice.dtos.res.ProductResponse;
import dev.edu.ngochandev.paymentservice.entities.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ItemMapper.class})
public interface ProductMapper {

    ProductResponse toResponseDto(ProductEntity entity);
}
