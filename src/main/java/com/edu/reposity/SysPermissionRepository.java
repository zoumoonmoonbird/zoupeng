package main.java.com.edu.reposity;

import com.edu.pojo.SysPermission;
import com.edu.pojo.SysRole;
import com.edu.pojo.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysPermissionRepository extends JpaRepository<SysPermission, Integer> {
}
