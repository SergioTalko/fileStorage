package com.fileStorage.service;

import com.fileStorage.dao.FileDAO;
import com.fileStorage.dao.StorageDAO;
import com.fileStorage.exception.NotEnoughSpaceException;
import com.fileStorage.model.File;
import com.fileStorage.model.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StorageService {


    private final StorageDAO storageDAO;

    private final FileDAO fileDAO;

    @Autowired
    public StorageService(StorageDAO storageDAO, FileDAO fileDAO) {
        this.storageDAO = storageDAO;
        this.fileDAO = fileDAO;
    }

    @Transactional
    public Storage saveStorage(Storage storage) {
        return storageDAO.save(storage);
    }

    public List<Storage> findAllFiles() {
        return storageDAO.findAll();
    }

    @Transactional
    public void delete(Storage storage) {
        storageDAO.delete(storage);
    }

    @Transactional
    public Storage updateStorage(Storage storage) {
        return storageDAO.save(storage);
    }

    @Transactional
    public String transferAll(Storage storageFrom, Storage storageTo) throws NotEnoughSpaceException {

        Storage getStorageTo = storageDAO.getOne(storageTo.getId());
        List<File> listFileFrom = fileDAO.findAllByStorageId(storageFrom.getId());
        long sizeAllFiles = listFileFrom.stream().mapToLong(File::getSize).sum();


        if (getStorageTo.getStorageSize() >= sizeAllFiles){


            listFileFrom.forEach(i -> i.setStorage(getStorageTo));
            storageFrom.setStorageSize(storageFrom.getStorageSize() + sizeAllFiles);
            getStorageTo.setStorageSize(getStorageTo.getStorageSize() - sizeAllFiles);

        }else {
            throw new NotEnoughSpaceException("Storage with id " + storageTo.getId() + "doesnt have enough space for files from storage with id " + storageFrom.getId());
        }

        storageDAO.save(storageFrom);
        storageDAO.save(getStorageTo);

        return "All files from storage with id " + storageFrom.getId() + "was transferred to storage with id " + storageTo.getId();
    }
}