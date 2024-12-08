package com.insurance.insurance.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FireRequestDTO {
    private LocalDateTime dateTime;
    private String content;
    private String price;

}
