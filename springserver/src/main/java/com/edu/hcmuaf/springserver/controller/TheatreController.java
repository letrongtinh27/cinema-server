package com.edu.hcmuaf.springserver.controller;

import com.edu.hcmuaf.springserver.dto.request.TheatreRequest;
import com.edu.hcmuaf.springserver.entity.Theatre;
import com.edu.hcmuaf.springserver.service.TheatreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("api/theatres")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TheatreController {
    @Autowired
    private TheatreService theatreService;
    private static final Logger logger = LoggerFactory.getLogger(TheatreController.class);

    @GetMapping("/all")
    public ResponseEntity<?> getListTheatre(@RequestParam(defaultValue = "name") String sort) {
        try {
            List<Theatre> theatreList = theatreService.getAllTheatre(sort);
            return ResponseEntity.ok(theatreList);
        } catch (Exception e) {
            logger.error("Error retrieving list of theatres", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving list of theatres");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTheatreById(@PathVariable int id) {
        try {
            Theatre theatre = theatreService.getTheatreById(id);
            if (theatre != null) {
                return ResponseEntity.ok(theatre);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error retrieving theatre with id {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving theatre");
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createTheatre(@RequestBody TheatreRequest theatreRequest) {
        try {
            Theatre theatre = theatreService.createTheatre(theatreRequest);
            return ResponseEntity.ok(theatre);
        } catch (Exception e) {
            logger.error("Error creating theatre", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating theatre");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTheatre(@RequestBody TheatreRequest theatreRequest,@PathVariable int id) {
        try {
            Theatre theatre = theatreService.updateTheatre(theatreRequest, id);
            if (theatre != null) {
                return ResponseEntity.ok(theatre);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error updating theatre with id {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating theatre");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheatre(@PathVariable long id) {
        try {
            theatreService.deleteTheatreById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting theatre with id {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<Theatre>> getAll(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "{}") String filter,
                                                        @RequestParam(defaultValue = "16") int perPage,
                                                        @RequestParam(defaultValue = "name") String sort,
                                                        @RequestParam(defaultValue = "DESC") String order) {
        try {
            Page<Theatre> theatres = theatreService.getAllwithSort(filter, page, perPage, sort, order);
            return ResponseEntity.ok(theatres);
        } catch (Exception e) {
            logger.error("Error retrieving all theatres", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
