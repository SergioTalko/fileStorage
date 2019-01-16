package com.fileStorage.service;

import com.fileStorage.dao.FileDAO;
import com.fileStorage.dao.StorageDAO;
import com.fileStorage.exception.FileNotMuchException;
import com.fileStorage.exception.NotEnoughSpaceException;
import com.fileStorage.model.File;
import com.fileStorage.model.Storage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FileService {


    private final FileDAO fileDAO;

    private final StorageDAO storageDAO;

    @Autowired
    public FileService(FileDAO fileDAO, StorageService storageService, StorageDAO storageDAO) {
        this.fileDAO = fileDAO;
        this.storageDAO = storageDAO;
    }


    public List<File> findAllFiles() {
        return fileDAO.findAll();
    }


    @Transactional
    public File saveFile(File file) throws FileNotMuchException, NotEnoughSpaceException {

        if (isValidName(file.getName())) {
            throw new FileNotMuchException("File name is empty or more than 10 characters");
        }

        Storage storage = storageDAO.getOne(file.getStorage().getId());

        if (storage.getStorageSize() >= file.getSize()) {
            storage.setStorageSize((storage.getStorageSize() - file.getSize()));
            storageDAO.saveAndFlush(storage);
        } else {
            throw new NotEnoughSpaceException("Storage with id " + file.getStorage().getId() + " doesnt have enough free space for file with id " + file.getId());
        }

        return fileDAO.save(file);

    }

    @Transactional
    public void deleteFile(File file) {
        fileDAO.delete(file);
    }

    @Transactional
    public File updateFile(File fileFromDb, File file) throws NotEnoughSpaceException, FileNotMuchException {
        if (isValidName(file.getName())) {
            throw new FileNotMuchException("File name is empty or more than 10 characters");
        }

        Storage storage = storageDAO.getOne(file.getStorage().getId());

        long differenceIfSizeWasChanged = fileFromDb.getSize() - file.getSize();
        storage.setStorageSize(storage.getStorageSize() + differenceIfSizeWasChanged);

        if (storage.getStorageSize() >= 0) {
            storageDAO.saveAndFlush(storage);
        } else {
            throw new NotEnoughSpaceException("Storage with id " + file.getStorage().getId() + " doesnt have enough free space for file with id " + file.getId());
        }

        BeanUtils.copyProperties(file, fileFromDb, "id");
        return fileDAO.save(fileFromDb);


    }


    private boolean isValidName(String name) {
        if (name != null && name.length() > 10) {
            return true;
        }
        return false;
    }
}
