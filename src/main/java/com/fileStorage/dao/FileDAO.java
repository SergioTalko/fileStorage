package com.fileStorage.dao;

import com.fileStorage.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface FileDAO extends JpaRepository<File,Long> {

    List<File> findAllByStorageId(long storageId);


}
