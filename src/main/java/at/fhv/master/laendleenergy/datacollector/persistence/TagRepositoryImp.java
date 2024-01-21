package at.fhv.master.laendleenergy.datacollector.persistence;

import at.fhv.master.laendleenergy.datacollector.model.Tag;
import at.fhv.master.laendleenergy.datacollector.model.repositories.TagRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class TagRepositoryImp implements TagRepository {

    @Inject
    EntityManager eM;

    @Override
    public void saveTag(Tag tag) {
            eM.persist(tag);
    }
}
