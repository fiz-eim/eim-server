package com.soflyit.system.controller;

import com.alibaba.fastjson.JSONArray;
import com.soflyit.common.core.constant.UserConstants;
import com.soflyit.common.core.domain.R;
import com.soflyit.common.core.utils.PageUtils;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.core.utils.poi.ExcelUtil;
import com.soflyit.common.core.web.controller.BaseController;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.core.web.page.TableDataInfo;
import com.soflyit.common.security.annotation.InnerAuth;
import com.soflyit.common.security.annotation.RequiresPermissions;
import com.soflyit.common.security.utils.RSAUtils;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.domain.*;
import com.soflyit.system.api.domain.vo.SysUserSimpleVO;
import com.soflyit.system.api.model.LoginUser;
import com.soflyit.system.config.SystemConfig;
import com.soflyit.system.domain.SysUserPost;
import com.soflyit.system.domain.vo.RouterVo;
import com.soflyit.system.domain.vo.SysUserInfoVo;
import com.soflyit.system.domain.vo.SysUserVo;
import com.soflyit.system.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户信息
 *
 * @author soflyit
 */
@RestController
@RequestMapping("/user")
@Api(tags = {"用户管理"})
public class SysUserController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysPostService postService;

    @Autowired
    private ISysPermissionService permissionService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private SystemConfig systemConfig;
    @Resource
    private ISysDeptUserService deptUserService;

    private ISysMenuService menuService;


    @RequiresPermissions("system:user:list")
    @ApiOperation(value = "获取用户列表")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    @ApiOperation(value = "获取用户列表根据用户数组")
    @GetMapping("/getUserListByIds/{ids}")
    public R<List<SysUser>> getUserListByIds(@PathVariable Long[] ids) {
        List<SysUser> list = userService.selectUserListByIds(ids);
        return R.ok(list);
    }

    @RequiresPermissions("system:user:export")
    @ApiOperation(value = "导出用户数据")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysUser user) {
        List<SysUser> list = userService.selectUserList(user);
        List<SysUserExport> exportList = list.stream().map(export -> {
            SysUserExport sysUserExport = new SysUserExport();
            List<String> nameList = new ArrayList<>();
            List<String> leaderList = new ArrayList<>();
            List<String> codeList = new ArrayList<>();
            for (SysDept dept : export.getDepts()) {
                nameList.add(dept.getDeptName());
                if (StringUtils.isNotEmpty(dept.getLeader())) {
                    leaderList.add(dept.getLeader());
                } else {
                    leaderList.add("暂无");
                }
                codeList.add(dept.getDeptCode());
            }
            BeanUtils.copyProperties(export, sysUserExport);
            String deptName = String.join("、", nameList);
            String leader = String.join("、", leaderList);
            String deptCode = String.join("、", codeList);
            sysUserExport.setDeptName(deptName);
            sysUserExport.setLeader(leader);
            sysUserExport.setDeptCode(deptCode);
            return sysUserExport;
        }).collect(Collectors.toList());
        ExcelUtil<SysUserExport> util = new ExcelUtil<SysUserExport>(SysUserExport.class);
        util.exportExcel(response, exportList, "用户数据");
    }

    @RequiresPermissions("system:user:import")
    @ApiOperation(value = "导入用户数据")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        try {
            InputStream inputStream = file.getInputStream();
            ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
            List<SysUser> userList = util.importExcel(inputStream);
            if (userList != null) {
                for (SysUser dept : userList) {
                    if (dept != null) {
                        SysDept sysDept = deptService.selectDeptByDeptCode(dept.getDeptCode());
                        if (sysDept != null) {
                            dept.setDeptId(sysDept.getDeptId());
                        } else {
                            return AjaxResult.error("导入失败！不存在部门！");
                        }
                    }
                }
            } else {
                throw new NullPointerException("导入失败! 数据为空");
            }

            Long operId = SecurityUtils.getUserId();
            String message = userService.importUser(userList, updateSupport, operId);
            return AjaxResult.success(message);
        } catch (NullPointerException e) {
            throw new NullPointerException("导入失败! 数据格式不正确");
        }
    }

    @ApiOperation(value = "获取导入用户模板")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.importTemplateExcel(response, "用户数据");
    }


    @InnerAuth
    @GetMapping("/info/{username}")
    @ApiOperation(value = "获取当前用户信息")
    public R<LoginUser> info(@PathVariable("username") String username, @RequestParam(name = "appId", required = false) Long appId, @RequestParam(name = "deptId", required = false) Long deptId) {
        return getUserInfo(appId, deptId, username);
    }

    private R<LoginUser> getUserInfo(Long appId, Long deptId, String username) {
        SysUser condition = new SysUser();
        if (appId != null) {
            condition.getParams().put("appId", appId);
        }
        if (deptId != null) {
            condition.setDeptId(deptId);
        }
        condition.setUserName(username);
        SysUser sysUser = userService.selectUserByUserName(condition);
        if (StringUtils.isNull(sysUser)) {
            condition.getParams().remove("appId");
            sysUser = userService.selectUserByUserName(condition);
            if (sysUser == null) {
                return R.fail("用户名或密码错误");
            } else if (!SysUser.isAdmin(sysUser.getUserId())) {
                return R.fail("用户没有系统访问权限，请联系管理员");
            }
        }

        Long defaultDeptId = deptId != null ? deptId : sysUser.getDeptId();
        SysDept dept = sysUser.getDepts().stream().filter(item -> item.getDeptId().equals(defaultDeptId)).findFirst().orElse(null);

        sysUser.setDept(dept);


        Set<String> roles = permissionService.getRolePermission(appId, sysUser.getUserId(), deptId);

        Set<String> permissions = permissionService.getMenuPermission(appId, sysUser.getUserId(), deptId);
        LoginUser sysUserVo = new LoginUser();
        sysUserVo.setSysUser(sysUser);
        sysUserVo.setRoles(roles);
        sysUserVo.setPermissions(permissions);
        return R.ok(sysUserVo);
    }


    @InnerAuth
    @ApiOperation(value = "获取当前用户信息(默认查询本系统用户)")
    @GetMapping("/info-default/{username}")
    public R<LoginUser> infoDefault(@PathVariable("username") String username) {
        Long appId = systemConfig.getAppId();
        return getUserInfo(appId, null, username);
    }


    @InnerAuth
    @ApiOperation(value = "获取当前用户信息(默认查询本系统用户)")
    @GetMapping("/info-phone/{phone}")
    public R<List<LoginUser>> getUserInfoByPhone(@PathVariable("phone") String phone, @RequestParam(name = "appId", required = false) Long appId) {
        SysUser condition = new SysUser();
        condition.getParams().put("appId", appId);
        condition.setPhonenumber(phone);
        List<SysUser> sysUsers = userService.getUsersByUserPhone(condition);
        if (CollectionUtils.isEmpty(sysUsers)) {
            condition.getParams().remove("appId");
            sysUsers = userService.getUsersByUserPhone(condition);
            if (CollectionUtils.isEmpty(sysUsers)) {
                return R.fail("手机号或密码错误");
            } else {
                return R.fail("用户没有系统访问权限，请联系管理员");
            }
        }

        List<LoginUser> userList = new ArrayList<>();
        sysUsers.forEach(sysUser -> {
            SysDept dept = sysUser.getDepts().stream().filter(item -> item.getDeptId().equals(sysUser.getDeptId())).findFirst().orElse(null);
            sysUser.setDept(dept);

            Set<String> roles = permissionService.getRolePermission(appId, sysUser.getUserId(), sysUser.getDeptId());

            Set<String> permissions = permissionService.getMenuPermission(appId, sysUser.getUserId(), sysUser.getDeptId());
            LoginUser sysUserVo = new LoginUser();
            sysUserVo.setSysUser(sysUser);
            sysUserVo.setRoles(roles);
            sysUserVo.setPermissions(permissions);
            userList.add(sysUserVo);
        });

        return R.ok(userList);
    }


    @InnerAuth
    @PostMapping("/register")
    @ApiOperation(value = "注册用户")
    public R<Boolean> register(@RequestBody SysUser sysUser) {
        String username = sysUser.getUserName();
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
            return R.fail("当前系统没有开启注册功能！");
        }
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(username))) {
            return R.fail("保存用户'" + username + "'失败，注册账号已存在");
        }
        return R.ok(userService.registerUser(sysUser));
    }


    @GetMapping("getInfo")
    @ApiOperation(value = "获取用户信息")
    public AjaxResult getInfo(@RequestParam(value = "appId", required = false) Long appId, @RequestParam(value = "activeDeptId", required = false) Long activeDeptId, HttpServletRequest request) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getSysUser();
        Long userId = user.getUserId();

        Set<String> roles = permissionService.getRolePermission(appId, user.getUserId(), user.getDeptId());

        Set<String> permissions = permissionService.getMenuPermission(appId, userId, user.getDeptId());
        SysUser sysUser = userService.selectUserById(appId, userId);
        if (sysUser.getDepts() != null) {
            SysDept dept = sysUser.getDepts().stream().filter(item -> item.getDeptId().equals(sysUser.getDeptId())).findFirst().orElse(null);
            sysUser.setDept(dept);
        }
        sysUser.setPassword(null);
        AjaxResult ajax = AjaxResult.success();
        ajax.putExtData("user", sysUser);
        ajax.putExtData("roles", roles);
        ajax.putExtData("permissions", permissions);
        ajax.putExtData("depts", user.getDepts());
        return ajax;
    }


    @GetMapping("getInfoV3")
    @ApiOperation(value = "获取用户信息")
    public AjaxResult<SysUserInfoVo> getInfoV3(@RequestParam(value = "appId", required = false) Long appId,
                                               @RequestParam(value = "activeDeptId", required = false) Long activeDeptId) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getSysUser();
        Long userId = user.getUserId();

        Set<String> roles = permissionService.getRolePermission(appId, user.getUserId(), user.getDeptId());

        Set<String> permissions = permissionService.getMenuPermission(appId, userId, user.getDeptId());
        SysUser sysUser = userService.selectUserById(appId, userId);
        if (sysUser.getDepts() != null) {
            SysDept dept = sysUser.getDepts().stream().filter(item -> item.getDeptId().equals(sysUser.getDeptId())).findFirst().orElse(null);
            sysUser.setDept(dept);
        }

        List<SysMenu> menus = menuService.selectMenuTreeByUser(appId, user);
        List<RouterVo> routerVoList = menuService.buildMenusV3(menus);

        sysUser.setPassword(null);
        SysUserInfoVo userInfoVo = new SysUserInfoVo();
        userInfoVo.setUser(sysUser);
        userInfoVo.setRoles(roles);
        userInfoVo.setPermissions(permissions);
        userInfoVo.setDepts(user.getDepts());
        userInfoVo.setRouters(routerVoList);

        return AjaxResult.success(userInfoVo);
    }


    @GetMapping("/getDefaultPassword")
    @ApiOperation(value = "获取默认密码")
    public AjaxResult getDefaultPassword() {
        try {
            return AjaxResult.success("获取密码成功", RSAUtils.encrypt(systemConfig.getDefaultPassword()));
        } catch (Exception e) {
            return AjaxResult.error("获取密码失败");
        }
    }


    @InnerAuth
    @GetMapping("/getUserInfo")
    @ApiOperation(value = "获取用户信息")
    public R<LoginUser> getUserInfo(@RequestParam(value = "appId", required = false) Long appId) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getSysUser();
        Long userId = user.getUserId();

        Set<String> roles = permissionService.getRolePermission(appId, user.getUserId(), user.getDeptId());

        Set<String> permissions = permissionService.getMenuPermission(appId, userId, user.getDeptId());
        LoginUser sysUserVo = new LoginUser();
        sysUserVo.setSysUser(userService.selectUserById(appId, userId));
        sysUserVo.setRoles(roles);
        sysUserVo.setPermissions(permissions);
        return R.ok(sysUserVo);
    }


    @ApiOperation(value = "根据用户编号获取详细信息")
    @RequiresPermissions("system:user:query")
    @GetMapping(value = {"/", "/{userId}"})
    public AjaxResult getInfo(@RequestParam(value = "appId", required = false) Long appId, @PathVariable(value = "userId", required = false) Long userId) {
        userService.checkUserDataScope(userId);
        AjaxResult ajax = AjaxResult.success();
        List<SysRole> roles = roleService.selectRoleAll(appId);
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        ajax.put("posts", postService.selectPostAll());
        if (StringUtils.isNotNull(userId)) {
            SysUser sysUser = userService.selectUserById(appId, userId);
            sysUser.setPassword(null);
            SysDept dept = null;
            if (CollectionUtils.isNotEmpty(sysUser.getDepts())) {
                dept = sysUser.getDepts().stream().filter(item -> item.getDeptId().equals(sysUser.getDeptId())).findFirst().orElse(null);
            }
            sysUser.setDept(dept);
            ajax.setData(sysUser);
            sysUser.setDeptIds(deptUserService.selectDeptUsersByUserId(userId).stream().map(SysDeptUser::getDeptId).toArray(Long[]::new));
            ajax.put("roleIds", sysUser.getRoles().stream().map(SysRole::getRoleId).collect(Collectors.toList()));
            List<SysUserPost> posts = userService.selectUserPostByUserId(userId);
            ajax.put("posts", posts);
        }
        return ajax;
    }


    @ApiOperation(value = "bpm根据用户id获取用户昵称")
    @RequiresPermissions("system:user:query")
    @GetMapping("/getNickName/{userId}")
    public AjaxResult getNickNameById(@PathVariable Long userId) {
        List<SysUser> list = userService.getNickNameById(userId);
        SysUser sysUser = CollectionUtils.isEmpty(list) ? null : list.get(0);
        return AjaxResult.success(sysUser);
    }


    @ApiOperation(value = "根据用户id获取用户昵称")
    @RequiresPermissions("system:user:query")
    @GetMapping("/getNickNames")
    public AjaxResult<List<SysUser>> getNickNames(@RequestParam(value = "userIds") List<Long> userIds) {
        List<SysUser> list = userService.getNickNames(userIds);
        return AjaxResult.success(list);
    }

    /*
     * @description: 齐成报表特有接口
     * 通过RAS（第三方数据库的Userid获取）用户信息
     * 用户无密码登录，industry嵌套到其他应用内部，实现自动登录
     * @author: Yang Daobing
     * @date: 2022-09-19 11:12
     * @param userId
     * @retrun com.soflyit.common.core.web.domain.AjaxResultAjaxResult
     */
    @ApiOperation(value = "根据用户ext1获取用户")
    @GetMapping("/getUserInfoByExt1/{userId}")
    public AjaxResult getUserInfoByExt1(@PathVariable Long userId) {
        List<SysUser> list = userService.getUserInfoByExt1(userId);
        SysUser sysUser = CollectionUtils.isEmpty(list) ? null : list.get(0);
        return AjaxResult.success(sysUser);
    }


    @ApiOperation(value = "根据用户名称获取详情信息")
    @GetMapping("user")
    public TableDataInfo user(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectUser(user);
        return getDataTable(list);
    }


    @ApiOperation(value = "新增用户")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysUserVo user) {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserName()))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber()) && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail()) && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setCreateBy(SecurityUtils.getUserId());
        try {
            user.setPassword(SecurityUtils.encryptPassword(RSAUtils.decrypt(user.getPassword())));
        } catch (Exception e) {
            return AjaxResult.error("密码格式不合法");
        }
        return toAjax(userService.insertUser(user));
    }



    @GetMapping("/checkUser")
    public AjaxResult checkUser(SysUserVo user) {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserName()))) {
            return AjaxResult.error("账号【" + user.getUserName() + "】已存在");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber()) && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return AjaxResult.error("手机号码【" + user.getPhonenumber() + "】已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail()) && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return AjaxResult.error("邮箱【" + user.getEmail() + "】已存在");
        }
        return AjaxResult.success();
    }


    @RequiresPermissions("system:user:edit")
    @ApiOperation(value = "修改用户")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysUserVo user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        if (StringUtils.isNotEmpty(user.getPhonenumber()) && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail()) && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUpdateBy(SecurityUtils.getUserId());
        return toAjax(userService.updateUser(user));
    }


    @RequiresPermissions("system:user:remove")
    @ApiOperation(value = "删除用户")
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds) {
        if (ArrayUtils.contains(userIds, SecurityUtils.getUserId())) {
            return AjaxResult.error("当前用户不能删除");
        }
        return toAjax(userService.deleteUserByIds(userIds));
    }


    @RequiresPermissions("system:user:edit")
    @ApiOperation(value = "重置密码")
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        try {
            user.setPassword(SecurityUtils.encryptPassword(RSAUtils.decrypt(user.getPassword())));
        } catch (Exception e) {
            return AjaxResult.error("密码格式不合法");
        }
        user.setUpdateBy(SecurityUtils.getUserId());

        SysUser sysUser = userService.getByUserId(user.getUserId());
        return toAjax(userService.resetPwd(user));
    }


    @ApiOperation(value = "修改用户状态")
    @RequiresPermissions("system:user:edit")
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setUpdateBy(SecurityUtils.getUserId());
        return toAjax(userService.updateUserStatus(user));
    }


    @ApiOperation(value = "根据用户编号获取授权角色")
    @RequiresPermissions("system:user:query")
    @GetMapping("/authRole/{userId}")
    public AjaxResult authRole(@PathVariable("userId") Long userId, @RequestParam(value = "appId", required = false) Long appId, @RequestParam(value = "deptId", required = false) Long deptId) {
        AjaxResult ajax = AjaxResult.success();
        SysUser user = userService.selectUserById(appId, userId);
        List<SysRole> roles = roleService.selectRolesByUserId(appId, userId, deptId);
        ajax.put("user", user);
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        return ajax;
    }


    @ApiOperation(value = "用户授权角色")
    @RequiresPermissions("system:user:edit")
    @PutMapping("/authRole")
    public AjaxResult insertAuthRole(Long userId, Long[] roleIds) {
        userService.checkUserDataScope(userId);
        userService.insertUserAuth(userId, roleIds);
        return success();
    }


    @ApiOperation(value = "获取岗位用户")
    @GetMapping("/getPostUsers")
    public TableDataInfo getPostUsers(SysUser sysUser) {
        startPage();
        List<SysUser> list = userService.selectUserListByPostId(sysUser);
        return getDataTable(list);
    }


    @ApiOperation(value = "获取用户根据岗位和部门 不分页")
    @PostMapping("/getPostAndDeptUsers")
    public R<List<SysUser>> getPostAndDeptUsers(@RequestBody SysUser sysUser) {
        List<SysUser> list = userService.selectUserListByPostId(sysUser);
        return R.ok(list);
    }


    @Deprecated
    public AjaxResult changeDefaultDept(Long deptId, HttpServletRequest request) {
        this.userService.changeDefaultDept(deptId, request);
        return AjaxResult.success();
    }

    @ApiOperation(value = "根据用户名数组查询用户信息")
    @PostMapping("/info/byUserNames")
    public R<List<SysUser>> getUserInfoByUserNames(@RequestBody List<String> userNames) {

        Assert.isTrue(!userNames.isEmpty(), "查询数量必须大于0！");

        List<SysUser> users = userService.getUserInfoByUserNames(userNames);
        return R.ok(users);
    }

    @InnerAuth
    @ApiOperation(value = "根据用户id数组查询用户信息")
    @PostMapping("/info/byIds")
    public R<List<SysUser>> getUserInfoByIds(@RequestBody List<Long> userIds) {

        Assert.isTrue(!userIds.isEmpty(), "查询数量必须大于0！");

        List<SysUser> users = userService.getUserInfoByIds(userIds);
        return R.ok(users);
    }

    @ApiOperation(value = "根据用户名数组获取岗位、部门信息")
    @PostMapping("/info/getPostInfoByUserNames")
    public R<JSONArray> getPostInfoByUserNames(@Validated @NotEmpty(message = "用户名数组不能为空！") @RequestBody List<String> userNames) {
        List<SysUserPost> userPosts = userService.selectUserPostByUserNames(userNames);
        JSONArray jsonArray = (JSONArray) JSONArray.toJSON(userPosts);
        return R.ok(jsonArray);
    }

    @ApiOperation(value = "根据电话号码查询用户信息")
    @PostMapping("/info/byPhonenumber")
    public R<SysUser> getUserInfoByPhonenumber(@RequestBody String phonenumber) {
        SysUser user = userService.getUserInfoByPhonenumber(phonenumber);
        return R.ok(user);
    }



    @ApiOperation(value = "获取用户列表")
    @GetMapping("/listByRemote")
    public R<List<SysUser>> listByRemote(@RequestParam(value = "userName", required = false) String userName, @RequestParam(value = "phoneNumber", required = false) String phoneNumber, @RequestParam(value = "pageNum", required = false) int pageNum, @RequestParam(value = "pageSize", required = false) int pageSize) {

        PageUtils.startPage(pageNum, pageSize);
        SysUser sysUser = new SysUser();
        sysUser.setUserName(userName);
        sysUser.setPhonenumber(phoneNumber);
        List<SysUser> list = userService.selectUserList(sysUser);
        TableDataInfo info = getDataTable(list);
        return R.ok(list, String.valueOf(info.getTotal()));
    }

    @GetMapping("getUserByNickName")
    public R<List<SysUserSimpleVO>> getUserByNickName(@RequestParam(value = "nickName") @Validated @NotBlank(message = "nickName不能为空！") String nickName) {
        List<SysUserSimpleVO> userList = userService.getUserByNickName(nickName);
        return R.ok(userList);
    }


    @ApiOperation(value = "获取用户列表", notes = "获取简单的用户信息，只查询用户表")
    @GetMapping("/getAllSimpleUserList")
    public R<List<SysUserSimpleVO>> getAllSimpleUserList() {
        List<SysUserSimpleVO> userList = userService.getAllSimpleUserList();
        return R.ok(userList);
    }


    @ApiOperation(value = "根据用户id获取用户信息")
    @GetMapping("/getByUserId/{userId}")
    public AjaxResult getByUserId(@PathVariable(value = "userId") Long userId) {
        SysUser sysUser = userService.getByUserId(userId);
        return AjaxResult.success(sysUser);
    }


    @ApiOperation(value = "查询所有用户基本信息")
    @GetMapping("/simpleUser/getAll")
    public AjaxResult<List<SysUserSimpleVO>> getAll() {
        return AjaxResult.success(userService.getAllSimpleUserList());
    }

    @ApiOperation(value = "根据属性IDS获取人员信息 岗位:post 角色:role 部门:dept 部门与岗位:deptAndPost")
    @GetMapping("/getUserListByAttribIds")
    public AjaxResult getUserListByAttribIds(@RequestParam("attribType") String attribType, @RequestParam("firstAttribIds") List<Long> firstAttribIds, @RequestParam(required = false, name = "secAttribIds") List<Long> secAttribIds) {
        List<SysUser> list = userService.getUserListByAttribIds(attribType, firstAttribIds, secAttribIds);
        return AjaxResult.success(list);
    }

    @ApiOperation(value = "根据递归部门获取人员信息")
    @GetMapping("/getUserListByRecursionDeptId")
    public AjaxResult getUserListByRecursionDeptId(@RequestParam(required = false, name = "recursionNum") Integer recursionNum, @RequestParam(required = false, name = "deptIds") List<Long> deptIds) {
        List<SysUser> list = userService.getUserListByRecursionDeptId(recursionNum, deptIds);
        return AjaxResult.success(list);
    }

    @Autowired
    public void setMenuService(ISysMenuService menuService) {
        this.menuService = menuService;
    }
}
