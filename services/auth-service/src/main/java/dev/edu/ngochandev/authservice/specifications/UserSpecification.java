package dev.edu.ngochandev.authservice.specifications;

import dev.edu.ngochandev.authservice.commons.MyUtils;
import dev.edu.ngochandev.common.dtos.req.AdvancedFilterRequestDto;
import dev.edu.ngochandev.authservice.entities.UserEntity;
import dev.edu.ngochandev.authservice.exceptions.FilterDataException;
import jakarta.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.util.StringUtils;

public class UserSpecification implements Specification<UserEntity> {
    private final List<AdvancedFilterRequestDto.FilterData> filterData;
    private final String search;

    public UserSpecification(List<AdvancedFilterRequestDto.FilterData> filterData, String search) {
        this.filterData = filterData;
        this.search = search;
    }

    @Override
    public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        // search
        if (StringUtils.hasLength(search)) {
            String searchPattern = "%" + search.toLowerCase() + "%";
            Predicate searchPredicate = cb.or(
                    cb.like(cb.lower(root.get("username")), searchPattern),
                    cb.like(cb.lower(root.get("email")), searchPattern),
                    cb.like(cb.lower(root.get("fullName")), searchPattern));
            predicates.add(searchPredicate);
        }
        // filter
        if (filterData != null && !filterData.isEmpty()) {
            for (AdvancedFilterRequestDto.FilterData filter : filterData) {
                String field = null;
                Class<?> fieldType = null;
                try {
                    field = filter.getField();
                    fieldType = root.get(field).getJavaType();
                    if (fieldType == null) {
                        throw new FilterDataException(String.format(
                                "Invalid filter field: %s does not exist in UserEntity", field));
                    }

                    if (filter.getValue().getClass().isAssignableFrom(fieldType)
                            || fieldType.isAssignableFrom(List.class)) {
                        throw new FilterDataException(String.format(
                                "Invalid filter value for field: %s must be of type %s or List<%s>",
                                field, fieldType.getSimpleName(), fieldType.getSimpleName()));
                    }

                    switch (filter.getOperator()) {
                        case EQUALS -> predicates.add(cb.equal(root.get(field), filter.getValue()));
                        case CONTAINS -> predicates.add(
                                cb.like(root.get(field), "%" + ((String) filter.getValue()).toLowerCase() + "%"));
                        case GREATER_THAN -> predicates.add(
                                cb.greaterThan(root.get(field), (Comparable) filter.getValue()));
                        case LESS_THAN -> predicates.add(cb.lessThan(root.get(field), (Comparable) filter.getValue()));
                        case BETWEEN -> {
                            List<?> valueList = (List<?>) filter.getValue();
                            if (fieldType == LocalDateTime.class) {
                                LocalDateTime start = MyUtils.parseFlexibleDate(
                                        valueList.get(0).toString());
                                LocalDateTime end = MyUtils.parseFlexibleDate(
                                        valueList.get(1).toString());
                                predicates.add(cb.between(root.get(field), start, end));
                            } else if ((fieldType == Integer.class)
                                    || (fieldType == int.class)
                                    || (fieldType == Long.class)
                                    || (fieldType == long.class)) {
                                Expression<Integer> expression = root.get(field);
                                Integer lower =
                                        Integer.parseInt(valueList.get(0).toString());
                                Integer upper =
                                        Integer.parseInt(valueList.get(1).toString());
                                predicates.add(cb.between(expression, lower, upper));
                            }
                        }
                        case IN -> {
                            if (filter.getValue() instanceof List<?> list && !list.isEmpty()) {
                                predicates.add(root.get(field).in(list));
                            }
                        }
                    }
                } catch (JpaSystemException e) {
                    throw new FilterDataException(String.format(
                            "Invalid filter value for field: %s must be of type %s or List<%s>",
                            field, fieldType.getSimpleName(), fieldType.getSimpleName()));
                } catch (FilterDataException e) {
                    throw e;
                }
            }
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
