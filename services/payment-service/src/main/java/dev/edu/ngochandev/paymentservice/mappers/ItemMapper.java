package dev.edu.ngochandev.paymentservice.mappers;

import dev.edu.ngochandev.paymentservice.dtos.res.ItemResponse;
import dev.edu.ngochandev.paymentservice.entities.ItemEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface ItemMapper {
    ItemResponse toResponseDto(ItemEntity entity);
}
