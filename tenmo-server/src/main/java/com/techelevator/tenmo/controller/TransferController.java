package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("transfer")
public class TransferController {

    private final TransferService transferService;

    @Autowired
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/{transfer_id}")
    public TransferDto getTransferById(@PathVariable int transfer_id) {
        return transferService.getTransferById(transfer_id);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public Integer createNewTransfer(@RequestBody TransferDto transferDto) {
        return transferService.createNewTransfer(transferDto);
    }
}
