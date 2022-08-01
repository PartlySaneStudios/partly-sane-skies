package me.partlysanestudios.partlysaneskies;

import java.io.File;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;


public class ConfigHandler {
	public static Configuration config;
	private static String file = "config/modID.cfg";
	
	public static void init() {
		config = new Configuration(new File(file));
		try {
			config.load();
		} catch (Exception e) {
			System.out.println("Cannot load configuration file!");
		} finally {
			config.save();
		}
		// Default config
		if (!config.hasKey("general", "loadedBefore")) {
			writeConfig("general", "loadedBefore", true);
			writeConfig("skyblockGeneral", "rngDrop", false);
		}
	}
	
	/*
	 * Removes specific category from configuration file.
	 */
	public static void removeConfig(String category) {
		config = new Configuration(new File(file));
		try {
			config.load();
			if (config.hasCategory(category))
				config.removeCategory(new ConfigCategory(category));
		} catch (Exception e) {
			System.out.println("Cannot load configuration file!");
		} finally {
			config.save();
		}
	}
	
	/*
	 * Removes specific key in specific category from configuration file.
	 */
	public static void removeConfig(String category, String key) {
		config = new Configuration(new File(file));
		try {
			config.load();
			if (config.getCategory(category).containsKey(key))
				config.getCategory(category).remove(key);
		} catch (Exception e) {
			System.out.println("Cannot load configuration file!");
		} finally {
			config.save();
		}
	}
	
	public static int getInt(String category, String key) {
		config = new Configuration(new File(file));
		try {
			config.load();
			if (config.getCategory(category).containsKey(key)) {
				return config.get(category, key, 0).getInt();
			}
		} catch (Exception e) {
			System.out.println("Cannot load configuration file!");
		} finally {
			config.save();
		}
		return 0;
	}
	
	public static double getDouble(String category, String key) {
		config = new Configuration(new File(file));
		try {
			config.load();
			if (config.getCategory(category).containsKey(key)) {
				return config.get(category, key, 0D).getDouble();
			}
		} catch (Exception e) {
			System.out.println("Cannot load configuration file!");
		} finally {
			config.save();
		}
		return 0D;
	}
	
	public static float getFloat(String category, String key) {
		config = new Configuration(new File(file));
		try {
			config.load();
			if (config.getCategory(category).containsKey(key)) {
				return (float)config.get(category, key, 0D).getDouble();
			}
		} catch (Exception e) {
			System.out.println("Cannot load configuration file!");
		} finally {
			config.save();
		}
		return 0f;
	}
	
	public static String getString(String category, String key) {
		config = new Configuration(new File(file));
		try {
			config.load();
			if (config.getCategory(category).containsKey(key)) {
				return config.get(category, key, "").getString();
			}
		} catch (Exception e) {
			System.out.println("Cannot load configuration file!");
		} finally {
			config.save();
		}
		return "";
	}
	
	public static long getLong(String category, String key) {
		config = new Configuration(new File(file));
		try {
			config.load();
			if (config.getCategory(category).containsKey(key)) {
				return Long.parseLong(config.get(category, key, 0L).getString());
			}
		} catch (Exception e) {
			System.out.println("Cannot load configuration file!");
		} finally {
			config.save();
		}
		return 0L;
	}
	
	public static short getShort(String category, String key) {
		config = new Configuration(new File(file));
		try {
			config.load();
			if (config.getCategory(category).containsKey(key)) {
				return (short)config.get(category, key, (short)0).getInt();
			}
		} catch (Exception e) {
			System.out.println("Cannot load configuration file!");
		} finally {
			config.save();
		}
		return (short)0;
	}
	
	public static byte getByte(String category, String key) {
		config = new Configuration(new File(file));
		try {
			config.load();
			if (config.getCategory(category).containsKey(key)) {
				return (byte)config.get(category, key, (byte)0).getInt();
			}
		} catch (Exception e) {
			System.out.println("Cannot load configuration file!");
		} finally {
			config.save();
		}
		return (byte)0;
	}
	
	public static boolean getBoolean(String category, String key) {
		config = new Configuration(new File(file));
		try {
			config.load();
			if (config.getCategory(category).containsKey(key))
				return config.get(category, key, false).getBoolean();
		} catch (Exception e) {
			System.out.println("Cannot load configuration file!");
		} finally {
			config.save();
		}
		return false;
	}
	
	public static void writeConfig(String category, String key, String value) {
		config = new Configuration(new File(file));
		try {
			config.load();
			config.getCategory(category).get(key).set(value);
		} catch (Exception e) {
			System.out.println("Cannot load configuration file!");
		} finally {
			config.save();
		}
	}
	
	public static void writeConfig(String category, String key, int value) {
		config = new Configuration(new File(file));
		try {
			config.load();
			config.getCategory(category).get(key).set(value);
		} catch (Exception e) {
			System.out.println("Cannot load configuration file!");
		} finally {
			config.save();
		}
	}
	
	public static void writeConfig(String category, String key, boolean value) {
		config = new Configuration(new File(file));
		try {
			config.load();
			config.getCategory(category).get(key).set(value);
		} catch (Exception e) {
			System.out.println("Cannot load configuration file!");
		} finally {
			config.save();
		}
	}
	
	public static void writeConfig(String category, String key, long value) {
		config = new Configuration(new File(file));
		try {
			config.load();
			config.getCategory(category).get(key).set(value);
		} catch (Exception e) {
			System.out.println("Cannot load configuration file!");
		} finally {
			config.save();
		}
	}
	
	public static void writeConfig(String category, String key, double value) {
		config = new Configuration(new File(file));
		try {
			config.load();
			config.getCategory(category).get(key).set(value);
		} catch (Exception e) {
			System.out.println("Cannot load configuration file!");
		} finally {
			config.save();
		}
	}
	
	public static void writeConfig(String category, String key, short value) {
		config = new Configuration(new File(file));
		try {
			config.load();
			config.getCategory(category).get(key).set(Integer.valueOf(value));
		} catch (Exception e) {
			System.out.println("Cannot load configuration file!");
		} finally {
			config.save();
		}
	}

	public static void writeConfig(String category, String key, byte value) {
		config = new Configuration(new File(file));
		try {
			config.load();
			config.getCategory(category).get(key).set(Integer.valueOf(value));
		} catch (Exception e) {
			System.out.println("Cannot load configuration file!");
		} finally {
			config.save();
		}
	}

	public static void writeConfig(String category, String key, float value) {
		config = new Configuration(new File(file));
		try {
			config.load();
			config.getCategory(category).get(key).set(Double.valueOf(value));
		} catch (Exception e) {
			System.out.println("Cannot load configuration file!");
		} finally {
			config.save();
		}
	}
	
	public static boolean hasCategory(String category) {
		config = new Configuration(new File(file));
		try {
			config.load();
			return config.hasCategory(category);
		} catch (Exception e) {
			System.out.println("Cannot load configuration file!");
		} finally {
			config.save();
		}
		return false;
	}
	
	public static boolean hasKey(String category, String key) {
		config = new Configuration(new File(file));
		try {
			config.load();
			if (!config.hasCategory(category))
				return false;
			return config.getCategory(category).containsKey(key);
		} catch (Exception e) {
			System.out.println("Cannot load configuration file!");
		} finally {
			config.save();
		}
		return false;
	}
	
	public static void setFile(String filename) {
		file = "config/" + filename;
	}
	
	public static String getFile() {
		return file;
	}
}
