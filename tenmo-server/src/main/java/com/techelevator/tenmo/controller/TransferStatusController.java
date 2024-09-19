package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.service.TransferStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("status")
public class TransferStatusController {

    private final TransferStatusService transferStatusService;

    @Autowired
    public TransferStatusController(TransferStatusService transferStatusService) {
        this.transferStatusService = transferStatusService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/{status_id}")
    public String getStatusById(@PathVariable @Valid int status_id) {
        return transferStatusService.getDescByID(status_id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/desc/{status_id}")
    public String getIdByDesc(@PathVariable @Valid int status_id) {
        return transferStatusService.getDescByID(status_id);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping(path = "transfer/{transfer_id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Integer updateStatus(@PathVariable @Valid int transfer_Id,
                                @RequestBody @Valid int transferStatusId) {
        return transferStatusService.updateTransferStatus(transfer_Id, transferStatusId);
    }

}
