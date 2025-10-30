package upc.edu.muusmart.campaignmanagement.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignResponse {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
    private Long userId;
    private Long stableId;
    private List<GoalResponse> goals;
    private List<ChannelResponse> channels;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
