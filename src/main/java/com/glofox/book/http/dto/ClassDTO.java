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
public class ClassDTO {

    @NotNull(message = "Name od Class required")
    @Size(min = 1, max = 120)
    @ApiModelProperty(notes = "Name of Class", required = true)
    private String className;

    @NotNull(message = "Start Date required")
    @ApiModelProperty(notes = "Start Date. Format: yyyy-MM-dd", required = true)
    private LocalDate startDate;

    @NotNull(message = "End Date required")
    @ApiModelProperty(notes = "End Date. Format: yyyy-MM-dd", required = true)
    private LocalDate endDate;

    @NotNull(message = "Capacity required")
    @ApiModelProperty(notes = "Number of People supported in Class", required = true)
    private Integer capacity;

}
