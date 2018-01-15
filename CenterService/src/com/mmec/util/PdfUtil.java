package com.mmec.util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.itextpdf.awt.geom.Rectangle2D.Float;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

public class PdfUtil {

    private static int i = 0;
    static List<float[]>  arrays = new ArrayList<float[]> ();
    private static Logger  log = Logger.getLogger(PdfUtil.class);
    /**
     * 数组静态变量清0
     */
    public static void clearArrays(){
    	arrays.clear();
    }
    
    /*
     * 自下而上，从右到左
     * @param filePath pdf文件
     * @param keyWord 在模板里做的关键字
     * 返回值为一个数组 分别为x轴,y轴,坐标在文档中的页数(x,y和创建签名域的一样)  57.832397----283.78784-----3.0  
     */
    public static List<float[]> getKeyWords(String filePath, final String keyWord)
    {
        try {
            PdfReader pdfReader = new PdfReader(filePath);
            int pageNum = pdfReader.getNumberOfPages();
            PdfReaderContentParser pdfReaderContentParser = new PdfReaderContentParser(
                    pdfReader);
            for (i = 1; i < pageNum+1; i++) {
                pdfReaderContentParser.processContent(i, new RenderListener() {
                	 @Override 
                    public void renderText(TextRenderInfo textRenderInfo) {
                        String text = textRenderInfo.getText(); // 整页内容
//                        System.out.println(text);
//                        PdfString s = textRenderInfo.getPdfString();
                        if (null != text && text.contains(keyWord)) {
                            Float boundingRectange = textRenderInfo.getBaseline().getBoundingRectange();
//                            sb = boundingRectange.x+"--"+boundingRectange.y+"---";
//                            a =keyWord;
                            float[] resu = new float[4];
                            resu[0] = boundingRectange.x;
                            resu[1] = boundingRectange.y;
                            resu[2] = i;
                            arrays.add(resu);
                        }
                    }

                    @Override
                    public void renderImage(ImageRenderInfo arg0) {
                       

                    }

                    @Override
                    public void endTextBlock() {
                        
                    }

                    @Override
                    public void beginTextBlock() {
                      
                    }					
                });
            }
           // arrays.add(a);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        log.info(keyWord);
        return arrays;
    }

    public static void main(String[] args) {

        List<float[]> ff = getKeyWords("D:/20160714202548914.pdf","*");
        for(float[] f : ff){
            System.out.println(f[0]+"----"+f[1]+"-----"+f[2]);
        }
     }

}
