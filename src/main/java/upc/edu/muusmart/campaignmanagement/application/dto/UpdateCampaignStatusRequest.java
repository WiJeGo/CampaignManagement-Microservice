package upc.edu.muusmart.campaignmanagement.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCampaignStatusRequest {

    @NotBlank(message = "Status is required")
    private String status;

}
