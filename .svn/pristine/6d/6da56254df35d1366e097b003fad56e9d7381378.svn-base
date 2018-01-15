package com.mmec.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GetAllFiles {
	private Inner inner = null;
	private List<File> list = new ArrayList<File>();

	public GetAllFiles() {
	}
	
	public class Inner {
		public Inner() {
		}

		public void getAllFiles(File dir, int level) {
			//System.out.println(getLevel(level)+dir.getName());
			level++;
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					//这里面用了递归的算法
					getAllFiles(files[i], level);
				} else {
					//System.out.println(getLevel(level)+files[i]);
					//System.out.println(files[i]);
					list.add(files[i]);
				}
			}
		}
	}
	//获取层级的方法
    public String getLevel(int level)
    {
      //A mutable sequence of characters.
      StringBuilder sb=new StringBuilder();
      for(int l=0;l<level;l++)
      {
        sb.append("|--");
      }
      return sb.toString();
    }
	public Inner getInnerInstance() {
		if (inner == null)
			inner = new Inner();
		return inner;
	}
	
	public List<File> getList() {
		return list;
	}

	public void setList(List<File> list) {
		this.list = list;
	}

	public static void main(String[] args) {
		// 第一种方式：
		GetAllFiles outter = new GetAllFiles();
		GetAllFiles.Inner inner = outter.new Inner(); // 必须通过Outter对象来创建
		File dir = new File("F:\\office\\CPC024422449044526");
		inner.getAllFiles(dir, 0);
		System.out.println(outter.list.size());
		// 第二种方式：
//		GetAllFiles.Inner inner1 = outter.getInnerInstance();
	}
}
