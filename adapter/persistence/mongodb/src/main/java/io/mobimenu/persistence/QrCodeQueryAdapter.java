package io.mobimenu.persistence;

import io.mobimenu.domain.filters.QrCodeFilter;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.mobimenu.common.api.Page;
import io.mobimenu.core.port.out.qrcode.QrCodeQueryOperationsPort;
import io.mobimenu.domain.QrCode;
import io.mobimenu.persistence.mapper.QrCodeMapper;
import io.mobimenu.persistence.repository.QrCodeRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class QrCodeQueryAdapter implements QrCodeQueryOperationsPort {

    private final QrCodeRepository repository;
    private final QrCodeMapper mapper = QrCodeMapper.INSTANCE;

    @Override
    public Uni<QrCode> getQrCodeById(String qrCodeId) {
        return repository.findByQrCodeId(qrCodeId).map(mapper::entityToDomainObject);
    }

    @Override
    public Uni<List<QrCode>> getByNumbersAndRestaurant(List<String> numbers, String restaurantId) {
        return repository.findByNumbersAndRestaurant(numbers, restaurantId).map(mapper::entitiesToDomainObjects);
    }

    @Override
    public Uni<Tuple2<Long, List<QrCode>>> getQrCodeByRestaurant(String restaurantId, Page page) {
        var countUni = repository.countAllByRestaurant(restaurantId);
        var qrCodeUni = repository.findAllByRestaurant(restaurantId, page.getOffset(), page.getLimit())
                .map(qrCodes -> Optional.ofNullable(qrCodes).orElseGet(List::of))
                .map(mapper::entitiesToDomainObjects);
        return Uni.combine().all().unis(countUni, qrCodeUni).asTuple();
    }

    @Override
    public Uni<Tuple2<Long, List<QrCode>>> getQrCodeByFilter(QrCodeFilter filter, Page page) {
        var countUni = repository.countByFilter(filter);
        var qrCodeUni = repository.findByFilter(filter, page.getOffset(), page.getLimit())
                .map(qrCodes -> Optional.ofNullable(qrCodes).orElseGet(List::of))
                .map(mapper::entitiesToDomainObjects);
        return Uni.combine().all().unis(countUni, qrCodeUni).asTuple();
    }

    @Override
    public Uni<QrCode> getByRestaurantTypeAndNumber(String restaurantId, QrCode.Type type, String number) {
        return repository.findByRestaurantTypeAndNumber(restaurantId, type, number)
                .map(mapper::entityToDomainObject);
    }

}
