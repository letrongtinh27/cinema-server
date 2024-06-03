package com.edu.hcmuaf.springserver.service;

import com.edu.hcmuaf.springserver.dto.request.TheatreRequest;
import com.edu.hcmuaf.springserver.entity.Theatre;
import com.edu.hcmuaf.springserver.repositories.LocationRepository;
import com.edu.hcmuaf.springserver.repositories.TheatreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TheatreService {
    @Autowired
    private TheatreRepository theatreRepository;
    @Autowired
    private LocationService locationService;

    public List<Theatre> getAllTheatre() {
        return theatreRepository.findAll();
    }

    public Theatre getTheatreById(int id) {
        return theatreRepository.findOneById(id);
    }

    public void deleteTheatreById(long id) {theatreRepository.deleteById(id);}

    public Theatre createTheatre(TheatreRequest theatreRequest) {
        System.out.println(theatreRequest);
        Theatre theatre = new Theatre();
        theatre.setAddress(theatreRequest.getAddress());
        theatre.setEmail(theatreRequest.getEmail());
        theatre.setName(theatreRequest.getName());
        theatre.setDescription(theatreRequest.getDescription());
        theatre.setLocation(locationService.getLocationById(theatreRequest.getLocation_id()));
        theatre.setOpening_hours(theatreRequest.getOpening_hours());
        theatre.setRoom_summary(theatreRequest.getRoom_summary());
        theatre.setPhone_number(theatreRequest.getPhone_number());
        theatre.setRooms(theatreRequest.getRooms());
        return theatreRepository.save(theatre);}

    public Theatre updateTheatre(TheatreRequest theatreRequest, int id) {
        Theatre existTheatre = theatreRepository.findOneById(id);
        existTheatre.setAddress(theatreRequest.getAddress());
        existTheatre.setEmail(theatreRequest.getEmail());
        existTheatre.setName(theatreRequest.getName());
        existTheatre.setLocation(locationService.getLocationById(theatreRequest.getLocation_id()));
        existTheatre.setDescription(theatreRequest.getDescription());
        existTheatre.setOpening_hours(theatreRequest.getOpening_hours());
        existTheatre.setRoom_summary(theatreRequest.getRoom_summary());
        existTheatre.setPhone_number(theatreRequest.getPhone_number());
        existTheatre.setRooms(theatreRequest.getRooms());
        return theatreRepository.save(existTheatre);
    }
}
