package com.jashawn.ap_tracker.vendor;

import org.springframework.data.jpa.domain.Specification;

public class VendorSpecifications {

    public static Specification<Vendor> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? null :
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%");
    }

    public static Specification<Vendor> isActive(Boolean active) {
        return (root, query, criteriaBuilder) -> {
            if (active == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(root.get("active"), active);
        };
    }
}
