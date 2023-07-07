package io.mobimenu.core.port.out.qrcode;

import io.mobimenu.domain.filters.QrCodeFilter;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.mobimenu.common.api.Page;
import io.mobimenu.domain.QrCode;
import java.util.List;

public interface QrCodeQueryOperationsPort {

    Uni<QrCode> getQrCodeById(String qrCodeId);

    Uni<List<QrCode>> getByNumbersAndRestaurant(List<String> numbers, String restaurantId);

    Uni<Tuple2<Long, List<QrCode>>> getQrCodeByRestaurant(String restaurantId, Page page);

    Uni<Tuple2<Long, List<QrCode>>> getQrCodeByFilter(QrCodeFilter filter, Page page);

    Uni<QrCode> getByRestaurantTypeAndNumber(String restaurantId, QrCode.Type type, String number);

}
