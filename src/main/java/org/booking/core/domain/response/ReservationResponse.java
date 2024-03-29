package org.booking.core.domain.response;

import lombok.Data;


@Data
public class ReservationResponse {

	private Long id;
	private Long businessServiceId;
	private Long employeeId;
	private String bookingTime;
	private DurationResponse duration;
	private boolean canceled;

}
