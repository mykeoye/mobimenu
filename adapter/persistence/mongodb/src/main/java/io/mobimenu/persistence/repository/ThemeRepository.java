package io.mobimenu.persistence.repository;

import java.util.List;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Uni;
import io.mobimenu.domain.enums.ThemeType;
import io.mobimenu.persistence.common.Fields;
import io.mobimenu.persistence.entity.ThemeEntity;
import javax.enterprise.context.ApplicationScoped;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import org.bson.types.ObjectId;

@ApplicationScoped
public class ThemeRepository implements ReactivePanacheMongoRepository<ThemeEntity> {

    public Uni<ThemeEntity> findByThemeId(String themeId) {
        return findById(new ObjectId(themeId));
    }

    public Uni<List<ThemeEntity>> findAllSystemDefined(int page, int limit) {
        return find(Fields.TYPE, ThemeType.SYSTEM).page(Page.of(page, limit)).list();
    }

    public Uni<Long> countAllSystemDefined() {
        return find(Fields.TYPE, ThemeType.SYSTEM).count();
    }

}
