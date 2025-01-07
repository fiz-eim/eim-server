package com.soflyit.chattask.im.channel.service.impl;

import cn.hutool.core.lang.Validator;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.soflyit.chattask.im.channel.domain.entity.ChannelMember;
import com.soflyit.chattask.im.channel.domain.entity.ChatChannel;
import com.soflyit.chattask.im.channel.domain.entity.HisChannelMember;
import com.soflyit.chattask.im.channel.domain.param.MemberChangeNotifyParam;
import com.soflyit.chattask.im.channel.domain.param.MemberRemoveParam;
import com.soflyit.chattask.im.channel.domain.vo.*;
import com.soflyit.chattask.im.channel.mapper.ChannelMemberMapper;
import com.soflyit.chattask.im.channel.mapper.HisChannelMemberMapper;
import com.soflyit.chattask.im.channel.service.IChannelMemberService;
import com.soflyit.chattask.im.channel.service.IChatChannelService;
import com.soflyit.chattask.im.config.SoflyImConfig;
import com.soflyit.chattask.im.event.domain.ChannelMemberAddEvent;
import com.soflyit.chattask.im.event.domain.ChannelMemberDeleteEvent;
import com.soflyit.chattask.im.event.domain.ChannelMemberUpdateEvent;
import com.soflyit.chattask.im.event.service.ChatEventService;
import com.soflyit.chattask.im.message.domain.param.SystemMessage;
import com.soflyit.chattask.im.message.domain.vo.sysdata.AddMemberData;
import com.soflyit.chattask.im.message.domain.vo.sysdata.ChangeOwnerData;
import com.soflyit.chattask.im.message.domain.vo.sysdata.LeaveMemberData;
import com.soflyit.chattask.im.message.domain.vo.sysdata.RemoveMemberData;
import com.soflyit.chattask.im.message.service.IMessageService;
import com.soflyit.chattask.im.message.service.impl.MessagePushService;
import com.soflyit.chattask.im.system.service.impl.UserNickNameService;
import com.soflyit.chattask.lib.netty.event.ChatBroadcast;
import com.soflyit.common.core.domain.R;
import com.soflyit.common.core.utils.DateUtils;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.core.utils.bean.BeanUtils;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.id.util.SnowflakeIdUtil;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.RemoteUserService;
import com.soflyit.system.api.model.LoginUser;
import io.netty.channel.ChannelId;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.soflyit.chattask.im.common.constant.ChannelConstant.*;
import static com.soflyit.common.core.constant.SecurityConstants.INNER;

/**
 * 频道成员Service业务层处理
 *
 * @author soflyit
 * @date 2023-11-16 11:05:18
 */
@Service
@Slf4j
public class ChannelMemberServiceImpl implements IChannelMemberService {

    private ChannelMemberMapper channelMemberMapper;
    private ChatEventService chatEventService;
    private UserNickNameService userNickNameService;
    private HisChannelMemberMapper hisChannelMemberMapper;
    private IMessageService messageService;

    private SoflyImConfig soflyImConfig;

    private IChatChannelService channelService;

    private RemoteUserService remoteUserService;

    @Autowired
    private MessagePushService messagePushService;


    @Override
    public ChannelMember selectChannelMemberById(Long id) {
        return channelMemberMapper.selectChannelMemberById(id);
    }


    @Override
    public List<MemberVo> selectChannelMemberList(ChannelMember channelMember) {

        Long channelId = channelMember.getChannelId();
        if (channelId == null) {
            log.warn("查询频道成员失败：频道Id不能为空");
            return new ArrayList<>();
        }
        ChatChannel channel = channelService.selectChatChannelById(channelId);
        if (channel == null) {
            log.warn("查询频道成员失败：获取频道【{}】信息失败", channelId);
            return new ArrayList<>();
        }

        String channelExtData = StringUtils.defaultString(channel.getExtData(), "{}");
        ChannelExtData extData = JSON.parseObject(channelExtData, ChannelExtData.class);


        List<ChannelMember> members = channelMemberMapper.selectChannelMemberList(channelMember);
        List<MemberVo> memberVoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(members)) {
            BanConfig banConfig = extData.getBanConfig();
            if (banConfig == null) {
                banConfig = new BanConfig();
            }
            Boolean banAllFlag = banConfig.getBanAllFlag();
            if (banAllFlag == null) {
                banAllFlag = Boolean.FALSE;
            }
            List<Long> blackList = banConfig.getBlacklist();
            if (blackList == null) {
                blackList = new ArrayList<>();
            }
            List<Long> whitelist = banConfig.getWhitelist();
            if (whitelist == null) {
                whitelist = new ArrayList<>();
            }


            Map<Long, String> userAvatarMap = new HashMap<>();
            List<Long> userIds = members.stream().map(ChannelMember::getUserId).collect(Collectors.toList());
            int index = 200;
            while (userIds.size() > index) {
                List<Long> subUserIds = userIds.subList(index - 200, index);
                R<Map<Long, String>> avatarMapResult = remoteUserService.getUserAvatarByUserIds(subUserIds, INNER);
                if (avatarMapResult.getCode() == R.SUCCESS) {
                    userAvatarMap.putAll(avatarMapResult.getData());
                }
                index += 200;
            }

            if (userIds.size() > index - 200) {
                List<Long> subUserIds = userIds.subList(index - 200, userIds.size());
                R<Map<Long, String>> avatarMapResult = remoteUserService.getUserAvatarByUserIds(subUserIds, INNER);
                if (avatarMapResult.getCode() == R.SUCCESS) {
                    userAvatarMap.putAll(avatarMapResult.getData());
                }
            }

            List<Long> finalWhitelist = whitelist;
            List<Long> finalBlackList = blackList;
            Boolean finalBanAllFlag = banAllFlag;

            List<Long> memberUserIds = new ArrayList<>();
            for (ChannelMember member : members) {
                if (memberUserIds.contains(member.getUserId())) {
                    continue;
                }
                memberUserIds.add(member.getUserId());
                MemberVo memberVo = new MemberVo();
                BeanUtils.copyProperties(member, memberVo);

                if (finalBlackList.contains(memberVo.getUserId())) {
                    memberVo.setBanFlag(Boolean.TRUE);
                } else if (finalWhitelist.contains(memberVo.getUserId())) {
                    memberVo.setBanFlag(Boolean.FALSE);
                } else if (finalBanAllFlag) {
                    memberVo.setBanFlag(Boolean.TRUE);
                }

                if (userAvatarMap != null) {
                    memberVo.setAvatar(userAvatarMap.get(member.getUserId()));
                }
                memberVoList.add(memberVo);

            }
        }
        memberVoList.sort(Comparator.comparing(ChannelMember::getManager));
        return memberVoList;
    }


    @Override
    public int insertChannelMember(ChannelMember channelMember) {
        channelMember.setCreateTime(DateUtils.getNowDate());
        return channelMemberMapper.insertChannelMember(channelMember);
    }


    @Override
    public int updateChannelMember(ChannelMember channelMember) {
        channelMember.setUpdateTime(DateUtils.getNowDate());
        return channelMemberMapper.updateChannelMember(channelMember);
    }


    @Override
    public int deleteChannelMemberByIds(Long[] ids) {
        return channelMemberMapper.deleteChannelMemberByIds(ids);
    }


    @Override
    public int deleteChannelMemberById(Long id) {
        return channelMemberMapper.deleteChannelMemberById(id);
    }


    @Override
    public AjaxResult<MemberCountVo> getMemberCount(Long channelId) {
        LambdaQueryWrapper<ChannelMember> memberQueryWrapper = new LambdaQueryWrapper<>();
        memberQueryWrapper.select(ChannelMember::getId);
        memberQueryWrapper.eq(ChannelMember::getChannelId, channelId);
        Long count = channelMemberMapper.selectCount(memberQueryWrapper);
        MemberCountVo countVo = new MemberCountVo();
        countVo.setId(channelId);
        if (count != null) {
            countVo.setMemberCount(count.intValue());
        } else {
            countVo.setMemberCount(0);
        }
        return AjaxResult.success(countVo);
    }


    @Override
    public AjaxResult<MemberVo> getMemberInfo(ChannelMember condition) {

        LambdaQueryWrapper<ChannelMember> memberQueryWrapper = new LambdaQueryWrapper<>(ChannelMember.class);
        memberQueryWrapper.eq(ChannelMember::getChannelId, condition.getChannelId());
        memberQueryWrapper.eq(ChannelMember::getUserId, condition.getUserId());

        List<ChannelMember> members = channelMemberMapper.selectList(memberQueryWrapper);
        if (CollectionUtils.isEmpty(members)) {
            String msg = "查询频道成员信息失败，成员不存在";
            return AjaxResult.error(msg);
        }
        ChannelMember member = members.remove(0);

        MemberVo memberVo = new MemberVo();
        BeanUtils.copyBeanProp(memberVo, member);
        memberVo.setNotifyPropData(JSON.parseObject(StringUtils.defaultString(member.getNotifyProps(), "{}"), MemberNotifyProp.class));
        return AjaxResult.success(memberVo);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult removeMember(MemberRemoveParam condition) {


        LoginUser user = SecurityUtils.getLoginUser();

        List<Long> memberUserIds = condition.getMemberList().stream().map(ChannelMember::getUserId).collect(Collectors.toList());


        LambdaQueryWrapper<ChannelMember> memberQueryWrapper = new LambdaQueryWrapper<>(ChannelMember.class);
        memberQueryWrapper.setEntityClass(ChannelMember.class);
        memberQueryWrapper.eq(ChannelMember::getChannelId, condition.getChannelId());
        memberQueryWrapper.in(ChannelMember::getUserId, memberUserIds);
        List<ChannelMember> membersToDelete = channelMemberMapper.selectList(memberQueryWrapper);

        if (CollectionUtils.isEmpty(membersToDelete)) {
            List<Long> deletedMemberIds = membersToDelete.stream().map(ChannelMember::getUserId).collect(Collectors.toList());
            log.warn("成员【{}】已删除，不需要再次删除", JSON.toJSONString(deletedMemberIds));
            return AjaxResult.success();
        }
        for (ChannelMember member : membersToDelete) {

            HisChannelMember historyMember = new HisChannelMember();
            historyMember.setId(SnowflakeIdUtil.nextId());
            historyMember.setMemberId(member.getId());
            if (member.getCreateTime() != null) {
                historyMember.setJoinTime(member.getCreateTime().getTime());
            }
            historyMember.setLeaveTime(System.currentTimeMillis());
            historyMember.setMemberData(JSON.toJSONString(member));

            hisChannelMemberMapper.insertHisChannelMember(historyMember);
            channelMemberMapper.deleteChannelMemberById(member.getId());
        }

        processRemoveMember(membersToDelete, user, condition.getChannelId());


        return AjaxResult.success();
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult leaveChannel(Long channelId) {

        LoginUser loginUser = SecurityUtils.getLoginUser();

        LambdaQueryWrapper<ChannelMember> memberQueryWrapper = new LambdaQueryWrapper<>(ChannelMember.class);
        memberQueryWrapper.eq(ChannelMember::getChannelId, channelId);
        memberQueryWrapper.eq(ChannelMember::getUserId, loginUser.getUserid());
        List<ChannelMember> memberUsers = channelMemberMapper.selectList(memberQueryWrapper);
        if (CollectionUtils.isEmpty(memberUsers)) {
            return AjaxResult.success();
        }
        ChannelMember member = memberUsers.remove(0);

        HisChannelMember historyMember = new HisChannelMember();
        historyMember.setId(SnowflakeIdUtil.nextId());
        historyMember.setMemberId(member.getId());
        if (member.getCreateTime() != null) {
            historyMember.setJoinTime(member.getCreateTime().getTime());
        }
        historyMember.setLeaveTime(System.currentTimeMillis());
        historyMember.setMemberData(JSON.toJSONString(member));

        hisChannelMemberMapper.insertHisChannelMember(historyMember);
        channelMemberMapper.deleteChannelMemberById(member.getId());

        if (CollectionUtils.isNotEmpty(memberUsers)) {
            memberUsers.forEach(item -> {
                channelMemberMapper.deleteChannelMemberById(item.getId());
                if (MEMBER_ROLE_OWNER.equals(item.getManager())) {
                    member.setManager(MEMBER_ROLE_OWNER);
                }
            });
        }


        processLeaveChannel(member);
        ChannelMember newOwner = null;
        if (MEMBER_ROLE_OWNER.equals(member.getManager())) {

            memberQueryWrapper.clear();
            memberQueryWrapper.select(ChannelMember::getUserId, ChannelMember::getManager, ChannelMember::getId, ChannelMember::getChannelId);
            memberQueryWrapper.eq(ChannelMember::getChannelId, channelId);
            memberQueryWrapper.orderByAsc(ChannelMember::getMemberType, ChannelMember::getId);
            memberQueryWrapper.last(" limit 1");
            List<ChannelMember> members = channelMemberMapper.selectList(memberQueryWrapper);
            if (CollectionUtils.isNotEmpty(members)) {
                newOwner = members.get(0);
            }
        }

        if (newOwner != null) {
            LambdaUpdateWrapper<ChannelMember> memberUpdateWrapper = new LambdaUpdateWrapper<>(ChannelMember.class);
            memberUpdateWrapper.set(ChannelMember::getManager, MEMBER_ROLE_OWNER);
            memberUpdateWrapper.eq(ChannelMember::getUserId, newOwner.getUserId());
            memberUpdateWrapper.eq(ChannelMember::getChannelId, channelId);
            channelMemberMapper.update(null, memberUpdateWrapper);

            processChangeOwner(member, newOwner);
        }
        return AjaxResult.success();
    }


    @Override
    public AjaxResult changeRole(ChannelMember member) {
        return AjaxResult.error("暂不支持该功能");
    }

    @Override
    public AjaxResult pinChannel(ChannelMember channelMember) {
        return AjaxResult.error("暂不支持该功能");
    }

    @Override
    public AjaxResult dndChannel(ChannelMember channelMember) {
        return AjaxResult.error("暂不支持该功能");

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult<List<ChannelMember>> addChannelMember(List<ChannelMember> channelMembers, Long channelId, Boolean checkBusinessFlag) {

        LoginUser loginUser = SecurityUtils.getLoginUser();

        if (CollectionUtils.isEmpty(channelMembers)) {
            String msg = "添加成员失败，成员列表不能为空";
            return AjaxResult.error(msg);
        }

        List<Long> userIds = channelMembers.stream().map(ChannelMember::getUserId).collect(Collectors.toList());
        userIds.add(loginUser.getUserid());

        Map<Long, String> userNameMap = userNickNameService.getNickNameByIds(userIds);

        ChannelMember condition = new ChannelMember();
        condition.setChannelId(channelId);
        List<ChannelMember> existMembers = channelMemberMapper.selectChannelMemberList(condition);
        List<Long> existUserIds = existMembers.stream().map(ChannelMember::getUserId).collect(Collectors.toList());



        String realName = null;
        List<Long> memberUserIds = new ArrayList<>();
        for (ChannelMember channelMember : channelMembers) {
            realName = userNameMap.get(channelMember.getUserId());
            if (existUserIds.contains(channelMember.getUserId())) {
                log.warn("用户{}已存在，无需添加", realName);
                continue;
            }
            channelMember.setId(SnowflakeIdUtil.nextId());
            channelMember.setRealName(realName);
            channelMember.setChannelId(channelId);
            channelMember.setManager(MEMBER_ROLE_MEMBER);
            channelMember.setPinnedFlag(PINNED_FLAG_FALSE);
            channelMember.setDndFlag(DND_FLAG_FALSE);
            channelMember.setCollapse(COLLAPSE_FLAG_FALSE);
            channelMember.setLastViewTime(new Date());
            channelMember.setMsgCount(0L);
            channelMember.setUnreadCount(0L);
            channelMember.setMentionCount(0L);
            channelMember.setRootMsgCount(0L);
            channelMember.setMentionRootCount(0L);
            channelMember.setUrgentMentionCount(0L);
            channelMember.setMemberType(MEMBER_TYPE_USER);

            MemberNotifyProp notifyProp = new MemberNotifyProp();
            notifyProp.setEmail(NOTICE_DEFAULT);
            notifyProp.setPush(NOTICE_DEFAULT);
            notifyProp.setDesktop(NOTICE_DEFAULT);
            notifyProp.setMarkUnread(NOTICE_MARK_UNREAD_ALL);
            channelMember.setNotifyProps(JSON.toJSONString(notifyProp));

            channelMember.setCreateBy(null);
            channelMember.setCreateTime(null);
            channelMember.setUpdateTime(null);
            channelMember.setUpdateBy(null);
            memberUserIds.add(channelMember.getUserId());
            channelMemberMapper.insertChannelMember(channelMember);
        }
        processAddMembers(new ArrayList<>(channelMembers), channelId, loginUser, userNameMap);
        return AjaxResult.success();
    }


    @Override
    public AjaxResult<ChannelMember> updateNotify(Long channelId, Long userId, MemberChangeNotifyParam param) {

        Long currentUserId = SecurityUtils.getUserId();
        if (currentUserId == null) {
            log.error("修改成员通知配置失败：没有登录");
            String msg = "修改成员通知配置失败：没有登录或token已过期";
            return AjaxResult.error(msg);
        }


        LambdaQueryWrapper<ChannelMember> memberQueryWrapper = new LambdaQueryWrapper<>(ChannelMember.class);
        memberQueryWrapper.eq(ChannelMember::getChannelId, channelId);
        memberQueryWrapper.eq(ChannelMember::getUserId, userId);
        List<ChannelMember> members = channelMemberMapper.selectList(memberQueryWrapper);
        if (CollectionUtils.isEmpty(members)) {
            log.error("修改成员通知配置失败：成员不存在");
            String msg = "修改成员通知配置失败：成员不存在";
            return AjaxResult.error(msg);
        }

        ChannelMember member = members.remove(0);

        String notifyProps = StringUtils.defaultString(member.getNotifyProps(), "{}");
        MemberChangeNotifyParam notifyPropsInDb = JSON.parseObject(notifyProps, MemberChangeNotifyParam.class);
        BeanUtils.copyBeanProp(notifyPropsInDb, param);

        member.setNotifyProps(JSON.toJSONString(notifyPropsInDb));
        channelMemberMapper.updateChannelMember(member);

        processChangeNotify(member);

        return AjaxResult.success(member);
    }

    @Override
    public ChannelMember getUnreadCount(Long userId, Long channelId) {


        LambdaQueryWrapper<ChannelMember> memberQueryWrapper = new LambdaQueryWrapper<>(ChannelMember.class);
        memberQueryWrapper.eq(ChannelMember::getChannelId, channelId);
        memberQueryWrapper.eq(ChannelMember::getUserId, userId);
        List<ChannelMember> members = channelMemberMapper.selectList(memberQueryWrapper);
        if (CollectionUtils.isNotEmpty(members)) {
            return members.get(0);
        }
        return new ChannelMember();
    }


    @Override
    public AjaxResult addManagers(List<ChannelMember> members, Long channelId) {
        return AjaxResult.error("暂不支持");
    }

    @Override
    public AjaxResult deleteManager(ChannelMember member, Long channelId) {
        return AjaxResult.error("暂不支持");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult changeOwner(ChannelMember member, Long channelId) {
        return AjaxResult.error("暂不支持");
    }


    @Override
    public AjaxResult changeChannelDisplayName(ChannelMember channelMember) {
        return AjaxResult.error("暂不支持");
    }


    @Override
    public AjaxResult updateMemberNickName(ChannelMember channelMember) {
        return AjaxResult.error("暂不支持");
    }

    @Override
    public AjaxResult collapseChannel(ChannelMember channelMember) {
        return AjaxResult.error("暂不支持");
    }


    private void processRemoveMember(List<ChannelMember> channelMembers, LoginUser user, Long channelId) {

        String operatorName = userNickNameService.getNickName(user.getUserid());

        if (CollectionUtils.isNotEmpty(channelMembers)) {
            RemoveMemberData removeMemberData = new RemoveMemberData();
            removeMemberData.setUserId(user.getUserid());
            removeMemberData.setRealName(operatorName);
            removeMemberData.setMemberList(channelMembers);

            SystemMessage systemMessage = new SystemMessage();

            systemMessage.setChannelId(channelId);
            systemMessage.setUserId(user.getUserid());
            systemMessage.setData(removeMemberData);

            messageService.generateSystemMessage(systemMessage, null);

            channelMembers.forEach(member -> {
                ChannelMemberDeleteEvent event = new ChannelMemberDeleteEvent();
                event.setData(member);
                ChatBroadcast<Long, Long, ChannelId> broadcast = new ChatBroadcast<>();
                broadcast.setUserId(member.getUserId());
                event.setBroadcast(broadcast);
                chatEventService.doProcessEvent(event);
            });
        }
    }

    private String getAvatarText(String name) {
        if (StringUtils.isNotEmpty(name)) {
            if (Validator.isChinese(name)) {
                return getChineseAvatarText(name);
            } else {
                if (name.length() > 2) {
                    return name.substring(0, 2);
                } else {
                    return name;
                }
            }
        }
        return null;
    }

    private String getChineseAvatarText(String name) {
        if (name.length() > 2) {
            return name.substring(name.length() - 2, name.length());
        } else {
            return name;
        }
    }


    private void processChangeNotify(ChannelMember member) {

        ChannelMemberUpdateEvent channelMemberDeleteEvent = new ChannelMemberUpdateEvent();
        channelMemberDeleteEvent.setData(member);

        ChatBroadcast<Long, Long, ChannelId> broadcast = new ChatBroadcast<>();
        broadcast.setUserId(member.getUserId());
        channelMemberDeleteEvent.setBroadcast(broadcast);
        chatEventService.doProcessEvent(channelMemberDeleteEvent);
    }


    private void processAddMembers(ArrayList<ChannelMember> channelMembers, Long channelId, LoginUser loginUser, Map<Long, String> userNameMap) {

        String operatorName = userNameMap.get(loginUser.getUserid());
        if (CollectionUtils.isNotEmpty(channelMembers)) {
            AddMemberData addMemberData = new AddMemberData();
            addMemberData.setUserId(loginUser.getUserid());
            addMemberData.setRealName(operatorName);
            addMemberData.setMemberList(channelMembers);

            SystemMessage systemMessage = new SystemMessage();
            systemMessage.setChannelId(channelId);
            systemMessage.setUserId(loginUser.getUserid());
            systemMessage.setData(addMemberData);

            messageService.generateSystemMessage(systemMessage, null);


            channelMembers.forEach(item -> {

                ChannelVo channel = channelService.selectUserChannel(item.getUserId(), channelId);
                ChannelMemberAddEvent channelMemberAddEvent = new ChannelMemberAddEvent();
                channelMemberAddEvent.setData(channel);

                ChatBroadcast<Long, Long, ChannelId> broadcast = new ChatBroadcast<>();
                broadcast.setUserId(item.getUserId());
                channelMemberAddEvent.setBroadcast(broadcast);
                chatEventService.doProcessEvent(channelMemberAddEvent);
            });


        }
    }


    private void processLeaveChannel(ChannelMember member) {
        if (member != null) {
            LeaveMemberData leaveMemberData = new LeaveMemberData();
            leaveMemberData.setUserId(member.getUserId());
            leaveMemberData.setRealName(member.getRealName());

            SystemMessage systemMessage = new SystemMessage();
            systemMessage.setChannelId(member.getChannelId());
            systemMessage.setUserId(member.getUserId());

            systemMessage.setData(leaveMemberData);

            messageService.generateSystemMessage(systemMessage, null);
        }
    }

    private void processChangeOwner(ChannelMember member, ChannelMember newOwner) {
        if (member != null) {

            ChangeOwnerData changeOwnerData = new ChangeOwnerData();
            changeOwnerData.setOldOwnerId(member.getUserId());
            changeOwnerData.setOldOwnerUser(member.getRealName());
            changeOwnerData.setNewOwnerId(newOwner.getId());
            changeOwnerData.setNewOwnerUser(newOwner.getRealName());

            SystemMessage systemMessage = new SystemMessage();
            systemMessage.setChannelId(member.getChannelId());
            systemMessage.setData(changeOwnerData);

            messageService.generateSystemMessage(systemMessage, null);
        }
    }

    @Autowired
    public void setChatEventService(ChatEventService chatEventService) {
        this.chatEventService = chatEventService;
    }

    @Autowired
    public void setChannelMemberMapper(ChannelMemberMapper channelMemberMapper) {
        this.channelMemberMapper = channelMemberMapper;
    }

    @Autowired
    public void setHisChannelMemberMapper(HisChannelMemberMapper hisChannelMemberMapper) {
        this.hisChannelMemberMapper = hisChannelMemberMapper;
    }

    @Autowired
    public void setMessageService(IMessageService messageService) {
        this.messageService = messageService;
    }

    @Autowired
    public void setUserNickNameService(UserNickNameService userNickNameService) {
        this.userNickNameService = userNickNameService;
    }

    @Autowired
    public void setSoflyImConfig(SoflyImConfig soflyImConfig) {
        this.soflyImConfig = soflyImConfig;
    }

    @Autowired
    public void setChannelService(IChatChannelService channelService) {
        this.channelService = channelService;
    }

    @Autowired
    public void setRemoteUserService(RemoteUserService remoteUserService) {
        this.remoteUserService = remoteUserService;
    }
}
