package api.com.passin.controllers;

import api.com.passin.dto.attendee.AttendeeResponseBadgeDTO;
import api.com.passin.services.AttendeeService;
import api.com.passin.services.CheckInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/attendees")
@RequiredArgsConstructor
public class AttendeeController {
	private final AttendeeService attendeeService;


	@GetMapping("/{attendeeId}/badge")
	public ResponseEntity<AttendeeResponseBadgeDTO> getAttendeeBadge(@PathVariable String attendeeId, UriComponentsBuilder uriComponentsBuilder) {
		AttendeeResponseBadgeDTO response = this.attendeeService.getAttendeeBadge(attendeeId, uriComponentsBuilder);
		return ResponseEntity.ok().body(response);
	}


	@PostMapping("/{attendeeId}/check-in")
	public ResponseEntity registerCheckIn(@PathVariable String attendeeId, UriComponentsBuilder uriComponentsBuilder) {
		this.attendeeService.checkInAttendee(attendeeId);

		var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/badge").buildAndExpand(attendeeId).toUri();
		
		return ResponseEntity.created(uri).build();
	}
}
