package io.mobimenu.web.common;

import io.mobimenu.common.util.AuthenticationTokenProvider;
import io.smallrye.jwt.build.Jwt;
import io.mobimenu.domain.User;
import io.mobimenu.persistence.common.Fields;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.jwt.Claims;
import java.time.Duration;
import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
public class JwtTokenProvider implements AuthenticationTokenProvider<User> {

    private final String issuer;

    public String generateToken(User user) {
        var restaurant = Optional.ofNullable(user.getRestaurants()).orElseGet(Collections::emptySet);
        return Jwt.issuer(issuer)
                .upn(user.getUserId())
                .claim(Claims.full_name.name(), "%s %s".formatted(user.getFirstName(), user.getLastName()))
                .claim(Fields.RESTAURANT_ID, !restaurant.isEmpty() ? restaurant.stream().findFirst().get() : "")
                .expiresIn(Duration.ofDays(1))
                .sign();
    }

}
