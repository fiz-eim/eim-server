package com.soflyit.chattask.im.channel.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class BanConfig {


    private List<Long> blacklist;


    private Boolean banAllFlag;


    private List<Long> whitelist;


}
