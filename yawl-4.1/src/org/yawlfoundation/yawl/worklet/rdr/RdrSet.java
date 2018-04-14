/*
 * Copyright (c) 2004-2012 The YAWL Foundation. All rights reserved.
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

package org.yawlfoundation.yawl.worklet.rdr;

import org.yawlfoundation.yawl.engine.YSpecificationID;
import org.yawlfoundation.yawl.util.XNode;
import org.yawlfoundation.yawl.worklet.support.Persister;
import org.yawlfoundation.yawl.worklet.rdrutil.RdrSetParser;

import java.util.*;

/**
 *  Maintains a set of RdrTrees for a particular specification, for each of the following
 *  purposes (potentially):
 *   - selection: set of trees (RdrTreeSet), one for each worklet enabled task
 *   - exception:
 *      - spec pre-constraints: one RdrTree
 *      - spec post-constraints: one RdrTree
 *      - task pre-constraints: set of trees, one for each task in the specification
 *      - task post-constraints: set of trees, one for each task in the specification
 *      - task aborts: set of trees, one for each task in the specification
 *      - time-outs: set of trees, one for each task in the specification
 *      - unavailable resources: set of trees, one for each task in the specification
 *      - task constraint violations (i.e. while the task is executing): set of trees,
 *           one for each task in the specification
 *      - spec-level external triggers: one RdrTree
 *      - task-level external triggers: set of trees, one for each task in the
 *           specification
 *
 *  @author Michael Adams
 *  v0.8, 04-09/2006
 */

public class RdrSet {

    private long id;                       // for Hibernate

    private YSpecificationID _specID ;
    private String _processName;
    private Map<RuleType, RdrTreeSet> _treeMap;

    public static final String CASE_LEVEL_TREE_FLAG = "__case_level_tree__";


    /**
     * Default constructor
     * @param specID the specification this rule set belongs to
     */
    public RdrSet(YSpecificationID specID) {
        _specID = specID ;
    }


    public RdrSet(String processName) {
        _processName = processName ;
    }


    private RdrSet() { }                // for hibernate


  //===========================================================================//

    /** clears and rebuilds the RdrSet */
    public void refresh() {
        RdrSetLoader loader = new RdrSetLoader();
        RdrSet reloaded = _specID != null ? loader.load(_specID) : loader.load(_processName);
        if (reloaded != null) {
            setTreeMap(new HashMap<RuleType, RdrTreeSet>(reloaded._treeMap));
        }
    }


    /** @return true if this spec's ruleset is not empty */
    public boolean hasRules() {
        return ! (_treeMap == null || _treeMap.isEmpty());
    }


    public void addTree(RdrTree tree, RuleType treeType) {
        if (_treeMap == null) {
            _treeMap = new HashMap<RuleType, RdrTreeSet>();
        }
        RdrTreeSet treeSet = _treeMap.get(treeType);
        if (treeSet == null) {
            treeSet = new RdrTreeSet(treeType);
            _treeMap.put(treeType, treeSet);
        }
        treeSet.add(tree);
        Persister.update(this);
    }


    /**
     *  Retrieves a specified RdrTree for the specified task
     *
     *  @param treeType - the tree exception type
     *  @param taskId - the task the tree represents
     *  @return the RDRTree for the specified spec and task
     */
    public RdrTree getTree(RuleType treeType, String taskId) {
        if (treeType.isCaseLevelType()) taskId = CASE_LEVEL_TREE_FLAG;
        RdrTreeSet treeSet = getTreeSet(treeType);
        return treeSet != null ? treeSet.get(taskId) : null;
    }

    public RdrTree getTree(RuleType treeType) { return getTree(treeType, null); }


    // get the RuleTypes that have trees in this set
    public Set<RuleType> getRules() {
        return _treeMap != null ? _treeMap.keySet() : Collections.<RuleType>emptySet();
    }


    // get the tree sets for this rdrSet
    public RdrTreeSet getTreeSet(RuleType ruleType) {
        return _treeMap != null ? _treeMap.get(ruleType) : null;
    }


    public RdrTreeSet removeTreeSet(RuleType ruleType) {
        return _treeMap != null ? _treeMap.remove(ruleType) : null;
    }


    public void setTreeMap(Map<RuleType, RdrTreeSet> map) { _treeMap = map; }


    public boolean save() {
        Persister.update(this);
        return true;
    }
    
    
    public String toXML() {
         return toXNode().toPrettyString();
    }
    
    
    private XNode toXNode() {
        XNode ruleSet = createRootXNode();
        treeToXNode(getTree(RuleType.CasePreconstraint), ruleSet, "constraints",
                "case", "pre");
        treeToXNode(getTree(RuleType.CasePostconstraint, null), ruleSet, "constraints",
                "case", "post");
        treeToXNode(getTree(RuleType.CaseExternalTrigger), ruleSet,  "external", "case");

        treeListToXNode(_treeMap.get(RuleType.ItemSelection), ruleSet, "selection");
        treeListToXNode(_treeMap.get(RuleType.ItemPreconstraint), ruleSet,
                "constraints", "item", "pre");
        treeListToXNode(_treeMap.get(RuleType.ItemPostconstraint), ruleSet,
                "constraints", "item", "post");
        treeListToXNode(_treeMap.get(RuleType.ItemAbort), ruleSet, "abort");
        treeListToXNode(_treeMap.get(RuleType.ItemTimeout), ruleSet, "timeout");
        treeListToXNode(_treeMap.get(RuleType.ItemResourceUnavailable), ruleSet,
                "resourceUnavailable");
        treeListToXNode(_treeMap.get(RuleType.ItemConstraintViolation), ruleSet, "violation");
        treeListToXNode(_treeMap.get(RuleType.ItemExternalTrigger), ruleSet, "external", "item");
        return ruleSet;
    }
    
    private XNode createRootXNode() {
        XNode xRoot = null;
        if (_specID != null) {
            xRoot = new XNode("spec");
            xRoot.addAttribute("uri", _specID.getUri());
            if (_specID.getIdentifier() != null) {
                xRoot.addAttribute("version", _specID.getVersionAsString());
                xRoot.addAttribute("identifier", _specID.getIdentifier());
            }
        }
        else if (_processName != null) {
            xRoot = new XNode("process");
            String name = getName();
            if (name != null) xRoot.addAttribute("name", name);
        }
        return xRoot != null ? xRoot : new XNode("root");
    }
    
    
    protected String getName() {
        return _specID != null ? _specID.getUri() : _processName;
    }


    protected String getProcessName() { return _processName; }

    protected YSpecificationID getSpecificationID() { return _specID; }


    private void treeListToXNode(RdrTreeSet treeSet, XNode parent, String... names) {
        if (treeSet != null) {
            for (RdrTree tree : treeSet.getAll()) {
                treeToXNode(tree, parent, names);
            }
        }
    }
    
    private void treeToXNode(RdrTree tree, XNode parent, String... names) {
        if (tree != null) {
            XNode xNode = getOrCreateNode(parent, names);
            XNode childTree = tree.toXNode();
            if (childTree.getName().equals("task")) {
                xNode.addChild(childTree);                    // task level tree
            }
            else {
                xNode.addChildren(childTree.getChildren());   // case level tree
            }
        }
    }


    private XNode getOrCreateNode(XNode parent, String... names) {
        XNode node = parent;
        for (String name : names) {
            node = node.getOrAddChild(name);
        }
        return node;
    }


    public String toString() {
        String id = _specID != null ? _specID.toString() : _processName;
        return "RuleSet for: " + id;
    }


    // XML is provided to this method:
    //   1. on the service side from the API when adding a new set (INCOMING), in which
    //      case the node ids are temporary and should be ignored by the parser; OR
    //   2. on the client side when reconstituting a set sent from the service (OUTGOING),
    //      in which case the node ids are permanent & valid and should be used by the
    //      parser.
    public void fromXML(String xml, boolean newSet) {
        _treeMap = new RdrSetParser().parse(xml, newSet);
    }

//===========================================================================//

    // persistence

    private void setTreeSet(Set<RdrTreeSet> treeSet) {
        _treeMap = new HashMap<RuleType, RdrTreeSet>();
        for (RdrTreeSet tree : treeSet) {
            _treeMap.put(tree.getRuleType(), tree);
        }
    }

    protected Set<RdrTreeSet> getTreeSet() {
        return _treeMap != null ? new HashSet<RdrTreeSet>(_treeMap.values()) : null;
    }

}
