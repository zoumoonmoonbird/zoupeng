package main.java.com.edu.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Table(name = "sys_role")
@Entity
@Data
public class SysRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Boolean available = Boolean.TRUE;
    private String description;
    private String role;
    //角色   多对多 权限
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "sys_role_permission", joinColumns = {@JoinColumn(name = "role_id")}, inverseJoinColumns = {@JoinColumn(name = "permission_id")})
    private List<SysPermission> permissions;
    public String getRole() {
        return role;
    }
}