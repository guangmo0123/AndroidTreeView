package billy.snxi.mytreeview.bean;

import billy.snxi.mytreeview.annotation.TreeNodeDesc;
import billy.snxi.mytreeview.annotation.TreeNodeId;
import billy.snxi.mytreeview.annotation.TreeNodeName;
import billy.snxi.mytreeview.annotation.TreeNodePid;

/**
 * 利用注解来标记bean字段
 * 使之在解析bean对象时，可以通过注解来获取对应的字段值，而不用知道具体的字段名
 */
public class FileBean {
    @TreeNodeId
    private int id;

    @TreeNodePid
    private int pid;

    @TreeNodeName
    private String name;

    @TreeNodeDesc
    private String desc;

    public FileBean(int id, int pid, String name, String desc) {
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
}
