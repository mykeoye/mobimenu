package io.mobimenu.persistence.mapper;

import java.util.List;
import io.mobimenu.domain.BankAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import io.mobimenu.persistence.entity.BankAccountEntity;

@Mapper(uses = { RestaurantMapper.class, UtilityMapper.class })
public interface BankAccountMapper {

    BankAccountMapper INSTANCE = Mappers.getMapper(BankAccountMapper.class);

    @Mapping(target = "bankAccountId", source = "id")
    BankAccount entityToDomainObject(BankAccountEntity entity);

    List<BankAccount> entitiesToDomainObject(List<BankAccountEntity> entities);

}
