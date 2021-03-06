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

package org.yawlfoundation.yawl.editor.ui.properties.data.binding;

import org.yawlfoundation.yawl.editor.core.data.YDataHandler;
import org.yawlfoundation.yawl.editor.ui.YAWLEditor;
import org.yawlfoundation.yawl.editor.ui.properties.data.DataUtils;
import org.yawlfoundation.yawl.editor.ui.properties.data.MultiInstanceHandler;
import org.yawlfoundation.yawl.editor.ui.properties.data.VariableRow;
import org.yawlfoundation.yawl.editor.ui.properties.data.validation.BindingTypeValidator;
import org.yawlfoundation.yawl.editor.ui.properties.dialog.component.ButtonBar;
import org.yawlfoundation.yawl.editor.ui.swing.MessageDialog;
import org.yawlfoundation.yawl.editor.ui.util.SplitPaneUtil;
import org.yawlfoundation.yawl.editor.ui.util.XMLUtilities;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Michael Adams
 * @date 21/11/2013
 */
public abstract class AbstractDataBindingDialog extends JDialog implements ActionListener {

    private java.util.List<VariableRow> _netVarList;
    private java.util.List<VariableRow> _taskVarList;
    private Map<String, String> _undoMap;
    private VariableRow _currentRow;
    private QueryPanel _queryPanel;
    private MIQueryPanel _miQueryPanel;
    private MultiInstanceHandler _miHandler;
    private ButtonBar _buttonBar;
    protected boolean _initialising;


    public AbstractDataBindingDialog(String taskID, VariableRow row,
                                     java.util.List<VariableRow> netVarList,
                                     java.util.List<VariableRow> taskVarList) {
        super();
        _initialising = true;
        _netVarList = netVarList;
        _taskVarList = taskVarList;
        _currentRow = row;
        _undoMap = new HashMap<String, String>();
        setTitle(makeTitle(taskID));
        add(getContent(row));
        init();
        setMinimumSize(new Dimension(426, row.isMultiInstance() ? 560 : 440));
        pack();
        setLocationRelativeTo(YAWLEditor.getInstance());
    }


    protected abstract String makeTitle(String taskID);

    protected abstract String getMIPanelTitle();


    protected boolean isInitialising() { return _initialising; }

    protected java.util.List<VariableRow> getNetVarList() { return _netVarList; }

    protected java.util.List<VariableRow> getTaskVarList() { return _taskVarList; }

    protected VariableRow getCurrentRow() { return _currentRow; }

    protected void setCurrentRow(VariableRow row) { _currentRow = row; }


    protected VariableRow getNetVariableRow(String name) {
        return getNamedVariableRow(_netVarList, name);
    }


    protected VariableRow getTaskVariableRow(String name) {
        return getNamedVariableRow(_taskVarList, name);
    }


    protected abstract JPanel buildTargetPanel();

    protected abstract JPanel buildGeneratePanel();


    public boolean hasChanges() { return ! _undoMap.isEmpty(); }

    public void setMultiInstanceHandler(MultiInstanceHandler miHandler) {
        _miHandler = miHandler;
    }

    protected MultiInstanceHandler getMultiInstanceHandler() { return _miHandler; }

    protected boolean isValidBinding(String binding) {
        return ! binding.isEmpty() && _buttonBar.isOKEnabled();
    }

    protected void initContent(VariableRow row) {
        _queryPanel.setParentDialogOKButton(_buttonBar.getOK());
    }

    protected void setEditorText(String binding) {
        _queryPanel.setText(binding);
    }

    protected String getEditorText() { return _queryPanel.getText(); }

    protected void setMIEditorText(String query) {
        if (_miQueryPanel != null) _miQueryPanel.setText(query);
    }

    protected String getMIEditorText() {
        return _miQueryPanel != null ? _miQueryPanel.getText() : null;
    }


    protected void setTargetVariableName(String name) {
        _queryPanel.setTargetVariableName(name);
        if (_miQueryPanel != null) _miQueryPanel.setTargetVariableName(name);
    }

    protected void setTypeValidator(BindingTypeValidator validator) {
        _queryPanel.setTypeValidator(validator);
    }

    protected BindingTypeValidator getTypeValidator() {
        return _queryPanel.getTypeValidator();
    }

    protected String formatQuery(String query, boolean prettify) {
        return XMLUtilities.formatXML(query, prettify, true);
    }

    protected Map<String, String> getUndoMap() {
        return _undoMap;
    }


    protected String makeTitle(String bindType, String taskID) {
        StringBuilder sb = new StringBuilder();
        sb.append(bindType)
          .append(" Data Bindings for Task ")
          .append(taskID);
        return sb.toString();
    }


    protected boolean generateBinding(VariableRow source, VariableRow target) {
        if (source != null) {
            String binding = DataUtils.createBinding(source);
            java.util.List<String> errors = getTypeValidator().validate(binding);
            if (errors.isEmpty()) {
                setEditorText(binding);
                return true;
            }
            else {
                showGenerateBindingError(source, target);
                setEditorText("");
            }
        }
        return false;
    }


    protected int confirmSaveOnComboChange(int scope, String varName) {
        String scopeStr = (scope == YDataHandler.INPUT) ? "task" : "net";
        String msg = "The binding for " + scopeStr + " variable '" + varName +
                     "' has changed.\n Save the updated binding?";
        return JOptionPane.showConfirmDialog(this, msg);
    }



    private JPanel getContent(VariableRow row) {
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(new EmptyBorder(7, 7, 0, 7));
        content.add(buildTopPanel(), BorderLayout.NORTH);
        content.add(buildCentrePanel(row), BorderLayout.CENTER);
        _buttonBar = new ButtonBar(this);
        _buttonBar.setOKEnabled(true);
        content.add(_buttonBar, BorderLayout.SOUTH);
        initContent(row);
        return content;
    }


    private JPanel buildTopPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(buildTargetPanel());
        panel.add(buildGeneratePanel());
        return panel;
    }


    private JPanel buildCentrePanel(VariableRow row) {
        JPanel panel = new JPanel(new BorderLayout());
        buildQueryPanel(row.isMultiInstance());
        if (row.isMultiInstance()) {
            buildMiQueryPanel();
            JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true,
                    _queryPanel, _miQueryPanel);
            SplitPaneUtil splitPaneUtil = new SplitPaneUtil();
            splitPaneUtil.setupDivider(splitPane, false);
            splitPaneUtil.setDividerLocation(splitPane, 0.6d);
            panel.add(splitPane, BorderLayout.CENTER);
        }
        else {
            panel.add(_queryPanel, BorderLayout.CENTER);
        }
        return panel;
    }



    private JPanel buildQueryPanel(boolean isMultiInstance) {
        _queryPanel = new QueryPanel(this, ! isMultiInstance);
        return _queryPanel;
    }


    private JPanel buildMiQueryPanel() {
        _miQueryPanel = new MIQueryPanel(getMIPanelTitle());
        return _miQueryPanel;
    }


    private void init() {
        setModal(true);
        setResizable(true);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }


    private VariableRow getNamedVariableRow(java.util.List<VariableRow> varList,
                                            String name) {
        if (name != null) {
            for (VariableRow row : varList) {
                if (row.getName().equals(name)) return row;
            }
        }
        return null;
    }


    private void showGenerateBindingError(VariableRow source, VariableRow target) {
        StringBuilder s = new StringBuilder();
        s.append("Unable to generate binding due to incompatible data types.\n");
        s.append("\tSource variable '").append(source.getName());
        s.append("' has data type: ").append(source.getDataType()).append(".\n");
        s.append("\tTarget variable '").append(target.getName());
        s.append("' has data type: ").append(target.getDataType()).append(".\n");
        MessageDialog.error(this, s.toString(), "Generate Binding Error");
    }

}
