package api.com.passin.config;

import api.com.passin.domain.attendee.exceptions.AttendeeAlreadyExistsException;
import api.com.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import api.com.passin.domain.checkin.exceptions.CheckInAlreadyExistsException;
import api.com.passin.domain.event.exceptions.EventFullException;
import api.com.passin.domain.event.exceptions.EventNotFoundException;
import api.com.passin.dto.general.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionEntityHandler {
	@ExceptionHandler(EventNotFoundException.class)
	public ResponseEntity handleEventNotFound(EventNotFoundException exception) {
		return ResponseEntity.notFound().build();
	}

	@ExceptionHandler(EventFullException.class)
	public ResponseEntity<ErrorResponseDTO> handleEventFull(EventFullException exception) {
		return ResponseEntity.badRequest().body(new ErrorResponseDTO(exception.getMessage()));
	}

	@ExceptionHandler(AttendeeNotFoundException.class)
	public ResponseEntity handleAttendeeFound(AttendeeNotFoundException exception) {
		return ResponseEntity.notFound().build();
	}

	@ExceptionHandler(AttendeeAlreadyExistsException.class)
	public ResponseEntity handleAttendeeAlreadyExist(AttendeeAlreadyExistsException exception) {
		return ResponseEntity.status(HttpStatus.CONFLICT).build();
	}

	@ExceptionHandler(CheckInAlreadyExistsException.class)
	public ResponseEntity handleCheckInAlreadyExist(CheckInAlreadyExistsException exception) {
		return ResponseEntity.status(HttpStatus.CONFLICT).build();
	}
}
