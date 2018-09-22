package billy.snxi.mytreeview.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import billy.snxi.mytreeview.R;
import billy.snxi.mytreeview.annotation.TreeNodeDesc;
import billy.snxi.mytreeview.annotation.TreeNodeId;
import billy.snxi.mytreeview.annotation.TreeNodeName;
import billy.snxi.mytreeview.annotation.TreeNodePid;
import billy.snxi.mytreeview.node.Node;

public class TreeHelper {
    /**
     * 将指定的bean List对象转化为标准的tree node list对象并按关系进行了排序，最终返回排序后的nodelist
     *
     * @param datas              标准的bean list数据结合
     * @param defaultExpendLevel 节点默认展开的层级
     * @param <T>
     * @return
     * @throws IllegalAccessException
     */
    public static <T> List<Node> getNodes(List<T> datas, int defaultExpendLevel) throws IllegalAccessException {
        //返回的处理好的nodelist
        List<Node> resultList = new ArrayList<>();
        //先转化用户数据list为nodelist
        List<Node> convertNodes = convertBeans2Nodes(datas);
        //设置节点之间的关系及图标
        setNodeRelation(convertNodes);
        //获取所有根节点的集合
        List<Node> rootNodes = getRootNodes(convertNodes);
        //循环所有根节点
        for (Node node : rootNodes) {
            //将所有根节点的子节点添加至sortlist中
            addNodeToSortNodes(resultList, node, defaultExpendLevel, 1);
        }
        return resultList;
    }

    /**
     * 过滤出treelist中可见的node节点
     *
     * @param nodes
     * @return
     */
    public static List<Node> getVisibleNodes(List<Node> nodes) {
        List<Node> resultList = new ArrayList<>();
        for (Node node : nodes) {
            if (node.isRootNode() || node.isParentExpand()) {
                //更新节点图标
                setNodeIcon(node);
                resultList.add(node);
            }
        }
        return resultList;
    }

    /**
     * 将指定的bean List对象转化为标准的tree node list对象并按关系进行了排序
     *
     * @param datas 标准的bean list数据结合
     * @param <T>
     * @return
     * @throws IllegalAccessException
     */
    private static <T> List<Node> convertBeans2Nodes(List<T> datas) throws IllegalAccessException {
        List<Node> nodes = new ArrayList<>();
        Node node = null;
        Class classBean = null;
        Field[] fields = null;
        int id, pid;
        String name, desc;
        for (T t : datas) {
            //重置变量值
            id = -1;
            pid = -1;
            name = null;
            desc = null;
            //获取t的class类型
            classBean = t.getClass();
            fields = classBean.getDeclaredFields();
            for (Field field : fields) {
                //判断是否有TreeNodeId
                if (field.getAnnotation(TreeNodeId.class) != null) {
                    field.setAccessible(true);
                    id = field.getInt(t);
                }
                if (field.getAnnotation(TreeNodePid.class) != null) {
                    field.setAccessible(true);
                    pid = field.getInt(t);
                }
                if (field.getAnnotation(TreeNodeName.class) != null) {
                    field.setAccessible(true);
                    name = (String) field.get(t);
                }
                if (field.getAnnotation(TreeNodeDesc.class) != null) {
                    field.setAccessible(true);
                    desc = (String) field.get(t);
                }
            }
            if (id == -1 || name == null) {
                continue;
            }
            node = new Node(id, pid, name, desc);
            nodes.add(node);
        }
        return nodes;
    }


    /**
     * 设置节点之间的关系，谁是谁的父节点，谁是谁的子节点
     * 节点的图标
     */
    private static void setNodeRelation(List<Node> nodes) {
        int size = nodes.size();
        Node m = null;
        Node n = null;
        for (int i = 0; i < size; i++) {
            m = nodes.get(i);
            //设置节点的图标
            setNodeIcon(m);
            for (int j = i + 1; j < size; j++) {
                n = nodes.get(j);
                if (m.getId() == n.getPid()) {  //m为n的父节点
                    m.getChildNodes().add(n);
                    n.setParentNode(m);
                } else if (m.getPid() == n.getId()) {  //m为n的子节点
                    n.getChildNodes().add(m);
                    m.setParentNode(n);
                }
            }
        }
    }

    /**
     * 设置Node节点的图标
     *
     * @param n
     */
    private static void setNodeIcon(Node n) {
        //叶节点
        if (n.isLeafNode()) {
            n.setIcon(-1);
        } else {
            if (n.isExpend()) { //节点展开式
                n.setIcon(R.mipmap.tree_ex);
            } else {
                n.setIcon(R.mipmap.tree_ec);
            }
        }
    }

    /**
     * 获取所有根节点的list集合
     *
     * @param nodes
     * @return
     */
    private static List<Node> getRootNodes(List<Node> nodes) {
        List<Node> rootNodes = new ArrayList<>();
        for (Node node : nodes) {
            if (node.isRootNode()) {
                rootNodes.add(node);
            }
        }
        return rootNodes;
    }

    /**
     * 递归循环，将父节点中的所有子节点添加至sortlist中
     *
     * @param sortList           排序后的resultList
     * @param node               根节点
     * @param defaultExpendLevel 该根节点默认展开的层级
     * @param currentLevel       当前层级
     */
    private static void addNodeToSortNodes(List<Node> sortList, Node node, int defaultExpendLevel, int currentLevel) {
        //将根节点添加到ResultList
        sortList.add(node);
        //默认展开的层级
        if (defaultExpendLevel >= currentLevel) {
            node.setExpend(true);
        }
        //叶节点
        if (node.isLeafNode()) {
            return;
        }
        for (Node node1 : node.getChildNodes()) {
            addNodeToSortNodes(sortList, node1, defaultExpendLevel, currentLevel + 1);
        }
    }

    /**
     * 获取nodelist中现有节点id的最大值
     *
     * @param nodes
     * @return
     */
    public static int getMaxNodeId(List<Node> nodes) {
        if (nodes == null || nodes.size() == 0) {
            return 0;
        }
        int result = nodes.get(0).getId();
        int id;
        for (int i = 1; i < nodes.size(); i++) {
            id = nodes.get(i).getId();
            if (id > result) {
                result = id;
            }
        }
        return result;
    }

}
