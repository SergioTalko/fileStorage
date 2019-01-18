package com.fileStorage.service;

import com.fileStorage.dao.FileDAO;
import com.fileStorage.dao.StorageDAO;
import com.fileStorage.exception.FileNotMuchException;
import com.fileStorage.exception.NotEnoughSpaceException;
import com.fileStorage.exception.NotFormatSupported;
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
    public FileService(FileDAO fileDAO, StorageDAO storageDAO) {
        this.fileDAO = fileDAO;
        this.storageDAO = storageDAO;
    }


    public List<File> findAllFiles() {
        return fileDAO.findAll();
    }


    @Transactional
    public File saveFile(File file) throws FileNotMuchException, NotEnoughSpaceException, NotFormatSupported {

        if (isValidName(file.getName())) {
            throw new FileNotMuchException("File name is empty or more than 10 characters");
        }

        if (!isValidFormatStorages(file, storageDAO.getOne(file.getStorage().getId()))){
            throw new NotFormatSupported("This format of file not supported in this storage");
        }

        Storage storage = storageDAO.getOne(file.getStorage().getId());

        if (storage.getStorageSize() >= file.getSize()) {
            storage.setStorageSize((storage.getStorageSize() - file.getSize()));
            storageDAO.saveAndFlush(storage);
        } else {
            throw new NotEnoughSpaceException("Storage with id " + file.getStorage().getId() + " doesnt have enough free space for file with name " + file.getName());
        }

        return fileDAO.save(file);

    }

    @Transactional
    public void deleteFile(File file) {
        fileDAO.delete(file);
    }

    @Transactional
    public File updateFile(File fileFromDb, File file) throws NotEnoughSpaceException, FileNotMuchException, NotFormatSupported {
        if (isValidName(file.getName())) {
            throw new FileNotMuchException("File name is empty or more than 10 characters");
        }

        if (!isValidFormatStorages(fileFromDb, storageDAO.getOne(file.getStorage().getId()))){
            throw new NotFormatSupported("This format of file not supported in this storage");
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

    @Transactional
    public String transferFile(File fileFromDb, Storage storageTo) throws NotEnoughSpaceException, NotFormatSupported {

        Storage storageForTransfer = storageDAO.getOne(storageTo.getId());
        Storage currentFileStorage = fileFromDb.getStorage();

        if (!isValidFormatStorages(fileFromDb, storageDAO.getOne(storageTo.getId()))){
            throw new NotFormatSupported("This format of file not supported in this storage");
        }

        if (storageForTransfer.getStorageSize() >= fileFromDb.getSize()) {
            fileFromDb.setStorage(storageForTransfer);
            currentFileStorage.setStorageSize(currentFileStorage.getStorageSize() + fileFromDb.getSize());
            storageForTransfer.setStorageSize(storageForTransfer.getStorageSize() - fileFromDb.getSize());

        }else {
            throw new NotEnoughSpaceException("Storage with id " + storageTo.getId() + " doesnt have enough space for file with id " + fileFromDb.getId());
        }

        storageDAO.save(storageForTransfer);
        storageDAO.save(currentFileStorage);
        fileDAO.save(fileFromDb);



        return "File with id " + fileFromDb.getId() + " was successfully transferred to storage with id " + storageTo.getId();
    }


    private boolean isValidName(String name) {
        if (name != null && name.length() > 10) {
            return true;
        }
        return false;
    }

    private boolean isValidFormatStorages(File file, Storage storage){

        String[] formats = storage.getFormatsSupported().split(",");

        for (String format: formats
             ) {
            if (format.equals(file.getFormat())){
                return true;
            }
        }



        return false;
    }
}
