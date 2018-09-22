package billy.snxi.mytreeview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import billy.snxi.mytreeview.adapter.SimpleTreeListViewAdapter;
import billy.snxi.mytreeview.bean.FileBean;
import billy.snxi.mytreeview.node.Node;
import billy.snxi.mytreeview.utils.TreeListViewAdapter;

public class MainActivity extends Activity {
    private static final String TAG = "tree_billy";
    private Context mContext;
    private ListView lv_tree_node;
    private List<FileBean> mFileList;
    private SimpleTreeListViewAdapter<FileBean> mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();
        initDatas();
        initAdapter();
    }

    private void initView() {
        lv_tree_node = findViewById(R.id.lv_tree_node);
    }

    private void initDatas() {
        mFileList = new ArrayList<>();
        //添加根节点
        for (int i = 1; i <= 5; i++) {
            mFileList.add(new FileBean(i, 0, "根目录" + i, "root" + i));
        }
        //添加二级节点
        for (int i = 1; i <= 4; i++) {
            mFileList.add(new FileBean(i + 10, i, i + "-" + i, "二级节点" + i));
        }
        //添加三级节点
        for (int i = 1; i <= 3; i++) {
            mFileList.add(new FileBean(i + 20, i + 10, i + "-" + i + "-" + i, "三级节点" + i));
        }
        //添加四级节点
        for (int i = 1; i <= 2; i++) {
            mFileList.add(new FileBean(i + 30, i + 20, i + "-" + i + "-" + i + "-" + i, "四级节点" + i));
        }
        //添加五级节点
        for (int i = 1; i <= 1; i++) {
            mFileList.add(new FileBean(i + 40, i + 30, i + "-" + i + "-" + i + "-" + i + "-" + i, "五级节点" + i));
        }
    }

    private void initAdapter() {
        try {
            mAdapter = new SimpleTreeListViewAdapter<>(mContext, lv_tree_node, mFileList, 0);
            mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
                @Override
                public void onNodeClick(Node node, int position) {
                    //点击叶子节点时
                    if (node.isLeafNode()) {
                        Toast.makeText(mContext, "position:" + position + ", name:" + node.getName(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            lv_tree_node.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    showTreeListViewMenu(position, view);
                    return true;
                }
            });
            lv_tree_node.setAdapter(mAdapter);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * TreeListView弹出菜单
     */
    private void showTreeListViewMenu(final int position, View view) {
        PopupMenu popup = new PopupMenu(mContext, view);
        popup.getMenuInflater().inflate(R.menu.menu_tree_list_view, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_tree_list_view_node_same_level_add:
                        showTreeNodeAddDialog(position, mAdapter.ADD_NODE_TYPE_SAME_LEVEL);
                        break;
                    case R.id.menu_tree_list_view_node_sub_level_add:
                        showTreeNodeAddDialog(position, mAdapter.ADD_NODE_TYPE_SUB_LEVEL);
                        break;
                    case R.id.menu_tree_list_view_node_delete:
                        if (!mAdapter.deleteTreeNode(position)) {
                            Toast.makeText(mContext, "只能删除叶子节点！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "节点删除成功！", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    /**
     * 填出添加nodeview
     *
     * @param position
     */
    public void showTreeNodeAddDialog(final int position, final int addType) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        if (addType == mAdapter.ADD_NODE_TYPE_SAME_LEVEL) {
            dialog.setTitle("添加节点");
        } else {
            dialog.setTitle("添加子节点");
        }
        final View addNodeView = LayoutInflater.from(mContext).inflate(R.layout.layout_tree_node_add, null);
        dialog.setView(addNodeView);
        dialog.setPositiveButton("添加", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TextView tv_node_add_name = addNodeView.findViewById(R.id.tv_node_add_name);
                TextView tv_node_add_desc = addNodeView.findViewById(R.id.tv_node_add_desc);
                String name = tv_node_add_name.getText().toString();
                String desc = tv_node_add_desc.getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(desc)) {
                    Toast.makeText(mContext, "节点名称和摘要不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAdapter.addTreeNode(position, name, desc, addType);
                Toast.makeText(mContext, "节点添加成功！", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setNegativeButton("取消", null);
        dialog.show();
    }

}
