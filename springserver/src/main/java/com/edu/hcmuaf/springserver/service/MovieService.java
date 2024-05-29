package com.edu.hcmuaf.springserver.service;

import com.edu.hcmuaf.springserver.entity.Category;
import com.edu.hcmuaf.springserver.entity.Movie;
import com.edu.hcmuaf.springserver.repositories.CategoryRepository;
import com.edu.hcmuaf.springserver.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> getAllMovie() {
        return movieRepository.findAll();
    }

    public Movie getMovieById(int id) {
        return movieRepository.findOneById(id);
    }
    public void deleteMovieById(long id) {movieRepository.deleteById(id);}

    public Movie createMovie(Movie movie) { return movieRepository.save(movie);}
//    public Movie updateMovie(Movie movie, int id) {
//        Movie existMovie = movieRepository.findOneById(id);
//        existMovie.setBackground_img_url(movie.getBackground_img_url());
//        existMovie.setTitle_img_url(movie.getTitle_img_url());
//        existMovie.setTitle(movie.getTitle());
//        existMovie.setReleased_date(movie.getReleased_date());
//        existMovie.setTrailer_video_url(movie.getTrailer_video_url());
//        existMovie.setPoster_url(movie.getPoster_url());
//        existMovie.setDescription(movie.getDescription());
//        existMovie.setSub_title(movie.getSub_title());
//        existMovie.setAge_type(movie.getAge_type());
//        existMovie.setType(movie.getType());
//
//        List<Category> updateCategory = new ArrayList<Category>();
//        for (Category category : movie.getCategories()) {
//            Category existCategory = existMovie.getCategories().stream().filter(v -> v.getId() == category.getId()).findFirst().orElse(null);
//            ;
//        }
//        return movieRepository.save(existMovie);
//    }

}
