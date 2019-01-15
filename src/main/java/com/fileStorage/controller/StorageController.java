package com.fileStorage.controller;

import com.fileStorage.exception.FileNotMuchException;
import com.fileStorage.model.Storage;
import com.fileStorage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("storage")
public class StorageController {

    private final StorageService storageService;

    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping
    @Transactional
    public Storage saveStorage(@RequestBody Storage storage) {
        return storageService.saveStorage(storage);
    }

    @GetMapping
    public List<Storage> list() {
        return storageService.findAllFiles();
    }

    @GetMapping("{id}")
    public Storage getOne(@PathVariable("id") Storage storage) {
        return storage;
    }

    @DeleteMapping("{id}")
    @Transactional
    public String delete(@PathVariable("id") Storage storage) {
        storageService.delete(storage);
        return "Storage with id " + storage.getId() + " was deleted";
    }


}
