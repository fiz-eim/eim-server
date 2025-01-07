package com.soflyit.chattask.im.message.domain.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TextTagData {

    private List<TextTag> tags;

    @Data
    public static class TextTag {

        private String tag;

        private String color;

        private Integer count;

        private List<TagDetail> details;

    }

    @Data
    public static class TagDetail {

        private Long userId;

        private String tagUser;

        private Date tagTime;

    }
}
