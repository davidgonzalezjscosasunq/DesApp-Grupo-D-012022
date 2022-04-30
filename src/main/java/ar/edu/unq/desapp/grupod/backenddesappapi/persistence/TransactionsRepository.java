package ar.edu.unq.desapp.grupod.backenddesappapi.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.Transaction;


@Repository
public interface TransactionsRepository extends CrudRepository<Transaction, Long> {

    List<Transaction> findAllByInterestedUserId(Long userId);

    List<Transaction> findAllBetweenDates(LocalDateTime start, LocalDateTime end);

}
