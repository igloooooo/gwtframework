package com.iglooit.core.base.client.presenter;

import com.clarity.core.base.client.event.DrillDownNavLoadEvent;
import com.clarity.core.base.client.mvp.DefaultPresenter;
import com.clarity.core.base.client.widget.layoutcontainer.BreadcrumbTrail;
import com.clarity.core.base.client.widget.layoutcontainer.DrillDownNavContainer;
import com.clarity.core.base.client.widget.layoutcontainer.DrillDownNavEntity;
import com.clarity.core.command.client.CommandServiceClientImpl;
import com.clarity.commons.iface.type.NonSerOpt;
import com.clarity.commons.iface.type.Tuple2;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.google.gwt.event.shared.HandlerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrillDownNavContainerPresenter extends DefaultPresenter<DrillDownNavContainer>
{
    private final CommandServiceClientImpl commandService;
    private final HandlerManager globalEventBus;

    private BreadcrumbTrail<DrillDownNavEntity> breadcrumbTrail;
    private Map<String, DrillDownNavEntity> drillDownEntityMap;

    public DrillDownNavContainerPresenter(DrillDownNavContainer display,
                                          CommandServiceClientImpl commandService,
                                          HandlerManager globalEventBus)
    {
        super(display, globalEventBus);
        this.commandService = commandService;
        this.globalEventBus = globalEventBus;

        breadcrumbTrail = new BreadcrumbTrail<DrillDownNavEntity>();
        drillDownEntityMap = new HashMap<String, DrillDownNavEntity>();
    }

    public BreadcrumbTrail<DrillDownNavEntity> getBreadcrumbTrail()
    {
        return breadcrumbTrail;
    }

    public int getBreadcrumbLength()
    {
        return breadcrumbTrail.getBreadcrumbList().size();
    }

    public void cleanBreadcrumbs()
    {
        breadcrumbTrail.getBreadcrumbList().clear();
        getDisplay().getBreadcrumbContainer().removeAll();
    }

    /**
     * if the breadcrumb list is existing already, then add it to the current drill down entity as the parent
     * else create a new parent drill down entity (addHomeBreadcrumb() method) based on the
     * current view mode(geo,location, circuit, etc)
     */
    public void addToBreadcrumbs(DrillDownNavEntity drillDownEntity,
                                 boolean isBreadcrumbClick)
    {
        if (isBreadcrumbClick)
        {
            resetBreadcrumbs(drillDownEntity);
        }
        else
        {
            drillDownEntityMap.put(drillDownEntity.getEntityId(), drillDownEntity);

            drillDownEntity.getAnchor().setText(drillDownEntity.getText());
            drillDownEntity.setParent(NonSerOpt.some(breadcrumbTrail.getLast()));
            resetBreadcrumbs(drillDownEntity);
        }
    }

    public void addHomeBreadcrumb(DrillDownNavEntity drillDownEntity,
                                  List<Tuple2<String, String>> attributes)
    {
        drillDownEntity.setAttributes(attributes);
        drillDownEntityMap.put(drillDownEntity.getEntityId(), drillDownEntity);

        drillDownEntity.removeAnchorSeparator();
        drillDownEntity.setParent(NonSerOpt.<DrillDownNavEntity>none());
        resetBreadcrumbs(drillDownEntity);
    }

    /**
     * this will show only the breadcrumbs up to the given drilldownEntity
     *
     * @param drillDownEntity
     */
    public void resetBreadcrumbs(DrillDownNavEntity drillDownEntity)
    {
        breadcrumbTrail.rearrangeBreadcrumbsInOrder(drillDownEntity);
        getDisplay().showBreadcrumbs(breadcrumbTrail);
        getDisplay().layout(true);
    }

    /**
     * this will show only the breadcrumbs up to the given drilldownEntity
     *
     * @param entityId
     */
    public void resetBreadcrumbsById(String entityId)
    {
        DrillDownNavEntity entity = drillDownEntityMap.get(entityId);

        breadcrumbTrail.rearrangeBreadcrumbsInOrder(entity);
        getDisplay().showBreadcrumbs(breadcrumbTrail);
        getDisplay().layout(true);
    }

    @Override
    public void bind()
    {
        getDisplay().addBackButtonSelectionListener(new SelectionListener<ButtonEvent>()
        {
            @Override
            public void componentSelected(ButtonEvent ce)
            {
                handleBackButtonClick();
            }
        });

        addHandlerRegistration(globalEventBus.addHandler(
            DrillDownNavLoadEvent.TYPE, new DrillDownNavLoadEvent.Handler()
        {
            @Override
            public void handle(DrillDownNavLoadEvent event)
            {
                if (event.isBreadcrumbClick())
                {

                }
                else
                {
                    if (event.getDrillDownEntity() == null)
                        return;

                    List<Tuple2<String, String>> attributesList = getDrillDownAttributeList();
                    for (Tuple2<String, String> attr : event.getDrillDownEntity().getAttributes())
                    {
                        attributesList.add(attr);
                    }

                    if (breadcrumbTrail.getBreadcrumbList().size() > 1)
                        breadcrumbTrail.getLast().setAttributes(attributesList);

                    addToBreadcrumbs(event.getDrillDownEntity(), event.isBreadcrumbClick());
                }
            }
        }));
    }

    private List<Tuple2<String, String>> getDrillDownAttributeList()
    {
        List<Tuple2<String, String>> attributeList = new ArrayList<Tuple2<String, String>>();
        if (breadcrumbTrail.getBreadcrumbList().size() > 1)
        {
        }
        return attributeList;
    }

    private void handleBackButtonClick()
    {
        DrillDownNavEntity drillDownEntity = breadcrumbTrail.getSecondLastBreadcrumb();
        drillDownEntity.doOnClick();
    }
}
