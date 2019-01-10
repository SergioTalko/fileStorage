package com.fileStorage.dao;

import com.fileStorage.model.File;
import org.hibernate.SessionFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface FileDAO extends JpaRepository<File,Long> {


}
