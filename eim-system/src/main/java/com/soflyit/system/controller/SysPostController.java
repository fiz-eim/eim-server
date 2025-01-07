package com.soflyit.system.controller;

import com.github.pagehelper.PageInfo;
import com.soflyit.common.core.constant.UserConstants;
import com.soflyit.common.core.domain.R;
import com.soflyit.common.core.utils.poi.ExcelUtil;
import com.soflyit.common.core.web.controller.BaseController;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.core.web.page.TableDataInfo;
import com.soflyit.common.security.annotation.RequiresPermissions;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.domain.SysPost;
import com.soflyit.system.api.domain.SysUser;
import com.soflyit.system.api.param.GetRolePostsQuery;
import com.soflyit.system.domain.SysUserPost;
import com.soflyit.system.service.ISysPostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 岗位信息操作处理
 *
 * @author soflyit
 */
@RestController
@RequestMapping("/post")
@Api(tags = {"岗位管理"})
public class SysPostController extends BaseController {
    @Autowired
    private ISysPostService postService;


    @RequiresPermissions("system:post:list")
    @ApiOperation(value = "获取岗位列表")
    @GetMapping("/list")
    public TableDataInfo list(SysPost post) {
        startPage();
        List<SysPost> list = postService.selectPostList(post);
        return getDataTable(list);
    }


    @RequiresPermissions("system:post:list")
    @ApiOperation(value = "获取岗位列表")
    @PostMapping("/listByPostName")
    public R<List<SysPost>> listByPostName(@RequestBody SysPost post) {
        List<SysPost> list = postService.listByPostName(post);
        return R.ok(list);
    }

    @RequiresPermissions("system:post:list")
    @ApiOperation(value = "获取岗位列表根据岗位Ids")
    @GetMapping("/getPostListByIds/{ids}")
    public AjaxResult list(@PathVariable Long[] ids) {
        List<SysPost> list = postService.selectPostListByIds(ids);
        return AjaxResult.success(list);
    }

    @RequiresPermissions("system:post:export")
    @ApiOperation(value = "导出岗位数据")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysPost post) {
        List<SysPost> list = postService.selectPostList(post);
        ExcelUtil<SysPost> util = new ExcelUtil<SysPost>(SysPost.class);
        util.exportExcel(response, list, "岗位数据");
    }


    @RequiresPermissions("system:post:query")
    @ApiOperation(value = "根据岗位编号获取详细信息")
    @GetMapping(value = "/{postId}")
    public AjaxResult getInfo(@PathVariable Long postId) {
        return AjaxResult.success(postService.selectPostById(postId));
    }


    @RequiresPermissions("system:post:add")
    @ApiOperation(value = "新增岗位")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysPost post) {
        if (UserConstants.NOT_UNIQUE.equals(postService.checkPostNameUnique(post))) {
            return AjaxResult.error("新增岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(postService.checkPostCodeUnique(post))) {
            return AjaxResult.error("新增岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        post.setCreateBy(SecurityUtils.getUserId());
        return toAjax(postService.insertPost(post));
    }


    @RequiresPermissions("system:post:edit")
    @ApiOperation(value = "修改岗位")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysPost post) {
        if (UserConstants.NOT_UNIQUE.equals(postService.checkPostNameUnique(post))) {
            return AjaxResult.error("修改岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(postService.checkPostCodeUnique(post))) {
            return AjaxResult.error("修改岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        post.setUpdateBy(SecurityUtils.getUserId());
        return toAjax(postService.updatePost(post));
    }


    @RequiresPermissions("system:post:remove")
    @ApiOperation(value = "批量删除岗位")
    @DeleteMapping("/{postIds}")
    public AjaxResult remove(@PathVariable Long[] postIds) {
        return toAjax(postService.deletePostByIds(postIds));
    }


    @GetMapping("/optionselect")
    @ApiOperation(value = "获取岗位选择框列表")
    public AjaxResult optionselect() {
        List<SysPost> posts = postService.selectPostAll();
        return AjaxResult.success(posts);
    }


    @RequiresPermissions("system:post:edit")
    @ApiOperation(value = "岗位状态修改")
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysPost post) {
        post.setUpdateBy(SecurityUtils.getUserId());
        return toAjax(postService.updatePostStatus(post));
    }


    @ApiOperation(value = "获取角色岗位")
    @GetMapping("/getRolePosts")
    public R<PageInfo> getRolePosts(GetRolePostsQuery getRolePostsQuery) {
        startPage();
        PageInfo list = postService.selectPostListByRoleId(getRolePostsQuery);
        return R.ok(list, "查询成功");
    }


    @ApiOperation(value = "批量选择用户授权")
    @RequiresPermissions("system:post:edit")
    @PostMapping("/authUser/selectAll")
    public AjaxResult selectAuthUserAll(@RequestBody List<SysUserPost> sysUserPosts) {
        return toAjax(postService.insertAuthUsers(sysUserPosts));
    }


    @ApiOperation(value = "查询未分配用户角色列表")
    @RequiresPermissions("system:post:list")
    @GetMapping("/authUser/selectUnSelectUser")
    public TableDataInfo selectUnSelectUser(SysUser sysUser) {
        startPage();
        List<SysUser> list = postService.selectUnSelectUser(sysUser);
        return getDataTable(list);
    }


    @ApiOperation(value = "取消授权用户")
    @RequiresPermissions("system:post:edit")
    @PutMapping("/authUser/cancelOne")
    public AjaxResult cancelAuthUserOne(@RequestBody SysUserPost sysUserPost) {
        return toAjax(postService.deleteAuthUsersOne(sysUserPost));
    }


    @ApiOperation(value = "批量取消授权用户")
    @RequiresPermissions("system:post:edit")
    @PostMapping("/authUser/cancelAll")
    public AjaxResult cancelAuthUserAll(@RequestBody List<SysUserPost> sysUserPosts) {
        return toAjax(postService.deleteAuthUsers(sysUserPosts));
    }


    @ApiOperation(value = "根据岗位ID和应用ID查询角色权限")
    @RequiresPermissions("system:post:list")
    @GetMapping("/selectByAppIdPostId")
    public AjaxResult selectByAppIdPostId(Long appId, Long postId) {
        return AjaxResult.success(postService.selectByAppIdPostId(appId, postId));
    }
}
