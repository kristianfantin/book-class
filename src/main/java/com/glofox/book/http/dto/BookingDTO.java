package com.glofox.book.http.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(NON_NULL)
public class BookingDTO {

    @NotNull(message = "Class Name required")
    @Size(min = 1, max = 120)
    @ApiModelProperty(notes = "Class Name - You can find in (/classes)", required = true)
    private String className;

    @NotNull(message = "Booking Date required")
    @ApiModelProperty(notes = "Booking Date. Format: yyyy-MM-dd", required = true)
    private LocalDate bookingDate;

    @NotNull(message = "Name of Member required")
    @Size(min = 1, max = 120)
    @ApiModelProperty(notes = "Name of Member who will participate of Class", required = true)
    private String memberName;

}
