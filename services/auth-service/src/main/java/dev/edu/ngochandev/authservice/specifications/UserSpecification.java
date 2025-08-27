package dev.edu.ngochandev.authservice.specifications;

import dev.edu.ngochandev.authservice.commons.MyUtils;
import dev.edu.ngochandev.common.dtos.req.AdvancedFilterRequestDto;
import dev.edu.ngochandev.authservice.entities.UserEntity;
import dev.edu.ngochandev.authservice.entities.UserOrganizationRoleEntity; // THÊM IMPORT
import dev.edu.ngochandev.authservice.exceptions.FilterDataException;
import jakarta.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class UserSpecification implements Specification<UserEntity> {
    private final List<AdvancedFilterRequestDto.FilterData> filterData;
    private final String search;
    private final Long organizationId;

    public UserSpecification(List<AdvancedFilterRequestDto.FilterData> filterData, String search, Long organizationId) {
        this.filterData = filterData;
        this.search = search;
        this.organizationId = organizationId;
    }

    @Override
    public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        Join<UserEntity, UserOrganizationRoleEntity> userOrgJoin = root.join("userOrganizationRoles");

        predicates.add(cb.equal(userOrgJoin.get("organization").get("id"), this.organizationId));

        predicates.add(cb.isFalse(userOrgJoin.get("isDeleted")));

        query.distinct(true);



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
                try {
                    String field = filter.getField();
                    Path<?> path = root.get(field);

                    switch (filter.getOperator()) {
                        case EQUALS:
                            predicates.add(cb.equal(path, filter.getValue()));
                            break;
                        case CONTAINS:
                            if (path.getJavaType() == String.class) {
                                predicates.add(cb.like(cb.lower(path.as(String.class)), "%" + ((String) filter.getValue()).toLowerCase() + "%"));
                            }
                            break;
                        case GREATER_THAN:
                            predicates.add(cb.greaterThan(path.as(Comparable.class), (Comparable) filter.getValue()));
                            break;
                        case LESS_THAN:
                            predicates.add(cb.lessThan(path.as(Comparable.class), (Comparable) filter.getValue()));
                            break;
                        case BETWEEN:
                            List<?> valueList = (List<?>) filter.getValue();
                            if (path.getJavaType() == LocalDateTime.class) {
                                LocalDateTime start = MyUtils.parseFlexibleDate(valueList.get(0).toString());
                                LocalDateTime end = MyUtils.parseFlexibleDate(valueList.get(1).toString());
                                predicates.add(cb.between(path.as(LocalDateTime.class), start, end));
                            } else if (valueList.get(0) instanceof Number && valueList.get(1) instanceof Number) {
                                predicates.add(cb.between(path.as(Double.class), ((Number) valueList.get(0)).doubleValue(), ((Number) valueList.get(1)).doubleValue()));
                            }
                            break;
                        case IN:
                            if (filter.getValue() instanceof List<?> list && !list.isEmpty()) {
                                predicates.add(path.in(list));
                            }
                            break;
                    }
                } catch (IllegalArgumentException e) {
                    throw new FilterDataException("Invalid filter field: " + filter.getField());
                }
            }
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}