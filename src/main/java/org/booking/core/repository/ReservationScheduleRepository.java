package org.booking.core.repository;

import org.booking.core.domain.entity.business.ReservationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationScheduleRepository extends JpaRepository<ReservationSchedule, Long> {

    ReservationSchedule findByBusinessId(Long businessId);
}
