package ar.edu.unq.desapp.grupod.backenddesappapi.persistence;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionsRepository extends CrudRepository<Transaction, Long> {

    List<Transaction> findAllByBuyer(Long userId);

}
