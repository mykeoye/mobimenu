package io.mobimenu.web.request;

import io.mobimenu.common.validation.ValidImage;
import io.mobimenu.core.port.in.restaurant.UpdateRestaurantUseCase;
import io.mobimenu.domain.PhoneNumber;
import io.mobimenu.domain.enums.SalesChannel;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

public record UpdateRestaurantRequest(
        @NotNull
        PhoneNumber phoneNumber,
        @NotBlank
        @Email
        String email,
        @NotBlank
        String address,
        @NotEmpty
        Set<SalesChannel> salesChannels,
        @ValidImage
        String logo,
        String primaryColour,
        String accentColour
) {
    public UpdateRestaurantUseCase.UpdateRestaurantCommand toCommand() {
                return UpdateRestaurantUseCase.UpdateRestaurantCommand.builder()
                        .accentColour(accentColour)
                        .address(address)
                        .email(email)
                        .logo(logo)
                        .phoneNumber(phoneNumber)
                        .primaryColour(primaryColour)
                        .salesChannels(salesChannels)
                        .build();
    }
}


