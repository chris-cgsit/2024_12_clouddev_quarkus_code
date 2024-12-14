package at.cgsit.demo.init;

import at.cgsit.demo.model.TestEntity;
import at.cgsit.demo.repository.TestEntityRepository;
import io.quarkus.runtime.StartupEvent;

import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.annotation.Priority;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;
import java.util.Random;

@Singleton
public class AppInitializer {

    @Inject
    Logger log;

    @Inject
    EntityManager em;

    @Inject
    TestEntityRepository repository;

    @Transactional
    public void loadUsers(@Observes @Priority(2) StartupEvent evt) {

        log.infov("server Startup Event {0}", evt);

        Long l = repository.countChatMessags();

        if( l == 0) {
        for (Integer i = 1; i<=5; i++) {

            TestEntity entity = new TestEntity();
            entity.setName("username " + i.toString());
            repository.insertTestEntity(entity);
        }
        }

    }
}
