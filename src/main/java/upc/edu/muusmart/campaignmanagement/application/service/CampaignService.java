package upc.edu.muusmart.campaignmanagement.application.service;

import upc.edu.muusmart.campaignmanagement.application.dto.*;
import upc.edu.muusmart.campaignmanagement.domain.model.aggregates.Campaign;
import upc.edu.muusmart.campaignmanagement.domain.model.aggregates.Channel;
import upc.edu.muusmart.campaignmanagement.domain.model.aggregates.Goal;
import upc.edu.muusmart.campaignmanagement.domain.repository.CampaignRepository;
import upc.edu.muusmart.campaignmanagement.domain.repository.ChannelRepository;
import upc.edu.muusmart.campaignmanagement.domain.repository.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final GoalRepository goalRepository;
    private final ChannelRepository channelRepository;

    public CampaignResponse createCampaign(CreateCampaignRequest request, Long userId) {
        campaignRepository.findByName(request.getName())
                .ifPresent(c -> {
                    throw new RuntimeException("Campaign already exists");
                });

        Campaign campaign = Campaign.builder()
                .name(request.getName())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(request.getStatus())
                .userId(userId)
                .stableId(request.getStableId())
                .build();

        Campaign savedCampaign = campaignRepository.save(campaign);
        return mapToCampaignResponse(savedCampaign);
    }

    public CampaignResponse getCampaignById(Long id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found with id: " + id));
        return mapToCampaignResponse(campaign);
    }

    public List<CampaignResponse> getAllCampaignsByUserId(Long userId, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> auth.equals("ROLE_ADMIN"));

        if (isAdmin) {
            return campaignRepository.findAll()
                    .stream()
                    .map(this::mapToCampaignResponse)
                    .collect(Collectors.toList());
        }

        return campaignRepository.findByUserId(userId)
                .stream()
                .map(this::mapToCampaignResponse)
                .collect(Collectors.toList());
    }

    public List<CampaignResponse> getCampaignsByStableId(Long stableId) {
        return campaignRepository.findByStableId(stableId)
                .stream()
                .map(this::mapToCampaignResponse)
                .collect(Collectors.toList());
    }

    public void deleteCampaign(Long id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found with id: " + id));
        campaignRepository.delete(campaign);
    }

    public CampaignResponse updateCampaignStatus(Long id, UpdateCampaignStatusRequest request) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found with id: " + id));
        campaign.updateStatus(request.getStatus());
        Campaign updatedCampaign = campaignRepository.save(campaign);
        return mapToCampaignResponse(updatedCampaign);
    }

    public CampaignResponse addGoalToCampaign(Long campaignId, AddGoalRequest request) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException("Campaign not found with id: " + campaignId));

        Goal goal = Goal.builder()
                .description(request.getDescription())
                .metric(request.getMetric())
                .targetValue(request.getTargetValue())
                .currentValue(request.getCurrentValue())
                .build();

        campaign.addGoal(goal);
        Campaign updatedCampaign = campaignRepository.save(campaign);
        return mapToCampaignResponse(updatedCampaign);
    }

    public CampaignResponse addChannelToCampaign(Long campaignId, AddChannelRequest request) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException("Campaign not found with id: " + campaignId));

        Channel channel = Channel.builder()
                .type(request.getType())
                .details(request.getDetails())
                .build();

        campaign.addChannel(channel);
        Campaign updatedCampaign = campaignRepository.save(campaign);
        return mapToCampaignResponse(updatedCampaign);
    }

    public List<GoalResponse> getGoalsByCampaignId(Long campaignId) {
        return goalRepository.findByCampaignId(campaignId)
                .stream()
                .map(this::mapToGoalResponse)
                .collect(Collectors.toList());
    }

    public List<ChannelResponse> getChannelsByCampaignId(Long campaignId) {
        return channelRepository.findByCampaignId(campaignId)
                .stream()
                .map(this::mapToChannelResponse)
                .collect(Collectors.toList());
    }

    private CampaignResponse mapToCampaignResponse(Campaign campaign) {
        return CampaignResponse.builder()
                .id(campaign.getId())
                .name(campaign.getName())
                .description(campaign.getDescription())
                .startDate(campaign.getStartDate())
                .endDate(campaign.getEndDate())
                .status(campaign.getStatus())
                .userId(campaign.getUserId())
                .stableId(campaign.getStableId())
                .goals(campaign.getGoals().stream()
                        .map(this::mapToGoalResponse)
                        .collect(Collectors.toList()))
                .channels(campaign.getChannels().stream()
                        .map(this::mapToChannelResponse)
                        .collect(Collectors.toList()))
                .createdAt(campaign.getCreatedAt())
                .updatedAt(campaign.getUpdatedAt())
                .build();
    }

    private GoalResponse mapToGoalResponse(Goal goal) {
        return GoalResponse.builder()
                .id(goal.getId())
                .description(goal.getDescription())
                .metric(goal.getMetric())
                .targetValue(goal.getTargetValue())
                .currentValue(goal.getCurrentValue())
                .build();
    }

    private ChannelResponse mapToChannelResponse(Channel channel) {
        return ChannelResponse.builder()
                .id(channel.getId())
                .type(channel.getType())
                .details(channel.getDetails())
                .build();
    }

}
