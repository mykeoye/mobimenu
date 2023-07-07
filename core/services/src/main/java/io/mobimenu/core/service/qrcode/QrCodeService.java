package io.mobimenu.core.service.qrcode;

import java.util.List;
import java.util.stream.Collectors;
import io.mobimenu.core.port.in.qrcode.UpdateQrCodeUseCase;
import io.mobimenu.core.port.in.qrcode.UploadQrCodeUseCase;
import io.mobimenu.core.port.out.menu.MenuQueryOperationsPort;
import io.mobimenu.domain.filters.QrCodeFilter;
import io.mobimenu.domain.structs.QrMenu;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.mobimenu.common.api.Code;
import io.mobimenu.common.api.Failure;
import io.mobimenu.common.api.Page;
import io.mobimenu.core.port.in.qrcode.CreateQrCodeUseCase;
import io.mobimenu.core.port.in.qrcode.ViewQrCodesUseCase;
import io.mobimenu.core.port.out.qrcode.QrCodeQueryOperationsPort;
import io.mobimenu.core.port.out.qrcode.QrCodeSaveOperationsPort;
import io.mobimenu.core.port.out.restaurant.RestaurantQueryOperationsPort;
import io.mobimenu.domain.QrCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QrCodeService implements CreateQrCodeUseCase, ViewQrCodesUseCase, UpdateQrCodeUseCase {

    private final QrCodeSaveOperationsPort qrCodeSaveOperationsPort;
    private final QrCodeQueryOperationsPort qrCodeQueryOperationsPort;
    private final RestaurantQueryOperationsPort restaurantQueryOperationsPort;
    private final MenuQueryOperationsPort menuQueryOperationsPort;
    private final UploadQrCodeUseCase uploadQrCodeUseCase;

    @Override
    public Uni<List<QrCode>> createQrCode(CreateQrCommand command) {
        var restaurantId = command.getRestaurantId();
        return restaurantQueryOperationsPort.getById(restaurantId)
                .onItem().ifNull().failWith(Failure.of(Code.RESTAURANT_NOT_FOUND))
                .chain(() -> qrCodeQueryOperationsPort.getByNumbersAndRestaurant(command.rangeToInt(), restaurantId))
                .flatMap(codes -> {
                    if (codes == null || codes.isEmpty()) {
                        return Uni.createFrom().nullItem();
                    }
                    return Uni.createFrom().item(codes);
                }).onItem().ifNotNull().failWith(Failure.of(Code.QRCODE_EXISTS))
                .map(numbers -> command.rangeToInt().stream()
                        .map(i -> QrCode.withRequiredFields(
                                i.toString(),
                                null,
                                command.getType(),
                                restaurantId))
                        .collect(Collectors.toList()))
                .flatMap(qrCodeSaveOperationsPort::saveCodes)
                .flatMap(uploadQrCodeUseCase::uploadCodes);
    }

    @Override
    public Uni<QrCode> loadById(String qrCodeId) {
        return qrCodeQueryOperationsPort.getQrCodeById(qrCodeId);
    }

    @Override
    public Uni<Tuple2<Long, List<QrCode>>> loadByRestaurant(String restaurantId, Page page) {
        return qrCodeQueryOperationsPort.getQrCodeByRestaurant(restaurantId, page);
    }

    @Override
    public Uni<Tuple2<Long, List<QrCode>>> loadByFilter(QrCodeFilter filter, Page page) {
        return qrCodeQueryOperationsPort.getQrCodeByFilter(filter, page);
    }

    @Override
    public Uni<QrMenu> loadMenuByQrId(String qrCodeId) {
        return qrCodeQueryOperationsPort.getQrCodeById(qrCodeId)
                .onItem().ifNull().failWith(Failure.of(Code.QRCODE_EXISTS))
                .flatMap(qrCode -> {
                    if (!QrCode.Status.ACTIVE.equals(qrCode.getStatus())) {
                        return Uni.createFrom().failure(Failure.of(Code.QRCODE_NOT_FOUND));
                    }
                    return Uni.createFrom().item(() -> qrCode);
                })
                .flatMap(qrCode -> menuQueryOperationsPort.getActiveMenuByRestaurant(qrCode.getRestaurantId())
                        .onItem().ifNull().failWith(Failure.of(Code.NO_ACTIVE_MENU))
                        .map(menu -> new QrMenu(qrCode.getQrCodeId(), qrCode.getServingDescription(), menu.getMenuId(), menu.getTheme(), menu.getRestaurantId())));
    }

    @Override
    public Uni<QrCode> updateStatus(UpdateQrCodeStatusCommand command) {
        return qrCodeQueryOperationsPort.getQrCodeById(command.getQrcodeId())
                .onItem().ifNull().failWith(Failure.of(Code.QRCODE_NOT_FOUND))
                .chain(() -> qrCodeSaveOperationsPort.updateStatus(command.getRestaurantId(), command.getStatus(), command.getQrcodeId()));
    }

    @Override
    public Uni<QrCode> updateQrCode(UpdateQrCodeCommand command) {
        return qrCodeQueryOperationsPort.getQrCodeById(command.getQrcodeId())
                .onItem().ifNull().failWith(Failure.of(Code.QRCODE_NOT_FOUND))
                .flatMap(qrCode -> qrCodeQueryOperationsPort.getByRestaurantTypeAndNumber(command.getRestaurantId(), command.getType(), command.getNumber())
                        .flatMap(existing -> {
                            // If we don't find a code with the same details we want to update it's safe to proceed
                            if (existing == null) {
                                return Uni.createFrom().nullItem();
                            }
                            // There's been no change to the qrcode, so we just return the current object
                            if (qrCode.getQrCodeId().equals(existing.getQrCodeId())) {
                                return Uni.createFrom().item(() -> qrCode);
                            }
                            // A qrcode with the same details exists so there will be a conflict if we allow the update
                            return Uni.createFrom().failure(Failure.of(Code.QRCODE_EXISTS));
                        }))
                .flatMap(qrCode -> {
                    if (qrCode != null) {
                        return Uni.createFrom().item(qrCode);
                    }
                    return qrCodeSaveOperationsPort.update(command.getQrcodeId(), command.getType(), command.getNumber());
                });
    }

}
