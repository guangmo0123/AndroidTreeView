package billy.snxi.mytreeview.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.List;

import billy.snxi.mytreeview.node.Node;

/**
 * TreeListView的抽象adapter，用户只需要实现getConvertView来定义item的UI，其它具体实现有本类来处理
 *
 * @param <T>
 */
public abstract class TreeListViewAdapter<T> extends BaseAdapter
        implements AdapterView.OnItemClickListener {
    protected Context mContext;
    protected List<Node> mNodeList;
    protected List<Node> mVisibleNodeList;
    protected ListView mListView;
    protected LayoutInflater mInflater;
    private OnTreeNodeClickListener mOnTreeNodeClickListener;

    public TreeListViewAdapter(Context context, ListView listView, List<T> datas, int defaultNodeLevel)
            throws IllegalAccessException {
        this.mContext = context;
        this.mListView = listView;
        mInflater = LayoutInflater.from(context);
        mNodeList = TreeHelper.getNodes(datas, defaultNodeLevel);
        mVisibleNodeList = TreeHelper.getVisibleNodes(mNodeList);
        mListView.setOnItemClickListener(this);
    }

    public void setOnTreeNodeClickListener(OnTreeNodeClickListener onTreeNodeClickListener) {
        this.mOnTreeNodeClickListener = onTreeNodeClickListener;
    }

    @Override
    public int getCount() {
        return mVisibleNodeList.size();
    }

    @Override
    public Object getItem(int position) {
        return mVisibleNodeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Node node = mVisibleNodeList.get(position);
        convertView = getConvertView(node, position, convertView, parent);
        //设置内边距
        convertView.setPadding(node.getLevel() * 20, 3, 3, 3);
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        expendOrCollapseNode(position);
        if (mOnTreeNodeClickListener != null) {
            mOnTreeNodeClickListener.onNodeClick(mVisibleNodeList.get(position), position);
        }
    }

    /**
     * node的点击处理过程
     *
     * @param position
     */
    private void expendOrCollapseNode(int position) {
        Node node = mVisibleNodeList.get(position);
        if (node == null) {
            return;
        }
        if (node.isLeafNode()) {
            return;
        }
        //设置node的展开状态
        node.setExpend(!node.isExpend());
        mVisibleNodeList = TreeHelper.getVisibleNodes(mNodeList);
        notifyDataSetChanged();
    }

    /**
     * TreeListView的Node对外点击接口
     */
    public interface OnTreeNodeClickListener {
        void onNodeClick(Node node, int position);
    }

    /**
     * 对外用户来负责listView的VIew UI，而内部有本类来实现
     *
     * @param node
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public abstract View getConvertView(Node node, int position, View convertView, ViewGroup parent);

}
