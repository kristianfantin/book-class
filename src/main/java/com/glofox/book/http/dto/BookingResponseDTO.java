package com.glofox.book.http.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(NON_NULL)
public class BookingResponseDTO extends BookingDTO {

    @ApiModelProperty(notes = "Id of Book")
    private Long id;

}
