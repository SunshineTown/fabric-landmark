package net.xingmot.landmark.config;

public final class Jsons{
    public String landmarkCreate=
            "{\"text\":\"%s\"," +
                    "\"color\":\"%s\"," +
                    "\"insertion\":\"[%s : %d  %d  %d]\"," +
                    "\"hoverEvent\":{\"action\":\"show_text\"," +
                                                "\"value\":\"[ %s : %d  %d  %d](id:%s)\"}" +
            "}";
    //id,color,id,x, y,z,dimension,x,y,z,id
    public String landmarkShare=
            "{\"text\":\"%s\"," +
                    "\"clickEvent\": {\"action\": \"run_command\", \"value\": \"/landmark return %s %s\"}," +
                    "\"color\":\"%s\"," +
                    "\"insertion\":\"[%s : %d  %d  %d]\"," +
                    "\"hoverEvent\":{\"action\":\"show_text\"," +
                    "\"value\":\"[ %s : %d  %d  %d](id:%s)\"}" +
                    "}";
    public String landmarkReturn=
            "{\"text\":\"直线距离丨曼哈顿距离：%sS丨%sM\"," +
                    "\"color\":\"aqua\"," +
                    "\"insertion\":\"[%s : %d  %d  %d]\"," +
                    "\"hoverEvent\":{\"action\":\"show_text\"," +
                    "\"value\":\"[ %s : %d  %d  %d]\"}" +
                    "}";
    //
    public String listPointExample=
            "{" +
                    "\"clickEvent\": {\"action\": \"suggest_command\", \"value\": \"/landmark share %s\"}," +
                    "\"color\": \"%s\"," +
                    "\"hoverEvent\": {\"action\": \"show_text\", \"value\": \"点击分享：[%s：%d  %d  %d](id:%s)\"}," +
                    "\"insertion\": \"[%s %s：%d  %d  %d]\"," +
                    "\"text\": \"%-40s\"" +
            "}," +
            "{" +
                    "\"clickEvent\": {\"action\": \"suggest_command\", \"value\": \"/landmark rename %s \"}," +
                    "\"color\": \"dark_gray\"," +
                    "\"hoverEvent\": {\"action\": \"show_text\", \"value\": \"点击更改地标的显示文本(id不变！)\"}," +
                    "\"text\": \"[重命名]\"," +
                    "\"underlined\": false" +
            "}," +
            "{" +
                    "\"clickEvent\": {\"action\": \"suggest_command\", \"value\": \"/landmark color %s \"}," +
                    "\"color\": \"dark_green\"," +
                    "\"hoverEvent\": {\"action\": \"show_text\", \"value\": \"点击更改地标的显示颜色\"}," +
                    "\"text\": \"[颜色]\"," +
                    "\"underlined\": false" +
            "}," +
            "{" +
                    "\"clickEvent\": {\"action\": \"run_command\", \"value\": \"/landmark delete %s\"}," +
                    "\"color\": \"dark_red\"," +
                    "\"hoverEvent\": {\"action\": \"show_text\", \"value\": \"点击删除该地标(无法恢复！)\"}," +
                    "\"text\": \"[删除]\n\"," +
                    "\"underlined\": false" +
            "}";
    //id,color,dimension,x,y,z,id,dimension,name,x,y,z,name,id,id,id
    public String listAddPoint="{\"clickEvent\": {\"action\": \"suggest_command\",\"value\": \"/landmark set \"}," + "\"color\": \"blue\", " +
            "\"hoverEvent\": {\"action\": \"show_text\", \"value\": \"[/landmark set ]\"},"+
            "\"text\": \" [+]点此添加新坐标点\"}";

}
