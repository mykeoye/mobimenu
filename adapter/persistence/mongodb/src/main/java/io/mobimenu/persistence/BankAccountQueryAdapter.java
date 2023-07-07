package io.mobimenu.persistence;

import io.mobimenu.domain.BankAccount;
import io.smallrye.mutiny.Uni;
import java.util.List;
import lombok.RequiredArgsConstructor;
import io.mobimenu.persistence.mapper.BankAccountMapper;
import io.mobimenu.persistence.repository.BankAccountRepository;
import io.mobimenu.core.port.out.payment.BankAccountQueryOperationsPort;

@RequiredArgsConstructor
public class BankAccountQueryAdapter implements BankAccountQueryOperationsPort {

    private final BankAccountRepository repository;
    private final BankAccountMapper mapper = BankAccountMapper.INSTANCE;

    @Override
    public Uni<List<BankAccount>> getByRestaurantId(String restaurantId) {
        return repository.findByRestaurant(restaurantId)
                .map(mapper::entitiesToDomainObject);
    }

}
