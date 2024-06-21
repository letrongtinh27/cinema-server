package com.edu.hcmuaf.springserver.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "show_times")
public class ShowTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "movie_id", referencedColumnName = "id")
    private Movie movie;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "theatre_id", referencedColumnName = "id")
    private Theatre theatre;
    private int room;
    private LocalDateTime start_time;
    private LocalDateTime end_time;
    private int status;
}
