package com.fileStorage.controller;

import com.fileStorage.exception.FileNotMuchException;
import com.fileStorage.exception.NotEnoughSpaceException;
import com.fileStorage.model.File;
import com.fileStorage.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public File saveFile(@RequestBody File file) throws FileNotMuchException, NotEnoughSpaceException {
        return fileService.saveFile(file);
    }

    @DeleteMapping("{id}")
    @Transactional
    public String delete(@PathVariable("id") File file) {
        fileService.delete(file);
        return "File with id " + file.getId() + " was deleted";
    }

    @GetMapping("{id}")
    public File getOne(@PathVariable("id") File file) {
        return file;
    }


}
