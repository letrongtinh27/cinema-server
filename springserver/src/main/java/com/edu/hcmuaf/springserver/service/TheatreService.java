package com.edu.hcmuaf.springserver.service;

import com.edu.hcmuaf.springserver.entity.Theatre;
import com.edu.hcmuaf.springserver.repositories.TheatreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TheatreService {
    @Autowired
    private TheatreRepository theatreRepository;

    public List<Theatre> getAllTheatre() {
        return theatreRepository.findAll();
    }

    public Theatre getTheatreById(int id) {
        return theatreRepository.findOneById(id);
    }

    public void deleteTheatreById(long id) {theatreRepository.deleteById(id);}

    public Theatre createTheatre(Theatre theatre) {
        theatre.setAddress(theatre.getAddress());
        theatre.setEmail(theatre.getEmail());
        theatre.setName(theatre.getName());
        theatre.setLocation(theatre.getLocation());
        theatre.setDescription(theatre.getDescription());
        theatre.setLocation_id(theatre.getLocation_id());
        theatre.setOpening_hours(theatre.getOpening_hours());
        theatre.setRoom_summary(theatre.getRoom_summary());
        theatre.setPhone_number(theatre.getPhone_number());
        theatre.setRooms(theatre.getRooms());
        return theatreRepository.save(theatre);}

    public Theatre updateTheatre(Theatre theatre, int id) {
        Theatre existTheatre = theatreRepository.findOneById(id);
        existTheatre.setAddress(theatre.getAddress());
        existTheatre.setEmail(theatre.getEmail());
        existTheatre.setName(theatre.getName());
        existTheatre.setLocation(theatre.getLocation());
        existTheatre.setDescription(theatre.getDescription());
        existTheatre.setLocation_id(theatre.getLocation_id());
        existTheatre.setOpening_hours(theatre.getOpening_hours());
        existTheatre.setRoom_summary(theatre.getRoom_summary());
        existTheatre.setPhone_number(theatre.getPhone_number());
        existTheatre.setRooms(theatre.getRooms());
        return theatreRepository.save(existTheatre);
    }
}
