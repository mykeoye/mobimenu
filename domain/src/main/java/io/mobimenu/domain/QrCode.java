package io.mobimenu.domain;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

/**
 * A QrCode identifies the restaurant and menu from whence an order comes. It is unique and
 * carries extra details like the table/room number so waiters can know what tables to serve
 */
@ToString
@Value
@Builder
public class QrCode {

    /**
     * A unique identifier for the QrCode
     */
     String qrCodeId;

    /**
     * The restaurant table or hotel number this code is generated for
     */
    String tableNumber;

    /**
     * The type of qr code generated. Can be either of Restaurant or Hotel
     */
    Type type;

    /**
     * URI of the generated QrCode image
     */
    String qrImageUri;

    /**
     * The restaurant {@link Restaurant} the QrCode belongs to
     */
    String restaurantId;

    /**
     * The status of the QrCode
     */
    @Builder.Default
    Status status = Status.ACTIVE;

    /**
     * Creates a QrCode object with all required fields
     *
     * @param number            the restaurant table / hotel room number the code was generated for
     * @param qrImageUri        URI of the generated QrCode image
     * @param type              the type of qr code generated. Can be either of Restaurant or Hotel
     * @param restaurantId      the restaurant {@link Restaurant} the QrCode belongs to
     *
     * @return                  a QrCode object with the required fields initialized
     */
    public static QrCode withRequiredFields(
            final String number,
            final String qrImageUri,
            final Type type,
            final String restaurantId) {

        return QrCode.builder()
                .tableNumber(number)
                .qrImageUri(qrImageUri)
                .type(type)
                .restaurantId(restaurantId)
                .build();
    }

    /**
     * Creates a QrCode object with all required fields
     *
     * @param qrCodeId          a unique identifier for the QrCode
     * @param tableNumber       the restaurant/hotel room number the code was generated for
     * @param qrImageUri        URI of the generated QrCode image
     * @param type              the type of qr code generated. Can be either of Restaurant or Hotel
     * @param restaurantId      the restaurant {@link Restaurant} the QrCode belongs to
     * @return                  a QrCode object with the required fields initialized
     */
    public static QrCode withAllFields(
            final String qrCodeId,
            final String tableNumber,
            final String qrImageUri,
            final Type type,
            final Status status,
            final String restaurantId) {

        return QrCode.builder()
                .qrCodeId(qrCodeId)
                .tableNumber(tableNumber)
                .qrImageUri(qrImageUri)
                .type(type)
                .status(status)
                .restaurantId(restaurantId)
                .build();
    }

    /**
     * The type of Qrcode generated
     */
    public enum Type {
        RESTAURANT, HOTEL
    }

    /**
     * The status of the Qrcode
     */
    public enum Status {
        ACTIVE, INACTIVE, ARCHIVED;
    }

    /**
     * Qrcode creation type
     */
    public enum CreationType { SINGLE, BULK }

    /**
     * QrCode creation range
     */
    public record Range(int from, int to) {
        public boolean isValid() {
            return from < to;
        }
    }

    public String getServingDescription() {
        return "%s %s".formatted(Type.RESTAURANT.equals(type) ? "Table" : "Room", tableNumber);
    }

}
