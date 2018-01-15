package com.mmec.util;

import org.apache.log4j.Logger;

/*
 * select * from tableName limit (currentPage-1)*pagesize,pagesize
pageNow：当前第几页
pageSize：每页显示的记录数
 */
public class PageUtil {
	private static Logger  log = Logger.getLogger(PageUtil.class);
	private int pageSize;//每页显示的条数
    private int recordCount;//总共的条数
    private int currentPage;//当前页面
    public PageUtil(int pageSize, int recordCount, int currentPage){
            this.pageSize = pageSize;
            this.recordCount = recordCount;
            setCurrentPage(currentPage);
        }
         //构造方法
        public PageUtil(int pageSize, int recordCount){
            this(pageSize, recordCount, 1);
        }
        //总页数
        public int getPageCount(){
            int size = recordCount/pageSize;//总条数/每页显示的条数=总页数
            int mod = recordCount % pageSize;//最后一页的条数
            if(mod != 0)
                size++;
            return recordCount == 0 ? 1 : size;
        }
        //包含，起始索引为0
        public int getFromIndex(){
            //log.info("from index:"+(currentPage-1) * pageSize);
            return (currentPage-1) * pageSize;
        }
        //不包含
        public int getToIndex(){
            //log.info("to index:"+Math.min(recordCount, currentPage * pageSize));
            return  Math.min(recordCount, currentPage * pageSize);
        }
        //得到当前页
        public int getCurrentPage(){
            return currentPage;
        }//设置当前页
        public void setCurrentPage(int currentPage){
            int validPage = currentPage <= 0 ? 1 : currentPage;
            validPage = validPage > getPageCount() ? getPageCount() : validPage;
            this.currentPage = validPage;
        }//得到每页显示的条数
        public int getPageSize(){
            return pageSize;
        }//设置每页显示的条数
        public void setPageSize(int pageSize){
            this.pageSize = pageSize;
        }//得到总共的条数
        public int getRecordCount(){
            return recordCount;
        }//设置总共的条数
        public void setRecordCount(int recordCount){
            this.recordCount = recordCount;
        } 
}
