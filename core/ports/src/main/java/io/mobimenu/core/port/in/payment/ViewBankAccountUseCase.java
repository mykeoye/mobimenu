package io.mobimenu.core.port.in.payment;

import io.mobimenu.domain.BankAccount;
import io.smallrye.mutiny.Uni;
import java.util.List;

public interface ViewBankAccountUseCase {

    Uni<List<BankAccount>> loadByRestaurant(String restaurantId);

}
