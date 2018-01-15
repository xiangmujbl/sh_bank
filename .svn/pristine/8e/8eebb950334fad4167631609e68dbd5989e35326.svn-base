package com.mmec.util.pdf;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class AddSignField{
	
	//日志服务
	private static Logger  log = Logger.getLogger(PdfUtil.class);
	
	/**
	 * 自定义一次性追加N个签名域
	 * @param src 源文件
	 * @param dest 目标文件
	 * @param list 列表
	 * @throws IOException 
	 */
	public static String addSignFieldFree(String src,String dest,List<Map<String,String>> list){
		try {
			PdfReader reader = new PdfReader(src);
			PdfStamper stamper = new PdfStamper(reader,new FileOutputStream(dest));
			for(int i=0;i<list.size();i++){
				PdfFormField field = PdfFormField.createSignature(stamper.getWriter());
		 		field.setFieldName(String.valueOf(i));
		 		Map<String,String> map=list.get(i);
		 		int page=Integer.valueOf(map.get("page"));
		 		float x=Float.valueOf(map.get("x"));
		 		float y=Float.valueOf(map.get("y"));
		 		int height=Integer.valueOf("height");
		 		int width=Integer.valueOf("width");
		 		float[] position=new float[4];
				position[0]=x;
				position[2]=x+height;
				position[1]=y;
				position[3]=y+width;
				Rectangle r=new Rectangle(
						position[0], position[1],
						position[2], position[3]);
				field.setWidget(r, PdfAnnotation.HIGHLIGHT_PUSH);
		 		field.setFlags(PdfAnnotation.FLAGS_PRINT);
		 		stamper.addAnnotation(field, page);
		 		stamper.close();
		 		reader.close();
			}
			return "";
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	
	/**
	 * 一次性添加签名域(标准版)
	 * @param src 源文件
	 * @param dest 目标文件
	 * @param count 签名域个数
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void addSignFieldOnce(String src,String dest,int count) throws IOException, DocumentException{
 		PdfReader reader = new PdfReader(src);
 		PdfStamper stamper = new PdfStamper(reader,new FileOutputStream(dest));
 		int addP=count/18;
 		stamper.insertPage(reader.getNumberOfPages()+1+addP, PageSize.A4);
 		for(int i=1;i<count+1;i++){
 		PdfFormField field = PdfFormField.createSignature(stamper.getWriter());
 		field.setFieldName(String.valueOf(i));
		//计算坐标位置
 		int para=i-1;
 		int py_x=para%3;
		int py_y=para/3;
		float[] position=new float[4];
		position[0]=60+py_x*160;
		position[2]=230+py_x*160;
		position[1]=720-py_y*80;
		position[3]=780-py_y*80;
		Rectangle r=new Rectangle(
				position[0], position[1],
				position[2], position[3]);
 		field.setWidget(r, PdfAnnotation.HIGHLIGHT_PUSH);
 		field.setFlags(PdfAnnotation.FLAGS_PRINT);
 		stamper.addAnnotation(field, reader.getNumberOfPages()); 
 		}
 		stamper.close();
 		reader.close();
     }
	
	/**
	 * 一次性添加签名域 分金社版
	 * @param src 源文件
	 * @param dest 目标文件
	 * @param count 数目
	 * @param keyword1 -关键词*
	 * @param keyword2 -关键词*
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void addSignFieldOnceFJS(String src,String dest,int count,String keyword1,String keyword2) throws IOException, DocumentException{
    	log.info("开始一次性添加签名域:"+"src:"+src+"dest:"+dest+"count:"+count+"keyword1:"+keyword1+"keyword2:"+keyword2);
 		PdfReader reader = new PdfReader(src);
 		PdfStamper stamper = new PdfStamper(reader,new FileOutputStream(dest));//添加图章
		for(int i=1;i<count+1;i++){
			float page=0;
	 		PdfFormField field = PdfFormField.createSignature(stamper.getWriter());
	 		field.setFieldName(String.valueOf(i));
	 		List<float[]> ff=new ArrayList<float[]>();
	 		float py_x=0;
			float py_y=0;
	 		//静态变量清零
	 		PdfUtil.clearArrays();
	 		if(i==1){
	 			ff =PdfUtil.getKeyWords(src,keyword1);
	 			log.info("ff.toString():"+ff.toString());
	 			page=ff.get(0)[2];
	 			log.info("ff:"+"ff[0]:"+String.valueOf(ff.get(0)[0])+"ff[1]:"+String.valueOf(ff.get(0)[1])+"ff[2]:"+String.valueOf(ff.get(0)[2]));
	 			py_x=ff.get(0)[0];
	 			py_y=ff.get(0)[1]-48;
				float[] position=new float[4];
				position[0]=py_x;
				position[2]=py_x+200;
				position[1]=py_y;
				position[3]=py_y+40;
				Rectangle r=new Rectangle(
						position[0], position[1],
						position[2], position[3]);
		 		field.setWidget(r, PdfAnnotation.HIGHLIGHT_OUTLINE);
		 		field.setFlags(PdfAnnotation.FLAGS_PRINT);
		 		//静态变量清零
		 		PdfUtil.clearArrays();
	 		}else{
	 			ff=PdfUtil.getKeyWords(src, keyword2);
	 			log.info("ff.toString():"+ff.toString());
	 			page=ff.get(1)[2];
	 			log.info("ffelse:"+"ff[0]:"+String.valueOf(ff.get(i-1)[0])+"ff[1]:"+String.valueOf(ff.get(i-1)[1])+"ff[2]:"+String.valueOf(ff.get(i-1)[2]));
	 			py_x=ff.get(1)[0];
	 			py_y=ff.get(1)[1]-48;
				float[] position=new float[4];
				position[0]=py_x;
				position[2]=py_x+111;
				position[1]=py_y;
				position[3]=py_y+111;
				Rectangle r=new Rectangle(
						position[0], position[1],
						position[2], position[3]);
				//边框 标注
		 		field.setWidget(r, PdfAnnotation.HIGHLIGHT_PUSH);
		 		field.setFlags(PdfAnnotation.FLAGS_PRINT);
		 		//静态变量清零
		 		PdfUtil.clearArrays();
	 		}
	 		stamper.addAnnotation(field, (int)page);
	 		}
 		stamper.close();
 		reader.close();
 		log.info("分金社添加签名域成功");
     }
	
	/**
	 * 一次性添加签名域 人人行版
	 * @param src 源文件
	 * @param dest 目标文件
	 * @param count 数目值
	 * @param keyword1 关键词*1
	 * @param keyword2 关键词*2
	 * @param keyword3 关键词 *3
	 * @throws IOException
	 * @throws DocumentException
	 */
    public static void addSignFieldOnceRenrx(String src,String dest,int count,String keyword1,String keyword2,String keyword3) throws IOException, DocumentException{
     	log.info("开始一次性添加签名域:"+"src:"+src+"dest:"+dest+"count:"+count+"keyword1:"+keyword1+"keyword2:"+keyword2);
  		PdfReader reader = new PdfReader(src);
  		PdfStamper stamper = new PdfStamper(reader,new FileOutputStream(dest));//添加图章
 		for(int i=1;i<count+1;i++)
 		{
 			float page=0;
 	 		PdfFormField field = PdfFormField.createSignature(stamper.getWriter());
 	 		field.setFieldName(String.valueOf(i));
 	 		List<float[]> ff=new ArrayList<float[]>();
 	 		float py_x=0;
 			float py_y=0;
 	 		//静态变量清零
 	 		PdfUtil.clearArrays();
 	 		if(i==1){
 	 			ff = PdfUtil.getKeyWords(src,keyword1);
 	 			log.info("ff.toString():"+ff.toString());
 	 			page=ff.get(0)[2];
 	 			log.info("11111====================="+ page);
 	 			log.info("ff:"+"ff[0]:"+String.valueOf(ff.get(0)[0])+"ff[1]:"+String.valueOf(ff.get(0)[1])+"ff[2]:"+String.valueOf(ff.get(0)[2]));
 	 			py_x=ff.get(0)[0];
 	 			py_y=ff.get(0)[1]-48;
 				float[] position=new float[4];
 				position[0]=py_x;
 				position[2]=py_x+200;
 				position[1]=py_y;
 				position[3]=py_y+40;
 				Rectangle r=new Rectangle(
 						position[0], position[1],
 						position[2], position[3]);
 		 		field.setWidget(r, PdfAnnotation.HIGHLIGHT_OUTLINE);
 		 		field.setFlags(PdfAnnotation.FLAGS_PRINT);
 		 		//静态变量清零
 		 		PdfUtil.clearArrays();
 		 		stamper.addAnnotation(field, (int)page);
 	 		}
 	 		else if (i==2)
 	 		{
 	 			ff = PdfUtil.getKeyWords(src,keyword2);
 	 			log.info("ff.toString():"+ff.toString());
 	 			page=ff.get(1)[2];
 	 			log.info("22222====================="+ page);
 	 			log.info("ff:"+"ff[0]:"+String.valueOf(ff.get(0)[0])+"ff[1]:"+String.valueOf(ff.get(0)[1])+"ff[2]:"+String.valueOf(ff.get(0)[2]));
 	 			py_x=ff.get(1)[0];
 	 			py_y=ff.get(1)[1]-48;
 				float[] position=new float[4];
 				position[0]=py_x;
 				position[2]=py_x+200;
 				position[1]=py_y;
 				position[3]=py_y+40;
 				Rectangle r=new Rectangle(
 						position[0], position[1],
 						position[2], position[3]);
 		 		field.setWidget(r, PdfAnnotation.HIGHLIGHT_OUTLINE);
 		 		field.setFlags(PdfAnnotation.FLAGS_PRINT);
 		 		//静态变量清零
 		 		PdfUtil.clearArrays();
 		 		stamper.addAnnotation(field, (int)page);
 	 		}
 	 		else{
 	 			ff = PdfUtil.getKeyWords(src, keyword3);
 	 			log.info("ff.toString():"+ff.toString());
 	 			page=ff.get(2)[2];
 	 			log.info("33333====================="+ page);
 	 			log.info("ffelse:"+"ff[0]:"+String.valueOf(ff.get(i-1)[0])+"ff[1]:"+String.valueOf(ff.get(i-1)[1])+"ff[2]:"+String.valueOf(ff.get(i-1)[2]));
 	 			py_x=ff.get(2)[0];
 	 			py_y=ff.get(2)[1]-48;
 				float[] position=new float[4];
 				position[0]=py_x;
 				position[2]=py_x+111;
 				position[1]=py_y;
 				position[3]=py_y+111;
 				Rectangle r=new Rectangle(
 						position[0], position[1],
 						position[2], position[3]);
 				//边框 标注
 		 		field.setWidget(r, PdfAnnotation.HIGHLIGHT_PUSH);
 		 		field.setFlags(PdfAnnotation.FLAGS_PRINT);
 		 		//静态变量清零
 		 		PdfUtil.clearArrays();
 		 		stamper.addAnnotation(field, (int)page);
 	 		}
 	 		}
  		stamper.close();
  		reader.close();
  		log.info("分金社添加签名域成功");
      }
    
    public static List<String> checkSignName(PdfReader reader){
    	AcroFields fields = reader.getAcroFields();
 		ArrayList<String> names = fields.getSignatureNames();
 		return names;
    }
}