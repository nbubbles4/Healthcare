/*
 * Copyright (c) 2004-2013 The YAWL Foundation. All rights reserved.
 * The YAWL Foundation is a collaboration of individuals and
 * organisations who are committed to improving workflow technology.
 *
 * This file is part of YAWL. YAWL is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation.
 *
 * YAWL is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General
 * Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with YAWL. If not, see <http://www.gnu.org/licenses/>.
 */

package org.yawlfoundation.yawl.editor.ui.actions.palette;

import org.yawlfoundation.yawl.editor.ui.swing.TooltipTogglingWidget;
import org.yawlfoundation.yawl.editor.ui.swing.menu.Palette;

import javax.swing.*;

public class MultipleCompositeTaskAction extends PaletteAction implements TooltipTogglingWidget {

    {
        putValue(Action.SHORT_DESCRIPTION, getDisabledTooltipText());
        putValue(Action.NAME, "Multiple Composite Task");
        putValue(Action.LONG_DESCRIPTION, "Multiple Instance Composite Task");
        putValue(Action.SMALL_ICON, getPaletteIcon("PaletteMultipleCompositeTask"));
    }

    public MultipleCompositeTaskAction() { super(); }

    public String getEnabledTooltipText() { return " Multiple Instance Composite Task "; }


    public String getButtonStatusText() {
        return getClickAnywhereText() + "multiple instance composite task.";
    }

    public Palette.SelectionState getSelectionID() {
        return Palette.SelectionState.MULTIPLE_COMPOSITE_TASK;
    }
}
