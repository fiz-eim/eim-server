package com.soflyit.system.domain.vo;

import com.soflyit.system.api.domain.SysUser;
import com.soflyit.system.domain.SysUserPost;
import lombok.Data;

import java.util.List;

/**
 * 适用于用户多部门模型
 *
 * @author lc
 * @Description
 * @create 2022-06-08 15:40
 */
@Data
public class SysUserVo extends SysUser {
    private Long[] deptIds;
    private List<SysUserPost> posts;

}
