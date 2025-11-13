package com.dailybrief.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dailybrief.model.Image;

public interface ImageRepository extends JpaRepository<Image, UUID> {

}
