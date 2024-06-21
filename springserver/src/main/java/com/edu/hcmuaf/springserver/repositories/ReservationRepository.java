package com.edu.hcmuaf.springserver.repositories;

import com.edu.hcmuaf.springserver.entity.Reservation;
import com.edu.hcmuaf.springserver.entity.Seat;
import com.edu.hcmuaf.springserver.entity.ShowTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    boolean existsReservationByOrder(String orderId);
    Optional<List<Reservation>> findReservationsByOrder(String codeOrder);
    Optional<Reservation> findReservationByShowTimeAndSeat(ShowTime showTime, Seat seat);

}

