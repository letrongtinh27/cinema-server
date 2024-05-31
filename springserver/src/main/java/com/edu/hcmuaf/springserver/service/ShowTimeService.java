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


}