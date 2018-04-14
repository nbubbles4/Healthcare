package org.yawlfoundation.yawl.elements.data.external;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jdom2.Element;
import org.yawlfoundation.yawl.elements.YTask;
import org.yawlfoundation.yawl.elements.data.YParameter;
import org.yawlfoundation.yawl.elements.data.YVariable;
import org.yawlfoundation.yawl.engine.YSpecificationID;
import org.yawlfoundation.yawl.util.JDOMUtil;
import org.yawlfoundation.yawl.util.StringUtil;

/**
 * NJ: Tutorial: http://yaug.org/node/45 When assigned to a variable, the
 * populateTaskParameter will be called at the start of a task if the database
 * gateway is selected for the input mapping of a task variable. The
 * updateFromTaskCompletion will be called on completion of a task if the
 * database gateway is assigned to the output mapping of a task variable.
 *
 * The populateCaseData and updateFromCaseDat methods are the same as the first
 * and second, but are intended for net variables.
 *
 * @author Dr. Noha and Dr. James
 */
public class MyExternalDBGatewayImpl extends AbstractExternalDBGateway
{

    private boolean configured = false;

    private boolean configure()
    {
        if (!configured)
        {
            configureSession("org.hibernate.dialect.H2Dialect",
                    "org.h2.Driver", "jdbc:h2:tcp://localhost/~/apache-tomcat-8.5.5/database/yawldb",
                    "", "", null);
            configured = true;
        }
        System.out.println("configured");
        return configured;
    }

    public String getDescription()
    {
        return "A simple example of an external database gateway using postgres-jdbc";
    }

    /**
     * This method is called from a a task where "input" has been specified in
     * the YAWL model. The input can be combined with other data (e.g. from a
     * database) and displayed. The input cannot be modified. To do this, need
     * to consider another method and update the model!
     *
     * @param task
     * @param param
     * @param caseData
     * @return
     */
    // Input to Task, from the Net (Net == Case, populate == Input, update == Output)
    @Override
    public Element populateTaskParameter(YTask task, YParameter param, Element caseData)
    {
        String taskName = task.getName();
        
        String paramName = param.getName();
        //Element result = new Element(paramName);
//        Element result = null;
//        if( paramName.equalsIgnoreCase("name") )
//        {
//            result  = (Element) JDOMUtil.stringToElement(StringUtil.wrap("George", "name")).clone();
//        }
//        else if( paramName.equalsIgnoreCase("age") )
//        {
//            result = (Element) JDOMUtil.stringToElement(StringUtil.wrap("56", "age")).clone();
//        }

        System.out.println("NOHA: Method  populateTaskParameter: " + taskName);
        
        Element result = null;
        if (configure())
        {
            List resultSet = _dbEngine.execSQLQuery(
                    "select name, age from customers;"
            );
            
            Object[] row0 = (Object [])resultSet.get(0);
            Element name
                = (Element) JDOMUtil.stringToElement(StringUtil.wrap(row0[0].toString(), "name")).clone();
            Element age
                = (Element) JDOMUtil.stringToElement(StringUtil.wrap(row0[1].toString(), "age")).clone();
            
            if( paramName.equalsIgnoreCase("name") )
            {
                result  = name;
            }
            else if( paramName.equalsIgnoreCase("age") )
            {
                result = age;
            }
        }


        
//        System.out.println("NOHA: Method  populateTaskParameter: " + taskName);
//        if (configure())
//        {
//            List resultSet = _dbEngine.execSQLQuery(
//                    "select name, age from customers;"
//            );
//            
//            //System.out.println("MyExternalDBGatewayImpl: selected");
//            for (Object row : resultSet) {
//                Object [] rowa = (Object []) row;
//                Element customer = new Element("Customer");
//                Element name
//                        = (Element) JDOMUtil.stringToElement(StringUtil.wrap(rowa[0].toString(), "Name")).clone();
//                Element age
//                        = (Element) JDOMUtil.stringToElement(StringUtil.wrap(rowa[1].toString(), "Age")).clone();
//                customer.addContent(name);
//                customer.addContent(age);
//                result.addContent(customer);
//            }
//        }

        return result;
    }

    //private Map<String, Action> paramActionMappings = new TreeMap<String, Action>();
    // Add paramName -> action mappings
    // 1..20
    // Output from Task, paramaters are sent to Net
    @Override
    public void updateFromTaskCompletion(String paramName, Element outputData, Element caseData)
    {
        System.out.println("NOHA: Method  updateFromTaskCompletion: " + paramName);
        // TODO Auto-generated method stub
        if (paramName.equals("bloodtype"))
        {
            // Save to database.table1
            // Action
        } else if (paramName.equals("age"))
        {
            // Save to database.table2
        }

        //Action paramAction = paramActionMappings.get(paramName);
        //paramAction.updateFromTaskCompletion(paramName, outputData, caseData);
    }

    // Output from Net, to Task input
    @Override
    public Element populateCaseData(YSpecificationID specID, String caseID,
            List<YParameter> inputParams, List<YVariable> localVars,
            Element caseDataTemplate)
    {
        System.out.println("NOHA: Method  populateCaseData: " + caseID);
        // TODO Auto-generated method stub
        return null;
    }

    // Input to Net, output from a Task
    @Override
    public void updateFromCaseData(YSpecificationID specID, String caseID,
            List<YParameter> outputParams, Element updatingData)
    {
        System.out.println("NOHA: Method  updateFromCaseData: " + caseID);
        // TODO Auto-generated method stub
    }

}
