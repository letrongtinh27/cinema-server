package com.edu.hcmuaf.springserver.controller;

import com.edu.hcmuaf.springserver.dto.response.SeatResponse;
import com.edu.hcmuaf.springserver.service.ReservationService;
import com.edu.hcmuaf.springserver.service.SeatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/seats")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SeatController {
    @Autowired
    private SeatService seatService;
    @Autowired
    private ReservationService reservationService;

    private static final Logger logger = LoggerFactory.getLogger(SeatController.class);

    @GetMapping("/get/{showTimeId}/{theatreId}/{room}")
    public ResponseEntity<List<SeatResponse>> getSeatsByShowTime(@PathVariable("showTimeId") int showTimeId, @PathVariable("theatreId") int theatreId, @PathVariable("room") int room) {
        try {
            List<SeatResponse> seats = seatService.getSeatsByShowTime(showTimeId, theatreId, room);

            Map<Integer, SeatResponse> idMap = new HashMap<>();

            Date now = new Date();

            for (SeatResponse seat : seats) {
                if(seat.getStatus()==1) {
                    seat.setBooked(true);
                }
                if (idMap.containsKey(seat.getId()) && seat.getPayment() != null) {
                    idMap.put(seat.getId(), seat);
                }
                else if (!idMap.containsKey(seat.getId())) {
                    idMap.put(seat.getId(), seat);
                }
                if(seat.getReservationId() != 0 && seat.getPayment() != null) {
                    if (seat.getPayment().equals("Đang thanh toán") && (seat.getExpired_time().before(now))) {
                        reservationService.updateReservationPaymentFailure(seat.getReservationId());
                        logger.info("Updated reservation {} due to payment failure.", seat.getReservationId());
                    }
                }
            }
            List<SeatResponse> result = new ArrayList<>(idMap.values());
            result.sort(new SeatResponse());

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            logger.error("Error fetching seats for showTimeId {}, theatreId {}, room {}", showTimeId, theatreId, room, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
