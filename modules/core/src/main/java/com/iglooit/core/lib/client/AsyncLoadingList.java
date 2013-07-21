package com.iglooit.core.lib.client;

import com.clarity.commons.iface.type.AppX;
import com.clarity.core.lib.iface.TypeConverter;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AsyncLoadingList<T>
{
    private boolean loadedOnce = false;
    private Loader<T> loader;
    private final List<T> currentHits;
    private final List<ListUpdatedHandler<T>> listUpdatedHandlers;
    private final ListLoadCallback<T> callback;
    //private static final boolean RELOAD_ON_REBIND = true;

    public AsyncLoadingList()
    {
        this.currentHits = new ArrayList<T>();
        this.listUpdatedHandlers = new ArrayList<ListUpdatedHandler<T>>();
        callback = new ListLoadCallback<T>()
        {
            public void setList(List<T> ts)
            {
                updateList(ts);
            }
        };

    }

    public AsyncLoadingList(Loader<T> loader)
    {
        this();
        this.loader = loader;
    }

    public <D> AsyncLoadingList(AsyncLoadingList<D> dLoader, final TypeConverter<D, T> typeConverter)
    {
        this();
        final AsyncLoadingList<T> outerLoader = this;
        dLoader.addListHandler(new ListUpdatedHandler<D>()
        {
            public void handle(List<D> updatedList)
            {
                List<T> ts = new ArrayList<T>();
                for (D d : updatedList)
                    ts.add(typeConverter.convert(d));
                outerLoader.updateList(ts);
            }
        });
    }

    public void updateList(List<T> updatedList)
    {
        loadedOnce = true;
        currentHits.clear();
        currentHits.addAll(updatedList);
        notifyHandlers();
    }

    private void notifyHandlers()
    {
        for (ListUpdatedHandler<T> listUpdatedHandler : listUpdatedHandlers)
            listUpdatedHandler.handle(Collections.unmodifiableList(currentHits));
    }

    public synchronized HandlerRegistration addListHandler(
        final BeforeLoading<T> beforeLoadingHandler,
        final ListUpdatedHandler<T> listUpdatedHandler)
    {
        beforeLoadingHandler.beforeLoading();
        load();
        listUpdatedHandler.handle(Collections.unmodifiableList(currentHits));
        listUpdatedHandlers.add(listUpdatedHandler);
        return new HandlerRegistration()
        {
            public void removeHandler()
            {
                listUpdatedHandlers.remove(listUpdatedHandler);
            }
        };
    }

    public synchronized HandlerRegistration addListHandler(final ListUpdatedHandler<T> listUpdatedHandler)
    {
        //if (!loadedOnce || RELOAD_ON_REBIND)
        load();
        listUpdatedHandler.handle(Collections.unmodifiableList(currentHits));
        listUpdatedHandlers.add(listUpdatedHandler);
        return new HandlerRegistration()
        {
            public void removeHandler()
            {
                listUpdatedHandlers.remove(listUpdatedHandler);
            }
        };
    }

    public void setLoader(Loader<T> loader)
    {
        this.loader = loader;
        loadedOnce = false;
        load();
    }

    public void load()
    {
        if (loader != null)
        {
            loadedOnce = true;
            loader.load(callback);
        }
        else
        {
            notifyHandlers();
        }
    }

    public void loadOnce(final ListLoadCallback<T> singleCallback)
    {
        if (loader == null)
            throw new AppX("Cannot have async loading list with null loader");
        loadedOnce = true;
        loader.load(new ListLoadCallback<T>()
        {
            public void setList(List<T> ts)
            {
                singleCallback.setList(ts);
                callback.setList(ts);
            }
        });
    }

    public interface ListLoadCallback<T>
    {
        void setList(List<T> list);
    }

    public interface BeforeLoading<T>
    {
        void beforeLoading();
    }

    public interface ListUpdatedHandler<T>
    {
        void handle(List<T> updatedList);
    }

    public interface Loader<T>
    {
        void load(ListLoadCallback<T> callback);
    }
}
