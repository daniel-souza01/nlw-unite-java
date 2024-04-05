package api.com.passin.services;

import api.com.passin.domain.attendee.Attendee;
import api.com.passin.domain.checkin.CheckIn;
import api.com.passin.domain.checkin.exceptions.CheckInAlreadyExistsException;
import api.com.passin.repositories.CheckInRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckInService {
	private final CheckInRepository checkInRepository;

	public void registerCheckIn(Attendee attendee) {
		this.verifyCheckInExists(attendee.getId());

		CheckIn newCheckIn = new CheckIn();
		newCheckIn.setAttendee(attendee);
		newCheckIn.setCreatedAt(LocalDateTime.now());

		this.checkInRepository.save(newCheckIn);
	}

	private void verifyCheckInExists(String attendeeId) {
		Optional<CheckIn> isCheckedIn = this.getCheckIn(attendeeId);
		if (isCheckedIn.isPresent()) throw new CheckInAlreadyExistsException("Attendee is already checked in");
	}

	public Optional<CheckIn> getCheckIn(String attendeeId) {
		return this.checkInRepository.findByAttendeeId(attendeeId);
	}
}
