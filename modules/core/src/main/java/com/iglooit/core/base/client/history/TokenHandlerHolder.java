package com.iglooit.core.base.client.history;

import com.clarity.commons.iface.util.StringUtil;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zsun
 * Date: 22/02/2012
 * Time: 4:44:53 PM
 * To change this template use File | Settings | File Templates.
 */
public final class TokenHandlerHolder
{
    private static TokenHandlerHolder theTokenHandlerHolder = null;

    private static Map<String, ITokenHandler> tokenHandlerMap = new HashMap<String, ITokenHandler>();;

    public static TokenHandlerHolder getInstance()
    {
        if (theTokenHandlerHolder == null)
            theTokenHandlerHolder = new TokenHandlerHolder();

        return theTokenHandlerHolder;
    }

    private TokenHandlerHolder()
    {

        addValueChangeHandler();
    }

    public void createToken(String id)
    {
        History.newItem(id);
    }

    public String createToken(String tokenKey, Map<String, String> params)
    {
        if (tokenKey == null || tokenKey.isEmpty()
            || params == null || params.isEmpty())
            return "";
        String id = "".concat(tokenKey).concat(TokenConstants.PARAM_START);
        for (Map.Entry<String, String> entry : params.entrySet())
        {
            id = id.concat(entry.getKey()).concat(TokenConstants.PARAM_PAIR_SEPARATOR)
                 .concat(entry.getValue()).concat(TokenConstants.PARAM_LIST_SEPARATOR);
        }
        id = id.substring(0, id.length() - 1); // remove the last char
        createToken(id);
        return id;
    }

    public void resolveToken(String token)
    {
        if (!StringUtil.isEmpty(token))
            handleToken(token);
        else
            return;
    }

    private void handleToken(String token)
    {
        if (StringUtil.isEmpty(token))
            return;
        String[] tokens = token.split(TokenConstants.PARAM_START);
        if (tokens == null || tokens.length < 1)
            return;
        String objectType = tokens[0];
        if (tokenHandlerMap.containsKey(objectType))
        {
            ITokenHandler tokenHandler = tokenHandlerMap.get(objectType);
            String params = token.substring(objectType.length() + 1, token.length());
            tokenHandler.handleToken(params);
        }
    }

    public void addValueChangeHandler()
    {
        History.addValueChangeHandler(new ValueChangeHandler<String>()
        {
            public void onValueChange(ValueChangeEvent<String> event)
            {
//                String historyToken = event.getValue();
//                System.out.println("onValueChange historyToken = " + historyToken);
//                handleToken(historyToken);
            }
        });
    }


    public static void addTokenHandler(String tokenType, ITokenHandler handler)
    {
        tokenHandlerMap.put(tokenType, handler);
    }

}
