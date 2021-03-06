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

package org.yawlfoundation.yawl.editor.ui.actions;

import org.yawlfoundation.yawl.editor.ui.YAWLEditor;
import org.yawlfoundation.yawl.editor.ui.net.NetGraph;
import org.yawlfoundation.yawl.editor.ui.swing.MessageDialog;
import org.yawlfoundation.yawl.editor.ui.swing.NetsPane;
import org.yawlfoundation.yawl.editor.ui.util.ResourceLoader;

import javax.swing.*;
import java.awt.event.ActionEvent;

public abstract class YAWLBaseAction extends AbstractAction {

    protected ImageIcon getMenuIcon(String iconName) {
        return ResourceLoader.getMenuIcon(iconName);
    }


    public void actionPerformed(ActionEvent e) {
        MessageDialog.info("The action labelled '" + getValue(Action.NAME) +
                "' is not yet implemented.\n\n", "No Action");
    }

    public NetGraph getGraph() {
        NetsPane pane = YAWLEditor.getNetsPane();
        return pane != null ? pane.getSelectedGraph() : null;
    }

    public boolean shouldBeEnabled() {
        return true;
    }

    public boolean shouldBeVisible() {
        return true;
    }
}
