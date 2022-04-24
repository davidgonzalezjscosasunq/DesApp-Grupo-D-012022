package ar.edu.unq.desapp.grupod.backenddesappapi.persistence;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.BuyOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradingOrdersRepository extends CrudRepository<BuyOrder, Long> {

    List<BuyOrder> findAllByBuyer(Long userId);

}
