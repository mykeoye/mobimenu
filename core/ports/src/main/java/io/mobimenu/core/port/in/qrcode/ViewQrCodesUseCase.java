package io.mobimenu.core.port.in.qrcode;

import io.mobimenu.domain.filters.QrCodeFilter;
import io.mobimenu.domain.structs.QrMenu;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.mobimenu.common.api.Page;
import io.mobimenu.domain.QrCode;
import java.util.List;

public interface ViewQrCodesUseCase {

    Uni<QrCode> loadById(String qrCodeId);

    Uni<Tuple2<Long, List<QrCode>>> loadByRestaurant(String restaurantId, Page page);

    Uni<Tuple2<Long, List<QrCode>>> loadByFilter(QrCodeFilter filter, Page page);

    Uni<QrMenu> loadMenuByQrId(String qrCodeId);

}
