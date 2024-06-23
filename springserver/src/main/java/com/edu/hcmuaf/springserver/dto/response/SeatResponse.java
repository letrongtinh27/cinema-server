package com.edu.hcmuaf.springserver.dto.response;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SeatResponse implements Comparator<SeatResponse>{
    private int id;
    private int reservationId;
    private String seatNumber;
    private int price;
    private Date expired_time;
    private String payment;
    private boolean isBooked;
    private int status;

    @Override
    public int compare(SeatResponse seat1, SeatResponse seat2) {
        Pattern pattern = Pattern.compile("([A-Z])(\\d+)");
        Matcher matcher1 = pattern.matcher(seat1.getSeatNumber());
        Matcher matcher2 = pattern.matcher(seat2.getSeatNumber());

        if (matcher1.matches() && matcher2.matches()) {
            String row1 = matcher1.group(1);
            int number1 = Integer.parseInt(matcher1.group(2));

            String row2 = matcher2.group(1);
            int number2 = Integer.parseInt(matcher2.group(2));

            int rowComparison = row1.compareTo(row2);
            if (rowComparison != 0) {
                return rowComparison;
            } else {
                return Integer.compare(number1, number2);
            }
        }
        return seat1.getSeatNumber().compareTo(seat2.getSeatNumber());
    }
}