package com.soflyit.system.service;

import com.soflyit.system.api.domain.SysUser;
import com.soflyit.system.api.domain.vo.SysUserSimpleVO;
import com.soflyit.system.domain.SysUserPost;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户 业务层
 *
 * @author soflyit
 */
public interface ISysUserService {

    List<SysUser> selectUserList(SysUser user);


    List<SysUser> selectUser(SysUser user);


    List<SysUser> selectAllocatedList(SysUser user);


    List<SysUser> selectUnallocatedList(SysUser user);


    SysUser selectUserByUserName(SysUser condition);


    List<SysUser> getUsersByUserPhone(SysUser condition);


    SysUser selectUserById(Long appId, Long userId);


    String selectUserRoleGroup(String userName);


    String selectUserPostGroup(String userName);


    String selectUserDeptGroup(String userName);


    String checkUserNameUnique(String userName);


    String checkPhoneUnique(SysUser user);


    String checkEmailUnique(SysUser user);


    void checkUserAllowed(SysUser user);


    void checkUserDataScope(Long userId);


    int insertUser(SysUser user);


    boolean registerUser(SysUser user);


    int updateUser(SysUser user);


    void insertUserAuth(Long userId, Long[] roleIds);


    int updateUserStatus(SysUser user);


    int updateUserProfile(SysUser user);


    boolean updateUserAvatar(String userName, String avatar);


    int resetPwd(SysUser user);


    int changePwd(SysUser user);


    int deleteUserByIds(Long[] userIds);


    String importUser(List<SysUser> userList, Boolean isUpdateSupport, Long operId);


    List<SysUser> getUserByDeptId(Long deptId);


    List<SysUser> selectUserListByPostId(SysUser sysUser);


    List<SysUserPost> selectUserPostByUserId(Long userId);


    List<SysUser> getNickNameById(Long userId);


    void changeDefaultDept(Long deptId, HttpServletRequest request);


    List<SysUser> getUserInfoByUserNames(List<String> userNames);


    List<SysUser> getUserInfoByIds(List<Long> userIds);


    List<SysUserPost> selectUserPostByUserNames(List<String> userNames);


    List<SysUser> getUserInfoByExt1(Long userId);


    SysUser getUserInfoByPhonenumber(String phonenumber);


    List<SysUser> selectUserListByIds(Long[] ids);


    List<SysUserSimpleVO> getUserByNickName(String nickName);



    List<SysUserSimpleVO> getAllSimpleUserList();


    SysUser getByUserId(Long userId);


    Integer checkLoginFailedCount(String userName, Integer maxCount);

    List<SysUser> getUserListByAttribIds(String attribType, List<Long> firstAttribIds, List<Long> secAttribIds);

    List<SysUser> getUserListByRecursionDeptId(Integer recursionNum, List<Long> deptIds);

    List<SysUser> getNickNames(List<Long> userIds);

    int freezeUsers(List<Long> userIds);
}
