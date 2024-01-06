package com.challenger.fridge.repository;


import com.challenger.fridge.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByCategoryName(String categoryName);

    Category findByCategoryNameAndParentCategory(String categoryName,Category category);
    Boolean existsByCategoryName(String categoryName);
}
