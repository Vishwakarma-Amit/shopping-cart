package com.dreamshops.repository;

import com.dreamshops.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Category findByName(String name);

    boolean existsByName(String name);
}
