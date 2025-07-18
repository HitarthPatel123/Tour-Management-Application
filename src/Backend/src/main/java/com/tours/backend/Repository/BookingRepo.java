package com.tours.backend.Repository;

import com.tours.backend.Entities.Booking;
import com.tours.backend.Entities.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepo extends JpaRepository<BookingRepo, Long> {

    @Query("SELECT SUM(b.numberOfTickets) FROM Booking b WHERE b.tour.id = :tourId")
    Integer countTicketsSoldForTour(@Param("tourId") Long tourId);

    List<Booking> findByTourId(Long tourId);

    @Query("SELECT SUM(b.numberOfTickets) FROM Booking b WHERE b.tour.id = :tourId AND b.paymentStatus = :status")
    Integer countTicketsSoldForTourWithSuccessfulPayment(
            @Param("tourId") Long tourId,
            @Param("status") BookingRepo.PaymentStatus status
    );

    List<Booking> findByTourIdAndPaymentStatus(Long tourId, BookingRepo.PaymentStatus paymentStatus);


    @Query("SELECT t FROM Tour t WHERE " +
            "(:country IS NULL OR t.location.country = :country) AND " +
            "(:lodgingType IS NULL OR t.lodging.lodgingType = :lodgingType) AND " +
            "(:transportType IS NULL OR t.transport.transportType = :transportType) AND " +
            "(:minPrice IS NULL OR t.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR t.price <= :maxPrice)")
    List<Tour> filterTours(@Param("country") String location,
                           @Param("lodgingType") String lodgingType,
                           @Param("transportType") String transportType,
                           @Param("minPrice") Double minPrice,
                           @Param("maxPrice") Double maxPrice);
}
