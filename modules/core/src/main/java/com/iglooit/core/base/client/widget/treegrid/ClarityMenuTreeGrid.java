package com.iglooit.core.base.client.widget.treegrid;

import com.clarity.core.base.client.PrivilegeResolver;
import com.clarity.core.base.client.model.MenuTreeModel;
import com.clarity.core.base.client.view.resource.Resource;
import com.clarity.core.base.client.widget.SecuredWidget;
import com.clarity.core.base.client.widget.SecuredWidgetDelegate;
import com.clarity.core.security.iface.access.domain.PrivilegeConst;
import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.dom.client.Element;

/**
 * Custom tree grid to represent a menu tree component.
 * <p/>
 * The default behavior of the tree grid does not
 * automatically keep the 'state' of a selected child node
 * so we have to keep track of the selected child node
 * manually so that when the tree gets collapsed the
 * selected child node is still shown as highlighted when the
 * tree is expanded.
 *
 * @param <M> model data
 */
public class ClarityMenuTreeGrid<M extends ModelData> extends TreeGrid
{

    private BaseTreeModel selectedNode;

    /**
     * Tree Grid constructor
     *
     * @param store
     * @param cm
     */
    public ClarityMenuTreeGrid(TreeStore store, ColumnModel cm)
    {
        super(store, cm);
        init();
    }

    /**
     * Initializes the grid
     */
    private void init()
    {
        setHideHeaders(true);
        setAutoExpandColumn(cm.getColumn(0).getId());
        getStyle().setLeafIcon(null);
        getStyle().setNodeCloseIcon(null);
        getStyle().setNodeOpenIcon(null);
        getStyle().setJointCollapsedIcon(Resource.ICONS_SIMPLE.arrowHeadRight());
        getStyle().setJointExpandedIcon(Resource.ICONS_SIMPLE.arrowHeadDown());

        initView();
        initListeners();
    }

    /**
     * Initializes the view
     */
    private void initView()
    {
        setView(new ClarityMenuTreeGridView()
        {
            @Override
            public void onJointChange(TreeNode node, TreePanel.Joint joint)
            {
                super.onJointChange(node, joint);

                if (selectedNode != null && selectedNode.getParent() != null && TreePanel.Joint.EXPANDED == joint)
                {
                    String selectedParent = selectedNode.getParent().get(MenuTreeModel.LABEL);
                    String expandedNode = node.getModel().get(MenuTreeModel.LABEL);
                    if (expandedNode.equals(selectedParent))
                    {
                        getView().getRow(selectedNode).addClassName("x-grid3-row-selected");
                    }
                }

            }
        });
    }

    /**
     * Initializes the listeners
     */
    private void initListeners()
    {
        addListener(Events.OnClick, new Listener<GridEvent<M>>()
        {
            @Override
            public void handleEvent(GridEvent<M> modelDataGridEvent)
            {
                BaseTreeModel newSelectedNode = (BaseTreeModel)modelDataGridEvent.getModel();
                if (newSelectedNode != null && newSelectedNode.getChildCount() == 0)
                {
                    if (selectedNode != null)
                    {
                        final Element row = getView().getRow(selectedNode);
                        if (row != null) row.removeClassName("x-grid3-row-selected");
                    }
                    selectedNode = newSelectedNode;
                }
            }
        });


        addListener(Events.Resize, new Listener<GridEvent<M>>()
        {
            @Override
            public void handleEvent(GridEvent<M> modelDataGridEvent)
            {
                getView().refresh(false);
            }
        });

    }

    /**
     * Returns the selected node
     *
     * @return selected node
     */
    public BaseTreeModel getSelectedNode()
    {
        return selectedNode;
    }

    public void select(BaseTreeModel model, boolean keepExisting)
    {
        final TreeGrid.TreeNode treeNode = findNode(model);
        if (treeNode != null)
        {
            //this ensures that we are using the data in the tree itself
            // ('model' might not be in the tree but used for selecting the TreeNode based on equals())
            getSelectionModel().select(treeNode.getModel(), keepExisting);
        }
    }

    public static class Secured<M extends ModelData> extends ClarityMenuTreeGrid<M> implements SecuredWidget
    {
        private SecuredWidgetDelegate delegate;

        public Secured(PrivilegeResolver resolver, PrivilegeConst privilege, TreeStore store, ColumnModel cm)
        {
            super(store, cm);
            delegate = new SecuredWidgetDelegate(resolver, privilege);
            delegate.setSecuredWidget(this);
        }

        @Override
        public void setEnabled(boolean enabled)
        {
            delegate.setEnabled(enabled);
        }

        @Override
        public boolean isRendered()
        {
            return delegate.isRendered();
        }

        @Override
        public void superSetEnabled(boolean enabled)
        {
            super.setEnabled(enabled);
        }

        @Override
        public boolean superIsRendered()
        {
            return super.isRendered();
        }
    }
}
