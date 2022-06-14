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

    @Query(value = "SELECT transaction FROM Transaction transaction " +
                   "WHERE transaction.interestedUser.id = ?1 " +
                   "AND transaction.startLocalDateTime >= ?2 AND transaction.startLocalDateTime <= ?3 " +
                   "AND transaction.state = 'CONFIRMED'"
    )
    List<Transaction> findConfirmedTransactionsBetweenDatesFor(Long userId, LocalDateTime startDate, LocalDateTime endDate);

}
