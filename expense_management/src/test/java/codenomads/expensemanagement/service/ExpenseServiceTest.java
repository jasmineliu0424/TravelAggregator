package codenomads.expensemanagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import codenomads.expensemanagement.domain.Expense;
import codenomads.expensemanagement.dto.AddExpenseRequest;
import codenomads.expensemanagement.gateway.TripGateway;
import codenomads.expensemanagement.repository.ExpenseRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;
    
    @Mock
    private TripGateway tripGateway;

    @InjectMocks
    private ExpenseService expenseService;

    private AddExpenseRequest testRequest;
    private Expense testExpense;

    @BeforeEach
    void setUp() {
        testRequest = createTestRequest();
        testExpense = createTestExpense();
    }

    @Test
    void addExpenseValidRequestShouldSaveExpense() {
        when(tripGateway.isUserInTrip(1L, 1L)).thenReturn(true);
        when(tripGateway.isUserInTrip(1L, 2L)).thenReturn(true);
        when(expenseRepository.save(any(Expense.class))).thenReturn(testExpense);

        Expense result = expenseService.addExpense(testRequest);

        assertNotNull(result);
        assertEquals(testExpense.getId(), result.getId());
        verify(tripGateway, times(2)).isUserInTrip(anyLong(), anyLong());
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }

    @Test
    void updateExpenseValidIdShouldUpdateAmount() {
        BigDecimal newAmount = new BigDecimal("200.00");
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(testExpense));
        when(expenseRepository.save(any(Expense.class))).thenReturn(testExpense);

        Expense result = expenseService.updateExpense(1L, newAmount);

        assertNotNull(result);
        verify(expenseRepository, times(1)).findById(1L);
        verify(expenseRepository, times(1)).save(argThat(expense -> 
            expense.getAmount().equals(newAmount)
        ));
    }

    @Test
    void updateExpenseInvalidIdShouldThrowException() {
        when(expenseRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> expenseService.updateExpense(999L, new BigDecimal("200.00")));

        assertEquals("Expense not found", exception.getMessage());
        verify(expenseRepository, never()).save(any(Expense.class));
    }

    private AddExpenseRequest createTestRequest() {
        AddExpenseRequest request = new AddExpenseRequest();
        request.setTripId(1L);
        request.setAmount(new BigDecimal("100.00"));
        request.setDescription("Test expense");
        request.setResponsibleUserIds(Set.of(1L, 2L));
        request.setSource(Expense.ExpenseSource.HOTEL);
        return request;
    }

    private Expense createTestExpense() {
        Expense expense = new Expense();
        expense.setId(1L);
        expense.setTripId(1L);
        expense.setAmount(new BigDecimal("100.00"));
        expense.setDescription("Test expense");
        expense.setResponsibleUserIds(Set.of(1L, 2L));
        expense.setSource(Expense.ExpenseSource.HOTEL);
        return expense;
    }

    private Expense createExpenseWithAmount(BigDecimal amount, Set<Long> userIds) {
        Expense expense = new Expense();
        expense.setId(1L);
        expense.setTripId(1L);
        expense.setAmount(amount);
        expense.setDescription("Test expense");
        expense.setResponsibleUserIds(userIds);
        expense.setSource(Expense.ExpenseSource.HOTEL);
        return expense;
    }
}