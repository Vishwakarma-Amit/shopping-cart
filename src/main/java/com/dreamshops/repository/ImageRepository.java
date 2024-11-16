package com.dreamshops.repository;

import com.dreamshops.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    Image findByDownloadUrl(String downloadUrl);

    List<Image> findByProductProductId(int ProductId);
}
