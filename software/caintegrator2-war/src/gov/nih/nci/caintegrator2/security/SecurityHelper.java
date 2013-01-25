/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.security;

import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroupRoleContext;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.exceptions.CSException;

import java.util.HashSet;
import java.util.Set;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.UserDetails;

/**
 * Providers helper methods to access authentication and authorization data.
 */
public final class SecurityHelper {
    private static final String STUDY_MANAGER_ROLE = "STUDY_MANAGER_ROLE";
    private static final String PLATFORM_MANAGER_ROLE = "PLATFORM_MANAGER_ROLE";
    //private static final String MODIFY_STUDY = "MODIFY_STUDY";
    
    private SecurityHelper() {
        super();
    }

    /**
     * Returns the username of the current user.
     * 
     * @return the current user's username.
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if (authentication != null) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString();
            }
        }
        return username;
    }
    
    /**
     * Check if the user is a study manager.
     * @param username object to use.
     * @return a boolean.
     */
    public static boolean isStudyManager(String username) {
        try {
            User user = getAuthorizationManager().getUser(username);
            String userId = String.valueOf(user.getUserId());
            if (retrieveProtectionGroups(userId, STUDY_MANAGER_ROLE).isEmpty()) {
                return false;
            }
            return true;
        } catch (CSException e) {
            throw new IllegalStateException("Unable to check user against CSM database.");
        }
    }
    
    /**
     * Check if the user is a platform manager.
     * @param username object to use.
     * @return a boolean.
     */
    public static boolean isPlatformManager(String username) {
        try {
            User user = getAuthorizationManager().getUser(username);
            String userId = String.valueOf(user.getUserId());
            if (retrieveProtectionGroups(userId, PLATFORM_MANAGER_ROLE).isEmpty()) {
                return false;
            }
            return true;
        } catch (CSException e) {
            throw new IllegalStateException("Unable to check user against CSM database.");
        }
    }

    @SuppressWarnings("unchecked")
    private static Set<ProtectionGroup> retrieveProtectionGroups(String userId, String myRole) 
    throws CSException {
        Set<ProtectionGroup> protectionGroups = new HashSet<ProtectionGroup>();
        Set<Group> groups = getAuthorizationManager().getGroups(userId);
        for (Group group : groups) {
            Set<ProtectionGroupRoleContext> pgrcs = 
                getAuthorizationManager().getProtectionGroupRoleContextForGroup(String.valueOf(group.getGroupId()));
            for (ProtectionGroupRoleContext pgrc : pgrcs) {
                for (Role role : (Set<Role>) pgrc.getRoles()) {
                    if (myRole.equals(role.getName())) {
                        protectionGroups.add(pgrc.getProtectionGroup());
                        break;
                    }
                }
            }
        }
        return protectionGroups;
    }
    
    private static AuthorizationManager getAuthorizationManager() throws CSException {
        return SecurityServiceProvider.getAuthorizationManager("caintegrator2");
    }

}
