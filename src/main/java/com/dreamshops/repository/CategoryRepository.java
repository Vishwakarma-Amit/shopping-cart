package com.dreamshops.repository;

import com.dreamshops.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByName(String name);

    Category existsByName(String name);
}
