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

package org.yawlfoundation.yawl.configuration.menu.action;

import org.yawlfoundation.yawl.configuration.element.TaskConfiguration;
import org.yawlfoundation.yawl.configuration.element.TaskConfigurationCache;
import org.yawlfoundation.yawl.editor.ui.YAWLEditor;
import org.yawlfoundation.yawl.editor.ui.elements.model.YAWLMultipleInstanceTask;
import org.yawlfoundation.yawl.editor.ui.elements.model.YAWLTask;
import org.yawlfoundation.yawl.editor.ui.swing.TooltipTogglingWidget;
import org.yawlfoundation.yawl.elements.YMultiInstanceAttributes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MultipleInstanceConfigurationAction extends ProcessConfigurationAction
        implements TooltipTogglingWidget {


	{
	    putValue(Action.SHORT_DESCRIPTION, getDisabledTooltipText());
	    putValue(Action.NAME, "Multiple Instances...");
	    putValue(Action.LONG_DESCRIPTION, "Configure the multiple instances for this task.");
        putValue(Action.SMALL_ICON, getMenuIcon("application_cascade"));
	  }


	public MultipleInstanceConfigurationAction(){
	}

	public void actionPerformed(ActionEvent event) {
		if(task instanceof YAWLMultipleInstanceTask){
				final YAWLMultipleInstanceTask task = (YAWLMultipleInstanceTask) this.task;
				EventQueue.invokeLater(new Runnable() {
		            public void run() {
		                MultipleInstanceConfigureJDialog dialog = new MultipleInstanceConfigureJDialog(new JFrame(), task);
		                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

		                });
		                dialog.setLocationRelativeTo(YAWLEditor.getInstance());
                    dialog.setResizable(false);
		                dialog.setVisible(true);
		            }
		        });
		}
	}

    public void setEnabled(boolean enable) {
        if (net == null || task == null) {
            super.setEnabled(false);
            return;
        }
        TaskConfiguration config = TaskConfigurationCache.getInstance()
                .get(net.getNetModel(), task);
        super.setEnabled(enable && config != null && config.isConfigurable() &&
                (task instanceof YAWLMultipleInstanceTask));
    }

	public String getDisabledTooltipText() {
		return null;
	}


	public String getEnabledTooltipText() {
		return "Configure the multiple instances for this task.";
	}
	/*
	 * MultipleInstanceConfigureJDialog.java
	 *
	 * Created on 25/01/2010, 11:08:44 AM
	 */

	/**
	 *
	 * @author jingxin
	 */
	private class MultipleInstanceConfigureJDialog extends JDialog {

		private final YAWLMultipleInstanceTask task;
		private long maxInstance;
		private long minInstance;
		private long threshhold;
		private String createType;

	    /** Creates new form MultipleInstanceConfigureJDialog */
	    public MultipleInstanceConfigureJDialog(Frame parent, YAWLMultipleInstanceTask task) {
	        super(parent, true);
	        this.task = task;
	        initComponents();

	    }

	    /** This method is called from within the constructor to
	     * initialize the form.
	     * WARNING: Do NOT modify this code. The content of this method is
	     * always regenerated by the Form Editor.
	     */
	    @SuppressWarnings("unchecked")
	    // <editor-fold defaultstate="collapsed" desc="Generated Code">
	    private void initComponents() {

	    	this.setTitle("Multiple Instance Task Configuration");

	        reduceMaxLabel = new JLabel();
	        reduceMaxjTextField = new JTextField();
	        increaseMinjLabel1 = new JLabel();
	        IncreaseMinjTextField = new JTextField();
	        IncreaseThresholdjLabel = new JLabel();
	        threshholdjTextField = new JTextField();

	        ForbidDynamiccheckbox = new Checkbox();
	        donejButton = new JButton();


	        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

	        reduceMaxLabel.setText("Reduce maximal number of instances to");

	        increaseMinjLabel1.setText("Increase minimal number of instances to");

	        IncreaseThresholdjLabel.setText("Increase Threshold");



	        this.maxInstance = task.getMaximumInstances();
	        this.minInstance = task.getMinimumInstances();
	        this.threshhold = task.getContinuationThreshold();
	        this.createType = task.getInstanceCreationType();


            TaskConfiguration config = TaskConfigurationCache.getInstance()
                    .get(net.getNetModel(), (YAWLTask) task);
	        this.reduceMaxjTextField.setText(((Long)config.getConfigurationInfor().getReduceMax()).toString());
	        this.IncreaseMinjTextField.setText(((Long)config.getConfigurationInfor().getIncreaseMin()).toString());
	        this.threshholdjTextField.setText(((Long)config.getConfigurationInfor().getIncreaseThreshold()).toString());
	        if(config.getConfigurationInfor().isForbidDynamic()){
	        	this.ForbidDynamiccheckbox.setEnabled(false);
	        }else {
	        	this.ForbidDynamiccheckbox.setEnabled(true);
	        }


	        ForbidDynamiccheckbox.setLabel("Forbid dynamic creation of instances");
	        donejButton.setText("Done");
	        donejButton.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(ActionEvent evt) {
	                donejButtonActionPerformed(evt);
	            }
	        });

	        GroupLayout layout = new GroupLayout(getContentPane());
	        getContentPane().setLayout(layout);
	        layout.setHorizontalGroup(
	            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
	            .addGroup(layout.createSequentialGroup()
	                .addContainerGap()
	                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
	                    .addComponent(ForbidDynamiccheckbox, GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
	                    .addComponent(donejButton)
	                    .addGroup(layout.createSequentialGroup()
	                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
	                            .addComponent(IncreaseThresholdjLabel, GroupLayout.Alignment.LEADING,
                                      GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
	                            .addGroup(GroupLayout.Alignment.LEADING,
                                  layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
	                                .addComponent(increaseMinjLabel1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                                .addComponent(reduceMaxLabel, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
	                        .addGap(10, 10, 10)
	                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
	                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
	                                .addComponent(IncreaseMinjTextField, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
	                                .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
	                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
	                                    .addComponent(reduceMaxjTextField, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)))
	                            .addComponent(threshholdjTextField, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE))))
	                .addContainerGap())
	        );
	        layout.setVerticalGroup(
	            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
	            .addGroup(layout.createSequentialGroup()
	                .addGap(37, 37, 37)
	                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	                    .addComponent(reduceMaxLabel, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
	                    .addComponent(reduceMaxjTextField, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
	                .addGap(22, 22, 22)
	                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	                    .addComponent(increaseMinjLabel1, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
	                    .addComponent(IncreaseMinjTextField, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
	                .addGap(18, 18, 18)
	                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	                    .addComponent(IncreaseThresholdjLabel, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
	                    .addComponent(threshholdjTextField, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
	                .addGap(33, 33, 33)
	                .addComponent(ForbidDynamiccheckbox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
	                .addGap(37, 37, 37)
	                .addComponent(donejButton)
	                .addContainerGap(25, Short.MAX_VALUE))
	        );

	        pack();
	    }// </editor-fold>

	    private void donejButtonActionPerformed(ActionEvent evt) {
	    	long newMax = Long.parseLong(this.reduceMaxjTextField.getText());
	    	long newMin = Long.parseLong(this.IncreaseMinjTextField.getText());
	    	long newThreshold = Long.parseLong(this.threshholdjTextField.getText());
	    	if( (newMax <= this.maxInstance)&&(newMax >= newMin)
	    			&&(newMin <= newMax) &&(newMin >= this.minInstance)
	    			&& (newThreshold >=this.threshhold) && (newThreshold >= newMin)
	    			&& (newThreshold<= newMax)){
			    	if(this.ForbidDynamiccheckbox.getState()){
			    		this.task.setInstanceCreationType(YMultiInstanceAttributes.CREATION_MODE_STATIC);
			    	}
                TaskConfiguration config = TaskConfigurationCache.getInstance()
                        .get(net.getNetModel(), (YAWLTask) task);
                config.getConfigurationInfor().setReduceMax(newMax);
                config.getConfigurationInfor().setIncreaseMin(newMin);
                config.getConfigurationInfor().setIncreaseThreshold(newThreshold);
			    	this.setVisible(false);

	    	} else {
	    		 JOptionPane.showMessageDialog(null, "Invalid input parameter!");
	    	}
	    	//`this.
	    }


	    // Variables declaration - do not modify
	    private Checkbox ForbidDynamiccheckbox;
	    private JTextField IncreaseMinjTextField;
	    private JLabel IncreaseThresholdjLabel;
	    private JLabel increaseMinjLabel1;
	    //private javax.swing.JLabel ForbidDynamicjLabel;
	    private JLabel reduceMaxLabel;
	    private JTextField reduceMaxjTextField;
	    private JTextField threshholdjTextField;
	    private JButton donejButton;
	    // End of variables declaration

	}

}
