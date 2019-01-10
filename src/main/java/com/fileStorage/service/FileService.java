package com.fileStorage.service;

import com.fileStorage.dao.FileDAO;
import com.fileStorage.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {


    private final FileDAO fileDAO;

    @Autowired
    public FileService(FileDAO fileDAO) {
        this.fileDAO = fileDAO;
    }



    public List<File> findAllFiles() {
        return fileDAO.findAll();
    }

    public void saveFile(File file){
        fileDAO.save(file);
    }



    private boolean isValidName(String name) {
        if (name != null && name.length() <= 10) {
            return true;
        }
        return false;
    }


}
