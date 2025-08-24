package com.example.POMicroservice.Services;

import com.example.POMicroservice.DTO.POItemNumber;
import com.example.POMicroservice.Domain.PO;
import com.example.POMicroservice.Repositories.PORepository;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Transactional

class POServiceIntegrationTest {

    private final PORepository poRepository;
    private final POService poService;

    @PersistenceContext
    private EntityManager entityManager;

    public POServiceIntegrationTest(PORepository poRepository, POService poService) {
        this.poRepository = poRepository;
        this.poService = poService;
    }

    @BeforeEach
    void setUp() {
        poRepository.deleteAll();

        // Add test POs
        poRepository.save(new PO("PO1", "PO1-01", "Company3", "Item", "Kg", 20, 10.00, LocalDateTime.now(), "JohnDepartmentAnalyst@cookfood.com",  "awaiting-approval",  ""));
        poRepository.save(new PO("PO1", "PO1-02", "Company2", "Item", "Kg", 20, 10.00, LocalDateTime.now(), "JohnDepartmentAnalyst@cookfood.com", LocalDateTime.now(), "JohnDepartmentAnalyst@cookfood.com", "approved",  ""));
    }

    @Test
    void getAllPO_returnsAllRecords() {
        List<PO> all = poService.getAllPO();
        assertThat(all).hasSize(2);
    }

    @Test
    void getAllApprovedPO_returnsOnlyApproved() {
        List<PO> approved = poService.getAllApprovedPO();
        assertThat(approved).hasSize(1);
        assertThat(approved.get(0).getStatus()).isEqualTo("approved");
    }

    @Test
    void getAllNonApprovedPO_returnsOnlyAwaiting() {
        List<PO> unapproved = poService.getAllNonApprovedPO();
        assertThat(unapproved).hasSize(1);
        assertThat(unapproved.get(0).getStatus()).isEqualTo("awaiting-approval");
    }

    @Test
    void approvePOs_shouldUpdateStatus() {
        POItemNumber item = new POItemNumber("PO1-01");
        poService.approvePOs(List.of(item), "admin@cookfood.com");

        entityManager.flush(); //Forces pending changes to be written to the database
        entityManager.clear(); //Detaches all entities from the persistence content so the findById() loads fresh from the db

        PO updated = poRepository.findById("PO1-01").orElseThrow();
        assertThat(updated.getStatus()).isEqualTo("Approved");
        assertThat(updated.getApprovedBy()).isEqualTo("admin@cookfood.com");
        assertThat(updated.getDateApproved()).isNotNull();
    }
}
