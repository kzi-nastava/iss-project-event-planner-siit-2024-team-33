package rs.ac.uns.ftn.asd.Projekatsiit2024.service;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.OfferReservation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.OfferReservationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class Scheduler {

    private final OfferReservationRepository reservationRepository;
    private final NotificationService notificationService;
    
    public Scheduler(OfferReservationRepository reservationRepository, NotificationService notificationService) {
			this.reservationRepository = reservationRepository;
			this.notificationService = notificationService;
    	}
    
    @Scheduled(fixedRate = 60000)
    public void checkUpcomingReservations() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneHourFromNow = now.plusHours(1*24*10);

        List<OfferReservation> upcomingReservations = reservationRepository.findByStartTimeBetween(now, oneHourFromNow);

        for (OfferReservation reservation : upcomingReservations) {
        	
            notificationService.createNotification(
                    reservation.getEvent().getOrganizer().getId(),
                    "You have an upcoming booked offer for your event: "+ reservation.getEvent().getName()+ ", at: " + reservation.getStartTime());
        }
    }
}
