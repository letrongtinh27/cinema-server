package com.edu.hcmuaf.springserver.service;

import com.edu.hcmuaf.springserver.entity.Movie;
import com.edu.hcmuaf.springserver.entity.ShowTime;

import com.edu.hcmuaf.springserver.entity.Theatre;
import com.edu.hcmuaf.springserver.repositories.ShowTimeRepository;
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
import java.util.logging.SimpleFormatter;

@Service
public class ShowTimeService {
    @Autowired
    private ShowTimeRepository showTimeRepository;
  
    public List<ShowTime> getShowTimesByMovieIdAndTheatreId(int movieId, int theatreId) {
        return showTimeRepository.findShowTimeByMovieIdAndTheatreId(movieId, theatreId).orElse(null);
    }
  
    public List<ShowTime> getAllShowTime() {
        return showTimeRepository.findAll();
    }

    public ShowTime getShowTimeById(int id) {
        return showTimeRepository.findById((long) id).orElse(null);
    }

    public ShowTime updateShowTime(int id,ShowTime showTime) {
        ShowTime existShowTime = showTimeRepository.findById((long) id).orElseThrow(() -> new IllegalArgumentException("not found"));
        return getShowTime(showTime, existShowTime);
    }

    private ShowTime getShowTime(ShowTime showTime, ShowTime existShowTime) {
        existShowTime.setMovie(showTime.getMovie());
        existShowTime.setRoom(showTime.getRoom());
        existShowTime.setTheatre(showTime.getTheatre());
        existShowTime.setStart_time(showTime.getStart_time());
        existShowTime.setEnd_time(showTime.getEnd_time());
        existShowTime.setStatus(showTime.getStatus());
        return showTimeRepository.save(existShowTime);
    }

    public ShowTime createShowTime(ShowTime showTime) {
        return getShowTime(showTime, showTime);
    }

    public void deleteShowTime(long id) {
        showTimeRepository.deleteById(id);
    }


    public Page<ShowTime> getAllwithSort(String filter, int page, int perPage, String sortBy, String order) {
        Sort.Direction direction = Sort.Direction.ASC;
        if (order.equalsIgnoreCase("DESC"))
            direction = Sort.Direction.DESC;

        JsonNode filterJson;
        try {
            filterJson = new ObjectMapper().readTree(java.net.URLDecoder.decode(filter, StandardCharsets.UTF_8));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Specification<ShowTime> specification = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (filterJson.has("q")) {
                Join<ShowTime, Movie> movieJoin = root.join("movie", JoinType.INNER);
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(criteriaBuilder.lower(movieJoin.get("title")), "%" + filterJson.get("q").asText().toLowerCase() + "%"));
            }
            return predicate;
        };
        return showTimeRepository.findAll(specification, PageRequest.of(page, perPage, Sort.by(direction, sortBy)));
    }

    public List<ShowTime> getAllwithSortTime(String filter) {
        JsonNode filterJson;
        try {
            filterJson = new ObjectMapper().readTree(java.net.URLDecoder.decode(filter, StandardCharsets.UTF_8));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Specification<ShowTime> specification = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (filterJson.has("start_time")) {
                String[] parts = filterJson.get("start_time").asText().split("/");
                if (parts.length == 2) {
                    int month = Integer.parseInt(parts[0]);
                    int year = Integer.parseInt(parts[1]);
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(criteriaBuilder.function("MONTH", Integer.class, root.get("start_time")), month));
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(criteriaBuilder.function("YEAR", Integer.class, root.get("start_time")), year));
                }
            }
            return predicate;
        };
        return showTimeRepository.findAll(specification);
    }

}