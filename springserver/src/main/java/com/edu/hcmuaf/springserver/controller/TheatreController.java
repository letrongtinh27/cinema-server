package com.edu.hcmuaf.springserver.controller;

import com.edu.hcmuaf.springserver.dto.request.TheatreRequest;
import com.edu.hcmuaf.springserver.entity.Theatre;
import com.edu.hcmuaf.springserver.service.TheatreService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    HttpServletRequest request;

    @GetMapping("/all")
    public ResponseEntity<?> getListTheatre() {
        List<Theatre> theatreList = theatreService.getAllTheatre();
        if (theatreList != null ) {
            return ResponseEntity.ok(theatreList);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTheatreById(@PathVariable int id) {
        Theatre theatre = theatreService.getTheatreById(id);
        if (theatre != null) {
            return ResponseEntity.ok(theatre);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @PostMapping("/")
    public ResponseEntity<?> createTheatre(@RequestBody TheatreRequest theatreRequest) {
        System.out.println(theatreRequest);
        return ResponseEntity.ok(theatreService.createTheatre(theatreRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTheatre(@RequestBody TheatreRequest theatreRequest,@PathVariable int id) {
        return ResponseEntity.ok(theatreService.updateTheatre(theatreRequest, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheatre(@PathVariable long id) {
        theatreService.deleteTheatreById(id);
        return ResponseEntity.noContent().build();
    }
}
