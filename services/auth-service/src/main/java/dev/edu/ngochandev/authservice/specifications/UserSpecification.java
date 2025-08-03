package dev.edu.ngochandev.authservice.specifications;

import dev.edu.ngochandev.authservice.commons.MyUtils;
import dev.edu.ngochandev.authservice.dtos.req.FilterRequestDto;
import dev.edu.ngochandev.authservice.entities.UserEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class UserSpecification implements Specification<UserEntity> {
    private final List<FilterRequestDto.FilterData> filterData;
    private final String search;

    public UserSpecification(List<FilterRequestDto.FilterData> filterData, String search) {
        this.filterData = filterData;
        this.search = search;
    }

    @Override
    public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        //search
        if(StringUtils.hasLength(search)) {
            String searchPattern = "%" + search.toLowerCase() + "%";
            Predicate searchPredicate = cb.or(
                    cb.like(cb.lower(root.get("username")), searchPattern),
                    cb.like(cb.lower(root.get("email")), searchPattern),
                    cb.like(cb.lower(root.get("fullName")), searchPattern)
            );
            predicates.add(searchPredicate);
        }
        //filter
        if(filterData != null && !filterData.isEmpty()) {
            for(FilterRequestDto.FilterData filter : filterData) {
                String field = filter.getField();
                Class<?> fieldType = root.get(field).getJavaType();

                switch (filter.getOperator()){
                    case EQUALS -> predicates.add(cb.equal(root.get(field), filter.getValue()));
                    case CONTAINS -> predicates.add(cb.like(root.get(field), "%" + ((String)filter.getValue()).toLowerCase() + "%"));
                    case GREATER_THAN -> predicates.add(cb.greaterThan(root.get(field), (Comparable) filter.getValue()));
                    case LESS_THAN -> predicates.add(cb.lessThan(root.get(field), (Comparable) filter.getValue()));
                    case BETWEEN -> {
                        List<?> valueList = (List<?>) filter.getValue();
                        if (fieldType == LocalDateTime.class) {
                            LocalDateTime start = MyUtils.parseFlexibleDate(valueList.get(0).toString());
                            LocalDateTime end = MyUtils.parseFlexibleDate(valueList.get(1).toString());
                            predicates.add(cb.between(root.get(field), start, end));
                        } else {
                            Comparable<Object> lowerBound = (Comparable) valueList.get(0);
                            Comparable<Object> upperBound = (Comparable) valueList.get(1);
                            predicates.add(cb.between(root.get(field), lowerBound, upperBound));
                        }
                    }
                    case IN -> {
                        if (filter.getValue() instanceof List<?> list && !list.isEmpty()) {
                            predicates.add(root.get(field).in(list));
                        }
                    }
                }
            }
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
