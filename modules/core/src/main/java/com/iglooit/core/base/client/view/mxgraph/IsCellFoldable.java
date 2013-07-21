package com.iglooit.core.base.client.view.mxgraph;

/**
 * Created by IntelliJ IDEA.
 * User: ftsang
 * Date: 03/02/2011
 * Time: 6:58:04 PM
 */
public interface IsCellFoldable
{
    boolean check(Cell c, IsCellFoldable defaultChecker);
}
