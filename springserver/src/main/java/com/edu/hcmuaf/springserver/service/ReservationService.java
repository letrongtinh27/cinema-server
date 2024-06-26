package com.edu.hcmuaf.springserver.service;

import com.edu.hcmuaf.springserver.entity.Reservation;
import com.edu.hcmuaf.springserver.entity.Seat;
import com.edu.hcmuaf.springserver.entity.ShowTime;
import com.edu.hcmuaf.springserver.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public void createReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }



    public void updateReservationPaymentFailure(int id) {
        Reservation reservation = reservationRepository.findById((long) id).orElse(null);


        if(reservation != null) {
            reservation.setPayment("Thanh toán thất bại");

            reservationRepository.save(reservation);
        }
    }
    public boolean checkExistReservation(String codeOrder) {
       return reservationRepository.existsReservationByOrder(codeOrder);
    }
    public List<Reservation> findReservationsByOrder(String codeOrder) {
        return reservationRepository.findReservationsByOrder(codeOrder).orElse(null);
    }
    public void editReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }
    public boolean checkBookingTicket(ShowTime showtime, Seat seat) {
        Reservation reservation = reservationRepository.findReservationByShowTimeAndSeat(showtime, seat).orElse(null);
        if(reservation == null) {
            return false;
        }
        Date now = new Date();
//        if expired time < now
        if(reservation.getExpired_time().before(now)) {
            reservation.setPayment("Thanh toán thất bại");
            reservationRepository.save(reservation);
            return false;
        }
        return true;
    }
}
