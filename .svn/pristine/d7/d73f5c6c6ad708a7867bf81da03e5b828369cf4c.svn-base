package com.mmec.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.google.gson.Gson;

public class FileUtil {
	private static Logger log = Logger.getLogger(FileUtil.class);

	public Result uploadImgByBase64(String base64Str, String filePath, String fileName) {

		try {
			log.info("uploadImgByBase64: filePath=" + filePath + " fileName: " + fileName);

			File path = new File(filePath);
			if (!path.exists()) {
				path.mkdirs();
			}

			boolean bo = PictureAndBase64.GenerateImage(base64Str, filePath + File.separator + fileName);

			if (bo) {
				log.info("uploadImgByBase64生成图片成功，filePath: " + filePath + File.separator + fileName);
				return new Result(ErrorData.SUCCESS, PropertiesUtil.getProperties().readValue("IMAGE_SUCCESS"),
						"filePath: " + filePath + File.separator + fileName);
			} else {
				log.info("uploadImgByBase64生成图片失败，base64Str: " + base64Str);
				return new Result(ErrorData.SYSTEM_ERROR, PropertiesUtil.getProperties().readValue("IMAGE_FAILED"),
						"filePath: " + filePath + File.separator + fileName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(ErrorData.SYSTEM_ERROR, PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), "");
		}
	}

	public Result uploadFileByDataHandler(DataHandler handler, String filePath, String fileName) {

		log.info("uploadFile: filePath=" + filePath + " fileName: " + fileName);

		if (filePath != null && !"".equals(filePath)) {

			File path = new File(filePath);
			if (!path.exists()) {
				path.mkdirs();
			}

			if (handler != null) {
				InputStream is = null;
				FileOutputStream fos = null;
				try {
					is = handler.getInputStream();

					File fileOut = new File(filePath + File.separator + fileName);
					fos = new FileOutputStream(fileOut);

					byte[] buff = new byte[1024 * 8];
					int len = 0;
					while ((len = is.read(buff)) > 0) {
						fos.write(buff, 0, len);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					log.error("FileNotFoundException: " + e);
					return new Result(ErrorData.SYSTEM_ERROR, PropertiesUtil.getProperties().readValue("CSCW"), "");
				} catch (Exception e) {
					e.printStackTrace();
					log.error("Exception: " + e);
					return new Result(ErrorData.SYSTEM_ERROR, PropertiesUtil.getProperties().readValue("CSCW"), "");
				} finally {
					try {
						if (fos != null) {
							fos.flush();
							fos.close();
						}
						if (is != null) {
							is.close();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				log.info("uploadFileByDataHandler上传文件成功，filePath:" + path.getAbsolutePath());
				return new Result(ErrorData.SUCCESS, "filePath:" + path.getAbsolutePath(), "");
			} else {
				log.info("DataHandler==null");
				return new Result(ErrorData.SYSTEM_ERROR, PropertiesUtil.getProperties().readValue("CSCW"), "");
			}
		} else {
			log.info("filePath==null");
			return new Result(ErrorData.SYSTEM_ERROR, PropertiesUtil.getProperties().readValue("CSCW"), "");
		}
	}

	/**
	 * Description: 向FTP服务器上传文件
	 * 
	 * @param url
	 *            FTP服务器hostname
	 * @param port
	 *            FTP服务器端口
	 * @param username
	 *            FTP登录账号
	 * @param password
	 *            FTP登录密码
	 * @param path
	 *            FTP服务器保存目录
	 * @param filename
	 *            上传到FTP服务器上的文件名
	 * @param input
	 *            输入流
	 * @return 成功返回true，否则返回false
	 */
	public static boolean uploadFile(String url, int port, String username, String password, String path,
			String filename, InputStream input) {
		boolean success = false;
		FTPClient ftp = new FTPClient();
		// ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
		try {
			int reply;
			ftp.connect(url, port);// 连接FTP服务器
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.login(username, password);// 登录
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return success;
			}
			ftp.changeWorkingDirectory(path);
			ftp.storeFile(filename, input);

			input.close();
			ftp.logout();
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return success;
	}

	// 获取目录下的文件名
	public static List<String> getFileName(String path) {
		// String path = "F:/office"; // 路径
		File f = new File(path);
		if (!f.exists()) {

			log.info(path + " not exists");

			return null;
		}
		List<String> pathList = new ArrayList<String>();
		File fa[] = f.listFiles();
		for (int i = 0; i < fa.length; i++) {
			File fs = fa[i];
			if (fs.isDirectory()) {

				log.info(fs.getName() + " [目录]");

			} else {

				log.info(fs.getName());
				pathList.add(fs.getName());
			}
		}
		return pathList;
	}

	public static boolean appendHtml(String jsonData, String src, String desc, String filePath) {
		log.info("jsonData======" + jsonData);
		if (!jsonData.isEmpty()) {
			log.info("进入数据装填");
			try {
				Gson gson = new Gson();
				Map<String, String> map = gson.fromJson(jsonData, Map.class);
				File input = new File(src);
				Document doc = Jsoup.parse(input, "UTF-8", "");
				for (String key : map.keySet()) {
					if (!"".equals(StringUtil.nullToString(key))) {
						log.info("通用外围key:" + key);
						Element ele = doc.getElementById(key);

						if (key.length() >= 8 && "checkbox".equals(key.substring(0, 8))) {
							if ("checked".equals(map.get(key).toString())) {
								ele.append("<img height='15' width='15' src=\"../checked.png\"/>");
							} else {
								ele.append("<img height='15' width='15' src=\"../unchecked.png\"/>");
							}
						}
						// 创建表单元素
						if (!(key.length() >= 8 && "checkbox".equals(key.substring(0, 8)))) {
							ele.append(StringUtil.nullToString(map.get(key)));
						}
					}
				}
				String content = doc.toString();
				// log.info(doc.toString());
				if (FileUtil.writeTxtFile(content, new File(desc)))
					return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	public static boolean writeTxtFile(String content, File fileName) throws Exception {
		RandomAccessFile mm = null;
		boolean flag = false;
		FileOutputStream o = null;
		try {
			o = new FileOutputStream(fileName);
			o.write(content.getBytes("UTF-8"));

			flag = true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (o != null) {
				o.close();
			}
			if (mm != null) {
				mm.close();
			}
		}
		return flag;
	}

	/**
	 * 创建文件
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean createFile(File fileName) throws Exception {
		boolean flag = false;
		try {
			if (!fileName.exists()) {
				fileName.createNewFile();
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public static void Copy1(File oldfile, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			// File oldfile = new File(oldPath);
			if (oldfile.exists()) {
				InputStream inStream = new FileInputStream(oldfile);
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread;
					log.info(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			log.info("error  ");
			e.printStackTrace();
		}

	}

	public static void Copy(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) {
				InputStream inStream = new FileInputStream(oldPath);
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread;
					// log.info(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			log.info("error  ");
			e.printStackTrace();
		}
	}

	public static boolean move(String srcFile, String destPath) {
		// File (or directory) to be moved
		File file = new File(srcFile);

		// Destination directory
		File dir = new File(destPath);

		// Move file to new directory
		boolean success = file.renameTo(new File(dir, file.getName()));

		return success;
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	public static void copyFile(String oldPath, String newPath) {
		InputStream inStream = null;
		FileOutputStream fs = null;
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				inStream = new FileInputStream(oldPath); // 读入原文件
				fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1024 * 8];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					// log.info(bytesum);
					fs.write(buffer, 0, byteread);
				}
			}
		} catch (Exception e) {

			log.info("复制单个文件操作出错");

			e.printStackTrace();
		} finally {
			if (null != inStream) {
				try {
					inStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (null != fs) {
				try {
					fs.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static void fileinput(String src, String desc) {
		File file = new File(desc);
		// 创建一个下载目录
		if (!file.isDirectory() && !file.exists()) {
			log.info("目录不存在");
			file.mkdir();
		}
		// 拷贝文件到下载路径
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			fis = new FileInputStream(src);
			bis = new BufferedInputStream(fis);

			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			// 缓冲数组
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = bis.read(b)) != -1) {
				bos.write(b, 0, len);
			}
			bos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭流
			try {
				if (bis != null)
					bis.close();
				if (bos != null)
					bos.close();

				if (fis != null) {
					fis.close();
				}

				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public static void copyFolder(File src, File dest) throws IOException {
		if (src.isDirectory()) {
			if (!dest.exists()) {
				dest.mkdir();
			}
			String files[] = src.list();
			for (String file : files) {
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				// 递归复制
				copyFolder(srcFile, destFile);
			}
		} else {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = new FileInputStream(src);
				out = new FileOutputStream(dest);

				byte[] buffer = new byte[1024];

				int length;

				while ((length = in.read(buffer)) > 0) {
					out.write(buffer, 0, length);
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}

			}
		}
	}

	public static boolean createFile(String destFileName) {
		File file = new File(destFileName);
		if (file.exists()) {
			// log.info("创建单个文件" + destFileName + "失败，目标文件已存在！");
			return false;
		}
		if (destFileName.endsWith(File.separator)) {
			// log.info("创建单个文件" + destFileName + "失败，目标文件不能为目录！");
			return false;
		}
		// 判断目标文件所在的目录是否存在
		if (!file.getParentFile().exists()) {
			// 如果目标文件所在的目录不存在，则创建父目录

			log.info("目标文件所在目录不存在，准备创建它！");

			if (!file.getParentFile().mkdirs()) {

				log.info("创建目标文件所在目录失败！");

				return false;
			}
		}
		// 创建目标文件
		try {
			if (file.createNewFile()) {

				log.info("创建单个文件" + destFileName + "成功！");

				return true;
			} else {

				log.info("创建单个文件" + destFileName + "失败！");

				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.info("创建单个文件" + destFileName + "失败！" + e.getMessage());
			return false;
		}
	}

	public static boolean createDir(String destDirName) {
		File dir = new File(destDirName);
		if (dir.exists()) {

			log.info("创建目录" + destDirName + "失败，目标目录已经存在");

			return false;
		}
		if (!destDirName.endsWith(File.separator)) {
			destDirName = destDirName + File.separator;
		}
		// 创建目录
		if (dir.mkdirs()) {

			log.info("创建目录" + destDirName + "成功！");

			return true;
		} else {

			log.info("创建目录" + destDirName + "失败！");

			return false;
		}
	}

	public static String createTempFile(String prefix, String suffix, String dirName) {
		File tempFile = null;
		if (dirName == null) {
			try {
				// 在默认文件夹下创建临时文件
				tempFile = File.createTempFile(prefix, suffix);
				// 返回临时文件的路径
				return tempFile.getCanonicalPath();
			} catch (IOException e) {
				e.printStackTrace();
				log.info("创建临时文件失败！" + e.getMessage());
				return null;
			}
		} else {
			File dir = new File(dirName);
			// 如果临时文件所在目录不存在，首先创建
			if (!dir.exists()) {
				if (!FileUtil.createDir(dirName)) {

					log.info("创建临时文件失败，不能创建临时文件所在的目录！");

					return null;
				}
			}
			try {
				// 在指定目录下创建临时文件
				tempFile = File.createTempFile(prefix, suffix, dir);
				return tempFile.getCanonicalPath();
			} catch (IOException e) {
				e.printStackTrace();
				log.info("创建临时文件失败！" + e.getMessage());
				return null;
			}
		}
	}

	public static void main(String[] args) {
		// 创建目录
		String dirName = "D:/work/temp/temp0/temp1";
		FileUtil.createDir(dirName);
		// 创建文件
		String fileName = dirName + "/temp2/tempFile.txt";
		FileUtil.createFile(fileName);
		// 创建临时文件
		String prefix = "temp";
		String suffix = ".txt";
		for (int i = 0; i < 10; i++) {
			log.info("创建了临时文件：" + FileUtil.createTempFile(prefix, suffix, dirName));
		}
		// 在默认目录下创建临时文件
		for (int i = 0; i < 10; i++) {
			log.info("在默认目录下创建了临时文件：" + FileUtil.createTempFile(prefix, suffix, null));
		}
	}

	public Result uploadFileByBase64(String fileInfo, String filePath, String fileName) {
		log.info("uploadFileByBase64:filePath:" + filePath + ",fileName:" + fileName);
		Result result = null;
		try {

			File path = new File(filePath);
			if (!path.exists()) {
				path.mkdirs();
			}
			boolean res = PictureAndBase64.GenerateImage(fileInfo, filePath + File.separator + fileName);
			if (res) {
				log.info("uploadFileByBase64 生成文件成功，filePath:" + filePath + File.separator + fileName);
				return result = new Result(ErrorData.SUCCESS, PropertiesUtil.getProperties().readValue("FILE_SUCCESS"),
						filePath + File.separator + fileName);
			} else {
				log.info("uploadFileByBase64 生成文件失败，fileInfo:" + fileInfo);
				return result = new Result(ErrorData.UPLOADATTACH_ERROR,
						PropertiesUtil.getProperties().readValue("FILE_FAIL"), "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return result = new Result(ErrorData.UPLOADATTACH_ERROR,
					PropertiesUtil.getProperties().readValue("UPLOADATTACH_ERROR"), "");
		}

	}

}
