package com.edu.hcmuaf.springserver.service;

import com.edu.hcmuaf.springserver.entity.*;
import com.edu.hcmuaf.springserver.repositories.TicketRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
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

    public Ticket createTicket(Ticket ticket){
        return ticketRepository.save(ticket);
    }

    public Ticket updateTicket(Ticket ticket, int id) {
        Ticket ticketSave = ticketRepository.findOneById(id);
        ticketSave.setShowTime(ticket.getShowTime());
        ticketSave.setSeat(ticket.getSeat());
        ticketSave.setReservation(ticket.getReservation());
        ticketSave.setTicketCode(ticket.getTicketCode());
        ticketSave.setPrice(ticket.getPrice());
        ticketRepository.save(ticketSave);
        return ticketSave;
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

    public void deleteTicket(long id) {
        ticketRepository.deleteById(id);
    }


    public Page<Ticket> getAllwithSort(String filter, int page, int perPage, String sortBy, String order) {
        Sort.Direction direction = Sort.Direction.ASC;
        if (order.equalsIgnoreCase("DESC"))
            direction = Sort.Direction.DESC;

        JsonNode filterJson;
        try {
            filterJson = new ObjectMapper().readTree(java.net.URLDecoder.decode(filter, StandardCharsets.UTF_8));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Specification<Ticket> specification = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (filterJson.has("q")) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("ticketCode"), "%" + filterJson.get("q").asText().toLowerCase() + "%"));
            }
            if (filterJson.has("movie")) {
                Join<Ticket, ShowTime> showTimeJoin = root.join("showTime", JoinType.INNER);
                Join<ShowTime, Movie> movieJoin = showTimeJoin.join("movie", JoinType.INNER);
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(movieJoin.get("title"), "%" + filterJson.get("movie").asText() + "%"));
            }
            if (filterJson.has("theatre")) {
                Join<Ticket, ShowTime> showTimeJoin = root.join("showTime", JoinType.INNER);
                Join<ShowTime, Theatre> theatreJoin = showTimeJoin.join("theatre", JoinType.INNER);
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(theatreJoin.get("name"), "%" + filterJson.get("theatre").asText() + "%"));
            }
            if (filterJson.has("reservation_time")) {
                String[] parts = filterJson.get("reservation_time").asText().split("/");
                if (parts.length == 2) {
                    int month = Integer.parseInt(parts[0]);
                    int year = Integer.parseInt(parts[1]);
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(criteriaBuilder.function("MONTH", Integer.class, root.get("reservation").get("reservation_time")), month));
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(criteriaBuilder.function("YEAR", Integer.class, root.get("reservation").get("reservation_time")), year));
                }
            }
            return predicate;
        };
        return ticketRepository.findAll(specification, PageRequest.of(page, perPage, Sort.by(direction, sortBy)));
    }

    public List<Ticket> getAllWithoutPagination(String filter) {
        JsonNode filterJson;
        try {
            filterJson = new ObjectMapper().readTree(java.net.URLDecoder.decode(filter, StandardCharsets.UTF_8));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Specification<Ticket> specification = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (filterJson.has("movie")) {
                Join<Ticket, ShowTime> showTimeJoin = root.join("showTime", JoinType.INNER);
                Join<ShowTime, Movie> movieJoin = showTimeJoin.join("movie", JoinType.INNER);
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(criteriaBuilder.lower(movieJoin.get("title")), "%" + filterJson.get("movie").asText().toLowerCase() + "%"));
            }

            if (filterJson.has("theatre")) {
                Join<Ticket, ShowTime> showTimeJoin = root.join("showTime", JoinType.INNER);
                Join<ShowTime, Theatre> theatreJoin = showTimeJoin.join("theatre", JoinType.INNER);
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(theatreJoin.get("name"), "%" + filterJson.get("theatre").asText() + "%"));
            }

            if (filterJson.has("reservation_time")) {
                String[] parts = filterJson.get("reservation_time").asText().split("/");
                if (parts.length == 2) {
                    int month = Integer.parseInt(parts[0]);
                    int year = Integer.parseInt(parts[1]);
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(criteriaBuilder.function("MONTH", Integer.class, root.get("reservation").get("reservation_time")), month));
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(criteriaBuilder.function("YEAR", Integer.class, root.get("reservation").get("reservation_time")), year));
                }
            }
            return predicate;
        };

        return ticketRepository.findAll(specification);
    }
}