package com.iglooit.core.base.server.domain;

import com.clarity.commons.iface.domain.SearchPaging;
import com.clarity.core.base.iface.domain.DomainEntity;
import com.clarity.core.base.iface.domain.JpaDomainEntity;
import com.clarity.core.base.iface.domain.JpaDomainEntityMeta;
import com.clarity.commons.iface.domain.meta.Meta;
import com.clarity.commons.iface.domain.OrderBy;
import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.type.Option;
import com.clarity.commons.iface.type.Tuple2;
import com.clarity.commons.iface.type.Tuple3;
import com.clarity.commons.iface.type.UUID;
import com.clarity.commons.server.util.ReflectionUtil;
import org.hibernate.Hibernate;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class DomainEntitySearch
{
    private Option<SearchPaging> searchPagingOpt;
    private QueryNode root;
    private List<QueryNode> resultHits = new ArrayList<QueryNode>();
    private List<Tuple3<QueryNode, String, OrderBy>> orderBys = new ArrayList<Tuple3<QueryNode, String, OrderBy>>();

    private boolean orWhereClause = false;

    private static final String REGEXP_QUERY = ".*?[^\\w ].*";

    private <DE extends JpaDomainEntity> DomainEntitySearch(Class<DE> start, Option<SearchPaging> searchPagingOpt)
    {
        this.root = new QueryNode(null, start, getRootAlias(), true);
        resultHits.add(root);
        this.searchPagingOpt = searchPagingOpt;
    }

    private <DE extends JpaDomainEntity> DomainEntitySearch(DE start, Option<SearchPaging> searchPagingOpt)
    {
        this.root = new QueryNode(null, start, getRootAlias(), true);
        resultHits.add(root);
        this.searchPagingOpt = searchPagingOpt;
    }

    public <DE extends JpaDomainEntity> DomainEntitySearch(Class<DE> start)
    {
        this(start, Option.<SearchPaging>none());
    }

    public <DE extends JpaDomainEntity> DomainEntitySearch(DE start)
    {
        this(start, Option.<SearchPaging>none());
    }

    public <DE extends JpaDomainEntity> DomainEntitySearch(Class<DE> start, SearchPaging paging)
    {
        this(start, Option.<SearchPaging>option(paging));
    }

    public <DE extends JpaDomainEntity> DomainEntitySearch(DE start, SearchPaging paging)
    {
        this(start, Option.<SearchPaging>option(paging));
    }


    public String getRootAlias()
    {
        return "ROOT";
    }

    public QueryNode getRoot()
    {
        return root;
    }

    public Option<SearchPaging> getSearchPagingOpt()
    {
        return searchPagingOpt;
    }

    public void setSearchPaging(SearchPaging searchPaging)
    {
        this.searchPagingOpt = Option.option(searchPaging);
    }

    public void setOrWhereClause(boolean orWhereClause)
    {
        this.orWhereClause = orWhereClause;
    }

    private String fieldNameFromPropertyName(String propertyName)
    {
        if (propertyName.contains("-"))
            return DomainEntity.getFieldNameFromMetaPropertyName(propertyName);
        return propertyName;
    }

    public final class QueryNode
    {
        private final QueryNode parent;
        private final List<QueryNode> joins = new ArrayList<QueryNode>();
        private final JoinPart joinPart;
        private String parentPropertyName;
        private boolean reverseJoin;
        private final boolean inResult;
        private Option<ArrayList<String>> soundexRegexps = Option.none();


        public String getAliasName()
        {
            String aliasName = getJoinPart().getJoinClass().getSimpleName() + "_" +
                fieldNameFromPropertyName(parentPropertyName);
            if (parent != null)
                aliasName = parent.getAliasName() + "__" + aliasName;
            return aliasName;
        }

        public boolean isRoot()
        {
            return parent == null;
        }

        private <DE extends JpaDomainEntity> QueryNode(QueryNode parent, Class<DE> domainEntityClass, boolean inResult)
        {
            this.parent = parent;
            joinPart = new ClassJoin<DE>(domainEntityClass);
            this.inResult = inResult;
        }

        private <DE extends JpaDomainEntity> QueryNode(QueryNode parent, DE domainEntity, boolean inResult)
        {
            this.parent = parent;
            joinPart = new InstanceJoin<DE>(domainEntity);
            this.inResult = inResult;
        }

        private <DE extends JpaDomainEntity> QueryNode(QueryNode parent,
                                                       Class<DE> domainEntityClass,
                                                       String parentPropertyName,
                                                       boolean inResult)
        {
            this.parent = parent;
            joinPart = new ClassJoin<DE>(domainEntityClass);
            this.parentPropertyName = parentPropertyName;
            this.inResult = inResult;
        }

        private <DE extends JpaDomainEntity> QueryNode(QueryNode parent,
                                                       DE domainEntity,
                                                       String parentPropertyName,
                                                       boolean inResult)
        {
            this.parent = parent;
            joinPart = new InstanceJoin<DE>(domainEntity);
            this.parentPropertyName = parentPropertyName;
            this.inResult = inResult;
        }

        private QueryNode join(QueryNode newChild)
        {
            // use reflection to look at the join, and see if it should be a reverse join.
            // to do this we need instances of the join classes
            String propertyName = newChild.getParentPropertyName();
            String forwardsPropertyName;
            String reversePropertyName;

            Class parentClass = this.getJoinPart().getJoinClass();
            Class childClass = newChild.getJoinPart().getJoinClass();

            Meta parentInstance = ReflectionUtil.instantiateMeta(parentClass).value();
            Meta childInstance = ReflectionUtil.instantiateMeta(childClass).value();

            // if we're using the default propertyname
            if (propertyName == null)
            {
                forwardsPropertyName = getDefaultPropertyName(childClass);
                reversePropertyName = getDefaultPropertyName(parentClass);
            }
            else
            {
                forwardsPropertyName = propertyName;
                reversePropertyName = propertyName;
            }

            // normal direction:
            if (parentInstance.getPropertyNames().contains(forwardsPropertyName))
            {
                newChild.reverseJoin = false;
                propertyName = forwardsPropertyName;
            }
            else if (childInstance.getPropertyNames().contains(reversePropertyName))
            {
                newChild.reverseJoin = true;
                propertyName = reversePropertyName;
            }
            else
            {
                throw new AppX("No property join between classes: " + parentInstance.getClass().getSimpleName() +
                    " and " + childInstance.getClass().getSimpleName() + " for propertyName: " + propertyName);
            }
            newChild.parentPropertyName = propertyName;
            joins.add(newChild);
            if (newChild.inResult)
                resultHits.add(newChild);
            return newChild;
        }

        public <DE extends JpaDomainEntity> QueryNode join(Class<DE> domainEntityClass)
        {
            return join(new QueryNode(this, domainEntityClass, false));
        }

        public <DE extends JpaDomainEntity> QueryNode join(DE domainEntity)
        {
            return join(new QueryNode(this, domainEntity, false));
        }

        public <DE extends JpaDomainEntity> QueryNode join(Class<DE> domainEntityClass, String parentPropertyName)
        {
            return join(new QueryNode(this, domainEntityClass, parentPropertyName, false));
        }

        public <DE extends JpaDomainEntity> QueryNode join(DE domainEntity, String parentPropertyName)
        {
            return join(new QueryNode(this, domainEntity, parentPropertyName, false));
        }

        public <DE extends JpaDomainEntity> QueryNode joinHit(Class<DE> domainEntityClass)
        {
            return join(new QueryNode(this, domainEntityClass, true));
        }

        public <DE extends JpaDomainEntity> QueryNode joinHit(DE domainEntity)
        {
            return join(new QueryNode(this, domainEntity, true));
        }

        public <DE extends JpaDomainEntity> QueryNode joinHit(Class<DE> domainEntityClass, String parentPropertyName)
        {
            return join(new QueryNode(this, domainEntityClass, parentPropertyName, true));
        }

        public <DE extends JpaDomainEntity> QueryNode joinHit(DE domainEntity, String parentPropertyName)
        {
            return join(new QueryNode(this, domainEntity, parentPropertyName, true));
        }

        public List<QueryNode> getJoins()
        {
            return joins;
        }

        public JoinPart getJoinPart()
        {
            return joinPart;
        }

        public String getParentPropertyName()
        {
            return parentPropertyName;
        }

        public String getParentFieldName()
        {
            return fieldNameFromPropertyName(parentPropertyName);
        }


        public void orderBy(String propertyName, OrderBy orderBy)
        {
            // todo ms: check for existance of the propertyname in this class
            Tuple3<QueryNode, String, OrderBy> nodeOrderBy =
                new Tuple3<QueryNode, String, OrderBy>(this, fieldNameFromPropertyName(propertyName), orderBy);
            orderBys.add(nodeOrderBy);
        }

        public Option<ArrayList<String>> getSoundexRegexps()
        {
            return soundexRegexps;
        }

        public void setSoundexRegexps(ArrayList<String> soundexRegexps)
        {
            this.soundexRegexps = Option.option(soundexRegexps);
        }
    }

    // lower the case of the first character
    private static String getDefaultPropertyName(Class c)
    {
        String cName = c.getSimpleName();
        return cName.substring(0, 1).toLowerCase(Locale.getDefault()) + cName.substring(1, cName.length());
    }

    private abstract static class JoinPart<DE extends JpaDomainEntity>
    {
        public abstract Class<DE> getJoinClass();

        public abstract DE getInstance();

        public boolean isClassJoin()
        {
            return ClassJoin.class.isAssignableFrom(this.getClass());
        }

        public boolean isInstanceJoin()
        {
            return !isClassJoin();
        }
    }

    private static class ClassJoin<DE extends JpaDomainEntity> extends JoinPart<DE>
    {
        private final Class<DE> joinClass;

        public ClassJoin(Class<DE> joinClass)
        {
            this.joinClass = joinClass;
        }

        public Class<DE> getJoinClass()
        {
            return joinClass;
        }

        public DE getInstance()
        {
            throw new AppX("Cannot get instance from a ClassJoin");
        }
    }

    private static class InstanceJoin<DE extends JpaDomainEntity> extends JoinPart<DE>
    {
        private final DE joinInstance;

        public InstanceJoin(DE joinInstance)
        {
            this.joinInstance = joinInstance;
        }

        public Class<DE> getJoinClass()
        {
            // return the unenhanced class instance
            return Hibernate.getClass(joinInstance);
            //return (Class<DE>)joinInstance.getClass();
        }

        public DE getInstance()
        {
            return joinInstance;
        }
    }

    public String getSearchString()
    {
        return getSearchData().getFirst();
    }

    private boolean isPropertyValid(String propertyName, Object property)
    {
        boolean valid = true;

        // some validity checks. strings must be non-empty to count
        if (property == null)
        {
            valid = false;
        }
//        else if (property instanceof Collection)
//        {
//            if (((Collection)property).size() > 0)
//                valid = true;
//            else
//                valid = false;
//        }
        else if (property instanceof String)
        {
            if (((String)property).length() == 0)
                valid = false;
        }
        else if (fieldNameFromPropertyName(propertyName).equals(
            fieldNameFromPropertyName(JpaDomainEntityMeta.ID_PROPERTYNAME)))
        {
            valid = ((UUID)property).isValid();
        }
        else if (fieldNameFromPropertyName(propertyName).equals(
            fieldNameFromPropertyName(JpaDomainEntityMeta.LOCK_VERSION_PROPERTYNAME)))
        {
            valid = false;
        }

        return valid;
    }

    private boolean useSoundex(QueryNode node, String propertyName, Object property)
    {
        boolean useSoundex = false;
        if (!useLike(property)) // no soundex on regexps
            if (node.getSoundexRegexps().isSome())
                for (String regex : node.getSoundexRegexps().value())
                    if (propertyName.matches(regex))
                        useSoundex = true;
        return useSoundex;
    }

    private boolean useLike(Object property)
    {
        if (property instanceof String)
        {
            String stringProperty = (String)property;
            if (stringProperty.matches(REGEXP_QUERY))
                return true;
        }
        return false;
    }

    private Tuple2<List<String>, Map<String, Object>> getSearchParams(QueryNode node)
    {

        String aliasName = node.getAliasName();
        JpaDomainEntity domainEntity = node.getJoinPart().getInstance();

        Map<String, Object> params = new TreeMap<String, Object>();
        List<String> wherePredicates = new ArrayList<String>();

        List<String> propertyNames = domainEntity.getPropertyNames();

        // if the UUID in the domain entity is valid, just match on that rather than all other properties.
        // this is effectively a .equals from two domain entities.
        if (domainEntity.getId() != null && domainEntity.getId().isValid())
        {
            String suffix = fieldNameFromPropertyName(JpaDomainEntityMeta.ID_PROPERTYNAME);
            String parameterPropertyName = aliasName + "_" + suffix;
            String fieldPropertyName = aliasName + "." + suffix;
            wherePredicates.add(fieldPropertyName + " = :" + parameterPropertyName);
            params.put(parameterPropertyName, domainEntity.getId());
        }
        // otherwise iterate through the properties.
        else
        {
            for (String propertyName : propertyNames)
            {
                Object property = domainEntity.get(propertyName);
                String fieldName = fieldNameFromPropertyName(propertyName);

                if (isPropertyValid(propertyName, property))
                {
                    String parameterPropertyName = aliasName + "_" + fieldName;
                    String fieldPropertyName = aliasName + "." + fieldName;

                    params.put(parameterPropertyName, property);

                    String connector = " = ";
                    if (useLike(property))
                        connector = " LIKE ";

                    if (useSoundex(node, propertyName, property))
                        wherePredicates.add("soundex( " + fieldPropertyName + " )" +
                            connector + " soundex( :" + parameterPropertyName + " )");
                    else
                        wherePredicates.add(fieldPropertyName + connector + ":" + parameterPropertyName);
                }
            }
        }

        return Tuple2.make(wherePredicates, params);
    }

    private void getSearchParams(QueryNode node,
                                 List<String> wherePredicates,
                                 Map<String, Object> parameters)
    {
        if (node.getJoinPart().isInstanceJoin())
        {
            Tuple2<List<String>, Map<String, Object>> nodeParams =
                getSearchParams(node);
            for (String predicate : nodeParams.getFirst())
                wherePredicates.add(predicate);
            parameters.putAll(nodeParams.getSecond());
        }
        for (QueryNode childNode : node.getJoins())
            getSearchParams(childNode, wherePredicates, parameters);
    }


    private Tuple2<List<String>, Map<String, Object>> getSearchParams()
    {
        // tree thing to build entire query.
        List<String> wherePredicates = new ArrayList<String>();
        Map<String, Object> parameters = new HashMap<String, Object>();
        getSearchParams(root, wherePredicates, parameters);
        return Tuple2.make(wherePredicates, parameters);
    }

    private String makeJoinFromString(QueryNode node)
    {
        StringBuilder joinString = new StringBuilder();
        if (!node.isRoot())
            joinString.append(", ");
        joinString.append(node.getJoinPart().getJoinClass().getSimpleName()).append(" ").append(node.getAliasName());
        for (QueryNode childNode : node.getJoins())
            joinString.append(makeJoinFromString(childNode));
        return joinString.toString();
    }

    private String makeOrderByString(QueryNode node)
    {
        if (orderBys.size() == 0)
            return "";
        StringBuilder sb = new StringBuilder(" order by ");
        for (Tuple3<QueryNode, String, OrderBy> nodeOrderBy : orderBys)
        {
            String nodeAliasName = nodeOrderBy.getFirst().getAliasName();
            String propertyName = nodeOrderBy.getSecond();
            String orderDirection = nodeOrderBy.getThird().toString();
            String orderClause = nodeAliasName + "." + propertyName + " " + orderDirection;
            sb.append(" ").append(orderClause).append(" ");
        }
        return sb.toString();
    }

    private List<String> makeJoinWherePredicates(QueryNode node)
    {
        List<String> predicates = new ArrayList<String>();
        for (QueryNode childNode : node.getJoins())
        {
            String predicate;
            if (childNode.reverseJoin)
                predicate = childNode.getAliasName() + "." + childNode.getParentFieldName() + " = " +
                    node.getAliasName();
            else
                predicate = node.getAliasName() + "." + childNode.getParentFieldName() + " = " +
                    childNode.getAliasName();
            predicates.add(predicate);

            // recurse
            predicates.addAll(makeJoinWherePredicates(childNode));
        }
        return predicates;
    }

    private Tuple2<String, Map<String, Object>> getSearchData()
    {
        Tuple2<List<String>, Map<String, Object>> searchParams = getSearchParams();
        Map<String, Object> params = searchParams.getSecond();

        // generate the query string out of the predicates.
        StringBuilder queryString = new StringBuilder("select ");

        for (int i = 0; i < resultHits.size(); i++)
        {
            if (i != 0)
                queryString.append(", ");
            queryString.append(resultHits.get(i).getAliasName());
        }


        // from clause
        queryString.append(" from ").append(makeJoinFromString(root));

        List<String> joinWherePredicates = makeJoinWherePredicates(root);
        List<String> queryWherePredicates = searchParams.getFirst();
        String joinWhereStr = assembleWherePredicatesString(joinWherePredicates, "and");
        String queryWhereStr = assembleWherePredicatesString(queryWherePredicates, orWhereClause ? "or" : "and");
        if (joinWherePredicates.size() > 0 && queryWherePredicates.size() > 0)
            queryString.append(" where ").append(joinWhereStr).append(" and ").append(queryWhereStr).append(" ");
        else if (joinWherePredicates.size() > 0 && queryWherePredicates.size() == 0)
            queryString.append(" where ").append(joinWhereStr).append(" ");
        else if (joinWherePredicates.size() == 0 && queryWherePredicates.size() > 0)
            queryString.append(" where ").append(queryWhereStr).append(" ");

        queryString.append(makeOrderByString(root));

        String qString = queryString.toString();

        return new Tuple2<String, Map<String, Object>>(qString, params);
    }

    private String assembleWherePredicatesString(List<String> predicates, String connector)
    {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < predicates.size(); i++)
        {
            if (i == 0)
                sb.append(" (");
            sb.append(" ");
            sb.append(predicates.get(i));
            if (i < predicates.size() - 1)
                sb.append(" ".concat(connector));
            else
                sb.append(" )");
        }
        return sb.toString();
    }

    public List search(EntityManager em)
    {
        Tuple2<String, Map<String, Object>> searchData = getSearchData();
        String searchQuery = searchData.getFirst();

        final Query q = em.createQuery(searchQuery);

        for (Map.Entry<String, Object> parameter : searchData.getSecond().entrySet())
            q.setParameter(parameter.getKey(), parameter.getValue());

        if (searchPagingOpt.isSome())
        {
            SearchPaging searchPaging = searchPagingOpt.value();
            if (!searchPaging.isTotalHitsFound())
            {
                List unpagedHits = q.getResultList();
                searchPaging.setTotalHits(unpagedHits.size());
                searchPaging.setTotalHitsFound(true);
            }
            q.setFirstResult(searchPagingOpt.value().getFirstHitIndex());
            q.setMaxResults(searchPagingOpt.value().getPageSize());
        }

        return q.getResultList();
    }
}
