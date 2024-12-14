package at.cgsit.demo.repository;

import at.cgsit.demo.model.TestEntity;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class TestEntityRepositoryTest {

    @Inject
    TestEntityRepository repository;

    @Inject
    Logger logger;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void countChatMessags() {
        Long l = repository.countChatMessags();
        logger.infov("anzahl entitäten {0}", l);
        assertNotNull(l);
    }

    @Test
    void insertTestEntity() {
        TestEntity testEntity = new TestEntity();
        testEntity.setName("Quarkus-Test");
        repository.insertTestEntity(testEntity);
    }

    @Test
    void findTestEntity() {
    }

    @Test
    void readTestEntityById() {
    }

    @Test
    void findAll() {
    }
}