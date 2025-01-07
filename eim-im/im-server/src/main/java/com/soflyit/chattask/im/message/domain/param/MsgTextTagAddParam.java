package com.soflyit.chattask.im.message.domain.param;

import com.soflyit.chattask.im.message.domain.entity.MsgTextTag;
import lombok.Data;

@Data
public class MsgTextTagAddParam extends MsgTextTag {

    private String tag;

    private String color;
}
