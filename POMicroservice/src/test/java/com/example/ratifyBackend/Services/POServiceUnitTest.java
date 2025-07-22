package com.example.ratifyBackend.Services;

import com.example.POMicroservice.DTO.POItemNumber;
import com.example.POMicroservice.Domain.PO;
import com.example.POMicroservice.Repositories.PORepository;
import com.example.POMicroservice.Services.POService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class POServiceUnitTest {

    @Mock
    private PORepository poRepository;

    @InjectMocks
    private POService poService;

    @Test
    void getAllPO_shouldReturnAllPOs() {
        List<PO> poList = List.of(
                new PO("PO1", "PO1-1", "Company3", "Item", "Kg", 20, 10.00, LocalDateTime.now(), "Awaiting-Approval", "JohnDepartmentAnalyst@cookfood.com", "", LocalDateTime.now()),
                new PO("PO1", "PO1-2", "Company3", "Item", "Kg", 20, 10.00, LocalDateTime.now(), "Awaiting-Approval", "JohnDepartmentAnalyst@cookfood.com", "", LocalDateTime.now())
        );

        when(poRepository.findAll()).thenReturn(poList);

        List<PO> result = poService.getAllPO();

        assertThat(result).hasSize(2).containsExactlyElementsOf(poList);
        verify(poRepository).findAll();
    }

    @Test
    void getAllNonApprovedPO_shouldReturnOnlyAwaitingApprovalPOs() {
        List<PO> awaitingList = List.of(
                new PO("PO1", "PO1-1", "Company3", "Item", "Kg", 20, 10.00, LocalDateTime.now(), "awaiting-approval", "JohnDepartmentAnalyst@cookfood.com", "", LocalDateTime.now())
        );

        when(poRepository.findByStatus("awaiting-approval")).thenReturn(awaitingList);

        List<PO> result = poService.getAllNonApprovedPO();

        assertThat(result).hasSize(1).containsExactlyElementsOf(awaitingList);
        verify(poRepository).findByStatus("awaiting-approval");
    }

    @Test
    void getAllApprovedPO_shouldReturnOnlyApprovedPOs() {
        List<PO> approvedList = List.of(
                new PO("PO1", "PO1-1", "Company3", "Item", "Kg", 20, 10.00, LocalDateTime.now(), "approved", "JohnDepartmentAnalyst@cookfood.com", "", LocalDateTime.now())
        );

        when(poRepository.findByStatus("approved")).thenReturn(approvedList);

        List<PO> result = poService.getAllApprovedPO();

        assertThat(result).hasSize(1).containsExactlyElementsOf(approvedList);
        verify(poRepository).findByStatus("approved");
    }

    @Test
    void approvePOs_shouldCallRepositoryWithEachPOItemNumber() {
        List<POItemNumber> itemNumbers = List.of(
                new POItemNumber("PO1-01"),
                new POItemNumber("PO1-02")
        );
        String approverEmail = "approver@example.com";

        poService.approvePOs(itemNumbers, approverEmail);

        verify(poRepository, times(2)).approvePOsByItemNumbers(anyString(), eq(approverEmail), any(LocalDateTime.class));
    }





}

