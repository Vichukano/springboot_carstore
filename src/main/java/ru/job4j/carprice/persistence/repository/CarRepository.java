package ru.job4j.carprice.persistence.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.job4j.carprice.model.Car;

import java.util.List;

@Repository
public interface CarRepository extends CrudRepository<Car, Long> {

    @Query("SELECT c FROM Car c WHERE c.body.type = :type")
    List<Car> findCarByBody(@Param("type") String type);

    @Query("SELECT c FROM Car c WHERE c.engine.type = :type")
    List<Car> findCarByEngine(@Param("type") String type);

    @Query("SELECT c FROM Car c WHERE c.transmission.type =:type")
    List<Car> findCarByTransmission(@Param("type") String type);

    @Query("SELECT c FROM Car c WHERE NOT (c.image.url = 'empty')")
    List<Car> findCarWithImage();

    @Query("SELECT c FROM Car c WHERE day(c.createDate) = day(current_date)")
    List<Car> findCarForLastDay();

    @Query("SELECT c FROM Car c WHERE c.sold = false")
    List<Car> findRelevant();
}
