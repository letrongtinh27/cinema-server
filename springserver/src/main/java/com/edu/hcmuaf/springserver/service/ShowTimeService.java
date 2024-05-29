package com.edu.hcmuaf.springserver.service;

import com.edu.hcmuaf.springserver.entity.ShowTime;

import com.edu.hcmuaf.springserver.entity.Theatre;
import com.edu.hcmuaf.springserver.repositories.ShowTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
        return showTimeRepository.findById(Long.valueOf(id)).orElse(null);
    }

    public ShowTime updateShowTime(int id,ShowTime showTime) {
        ShowTime existShowTime = showTimeRepository.findShowTimesById(id).orElseThrow(() -> new IllegalArgumentException("not found"));
        existShowTime.setMovieId(showTime.getMovieId());
        existShowTime.setRoom(showTime.getRoom());
        existShowTime.setTheatreId(showTime.getTheatreId());
        existShowTime.setStart_time(showTime.getStart_time());
        existShowTime.setEnd_time(showTime.getEnd_time());
        existShowTime.setStatus(showTime.getStatus());
        existShowTime.setMovie_st(showTime.getMovie_st());
        return showTimeRepository.save(existShowTime);
    }

    public ShowTime createShowTime(ShowTime showTime) {
        showTime.setMovieId(showTime.getMovieId());
        showTime.setRoom(showTime.getRoom());
        showTime.setTheatreId(showTime.getTheatreId());
        showTime.setStart_time(showTime.getStart_time());
        showTime.setEnd_time(showTime.getEnd_time());
        showTime.setStatus(showTime.getStatus());
        showTime.setMovie_st(showTime.getMovie_st());
        return showTimeRepository.save(showTime);
    }

    public void deleteShowTime(long id) {
        showTimeRepository.deleteById(id);
    }


}