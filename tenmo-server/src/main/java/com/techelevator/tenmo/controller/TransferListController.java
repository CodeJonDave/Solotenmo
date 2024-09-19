package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.service.TransferListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("transferList")
public class TransferListController {


    private final TransferListService transferListService;

    @Autowired
    public TransferListController(TransferListService transferListService) {
        this.transferListService = transferListService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/Full")
    public List<TransferDto> getTransferHistory() {
        return transferListService.getTransferHistory();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/Pending")
    public List<TransferDto> getPendingTransfers() {
        return transferListService.getPendingTransfers();
    }
}
