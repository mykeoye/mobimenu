package io.mobimenu.persistence.context;

import io.mobimenu.domain.enums.AccountType;
import io.mobimenu.domain.Restaurant;
import io.mobimenu.domain.Theme;
import io.mobimenu.domain.enums.ThemeType;
import io.mobimenu.domain.User;
import org.bson.types.ObjectId;

public class TestObject {

    public static Restaurant restaurant() {
        return restaurant("Dominos Pizza");
    }

    public static Restaurant restaurant(String name) {
        return Restaurant.withRequiredFields(name, Restaurant.Type.CHAIN);
    }

    public static String userId() {
        return ObjectId.get().toString();
    }

    public static User user() {
        return User.withRequiredFields("Jeff",
                "Bezos",
                "bezos@amazon.com",
                AccountType.OWNER,
                "password");
    }

    public static Theme theme() {
        return new Theme("#ffffff", "#af48cb", "http:://sm.jpg", "", ThemeType.SYSTEM);
    }
}
