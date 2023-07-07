package io.mobimenu.core.service.bankaccount;

import io.mobimenu.core.port.in.payment.ViewBankAccountUseCase;
import io.mobimenu.core.port.out.payment.BankAccountQueryOperationsPort;
import io.mobimenu.domain.BankAccount;
import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BankAccountService implements ViewBankAccountUseCase {

    private final BankAccountQueryOperationsPort bankAccountQueryOperationsPort;

    @Override
    public Uni<List<BankAccount>> loadByRestaurant(String restaurantId) {
        return bankAccountQueryOperationsPort.getByRestaurantId(restaurantId);
    }

}
