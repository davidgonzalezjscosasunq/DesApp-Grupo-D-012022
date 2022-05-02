package ar.edu.unq.desapp.grupod.backenddesappapi.persistence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.Transaction;


@Repository
public interface TransactionsRepository extends CrudRepository<Transaction, Long> {

    List<Transaction> findAllByInterestedUserId(Long userId);

    @Query(value = "(SELECT * FROM TRANSACTIONS WHERE interested_user_id = ?1) " +
            "INNER JOIN " +
            "(SELECT * FROM TRANSACTIONS WHERE (start_local_date_time >= ?2 AND start_local_date_time <= ?3))", nativeQuery = true)
    List<Transaction> findAllByUserIdBetweenDates(Long userId, LocalDateTime startDate, LocalDateTime endDate);

}
