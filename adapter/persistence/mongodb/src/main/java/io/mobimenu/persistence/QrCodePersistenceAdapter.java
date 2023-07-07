package io.mobimenu.persistence;

import java.time.Instant;
import java.util.List;

import io.mobimenu.persistence.common.Fields;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;
import io.mobimenu.core.port.out.qrcode.QrCodeSaveOperationsPort;
import io.mobimenu.domain.QrCode;
import io.mobimenu.persistence.entity.QrCodeEntity;
import io.mobimenu.persistence.mapper.QrCodeMapper;
import io.mobimenu.persistence.repository.QrCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Slf4j
@RequiredArgsConstructor
public class QrCodePersistenceAdapter implements QrCodeSaveOperationsPort {

    private final QrCodeRepository repository;
    private final QrCodeMapper mapper = QrCodeMapper.INSTANCE;

    @Override
    public Uni<List<QrCode>> saveCodes(List<QrCode> qrCodes) {
        List<QrCodeEntity> entities = mapper.domainObjectsToPersistentEntity(qrCodes).stream().toList();
        return repository.save(entities).map(mapper::entitiesToDomainObjects);
    }

    @Override
    public Uni<QrCode> updateStatus(String restaurantId, QrCode.Status status, String qrcodeId) {
        return repository.update("%s = :status and %s = :updated".formatted(Fields.STATUS, Fields.UPDATED),
                        Parameters.with(Fields.STATUS, status).and(Fields.UPDATED, Instant.now()))
                .where("%s = :restaurantId and %s = :_id".formatted(Fields.RESTAURANT_ID, Fields._ID),
                        Parameters.with(Fields.RESTAURANT_ID, new ObjectId(restaurantId)).and(Fields._ID, new ObjectId(qrcodeId)))
                .flatMap(updateCount -> {
                    if (updateCount > 0) {
                        log.info("Update successful, update count: {}", updateCount);
                    }
                    return repository.findById(new ObjectId(qrcodeId));
                }).map(mapper::entityToDomainObject);
    }

    @Override
    public Uni<QrCode> update(String qrCodeId, QrCode.Type type, String number) {
        return repository.findById(new ObjectId(qrCodeId))
                .map(entity -> {
                    entity.type = type;
                    entity.tableNumber = number;
                    return entity;
                })
                .flatMap(repository::update)
                .map(mapper::entityToDomainObject);
    }

    @Override
    public Uni<QrCode> updateUrl(String qrCodeId, String url) {
        return repository.findById(new ObjectId(qrCodeId))
                .map(entity -> {
                    entity.qrImageUri = url;
                    return entity;
                })
                .flatMap(repository::update)
                .map(mapper::entityToDomainObject);
    }

}
