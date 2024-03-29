package org.booking.core.repository;

import org.booking.core.domain.entity.user.history.UserReservationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReservationHistoryRepository extends JpaRepository<UserReservationHistory, Long> {
}
