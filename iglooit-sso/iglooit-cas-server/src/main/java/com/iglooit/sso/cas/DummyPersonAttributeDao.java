package com.iglooit.sso.cas;

import org.jasig.services.persondir.IPersonAttributes;
import org.jasig.services.persondir.support.AbstractDefaultAttributePersonAttributeDao;
import org.jasig.services.persondir.support.CaseInsensitiveAttributeNamedPersonImpl;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DummyPersonAttributeDao extends AbstractDefaultAttributePersonAttributeDao
{
    private static final String SPRING_ATTRIBUTE_AUTHORITIES = "authorities";

    private static final String ADMIN_ROLE_KEYWORD = "admin";

    @Resource(name = "default-role")
    private String defaultRole;

    @Resource(name = "cas-admin-role")
    private String casAdminRole;

    public void setDefaultRole(String defaultRole)
    {
        this.defaultRole = defaultRole;
    }

    public void setCasAdminRole(String casAdminRole)
    {
        this.casAdminRole = casAdminRole;
    }

    @Override
    public Set<IPersonAttributes> getPeopleWithMultivaluedAttributes(Map<String, List<Object>> stringListMap)
    {
        String username = getUsernameAttributeProvider().getUsernameFromQuery(stringListMap);
        logger.info("DummyPersonAttributeDao.getPeopleWithMultivaluedAttributes - username: " + username);
        final Set<IPersonAttributes> mappedPeople = new LinkedHashSet<IPersonAttributes>();
        Map<String, List<Object>> newStringListMap = new HashMap<String, List<Object>>(stringListMap);
        List<Object> attributes = new ArrayList<Object>();
        if (defaultRole != null)
            attributes.add(defaultRole);
        if (username.contains(ADMIN_ROLE_KEYWORD) && casAdminRole != null)
            attributes.add(casAdminRole);
        newStringListMap.put(SPRING_ATTRIBUTE_AUTHORITIES, attributes);
        IPersonAttributes person = new CaseInsensitiveAttributeNamedPersonImpl(username, newStringListMap);
        mappedPeople.add(person);
        return Collections.unmodifiableSet(mappedPeople);
    }

    @Override
    public Set<String> getPossibleUserAttributeNames()
    {
        logger.info("DummyPersonAttributeDao.getPossibleUserAttributeNames");
        Set<String> possibleRoles = new HashSet<String>();
        if (defaultRole != null)
            possibleRoles.add(defaultRole);
        if (casAdminRole != null)
            possibleRoles.add(casAdminRole);
        return possibleRoles;
    }

    @Override
    public Set<String> getAvailableQueryAttributes()
    {
        logger.info("DummyPersonAttributeDao.getAvailableQueryAttributes");
        return null;
    }
}
