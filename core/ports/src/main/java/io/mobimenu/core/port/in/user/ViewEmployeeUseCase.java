package io.mobimenu.core.port.in.user;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.mobimenu.common.api.Page;
import io.mobimenu.domain.User;
import java.util.List;

public interface ViewEmployeeUseCase {

    Uni<User> loadSingleEmployee(String id);

    Uni<Tuple2<Long, List<User>>> loadAllEmployeesByRestaurant(String restaurantId, Page page);
}
