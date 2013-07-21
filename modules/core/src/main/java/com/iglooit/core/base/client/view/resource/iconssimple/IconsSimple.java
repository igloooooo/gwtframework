package com.iglooit.core.base.client.view.resource.iconssimple;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

import javax.annotation.Resource;

@SuppressWarnings("deprecation")
public interface IconsSimple extends ImageBundle
{
    // default (black) opaque icons

    @Resource("eye.png")
    AbstractImagePrototype eye();

    @Resource("gear.png")
    AbstractImagePrototype gear();

    @Resource("minus-circle.png")
    AbstractImagePrototype minusCircle();

    @Resource("tick.png")
    AbstractImagePrototype tick();

    @Resource("question.png")
    AbstractImagePrototype question();

    @Resource("info.png")
    AbstractImagePrototype info();

    @Resource("user.png")
    AbstractImagePrototype user();

    @Resource("users.png")
    AbstractImagePrototype users();

    @Resource("user-group.png")
    AbstractImagePrototype userGroup();
    
    @Resource("reload.png")
    AbstractImagePrototype reload();
    
    @Resource("layers.png")
    AbstractImagePrototype layers();

    @Resource("layout-3col.png")
    AbstractImagePrototype layout3Col();

    @Resource("layout-2col.png")
    AbstractImagePrototype layout2Col();

    @Resource("layout-2col-wide-left.png")
    AbstractImagePrototype layout2ColWideLeft();

    @Resource("layout-2col-wide-right.png")
    AbstractImagePrototype layout2ColWideRight();

    @Resource("layout-1col.png")
    AbstractImagePrototype layout1Col();

    @Resource("arrow-head-down.png")
    AbstractImagePrototype arrowHeadDown();

    @Resource("arrow-head-right.png")
    AbstractImagePrototype arrowHeadRight();

    @Resource("arrow-nav-left.png")
    AbstractImagePrototype arrowNavLeft();

    @Resource("arrow-nav-right.png")
    AbstractImagePrototype arrowNavRight();

    @Resource("arrow-path-right.png")
    AbstractImagePrototype arrowPathRight();

    @Resource("arrow-up-mask.png")
    AbstractImagePrototype arrowUpMask();

    @Resource("arrow-down-mask.png")
    AbstractImagePrototype arrowDownMask();

    @Resource("arrow-right-mask.png")
    AbstractImagePrototype arrowRightMask();

    @Resource("search.png")
    AbstractImagePrototype search();

    @Resource("cross.png")
    AbstractImagePrototype cross();

    @Resource("clock.png")
    AbstractImagePrototype clock();

    @Resource("plus.png")
    AbstractImagePrototype plus();
    
    @Resource("printer.png")
    AbstractImagePrototype printer();

    @Resource("equipment.png")
    AbstractImagePrototype equipment();

    @Resource("frame.png")
    AbstractImagePrototype frame();

    @Resource("wireless-single.png")
    AbstractImagePrototype wirelessSingle();

    @Resource("graph-line.png")
    AbstractImagePrototype graphLine();

    @Resource("tree-nodes-nav.png")
    AbstractImagePrototype treeNodesNav();

    @Resource("monitor.png")
    AbstractImagePrototype monitor();

    @Resource("exclamation.png")
    AbstractImagePrototype exclamation();

    @Resource("transfer.png")
    AbstractImagePrototype transfer();

    @Resource("hierarchy-top.png")
    AbstractImagePrototype hierarchyTop();

    @Resource("hierarchy.png")
    AbstractImagePrototype hierarchy();

    @Resource("disable.png")
    AbstractImagePrototype disable();

    @Resource("play.png")
    AbstractImagePrototype play();

    @Resource("list.png")
    AbstractImagePrototype list();

    @Resource("document-bar.png")
    AbstractImagePrototype documentBar();

    @Resource("document-edit.png")
    AbstractImagePrototype documentEdit();

    @Resource("document-arrow-right.png")
    AbstractImagePrototype documentArrowRight();

    @Resource("link-external.png")
    AbstractImagePrototype linkExternal();

    @Resource("synchronise.png")
    AbstractImagePrototype synchronise();

    @Resource("aggregate.png")
    AbstractImagePrototype aggregate();

    @Resource("bell.png")
    AbstractImagePrototype bell();

    @Resource("calendar.png")
    AbstractImagePrototype calendar();


    // white opaque icons

    @Resource("exclamation-white.png")
    AbstractImagePrototype exclamationWhite();

    @Resource("tick-white.png")
    AbstractImagePrototype tickWhite();

    @Resource("cross-white.png")
    AbstractImagePrototype crossWhite();

    @Resource("arrow-nav-left-white.png")
    AbstractImagePrototype arrowNavLeftWhite();


    // large icons 32x32
    @Resource("widget-32.png")
    AbstractImagePrototype widget32();

    @Resource("gauge-32.png")
    AbstractImagePrototype gauge32();

    @Resource("hierarchy-32.png")
    AbstractImagePrototype hierarchy32();

    @Resource("graph-line-32.png")
    AbstractImagePrototype graphLine32();

    @Resource("graph-bullet-32.png")
    AbstractImagePrototype graphBullet32();

}
