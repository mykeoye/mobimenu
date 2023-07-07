package io.mobimenu.core.service.restaurant;

import io.mobimenu.common.util.IdentifierProvider;
import io.mobimenu.core.port.in.restaurant.UpdateRestaurantUseCase;
import io.mobimenu.domain.Theme;
import io.mobimenu.storage.StorageFolder;
import io.mobimenu.storage.StorageProvider;
import io.mobimenu.storage.StorageRequest;
import io.mobimenu.storage.StorageResponse;
import io.smallrye.mutiny.Uni;
import io.mobimenu.common.api.Code;
import io.mobimenu.common.api.Failure;
import io.mobimenu.common.security.PasswordSecurity;
import io.mobimenu.core.port.in.restaurant.CreateRestaurantUseCase;
import io.mobimenu.core.port.out.restaurant.RestaurantQueryOperationsPort;
import io.mobimenu.core.port.out.user.UserQueryOperationsPort;
import io.mobimenu.core.port.out.restaurant.RestaurantSaveOperationsPort;
import io.mobimenu.core.port.out.user.UserSaveOperationsPort;
import io.mobimenu.domain.enums.AccountType;
import io.mobimenu.domain.Restaurant;
import io.mobimenu.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.utils.StringUtils;

import java.io.ByteArrayInputStream;
import java.util.Base64;

@Slf4j
@RequiredArgsConstructor
public class RestaurantService implements CreateRestaurantUseCase, UpdateRestaurantUseCase {

    private final RestaurantQueryOperationsPort restaurantQueryOperationsPort;
    private final RestaurantSaveOperationsPort restaurantSaveOperationsPort;
    private final UserQueryOperationsPort userQueryOperationsPort;
    private final UserSaveOperationsPort userSaveOperationsPort;
    private final PasswordSecurity passwordSecurity;
    private final StorageProvider storageProvider;

    @Override
    public Uni<Restaurant> createRestaurant(CreateRestaurantCommand command) {
        return userQueryOperationsPort.getByEmailAndAccountType(command.getEmailAddress(), AccountType.OWNER)
                .onItem().ifNotNull().failWith(Failure.of(Code.ACCOUNT_EXISTS))
                .chain(() -> restaurantQueryOperationsPort.getByName(command.getRestaurantName()))
                .onItem().ifNotNull().failWith(Failure.of(Code.RESTAURANT_EXISTS))
                .chain(() -> userSaveOperationsPort.saveUser(User.withRequiredFields(
                        command.getFirstName().trim(),
                        command.getLastName().trim(),
                        command.getEmailAddress().trim(),
                        AccountType.OWNER,
                        passwordSecurity.hash(command.getPassword()))))
                .flatMap(user -> restaurantSaveOperationsPort.saveRestaurant(Restaurant.withRequiredFields(
                        command.getRestaurantName(),
                        command.getRestaurantType()),
                        user.getUserId()))
                .flatMap(restaurant -> userSaveOperationsPort.linkRestaurantToUser(command.getEmailAddress(), restaurant.getRestaurantId(), AccountType.OWNER)
                        .map(ignored -> restaurant));
    }

    @Override
    public Uni<Restaurant> updateRestaurant(String restaurantId, UpdateRestaurantCommand updateRestaurantCommand) {
        return restaurantQueryOperationsPort.getById(restaurantId)
                .onItem().ifNull().failWith(Failure.of(Code.RESTAURANT_NOT_FOUND))
                .chain(() -> {
                    Uni<String> logoUri = Uni.createFrom().nullItem();
                    if (StringUtils.isNotBlank(updateRestaurantCommand.getLogo())) {
                        logoUri = uploadLogo(updateRestaurantCommand);
                    }
                    return logoUri.flatMap(url -> restaurantSaveOperationsPort.updateRestaurant(
                                    restaurantId,
                                    updateRestaurantCommand.getEmail(),
                                    updateRestaurantCommand.getAddress(),
                                    updateRestaurantCommand.getPhoneNumber(),
                                    updateRestaurantCommand.getSalesChannels(),
                                    new Theme(url,
                                            updateRestaurantCommand.getPrimaryColour(),
                                            updateRestaurantCommand.getAccentColour())));
                });
    }

    private Uni<String> uploadLogo(UpdateRestaurantCommand updateRestaurantCommand) {
        var arr = updateRestaurantCommand.getLogo().split("base64,");
        String logo = arr[1];
        var bytes = Base64.getDecoder().decode(logo);
        String contentType = arr[0].split(":")[1];

        var storageRequest = new StorageRequest(IdentifierProvider.uuid("logo"),
                new ByteArrayInputStream(bytes),
                bytes.length, contentType, null);
        var uploadedFile = storageProvider.uploadFile(StorageFolder.LOGO, storageRequest);
        return uploadedFile.map(StorageResponse::url);
    }
}
