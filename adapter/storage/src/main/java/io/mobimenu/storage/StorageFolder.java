package io.mobimenu.storage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StorageFolder {

    QR_CODE("qrcodes"),
    MEALS("meals"),
    LOGO("logos");


    private final String path;

}
