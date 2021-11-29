package com.glofox.book.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "BOOK_ENTITY")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
public class BookEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "CLASS_ID", nullable = false)
    private Long classId;

    @Column(name = "BOOKING_DATE", nullable = false)
    private LocalDate bookingDate;

    @Column(name = "MEMBER_NAME", nullable = false, length = 120)
    private String memberName;

    @Column(name = "AUX_MEMBER_NAME", nullable = false, length = 120)
    private String auxMemberName;


}
