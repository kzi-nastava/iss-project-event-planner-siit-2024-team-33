package rs.ac.uns.ftn.asd.Projekatsiit2024.exception.offerReservation;

import lombok.Getter;

@Getter
public class ServiceBookingException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final String errorCode;

    public ServiceBookingException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ServiceBookingException(String message) {
        super(message);
        this.errorCode = "";
    }
}
