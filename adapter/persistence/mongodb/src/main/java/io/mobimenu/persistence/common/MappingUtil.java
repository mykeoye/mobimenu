package io.mobimenu.persistence.common;

import org.bson.types.ObjectId;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class MappingUtil {

    public static Set<ObjectId> toObjectIds(Collection<String> ids) {
        return ids.stream().map(ObjectId::new).collect(Collectors.toSet());
    }
}
