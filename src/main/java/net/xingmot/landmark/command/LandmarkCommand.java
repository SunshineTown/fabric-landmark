package net.xingmot.landmark.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.arguments.*;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.dimension.DimensionType;
import net.xingmot.landmark.LandmarkMod;
import net.xingmot.landmark.config.LandmarkPoint;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class LandmarkCommand {
    //指令注册
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        {
            dispatcher.register(literal("landmark")
                    .then(literal("set")        //    landmark <name> <x> <y> <z>
                            .requires(source -> source.hasPermissionLevel(1))
                            .then(argument("id", StringArgumentType.word())
                                    .then(argument("location", BlockPosArgumentType.blockPos())
                                            .then(argument("color", ColorArgumentType.color())
                                                    .executes(context ->
                                                            setLandmark(context.getSource(),
                                                                    StringArgumentType.getString(context,"id"),
                                                                    BlockPosArgumentType.getBlockPos(context,"location"),
                                                                    ColorArgumentType.getColor(context,"color"),
                                                                    Objects.requireNonNull(DimensionType.getId(context.getSource().getPlayer().getServerWorld().getDimension().getType())).getPath()))
                                                    .then(argument("dimension",DimensionArgumentType.dimension())
                                                            //.suggests(suggestDimension())
                                                            .executes(context ->
                                                                    setLandmark(context.getSource(),
                                                                            StringArgumentType.getString(context,"id"),
                                                                            BlockPosArgumentType.getBlockPos(context,"location"),
                                                                            ColorArgumentType.getColor(context,"color"),
                                                                            Objects.requireNonNull(DimensionType.getId(DimensionArgumentType.getDimensionArgument(context, "dimension"))).getPath()
                                                                    )))))))
                    .then(literal("delete")
                            .requires(source -> source.hasPermissionLevel(1))
                            .then(argument("id",StringArgumentType.word())
                                    .executes(context -> delLandmark(context.getSource(),StringArgumentType.getString(context,"id")))))
                    .then(literal("list")
                            .executes(context -> listLandmark(context.getSource())))
                    .then(literal("save")
                            .requires(source -> source.hasPermissionLevel(1))
                            .executes(context -> saveLandmark(context.getSource())))
                    .then(literal("color")
                            .requires(source -> source.hasPermissionLevel(1))
                            .then(argument("id",StringArgumentType.word())
                                    .then(argument("color",ColorArgumentType.color())
                                            .executes(context -> colorLandmark(context.getSource(),
                                                    StringArgumentType.getString(context,"id"),
                                                    ColorArgumentType.getColor(context,"color"))))))
                    .then(literal("rename")
                            .requires(source -> source.hasPermissionLevel(1))
                            .then(argument("id",StringArgumentType.word())
                                    .then(argument("name", MessageArgumentType.message())
                                            .executes(context -> renameLandmark(context.getSource(),
                                                    StringArgumentType.getString(context,"id"),
                                                    MessageArgumentType.getMessage(context,"name").getString())))))
                    .then(literal("share")
                            .then(argument("id",StringArgumentType.word())
                                    .executes(context -> shareLandmark(context.getSource(),
                                            StringArgumentType.getString(context,"id"),
                                            true,context.getSource().getPlayer()))
                                    .then(argument("player", EntityArgumentType.player())
                                            .executes(context -> shareLandmark(context.getSource(),
                                                    StringArgumentType.getString(context,"id"),
                                                    false,EntityArgumentType.getEntity(context,"player"))))))
                    .then(literal("at")
                            .then(argument("player", EntityArgumentType.player())
                                    .then(argument("text", MessageArgumentType.message())
                                            .executes(context -> atLandmark(context.getSource(),
                                                    EntityArgumentType.getPlayer(context,"player"),
                                                    MessageArgumentType.getMessage(context,"text").getString())))))
                    .then(literal("return")
                            .then(argument("player", EntityArgumentType.player())
                                    .then(argument("id", StringArgumentType.word())
                                            .executes(context -> returnLandmark(context.getSource(),
                                                    EntityArgumentType.getPlayer(context,"player"),
                                                    StringArgumentType.getString(context,"id"))))))
                    .then(literal("reload")
                            .then(literal("config")
                                    .executes(context -> reloadConfigLandmark(context.getSource())))
                            .then(literal("point")
                                    .executes(context -> reloadPointLandmark(context.getSource()))))
            );
        }//landmark
        {
            dispatcher.register(literal("lmsg")
                    .then(argument("message",MessageArgumentType.message())
                            .executes(context -> lmsg(context.getSource(),MessageArgumentType.getMessage(context,"message").getString()))
                    ));
        }//lmsg
    }

    //地标建立 /landmark set id ~ ~ ~ color [dimension]
    private static int setLandmark(ServerCommandSource source, String id, BlockPos coordinate, Formatting color, String dimension) throws CommandSyntaxException{
        ServerPlayerEntity player = source.getPlayer();
        LandmarkMod.pointManager.addPoint("default",id,id,color,coordinate.getX(),coordinate.getY(),coordinate.getZ(),dimension);
        try{
            LandmarkMod.pointManager.savePoint();
            player.sendMessage(Text.Serializer.fromJson(String.format("[{\"text\":\"%s\"},",LandmarkMod.config.landmark.landmarkCreateTips) +
                    getSmpPointJson(id) +","+normalJson(" ！")+"]"));
        }catch(Exception e){
            e.printStackTrace();
        }
        return 1;
    }

    //地标删除 /landmark delete id
    private static int delLandmark(ServerCommandSource source, String id) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayer();

        if(LandmarkMod.pointManager.delPoint("default",id)){
            player.sendMessage(new LiteralText(LandmarkMod.config.landmark.landmarkDel+"["+id+"] ！"));
        }else{
            player.sendMessage(new LiteralText(LandmarkMod.config.landmark.landmarkNull));
        }
        return 1;
    }

    //地标列表 /landmark list      分类暂时未加：[category]
    private static int listLandmark(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player =source.getPlayer();
        StringBuilder jsons= new StringBuilder("[");
        String id;
        String name;
        Formatting color;
        String dimension;
        String des;     //暂未加入
        int x,y,z;
        if(isPointEmpty("default")){
            jsons.append(normalJson(LandmarkMod.config.landmark.landmarkListNull)).append(",").append(LandmarkMod.config.jsons.listAddPoint).append("]");
            player.sendMessage(Text.Serializer.fromJson(jsons.toString()));
        }else{
            for(Map.Entry<String, LandmarkPoint> entry : LandmarkMod.point.category.get("default").pointGroups.entrySet()){
                id=entry.getKey();
                name=entry.getValue().name;
                color=entry.getValue().color;
                x=entry.getValue().x;
                y=entry.getValue().y;
                z=entry.getValue().z;
                dimension=entry.getValue().dimension;
                des=entry.getValue().description;        //暂时没做
                jsons.append(String.format(LandmarkMod.config.jsons.listPointExample, id, LandmarkMod.config.landmark.dimensions_name.get(dimension),
                        x, y, z, id, id,LandmarkMod.config.landmark.dimensions_name2.get(dimension)+"丨"+name, x, y, z,
                        color + "["+name+"]", id, id, id)).append(",");
            }
            jsons.append(LandmarkMod.config.jsons.listAddPoint);
            jsons.append("]");
            player.sendMessage(Text.Serializer.fromJson(jsons.toString()));
        }
        return 1;
    }

    //地标重命名
    private static int renameLandmark(ServerCommandSource source,String id,String name) throws CommandSyntaxException {
        ServerPlayerEntity player=source.getPlayer();
        if(!existPoint("default", id)){
            player.sendMessage(new LiteralText(LandmarkMod.config.landmark.landmarkNull));
        }else{
            LandmarkMod.pointManager.renamePoint("default",id,name);
            player.sendMessage(Text.Serializer.fromJson("["+normalJson(LandmarkMod.config.landmark.landmarkRenameTips)+","+
                            getSmpPointJson(id)+"]"));
        }
        return 1;
    }
    //地标改颜色
    private static int colorLandmark(ServerCommandSource source,String id,Formatting color) throws CommandSyntaxException {
        ServerPlayerEntity player=source.getPlayer();
        if(!existPoint("default", id)){
            player.sendMessage(new LiteralText(LandmarkMod.config.landmark.landmarkNull));
        }else {
            LandmarkMod.pointManager.colorPoint("default", id, color);
            player.sendMessage(Text.Serializer.fromJson("["+normalJson(LandmarkMod.config.landmark.landmarkColorTips)+","+
                    getSmpPointJson(id)+"]"));
        }
        return 1;
    }

    //@指令
    private static int atLandmark(ServerCommandSource source,Entity player1,String text) throws CommandSyntaxException {
        ServerPlayerEntity player=source.getPlayer();
        MinecraftServer server=source.getMinecraftServer();
        server.getPlayerManager().broadcastChatMessage(new LiteralText(String.format(LandmarkMod.config.share.landmarkAtTips+
                        text, player.getGameProfile().getName(),player1.getEntityName())),true);
        return 1;
    }

    //快捷回复
    private static int returnLandmark(ServerCommandSource source,Entity player1,String id) throws CommandSyntaxException{
        ServerPlayerEntity player=source.getPlayer();
        MinecraftServer server=source.getMinecraftServer();
        if(!existPoint("default", id)){
            player.sendMessage(new LiteralText(LandmarkMod.config.share.landmarkReturnNullPoint));
        }else{
            BlockPos coordinate0=player.getBlockPos();
            int x1,y1,z1;
            String dimension=getPoint("default",id).dimension;
            x1= getPoint("default", id).x;y1= getPoint("default", id).y;z1= getPoint("default", id).z;
            String squaredDistance=String.format("%.2f",coordinate0.getSquaredDistance(x1,y1,z1,true));
            String manhattanDistance=String.format("%d",coordinate0.getManhattanDistance(new Vec3i(x1,y1,z1)));
            server.getPlayerManager().broadcastChatMessage(Text.Serializer.fromJson("["+normalJson(String.format(LandmarkMod.config.share.landmarkAtTips+
                            LandmarkMod.config.share.landmarkReturnTips,player.getGameProfile().getName(),player1.getEntityName()))+","+
                            String.format(LandmarkMod.config.jsons.landmarkReturn,squaredDistance,manhattanDistance,
                                    LandmarkMod.config.landmark.dimensions_name2.get(dimension),coordinate0.getX(),coordinate0.getY(),coordinate0.getZ(),
                                    LandmarkMod.config.landmark.dimensions_name.get(dimension),coordinate0.getX(),coordinate0.getY(),coordinate0.getZ())+"]"
                    ),true);
        }
        return 1;
    }

    //分享
    private static int shareLandmark(ServerCommandSource source,String id, Boolean broadcast, Entity player1) throws CommandSyntaxException {
        ServerPlayerEntity player=source.getPlayer();
        MinecraftServer server=source.getMinecraftServer();
        if(!existPoint("default", id)){
            player.sendMessage(new LiteralText(LandmarkMod.config.landmark.landmarkNull));
        }else{
            Formatting color= getPoint("default", id).color;
            String name= getPoint("default", id).name;
            String dimension= getPoint("default", id).dimension;
            int x= getPoint("default", id).x;
            int y= getPoint("default", id).y;
            int z= getPoint("default", id).z;

            String msg="["+normalJson(String.format(LandmarkMod.config.share.landmarkShareTipsReceive,player.getGameProfile().getName()))+
                    ","+String.format(LandmarkMod.config.jsons.landmarkShare,color + "["+name+"]",player1.getEntityName(),id,name,
                    x, y,z,LandmarkMod.config.landmark.dimensions_name.get(dimension),x,y,z,id)+
                    ","+ normalJson(LandmarkMod.config.share.landmarkShareTipsReceive2)+"]";
            if(broadcast){
                server.getPlayerManager().broadcastChatMessage(Text.Serializer.fromJson(msg),true);
            }else{
                player1.sendMessage(Text.Serializer.fromJson(msg));
                player.sendMessage(new LiteralText(String.format(LandmarkMod.config.share.landmarkShareTipsSend,player1.getEntityName())));
            }
        }
        return 1;
    }

    //自动替换坐标[id]
    private static int lmsg(ServerCommandSource source,String message) throws CommandSyntaxException {
        MinecraftServer server=source.getMinecraftServer();
        StringBuilder result= new StringBuilder();
        StringBuilder msg= new StringBuilder();
        StringBuilder arg= new StringBuilder();
        boolean msgflag=false;
        boolean argflag=false;
        result.append("[").append(normalJson(String.format("<%s> ",source.getPlayer().getGameProfile().getName()))).append(",");
        for (char a :message.toCharArray()) {
            if(a != '[' && a != ']'&& !msgflag){
                msg.append(a);
            }else if(a=='['&& !msgflag){
                msgflag=true;
                argflag=true;
                result.append(normalJson(msg.toString())).append(",");
                msg.delete(0,msg.length());
            }else if(a==']'&& msgflag){
                msgflag=false;
                argflag=false;
                result.append(getSmpPointJson(arg.toString())).append(",");
                arg.delete(0,arg.length());
            }else if(msgflag&&argflag){
                if(StringReader.isAllowedInUnquotedString(a)){
                    arg.append(a);
                }else{
                    argflag=false;
                }
            }
        }
        result.append(normalJson(msg.toString())).append("]");
        server.getPlayerManager().broadcastChatMessage(Text.Serializer.fromJson(result.toString()),true);
        return 1;
    }

    //保存地标到json文件
    private static int saveLandmark(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player=source.getPlayer();
        try {
            LandmarkMod.pointManager.savePoint();
            player.sendMessage(new LiteralText(LandmarkMod.config.landmark.landmarkSaveTips));
        }catch (IOException e){
            e.printStackTrace();
        }
        return 1;
    }

    //重载地标
    private static int reloadPointLandmark(ServerCommandSource source) throws CommandSyntaxException{
        LandmarkMod.setupConfig();
        return 1;
    }

    //重载配置文件
    private static int reloadConfigLandmark(ServerCommandSource source) throws CommandSyntaxException{
        LandmarkMod.setupPoint(source.getMinecraftServer());
        return 1;
    }

    private static String getSmpPointJson(String id) {
        String name=getPoint("default",id).name;
        Formatting color=getPoint("default",id).color;
        int x=getPoint("default",id).x, y=getPoint("default",id).y,  z=getPoint("default",id).z;
        String dimension=getPoint("default",id).dimension;

        return String.format(LandmarkMod.config.jsons.landmarkCreate,color + "[" + name + "]", id,
                x, y, z, LandmarkMod.config.landmark.dimensions_name.get(dimension), x, y, z, id);
    }


    private static LandmarkPoint getPoint(String type, String id) {
        return LandmarkMod.point.category.get(type).pointGroups.get(id);
    }
    private static Boolean isPointEmpty(String type){
        return(LandmarkMod.point.category.isEmpty()||LandmarkMod.point.category.get(type).pointGroups.isEmpty());
    }
    private static boolean existPoint(String type,String id) {
        return !(isPointEmpty(type) || (getPoint(type, id) == null));
    }
    private static String normalJson(String text){
        return String.format("{\"text\":\"%s\",\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false}",text);
    }

}