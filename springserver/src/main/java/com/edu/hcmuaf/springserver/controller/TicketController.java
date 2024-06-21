package com.edu.hcmuaf.springserver.controller;

import com.edu.hcmuaf.springserver.dto.response.TicketResponse;
import com.edu.hcmuaf.springserver.entity.ShowTime;
import com.edu.hcmuaf.springserver.entity.Ticket;
import com.edu.hcmuaf.springserver.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/tickets")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    private static final Logger logger = LoggerFactory.getLogger(TicketController.class);

//    @GetMapping("/all")
//    public ResponseEntity<?> getListTicket() {
//        List<Ticket> ticketList = ticketService.getAllTicket();
//        if (ticketList != null ) {
//            return ResponseEntity.ok(ticketList);
//        }
//        return ResponseEntity.badRequest().body(null);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTicketById(@PathVariable int id) {
        try {
            Ticket ticket = ticketService.getTicketById(id);
            if (ticket != null) {
                return ResponseEntity.ok(ticket);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error retrieving ticket with id {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving ticket");
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createTicket(@RequestBody Ticket ticket) {
        try {
            Ticket createdTicket = ticketService.createTicket(ticket);
            return ResponseEntity.ok(createdTicket);
        } catch (Exception e) {
            logger.error("Error creating ticket", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating ticket");
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTicket(@RequestBody Ticket ticket, @PathVariable int id) {
        try {
            Ticket updatedTicket = ticketService.updateTicket(ticket, id);
            if (updatedTicket != null) {
                return ResponseEntity.ok(updatedTicket);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error updating ticket with id {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating ticket");
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable long id) {
        try {
            ticketService.deleteTicket(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting ticket with id {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    @GetMapping("/get/{userId}")
    public ResponseEntity<?> getTicketsByUserId(@PathVariable int userId) {
        try {
            List<Ticket> ticketList = ticketService.findTicketsByUserId(userId);

            List<TicketResponse> ticketResponseList = new ArrayList<>();
            for (Ticket ticket : ticketList) {
                TicketResponse ticketResponse = new TicketResponse();
                ticketResponse.setId(ticket.getId());
                ticketResponse.setOrderCode(ticket.getReservation().getOrder());
                ticketResponse.setReservationTime(ticket.getReservation().getReservation_time().toString());
                ticketResponse.setTicketCode(ticket.getTicketCode());
                ticketResponse.setPrice(ticket.getPrice());

                ticketResponseList.add(ticketResponse);
            }
            if (!ticketResponseList.isEmpty()) {
                return ResponseEntity.ok(ticketResponseList);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error retrieving tickets for user with id {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<Ticket>> getAll(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "{}") String filter,
                                                         @RequestParam(defaultValue = "16") int perPage,
                                                         @RequestParam(defaultValue = "showTime.movie.title") String sort,
                                                         @RequestParam(defaultValue = "DESC") String order) {
        try {
            Page<Ticket> tickets = ticketService.getAllwithSort(filter, page, perPage, sort, order);
            return ResponseEntity.ok(tickets);
        } catch (Exception e) {
            logger.error("Error retrieving all tickets", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Ticket>> getAllTicketsWithoutPagination(@RequestParam(defaultValue = "{}") String filter) {
        try {
            List<Ticket> tickets = ticketService.getAllWithoutPagination(filter);
            return ResponseEntity.ok(tickets);
        } catch (Exception e) {
            logger.error("Error retrieving all tickets without pagination", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
  

