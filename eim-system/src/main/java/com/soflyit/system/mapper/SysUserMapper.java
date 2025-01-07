package com.soflyit.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soflyit.system.api.domain.SysDept;
import com.soflyit.system.api.domain.SysPost;
import com.soflyit.system.api.domain.SysUser;
import com.soflyit.system.api.domain.vo.SysUserSimpleVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户表 数据层
 *
 * @author soflyit
 */
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

    List<SysUser> selectUserListByIds(Long[] userIds);


    List<SysUser> selectUserList(SysUser sysUser);


    List<SysUser> selectUser(SysUser sysUser);


    List<SysUser> selectAllocatedList(SysUser user);


    List<SysUser> selectUnallocatedList(SysUser user);


    SysUser selectUserByUserName(SysUser condition);


    List<SysUser> selectUserByUserPhone(SysUser condition);


    SysUser selectUserById(SysUser user);


    int insertUser(SysUser user);


    int updateUser(SysUser user);


    int updateUserAvatar(@Param("userName") String userName, @Param("avatar") String avatar);


    int deleteUserById(Long userId);


    int deleteUserByIds(Long[] userIds);


    int checkUserNameUnique(String userName);


    SysUser checkPhoneUnique(String phonenumber);


    SysUser checkEmailUnique(String email);


    List<SysUser> selectUserByDeptId(Long deptId);


    List<SysUser> getUserByDeptId(Long deptId);


    List<SysUser> selectUserListByPostId(SysUser sysUser);


    List<SysUser> selectUnSelectUser(SysUser sysUser);


    List<SysUser> getNickNameById(Long userId);


    List<SysUser> getUserInfoByUserNames(@Param("userNames") List<String> userNames);


    List<SysUser> getUserInfoByIds(@Param("userIds") List<Long> userIds);


    List<SysDept> selectUserListByUserId(SysUser users);

    List<SysUser> getUserInfoByExt1(Long userId);


    SysUser selectInfoByPhonenumber(@Param("phonenumber") String phonenumber);

    List<SysPost> selectPostListByUserId(SysUser users);

    List<SysUserSimpleVO> selectUserByNickName(@Param("nickName") String nickName);



    List<SysUserSimpleVO> getAllSimpleUserList();


    SysUser getByUserId(Long userId);


    void lockByUserName(String userName);

    List<SysUser> selectUserListByAttribIds(@Param("attribType") String attribType, @Param("firstAttribIds") List<Long> firstAttribIds, @Param("secAttribIds") List<Long> secAttribIds);

    List<SysUser> getNickNames(List<Long> userIds);
}
