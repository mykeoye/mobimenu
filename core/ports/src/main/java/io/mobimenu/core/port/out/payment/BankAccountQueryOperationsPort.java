package io.mobimenu.core.port.out.payment;

import io.mobimenu.domain.BankAccount;
import io.smallrye.mutiny.Uni;
import java.util.List;

public interface BankAccountQueryOperationsPort {

    Uni<List<BankAccount>> getByRestaurantId(String restaurantId);

}
