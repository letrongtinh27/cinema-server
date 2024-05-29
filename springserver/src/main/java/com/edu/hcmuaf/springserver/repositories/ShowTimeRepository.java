package com.edu.hcmuaf.springserver.repositories;

import com.edu.hcmuaf.springserver.entity.ShowTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ShowTimeRepository extends JpaRepository<ShowTime, Long> {

    Optional<ShowTime> findShowTimesById(int id);
    Optional<List<ShowTime>> findShowTimeByMovieIdAndTheatreId(int movieId, int theatreId);

}
