package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.service.TransferTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("type")
public class TransferTypeController {

    private final TransferTypeService transferTypeService;

    @Autowired
    public TransferTypeController(TransferTypeService transferTypeService) {
        this.transferTypeService = transferTypeService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/{type_id}")
    public String getTransferTypeDesc(@PathVariable @Valid int type_id){
        return transferTypeService.getDescById(type_id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/desc/{description}")
    public Integer getTransferTypeId(@PathVariable @Valid String description){
        return transferTypeService.getIdByDesc(description);
    }
}
