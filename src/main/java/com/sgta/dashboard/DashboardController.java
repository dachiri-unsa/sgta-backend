package com.sgta.dashboard;

import com.sgta.dashboard.response.DashboardResponse;
import com.sgta.security.user.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<DashboardResponse> getSummary(
            @AuthenticationPrincipal UserPrincipal principal) {
        var context = new DashboardContext(principal.getRoles(), principal.getUserId());
        DashboardResponse response = dashboardService.getSummary(context);
        return ResponseEntity.ok(response);
    }
}
