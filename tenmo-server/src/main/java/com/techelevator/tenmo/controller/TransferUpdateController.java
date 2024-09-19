package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.service.TransferUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("transfer")
public class TransferUpdateController {
    private final TransferUpdateService transferUpdateService;

    @Autowired
    public TransferUpdateController(TransferUpdateService transferUpdateService) {
        this.transferUpdateService = transferUpdateService;
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping(path = "/Approved")
    public Integer approveTransferRequest(@RequestBody TransferDto transfer) {
        return transferUpdateService.approveTransfer(transfer);
    }

    @PutMapping(path = "/Rejected")
    public Integer rejectTransferRequest(@RequestBody TransferDto transfer) {
        return transferUpdateService.rejectTransfer(transfer);
    }
}
