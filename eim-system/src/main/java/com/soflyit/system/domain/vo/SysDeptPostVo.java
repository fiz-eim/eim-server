package com.soflyit.system.domain.vo;

import com.soflyit.system.api.domain.SysDeptPost;
import lombok.Data;

/**
 * @author lc
 * @Description
 * @create 2022-06-08 10:45
 */
@Data
public class SysDeptPostVo extends SysDeptPost {


    private String deptName;

    private String postName;
}
