package com.insurance.insurance.entity;


import com.insurance.insurance.dto.AutoRenewDTO;
import com.insurance.insurance.dto.FireRenewDTO;
import com.insurance.insurance.dto.HealthRenewDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class RenewableInsurance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private SiteUser siteUser;

    @OneToOne
    private Insurance insurance;

    private LocalDate createDate;
}
