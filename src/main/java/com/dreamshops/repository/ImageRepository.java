package com.dreamshops.repository;

import com.dreamshops.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    Image findByDownloadUrl(String downloadUrl);
}
