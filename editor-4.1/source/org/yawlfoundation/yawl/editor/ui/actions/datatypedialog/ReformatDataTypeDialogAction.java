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

package org.yawlfoundation.yawl.editor.ui.actions.datatypedialog;

import org.yawlfoundation.yawl.editor.ui.actions.YAWLBaseAction;
import org.yawlfoundation.yawl.editor.ui.data.editorpane.ValidityEditorPane;
import org.yawlfoundation.yawl.editor.ui.swing.menu.DataTypeDialogToolBarMenu;
import org.yawlfoundation.yawl.editor.ui.util.XMLUtilities;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ReformatDataTypeDialogAction extends YAWLBaseAction {

    {
        putValue(Action.SHORT_DESCRIPTION, " (Re)format text");
        putValue(Action.NAME, "ReformatText");
        putValue(Action.LONG_DESCRIPTION, "Reformat Text");
        putValue(Action.SMALL_ICON, getMenuIcon("autoFormat"));
        putValue(Action.MNEMONIC_KEY, new Integer(java.awt.event.KeyEvent.VK_R));
    }


    public void actionPerformed(ActionEvent event) {
        ValidityEditorPane pane = DataTypeDialogToolBarMenu.getEditorPane().getEditor();
        int caretPos = pane.getCaretPosition();
        pane.setText(XMLUtilities.formatXML(pane.getText(), true, false));
        pane.setCaretPosition(Math.min(caretPos, pane.getText().length() -1));
    }
}