package com.sgta.shared.permission;

import com.sgta.role.model.RoleName;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

    public boolean can(Set<RoleName> userRoles, AppModule module, Action action) {
        return module.allows(userRoles, action);
    }

    public boolean canView(Set<RoleName> userRoles, AppModule module) {
        return can(userRoles, module, Action.VIEW);
    }

    public boolean canCreate(Set<RoleName> userRoles, AppModule module) {
        return can(userRoles, module, Action.CREATE);
    }

    public boolean canEdit(Set<RoleName> userRoles, AppModule module) {
        return can(userRoles, module, Action.EDIT);
    }

    public boolean canDelete(Set<RoleName> userRoles, AppModule module) {
        return can(userRoles, module, Action.DELETE);
    }
}
