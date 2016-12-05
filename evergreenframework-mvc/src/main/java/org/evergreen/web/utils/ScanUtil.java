package org.evergreen.web.utils;

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
	/**
	 * 存放所有类的类名
	 */
	private static List<String> list = new ArrayList<String>();

	/**
	 * 扫描
	 * @param packages
	 * @return
	 */
	public static List<String> scanPackage(String packages) {
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
		// listFiles()获取路径下的所有文件或文件夹，返回的是一个File[]数组
		File[] fs = new File(classPath).listFiles();
		if (fs != null) {
			for (File file : fs) {
				// file.isDirectory()方法判断当前额的file对象是否是一个文件夹
				if (file.isDirectory()) {
					// 获取文件夹的绝度路径
					// 递归调用
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
		ScanUtil.scanPackage("org.evergreen.web");
		for (String string : list) {
			System.out.println(string);
		}
	}
}
