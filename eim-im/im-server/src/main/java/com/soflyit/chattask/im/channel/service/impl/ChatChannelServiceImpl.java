package com.soflyit.chattask.im.channel.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soflyit.chattask.im.bot.service.IImCardService;
import com.soflyit.chattask.im.channel.domain.entity.ChannelMember;
import com.soflyit.chattask.im.channel.domain.entity.ChatChannel;
import com.soflyit.chattask.im.channel.domain.param.CardData;
import com.soflyit.chattask.im.channel.domain.param.ChannelAddParam;
import com.soflyit.chattask.im.channel.domain.param.ChannelQueryParam;
import com.soflyit.chattask.im.channel.domain.param.UserChannelQueryParam;
import com.soflyit.chattask.im.channel.domain.vo.ChannelExtData;
import com.soflyit.chattask.im.channel.domain.vo.ChannelVo;
import com.soflyit.chattask.im.channel.domain.vo.DirectChannelVo;
import com.soflyit.chattask.im.channel.mapper.ChannelMemberMapper;
import com.soflyit.chattask.im.channel.mapper.ChatChannelMapper;
import com.soflyit.chattask.im.channel.service.IChatChannelService;
import com.soflyit.chattask.im.common.util.MessageMetaUtil;
import com.soflyit.chattask.im.event.domain.ChannelCreatedEvent;
import com.soflyit.chattask.im.event.domain.ChannelDeleteEvent;
import com.soflyit.chattask.im.event.domain.ChannelRestoreEvent;
import com.soflyit.chattask.im.event.domain.ChannelUpdateEvent;
import com.soflyit.chattask.im.event.service.ChatEventService;
import com.soflyit.chattask.im.message.domain.entity.*;
import com.soflyit.chattask.im.message.domain.vo.*;
import com.soflyit.chattask.im.message.mapper.*;
import com.soflyit.chattask.im.message.service.IMsgTopService;
import com.soflyit.chattask.im.message.service.impl.MessageQueryService;
import com.soflyit.chattask.im.system.service.impl.UserNickNameService;
import com.soflyit.chattask.lib.netty.event.ChatBroadcast;
import com.soflyit.common.core.exception.base.BaseException;
import com.soflyit.common.core.utils.PageUtils;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.core.utils.bean.BeanUtils;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.id.util.SnowflakeIdUtil;
import com.soflyit.common.security.auth.AuthUtil;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.RemoteAvatarApi;
import com.soflyit.system.api.domain.SysUser;
import com.soflyit.system.api.model.LoginUser;
import io.netty.channel.ChannelId;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.soflyit.chattask.im.common.constant.ChannelConstant.*;
import static com.soflyit.chattask.im.common.constant.MessageConstant.MESSAGE_TYPE_BOT;
import static com.soflyit.chattask.im.common.constant.MessageConstant.MESSAGE_TYPE_USER;
import static com.soflyit.common.core.constant.SecurityConstants.INNER;

/**
 * 频道Service业务层处理
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@Service
@Slf4j
public class ChatChannelServiceImpl extends ServiceImpl<ChatChannelMapper, ChatChannel> implements IChatChannelService {

    private ChatChannelMapper chatChannelMapper;

    private ChannelMemberMapper channelMemberMapper;

    private ChatEventService chatEventService;

    private MessageMapper messageMapper;

    private UserNickNameService userNickNameService;

    private ChannelIconService channelIconService;

    private MessageQueryService messageQueryService;

    @Autowired
    private IMsgTopService msgTopService;

    @Autowired
    private MemberDeleteMsgMapper memberDeleteMsgMapper;

    @Autowired
    private IImCardService imCardService;

    @Autowired
    private MsgUnreadMapper msgUnreadMapper;

    @Autowired
    private MsgReplyMapper msgReplyMapper;

    private static final String EXT_DEFAULT_VALUE = "{\"banConfig\": {\"blacklist\": [], \"whitelist\": [], \"banAllFlag\": false}, \"ownerManageFlag\": false, \"ownerTopMsgFlag\": false, \"shortcutBarFlag\": true, \"memberReviewFlag\": false, \"ownerMentionAllFlag\": false, \"memberAppFlag\":true}";

    @Autowired
    private MsgFileMapper msgFileMapper;

    @Autowired
    private RemoteAvatarApi avatarService;


    @Override
    public ChatChannel selectChatChannelById(Long id) {
        return chatChannelMapper.selectChatChannelById(id);
    }


    @Override
    public AjaxResult getChannelInfo(Long id) {
        if (id == null) {
            return AjaxResult.error("获取频道信息失败，id不能为空");
        }
        ChatChannel channel = chatChannelMapper.selectChatChannelById(id);
        if (channel == null) {
            return AjaxResult.error("获取频道信息失败，频道不存在");
        }
        return AjaxResult.success(channel);
    }


    @Override
    public List<ChatChannel> selectChatChannelList(ChannelQueryParam chatChannel) {
        LambdaQueryWrapper<ChatChannel> queryWrapper = new LambdaQueryWrapper<>(ChatChannel.class);
        queryWrapper.select(ChatChannel::getId, ChatChannel::getName, ChatChannel::getType, ChatChannel::getIcon, ChatChannel::getLastMsgTime,
                ChatChannel::getTotalMsgCount, ChatChannel::getLastRootMsgTime, ChatChannel::getTotalRootMsgCount, ChatChannel::getExtData,
                ChatChannel::getRemark, ChatChannel::getCreateBy, ChatChannel::getCreateTime, ChatChannel::getUpdateTime,
                ChatChannel::getUpdateBy, ChatChannel::getDeleteTime);

        Boolean includeDirect = chatChannel.getIncludeDirect();
        if (includeDirect == null || !includeDirect) {
            queryWrapper.gt(ChatChannel::getType, CHANNEL_TYPE_DIRECT);
        }
        Boolean includeDeleted = chatChannel.getIncludeDeleted();

        if (includeDeleted == null || !includeDeleted) {
            queryWrapper.eq(ChatChannel::getDeleteTime, -1);
        }
        return chatChannelMapper.selectList(queryWrapper);
    }



    @Override
    public List<ChannelVo> selectUserChannelList(UserChannelQueryParam chatChannel) {

        Map<Long, String> userIdMap = new HashMap<>();
        Map<Long, String> botIdMap = new HashMap<>();
        List<ChannelVo> result;

        String token = SecurityUtils.getToken();
        LoginUser user = AuthUtil.getLoginUser(token);
        if (user == null) {
            throw new BaseException("查询用户频道失败：用户未登录或token已过期");
        }
        ChannelMember memberCondition = new ChannelMember();
        memberCondition.setUserId(user.getUserid());
        List<ChannelMember> members = channelMemberMapper.selectChannelMemberList(memberCondition);
        List<Long> channelIds = new ArrayList<>(100);
        List expectedIds = chatChannel.getChannelIds();
        Map<Long, ChannelMember> memberMap = new HashMap<>();

        members.stream().forEach(item -> {
            if (CollectionUtils.isEmpty(expectedIds) ||
                    (CollectionUtils.isNotEmpty(expectedIds) && expectedIds.contains(item.getChannelId()))) {
                channelIds.add(item.getChannelId());
                memberMap.put(item.getChannelId(), item);
            }
        });
        if (CollectionUtils.isEmpty(channelIds)) {
            return new ArrayList<>();
        }


        PageUtils.startPage();
        LambdaQueryWrapper<ChatChannel> queryWrapper = new LambdaQueryWrapper<>(ChatChannel.class);
        queryWrapper.select(ChatChannel::getId, ChatChannel::getName, ChatChannel::getType, ChatChannel::getIcon,
                ChatChannel::getLastMsgTime, ChatChannel::getTotalMsgCount, ChatChannel::getLastRootMsgTime,
                ChatChannel::getTotalRootMsgCount, ChatChannel::getRemark, ChatChannel::getCreateBy, ChatChannel::getExtData,
                ChatChannel::getCreateTime, ChatChannel::getUpdateTime, ChatChannel::getUpdateBy, ChatChannel::getDeleteTime);

        Boolean includeDirect = chatChannel.getIncludeDirect();
        if (includeDirect == null || !includeDirect) {
            queryWrapper.gt(ChatChannel::getType, CHANNEL_TYPE_DIRECT);
        }

        Boolean includeDeleted = chatChannel.getIncludeDeleted();
        if (includeDeleted == null || !includeDeleted) {
            queryWrapper.eq(ChatChannel::getDeleteTime, -1);
        }
        queryWrapper.in(ChatChannel::getId, channelIds);

        List<ChatChannel> channelList = chatChannelMapper.selectList(queryWrapper);


        if (CollectionUtils.isNotEmpty(channelList)) {
            result = new ArrayList<>(channelList.size());

            List<Long> channelIdList = channelList.stream().map(ChatChannel::getId).collect(Collectors.toList());

            List<Long> exceptMsgIds = new ArrayList<>();
            processMemberDeleteMsgId(exceptMsgIds, user.getUserid());

            QueryWrapper<Message> messageQueryWrapper = new QueryWrapper();
            messageQueryWrapper.select("max(id) id", "min(id) first_msg_id", "channel_id");
            LambdaQueryWrapper<Message> lambdaQueryWrapper = messageQueryWrapper.lambda().in(Message::getChannelId, channelIdList)
                    .eq(Message::getDeleteTime, -1).isNull(Message::getRootId).groupBy(Message::getChannelId);
            if (CollectionUtils.isNotEmpty(exceptMsgIds)) {
                lambdaQueryWrapper.notIn(Message::getId, exceptMsgIds);
            }

            List<MessageVo> messageList = null;
            List<Message> maxMessages = messageMapper.selectList(lambdaQueryWrapper);
            Set<Long> userIdSet = new HashSet<>();
            Map<Long, Long> minMsgIdMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(maxMessages)) {
                List<Long> maxMsgIds = new ArrayList<>();
                maxMessages.forEach(item -> {
                    maxMsgIds.add(item.getId());
                    minMsgIdMap.put(item.getChannelId(), item.getFirstMsgId());
                });

                messageQueryWrapper.clear();
                lambdaQueryWrapper = messageQueryWrapper.lambda().setEntityClass(Message.class)
                        .in(Message::getId, maxMsgIds);
                List<Message> messages = messageMapper.selectList(lambdaQueryWrapper);
                if (CollectionUtils.isNotEmpty(messages)) {
                    messageQueryService.getUserIdNameMap(messages, userIdMap, botIdMap);
                    messageList = messages.stream().map(item -> {
                        MessageVo messageVo = new MessageVo();
                        userIdSet.add(item.getUserId());
                        BeanUtils.copyProperties(item, messageVo);
                        messageVo.setProps(JSON.parseObject(item.getPropsStr(), MessageProp.class));
                        return messageVo;
                    }).collect(Collectors.toList());

                    processMessageFileInfo(messageList);
                }
            }

            Map<Long, List<MessageVo>> messageMap;
            if (CollectionUtils.isNotEmpty(messageList)) {
                processMessageDatas(messageList, userIdMap, user, botIdMap);
                messageMap = messageList.stream().collect(Collectors.groupingBy(Message::getChannelId));
            } else {
                messageMap = null;
            }
            List<ChannelVo> directChannelList = new ArrayList<>();

            channelList.forEach(item -> {
                ChannelVo channelVo = new ChannelVo();
                BeanUtils.copyBeanProp(channelVo, item);
                ChannelMember member = memberMap.get(item.getId());
                channelVo.setPinnedFlag(member.getPinnedFlag());
                channelVo.setDndFlag(member.getDndFlag());
                channelVo.setCollapse(member.getCollapse() == null ? COLLAPSE_FLAG_FALSE : member.getCollapse());
                channelVo.setDisplayName(member.getDisplayName());
                channelVo.setExt(JSON.parseObject(StringUtils.defaultString(channelVo.getExtData(), EXT_DEFAULT_VALUE), ChannelExtData.class));

                if (item.getType() == CHANNEL_TYPE_DIRECT) {
                    directChannelList.add(channelVo);
                }
                result.add(channelVo);
                if (MapUtils.isNotEmpty(messageMap)) {
                    List<MessageVo> messages = messageMap.get(item.getId());
                    if (CollectionUtils.isNotEmpty(messages)) {
                        MessageVo msg = messages.get(0);
                        msg.setProps(null);
                        channelVo.setLastMessage(msg);
                    }
                }

                if (MapUtils.isNotEmpty(minMsgIdMap)) {
                    Long firstMsgId = minMsgIdMap.get(item.getId());
                    MessageVo msg = new MessageVo();
                    msg.setId(firstMsgId);
                    channelVo.setFirstMessage(msg);
                }
            });
            msgTopService.processChannelTopMsg(channelIdList, result);
            channelIconService.processChannelIconForQuery(result, user);
            imCardService.processTopCard(channelIdList, result);
            processDirectChannelName(directChannelList, user);


        } else {
            result = new ArrayList<>();
        }

        return result;
    }


    public List<DirectChannelVo> selectUserDirectChannelList(UserChannelQueryParam chatChannel, LoginUser loginUser) {
        if (loginUser == null) {
            loginUser = SecurityUtils.getLoginUser();
        }
        List<DirectChannelVo> result = new ArrayList<>();
        if (loginUser == null) {
            return result;
        }
        Long userId = loginUser.getUserid();
        if (userId == null) {
            return result;
        }

        LambdaQueryWrapper<ChannelMember> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(ChannelMember::getChannelId, ChannelMember::getUserId, ChannelMember::getId);
        queryWrapper.eq(ChannelMember::getUserId, userId);
        List<ChannelMember> members = channelMemberMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(members)) {
            return result;
        }
        Set<Long> channelIds = members.stream().map(ChannelMember::getChannelId).collect(Collectors.toSet());

        if (CollectionUtils.isEmpty(channelIds)) {
            return result;
        }

        LambdaQueryWrapper<ChatChannel> channelQueryWrapper = new LambdaQueryWrapper<>();

        channelQueryWrapper.eq(ChatChannel::getType, CHANNEL_TYPE_DIRECT);
        channelQueryWrapper.in(ChatChannel::getId, channelIds);
        List<ChatChannel> channelList = chatChannelMapper.selectList(channelQueryWrapper);

        if (CollectionUtils.isEmpty(channelList)) {
            return result;
        }
        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ChannelMember::getChannelId, channelList.stream().map(ChatChannel::getId).collect(Collectors.toList()));
        List<ChannelMember> channelMembers = channelMemberMapper.selectList(queryWrapper);
        Map<Long, List<ChannelMember>> memberMap = new HashMap<>();
        channelMembers.forEach(item -> {
            List<ChannelMember> memberList = memberMap.computeIfAbsent(item.getChannelId(), key -> new ArrayList<>());
            memberList.add(item);
        });


        channelList.forEach(item -> {
            DirectChannelVo channelVo = new DirectChannelVo();
            BeanUtils.copyBeanProp(channelVo, item);
            channelVo.setMemberList(memberMap.get(item.getId()));
            result.add(channelVo);
        });
        return result;
    }

    private void processMemberDeleteMsgId(List<Long> exceptMsgIds, Long userId) {
        LambdaQueryWrapper<MemberDeleteMsg> delMsgQueryWrapper = new LambdaQueryWrapper<>(MemberDeleteMsg.class);
        delMsgQueryWrapper.eq(MemberDeleteMsg::getUserId, userId);
        List<MemberDeleteMsg> deleteMsgs = memberDeleteMsgMapper.selectList(delMsgQueryWrapper);
        if (CollectionUtils.isNotEmpty(deleteMsgs)) {
            deleteMsgs.forEach(item -> {
                exceptMsgIds.add(item.getMsgId());
            });
        }
    }


    @Override
    public void processDirectChannelName(List<ChannelVo> directChannelList, LoginUser user) {

        LambdaQueryWrapper<ChannelMember> memberQueryWrapper = new LambdaQueryWrapper<>();
        memberQueryWrapper.select(ChannelMember::getChannelId, ChannelMember::getUserId, ChannelMember::getRealName, ChannelMember::getId);
        List<Long> channelIds = directChannelList.stream().map(ChatChannel::getId).collect(Collectors.toList());
        List<ChannelMember> channelMembers = null;
        if (CollectionUtils.isNotEmpty(channelIds)) {
            memberQueryWrapper.in(ChannelMember::getChannelId, channelIds);
            memberQueryWrapper.ne(ChannelMember::getUserId, user.getUserid());
            channelMembers = channelMemberMapper.selectList(memberQueryWrapper);
        }
        Map<Long, ChannelMember> channelIdNameMap = new HashMap<>();
        List<ChannelVo> myChannels = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(channelMembers)) {
            channelMembers.forEach(item -> {
                channelIdNameMap.put(item.getChannelId(), item);
            });
        }

        directChannelList.forEach(channelVo -> {
            ChannelMember member = channelIdNameMap.get(channelVo.getId());
            String channelName = null;
            Long userId = null;
            if (member != null) {
                channelName = member.getRealName();
                userId = member.getUserId();
                channelVo.setDirectChannelName(channelName);
                channelVo.setDirectChannelIcon(avatarService.getUserAvatarByUserId(userId.toString(), INNER).getData());
            } else {
                userId = user.getUserid();
            }
            if (StringUtils.isEmpty(channelName)) {
                myChannels.add(channelVo);
            } else {
                channelVo.setName(channelName);
            }
            ChannelExtData ext = channelVo.getExt();
            if (ext == null) {
                ext = new ChannelExtData();
                channelVo.setExt(ext);
            }
            ext.setUserId(userId);
        });

        if (CollectionUtils.isNotEmpty(myChannels)) {
            memberQueryWrapper.clear();
            memberQueryWrapper.select(ChannelMember::getChannelId, ChannelMember::getUserId, ChannelMember::getRealName, ChannelMember::getId);
            memberQueryWrapper.in(ChannelMember::getChannelId, myChannels.stream().map(ChannelVo::getId).collect(Collectors.toList()));
            channelMembers = channelMemberMapper.selectList(memberQueryWrapper);
            if (CollectionUtils.isNotEmpty(channelMembers)) {
                Map<Long, List<ChannelMember>> memberMap = channelMembers.stream().collect(Collectors.groupingBy(ChannelMember::getChannelId));
                parseMyChannelName(myChannels, memberMap, user);
            }
        }
    }



    @Override
    public AjaxResult updateChannelRemark(ChatChannel chatChannel) {

        ChatChannel channel = chatChannelMapper.selectChatChannelById(chatChannel.getId());
        channel.setRemark(chatChannel.getRemark());
        channel.setUpdateBy(null);
        channel.setUpdateTime(null);
        chatChannelMapper.updateChatChannelRemark(channel);

        ChannelVo chatChannelVo = new ChannelVo();
        BeanUtils.copyBeanProp(chatChannelVo, channel);
        chatChannelVo.setExt(JSON.parseObject(StringUtils.defaultString(chatChannelVo.getExtData(), EXT_DEFAULT_VALUE), ChannelExtData.class));

        processEdit(chatChannelVo, null);

        return AjaxResult.success(channel);
    }


    @Override
    public AjaxResult editChannelConfig(ChannelVo channelVo) {
        ChatChannel channel = chatChannelMapper.selectChatChannelById(channelVo.getId());
        if (channel == null) {
            return AjaxResult.error("更新群组配置失败: 获取群组信息失败");
        }
        ChannelExtData extData = channelVo.getExt();
        String channelExtData = JSON.toJSONString(extData);

        LoginUser loginUser = SecurityUtils.getLoginUser();

        LambdaUpdateWrapper<ChatChannel> channelUpdateWrapper = new LambdaUpdateWrapper<>();
        channelUpdateWrapper.set(ChatChannel::getExtData, channelExtData);
        channelUpdateWrapper.set(ChatChannel::getUpdateBy, loginUser.getUserid());
        channelUpdateWrapper.set(ChatChannel::getUpdateTime, new Date());
        channelUpdateWrapper.eq(ChatChannel::getId, channelVo.getId());
        chatChannelMapper.update(null, channelUpdateWrapper);

        ChannelVo chatChannelVo = new ChannelVo();
        BeanUtils.copyBeanProp(chatChannelVo, channel);
        chatChannelVo.setExt(channelVo.getExt());

        processEdit(chatChannelVo, null);
        return AjaxResult.success();
    }

    private void parseMyChannelName(List<ChannelVo> myChannels, Map<Long, List<ChannelMember>> memberMap, LoginUser user) {
        myChannels.forEach(channelVo -> {
            List<ChannelMember> members = memberMap.get(channelVo.getId());
            List<ChannelMember> exceptMes = null;
            if (CollectionUtils.isNotEmpty(members)) {
                exceptMes = members.stream().filter(item -> !item.getUserId().equals(user.getUserid())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(exceptMes)) {
                    channelVo.setName("我 (" + members.get(0).getRealName() + ")");
                } else {
                    String channelName = parseMyChannelName(exceptMes);
                    channelVo.setName(channelName);
                }
            }
        });

    }

    private String parseMyChannelName(List<ChannelMember> exceptMes) {
        StringBuffer nameBuffer = new StringBuffer();
        nameBuffer.append(exceptMes.get(0).getRealName());
        if (exceptMes.size() > 1) {
            nameBuffer.append(", ").append(exceptMes.get(1).getRealName());
        }
        return nameBuffer.toString();
    }


    public ChannelVo selectUserChannel(Long userId, Long channelId) {

        LambdaQueryWrapper<ChatChannel> queryWrapper = new LambdaQueryWrapper<>(ChatChannel.class);
        queryWrapper.select(ChatChannel::getId, ChatChannel::getName, ChatChannel::getType, ChatChannel::getLastMsgTime,
                ChatChannel::getTotalMsgCount, ChatChannel::getLastRootMsgTime, ChatChannel::getTotalRootMsgCount,
                ChatChannel::getRemark, ChatChannel::getCreateBy, ChatChannel::getCreateTime, ChatChannel::getUpdateTime,
                ChatChannel::getUpdateBy, ChatChannel::getDeleteTime);

        Boolean includeDeleted = Boolean.FALSE;

        if (includeDeleted == null || !includeDeleted) {
            queryWrapper.eq(ChatChannel::getDeleteTime, -1);
        }
        queryWrapper.eq(ChatChannel::getId, channelId);

        ChatChannel channel = chatChannelMapper.selectOne(queryWrapper);
        ChannelVo channelVo = new ChannelVo();
        BeanUtils.copyBeanProp(channelVo, channel);


        ChannelMember memberCondition = new ChannelMember();
        memberCondition.setUserId(userId);
        memberCondition.setChannelId(channelId);
        List<ChannelMember> members = channelMemberMapper.selectChannelMemberList(memberCondition);
        if (CollectionUtils.isNotEmpty(members)) {
            ChannelMember member = members.get(0);
            channelVo.setPinnedFlag(member.getPinnedFlag());
            channelVo.setDndFlag(member.getDndFlag());
            channelVo.setCollapse(member.getCollapse() == null ? COLLAPSE_FLAG_FALSE : member.getCollapse());
            channelVo.setDisplayName(member.getDisplayName());
            channelVo.setExt(JSON.parseObject(StringUtils.defaultString(channelVo.getExtData(), EXT_DEFAULT_VALUE), ChannelExtData.class));
        }


        QueryWrapper<Message> messageQueryWrapper = new QueryWrapper();
        messageQueryWrapper.select("max(id) id", "min(id) first_msg_id", "channel_id");
        LambdaQueryWrapper<Message> lambdaQueryWrapper = messageQueryWrapper.lambda().eq(Message::getChannelId, channelId)
                .eq(Message::getDeleteTime, -1).groupBy(Message::getChannelId);
        List<Message> maxMessages = messageMapper.selectList(lambdaQueryWrapper);
        List<Long> firstMsgIds = new ArrayList<>();
        List<Long> maxMsgIds = new ArrayList<>();
        if (CollectionUtils.isEmpty(maxMessages)) {
            return channelVo;
        }

        firstMsgIds.addAll(maxMessages.stream().map(item -> {
            maxMsgIds.add(item.getId());
            return item.getFirstMsgId();
        }).collect(Collectors.toList()));
        messageQueryWrapper.clear();
        lambdaQueryWrapper = messageQueryWrapper.lambda().setEntityClass(Message.class)
                .in(Message::getId, maxMsgIds);
        List<Message> messages = messageMapper.selectList(lambdaQueryWrapper);
        Map<Long, String> userIdMap = new HashMap<>();
        Map<Long, String> botIdMap = new HashMap<>();

        if (CollectionUtils.isNotEmpty(messages)) {
            messageQueryService.getUserIdNameMap(messages, userIdMap, botIdMap);
            Message messageInDb = messages.get(0);
            MessageVo messageVo = new MessageVo();
            BeanUtils.copyProperties(messageInDb, messageVo);
            messageVo.setProps(JSON.parseObject(messageInDb.getPropsStr(), MessageProp.class));
            List<MessageVo> messageList = new ArrayList<>();
            messageList.add(messageVo);
            processMessageFileInfo(messageList);

            Long messageUserId = messageInDb.getUserId();
            List<Long> messageUserIds = new ArrayList<>();
            messageUserIds.add(messageUserId);
            messageUserIds.add(userId);
            Map<Long, String> userIdNameMap = getUserIdNameMap(messageUserIds, null);
            LoginUser user = new LoginUser();
            user.setUserid(userId);
            processMessageData(messageVo, userIdNameMap, user, botIdMap);
            messageVo.setProps(null);
            channelVo.setLastMessage(messageVo);

            MessageVo firstMsg = new MessageVo();
            firstMsg.setId(firstMsgIds.get(0));
            channelVo.setFirstMessage(firstMsg);

        }
        return channelVo;

    }


    private void processMessageData(MessageVo msg, Map<Long, String> userIdNameMap, LoginUser currentUser, Map<Long, String> botIdNameMap) {
        Long userId = msg.getUserId();
        if (MESSAGE_TYPE_USER.equals(msg.getType())) {
            if (userIdNameMap != null) {
                msg.setCreateUser(userIdNameMap.get(userId));
            }
        } else if (MESSAGE_TYPE_BOT.equals(msg.getType())) {
            if (botIdNameMap != null) {
                msg.setCreateUser(botIdNameMap.get(userId));
            }
        }

        String mentionUsers = msg.getMentionUsers();
        List<Long> mentUserIdList = JSON.parseArray(StringUtils.defaultString(mentionUsers, "[]"), Long.class);
        MessageMetadata messageMetadata = msg.getMetaData();
        if (messageMetadata == null) {
            messageMetadata = new MessageMetadata();
            msg.setMetaData(messageMetadata);
        }
        MetaStatusFlag statusFlag = MessageMetaUtil.getStatusFlag(messageMetadata);
        if (currentUser != null) {
            statusFlag.setMentionMe(mentUserIdList.contains(currentUser.getUserid()));
        } else {
            statusFlag.setMentionMe(Boolean.FALSE);
        }


        String propStr = msg.getPropsStr();
        messageMetadata.setMsgForwardData(new ArrayList<>());
        MessageProp prop = JSON.parseObject(propStr, MessageProp.class);
        if (prop.getForwardFlag() != null && prop.getForwardFlag()) {
            messageQueryService.parseForwardMessage(prop.getForwardMsgIds(), msg, null);
        }

        if ("card".equals(prop.getContentType())) {
            Map<String, Object> messageParams = prop.getMessageParams();
            Object cardId = messageParams.get("cardId");
            if (cardId instanceof Long) {
                MetaCardInfo metaCard = MessageMetaUtil.getMetaCard(messageMetadata);
                metaCard.setCardData(new CardData());
                metaCard.setCardInfo(prop.getMessageParams());
            }
        }

    }


    private void processMessageDatas(List<MessageVo> msgs, Map<Long, String> userIdNameMap, LoginUser currentUser, Map<Long, String> botIdNameMap) {


        Map<Long, List<Long>> msgIdForwardMap = new HashMap<>();
        msgs.forEach(msg -> {

            Long userId = msg.getUserId();
            if (MESSAGE_TYPE_USER.equals(msg.getType())) {
                if (userIdNameMap != null) {
                    msg.setCreateUser(userIdNameMap.get(userId));
                }
            } else if (MESSAGE_TYPE_BOT.equals(msg.getType())) {
                if (botIdNameMap != null) {
                    msg.setCreateUser(botIdNameMap.get(userId));
                }
            }

            String mentionUsers = msg.getMentionUsers();
            List<Long> mentUserIdList = JSON.parseArray(StringUtils.defaultString(mentionUsers, "[]"), Long.class);

            String propStr = msg.getPropsStr();
            MessageMetadata messageMetadata = msg.getMetaData();
            if (messageMetadata == null) {
                messageMetadata = new MessageMetadata();
                msg.setMetaData(messageMetadata);
            }
            MetaStatusFlag statusFlag = MessageMetaUtil.getStatusFlag(messageMetadata);
            if (currentUser != null) {
                statusFlag.setMentionMe(mentUserIdList.contains(currentUser.getUserid()));
            } else {
                statusFlag.setMentionMe(Boolean.FALSE);
            }

            MessageProp prop = JSON.parseObject(propStr, MessageProp.class);
            if (prop.getForwardFlag() != null && prop.getForwardFlag()) {
                if (CollectionUtils.isNotEmpty(prop.getForwardMsgIds())) {
                    msgIdForwardMap.put(msg.getId(), prop.getForwardMsgIds());
                }
            }
            if (prop.getSystemData() != null) {
                messageMetadata.setSystemData(prop.getSystemData());
            }


            if ("card".equals(prop.getContentType())) {
                Map<String, Object> messageParams = prop.getMessageParams();
                Object cardId = messageParams.get("cardId");
                if (cardId instanceof Long) {
                    MetaCardInfo card =  MessageMetaUtil.getMetaCard(messageMetadata);
                    card.setCardData(new CardData());
                    card.setCardInfo(prop.getMessageParams());
                }
            }

        });
        messageQueryService.parseForwardMessages(msgs, msgIdForwardMap, null);

    }


    private void processMessageFileInfo(List<MessageVo> msgList) {
        List<Long> msgIds = new ArrayList<>();
        msgList.forEach(item -> {
            msgIds.add(item.getId());
        });

        LambdaQueryWrapper<MsgFile> msgFileQueryWrapper = new LambdaQueryWrapper<>();
        msgFileQueryWrapper.select(MsgFile::getMsgId, MsgFile::getName, MsgFile::getId, MsgFile::getSize,
                MsgFile::getExtension, MsgFile::getHasPreviewImage, MsgFile::getWidth, MsgFile::getHeight, MsgFile::getMiniPreview);
        msgFileQueryWrapper.in(MsgFile::getMsgId, msgIds);
        msgFileQueryWrapper.eq(MsgFile::getDeleteTime, -1L);
        List<MsgFile> msgFileList = msgFileMapper.selectList(msgFileQueryWrapper);
        Map<Long, List<MsgFile>> msgFiles = msgFileList.stream().map(item -> {
            String name = item.getName();
            name = name + "." + item.getExtension();
            item.setName(name);
            return item;
        }).collect(Collectors.groupingBy(MsgFile::getMsgId));
        msgList.forEach(msg -> {
            msg.setFiles(msgFiles.get(msg.getId()));
        });
    }

    private Map<Long, String> getUserIdNameMap(List<Long> userIds, String token) {
        return userNickNameService.getNickNameByIds(userIds);
    }


    @Override
    public List<ChannelMember> selectUserMemberList(Long userId, Boolean includeDeleted) {

        if (userId == null) {
            log.warn("查询用户未读信息失败：用户Id为空");
            return new ArrayList<>();
        }
        ChannelMember condition = new ChannelMember();
        condition.setUserId(userId);
        if (includeDeleted != null) {
            condition.getParams().put("includeDeleted", includeDeleted);
        }
        List<ChannelMember> members = channelMemberMapper.selectChannelMemberList(condition);

        processTopicUnread(members, userId);

        return members;
    }

    private void processTopicUnread(List<ChannelMember> members, Long userId) {
        if (CollectionUtils.isEmpty(members)) {
            return;
        }
        List<Long> channelIds = members.stream().map(ChannelMember::getChannelId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(channelIds)) {
            return;
        }

        LambdaQueryWrapper<MsgReply> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(MsgReply::getChannelId, channelIds);
        List<MsgReply> msgReplies = msgReplyMapper.selectList(queryWrapper);

        if (CollectionUtils.isNotEmpty(msgReplies)) {
            List<Long> msgIds = new ArrayList<>();
            msgReplies.forEach(item -> {
                List<SysUser> users = JSON.parseArray(StringUtils.defaultString(item.getParticipants(), "[]"), SysUser.class);
                Optional<SysUser> userOpt = users.stream().filter(user -> user.getUserId().equals(userId)).findFirst();
                if (CollectionUtils.isNotEmpty(users) && !userOpt.isPresent()) {
                    msgIds.add(item.getId());
                }
            });

            if (CollectionUtils.isEmpty(msgIds)) {
                return;
            }

            MsgUnread unread = new MsgUnread();
            unread.setUserId(userId);
            unread.getParams().put("channelIds", channelIds);
            unread.getParams().put("rootIds", msgIds);
            List<MsgUnread> unreadList = msgUnreadMapper.selectUnreadTopicMsg(unread);
            Map<Long, Long> unreadMap = unreadList.stream().collect(Collectors.groupingBy(MsgUnread::getChannelId, Collectors.counting()));
            members.forEach(item -> {

                Long unreadCount = unreadMap.get(item.getChannelId());
                if (unreadCount != null) {
                    if (item.getUnreadCount() == null) {
                        item.setUnreadCount(0L);
                    } else {
                        item.setUnreadCount(item.getUnreadCount() - unreadCount);
                        if (item.getUnreadCount() < 0L) {
                            item.setUnreadCount(0L);
                        }
                    }
                }
            });
        }


    }

    @Override
    public List<ChatChannel> selectChatChannelListByDelete() {
        LambdaQueryWrapper<ChatChannel> queryWrapper = new LambdaQueryWrapper<>(ChatChannel.class);
        queryWrapper.ne(ChatChannel::getDeleteTime, -1);
        return chatChannelMapper.selectList(queryWrapper);
    }


    @Override
    public List<ChatChannel> getPinnedChannel(Long userId, Integer type) {
        ChatChannel param = new ChatChannel();
        param.setType(type);
        param.getParams().put("userId", userId);
        param.getParams().put("pinnedFlag", PINNED_FLAG_TRUE);
        return chatChannelMapper.selectChannelByUserId(param);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ChatChannel insertChatChannel(ChannelAddParam chatChannel) {
        LoginUser user = chatChannel.getLoginUser();
        if (user == null) {
            user = SecurityUtils.getLoginUser();
        }

        checkMembersForAdd(chatChannel, user);


        doCheckChannelType(chatChannel);

        if (CHANNEL_TYPE_DIRECT.equals(chatChannel.getType())) {

            ChatChannel existChannel = checkRepeatChannel(chatChannel, user);
            if (existChannel != null) {
                return existChannel;
            }
        }

        chatChannel.setId(SnowflakeIdUtil.nextId());


        ChatChannel channel = new ChatChannel();
        channel.setId(chatChannel.getId());
        channel.setName(chatChannel.getName());
        channel.setType(chatChannel.getType());
        channel.setRemark(chatChannel.getRemark());
        channel.setExtData(EXT_DEFAULT_VALUE);

        chatChannelMapper.insertChatChannel(channel);


        List<ChannelMember> members = processChannelMember(channel, chatChannel, user);

        Long businessId = chatChannel.getBusinessId();


        processNotify(channel, chatChannel.getChannelMembers(), user, businessId);


        channelIconService.processChannelIcon(chatChannel, members);

        return channel;
    }


    private void checkMembersForAdd(ChannelAddParam chatChannel, LoginUser user) {

        List<Long> memberIds = chatChannel.getChannelMembers();
        List<ChannelMember> members = chatChannel.getMembers();

        Boolean memberIdIsEmpty = CollectionUtils.isEmpty(memberIds);
        Boolean membersEmpty = CollectionUtils.isEmpty(members);

        if (memberIdIsEmpty && membersEmpty) {
            throw new BaseException("添加成员失败，成员列表不能为空");
        }

        Long userId = user.getUserid();


        if ((CollectionUtils.isNotEmpty(memberIds))) {
            if (memberIds.size() == 1) {
                if (CHANNEL_TYPE_DIRECT.equals(chatChannel.getType()) || !memberIds.contains(userId)) {
                    memberIds.add(userId);
                }
            }
        }
        if ((CollectionUtils.isNotEmpty(members) && members.size() == 1)) {
            if (CHANNEL_TYPE_DIRECT.equals(chatChannel.getType()) || !memberIds.contains(userId)) {
                ChannelMember member = new ChannelMember();
                member.setMemberType(MEMBER_TYPE_USER);
                member.setUserId(userId);
                members.add(member);
            }
        }
    }


    private void processNotify(ChatChannel channel, List<Long> memberIds, LoginUser user, Long businessId) {
        ChannelVo channelVo = selectUserChannel(memberIds.get(0), channel.getId());
        ChannelCreatedEvent channelCreatedEvent = new ChannelCreatedEvent();
        channelVo.setMsgTops(new ArrayList<>());
        channelCreatedEvent.setData(channelVo);

        if (businessId != null) {
            List<Long> channelIdList = new ArrayList<>();
            channelIdList.add(channel.getId());
            List<ChannelVo> channelVoList = new ArrayList<>();
            channelVoList.add(channelVo);
            imCardService.processTopCard(channelIdList, channelVoList);
        }

        if (channel.getType().equals(CHANNEL_TYPE_DIRECT)) {
            List<ChannelVo> channelVos = new ArrayList<>();
            channelVos.add(channelVo);
            String directChannelName = buildDirectChannelName(memberIds, user, channelVo.getName());
            channelVo.setDirectChannelName(directChannelName);
            List<Long> exceptMe = memberIds.stream().filter(id -> !id.equals(user.getUserid())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(exceptMe)) {
                channelVo.setDirectChannelIcon(avatarService.getUserAvatarByUserId(exceptMe.get(0).toString(), INNER).getData());
            }
            channelVo.setName(userNickNameService.getNickName(user.getUserid()));
            channelVo.setIcon(avatarService.getUserAvatarByUserId(user.getUserid().toString(), INNER).getData());
        }

        ChatBroadcast<Long, Long, ChannelId> broadcast = new ChatBroadcast<>();
        broadcast.setOmitUsers(new HashMap<>());
        broadcast.setUserIds(memberIds);
        channelCreatedEvent.setBroadcast(broadcast);

        chatEventService.doProcessEvent(channelCreatedEvent);
    }

    private String buildDirectChannelName(List<Long> memberIds, LoginUser user, String name) {
        List<Long> exceptMe = memberIds.stream().filter(id -> !id.equals(user.getUserid())).collect(Collectors.toList());
        String result = null;
        if (CollectionUtils.isNotEmpty(exceptMe)) {
            Long userId = exceptMe.get(0);
            if (userId != null) {
                result = userNickNameService.getNickName(userId);
            }
        }
        if (StringUtils.isEmpty(result)) {
            result = name;
        }
        return result;
    }


    private void processEdit(ChannelVo channel, List<Long> memberIds) {
        if (channel.getMsgTops() == null) {
            channel.setMsgTops(new ArrayList<>());
        }
        ChannelUpdateEvent channelUpdateEvent = new ChannelUpdateEvent();
        channelUpdateEvent.setData(channel);

        ChatBroadcast<Long, Long, ChannelId> broadcast = new ChatBroadcast<>();
        broadcast.setChannelId(channel.getId());
        broadcast.setOmitUsers(new HashMap<>());
        channelUpdateEvent.setBroadcast(broadcast);

        chatEventService.doProcessEvent(channelUpdateEvent);
    }


    private void processDelete(ChatChannel channel) {

        ChannelDeleteEvent channelDeleteEvent = new ChannelDeleteEvent();
        channelDeleteEvent.setData(channel);

        ChatBroadcast<Long, Long, ChannelId> broadcast = new ChatBroadcast<>();
        broadcast.setChannelId(channel.getId());
        broadcast.setOmitUsers(new HashMap<>());
        channelDeleteEvent.setBroadcast(broadcast);

        chatEventService.doProcessEvent(channelDeleteEvent);
    }


    private void processRestore(ChatChannel channel, List<Long> memberIds) {

        ChannelRestoreEvent channelRestoreEvent = new ChannelRestoreEvent();
        channelRestoreEvent.setData(channel);

        ChatBroadcast<Long, Long, ChannelId> broadcast = new ChatBroadcast<>();
        broadcast.setChannelId(channel.getId());
        broadcast.setOmitUsers(new HashMap<>());
        channelRestoreEvent.setBroadcast(broadcast);

        chatEventService.doProcessEvent(channelRestoreEvent);
    }


    private List<ChannelMember> processChannelMember(ChatChannel channel, ChannelAddParam addParam, LoginUser user) {

        List<Long> memberIds = addParam.getChannelMembers();
        List<ChannelMember> memberDatas = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(addParam.getMembers())) {
            memberDatas.addAll(addParam.getMembers());
        }

        if (memberIds == null) {
            memberIds = new ArrayList<>();
        }

        if (CollectionUtils.isEmpty(memberDatas)) {
            if (CollectionUtils.isNotEmpty(memberIds)) {
                memberIds.forEach(item -> {
                    ChannelMember member = new ChannelMember();
                    member.setUserId(item);
                    member.setMemberType(MEMBER_TYPE_USER);
                    memberDatas.add(member);
                });
            }
        } else {
            List<Long> finalMemberIds = memberIds;
            memberDatas.forEach(item -> {
                if (MEMBER_TYPE_USER.equals(item.getMemberType()) && !finalMemberIds.contains(item.getId())) {
                    finalMemberIds.add(item.getId());
                }
            });
        }
        addParam.setChannelMembers(memberIds);

        List<ChannelMember> members = new ArrayList<>();

        Map<Long, String> userNameMap = getUserIdNameMap(memberDatas.stream().map(ChannelMember::getUserId).collect(Collectors.toList()), null);

        memberDatas.forEach(item -> {
            Long userId = item.getUserId();
            if (userId != null) {
                String userName = item.getRealName();

                if (MEMBER_TYPE_USER.equals(item.getMemberType())) {
                    String realName = userNameMap.get(userId);
                    if (realName == null) {
                        log.error("创建群组失败：成员【{}】不存在", userId);
                        throw new BaseException("创建群组失败，成员不存在");
                    }

                    if (userName == null) {
                        userName = realName;
                    }
                }
                ChannelMember member = new ChannelMember();
                member.setChannelId(channel.getId());
                member.setUserId(userId);
                member.setManager(userId.equals(user.getUserid()) ? 1 : 3);
                member.setRealName(userName);
                member.setMemberType(item.getMemberType());
                member.setMentionCount(0L);
                member.setMsgCount(0L);
                member.setUnreadCount(0L);
                member.setRootMsgCount(0L);
                member.setMentionRootCount(0L);
                member.setUrgentMentionCount(0L);
                member.setId(SnowflakeIdUtil.nextId());
                member.setPinnedFlag(PINNED_FLAG_FALSE);
                member.setDndFlag(DND_FLAG_FALSE);
                member.setCollapse(COLLAPSE_FLAG_FALSE);
                members.add(member);
            } else {
                log.error("userId is null:{}", JSON.toJSONString(item));
            }

        });
        channelMemberMapper.batchInsert(members);
        return members;
    }

    private void doCheckChannelType(ChannelAddParam chatChannel) {

        Integer type = chatChannel.getType();
        if (type == null) {
            throw new BaseException("添加频道失败，频道类型不能为空");
        }
        if (CHANNEL_TYPE_DIRECT.equals(type)) {
            chatChannel.setName(null);

        }
    }


    private ChatChannel checkRepeatChannel(ChannelAddParam chatChannel, LoginUser user) {
        List<ChannelMember> members = chatChannel.getMembers();
        List<Long> memberIds = chatChannel.getChannelMembers();
        Long userId = null;
        if (CollectionUtils.isNotEmpty(memberIds)) {
            Optional<Long> userIdOptional = memberIds.stream().filter(item -> !item.equals(user.getUserid())).findFirst();
            if (userIdOptional.isPresent()) {
                userId = userIdOptional.get();
            }
        } else if (CollectionUtils.isNotEmpty(members)) {
            Optional<Long> userIdOptional = members.stream().map(ChannelMember::getUserId).filter(item -> !item.equals(user.getUserid())).findFirst();
            if (userIdOptional.isPresent()) {
                userId = userIdOptional.get();
            }
        }
        if (userId == null) {
            userId = user.getUserid();
        }
        List<ChannelMember> memberChannelIds = channelMemberMapper.selectDirectChannelIdByUserId(user.getUserid());
        List<Long> channelIds = memberChannelIds.stream().map(ChannelMember::getChannelId).collect(Collectors.toList());
        ChatChannel channel = null;
        if (CollectionUtils.isEmpty(channelIds)) {
            return channel;
        }
        List<ChannelMember> existMembers = channelMemberMapper.selectDirectChannelMembers(channelIds);
        Long finalUserId = userId;
        List<ChannelMember> filterdMembers = existMembers.stream().filter(item -> item.getUserId().equals(finalUserId)).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(filterdMembers)) {
            Long channelId = null;

            if (userId.equals(user.getUserid())) {
                Map<Long, List<ChannelMember>> channelMap = filterdMembers.stream().collect(Collectors.groupingBy(ChannelMember::getChannelId));
                for (Long id : channelIds) {
                    List<ChannelMember> membersList = channelMap.get(id);
                    if (CollectionUtils.isNotEmpty(membersList) && membersList.size() == 2) {
                        channelId = id;
                        break;
                    }
                }
            } else {
                channelId = filterdMembers.get(0).getChannelId();
            }
            if (channelId != null) {
                channel = chatChannelMapper.selectChatChannelById(channelId);
            }
            if (channel != null && channel.getDeleteTime() > -1) {
                LambdaUpdateWrapper<ChatChannel> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.set(ChatChannel::getDeleteBy, null);
                updateWrapper.set(ChatChannel::getDeleteTime, -1L);
                updateWrapper.set(ChatChannel::getUpdateBy, user.getUserid());
                updateWrapper.set(ChatChannel::getUpdateTime, new Date());
                updateWrapper.eq(ChatChannel::getId, channelId);
                chatChannelMapper.update(null, updateWrapper);
                channel.setDeleteTime(-1L);
                channel.setDeleteBy(null);
            }
        }
        return channel;
    }


    @Override
    public AjaxResult updateChatChannel(ChatChannel chatChannel) {

        ChatChannel channel = chatChannelMapper.selectChatChannelById(chatChannel.getId());
        if (channel == null) {
            String msg = "更新频道失败，频道不存在";
            return AjaxResult.error(msg);
        }

        ChatChannel updateParam = new ChatChannel();
        updateParam.setId(chatChannel.getId());
        updateParam.setName(chatChannel.getName());
        updateParam.setRemark(chatChannel.getRemark());
        int effectCount = chatChannelMapper.updateChatChannel(updateParam);
        if (effectCount > 0) {

            ChannelVo chatChannelVo = new ChannelVo();
            BeanUtils.copyBeanProp(chatChannelVo, channel);
            chatChannelVo.setName(chatChannel.getName());
            chatChannelVo.setRemark(chatChannel.getRemark());
            chatChannelVo.setExt(JSON.parseObject(StringUtils.defaultString(chatChannelVo.getExtData(), EXT_DEFAULT_VALUE), ChannelExtData.class));

            processEdit(chatChannelVo, new ArrayList<>());
        } else {
            log.warn("频道信息未变更， channelId:{}", channel.getId());
        }
        return AjaxResult.success();
    }


    @Override
    public int deleteChatChannelByIds(Long[] ids) {
        return chatChannelMapper.deleteChatChannelByIds(ids);
    }


    @Override
    public int deleteChatChannelById(Long id) {
        return chatChannelMapper.deleteChatChannelById(id);
    }


    @Override
    public AjaxResult deleteChatChannel(Long id) {

        ChatChannel channel = chatChannelMapper.selectChatChannelById(id);
        if (channel == null) {
            String msg = "删除频道失败，频道不存";
            return AjaxResult.error(msg);
        }

        if (channel.getDeleteTime() > -1) {
            log.warn("频道【{}】已归档，无需再次归档；频道信息：{}", channel.getName(), JSON.toJSONString(channel));
            return AjaxResult.success();
        }

        ChatChannel updateParam = new ChatChannel();
        updateParam.setId(id);
        updateParam.setDeleteBy(SecurityUtils.getUserId());
        updateParam.setDeleteTime(System.currentTimeMillis());
        channel.setDeleteBy(SecurityUtils.getUserId());
        channel.setDeleteTime(System.currentTimeMillis());

        chatChannelMapper.updateChatChannel(updateParam);
        processDelete(channel);
        return AjaxResult.success();
    }


    @Override
    public AjaxResult<ChatChannel> restoreChatChannel(Long id) {
        ChatChannel channel = chatChannelMapper.selectChatChannelById(id);
        if (channel == null) {
            String msg = "恢复频道失败，频道不存在";
            return AjaxResult.error(msg);
        }
        if (channel.getDeleteTime().equals(-1L)) {
            log.warn("频道【{}】正常，无法恢复；频道信息：{}", channel.getName(), JSON.toJSONString(channel));
            return AjaxResult.success();
        }

        ChatChannel updateParam = new ChatChannel();
        updateParam.setId(id);
        updateParam.setDeleteTime(-1L);
        channel.setDeleteTime(-1L);
        updateParam.setDeleteBy(null);
        channel.setDeleteBy(null);

        LambdaUpdateWrapper<ChatChannel> updateWrapper = new LambdaUpdateWrapper<>(updateParam);
        updateWrapper.eq(ChatChannel::getId, id);
        int effectCount = chatChannelMapper.update(updateParam, updateWrapper);
        if (effectCount < 1) {
            log.warn("频道【{}】信息未变更：{}", channel.getName(), JSON.toJSONString(channel));
            return AjaxResult.success();
        }

        processRestore(channel, new ArrayList<>());
        return AjaxResult.success(channel);
    }

    @Override
    public void createFolder(Long channelId) {
        throw new BaseException("暂不支持");
    }

    @Autowired
    public void setChatChannelMapper(ChatChannelMapper chatChannelMapper) {
        this.chatChannelMapper = chatChannelMapper;
    }

    @Autowired
    public void setChannelMemberMapper(ChannelMemberMapper channelMemberMapper) {
        this.channelMemberMapper = channelMemberMapper;
    }

    @Autowired
    public void setChatEventService(ChatEventService chatEventService) {
        this.chatEventService = chatEventService;
    }


    @Autowired
    public void setMessageMapper(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    @Autowired
    public void setUserNickNameService(UserNickNameService userNickNameService) {
        this.userNickNameService = userNickNameService;
    }

    @Autowired
    public void setChannelIconService(ChannelIconService channelIconService) {
        this.channelIconService = channelIconService;
    }

    @Autowired
    public void setMessageQueryService(MessageQueryService messageQueryService) {
        this.messageQueryService = messageQueryService;
    }

}
