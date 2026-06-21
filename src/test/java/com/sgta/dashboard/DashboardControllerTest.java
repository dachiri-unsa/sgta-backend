package com.sgta.dashboard;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sgta.dashboard.response.DashboardResponse;
import com.sgta.dashboard.response.StockAnalytics;
import com.sgta.dashboard.response.WorkOrderAnalytics;
import com.sgta.security.jwt.JwtService;
import com.sgta.security.user.UserPrincipal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DashboardController.class)
class DashboardControllerTest {

    @Autowired MockMvc mockMvc;

    @MockitoBean DashboardService dashboardService;
    @MockitoBean JwtService jwtService;
    @MockitoBean UserDetailsService userDetailsService;

    @Test
    void unauthenticatedRequestReturns401() throws Exception {
        mockMvc.perform(get("/api/dashboard/summary"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void authenticatedRequestReturnsDashboard() throws Exception {
        var principal = new UserPrincipal(1L, "admin@sgta.com", "password", List.of("ADMIN"), true);
        var auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        var response = new DashboardResponse(
                10, 20, 100, 50, 15, 8,
                new StockAnalytics(80, 15, 5),
                new WorkOrderAnalytics(30, 20),
                List.of()
        );

        when(dashboardService.getSummary(any())).thenReturn(response);

        mockMvc.perform(get("/api/dashboard/summary").with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerCount").value(10))
                .andExpect(jsonPath("$.vehicleCount").value(20))
                .andExpect(jsonPath("$.partCount").value(100))
                .andExpect(jsonPath("$.workOrderCount").value(50))
                .andExpect(jsonPath("$.invoiceCount").value(15))
                .andExpect(jsonPath("$.userCount").value(8))
                .andExpect(jsonPath("$.stockAnalytics.adequate").value(80))
                .andExpect(jsonPath("$.stockAnalytics.low").value(15))
                .andExpect(jsonPath("$.stockAnalytics.outOfStock").value(5))
                .andExpect(jsonPath("$.workOrderAnalytics.completed").value(30))
                .andExpect(jsonPath("$.workOrderAnalytics.pending").value(20));

        verify(dashboardService).getSummary(any());
    }
}
