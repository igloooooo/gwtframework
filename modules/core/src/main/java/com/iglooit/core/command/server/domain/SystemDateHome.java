package com.iglooit.core.command.server.domain;

import com.iglooit.commons.iface.domain.DateCacheEntry;
import com.iglooit.commons.iface.type.AppX;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component("systemDateHome")
public class SystemDateHome
{
    private static final Log LOG = LogFactory.getLog(SystemDateHome.class);
    public static final String BEAN_NAME = "timeHome";

    private JdbcTemplate jdbc;
    private String systemTimestampQuery = "SELECT systimestamp as sys_time FROM dual";

    private Date serverDate;

    public DateCacheEntry getDate()
    {
        return getDate(new Date());
    }

    public DateCacheEntry getDate(Date localDate)
    {
        serverDate = getSystemDateFromDB();
        return new DateCacheEntry(localDate, serverDate);
    }

    @Resource
    protected void setOssDataSource(@Qualifier("ossDataSource") DataSource ds)
    {
        jdbc = new JdbcTemplate(ds);
    }

    protected Date getSystemDateFromDB()
    {
        List<Date> dates =
            jdbc.query(systemTimestampQuery,
                new Object[]{},
                new RowMapper<Date>()
                {
                    public Date mapRow(ResultSet rs, int rowNum) throws SQLException
                    {
                        return rs.getDate(1);
                    }
                });

        if (dates.size() != 1)
        {
            throw new AppX("Couldn't get the date from database.");
        }
        serverDate = dates.get(0);
        if (LOG.isDebugEnabled())
        {
            LOG.debug("DB Date: " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS Z").format(serverDate));
        }
        return serverDate;
    }
}
