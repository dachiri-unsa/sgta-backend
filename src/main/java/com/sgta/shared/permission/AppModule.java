package com.sgta.shared.permission;

import static com.sgta.role.model.RoleName.*;

import com.sgta.role.model.RoleName;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public enum AppModule {

    USERS(
        view(ADMIN),
        create(ADMIN),
        edit(ADMIN),
        delete(ADMIN)
    ),
    CUSTOMERS(
        view(ADMIN, MECHANIC, RECEPTIONIST, CASHIER, INVENTORY_MANAGER),
        create(ADMIN, RECEPTIONIST),
        edit(ADMIN, RECEPTIONIST),
        delete(ADMIN)
    ),
    VEHICLES(
        view(ADMIN, MECHANIC, RECEPTIONIST, INVENTORY_MANAGER),
        create(ADMIN, RECEPTIONIST),
        edit(ADMIN, RECEPTIONIST),
        delete(ADMIN)
    ),
    PARTS(
        view(ADMIN, MECHANIC, RECEPTIONIST, INVENTORY_MANAGER),
        create(ADMIN, INVENTORY_MANAGER),
        edit(ADMIN, INVENTORY_MANAGER),
        delete(ADMIN, INVENTORY_MANAGER)
    ),
    WORK_ORDERS(
        view(ADMIN, MECHANIC, RECEPTIONIST, CASHIER, INVENTORY_MANAGER),
        create(ADMIN, RECEPTIONIST),
        edit(ADMIN, MECHANIC),
        delete(ADMIN)
    ),
    WORK_ORDER_PARTS(
        view(ADMIN, MECHANIC, RECEPTIONIST, CASHIER, INVENTORY_MANAGER),
        create(ADMIN, MECHANIC),
        edit(ADMIN, MECHANIC),
        delete(ADMIN)
    ),
    INVOICES(
        view(ADMIN, RECEPTIONIST, CASHIER),
        create(ADMIN, CASHIER),
        edit(ADMIN, CASHIER),
        delete(ADMIN, CASHIER)
    );

    private final Map<Action, Set<RoleName>> actionPermissions;

    AppModule(PermissionEntry view, PermissionEntry create, PermissionEntry edit, PermissionEntry delete) {
        Map<Action, Set<RoleName>> map = new EnumMap<>(Action.class);
        map.put(Action.VIEW, view.roles());
        map.put(Action.CREATE, create.roles());
        map.put(Action.EDIT, edit.roles());
        map.put(Action.DELETE, delete.roles());
        this.actionPermissions = Collections.unmodifiableMap(map);
    }

    public boolean allows(Set<RoleName> userRoles, Action action) {
        Set<RoleName> allowed = actionPermissions.get(action);
        return allowed != null && !Collections.disjoint(userRoles, allowed);
    }

    private static PermissionEntry view(RoleName first, RoleName... rest) {
        return new PermissionEntry(EnumSet.of(first, rest));
    }

    private static PermissionEntry create(RoleName first, RoleName... rest) {
        return new PermissionEntry(EnumSet.of(first, rest));
    }

    private static PermissionEntry edit(RoleName first, RoleName... rest) {
        return new PermissionEntry(EnumSet.of(first, rest));
    }

    private static PermissionEntry delete(RoleName first, RoleName... rest) {
        return new PermissionEntry(EnumSet.of(first, rest));
    }

    private record PermissionEntry(Set<RoleName> roles) {
        PermissionEntry {
            roles = Collections.unmodifiableSet(roles);
        }
    }
}
