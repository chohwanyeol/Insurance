package com.insurance.insurance.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AutoRequestDTO {
    private LocalDateTime dateTime;
    private String vehicle_number;
    private String content;
    private int price;
}
