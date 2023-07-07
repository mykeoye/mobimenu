package io.mobimenu.persistence.mapper;

import java.util.List;
import io.mobimenu.domain.QrCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import io.mobimenu.persistence.entity.QrCodeEntity;

@Mapper(uses = { RestaurantMapper.class, MenuMapper.class, UtilityMapper.class })
public interface QrCodeMapper {

    QrCodeMapper INSTANCE = Mappers.getMapper(QrCodeMapper.class);

    @Mapping(target = "qrCodeId", source = "id")
    QrCode entityToDomainObject(QrCodeEntity entity);

    @Mapping(target = "id", ignore = true)
    QrCodeEntity domainObjectToPersistEntity(QrCode qrCode);

    List<QrCodeEntity> domainObjectsToPersistentEntity(List<QrCode> qrCodes);

    List<QrCode> entitiesToDomainObjects(List<QrCodeEntity> entities);

}
