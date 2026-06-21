package com.sgta.dashboard;

import static com.sgta.shared.permission.AppModule.*;

import com.sgta.customer.CustomerRepository;
import com.sgta.dashboard.response.DashboardResponse;
import com.sgta.dashboard.response.RecentWorkOrderDto;
import com.sgta.dashboard.response.StockAnalytics;
import com.sgta.dashboard.response.WorkOrderAnalytics;
import com.sgta.invoice.InvoiceRepository;
import com.sgta.part.PartRepository;
import com.sgta.shared.permission.PermissionService;
import com.sgta.user.UserRepository;
import com.sgta.vehicle.VehicleRepository;
import com.sgta.workorder.repository.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final PermissionService permissionService;
    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;
    private final PartRepository partRepository;
    private final WorkOrderRepository workOrderRepository;
    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public DashboardResponse getSummary(DashboardContext ctx) {
        var roles = ctx.roles();
        Long mechanicFilter = ctx.filterWorkOrdersByMechanic() ? ctx.userId() : null;

        long customerCount = 0;
        if (permissionService.canView(roles, CUSTOMERS)) {
            customerCount = customerRepository.countByStatusTrue();
        }

        long vehicleCount = 0;
        if (permissionService.canView(roles, VEHICLES)) {
            vehicleCount = vehicleRepository.countByStatusTrue();
        }

        long partCount = 0;
        StockAnalytics stockAnalytics = StockAnalytics.ZERO;
        if (permissionService.canView(roles, PARTS)) {
            partCount = partRepository.countByStatusTrue();
            stockAnalytics = new StockAnalytics(
                    partRepository.countAdequateStock(),
                    partRepository.countLowStock(),
                    partRepository.countOutOfStock()
            );
        }

        long workOrderCount = 0;
        WorkOrderAnalytics workOrderAnalytics = WorkOrderAnalytics.ZERO;
        var recentWorkOrders = java.util.Collections.<RecentWorkOrderDto>emptyList();
        if (permissionService.canView(roles, WORK_ORDERS)) {
            workOrderCount = workOrderRepository.countByStatusTrue(mechanicFilter);
            workOrderAnalytics = new WorkOrderAnalytics(
                    workOrderRepository.countCompleted(mechanicFilter),
                    workOrderRepository.countPending(mechanicFilter)
            );
            recentWorkOrders = workOrderRepository
                    .findTop5ByStatusTrueOrderByIntakeDateDesc(mechanicFilter)
                    .stream()
                    .map(wo -> new RecentWorkOrderDto(
                            wo.getId(), wo.getCode(), wo.getPriority(),
                            wo.getIntakeDate(), wo.getTotal()))
                    .toList();
        }

        long invoiceCount = 0;
        if (permissionService.canView(roles, INVOICES)) {
            invoiceCount = invoiceRepository.countByStatusTrue();
        }

        long userCount = 0;
        if (permissionService.canView(roles, USERS)) {
            userCount = userRepository.countByStatusTrue();
        }

        return new DashboardResponse(
                customerCount, vehicleCount, partCount, workOrderCount,
                invoiceCount, userCount,
                stockAnalytics, workOrderAnalytics,
                recentWorkOrders
        );
    }

}
