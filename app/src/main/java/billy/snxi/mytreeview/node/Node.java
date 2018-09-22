package billy.snxi.mytreeview.node;

import java.util.ArrayList;
import java.util.List;

/**
 * listview的节点类，任何数据对象都需要转化为本节点类
 */
public class Node {

    private int id;
    private int pid;
    private String name;
    private String desc;
    /**
     * 节点图标
     */
    private int icon = -1;
    /**
     * 父节点
     */
    private Node parentNode;
    /**
     * 子节点集合
     */
    private List<Node> childNodes = new ArrayList<>();
    /**
     * 节点层级
     */
    private int level = 0;
    /**
     * 节点是否展开
     */
    private boolean isExpend = false;

    public Node(int id, int pid, String name, String desc) {
        this.id = id;
        this.pid = pid;
        this.name = name;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }

    public List<Node> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(List<Node> childNodes) {
        this.childNodes = childNodes;
    }

    /**
     * 返回节点层级
     */
    public int getLevel() {
        if (parentNode == null) {
            return 0;
        }
        return parentNode.getLevel() + 1;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isExpend() {
        return isExpend;
    }

    /**
     * 设置节点展开状态
     */
    public void setExpend(boolean expend) {
        isExpend = expend;
        //当关闭节点时，则还需要关闭其下的所有子节点
        if (!isExpend) {
            for (Node node : childNodes) {
                node.setExpend(false);
            }
        }
    }

    /**
     * 是否为根节点
     */
    public boolean isRootNode() {
        return parentNode == null;
    }

    /**
     * 判断父节点是否为展开状态
     */
    public boolean isParentExpand() {
        if (parentNode == null) {
            return false;
        }
        return parentNode.isExpend;
    }

    /**
     * 判断该节点是否为叶子节点
     */
    public boolean isLeafNode() {
        return childNodes.size() == 0;
    }
}
