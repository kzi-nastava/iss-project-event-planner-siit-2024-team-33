package rs.ac.uns.ftn.asd.Projekatsiit2024.budget;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.persistence.EntityNotFoundException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.UserPrincipal;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.BudgetItem;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Offer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferReservation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Organizer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.offer.BudgetItemRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.offer.OfferCategoryRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.offer.OfferReservationRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.offer.BudgetService;

public class BudgetPlanningServicesTest {
	@Mock
    private BudgetItemRepository budgetItemRepo;
    @Mock
    private OfferReservationRepository offerReservationRepo;
    @Mock
    private EventRepository eventRepo;
    @Mock
    private OfferCategoryRepository offerCategoryRepo;

    @InjectMocks
    private BudgetService budgetService;

    private Event event;
    private OfferCategory category;
    private UserPrincipal principal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        budgetService = spy(budgetService);

        // Fake user + security context
        Organizer organizer = new Organizer();
        organizer.setId(1);

        event = new Event();
        event.setId(10);
        event.setName("My Event");
        event.setOrganizer(organizer);

        category = new OfferCategory();
        category.setId(20);
        category.setName("Catering");

        principal = mock(UserPrincipal.class);
        when(principal.getUser()).thenReturn(organizer);

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(principal);

        SecurityContext ctx = mock(SecurityContext.class);
        when(ctx.getAuthentication()).thenReturn(auth);

        SecurityContextHolder.setContext(ctx);
    }

    @Test
    void createItem_success() {
        when(eventRepo.findById(10)).thenReturn(Optional.of(event));
        when(offerCategoryRepo.findById(20)).thenReturn(Optional.of(category));
        when(budgetItemRepo.findByEventAndCategory(10, 20)).thenReturn(null);

        BudgetItem saved = new BudgetItem();
        saved.setId(100);
        saved.setEvent(event);
        saved.setBudgetCategory(category);
        saved.setBudget(500.0);

        when(budgetItemRepo.saveAndFlush(any())).thenReturn(saved);

        BudgetItem result = budgetService.createItem(10, 20, 500.0);

        assertThat(result.getId()).isEqualTo(100);
        assertThat(result.getBudget()).isEqualTo(500.0);
        verify(budgetItemRepo).saveAndFlush(any(BudgetItem.class));
    }

    @Test
    void createItem_existingBudgetItem_throwsException() {
        when(budgetItemRepo.findByEventAndCategory(10, 20)).thenReturn(new BudgetItem());

        assertThatThrownBy(() -> budgetService.createItem(10, 20, 500.0))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void createItem_negativeBudget_throwsException() {
        assertThatThrownBy(() -> budgetService.createItem(10, 20, -10.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("negative");
    }

    @Test
    void createItem_nonexistentEvent_throwsException() {
        when(eventRepo.findById(10)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> budgetService.createItem(10, 20, 100.0))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void createItem_wrongUser_throwsException() {
        Organizer otherUser = new Organizer();
        otherUser.setId(999);
        event.setOrganizer(otherUser);

        when(eventRepo.findById(10)).thenReturn(Optional.of(event));
        when(offerCategoryRepo.findById(20)).thenReturn(Optional.of(category));
        when(budgetItemRepo.findByEventAndCategory(10, 20)).thenReturn(null);

        assertThatThrownBy(() -> budgetService.createItem(10, 20, 100.0))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void editItem_success() {
        BudgetItem bi = new BudgetItem();
        bi.setBudget(500.0);
        bi.setEvent(event);
        bi.setBudgetCategory(category);

        when(budgetItemRepo.findByEventAndCategory(10, 20)).thenReturn(bi);
        when(budgetItemRepo.saveAndFlush(any())).thenAnswer(invocation -> invocation.getArgument(0));

        BudgetItem result = budgetService.editItem(10, 20, 700.0);

        assertThat(result.getBudget()).isEqualTo(700.0);
        verify(budgetItemRepo).saveAndFlush(bi);
    }

    @Test
    void editItem_negativeBudget_throwsException() {
        BudgetItem bi = new BudgetItem();
        bi.setBudget(500.0);
        bi.setEvent(event);
        bi.setBudgetCategory(category);

        when(budgetItemRepo.findByEventAndCategory(10, 20)).thenReturn(bi);

        assertThatThrownBy(() -> budgetService.editItem(10, 20, -100.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("negative");
    }

    @Test
    void editItem_lowerThanUsedBudget_throwsException() {
        BudgetItem bi = spy(new BudgetItem());
        bi.setBudget(500.0);
        bi.setEvent(event);
        bi.setBudgetCategory(category);

        doReturn(300.0).when(budgetService).getUsedBudget(bi);

        when(budgetItemRepo.findByEventAndCategory(10, 20)).thenReturn(bi);

        assertThatThrownBy(() -> budgetService.editItem(10, 20, 200.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("lower than the total spend money");
    }

    @Test
    void editItem_nonexistentBudgetItem_throwsException() {
        when(budgetItemRepo.findByEventAndCategory(10, 20)).thenReturn(null);

        assertThatThrownBy(() -> budgetService.editItem(10, 20, 100.0))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void editItem_wrongUser_throwsException() {
        BudgetItem bi = new BudgetItem();
        bi.setBudget(500.0);

        Organizer otherUser = new Organizer();
        otherUser.setId(999);
        Event otherEvent = new Event();
        otherEvent.setOrganizer(otherUser);
        bi.setEvent(otherEvent);
        bi.setBudgetCategory(category);

        when(budgetItemRepo.findByEventAndCategory(10, 20)).thenReturn(bi);

        assertThatThrownBy(() -> budgetService.editItem(10, 20, 600.0))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void editItem_noLoggedUser_throwsException() {
        BudgetItem bi = new BudgetItem();
        bi.setBudget(500.0);
        bi.setEvent(event);
        bi.setBudgetCategory(category);

        when(budgetItemRepo.findByEventAndCategory(10, 20)).thenReturn(bi);

        // Simulate no user in SecurityContext
        SecurityContextHolder.clearContext();

        assertThatThrownBy(() -> budgetService.editItem(10, 20, 600.0))
                .isInstanceOf(AuthenticationCredentialsNotFoundException.class);
    }
    
    @Test
    void deleteItem_success() {
        BudgetItem bi = new BudgetItem();
        bi.setBudgetCategory(category);
        bi.setEvent(event);

        when(eventRepo.findById(10)).thenReturn(Optional.of(event));
        when(budgetItemRepo.findByEventAndCategory(10, 20)).thenReturn(bi);

        budgetService.deleteItem(10, 20);

        verify(budgetItemRepo).delete(bi);
        verify(budgetItemRepo).flush();
    }

    @Test
    void deleteItem_nonexistentEvent_throwsException() {
        when(eventRepo.findById(10)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> budgetService.deleteItem(10, 20))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void deleteItem_nonexistentBudgetItem_throwsException() {
        when(eventRepo.findById(10)).thenReturn(Optional.of(event));
        when(budgetItemRepo.findByEventAndCategory(10, 20)).thenReturn(null);

        assertThatThrownBy(() -> budgetService.deleteItem(10, 20))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void deleteItem_wrongUser_throwsException() {
        Organizer otherUser = new Organizer();
        otherUser.setId(999);
        event.setOrganizer(otherUser);

        when(eventRepo.findById(10)).thenReturn(Optional.of(event));

        assertThatThrownBy(() -> budgetService.deleteItem(10, 20))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void deleteItem_hasReservations_throwsException() {
        BudgetItem bi = new BudgetItem();
        bi.setBudgetCategory(category);
        bi.setEvent(event);

        // Fake that event has reservations with matching category
        OfferReservation res = mock(OfferReservation.class);
        when(res.getOffer()).thenReturn(mock(Offer.class));

        // Override getCategoryId to match category
        Offer offer = mock(Offer.class);
        when(offer.getCategoryId()).thenReturn(category.getId());
        when(res.getOffer()).thenReturn(offer);

        event.setReservations(List.of(res));

        when(eventRepo.findById(10)).thenReturn(Optional.of(event));
        when(budgetItemRepo.findByEventAndCategory(10, 20)).thenReturn(bi);

        assertThatThrownBy(() -> budgetService.deleteItem(10, 20))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("reservations");
    }

    @Test
    void deleteItem_noLoggedUser_throwsException() {
        BudgetItem bi = new BudgetItem();
        bi.setBudgetCategory(category);
        bi.setEvent(event);

        when(eventRepo.findById(10)).thenReturn(Optional.of(event));
        when(budgetItemRepo.findByEventAndCategory(10, 20)).thenReturn(bi);

        SecurityContextHolder.clearContext();

        assertThatThrownBy(() -> budgetService.deleteItem(10, 20))
                .isInstanceOf(AuthenticationCredentialsNotFoundException.class);
    }
}
