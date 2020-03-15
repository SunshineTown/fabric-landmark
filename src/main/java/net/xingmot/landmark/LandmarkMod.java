package net.xingmot.landmark;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.xingmot.landmark.command.LandmarkCommand;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.xingmot.landmark.config.LandmarkConfig;
import net.xingmot.landmark.config.PointManager;
import net.xingmot.landmark.config.Pojo;

import java.io.*;
import java.nio.charset.StandardCharsets;


public class LandmarkMod implements ModInitializer {
	public static LandmarkConfig config;
	public static Pojo point;
	public static String levelName;
	@Override
	public void onInitialize() {
		if(setupConfig()){
			CommandRegistry.INSTANCE.register(false, LandmarkCommand::register);
			ServerStartCallback.EVENT.register(server -> {
				levelName=server.getLevelName();
				setupPoint();
			});
		}

	}

	public static  boolean setupConfig(){
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			try{
				File configFile = new File("config/Landmark/Landmark.json");
				if(configFile.exists()){
					try (Reader reader = new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8)){
						config = gson.fromJson(reader, LandmarkConfig.class);
					}
				}else{
					configFile.getParentFile().mkdir();
					config = new LandmarkConfig();
				}
				try(Writer writer = new OutputStreamWriter(new FileOutputStream(configFile), StandardCharsets.UTF_8)){
					writer.write(gson.toJson(config));
				}
				return true;
			}catch (Exception e){
				e.printStackTrace();
				return false;
			}
	}
	public static void setupPoint(){
		PointManager manager=new PointManager();
		try {
			manager.loadPoint();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
