package com.soflyit.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.soflyit.common.core.constant.UserConstants;
import com.soflyit.common.core.exception.ServiceException;
import com.soflyit.common.core.utils.SpringUtils;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.core.utils.bean.BeanValidators;
import com.soflyit.common.datascope.annotation.DataScope;
import com.soflyit.common.redis.service.RedisService;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.domain.*;
import com.soflyit.system.api.domain.vo.SysUserSimpleVO;
import com.soflyit.system.config.SystemConfig;
import com.soflyit.system.domain.SysUserPost;
import com.soflyit.system.domain.SysUserRole;
import com.soflyit.system.domain.vo.SysUserVo;
import com.soflyit.system.mapper.*;
import com.soflyit.system.service.ISysDeptService;
import com.soflyit.system.service.ISysUserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Validator;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 用户 业务层处理
 *
 * @author soflyit
 */
@Service
public class SysUserServiceImpl implements ISysUserService {
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);
    @Autowired
    protected Validator validator;
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysRoleMapper roleMapper;
    @Autowired
    private SysPostMapper postMapper;
    @Autowired
    private SysDeptMapper deptMapper;
    @Autowired
    private SysUserRoleMapper userRoleMapper;
    @Autowired
    private SysUserPostMapper userPostMapper;
    @Resource
    private SysDeptUserMapper deptUserMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private SystemConfig systemConfig;

    @Autowired
    private ISysDeptService deptService;

    private static final String userIdNameKey = "system:user:nickName";
    @Autowired
    private AvatarService avatarService;


    @Override
    @DataScope(deptAlias = "u", userAlias = "u", deptFieldName = "dept_id")
    public List<SysUser> selectUserList(SysUser user) {
        List<SysUser> list = userMapper.selectUserList(user);
        for (SysUser users : list) {
            List<SysDept> userList = userMapper.selectUserListByUserId(users);
            List<SysPost> posts = userMapper.selectPostListByUserId(users);
            users.setPostsList(posts);
            users.setDepts(userList);
            List<SysUserPost> sysUserPosts = userPostMapper.selectUserPostByUserId(users.getUserId());
            StringBuilder postNames = new StringBuilder();
            for (SysUserPost sysUserPost : sysUserPosts) {
                postNames.append(sysUserPost.getDeptName()).append(" / ").append(sysUserPost.getPostName()).append(",");
            }
            if (postNames.length() > 0) {
                postNames.deleteCharAt(postNames.length() - 1);
            }
            users.setPostNames(postNames.toString());
            String avatar = avatarService.getUserAvatar(users);
            users.setAvatar(avatar);
        }
        return list;
    }


    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUser(SysUser user) {
        return userMapper.selectUser(user);
    }


    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectAllocatedList(SysUser user) {
        return userMapper.selectAllocatedList(user);
    }


    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUnallocatedList(SysUser user) {
        return userMapper.selectUnallocatedList(user);
    }


    @Override
    public SysUser selectUserByUserName(SysUser condition) {
        return userMapper.selectUserByUserName(condition);
    }


    @Override
    public SysUser selectUserById(Long appId, Long userId) {
        SysUser condition = new SysUser();
        condition.setUserId(userId);
        if (!SysUser.isAdmin(userId)) {
            condition.getParams().put("appId", appId);
        }
        SysUser sysUser = userMapper.selectUserById(condition);
        if (Objects.nonNull(sysUser)) {
            List<SysPost> posts = userMapper.selectPostListByUserId(sysUser);
            sysUser.setPostsList(posts);
        }
        String avatar = avatarService.getUserAvatar(sysUser);
        sysUser.setAvatar(avatar);
        return sysUser;
    }


    @Override
    public String selectUserRoleGroup(String userName) {
        Map<String, Object> params = new HashMap<>();
        params.put("userName", userName);
        List<SysRole> list = roleMapper.selectRolePermissionByUserInfo(params);
        if (CollectionUtils.isEmpty(list)) {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysRole::getRoleName).collect(Collectors.joining(","));
    }


    @Override
    public String selectUserPostGroup(String userName) {
        List<SysPost> list = postMapper.selectPostsByUserName(userName);
        if (CollectionUtils.isEmpty(list)) {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysPost::getPostName).collect(Collectors.joining(","));
    }


    @Override
    public String selectUserDeptGroup(String userName) {
        List<SysDept> list = deptMapper.selectDeptsByUserName(userName);
        if (CollectionUtils.isEmpty(list)) {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysDept::getDeptName).collect(Collectors.joining(","));
    }


    @Override
    public String checkUserNameUnique(String userName) {
        int count = userMapper.checkUserNameUnique(userName);
        if (count > 0) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }


    @Override
    public String checkPhoneUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkPhoneUnique(user.getPhonenumber());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }


    @Override
    public String checkEmailUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }


    @Override
    public void checkUserAllowed(SysUser user) {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员用户");
        }
    }


    @Override
    public void checkUserDataScope(Long userId) {
        if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
            SysUser user = new SysUser();
            user.setUserId(userId);
            List<SysUser> users = SpringUtils.getAopProxy(this).selectUserList(user);
            if (StringUtils.isEmpty(users)) {
                throw new ServiceException("没有权限访问用户数据！");
            }
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertUser(SysUser user) {

        int rows = userMapper.insertUser(user);
        if (user.getUserId() != null && StringUtils.isNotEmpty(user.getNickName())) {
            redisService.setCacheMapValue(userIdNameKey, String.valueOf(user.getUserId()), user.getNickName());
        }


        insertUserPost(user);

        insertUserRole(user);

        insertDeptUser(user);
        return rows;
    }

    private void insertDeptUser(SysUser user) {
        Long[] deptIds = user.getDeptIds();
        List<SysDeptUser> deptUsers = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(deptIds)) {
            Arrays.stream(deptIds).forEach(d -> {
                SysDeptUser deptUser = new SysDeptUser();
                deptUser.setDeptId(d);
                deptUser.setUserId(user.getUserId());
                deptUser.setCreateTime(new Date());
                deptUser.setCreateBy(SecurityUtils.getUserId());
                deptUsers.add(deptUser);
            });
            if (!CollectionUtils.isEmpty(deptUsers)) {
                deptUserMapper.batchDeptUser(deptUsers);
            }
        }
    }


    @Override
    public boolean registerUser(SysUser user) {
        return userMapper.insertUser(user) > 0;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUser(SysUser user) {
        Long userId = user.getUserId();


        userPostMapper.deleteUserPostByUserId(userId);

        insertUserPost(user);

        deptUserMapper.deleteSysDeptUserByUserId(userId);

        insertDeptUser(user);
        if (user.getUserId() != null && StringUtils.isNotEmpty(user.getNickName())) {
            redisService.setCacheMapValue(userIdNameKey, String.valueOf(user.getUserId()), user.getNickName());
        }
        user.setAvatar(null);

        return userMapper.updateUser(user);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertUserAuth(Long userId, Long[] roleIds) {
        userRoleMapper.deleteUserRoleByUserId(userId);
        insertUserRole(userId, roleIds);
    }


    @Override
    public int updateUserStatus(SysUser user) {
        return userMapper.updateUser(user);
    }


    @Override
    public int updateUserProfile(SysUser user) {
        return userMapper.updateUser(user);
    }


    @Override
    public boolean updateUserAvatar(String userName, String avatar) {
        return userMapper.updateUserAvatar(userName, avatar) > 0;
    }


    @Override
    public int resetPwd(SysUser user) {

        user.setDefaultPwdFlag(Short.valueOf("1"));
        return userMapper.updateUser(user);
    }


    @Override
    public int changePwd(SysUser user) {

        user.setDefaultPwdFlag(Short.valueOf("0"));
        return userMapper.updateUser(user);
    }


    public void insertUserRole(SysUser user) {
        Long[] roles = user.getRoleIds();
        if (ArrayUtils.isNotEmpty(roles)) {

            List<SysUserRole> list = new ArrayList<SysUserRole>();
            for (Long roleId : roles) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(user.getUserId());
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (!list.isEmpty()) {
                userRoleMapper.batchUserRole(list);
            }
        }
    }


    public void insertUserPost(SysUser user) {
        if (user instanceof SysUserVo) {
            SysUserVo userVo = (SysUserVo) user;
            List<SysUserPost> posts = userVo.getPosts();
            if (CollectionUtils.isEmpty(posts)) {
                return;
            }
            posts.forEach(p -> {
                p.setUserId(user.getUserId());
            });
            userPostMapper.batchUserPost(posts);
        }

    }


    public void insertUserRole(Long userId, Long[] roleIds) {
        if (StringUtils.isNotNull(roleIds)) {

            List<SysUserRole> list = new ArrayList<SysUserRole>();
            for (Long roleId : roleIds) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (!list.isEmpty()) {
                userRoleMapper.batchUserRole(list);
            }
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserByIds(Long[] userIds) {
        for (Long userId : userIds) {
            checkUserAllowed(new SysUser(userId));
            checkUserDataScope(userId);
        }

        userRoleMapper.deleteUserRole(userIds);

        userPostMapper.deleteUserPost(userIds);

        for (Long userId : userIds) {
            deptUserMapper.deleteSysDeptUserByUserId(userId);
        }
        return userMapper.deleteUserByIds(userIds);
    }


    public void addDeptUser(SysUser user, Boolean isUpdateSupport) {
        SysUser sysUser = userMapper.selectUserByUserName(user);
        SysDeptUser sysDeptUser = new SysDeptUser();
        sysDeptUser.setUserId(user.getUserId());
        sysDeptUser.setDeptId(user.getDeptId());
        if (isUpdateSupport) {
            sysDeptUser.setCreateTime(sysUser.getCreateTime());
            sysDeptUser.setCreateBy(sysUser.getCreateBy());
            sysDeptUser.setUpdateBy(SecurityUtils.getUserId());
            sysDeptUser.setUpdateTime(new Date());
        } else {
            sysDeptUser.setCreateTime(new Date());
            sysDeptUser.setCreateBy(SecurityUtils.getUserId());
        }
        deptUserMapper.insertSysDeptUser(sysDeptUser);
    }


    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, Long operId) {
        if (StringUtils.isNull(userList) || userList.isEmpty()) {
            throw new ServiceException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (SysUser user : userList) {
            try {

                SysUser condition = new SysUser();
                condition.setUserName(user.getUserName());
                condition.setPhonenumber(user.getPhonenumber());
                condition.getParams();
                SysUser u = userMapper.selectUserByUserName(condition);
                List<SysUser> ps = userMapper.selectUserByUserPhone(condition);
                if (StringUtils.isNull(u)) {
                    if (CollectionUtils.isEmpty(ps)) {
                        BeanValidators.validateWithException(validator, user);
                        user.setPassword(SecurityUtils.encryptPassword(systemConfig.getDefaultPassword()));
                        user.setCreateBy(operId);
                        if (userMapper.insertUser(user) > 0) {
                            this.addDeptUser(userMapper.selectUser(user).get(0), false);
                        }
                        if (user.getUserId() != null && StringUtils.isNotEmpty(user.getNickName())) {
                            redisService.setCacheMapValue(userIdNameKey, String.valueOf(user.getUserId()), user.getNickName());
                        }
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 导入成功");
                    } else {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、手机号 " + user.getPhonenumber() + " 已存在");
                    }

                } else if (isUpdateSupport) {
                    BeanValidators.validateWithException(validator, user);
                    SysUser sysUser = userMapper.selectUserByUserName(user);
                    user.setUserId(sysUser.getUserId());
                    user.setUpdateBy(operId);
                    userMapper.updateUser(user);
                    if (StringUtils.isNotEmpty(user.getNickName())) {
                        redisService.setCacheMapValue(userIdNameKey, String.valueOf(user.getUserId()), user.getNickName());
                    }
                    SysDeptUser deleteDept = new SysDeptUser();
                    deleteDept.setDeptId(sysUser.getDeptId());
                    deleteDept.setUserId(sysUser.getUserId());
                    deptUserMapper.deleteByDeptIdUserId(deleteDept);
                    this.addDeptUser(user, true);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUserName() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }



    @Override
    public List<SysUser> getUserByDeptId(Long deptId) {
        return userMapper.getUserByDeptId(deptId);
    }


    @Override
    public List<SysUser> getUsersByUserPhone(SysUser condition) {
        return userMapper.selectUserByUserPhone(condition);
    }


    @Override
    public List<SysUser> selectUserListByPostId(SysUser sysUser) {
        return userMapper.selectUserListByPostId(sysUser);
    }


    @Override
    public List<SysUser> getNickNameById(Long userId) {
        return userMapper.getNickNameById(userId);
    }


    @Override
    public List<SysUser> getNickNames(List<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            log.warn("查询用户昵称失败：用户Id不能为空");
            return new ArrayList<>();
        }
        return userMapper.getNickNames(userIds);
    }

    @Override
    public int freezeUsers(List<Long> userIds) {
        SysUser sysUser = new SysUser();
        sysUser.setStatus("1");
        LambdaUpdateWrapper<SysUser> userUpdateWrapper = new LambdaUpdateWrapper<>();
        userUpdateWrapper.in(SysUser::getUserId, userIds);
        return userMapper.update(sysUser, userUpdateWrapper);
    }


    @Override
    public List<SysUserPost> selectUserPostByUserId(Long userId) {
        return userPostMapper.selectUserPostByUserId(userId);
    }

    @Override
    public void changeDefaultDept(Long deptId, HttpServletRequest request) {


    }

    @Override
    public List<SysUser> getUserInfoByUserNames(List<String> userNames) {
        List<SysUser> sysUserList = userMapper.getUserInfoByUserNames(userNames);
        for (SysUser users : sysUserList) {
            try {
                List<SysDept> userList = userMapper.selectUserListByUserId(users);
                List<SysPost> posts = userMapper.selectPostListByUserId(users);
                List<SysUserPost> sysUserPosts = userPostMapper.selectUserPostByUserId(users.getUserId());
                StringBuilder postNames = new StringBuilder();
                for (SysUserPost sysUserPost : sysUserPosts) {
                    postNames.append(sysUserPost.getDeptName()).append(" / ").append(sysUserPost.getPostName()).append(",");
                }
                if (postNames.length() > 0) {
                    postNames.deleteCharAt(postNames.length() - 1);
                }
                users.setPostNames(postNames.toString());
                users.setDepts(userList);
                users.setPostsList(posts);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

        }
        return sysUserList;
    }

    @Override
    public List<SysUser> getUserInfoByIds(List<Long> userIds) {
        return userMapper.getUserInfoByIds(userIds);
    }

    @Override
    public List<SysUserPost> selectUserPostByUserNames(List<String> userNames) {
        return userPostMapper.selectUserPostByUserNames(userNames);
    }

    @Override
    public List<SysUser> getUserInfoByExt1(Long userId) {
        return userMapper.getUserInfoByExt1(userId);
    }

    @Override
    public SysUser getUserInfoByPhonenumber(String phonenumber) {
        SysUser user = new SysUser();
        user = this.userMapper.selectInfoByPhonenumber(phonenumber);
        return user;
    }

    @Override
    @DataScope(deptAlias = "u", userAlias = "u", deptFieldName = "dept_id")
    public List<SysUser> selectUserListByIds(Long[] ids) {
        List<SysUser> list = userMapper.selectUserListByIds(ids);
        for (SysUser users : list) {
            List<SysDept> userList = userMapper.selectUserListByUserId(users);
            List<SysPost> posts = userMapper.selectPostListByUserId(users);
            List<SysUserPost> sysUserPosts = userPostMapper.selectUserPostByUserId(users.getUserId());
            StringBuilder postNames = new StringBuilder();
            for (SysUserPost sysUserPost : sysUserPosts) {
                postNames.append(sysUserPost.getDeptName()).append(" / ").append(sysUserPost.getPostName()).append(",");
            }
            if (postNames.length() > 0) {
                postNames.deleteCharAt(postNames.length() - 1);
            }
            users.setPostNames(postNames.toString());
            users.setDepts(userList);
            users.setPostsList(posts);
        }
        return list;
    }

    @Override
    public List<SysUserSimpleVO> getUserByNickName(String nickName) {
        return userMapper.selectUserByNickName(nickName);

    }


    @Override
    public List<SysUserSimpleVO> getAllSimpleUserList() {
        return userMapper.getAllSimpleUserList();
    }


    @Override
    public SysUser getByUserId(Long userId) {
        return userMapper.getByUserId(userId);
    }


    @Override
    public Integer checkLoginFailedCount(String userName, Integer maxCount) {
        String loginFailedKey = "user_login_failed:" + userName;
        Integer failedCount = 0;
        if (redisService.hasKey(loginFailedKey)) {
            failedCount = redisService.getCacheObject(loginFailedKey);
            if (failedCount == null) {
                failedCount = 0;
            }
            failedCount++;
            if (failedCount >= maxCount) {
                lockUserByName(userName);
                redisService.deleteObject(loginFailedKey);
            } else {
                redisService.setCacheObject(loginFailedKey, failedCount, 30L, TimeUnit.DAYS);
            }
        } else {
            failedCount++;
            redisService.setCacheObject(loginFailedKey, failedCount, 30L, TimeUnit.DAYS);
        }
        return failedCount;
    }

    private void lockUserByName(String userName) {
        userMapper.lockByUserName(userName);
    }

    @Override
    public List<SysUser> getUserListByAttribIds(String attribType, List<Long> firstAttribIds, List<Long> secAttribIds) {
        return userMapper.selectUserListByAttribIds(attribType, firstAttribIds, secAttribIds);
    }

    @Override
    public List<SysUser> getUserListByRecursionDeptId(Integer recursionNum, List<Long> deptIds) {
        List<String> userStrIds = new ArrayList<>();
        deptIds.forEach(deptId -> {
            for (int i = 0; i < recursionNum; i++) {
                SysDept sysDept = deptService.selectDeptById(deptId);
                if (i == 0) {
                    userStrIds.add(sysDept.getLeader());
                }
                SysDept parentDept = deptService.selectDeptById(sysDept.getParentId());
                if (Objects.isNull(parentDept)) {
                    break;
                }
                userStrIds.add(parentDept.getLeader());
                deptId = parentDept.getDeptId();
            }
        });

        List<Long> userIds = userStrIds.stream().filter(Objects::nonNull).distinct().map(Long::valueOf).collect(Collectors.toList());
        return CollectionUtils.isEmpty(userIds) ? null : this.getUserInfoByIds(userIds);
    }

    @PostConstruct
    private void initUserNickName() {
        SysUser condition = new SysUser();
        List<SysUser> sysUserList = userMapper.selectUserList(condition);
        Map<String, String> userIdNameMap = sysUserList.stream().collect(Collectors.toMap(key -> String.valueOf(key.getUserId()), SysUser::getNickName));
        redisService.setCacheMap(userIdNameKey, userIdNameMap);
    }

}
