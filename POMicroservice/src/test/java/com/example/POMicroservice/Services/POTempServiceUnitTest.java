package com.example.POMicroservice.Services;

import com.example.POMicroservice.DTO.EditPOTempRequest;
import com.example.POMicroservice.Domain.PO;
import com.example.POMicroservice.Domain.POTemp;
import com.example.POMicroservice.Repositories.PORepository;
import com.example.POMicroservice.Repositories.POTempRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class POTempServiceUnitTest {

    @Mock
    private POTempRepository poTempRepository;

    @Mock
    private PORepository poRepository;

    @InjectMocks
    private POTempService poTempService;

    @InjectMocks
    private POService poService;

    @Test
    void returnAllPOTemp() {
        POTemp PO1 = new POTemp("JohnDepartmentAnalyst@cookfood.com", "Company1", "Item", "Kg", 20, 10.00, "");
        POTemp PO2 = new POTemp("JohnDepartmentAnalyst@cookfood.com", "Company2", "Item", "Kg", 20, 10.00, "");
        POTemp PO3 = new POTemp("JohnDepartmentAnalyst@cookfood.com", "Company3", "Item", "Kg", 20, 10.00, "");

        List<POTemp> mockList = List.of(PO1, PO2, PO3);

        when(poTempRepository.findAll()).thenReturn(mockList);

        List<POTemp> result = poTempService.getAllPOTemp();

        assertThat(result).hasSize(3);
    }

    @Test
    void createPOTemp() {

        POTemp PO3 = new POTemp("JohnDepartmentAnalyst@cookfood.com", "Company3", "Item", "Kg", 20, 10.00,"");

        // Simulate save call (optional if you don't assert anything from it)
        when(poTempRepository.save(any(POTemp.class))).thenReturn(PO3);

        // Simulate what getAllTempPO should return
        when(poTempRepository.findAll()).thenReturn(List.of(PO3));

        // Save call will be inside createPOTemp()
        poTempService.createPOTemp(PO3);

        // Findall call will be inside getAllTempPO()
        List<POTemp> result = poTempService.getAllPOTemp();

        assertThat(result).hasSize(1);
    }

    @Test
    void CopyTempPOBasket() {

        POTemp PO1 = new POTemp("JohnDepartmentAnalyst@cookfood.com", "Company3", "Item", "Kg", 20, 10.00, "");

        // Simulate save call (optional if you don't assert anything from it)
        when(poTempRepository.save(any(POTemp.class))).thenReturn(PO1);

        // Simulate what getAllTempPO should return
        when(poTempRepository.findAll()).thenReturn(List.of(PO1));

        // Save call will be inside createPOTemp()
        poTempService.createPOTemp(PO1);

        // Find all call will be inside getAllTempPO()
        List<POTemp> result1 = poTempService.getAllPOTemp();

        assertThat(result1).hasSize(1);

        PO PO2 = new PO("PO1", "PO1-01", "Company3", "Item", "Kg", 20, 10.00, LocalDateTime.now(), "JohnDepartmentAnalyst@cookfood.com",  "awaiting-approval",  "");

        List<PO> poList = List.of(PO2);

        // Simulate what getAllTempPO should return
        when(poRepository.findAll()).thenReturn(poList);

        // Simulate save call (optional if you don't assert anything from it)
        when(poRepository.saveAll(anyList())).thenReturn(poList);

        // Call Copy table from TempPO to PO table
        poTempService.CopyTempPOBasket();

        // Find all call will be inside getAllTempPO()
        List<PO> result2 = poService.getAllPO();

        assertThat(result2).hasSize(1);
    }

    @Test
    void deletePOTemp_shouldReindexRemainingItems() {
        // Given: initial data
        POTemp po1 = new POTemp("JohnDepartmentAnalyst@cookfood.com", "Company1", "Item", "Kg", 20, 10.00, "");
        po1.setPoitemnumber("PO-01");

        POTemp po2 = new POTemp("JohnDepartmentAnalyst@cookfood.com", "Company2", "Item", "Kg", 20, 10.00, "");
        po2.setPoitemnumber("PO-02"); // <-- To be deleted

        POTemp po3 = new POTemp("JohnDepartmentAnalyst@cookfood.com", "Company3", "Item", "Kg", 20, 10.00, "");
        po3.setPoitemnumber("PO-03");

        // When deletePOTemp("PO002") is called, po2 will be deleted, and po3 becomes PO002

        List<POTemp> remainingAfterDelete = List.of(po1, po3); // PO2 gone

        when(poTempRepository.findAll()).thenReturn(remainingAfterDelete);

        // When: we call deletePOTemp
        poTempService.deletePOTemp("PO-02");

        // Then: verify deleteById was called correctly
        verify(poTempRepository).deleteById("PO-02");

        // Then: verify PO003 was deleted and re-saved as PO002
        ArgumentCaptor<POTemp> saveCaptor = ArgumentCaptor.forClass(POTemp.class);
        verify(poTempRepository).delete(po3);
        verify(poTempRepository).save(saveCaptor.capture());

        POTemp reindexedPO = saveCaptor.getValue();
        assertThat(reindexedPO.getPoitemnumber()).isEqualTo("PO-02");
    }

    @Test
    void updatePOTemp_shouldUpdateCompanyAndSave() {
        // Arrange: Create initial POTemp
        POTemp existingPO = new POTemp("JohnDepartmentAnalyst@cookfood.com", "OldCompany", "Item", "Kg", 10, 5.0, "");
        existingPO.setPoitemnumber("PO-01");

        // Create a mock request
        EditPOTempRequest request = mock(EditPOTempRequest.class);
        when(request.getField()).thenReturn("company");
        when(request.getNewValue()).thenReturn("NewCompany");

        // Mock findByPoitemnumber to return the existing PO
        when(poTempRepository.findByPoitemnumber("PO-01")).thenReturn(Optional.of(existingPO));

        // Act
        poTempService.updatePOTemp(request, "PO-01");

        // Assert: capture saved object and verify updates
        ArgumentCaptor<POTemp> captor = ArgumentCaptor.forClass(POTemp.class);
        verify(poTempRepository).save(captor.capture());

        POTemp updated = captor.getValue();
        assertThat(updated.getCompany()).isEqualTo("NewCompany");
    }


}

