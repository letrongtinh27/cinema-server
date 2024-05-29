package com.edu.hcmuaf.springserver.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int user_id;
    private int show_time_id;
    private int seat_id;
    @Column(name = "code_order")
    private String order;
    private String phone_number;
    private String email;
    private int original_price;
    private int total_price;
    private Date reservation_time;
    private Date expired_time;
    private String payment;
}
