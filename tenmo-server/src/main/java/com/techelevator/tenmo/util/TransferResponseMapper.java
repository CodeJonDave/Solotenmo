package com.techelevator.tenmo.util;

import com.techelevator.tenmo.model.TransferDto;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class TransferResponseMapper {

    public static TransferDto mapRowToResponse(SqlRowSet rs) {
        TransferDto transferResponseDto = new TransferDto();
        transferResponseDto.setTransferId(rs.getInt("transfer_id"));
        transferResponseDto.setTransferType(rs.getString("transfer_type"));
        transferResponseDto.setTransferStatus(rs.getString("transfer_status"));
        transferResponseDto.setAccount_from(rs.getString("account_from_username"));
        transferResponseDto.setAccount_to(rs.getString("account_to_username"));
        transferResponseDto.setAmount(rs.getBigDecimal("amount"));
        return transferResponseDto;
    }
}
