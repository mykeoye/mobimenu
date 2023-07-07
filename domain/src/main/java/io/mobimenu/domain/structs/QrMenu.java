package io.mobimenu.domain.structs;

import io.mobimenu.domain.Theme;

/**
 * A simple data transfer object for returning the active menu a qrcode should link to
 */
public record QrMenu(
        String qrCodeId,
        String tableNumber,
        String menuId,
        Theme theme,
        String restaurantId) {
}
