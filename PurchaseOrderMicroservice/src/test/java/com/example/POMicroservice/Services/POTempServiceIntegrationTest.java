package com.example.POMicroservice.Services;

import com.example.POMicroservice.DTO.EditPOTempRequest;
import com.example.POMicroservice.Domain.PO;
import com.example.POMicroservice.Domain.POTemp;
import com.example.POMicroservice.Repositories.PORepository;
import com.example.POMicroservice.Repositories.POTempRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class POTempServiceIntegrationTest {

    private final POTempService poTempService;
    private final PORepository poRepository;
    private final POTempRepository poTempRepository;

    public POTempServiceIntegrationTest(POTempRepository poTempRepository, POTempService poTempService, PORepository poRepository) {
        this.poTempRepository = poTempRepository;
        this.poTempService = poTempService;
        this.poRepository = poRepository;
    }

    @BeforeEach
    void setUp() {
        poTempRepository.deleteAll();
        poRepository.deleteAll();
    }

    @Test
    void getAllTempPO_returnsAllRecords() {

        poTempService.createPOTemp(new POTemp("JohnDepartmentAnalyst@cookfood.com","Company1", "Item", "Kg", 20, 10.00, ""));
        poTempService.createPOTemp(new POTemp("JohnDepartmentAnalyst@cookfood.com","Company2", "Item", "Kg", 20, 10.00, ""));
        poTempService.createPOTemp(new POTemp("JohnDepartmentAnalyst@cookfood.com","Company3", "Item", "Kg", 20, 10.00, ""));

        List<POTemp> result = poTempService.getAllPOTemp();
        assertThat(result).hasSize(3);
    }

    @Test
    void createPOTemp_shouldGenerateAndPersistNewTempPO() {
        POTemp newPO = new POTemp("JohnDepartmentAnalyst@cookfood.com","Company1", "Item", "Kg", 20, 10.00, "");

        POTemp savedPO = poTempService.createPOTemp(newPO);

        // Check saved
        List<POTemp> all = poTempRepository.findAll();
        assertThat(all).hasSize(1);

        // Validate PO fields
        assertThat(savedPO.getPonumber()).startsWith("PO");
        assertThat(savedPO.getPoitemnumber()).contains("-");
        assertThat(savedPO.getDateRaised()).isNotNull();

        // Validate persisted object is same as returned
        POTemp persisted = all.get(0);
        assertThat(persisted.getPonumber()).isEqualTo(savedPO.getPonumber());
        assertThat(persisted.getPoitemnumber()).isEqualTo(savedPO.getPoitemnumber());
    }

    @Test
    void copyTempPOBasket_shouldTransferAndClearTemp() {

        poTempService.createPOTemp(new POTemp("JohnDepartmentAnalyst@cookfood.com","Company1", "Item", "Kg", 20, 10.00, ""));
        poTempService.createPOTemp(new POTemp("JohnDepartmentAnalyst@cookfood.com","Company2", "Item", "Kg", 20, 10.00, ""));
        poTempService.createPOTemp(new POTemp("JohnDepartmentAnalyst@cookfood.com","Company3", "Item", "Kg", 20, 10.00, ""));

        poTempService.CopyTempPOBasket();

        List<PO> poList = poRepository.findAll();
        List<POTemp> tempAfter = poTempRepository.findAll();

        assertThat(poList).hasSize(3);
        assertThat(tempAfter).isEmpty();
    }

    @Test
    void deletePOTemp_shouldDeleteAndReindex() {

        poTempService.createPOTemp(new POTemp("JohnDepartmentAnalyst@cookfood.com","Company1", "Item", "Kg", 20, 10.00,""));
        poTempService.createPOTemp(new POTemp("JohnDepartmentAnalyst@cookfood.com","Company2", "Item", "Kg", 20, 10.00, ""));
        poTempService.createPOTemp(new POTemp("JohnDepartmentAnalyst@cookfood.com","Company3", "Item", "Kg", 20, 10.00, ""));

        // Delete second item PO01-02
        poTempService.deletePOTemp("PO01-02");

        List<POTemp> result = poTempRepository.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).noneMatch(po -> po.getPoitemnumber().equals("PO01-03"));
        assertThat(result).anyMatch(po -> po.getPoitemnumber().equals("PO01-02")); // PO01-03 becomes PO01-02
    }

    @Test
    void updatePOTemp_shouldChangeField() {

        poTempService.createPOTemp(new POTemp("JohnDepartmentAnalyst@cookfood.com","Company1", "Item", "Kg", 20, 10.00, ""));
        poTempService.createPOTemp(new POTemp("JohnDepartmentAnalyst@cookfood.com","Company2", "Item", "Kg", 20, 10.00, ""));
        poTempService.createPOTemp(new POTemp("JohnDepartmentAnalyst@cookfood.com","Company3", "Item", "Kg", 20, 10.00, ""));

        EditPOTempRequest updateRequest = new EditPOTempRequest();
        updateRequest.setField("company");
        updateRequest.setNewValue("UpdatedCompany");

        poTempService.updatePOTemp(updateRequest, "PO01-01");

        POTemp updated = poTempRepository.findByPoitemnumber("PO01-01").orElseThrow();
        assertThat(updated.getCompany()).isEqualTo("UpdatedCompany");
    }

}