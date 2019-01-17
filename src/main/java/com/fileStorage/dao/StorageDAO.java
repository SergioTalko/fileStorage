package com.fileStorage.dao;

import com.fileStorage.model.File;
import com.fileStorage.model.Storage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface StorageDAO extends JpaRepository<Storage,Long> {


}
