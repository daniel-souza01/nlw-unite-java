package api.com.passin.services;

import api.com.passin.domain.attendee.Attendee;
import api.com.passin.domain.attendee.exceptions.AttendeeAlreadyExistsException;
import api.com.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import api.com.passin.domain.checkin.CheckIn;
import api.com.passin.dto.attendee.AttendeeResponseBadgeDTO;
import api.com.passin.dto.attendee.AttendeeDetails;
import api.com.passin.dto.attendee.AttendeesListResponseDTO;
import api.com.passin.dto.attendee.AttendeeBadgeDTO;
import api.com.passin.repositories.AttendeeRepository;
import api.com.passin.repositories.CheckInRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {
	private final AttendeeRepository attendeeRepository;
	private final CheckInService checkInService;

	public List<Attendee> getAllAttendeesFromEvent(String eventId) {
		return this.attendeeRepository.findByEventId(eventId);
	}

	public AttendeesListResponseDTO getEventsAttendee(String eventId) {
		List<Attendee> attendeeList = this.getAllAttendeesFromEvent(eventId);

		List<AttendeeDetails> attendeeDetailsList = attendeeList.stream().map(attendee -> {
			Optional<CheckIn> checkIn = this.checkInService.getCheckIn(attendee.getId());
			LocalDateTime checkedInAt = checkIn.<LocalDateTime>map(CheckIn::getCreatedAt).orElse(null);
			return new AttendeeDetails(attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreatedAt(), checkedInAt);
		}).toList();

		return new AttendeesListResponseDTO(attendeeDetailsList);
	}

	public void verifyAttendeeSubscription(String email, String eventId) {
		Optional<Attendee> isAttendeeRegistered = this.attendeeRepository.findByEventIdAndEmail(eventId, email);
		if (isAttendeeRegistered.isPresent())
			throw new AttendeeAlreadyExistsException("Attendee is already registered");
	}

	public Attendee registerAttendee(Attendee newAttendee) {
		this.attendeeRepository.save(newAttendee);
		return newAttendee;
	}

	public void checkInAttendee(String attendeeId) {
		Attendee attendee = this.getAttendee(attendeeId);
		this.checkInService.registerCheckIn(attendee);
	}

	private Attendee getAttendee(String attendeeId) {
		return this.attendeeRepository.findById(attendeeId).orElseThrow(() -> new AttendeeNotFoundException("attendee not found with ID: " + attendeeId));
	}

	public AttendeeResponseBadgeDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder) {
		Attendee attendee = this.getAttendee(attendeeId);

		var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/check-in").buildAndExpand(attendeeId).toUri().toString();

		AttendeeBadgeDTO badgeDTO = new AttendeeBadgeDTO(attendee.getName(), attendee.getEmail(), uri, attendee.getEvent().getId());
		return new AttendeeResponseBadgeDTO(badgeDTO);
	}
}
