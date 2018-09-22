package billy.snxi.mytreeview.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import billy.snxi.mytreeview.R;
import billy.snxi.mytreeview.node.Node;
import billy.snxi.mytreeview.utils.TreeHelper;
import billy.snxi.mytreeview.utils.TreeListViewAdapter;

public class SimpleTreeListViewAdapter<T> extends TreeListViewAdapter<T> {

    public final int ADD_NODE_TYPE_SAME_LEVEL = 0;
    public final int ADD_NODE_TYPE_SUB_LEVEL = 1;

    public SimpleTreeListViewAdapter(Context context, ListView listView, List<T> datas, int defaultNodeLevel)
            throws IllegalAccessException {
        super(context, listView, datas, defaultNodeLevel);
    }

    @Override
    public View getConvertView(Node node, int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_tree_node, parent, false);
            holder = new ViewHolder();
            holder.iv_icon = convertView.findViewById(R.id.iv_icon);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_desc = convertView.findViewById(R.id.tv_desc);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //图标
        if (node.getIcon() == -1) {
            holder.iv_icon.setVisibility(View.INVISIBLE);
        } else {
            holder.iv_icon.setVisibility(View.VISIBLE);
            holder.iv_icon.setImageResource(node.getIcon());
        }
        holder.tv_name.setText(node.getName());
        holder.tv_desc.setText(node.getDesc());

        return convertView;
    }

    /**
     * 像Treelistview添加节点
     *
     * @param position
     * @param neme
     * @param desc
     * @param addType  添加节点的类型，是同级添加，还是添加子节点
     */
    public void addTreeNode(int position, String neme, String desc, int addType) {
        Node node = mVisibleNodeList.get(position);
        int index = mNodeList.indexOf(node);
        Node addNode = null;
        if (addType == ADD_NODE_TYPE_SAME_LEVEL) {  //添加同级节点
            addNode = new Node(TreeHelper.getMaxNodeId(mNodeList) + 1, node.getPid(), neme, desc);
            addNode.setParentNode(node.getParentNode());
            //若不是根节点，则将添加的节点添加至父节点的子节点列表中
            if (!node.isRootNode()) {
                node.getParentNode().getChildNodes().add(addNode);
            }
        } else {    //添加子节点
            addNode = new Node(TreeHelper.getMaxNodeId(mNodeList) + 1, node.getId(), neme, desc);
            addNode.setParentNode(node);
            node.getChildNodes().add(addNode);
        }
        mNodeList.add(index + 1, addNode);
        mVisibleNodeList = TreeHelper.getVisibleNodes(mNodeList);
        notifyDataSetChanged();
    }

    /**
     * 删除某个节点，只能删除子节点
     *
     * @param position
     */
    public boolean deleteTreeNode(int position) {
        Node node = mVisibleNodeList.get(position);
        //只能删除叶子节点
        if (!node.isLeafNode()) {
            return false;
        }
        //根节点为叶节点时，无需从父节点中子节点的列表中移除
        if (!node.isRootNode()) {
            node.getParentNode().getChildNodes().remove(node);
        }
        mNodeList.remove(node);
        mVisibleNodeList = TreeHelper.getVisibleNodes(mNodeList);
        notifyDataSetChanged();
        return true;
    }

    private class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_desc;
    }
}
