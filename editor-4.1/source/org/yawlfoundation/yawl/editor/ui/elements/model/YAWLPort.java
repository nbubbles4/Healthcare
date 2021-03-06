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

package org.yawlfoundation.yawl.editor.ui.elements.model;

import org.jgraph.graph.Edge;

import org.jgraph.graph.DefaultPort;


public class YAWLPort extends DefaultPort {

    private int position = YAWLTask.NOWHERE;

    public boolean acceptsIncomingFlows() {
        return hasConditionAsParent() || ! hasDecoratorAtThisPosition() &&
                ! hasJoinDecorator() &&
                (getEdges().size() == 0 || isTargetThis(getFirstEdge()));
    }

    public boolean generatesOutgoingFlows() {
        return hasConditionAsParent() || !hasDecoratorAtThisPosition() &&
                !hasSplitDecorator()
                && (getEdges().size() == 0 || isSourceThis(getFirstEdge()));
    }

    private boolean isSourceThis(Edge edge) {
        return edge.getSource() == this;
    }

    private boolean isTargetThis(Edge edge) {
        return edge.getTarget() == this;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    private Edge getFirstEdge() {
        return (Edge) getEdges().iterator().next();
    }

    private boolean hasDecoratorAtThisPosition() {
        if (getParent() instanceof YAWLTask) {
            YAWLTask task = (YAWLTask) getParent();
            if (task.hasDecoratorAtPosition(position)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasSplitDecorator() {
        if (getParent() instanceof YAWLTask) {
            YAWLTask task = (YAWLTask) getParent();
            if (task.hasSplitDecorator()) {
                return true;
            }
        }
        return false;
    }

    private boolean hasJoinDecorator() {
        if (getParent() instanceof YAWLTask ) {
            YAWLTask task = (YAWLTask) getParent();
            if (task.hasJoinDecorator()) {
                return true;
            }
        }
        return false;
    }

    private boolean hasConditionAsParent() {
        return getParent() instanceof Condition;
    }

    public YAWLTask getTask() {
        if (getParent() instanceof Decorator) {
            Decorator decorator = (Decorator) getParent();
            return decorator.getTask();
        } else if (getParent() instanceof YAWLTask) {
            return (YAWLTask) getParent();
        }
        return null;
    }

    public YAWLVertex getVertex() {
        if (getParent() instanceof Decorator) {
            Decorator decorator = (Decorator) getParent();
            return decorator.getTask();
        }
        return (YAWLVertex) getParent();
    }

    public String getVertexID() { return getVertex().getID(); }

    public String toString() {
        return "[parent = " + ((YAWLVertex) getParent()).getID() +
                " [id = " + this.hashCode() + ", flows = " + getEdges().size() + " ]";
    }
}
