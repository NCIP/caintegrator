/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.transfer;

import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 * Represents a node in our permissions tree. Simply consists of the id of the object and whether or
 * not that node has been selected in the UI.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class TreeNode implements Comparable<TreeNode> {
    private final Long nodeId;
    private final String label;
    private final boolean selected;
    private final Set<TreeNode> children = new TreeSet<TreeNode>();

    /**
     * Class constructor.
     * @param nodeId the id of the node
     * @param label the label of this node
     * @param selected whether the node is selected
     */
    public TreeNode(Long nodeId, String label, boolean selected) {
        this.nodeId = nodeId;
        this.label = label;
        this.selected = selected;
    }

    /**
     * @return the nodeId
     */
    public Long getNodeId() {
        return nodeId;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @return the selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * @return the children
     */
    public Set<TreeNode> getChildren() {
        return children;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(TreeNode o) {
        return new CompareToBuilder().append(this.label, o.getLabel()).toComparison();
    }
}
