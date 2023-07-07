package io.mobimenu.persistence.mapper;

import io.netty.util.internal.StringUtil;
import io.mobimenu.domain.PhoneNumber;
import io.mobimenu.domain.User;
import io.mobimenu.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(uses = { UtilityMapper.class, RestaurantMapper.class })
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    UserEntity domainObjectToPersistEntity(User user);

    @Mapping(target = "userId", source = "id")
    User entityToDomainObject(UserEntity entity);

    List<User> entitiesToDomainObjects(List<UserEntity> entities);

    default String phoneNumberToString(PhoneNumber phoneNumber) {
        return (phoneNumber != null) ? phoneNumber.toString() : null;
    }

    default PhoneNumber stringToPhoneNumber(String phoneNumber) {
        if (StringUtil.isNullOrEmpty(phoneNumber)) {
            return null;
        }
        var s = phoneNumber.split(" ");
        if (s.length != 2) {
            return null;
        }
        return new PhoneNumber(s[0], s[1]);
    }

}
