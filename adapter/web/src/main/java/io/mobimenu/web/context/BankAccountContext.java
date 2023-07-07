package io.mobimenu.web.context;

import io.mobimenu.core.port.in.payment.ViewBankAccountUseCase;
import io.mobimenu.core.port.out.payment.BankAccountQueryOperationsPort;
import io.mobimenu.core.service.bankaccount.BankAccountService;
import io.mobimenu.persistence.BankAccountQueryAdapter;
import io.mobimenu.persistence.repository.BankAccountRepository;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@ApplicationScoped
public class BankAccountContext {

    @Produces
    @Singleton
    public BankAccountQueryOperationsPort bankAccountQueryOperationsPort(BankAccountRepository repository) {
        return new BankAccountQueryAdapter(repository);
    }

    @Produces
    @Singleton
    public ViewBankAccountUseCase viewBankAccountUseCase(BankAccountQueryOperationsPort bankAccountQueryOperationsPort) {
        return new BankAccountService(bankAccountQueryOperationsPort);
    }

}
