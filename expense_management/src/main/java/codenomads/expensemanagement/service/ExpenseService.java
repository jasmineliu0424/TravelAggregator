package codenomads.expensemanagement.service;

import codenomads.expensemanagement.domain.Expense;
import codenomads.expensemanagement.dto.AddExpenseRequest;
import codenomads.expensemanagement.dto.UserExpenseSummary;
import codenomads.expensemanagement.dto.BalanceSheet;
import codenomads.expensemanagement.repository.ExpenseRepository;
import codenomads.expensemanagement.gateway.TripGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        expense.setCreatedByUserId(addExpenseRequest.getCreatedByUserId());
        expense.setOccurredOn(addExpenseRequest.getOccurredOn() != null ? 
            addExpenseRequest.getOccurredOn() : LocalDate.now());

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
        if (tripId == null || userId == null) {
          throw new IllegalArgumentException("tripId and userId are required");
        }
        // Fetch all expenses for the given trip
        List<Expense> expenses = expenseRepository.findByTripId(tripId);

        return expenses.stream()
            .filter(expense -> expense.getResponsibleUserIds().contains(userId))
            .map(expense -> expense.getAmount().divide(BigDecimal.valueOf(expense.getResponsibleUserIds().size())))
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .setScale(2, RoundingMode.HALF_UP);
    }

    public UserExpenseSummary getUserSummary(Long tripId, Long userId, LocalDate fromInclusive, LocalDate toExclusive) {
        if (tripId == null || userId == null || fromInclusive == null || toExclusive == null) {
            throw new IllegalArgumentException("All parameters must be non-null");
        }
        if (fromInclusive.isAfter(toExclusive)) {
            throw new IllegalArgumentException("fromInclusive must be before or equal to toExclusive");
        }

        List<Expense> expenses = expenseRepository.findByTripIdAndOccurredOnBetween(tripId, fromInclusive, toExclusive);

        BigDecimal paidByUser = expenses.stream()
            .filter(expense -> userId.equals(expense.getCreatedByUserId()))
            .map(Expense::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal shareOfUser = expenses.stream()
            .filter(expense -> expense.getResponsibleUserIds().contains(userId))
            .map(expense -> expense.getAmount().divide(
                BigDecimal.valueOf(expense.getResponsibleUserIds().size()), 
                10, 
                RoundingMode.HALF_UP))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal net = paidByUser.subtract(shareOfUser);

        Map<Expense.ExpenseSource, BigDecimal> byCategory = new HashMap<>();
        for (Expense.ExpenseSource source : Expense.ExpenseSource.values()) {
            BigDecimal categorySum = expenses.stream()
                .filter(expense -> expense.getResponsibleUserIds().contains(userId))
                .filter(expense -> source.equals(expense.getSource()))
                .map(expense -> expense.getAmount().divide(
                    BigDecimal.valueOf(expense.getResponsibleUserIds().size()), 
                    10, 
                    RoundingMode.HALF_UP))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            byCategory.put(source, categorySum.setScale(2, RoundingMode.HALF_UP));
        }

        return new UserExpenseSummary(
            paidByUser.setScale(2, RoundingMode.HALF_UP),
            shareOfUser.setScale(2, RoundingMode.HALF_UP),
            net.setScale(2, RoundingMode.HALF_UP),
            byCategory
        );
    }

    public BalanceSheet getBalanceSheet(Long tripId) {
        if (tripId == null) {
            throw new IllegalArgumentException("tripId cannot be null");
        }

        List<Expense> expenses = expenseRepository.findByTripId(tripId);
        
        Set<Long> participants = new HashSet<>();
        for (Expense expense : expenses) {
            participants.add(expense.getCreatedByUserId());
            participants.addAll(expense.getResponsibleUserIds());
        }

        Map<Long, BigDecimal> netByUser = new HashMap<>();

        for (Long userId : participants) {
            BigDecimal paidByUser = expenses.stream()
                .filter(expense -> userId.equals(expense.getCreatedByUserId()))
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal shareOfUser = expenses.stream()
                .filter(expense -> expense.getResponsibleUserIds().contains(userId))
                .map(expense -> expense.getAmount().divide(
                    BigDecimal.valueOf(expense.getResponsibleUserIds().size()), 
                    10, 
                    RoundingMode.HALF_UP))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal net = paidByUser.subtract(shareOfUser);
            netByUser.put(userId, net.setScale(2, RoundingMode.HALF_UP));
        }

        return new BalanceSheet(netByUser);
    }
}
