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
@Table(name = "show_time")
public class ShowTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinTable(name = "movies",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "id")
    )
    private Movie movie_st;

    @OneToOne
    @JoinTable(name = "theatres",
            joinColumns = @JoinColumn(name = "theatre_id"),
            inverseJoinColumns = @JoinColumn(name = "id"))
    private Theatre theatre_name;
    @Column(name = "movie_id")
    private int movieId;
    @Column(name = "theatre_id")
    private int theatreId;
    private int room;
    private LocalDateTime start_time;
    private LocalDateTime end_time;
    private int status;
}
