package com.sgta.dashboard;

import com.sgta.role.model.RoleName;
import java.util.Set;

public record DashboardContext(
        Set<RoleName> roles,
        Long userId
) {
    public boolean isAdmin() {
        return roles.contains(RoleName.ADMIN);
    }

    public boolean filterWorkOrdersByMechanic() {
        return !isAdmin() && roles.contains(RoleName.MECHANIC);
    }
}
