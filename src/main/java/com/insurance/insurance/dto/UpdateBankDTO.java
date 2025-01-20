package com.insurance.insurance.dto;

import com.insurance.insurance.entity.Transaction;
import com.insurance.insurance.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UpdateBankDTO {
    private String bank;
    private String account;
}
