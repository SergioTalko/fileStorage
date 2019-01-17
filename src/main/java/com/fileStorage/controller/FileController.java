package com.fileStorage.controller;

import com.fileStorage.exception.FileNotMuchException;
import com.fileStorage.exception.NotEnoughSpaceException;
import com.fileStorage.model.File;
import com.fileStorage.model.Storage;
import com.fileStorage.service.FileService;
import com.fileStorage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("file")
public class FileController {

    private final FileService fileService;


    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping
    public List<File> list() {
        return fileService.findAllFiles();
    }

    @PostMapping
    public File saveFile(@RequestBody File file) throws FileNotMuchException, NotEnoughSpaceException {
        return fileService.saveFile(file);
    }

    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") File file) {
        fileService.deleteFile(file);
        return "File with id " + file.getId() + " was deleted";
    }

    @GetMapping("{id}")
    public File getOne(@PathVariable("id") File file) {
        return file;
    }


    @PutMapping("{id}")
    public File update(
            @PathVariable("id") File fileFromDb,
            @RequestBody File file
    ) throws NotEnoughSpaceException, FileNotMuchException {

        return fileService.updateFile(fileFromDb,file);
    }

    @PutMapping("fileTransfer/{id}")
    public String transferFile(
            @PathVariable("id") File fileFromDb,
            @RequestBody Storage storageTo
            ) throws NotEnoughSpaceException {

        return fileService.transferFile(fileFromDb,storageTo);
    }


}
