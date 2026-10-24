package com.theater_venue_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "seat_layouts")
@AllArgsConstructor
@NoArgsConstructor
public class SeatLayout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id",nullable = false)
    private Screen screen;

    @Column(columnDefinition = "TEXT")
    private String layoutJson;

    private  Integer rowCount;

    private Integer columnCount;
}
