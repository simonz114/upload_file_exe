package com.exercise.fileupload.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("meta_info")
public interface MetaInfoRepository extends JpaRepository<FileDto, UUID> {
}
