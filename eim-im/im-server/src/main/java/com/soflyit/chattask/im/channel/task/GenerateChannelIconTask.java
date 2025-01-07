package com.soflyit.chattask.im.channel.task;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.soflyit.chattask.im.channel.domain.entity.ChannelMember;
import com.soflyit.chattask.im.channel.domain.entity.ChatChannel;
import com.soflyit.chattask.im.channel.domain.param.ChannelAddParam;
import com.soflyit.chattask.im.common.web.ByteArrayMultipartFile;
import com.soflyit.chattask.im.config.SoflyImFileConfig;
import com.soflyit.common.core.constant.HttpStatus;
import com.soflyit.common.core.domain.R;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.system.api.RemoteAvatarApi;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.filters.Watermark;
import net.coobird.thumbnailator.geometry.Coordinate;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.soflyit.common.core.constant.SecurityConstants.INNER;

/**
 * 生成频道图标任务<br>
 * 详细说明
 *
 * @author Toney
 * @date 2024-02-18 16:23
 */
@Slf4j
public class GenerateChannelIconTask implements Runnable {

    private final ChatChannel chatChannel;

    private final List<ChannelMember> channelMembers;

    private final RemoteAvatarApi remoteAvatarApi;

    private final SoflyImFileConfig soflyImfileConfig;

    private final Consumer callBack;

    public GenerateChannelIconTask(ChatChannel chatChannel, List<ChannelMember> channelMembers, RemoteAvatarApi remoteAvatarApi, SoflyImFileConfig soflyImfileConfig, Consumer<ChatChannel> callBack) {
        this.chatChannel = chatChannel;
        this.channelMembers = channelMembers;
        this.remoteAvatarApi = remoteAvatarApi;
        this.soflyImfileConfig = soflyImfileConfig;
        this.callBack = callBack;
    }

    @Override
    public void run() {
        if (chatChannel instanceof ChannelAddParam) {
            processDiscussionChannelIcon((ChannelAddParam) chatChannel);
        } else {
            processDiscussionChannelIcon(chatChannel, channelMembers);
        }
        if (callBack != null) {
            callBack.accept(chatChannel);
        }
    }


    private void processDiscussionChannelIcon(ChannelAddParam channelData) {
        log.debug("开始生成logo(add params):{}", channelData.getId());
        List<Long> members = channelData.getChannelMembers();
        if (members == null) {
            members = new ArrayList<>();
        }
        if (CollectionUtils.isEmpty(members)) {
            if (CollectionUtils.isNotEmpty(channelMembers)) {
                List<Long> finalMembers = members;
                channelMembers.forEach(member -> {
                    finalMembers.add(member.getUserId());
                });
            }
        }

        List<Long> userIds = new ArrayList<>();
        Long member = null;
        for (int i = 0; i < members.size(); i++) {
            member = members.get(i);
            if (member != null) {
                userIds.add(member);
            }
            if (userIds.size() >= 9) {
                break;
            }
        }
        log.debug("生成频道logo，检查成员：{}", JSON.toJSONString(userIds));
        if (CollectionUtils.isNotEmpty(userIds)) {
            R<Map<Long, String>> userAvatarResult = remoteAvatarApi.getUserAvatarByUserIds(userIds, INNER);
            log.debug("生成频道logo，获取成员头像：{}", JSON.toJSONString(userAvatarResult));
            if (userAvatarResult != null && userAvatarResult.getCode() == HttpStatus.SUCCESS) {
                generatorChannelIcon(channelData, userAvatarResult.getData().values());
            }
        }

    }

    private void processDiscussionChannelIcon(ChatChannel channel, List<ChannelMember> channelMembers) {
        log.debug("开始生成logo(channel params):{}", channel.getId());

        List<Long> members = channelMembers.stream().map(ChannelMember::getUserId).collect(Collectors.toList());
        List<Long> userIds = new ArrayList<>();
        Long member = null;
        for (int i = 0; i < members.size(); i++) {
            member = members.get(i);
            if (member != null) {
                userIds.add(member);
            }
            if (userIds.size() >= 9) {
                break;
            }
        }
        log.debug("生成频道logo，检查成员：{}", JSON.toJSONString(userIds));
        if (CollectionUtils.isNotEmpty(userIds)) {
            R<Map<Long, String>> userAvatarResult = remoteAvatarApi.getUserAvatarByUserIds(userIds, INNER);
            log.debug("生成频道logo，获取成员头像：{}", JSON.toJSONString(userAvatarResult));
            if (userAvatarResult != null && userAvatarResult.getCode() == HttpStatus.SUCCESS) {
                generatorChannelIcon(channel, userAvatarResult.getData().values());
            }
        }
    }


    private void generatorChannelIcon(ChatChannel chatChannel, Collection<String> userAvatars) {

        List<String> avatars = new ArrayList<>(userAvatars);
        String iconData = generateIcon(avatars, chatChannel.getId());
        log.debug("生成频道logo成功:{}", iconData);
        chatChannel.setIcon(iconData);
    }


    private String generateIcon(List<String> avatars, Long id) {

        if (CollectionUtils.isEmpty(avatars)) {
            return null;
        }
        List<File> avatarFiles = new ArrayList<>();
        File templateFile = new File(soflyImfileConfig.getMsgFileTmpDir(), "channel-icon");
        if (FileUtil.exist(templateFile)) {
            templateFile.mkdirs();
        }

        avatars.forEach(avatarPath -> {
            String filename = avatarPath;
            if (avatarPath.indexOf("?") > 0) {
                filename = avatarPath.substring(0, avatarPath.indexOf("?"));
            }
            String type = FileNameUtil.getSuffix(filename);
            if (StringUtils.isNotEmpty(type) && type.indexOf("?") > -1) {
                type = type.substring(0, type.indexOf("?"));
            }
            String fileName = UUID.fastUUID().toString(Boolean.TRUE);
            File avatarFile = new File(templateFile, fileName + "." + type);
            HttpUtil.downloadFile(avatarPath, avatarFile);

            if (type.equals("svg")) {
                avatarFile = processSvgFile(avatarFile, fileName);
            }
            avatarFiles.add(avatarFile);
        });

        String resultName = id == null ? UUID.fastUUID().toString(Boolean.TRUE) : String.valueOf(id);

        File resultFile = new File(templateFile, resultName + "-result.png");
        mergeAvatar(avatarFiles, resultFile);
        MultipartFile multipartFile = new ByteArrayMultipartFile("file", resultFile.getName(), "image/png", FileUtil.readBytes(resultFile));
        R<String> uploadResult = remoteAvatarApi.uploadIcon(multipartFile);
        if (uploadResult != null && uploadResult.getCode() == R.SUCCESS) {
            FileUtil.del(resultFile);
            return uploadResult.getData();
        } else {
            log.error("上传频道【{}】logo失败：{}", id, uploadResult.getMsg());
        }
        return null;
    }


    private void mergeAvatar(List<File> avatarFiles, File resultFile) {
        int count = avatarFiles.size();
        try {
            resultFile.createNewFile();
            if (count < 5) {
                mergeAvatar(avatarFiles, resultFile, 60, 60);
            } else {
                mergeAvatar(avatarFiles, resultFile, 40, 40);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void mergeAvatar(List<File> avatarFiles, File resultFile, int width, int height) throws IOException {
        int margin = ((120 / width) + 1) * 5;
        int newImageWidth = 120 + margin;
        int newImageHeight = 120 + margin;
        BufferedImage image = new BufferedImage(newImageWidth, newImageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        image = g.getDeviceConfiguration().createCompatibleImage(newImageWidth, newImageHeight, Transparency.TRANSLUCENT);
        int count = avatarFiles.size();
        Thumbnails.Builder<BufferedImage> builder = Thumbnails.of(image);
        for (int i = 0; i < count; i++) {
            File avatarFile = avatarFiles.get(i);
            Watermark watermark = buildWatermark(avatarFile, i, count, width, height);
            builder.watermark(watermark);
            FileUtil.del(avatarFile);
        }

        builder.size(newImageWidth, newImageHeight).outputFormat("png").toFile(resultFile);

    }

    private Watermark buildWatermark(File file, int i, int count, int width, int height) {
        Watermark watermark = null;
        try {
            BufferedImage bufferedImage = Thumbnails.of(file).scale(1).asBufferedImage();
            int imageWidth = bufferedImage.getWidth();
            int imageHeight = bufferedImage.getHeight();
            int targetWidth = width;
            int targetHeight = height;
            if (imageHeight / (double) imageWidth > height / (double) width) {
                targetHeight = (int) (targetWidth * (imageHeight / (double) imageWidth));
            } else {
                targetWidth = (int) (targetHeight * (imageWidth / (double) imageHeight));
            }
            bufferedImage = Thumbnails.of(bufferedImage).size(targetWidth, targetHeight).asBufferedImage();
            bufferedImage = Thumbnails.of(bufferedImage).sourceRegion(Positions.CENTER, width, height).size(width, height).asBufferedImage();
            int top = calcTop(i + 1, count, height);
            int left = calcLeft(i + 1, count, width);
            Position position = new Coordinate(left, top);
            watermark = new Watermark(position, bufferedImage, 1f, 5);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return watermark;
    }

    private int calcTop(int i, int count, int height) {
        int result = 0;
        if (count < 3) {
            result = (120 - height) / 2;
        } else if (count == 5) {
            int offset = (120 - (height * 2)) / 2 + 5;
            if (i < 3) {
                result = offset;
            } else if (i > 2) {
                result = height + 5 + offset;
            }
        } else if (count == 6) {
            int offset = (120 - (height * 2)) / 2 + 5;
            if (i < 4) {
                result = offset;
            } else if ((i > 3)) {
                result = height + 5 + offset;
            }
        } else if (count < 7) {
            if (i == 1 || (i == 2 && count > 3) || (i == 3 && count > 5)) {
                result = 0;
            } else if ((i == 2 && count == 3) || (i == 3 && count < 6) || (i > 3)) {
                result = height + 5;
            }
        } else {
            if (i == 1 || (i == 2 && count > 7) || (i == 3 && count == 9)) {
                result = 0;
            } else if ((i == 2 && count == 7) || (i == 3 && count < 9) || (i == 4) || (i == 5 && count > 7) || (i == 6 && count == 9)) {
                result = 5 + height;
            } else if ((i == 5 && count == 7) || (i == 6 && count < 9) || (i > 6)) {
                result = 10 + height * 2;
            }
        }
        return result;
    }

    private int calcLeft(int i, int count, int width) {
        int result = 0;
        if (i == 1) {
            if (count == 1 || count == 3 || count == 7) {
                result = (120 - width) / 2;
            } else if (count == 2 || count == 4 || count == 6 || count == 9) {
                result = 0;
            } else if (count == 5 || count == 8) {
                result = (120 - width * 2) / 2;
            }
        } else if (i == 2) {
            if (count == 2 || count == 4 || count == 6 || count == 9) {
                result = width + 5;
            } else if (count == 3 || count == 7) {
                result = 0;
            } else if (count == 5 || count == 8) {
                result = (120 - 2 * width) / 2 + width + 5;
            }
        } else if (i == 3) {
            if (count == 3 || count == 7) {
                result = width + 5;
            } else if (count == 4 || count == 5 || count == 8) {
                result = 0;
            } else if (count == 6 || count == 9) {
                result = (width + 5) * 2;
            }
        } else if (i == 4) {
            if (count == 4 || count == 5 || count == 8) {
                result = width + 5;
            } else if (count == 6 || count == 9) {
                width = 0;
            } else if (count == 7) {
                result = (width + 5) * 2;
            }
        } else if (i == 5) {
            if (count == 5 || count == 8) {
                result = (width + 5) * 2;
            } else if (count == 6 || count == 9) {
                result = width + 5;
            } else if (count == 7) {
                result = 0;
            }
        } else if (i == 6) {
            if (count == 6 || count == 9) {
                result = (width + 5) * 2;
            } else if (count == 7) {
                result = width + 5;
            } else if (count == 8) {
                result = 0;
            }
        } else if (i == 7) {
            if (count == 7) {
                result = (width + 5) * 2;
            } else if (count == 8) {
                result = width + 5;
            } else {
                result = 0;
            }
        } else if (i == 8) {
            if (count == 8) {
                result = (width + 5) * 2;
            } else {
                result = width + 5;
            }
        } else {
            result = (width + 5) * 2;
        }
        return result;
    }

    private File processSvgFile(File avatarFile, String fileName) {

        File pngFile = new File(avatarFile.getParentFile(), fileName + ".png");
        if (!FileUtil.exist(pngFile)) {
            try {
                pngFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try (FileOutputStream fos = new FileOutputStream(pngFile)) {
            byte[] bytes = FileUtil.readBytes(avatarFile);
            PNGTranscoder t = new PNGTranscoder();
            TranscoderInput input = new TranscoderInput(new ByteArrayInputStream(bytes));
            TranscoderOutput output = new TranscoderOutput(fos);

            t.addTranscodingHint(ImageTranscoder.KEY_WIDTH, new Float(120));
            t.addTranscodingHint(ImageTranscoder.KEY_HEIGHT, new Float(120));
            t.transcode(input, output);
            fos.flush();
            FileUtil.del(avatarFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TranscoderException e) {
            throw new RuntimeException(e);
        }
        return pngFile;
    }

}
