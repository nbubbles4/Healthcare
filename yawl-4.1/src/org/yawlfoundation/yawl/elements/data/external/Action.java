package org.yawlfoundation.yawl.elements.data.external;

import org.jdom2.Element;

/**
 * TESTING - MAY NOT USE THIS APPROACH, IF NOT, DELETE
 * @author noha
 */
public interface Action {
    public void updateFromTaskCompletion(String paramName, Element outputData,
            Element caseData);
}
