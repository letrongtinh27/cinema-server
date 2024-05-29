package com.edu.hcmuaf.springserver.service;


import com.edu.hcmuaf.springserver.entity.Ticket;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private MovieService movieService;

    public void sendHtmlEmail(String to, Ticket ticket) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(to);
        helper.setSubject("Vé xem phim của bạn");

        Context context = new Context();

        System.out.println(ticket);
        context.setVariable("movieName", movieService.getMovieById(ticket.getShowTime().getMovieId()).getTitle());
        context.setVariable("code", ticket.getTicketCode());

        LocalDateTime date = ticket.getShowTime().getStart_time();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        context.setVariable("date", date.format(dateFormatter));
        context.setVariable("time", date.format(timeFormatter));
        context.setVariable("room", ticket.getShowTime().getRoom());
        context.setVariable("seat", ticket.getSeat().getRow_char()+""+ticket.getSeat().getSeat_number());

        String htmlContent = templateEngine.process("ticket.html", context);
        helper.setText(htmlContent, true);

        javaMailSender.send(message);
    }
}
