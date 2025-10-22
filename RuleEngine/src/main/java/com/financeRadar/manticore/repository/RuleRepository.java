package com.financeRadar.manticore.repository;

import com.financeRadar.manticore.entity.Rule;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для взаимодействия с {@link Rule}
 *
 * @author Linempy
 * @since 19.10.2025
 */
public interface RuleRepository extends JpaRepository<Rule, Long> {

    List<Rule> findAllByEnabledTrue();

    Optional<List<Rule>> findAllByUpdatedAtAfterAndEnabledTrue(LocalDateTime updatedAt);

    default Rule findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Правило %d не было найдено", id)));
    }
}