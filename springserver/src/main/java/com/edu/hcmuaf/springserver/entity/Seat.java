package com.edu.hcmuaf.springserver.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Long theatre_id;
    private char row_char;
    private int seat_number;
    private String room;
    private int price;
    private int status;


}
