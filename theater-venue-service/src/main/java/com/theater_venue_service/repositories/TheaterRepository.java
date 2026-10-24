package com.theater_venue_service.repositories;

import com.theater_venue_service.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheaterRepository extends JpaRepository<Theater,Long> {

    List<Theater> findByCity(String city);
    List<Theater> findByIsActive(boolean isActive);
    List<Theater> findByCityAndIsActive(String city,Boolean isActive);

}
