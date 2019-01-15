package com.fileStorage.service;

import com.fileStorage.dao.StorageDAO;
import com.fileStorage.model.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StorageService {


    private final StorageDAO storageDAO;

    @Autowired
    public StorageService(StorageDAO storageDAO) {
        this.storageDAO = storageDAO;
    }

    public Storage saveStorage(Storage storage) {
        return storageDAO.save(storage);
    }

    public List<Storage> findAllFiles() {
        return storageDAO.findAll();
    }

    public void delete(Storage storage) {
        storageDAO.delete(storage);
    }

}