package api.com.passin.services;

import api.com.passin.domain.attendee.Attendee;
import api.com.passin.domain.checkin.CheckIn;
import api.com.passin.dto.attendee.AttendeeDetails;
import api.com.passin.dto.attendee.AttendeesListResponseDTO;
import api.com.passin.repositories.AttendeeRepository;
import api.com.passin.repositories.CheckInRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {
	private final AttendeeRepository attendeeRepository;
	private final CheckInRepository checkInRepository;

	public List<Attendee> getAllAttendeesFromEvent(String eventId) {
		return this.attendeeRepository.findByEventId(eventId);
	}

	public AttendeesListResponseDTO getEventsAttendee(String eventId) {
		List<Attendee> attendeeList = this.getAllAttendeesFromEvent(eventId);

		List<AttendeeDetails> attendeeDetailsList = attendeeList.stream().map(attendee -> {
			Optional<CheckIn> checkIn = this.checkInRepository.findByAttendeeId(attendee.getId());
			LocalDateTime checkedInAt = checkIn.<LocalDateTime>map(CheckIn::getCreatedAt).orElse(null);
			return new AttendeeDetails(attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreatedAt(), checkedInAt);
		}).toList();

		return new AttendeesListResponseDTO(attendeeDetailsList);
	}
}
