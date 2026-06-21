package com.sgta.dashboard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sgta.customer.CustomerRepository;
import com.sgta.dashboard.response.DashboardResponse;
import com.sgta.dashboard.response.StockAnalytics;
import com.sgta.invoice.InvoiceRepository;
import com.sgta.part.PartRepository;
import com.sgta.role.model.RoleName;
import com.sgta.shared.permission.AppModule;
import com.sgta.shared.permission.PermissionService;
import com.sgta.user.UserRepository;
import com.sgta.vehicle.VehicleRepository;
import com.sgta.workorder.repository.WorkOrderRepository;
import java.util.EnumSet;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock PermissionService permissionService;
    @Mock CustomerRepository customerRepository;
    @Mock VehicleRepository vehicleRepository;
    @Mock PartRepository partRepository;
    @Mock WorkOrderRepository workOrderRepository;
    @Mock InvoiceRepository invoiceRepository;
    @Mock UserRepository userRepository;

    @InjectMocks DashboardService dashboardService;

    @Test
    void mechanicSeesOnlyHisWorkOrders() {
        var roles = EnumSet.of(RoleName.MECHANIC);
        var ctx = new DashboardContext(roles, 2L);

        when(permissionService.canView(roles, AppModule.CUSTOMERS)).thenReturn(true);
        when(permissionService.canView(roles, AppModule.VEHICLES)).thenReturn(true);
        when(permissionService.canView(roles, AppModule.PARTS)).thenReturn(true);
        when(permissionService.canView(roles, AppModule.WORK_ORDERS)).thenReturn(true);
        when(permissionService.canView(roles, AppModule.INVOICES)).thenReturn(false);
        when(permissionService.canView(roles, AppModule.USERS)).thenReturn(false);

        when(customerRepository.countByStatusTrue()).thenReturn(10L);
        when(vehicleRepository.countByStatusTrue()).thenReturn(20L);
        when(partRepository.countByStatusTrue()).thenReturn(100L);
        when(partRepository.countAdequateStock()).thenReturn(80L);
        when(partRepository.countLowStock()).thenReturn(15L);
        when(partRepository.countOutOfStock()).thenReturn(5L);

        when(workOrderRepository.countByStatusTrue(2L)).thenReturn(5L);
        when(workOrderRepository.countCompleted(2L)).thenReturn(3L);
        when(workOrderRepository.countPending(2L)).thenReturn(2L);
        when(workOrderRepository.findTop5ByStatusTrueOrderByIntakeDateDesc(2L))
                .thenReturn(List.of());

        DashboardResponse response = dashboardService.getSummary(ctx);

        assertThat(response.customerCount()).isEqualTo(10);
        assertThat(response.vehicleCount()).isEqualTo(20);
        assertThat(response.partCount()).isEqualTo(100);
        assertThat(response.workOrderCount()).isEqualTo(5);
        assertThat(response.invoiceCount()).isEqualTo(0);
        assertThat(response.userCount()).isEqualTo(0);

        assertThat(response.stockAnalytics().adequate()).isEqualTo(80);
        assertThat(response.stockAnalytics().low()).isEqualTo(15);
        assertThat(response.stockAnalytics().outOfStock()).isEqualTo(5);

        assertThat(response.workOrderAnalytics().completed()).isEqualTo(3);
        assertThat(response.workOrderAnalytics().pending()).isEqualTo(2);

        verify(workOrderRepository).countByStatusTrue(2L);
        verify(workOrderRepository, never()).countByStatusTrue(null);
    }

    @Test
    void adminSeesFullDashboard() {
        var roles = EnumSet.of(RoleName.ADMIN);
        var ctx = new DashboardContext(roles, 1L);

        when(permissionService.canView(any(), any())).thenReturn(true);

        when(customerRepository.countByStatusTrue()).thenReturn(10L);
        when(vehicleRepository.countByStatusTrue()).thenReturn(20L);
        when(partRepository.countByStatusTrue()).thenReturn(100L);
        when(partRepository.countAdequateStock()).thenReturn(80L);
        when(partRepository.countLowStock()).thenReturn(15L);
        when(partRepository.countOutOfStock()).thenReturn(5L);

        when(workOrderRepository.countByStatusTrue(null)).thenReturn(50L);
        when(workOrderRepository.countCompleted(null)).thenReturn(30L);
        when(workOrderRepository.countPending(null)).thenReturn(20L);
        when(workOrderRepository.findTop5ByStatusTrueOrderByIntakeDateDesc(null))
                .thenReturn(List.of());

        when(invoiceRepository.countByStatusTrue()).thenReturn(15L);
        when(userRepository.countByStatusTrue()).thenReturn(8L);

        DashboardResponse response = dashboardService.getSummary(ctx);

        assertThat(response.customerCount()).isEqualTo(10);
        assertThat(response.vehicleCount()).isEqualTo(20);
        assertThat(response.partCount()).isEqualTo(100);
        assertThat(response.workOrderCount()).isEqualTo(50);
        assertThat(response.invoiceCount()).isEqualTo(15);
        assertThat(response.userCount()).isEqualTo(8);

        assertThat(response.stockAnalytics()).isNotNull();
        assertThat(response.workOrderAnalytics()).isNotNull();
        assertThat(response.recentWorkOrders()).isEmpty();
    }

    @Test
    void roleBasedVisibility() {
        var roles = EnumSet.of(RoleName.RECEPTIONIST);
        var ctx = new DashboardContext(roles, 1L);

        when(permissionService.canView(roles, AppModule.CUSTOMERS)).thenReturn(true);
        when(permissionService.canView(roles, AppModule.VEHICLES)).thenReturn(true);
        when(permissionService.canView(roles, AppModule.PARTS)).thenReturn(false);
        when(permissionService.canView(roles, AppModule.WORK_ORDERS)).thenReturn(true);
        when(permissionService.canView(roles, AppModule.INVOICES)).thenReturn(true);
        when(permissionService.canView(roles, AppModule.USERS)).thenReturn(false);

        when(customerRepository.countByStatusTrue()).thenReturn(10L);
        when(vehicleRepository.countByStatusTrue()).thenReturn(20L);

        when(workOrderRepository.countByStatusTrue(null)).thenReturn(50L);
        when(workOrderRepository.countCompleted(null)).thenReturn(30L);
        when(workOrderRepository.countPending(null)).thenReturn(20L);
        when(workOrderRepository.findTop5ByStatusTrueOrderByIntakeDateDesc(null))
                .thenReturn(List.of());

        when(invoiceRepository.countByStatusTrue()).thenReturn(15L);

        DashboardResponse response = dashboardService.getSummary(ctx);

        assertThat(response.customerCount()).isEqualTo(10);
        assertThat(response.vehicleCount()).isEqualTo(20);
        assertThat(response.partCount()).isEqualTo(0);
        assertThat(response.workOrderCount()).isEqualTo(50);
        assertThat(response.invoiceCount()).isEqualTo(15);
        assertThat(response.userCount()).isEqualTo(0);

        assertThat(response.stockAnalytics()).isEqualTo(StockAnalytics.ZERO);
        assertThat(response.workOrderAnalytics()).isNotNull();
    }
}
