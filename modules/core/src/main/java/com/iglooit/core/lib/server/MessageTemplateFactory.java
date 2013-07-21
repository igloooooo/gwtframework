package com.iglooit.core.lib.server;

import com.clarity.commons.iface.domain.SystemDateProvider;
import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.type.Option;
import com.clarity.commons.iface.type.Tuple2;
import com.clarity.core.account.iface.domain.UserRole;
import com.clarity.core.iface.SecurityService;
import com.clarity.core.lib.iface.BssTimeUtil;
import com.clarity.core.lib.server.util.MessageTemplateBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ftsang
 * Date: 26/09/12
 * Time: 3:02 PM
 *
 * Some placeholders are provided by default:
 * ${.date} - current date YYYY-MM-DD
 * ${.timestamp} - current date time YYYY-MM-DD HH:mm
 * ${.user} - logged in user
 *
 */
@Component
public class MessageTemplateFactory
{
    @Resource
    private SecurityService securityService;

    public String generateMessage(String template, final Map<String, String> data)
    {
        MessageTemplateBuilder builder = new MessageTemplateBuilder(template,
            new MessageTemplateBuilder.FindValueDelegate()
            {
                @Override
                public Tuple2<String, Boolean> findValue(String keyword)
                {
                    return getValue(keyword, data);
                }
            });

        return builder.toString();
    }

    private Tuple2<String, Boolean> getValue(String keyword, Map<String, String> notificationData)
    {
        String result;
        boolean found = true;
        if (keyword.equals(".timestamp"))
        {
            SimpleDateFormat sdf = new SimpleDateFormat(BssTimeUtil.DATEFORMAT_1);
            result = sdf.format(SystemDateProvider.now());
        }
        else if (keyword.equals(".date"))
        {
            SimpleDateFormat sdf = new SimpleDateFormat(ServerTimeUtil.YYYY_MM_DD);
            result = sdf.format(SystemDateProvider.now());
        }
        else if (keyword.equals(".user"))
        {
            Option<UserRole> op = securityService.getActiveUserRole();
            if (op.isNone())
                throw new AppX("User is not logged in");
            result = op.value().getUsername().toUpperCase();
        }
        else
        {
            result = notificationData.get(keyword);
            if (result == null)
                found = notificationData.containsKey(keyword);
        }

        return new Tuple2<>(result, found);
    }

}
