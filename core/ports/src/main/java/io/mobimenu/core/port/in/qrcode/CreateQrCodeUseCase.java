package io.mobimenu.core.port.in.qrcode;

import lombok.Builder;
import lombok.Value;
import java.util.List;
import java.util.stream.IntStream;
import lombok.ToString;
import io.smallrye.mutiny.Uni;
import io.mobimenu.domain.QrCode;
import lombok.RequiredArgsConstructor;

public interface CreateQrCodeUseCase {

    Uni<List<QrCode>> createQrCode(CreateQrCommand command);

    @Builder
    @Value
    @ToString
    @RequiredArgsConstructor
    class CreateQrCommand {
        QrCode.Type type;
        QrCode.Status status = QrCode.Status.ACTIVE;
        String number;
        QrCode.CreationType creationType;
        String restaurantId;
        QrCode.Range range;
        String outletId;

        public List<String> rangeToInt() {
            return switch (getCreationType()) {
                case SINGLE -> List.of(number);
                case BULK -> {
                    var range = getRange();
                    yield  IntStream.rangeClosed(range.from(), range.to())
                            .boxed()
                            .map(i -> Integer.toString(i))
                            .toList();
                }
            };
        }
    }
}
