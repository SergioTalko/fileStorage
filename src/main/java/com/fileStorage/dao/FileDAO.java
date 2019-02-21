package com.fileStorage.dao;

import com.fileStorage.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RepositoryRestResource
@CrossOrigin(origins = "http://localhost:4200")
public interface FileDAO extends JpaRepository<File,Long> {

    List<File> findAllByStorageId(long storageId);

}
