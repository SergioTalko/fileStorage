package com.fileStorage.controller;

import com.fileStorage.exception.NotEnoughSpaceException;
import com.fileStorage.exception.NotFormatSupported;
import com.fileStorage.model.Storage;
import com.fileStorage.service.StorageService;
import org.springframework.beans.BeanUtils;
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
    public String delete(@PathVariable("id") Storage storage) {
        storageService.delete(storage);
        return "Storage with id " + storage.getId() + " was deleted";
    }

    @PutMapping("{id}")
    public Storage updateStorage(
            @PathVariable("id") Storage storageFromDb,
            @RequestBody Storage storage
    ) {
        BeanUtils.copyProperties(storage, storageFromDb, "id", "storageSize");
        return storageService.updateStorage(storageFromDb);
    }

    @PutMapping("/transferAll/{id}")
    public String transferAll(
            @PathVariable("id") Storage storageFrom,
            @RequestBody Storage storageTo
    ) throws NotEnoughSpaceException, NotFormatSupported {
       return storageService.transferAll(storageFrom,storageTo);

    }


}
