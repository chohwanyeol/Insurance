package com.insurance.insurance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "User_Info")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private LocalDate birthDay;

    @Column(unique = true)
    private String email;

    private String location;

    private String detailLocation;

    private LocalDate create_date;

    private LocalDate update_date;

}
