package com.fileStorage.dao;

import com.fileStorage.model.Storage;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StorageDAO extends JpaRepository<Storage,Long> {


}
