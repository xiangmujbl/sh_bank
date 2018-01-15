package com.mmec.util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.awt.geom.Rectangle2D.Float;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

public class PdfUtilNew {
    /*
     * 自下而上，从右到左
     * @param filePath pdf文件
     * @param keyWord 在模板里做的关键字
     * 返回值为一个数组 分别为x轴,y轴,坐标在文档中的页数(x,y和创建签名域的一样)  57.832397----283.78784-----3.0  
     */
	private String retStr;
	
	
    public List<tempObject> searchKeyWords(String filePath, String keyWord)
    {
    	List<tempObject>  oo = new ArrayList<tempObject> ();
    	List<tempObject>  retObj = new ArrayList<tempObject> ();
        try {
            PdfReader pdfReader = new PdfReader(filePath);
            int pageNum = pdfReader.getNumberOfPages();
            PdfReaderContentParser pdfReaderContentParser = new PdfReaderContentParser(pdfReader);
            for (int i = 1; i < pageNum+1; i++) {
            	PdfUtil2 p2 = new PdfUtil2(); 
            	p2.setKeyWord(keyWord);
            	p2.setNumber(i);
                pdfReaderContentParser.processContent(i,p2);
                oo =  p2.getArrays();
                for(int j = 0;j < oo.size();)
                {
                	if(oo.get(j).getChars() == null){
                		oo.remove(j);
                		j = 0;
                	}else{
                		j++;
                	}
                }
                for(int j = 0;j<oo.size();j++){
	                
	                if(oo.get(j).getChars().contains(keyWord))
	                {
//	                	System.out.println(oo.get(j).getChars());
//	                	float ff[] = oo.get(j).getTempFloat();
	                	retObj.add(oo.get(j));
//	                	System.out.println(ff[0]+"----"+ff[1]+"-----"+ff[2]);
	                }
//	                for(int k = 0;k < oo.get(j).getTempFloat().length;k++){
//	                	System.out.println(oo.get(j).getTempFloat()[k]);
//	                }                
                }
            }
            pdfReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retObj;
    }
    
    
   public List<float[]> getKeyWords(String filePath, String keyWord)
   {
	   	List<float[]> list = new ArrayList<float[]>();
	   	PdfUtilNew p = new PdfUtilNew();
	   	List<tempObject> fff = p.searchKeyWords(filePath,keyWord);
	   	for (int i=0;i<fff.size();i++) 
	   	{
	   		if(fff.get(i).getChars().contains(keyWord))
	   		{
	   			float ff[] = fff.get(i).getTempFloat();
	   			list.add(ff);
  		 }
  	   }
	   	return list;
   }

//    public static void main(String[] args) {
//    	PdfUtil p = new PdfUtil();
//    	 List<float[]> fff = p.getKeyWords("D:/20160912151631772.pdf","代表");
////    	 System.out.println(fff.size());
//    	 for (float[] ff : fff) {
////			System.out.println(tempObject.getTempFloat());
////    		 float [] ff = tempObject.getTempFloat();
////    		 System.out.println(tempObject.getChars());
//    		 System.out.println(ff[0]+"----"+ff[1]+"-----"+ff[2]);
//		}
//    }

}
class PdfUtil2 implements RenderListener{

	private static final Object[] String = null;
	private int number;
	private String keyWord ;
   
	private List<tempObject> arrays = new ArrayList<tempObject> ();
	
	private String retText;
    
	public String getRetText() {
		return retText;
	}
	public void setRetText(String retText) {
		this.retText = retText;
	}
	
	public int getNumber() {
		return number;
	}
	public List<tempObject> getArrays() {
		return arrays;
	}
	public void setArrays(List<tempObject> arrays) {
		this.arrays = arrays;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	StringBuffer sb = new StringBuffer();
    public void renderText(TextRenderInfo textRenderInfo) {
        String text = textRenderInfo.getText(); // 整页内容
        
        String re[]=text.split("\\t");
        text = "";
        for(int i = 0;i < re.length;i++){
        	text += re[i];
        }

        re=text.split("\\r");
        text = "";
        for(int i = 0;i < re.length;i++){
        	text += re[i];
        }

        re=text.split("\\n");
        text = "";
        for(int i = 0;i < re.length;i++){
        	text += re[i];
        }
        
        re=text.split("\\u0020");
        text = "";
        for(int i = 0;i < re.length;i++){
        	text += re[i];
        }
        
        re=text.split("\\u00C2");
        text = "";
        for(int i = 0;i < re.length;i++){
        	text += re[i];
        }
        
        re=text.split("\\u00A0");
        text = "";
        for(int i = 0;i < re.length;i++){
        	text += re[i];
        }        

        if (null != text)
        {
        	Float boundingRectange = textRenderInfo.getBaseline().getBoundingRectange();
            if(!"".equals(text)){
	    		if(arrays.size() == 0){
		            tempObject temp = new tempObject();
		            temp.setTempFloat(new float[3]);
		            temp.getTempFloat()[0] = boundingRectange.x;
		            temp.getTempFloat()[1] = boundingRectange.y;
		            temp.getTempFloat()[2] = number;
		            arrays.add(temp);
		            if(!"".equals(StringUtil.nullToString(text)))
		            {
		            	if(null != temp.getChars())
		            	{	
		            		temp.setChars(temp.getChars()+text);
		            	}else
		            	{
		            		temp.setChars(text);
		            	}
		            }
	    		}
	    		if(arrays.size() > 0)
	    		{
	    			tempObject temp = arrays.get(arrays.size() -1);
	        		if(null != temp.getChars())
	            	{	
	            		temp.setChars(temp.getChars()+text);
	            	}else
	            	{
	            		temp.setChars(text);
	            	}
		            temp.getTempFloat()[0] = boundingRectange.x;
		            temp.getTempFloat()[1] = boundingRectange.y;
		            temp.getTempFloat()[2] = number;
	    		}
	    		if(text.equals(keyWord)){
	    			tempObject temp = arrays.get(arrays.size() -1);
	        		if(null != temp.getChars())
	            	{	
	            		temp.setChars(temp.getChars()+text);
	            	}else
	            	{
	            		temp.setChars(text);
	            	}
		            temp.getTempFloat()[0] = boundingRectange.x;
		            temp.getTempFloat()[1] = boundingRectange.y;
		            temp.getTempFloat()[2] = number;
		            
		            tempObject ntemp = new tempObject();
		            ntemp.setTempFloat(new float[4]);
		            arrays.add(ntemp);
	    		}
	    	}else{
	            tempObject temp = new tempObject();
	            temp.setTempFloat(new float[4]);
	            arrays.add(temp);
	    	}
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
}