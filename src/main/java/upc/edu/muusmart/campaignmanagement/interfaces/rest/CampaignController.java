package upc.edu.muusmart.campaignmanagement.interfaces.rest;

import upc.edu.muusmart.campaignmanagement.application.dto.*;
import upc.edu.muusmart.campaignmanagement.application.service.CampaignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/campaigns")
@RequiredArgsConstructor
@Tag(name = "Campaigns", description = "Campaign Management API")
@SecurityRequirement(name = "Bearer Authentication")
public class CampaignController {

    private final CampaignService campaignService;

    @PostMapping
    @Operation(summary = "Create a new campaign")
    public ResponseEntity<CampaignResponse> createCampaign(
            @Valid @RequestBody CreateCampaignRequest request,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        CampaignResponse response = campaignService.createCampaign(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Get all campaigns")
    public ResponseEntity<List<CampaignResponse>> getAllCampaigns(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        List<CampaignResponse> campaigns = campaignService.getAllCampaignsByUserId(userId, authentication);
        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get campaign by ID")
    public ResponseEntity<CampaignResponse> getCampaignById(@PathVariable Long id) {
        CampaignResponse response = campaignService.getCampaignById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a campaign")
    public ResponseEntity<Void> deleteCampaign(@PathVariable Long id) {
        campaignService.deleteCampaign(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/update-status")
    @Operation(summary = "Update campaign status")
    public ResponseEntity<CampaignResponse> updateCampaignStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCampaignStatusRequest request) {
        CampaignResponse response = campaignService.updateCampaignStatus(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/add-goal")
    @Operation(summary = "Add a goal to a campaign")
    public ResponseEntity<CampaignResponse> addGoalToCampaign(
            @PathVariable Long id,
            @Valid @RequestBody AddGoalRequest request) {
        CampaignResponse response = campaignService.addGoalToCampaign(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/add-channel")
    @Operation(summary = "Add a channel to a campaign")
    public ResponseEntity<CampaignResponse> addChannelToCampaign(
            @PathVariable Long id,
            @Valid @RequestBody AddChannelRequest request) {
        CampaignResponse response = campaignService.addChannelToCampaign(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/goals")
    @Operation(summary = "Get all goals for a campaign")
    public ResponseEntity<List<GoalResponse>> getGoalsByCampaign(@PathVariable Long id) {
        List<GoalResponse> goals = campaignService.getGoalsByCampaignId(id);
        return ResponseEntity.ok(goals);
    }

    @GetMapping("/{id}/channels")
    @Operation(summary = "Get all channels for a campaign")
    public ResponseEntity<List<ChannelResponse>> getChannelsByCampaign(@PathVariable Long id) {
        List<ChannelResponse> channels = campaignService.getChannelsByCampaignId(id);
        return ResponseEntity.ok(channels);
    }
}
