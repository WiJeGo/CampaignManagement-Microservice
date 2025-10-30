package upc.edu.muusmart.campaignmanagement.domain.repository;

import jakarta.validation.constraints.NotBlank;
import upc.edu.muusmart.campaignmanagement.domain.model.aggregates.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    List<Campaign> findByUserId(Long userId);
    List<Campaign> findByStableId(Long stableId);

    Optional<Object> findByName(@NotBlank(message = "Campaign name is required") String name);
}
