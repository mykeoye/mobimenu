package io.mobimenu.core.port.in.user;

import io.mobimenu.common.api.Page;
import io.mobimenu.domain.User;
import io.smallrye.mutiny.Uni;
import java.util.List;
import io.smallrye.mutiny.tuples.Tuple2;

public interface ViewCustomerUseCase {

    Uni<User> loadCustomerById(String userId);

    Uni<Tuple2<Long, List<User>>> loadCustomersByRestaurant(String restaurantId, Page page);

}
