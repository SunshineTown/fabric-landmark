package net.xingmot.landmark.config;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Map;

public final class Landmark{
    public Map<String, String> dimensions_name = Maps.newHashMap(ImmutableMap.of(
            "overworld", "§2主世界§f", "the_nether", "§c地狱§f", "the_end", "§d末地§f"));
    public Map<String, String> dimensions_name2 = Maps.newHashMap(ImmutableMap.of(
            "overworld", "主世界", "the_nether", "地狱", "the_end", "末地"));
    public String listTips="-----当前是第§b %d §f页-----";    //暂时未加入
    public String landmarkCreateTips="成功创建地标：";
    public String landmarkSaveTips="成功保存地标！";
    public String landmarkDel="成功删除地标：";
    public String landmarkColorTips="颜色修改成功！效果：";
    public String landmarkRenameTips="重命名成功！效果：";
    public String landmarkNull ="地标id输入错误或id不存在！";
    public String landmarkListNull ="--- 地标列表为空 ---\n";
    public String landmarkLog="成功添加地标信息！";         //暂时未加入
}
