package com.glofox.book.http.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(NON_NULL)
public class BookingClassResponseDTO {

    @ApiModelProperty(notes = "Date of Booking")
    private LocalDate date;

    @ApiModelProperty(notes = "Number of Bookings")
    private Long numberOfBookings;

    @ApiModelProperty(notes = "List of Booking from Class")
    private List<BookingResponseDTO> bookings;

}
