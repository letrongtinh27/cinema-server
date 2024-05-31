package com.edu.hcmuaf.springserver.service;

import com.edu.hcmuaf.springserver.entity.Ticket;
import com.edu.hcmuaf.springserver.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    public List<Ticket> getAllTicket() {
        return ticketRepository.findAll();
    }

    public Ticket getTicketById(int id) {
        return ticketRepository.findOneById(id);
    }

    public boolean checkExistTicket(int showTimeId, int seatId) {
        return ticketRepository.existsByShowTimeIdAndSeatId((long) showTimeId, seatId);
    }
    public void saveTicket(Ticket ticket) {
        ticketRepository.save(ticket);
    }
    public Ticket findTicketByTicketCode(String ticketCode) {
        return ticketRepository.findTicketByTicketCode(ticketCode).orElse(null);
    }

    public List<Ticket> findTicketsByUserId(int userId) {
        return ticketRepository.findTicketsByUserId((long) userId);
    }

    public void deleteTicket(long id) {ticketRepository.deleteById(id);}
}