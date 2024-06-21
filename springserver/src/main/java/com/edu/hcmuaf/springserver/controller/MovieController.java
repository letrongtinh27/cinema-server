package com.edu.hcmuaf.springserver.controller;

import com.edu.hcmuaf.springserver.dto.request.UpdateMovieRequest;
import com.edu.hcmuaf.springserver.entity.Movie;
import com.edu.hcmuaf.springserver.service.MovieService;
import org.hibernate.sql.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Page;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/movies")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MovieController {
    private static final Logger logger = LoggerFactory.getLogger(MovieController.class);

    @Autowired
    private MovieService movieService;

    @GetMapping("/all")
    public ResponseEntity<?> getListMovie() {
        try {
            List<Movie> listMovie = movieService.getAllMovie();

            if (listMovie != null) {
                listMovie.removeIf(movie -> movie.getIs_active() == 1);
                return ResponseEntity.ok(listMovie);
            }
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("Error fetching get list movie ", e);
            return ResponseEntity.badRequest().body(null);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable int id) {
        try {
            Movie movie = movieService.getMovieById(id);
            if (movie != null) {
                return ResponseEntity.ok(movie);
            }
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("Error fetching get movie for id {}", id, e);
            return ResponseEntity.badRequest().body(null);
        }

    }

    @GetMapping("/search")
    public ResponseEntity<?> searchMovie(@Param("name") String name) {
        try {
            if(!name.equals(" ") && !name.isEmpty()) {
                List<Movie> movies = movieService.getAllMovie();
                List<Movie> searchMovies = new ArrayList<>();
                if(movies != null) {
                    movies.removeIf(movie -> movie.getIs_active() == 1);
                    for (Movie movie : movies) {
                        if(movie.getTitle().toLowerCase().contains(name.toLowerCase())) {
                            searchMovies.add(movie);
                        }
                    }
                }
                if(!searchMovies.isEmpty()) {
                    return ResponseEntity.ok((searchMovies));
                }
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error fetching search movie for name {}", name, e);
            return ResponseEntity.badRequest().build();

        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createMovie(@RequestBody Movie movie) {
        logger.info("Request to create new movie: {}", movie.getTitle());
        return ResponseEntity.ok(movieService.createMovie(movie));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMovie(@RequestBody UpdateMovieRequest movie, @PathVariable int id) {
        logger.info("Request to update movie with id: {}", id);
        return ResponseEntity.ok(movieService.updateMovie(movie, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable int id) {
        logger.info("Request to delete movie with id: {}", id);
        movieService.deleteMovieById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<Movie>> getAll(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "{}") String filter,
                                                      @RequestParam(defaultValue = "16") int perPage,
                                                      @RequestParam(defaultValue = "title") String sort,
                                                      @RequestParam(defaultValue = "DESC") String order) {
        Page<Movie> movies = movieService.getAllwithSort(filter, page, perPage, sort, order);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/all_filter")
    public ResponseEntity<List<Movie>> getAllMoviesByReleasedDate(@RequestParam(defaultValue = "{}") String filter) {
        List<Movie> movies = movieService.getAllMoviesByReleasedDate(filter);
        return ResponseEntity.ok(movies);
    }
}