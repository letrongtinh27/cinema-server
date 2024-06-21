package com.edu.hcmuaf.springserver.controller;

import com.edu.hcmuaf.springserver.dto.response.ShowsResponse;
import com.edu.hcmuaf.springserver.entity.ShowTime;
import com.edu.hcmuaf.springserver.entity.User;
import com.edu.hcmuaf.springserver.service.ShowTimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/shows")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ShowTimeController {
    @Autowired
    private ShowTimeService showTimeService;
    private static final Logger logger = LoggerFactory.getLogger(ShowTimeController.class);

    @GetMapping("/all")
    public ResponseEntity<List<ShowTime>> getAllShowWithFilter(@RequestParam(defaultValue = "{}") String filter) {
        List<ShowTime> showTimes = showTimeService.getAllwithSortTime(filter);
        return ResponseEntity.ok(showTimes);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getShowsByMovieIdAndTheatreId(@RequestParam int movieId, @RequestParam int theatreId, @RequestParam String date) {
        List<ShowsResponse> responses = new ArrayList<>();

        List<ShowTime> showTimeList = showTimeService.getShowTimesByMovieIdAndTheatreId(movieId, theatreId);
        showTimeList.removeIf(showTime -> showTime.getStatus()==1);

        if(!showTimeList.isEmpty()){
            for (ShowTime shows : showTimeList) {
                ShowsResponse s = new ShowsResponse();

                LocalDate dt = shows.getStart_time().toLocalDate();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M");
                s.setDate(dt.format(formatter));

                LocalDateTime now = LocalDateTime.now();

                if(s.getDate().equals(date)) {
                    s.setId(shows.getId());
                    s.setMovieId(Math.toIntExact(shows.getMovie().getId()));
                    s.setRoom(shows.getRoom());
                    s.setTheatreId(Math.toIntExact(shows.getTheatre().getId()));
                    s.setStart_time(shows.getStart_time().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                    s.setStatus(shows.getStatus());

                    if(now.isBefore(shows.getStart_time())) {
                        responses.add(s);
                    }
                }
            }
            return ResponseEntity.ok(responses);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getShowTimeById(@PathVariable int id) {
        try {
            ShowTime showTime = showTimeService.getShowTimeById(id);
            if (showTime != null) {
                return ResponseEntity.ok(showTime);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error retrieving show time with id {}", id, e);
            return ResponseEntity.badRequest().body("Error retrieving show time");
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShowTime(@PathVariable long id) {
        try {
            showTimeService.deleteShowTime(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting show time with id {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createShowTime(@RequestBody ShowTime showTime) {
        try {
            return ResponseEntity.ok(showTimeService.createShowTime(showTime));
        } catch (Exception e) {
            logger.error("Error creating show time", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating show time");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateShowTime(@RequestBody ShowTime showTime, @PathVariable int id) {
        try {
            return ResponseEntity.ok(showTimeService.updateShowTime(id, showTime));
        } catch (Exception e) {
            logger.error("Error updating show time with id {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating show time");
        }
    }

    @GetMapping
    public ResponseEntity<Page<ShowTime>> getAllShowTime(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "{}") String filter,
                                                      @RequestParam(defaultValue = "16") int perPage,
                                                      @RequestParam(defaultValue = "movie.title") String sort,
                                                      @RequestParam(defaultValue = "DESC") String order) {
        try {
            Page<ShowTime> showTimes = showTimeService.getAllwithSort(filter, page, perPage, sort, order);
            return ResponseEntity.ok(showTimes);
        } catch (Exception e) {
            logger.error("Error retrieving all show times", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}