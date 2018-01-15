package com.mmec.aps.service.impl;

import java.awt.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;

import com.mmec.aps.service.ImgpathService;
import com.mmec.thrift.service.ImgResult;
import com.mmec.thrift.service.Imgpath;
import com.mmec.util.PdfToImgUtil;
import com.mmec.util.lucency.ImgAlpha;

/**
 * pdf转图片实现类
 * 
 * @author Administrator
 * 
 */
public class ImgpathServiceImpl implements ImgpathService {

	private Logger log = Logger.getLogger(ImgpathServiceImpl.class);

	@Override
	public Imgpath getPathForPdfToImg(String filepath, float zoom) {
		// 初始化结果
		Imgpath imgpaths = new Imgpath(200, null);
		log.info(filepath);
		// 图片存放路径
		Map<String, Map<Integer, String>> filesMap = new HashMap<String, Map<Integer, String>>();

		File path = new File(filepath);

		// 判断当前路径是否存在，且是个目录
		if (path.exists() && path.isDirectory()) {
			// 返回一个相对路径名数组，这些路径名表示此相对路径名表示的目录中的文件
			File[] files = path.listFiles();
			// 遍历文件路径数组
			for (File file : files) {
				// 当前文件类型是目录或备份文件或非PDF格式文件，则跳出本次循环
				if (file.isDirectory() || file.getName().contains("cache_") || !file.getName().contains(".pdf")) {
					continue;
				}				
				// 将指定pdf文件转换为指定路径的缩略图
				try {
					Map<Integer, String> fileMap = PdfToImgUtil.tranfer(file,
							zoom);

					filesMap.put(file.getName(), fileMap);
				} catch (IOException e) {
					log.error("pdf文件转图片失败;", e);
					log.info("IOException:"+e.getMessage());
					imgpaths.setCode(400);

					return imgpaths;
				} catch (PDFException e) {
					log.error("pdf文件转图片失败;", e);
					log.info("PDFException:"+e.getMessage());
					// 结果码，200成功，400错误
					imgpaths.setCode(400);
				} catch (PDFSecurityException e) {
					log.error("pdf文件转图片失败;", e);
					log.info("PDFSecurityException:"+e.getMessage());
					imgpaths.setCode(400);
				} catch (Exception e) {
					log.error("失败;", e);
					log.info("Exception:"+e.getMessage());
					log.info(e.getMessage());
					// 结果码，200成功，400错误
					imgpaths.setCode(400);
			}
			}
		}		
		// 保存文件-图片映射路径
		imgpaths.setPaths(filesMap);

		return imgpaths;
	}
	
	@Override
	public ImgResult clearImgbg(String srcPath, String targetPath) {
		//初始化结果
		ImgResult imgResult = new ImgResult(200, targetPath);
		log.info(srcPath);

		FileOutputStream os = null ; 
    	try {
			Image srcImg = ImageIO.read(new File(srcPath));
			byte[] b = ImgAlpha.transferAlpha(srcImg);
			os = new FileOutputStream(targetPath);
			os.write(b);
			
		} catch (Exception e) {
			log.error("图片去背景失败;", e);
			// 结果码，200成功，400错误
			imgResult.setCode(400);
			return imgResult;
		}finally{
			 if(null!=os){
				 try {
					os.close();
				} catch (Exception e2) {
				}
			 }
		}
    	
		return imgResult;
	}

	public static void main(String[] args) throws PDFException,
			PDFSecurityException, IOException {
		String filepath = "";
		float zoom = 1;
		ImgpathService service = new ImgpathServiceImpl();
		service.getPathForPdfToImg(filepath, zoom);
	}
}
