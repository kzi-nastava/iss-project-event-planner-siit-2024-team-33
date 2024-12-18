package rs.ac.uns.ftn.asd.Projekatsiit2024.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.BudgetItem;

@Repository
public interface BudgetItemRepository extends JpaRepository<BudgetItem,Integer>{
	@Query("SELECT bi FROM BudgetItem bi WHERE bi.event.id=:eventId AND bi.budgetCategory.id=:categoryId")
	public BudgetItem findByEventAndCategory(@Param("eventId") Integer eventId, @Param("categoryId") Integer categoryId);
	
	@Query("SELECT bi FROM BudgetItem bi WHERE bi.event.id=:eventId")
	public Set<BudgetItem> findByEventId(@Param("eventId") Integer eventId);
}
