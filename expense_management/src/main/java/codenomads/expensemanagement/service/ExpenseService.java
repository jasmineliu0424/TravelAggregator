package codenomads.expensemanagement.service;

import codenomads.expensemanagement.domain.Expense;
import codenomads.expensemanagement.dto.AddExpenseRequest;
import codenomads.expensemanagement.repository.ExpenseRepository;
import codenomads.expensemanagement.gateway.TripGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final TripGateway tripGateway;

    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository, TripGateway tripGateway) {
        this.expenseRepository = expenseRepository;
        this.tripGateway = tripGateway;
    }

    @Transactional
    public Expense addExpense(@Valid AddExpenseRequest addExpenseRequest) {
        // Verify that all responsible users belong to the trip
        for (Long userId : addExpenseRequest.getResponsibleUserIds()) {
            if (!tripGateway.isUserInTrip(addExpenseRequest.getTripId(), userId)) {
                throw new IllegalArgumentException("User " + userId + " is not part of the trip " + addExpenseRequest.getTripId());
            }
        }

        Expense expense = new Expense();
        expense.setTripId(addExpenseRequest.getTripId());
        expense.setAmount(addExpenseRequest.getAmount());
        expense.setDescription(addExpenseRequest.getDescription());
        expense.setResponsibleUserIds(addExpenseRequest.getResponsibleUserIds());
        expense.setSource(addExpenseRequest.getSource());

        return expenseRepository.save(expense);
    }

    public Expense updateExpense(Long id, BigDecimal amount) {
        Expense expense = expenseRepository.findById(id).orElseThrow(() -> new RuntimeException("Expense not found"));
        expense.setAmount(amount);
        return expenseRepository.save(expense);
    }

    public void removeExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    public List<Expense> queryExpenses() {
        return expenseRepository.findAll();
    }

    public BigDecimal queryExpensesForUserInTrip(Long tripId, Long userId) {
        // Fetch all expenses for the given trip
        List<Expense> expenses = expenseRepository.findByTripId(tripId);

        return expenses.stream()
            .filter(expense -> expense.getResponsibleUserIds().contains(userId))
            .map(expense -> expense.getAmount().divide(BigDecimal.valueOf(expense.getResponsibleUserIds().size())))
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .setScale(2, RoundingMode.HALF_UP);
    }
}