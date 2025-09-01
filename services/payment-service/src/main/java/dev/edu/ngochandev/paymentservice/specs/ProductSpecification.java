package dev.edu.ngochandev.paymentservice.specs;

import dev.edu.ngochandev.common.dtos.req.AdvancedFilterRequestDto;
import dev.edu.ngochandev.common.enums.OperatorFilter;
import dev.edu.ngochandev.paymentservice.commons.MyUtils;
import dev.edu.ngochandev.paymentservice.commons.enums.CurrencyType;
import dev.edu.ngochandev.paymentservice.entities.CategoryEntity;
import dev.edu.ngochandev.paymentservice.entities.ItemEntity;
import dev.edu.ngochandev.paymentservice.entities.ProductEntity;
import dev.edu.ngochandev.paymentservice.exceptions.FilterDataException;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification implements Specification<ProductEntity> {

    private final List<AdvancedFilterRequestDto.FilterData> filterData;
    private final String search;

    public ProductSpecification(List<AdvancedFilterRequestDto.FilterData> filterData, String search) {
        this.filterData = filterData;
        this.search = search;
    }

    @Override
    public Predicate toPredicate(Root<ProductEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.hasLength(search)) {
            String searchPattern = "%" + search.toLowerCase() + "%";
            Predicate searchPredicate = cb.or(
                    cb.like(cb.lower(root.get("name")), searchPattern),
                    cb.like(cb.lower(root.get("description")), searchPattern)
            );
            predicates.add(searchPredicate);
        }

        if (filterData != null && !filterData.isEmpty()) {
            for (AdvancedFilterRequestDto.FilterData filter : filterData) {
                try {
                    if ("categories".equalsIgnoreCase(filter.getField())) {
                        Join<ProductEntity, ItemEntity> itemJoin = root.join("items", JoinType.INNER);
                        Join<ItemEntity, CategoryEntity> categoryJoin = itemJoin.join("categories", JoinType.INNER);

                        Predicate categoryPredicate = createPredicateForField(cb, categoryJoin.get("slug"), filter.getOperator(), filter.getValue());
                        if(categoryPredicate != null) {
                            predicates.add(categoryPredicate);
                        }
                        query.distinct(true);
                        continue;
                    }

                    Path<?> path = root.get(filter.getField());
                    Predicate fieldPredicate = createPredicateForField(cb, path, filter.getOperator(), filter.getValue());
                    if(fieldPredicate != null) {
                        predicates.add(fieldPredicate);
                    }

                } catch (IllegalArgumentException e) {
                    throw new FilterDataException("Invalid filter field: " + filter.getField());
                }
            }
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }

    // SỬA LẠI THAM SỐ THỨ 3 Ở ĐÂY
    private Predicate createPredicateForField(CriteriaBuilder cb, Path<?> path, OperatorFilter operator, Object value) {
        switch (operator) {
            case EQUALS:
                return cb.equal(path, value);
            case CONTAINS:
                if (path.getJavaType() == String.class) {
                    return cb.like(cb.lower(path.as(String.class)), "%" + ((String) value).toLowerCase() + "%");
                }
                break;
            case GREATER_THAN:
                if ("price".equalsIgnoreCase(path.getAlias())) {
                    return cb.greaterThan(path.as(Long.class), CurrencyType.toStoredAmount(Double.parseDouble(value.toString()), CurrencyType.VND));
                }
                return cb.greaterThan(path.as(Comparable.class), (Comparable) value);
            case LESS_THAN:
                if ("price".equalsIgnoreCase(path.getAlias())) {
                    return cb.lessThan(path.as(Long.class), CurrencyType.toStoredAmount(Double.parseDouble(value.toString()), CurrencyType.VND));
                }
                return cb.lessThan(path.as(Comparable.class), (Comparable) value);
            case BETWEEN:
                List<?> valueList = (List<?>) value;
                if (path.getJavaType() == LocalDateTime.class) {
                    LocalDateTime start = MyUtils.parseFlexibleDate(valueList.get(0).toString());
                    LocalDateTime end = MyUtils.parseFlexibleDate(valueList.get(1).toString());
                    return cb.between(path.as(LocalDateTime.class), start, end);
                } else if ("price".equalsIgnoreCase(path.getAlias())) {
                    Long startPrice = CurrencyType.toStoredAmount(Double.parseDouble(valueList.get(0).toString()), CurrencyType.VND);
                    Long endPrice = CurrencyType.toStoredAmount(Double.parseDouble(valueList.get(1).toString()), CurrencyType.VND);
                    return cb.between(path.as(Long.class), startPrice, endPrice);
                }
                break;
            case IN:
                if (value instanceof List<?> list && !list.isEmpty()) {
                    return path.in(list);
                }
                break;
        }
        return null;
    }
}