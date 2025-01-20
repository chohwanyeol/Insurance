package com.insurance.insurance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AutoInsurance extends Insurance{

    private String vehicleNumber;    // 차량 번호
    private String vehicleModel;     // 차량 모델
    private Integer vehicleYear;     // 차량 연식
    private LocalDate drivingStartDate; // 운전 경력 (시작년)
    private Boolean recentAccident;  // 최근 사고 여부
    private String vehicleUsage;     // 차량 용도 (출퇴근, 영업용 등)
}
