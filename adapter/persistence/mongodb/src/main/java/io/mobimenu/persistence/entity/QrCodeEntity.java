package io.mobimenu.persistence.entity;

import lombok.ToString;
import io.mobimenu.domain.QrCode;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

@ToString
@MongoEntity(collection = "qrcodes")
public class QrCodeEntity extends BaseEntity {
    public String tableNumber;
    public QrCode.Type type;
    public ObjectId restaurantId;
    public String qrImageUri;
    public QrCode.Status status;
}
