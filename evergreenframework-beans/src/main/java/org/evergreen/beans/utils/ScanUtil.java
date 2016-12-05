package org.evergreen.beans.utils;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 扫描工具类,用于扫描指定包下面的所有类,包括子包
 * @author lenovo
 *
 */
public class ScanUtil {

	private static List<String> list = new ArrayList<String>();

	/**
	 * 扫描指定的包
	 * @return
	 */
	public static List<String> scan(String packages) {
		String path = toPackagePath(packages);
		try {
			toClassPath(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 将路径转换成包名
	 * @param packagePath
	 * @return
	 */
	private static String toPackagePath(String packagePath) {
		packagePath = packagePath.replace(".", File.separator);
		return getRealPath()+packagePath;
	}

	/**
	 * 遍历目录将类名存入List中
	 * @param classPath
	 * @throws Exception
	 */
	private static void toClassPath(String classPath) throws Exception {
		classPath = URLDecoder.decode(classPath, "utf-8");
		File[] fs = new File(classPath).listFiles();
		if (fs != null) {
			for (File file : fs) {
				if (file.isDirectory()) {
					toClassPath(file.getAbsolutePath());
				} else {
					// 将文件名转换成完整类名
					String path = toClassName(file);
					if (path != null) 
						list.add(path);
				}
			}
		}
	}

	/**
	 *  将文件名转换成完整类名
	 * @param file
	 * @return
	 * @throws Exception
	 */
	private static String toClassName(File file) throws Exception {
		if (file.getName().endsWith(".class")) {
			String path = URLDecoder.decode(getRealPath(), "utf-8");
			path = new File(path).getAbsolutePath() + File.separator;
			path = file.getAbsolutePath().substring(path.length(),
					file.getAbsolutePath().length());
			String className = path.replace(File.separator, ".");
			className = className.substring(0, className.length() - 6);
			return className;
		} else {
			return null;
		}
	}
	
	/**
	 * 获取当前项目的classes目录
	 * @return
	 */
	private static String getRealPath(){
		return Thread.currentThread().getContextClassLoader().getResource("").getPath();
	}

	public static void main(String[] args) throws Exception{
		List<String> list = ScanUtil.scan("org.evergreen");
		for (String string : list) {
			System.out.println(string);
		}
	}
}
