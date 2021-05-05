package main.java.com.edu.pojo;

import lombok.Data;

import java.util.Arrays;
import java.util.List;
import javax.persistence.*;

@Table(name = "sys_permission")
@Entity
@Data
public class SysPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    //名称
    private String name;
    //资源类型，[menu|button]
    @Column(columnDefinition =
            "enum('menu','button')")
    private String resourceType;
    private String url;//资源路径.
    //权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view
    private String permission;
    private Long parentId; //父编号
    private String parentIds; //父编号列表
    private Boolean available = Boolean.FALSE;
    @Transient
    private List permissions;

    public List getPermissions() {
        return
                Arrays.asList(this.permission.trim().split("|"));
    }

    public void setPermissions(List permissions) {
        this.permissions = permissions;
    }
}