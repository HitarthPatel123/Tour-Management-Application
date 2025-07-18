package com.tours.backend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name="Booking")
@Builder

public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bookingId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private Users customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tour_id", referencedColumnName = "id", nullable = false)
    private Tour tour;

    private int numberOfTickets;
    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Temporal(TemporalType.TIMESTAMP)
    private Date bookingDate;

    private boolean isBookingConfirmed;

    private String paymentTransactionId;

    public boolean checkAvailability() {return tour.getTicketsAvailable() > 0;}

    public void confirmBooking() {
        if(paymentStatus == PaymentStatus.SUCCESS && checkAvailability()) {
            tour.setTicketsAvailable(tour.getTicketsAvailable() - numberOfTickets);
            this.isBookingConfirmed = true;
        } else{
            isBookingConfirmed = false;
        }
    }

    public void handlePaymentFailure(String reason) {
        this.paymentStatus = PaymentStatus.FAILED;
        this.isBookingConfirmed = false;
    }
}
