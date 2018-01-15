package com.mmec.util;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.rowset.SqlRowSet;


/**
 * 分页代码
 */
public class PageMaimai implements Serializable{

	private static Log log = LogFactory.getLog(PageMaimai.class);

	private int curPageNo = 0; // 当前页数，从0开始

	private int size = 0; // 所有数据条数

	private String url; // 页面跳转的路径

	private List showList; // 当前页面需要显示的数据列表

	private int pageSize = 10;// 每页显示的数据条数

	private int groupSize = 1;// 多少页为一组

	//private String pageNavigation;// 导航条

	/**
	 * 每次通过sql语句从数据库里面分组取出需要显示的数据
	 * 
	 * @param request javax.servlet.http.HttpServletRequest对象
	 * @param sql String 查询数据库的sql语句
	 * @param pageSize int 每页显示的条数
	 * @param groupSize int 分成多少组
	 * @param url String 页面跳转的路径，若没有特殊的参数传递，可以传入null或""，
	 *            如是在aciton里面调用，并且action是继承自DispatherAction的话最好传入完整的路径
	 */
	public void init(HttpServletRequest request, String sql, int pageSize,
			int groupSize, int pageNo, String url) {
		// 上一页、下一页跳转路径
		if (url != null) {
			this.url = url;
		} else {
			this.url = request.getRequestURL() + "";
		}
		if (pageSize > 0)
			this.pageSize = pageSize;// 每页多少条记录
		if (groupSize > 0)
			this.groupSize = groupSize;

		// 当前第几页
		if (pageNo < 0) {
			this.curPageNo = 0;
		} else {
			this.curPageNo = pageNo;
		}

		int curGroup = this.curPageNo / this.groupSize + 1;

		// 是否是新的一组数据，如果是则到数据库取数据
		this.size = parseInt(request.getSession().getAttribute("page_all_size")
				+ "", 0);
		if (this.curPageNo % this.groupSize == 0
				|| (request.getSession().getAttribute("cur_group") != null && parseInt(
						"" + request.getSession().getAttribute("cur_group"), 1) != curGroup)
				|| this.size == 0 || request.getParameter("reload") != null) {

			request.getSession().setAttribute("cur_group", curGroup);

			if (pageNo > 0
					&& request.getSession().getAttribute("page_sql") != null) {
				sql = request.getSession().getAttribute("page_sql") + "";
			} else {
				request.getSession().setAttribute("page_sql", sql);
			}

			this.size = getTotalCount(sql);
			List list = getPageData(sql, (this.curPageNo / this.groupSize)
					* this.pageSize * this.groupSize, this.pageSize
					* this.groupSize);
			request.getSession().setAttribute("page_all_size", this.size);
			request.getSession().setAttribute("page_cur_list", list);
			this.setShowList(list);// 设置页面上的显示数据
		} else {
			this.setShowList((List) request.getSession().getAttribute(
					"page_cur_list"));// 设置页面上的显示数据
		}
	}

	/**
	 * 每次通过sql语句从数据库里面分组取出需要显示的数据
	 * 
	 * @param request
	 *            javax.servlet.http.HttpServletRequest对象
	 * @param sql
	 *            String 查询数据库的sql语句
	 * @param pageSize
	 *            int 每页显示的条数
	 * @param groupSize
	 *            int 分成多少组
	 * @param url
	 *            String 页面跳转的路径，若没有特殊的参数传递，可以传入null或""，
	 *            如是在aciton里面调用，并且action是继承自DispatherAction的话最好传入完整的路径
	 */
	public void init(HttpServletRequest request, String sql, int pageSize,
			int groupSize, String url) {
		// 当前第几页
		String curPage = request.getParameter("pageNo");

		init(request, sql, pageSize, groupSize, parseInt(curPage, -1), url);
	}

	/**
	 * 每次通过sql语句从数据库里面分组取出需要显示的数据
	 * 
	 * @param request
	 *            javax.servlet.http.HttpServletRequest对象
	 * @param sql
	 *            String 查询数据库的sql语句
	 * @param pageSize
	 *            int 每页显示的条数
	 * @param groupSize
	 *            int 分成多少组
	 * @param url
	 *            String 页面跳转的路径，若没有特殊的参数传递，可以传入null或""，
	 *            如是在aciton里面调用，并且action是继承自DispatherAction的话最好传入完整的路径
	 */
	public void init(HttpServletRequest request, String sql, int pageSize,
			int groupSize, int pageNo) {
		init(request, sql, pageSize, groupSize, pageNo, "");
	}

	/**
	 * 这种方式是一次性把所有的数据取出来，再分页显示
	 * 
	 * 只需传递一次数据，会有一个pageNo的参数进行传递
	 * 
	 * 这种方法在jsp页面上使用很方便，但是在servlet或action里面使用时，要达到减少数据库访问的目的，则需先判断是否执行查询数据库操作，如下使用：
	 * 
	 * List list = null; if(request.getParameter("pageNo") == null ||
	 * request.getParameter("pageNo").equals("null")){ list =
	 * dao.getDataFromDb();//调用自己的方法操作数据库，从数据库里面取出数据 } CutPage cp = new
	 * CupPage(request,list,20,"");
	 * 
	 * @param request
	 *            javax.servlet.http.HttpServletRequest对象
	 * @param allList
	 *            传进来的所有数据，第一次会放把数据放在session里面，以后在session中取数据
	 * @param pageSize
	 *            每页显示的条数
	 * @param url
	 *            页面跳转的路径，若没有特殊的参数传递，可以赋成null或""，
	 *            如是在aciton里面调用，并且action是继承自DispatherAction的话最好传入完整的路径
	 */
	public void init(HttpServletRequest request, List allList, int pageSize,
			String url) {
		String page_num = request.getParameter("pageNo");

		this.curPageNo = parseInt(page_num, 0);// 当前页码

		List list = new ArrayList();
		if (allList != null) {
			list = allList;
			request.getSession().setAttribute("all_List", list);
		} else {
			list = (List) request.getSession().getAttribute("all_List");
			if (list == null) {
				list = new ArrayList();
			}
		}

		this.size = list.size();// 总的数据条数
		if (url != null) {
			this.url = url;
		} else {
			this.url = request.getRequestURL() + "";
		}
		if (pageSize > 0)
			this.pageSize = pageSize;// 每页显示的条数

		setShowList(list);// 设置显示数据
	}

	/**
	 * 返回分页导航条
	 * 
	 * @return pageNavigation String 分页导航条
	 */
	public String getPageNavigation() {
		// 最终返回的分页导航条
		String pageNavigation = "共有" + size + "条数据&nbsp;&nbsp;";

		// 记录数超过一页,需要分页
		if (size > pageSize) {
			if (url != null && !"".equals(url)) {
				if (url.indexOf("?") > -1) {
					// 如果url中已经包含了其他的参数,就把curPageNo参数接在后面
					url += "&";
				} else {
					// 如果url中没有别的参数
					url += "?";
				}
				// 生成一个提交页面的函数
				pageNavigation += "<script>";
				pageNavigation += "function gotoPage(page_num){";
				pageNavigation += "location.href='" + url
						+ "pageNo='+page_num;";
				pageNavigation += "}</script>";
			}
			pageNavigation += "每页"
					+ pageSize
					+ "条&nbsp;&nbsp;"
					+ "当前第<select name='pageNo' id='pageNo' onchange='javascript:gotoPage(this.value)'>";
			int curPageNos = size % pageSize == 0 ? size / pageSize : size
					/ pageSize + 1;
			for (int i = 0; i < curPageNos; i++) {
				if (i == curPageNo) {
					pageNavigation += "<option value='" + i + "' selected>"
							+ (i + 1) + "</option>";
				} else {
					pageNavigation += "<option value='" + i + "'>" + (i + 1)
							+ "</option>";
				}
			}
			pageNavigation += "</select>页&nbsp;&nbsp;共" + curPageNos
					+ "页&nbsp;&nbsp;";
			// 如果不是第一页,导航条将包含"首页"和"上一页"的连接
			if (curPageNo > 0) {
				pageNavigation += "[<a href=\"javascript:void(0);\" onclick=\"gotoPage(0);return false;\">首页</a>]&nbsp;"
						+ "[<a href=\"javascript:void(0);\" onclick=\"gotoPage("
						+ (curPageNo - 1) + ");return false;\">上一页</a>]&nbsp;&nbsp;";
			} else {
				pageNavigation += "[首页]&nbsp;[上一页]&nbsp;&nbsp;";
			}
			// 如果不是最后一页,导航条将包含"末页"和"下一页"
			if (curPageNo < curPageNos - 1) {
				pageNavigation += "[<a href=\"javascript:void(0);\" onclick=\"gotoPage("
						+ (curPageNo + 1)
						+ ");return false;\">下一页</a>]&nbsp;"
						+ "[<a href=\"javascript:void(0);\" onclick=\"gotoPage("
						+ (curPageNos - 1) + ");return false;\">末页</a>]";
			} else {
				pageNavigation += "[下一页]&nbsp;[末页]";
			}
		}

		return pageNavigation;
	}

	/**
	 * 返回分页后的总页数
	 * 
	 * @return pagecount int 总页数
	 */
	public int getPageCount() {
		int pagecount = 0;
		if (size % pageSize == 0) {
			pagecount = size / pageSize;
		} else {
			pagecount = size / pageSize + 1;
		}
		return pagecount;
	}

	/**
	 * 返回最后一页的记录数
	 * 
	 * @return lastpagesize int 最后一页的记录数
	 */
	public int getLastPageCount() {
		int lastpagesize = 0;
		if (size % pageSize == 0) {
			lastpagesize = pageSize;
		} else {
			lastpagesize = size % pageSize;
		}
		return lastpagesize;
	}

	// 设置显示的记录列表
	private void setShowList(List list) {
		log.info("$$totalSize=" + this.size + "; curPageNo=" + this.curPageNo
				+ "; pageSize=" + this.pageSize + "; groupSize="
				+ this.groupSize);
		if (list == null) {
			list = new ArrayList();
		}
		if (pageSize >= list.size()) {
			this.showList = list;
		} else {
			if (groupSize <= 1) {
				groupSize = 1;
				if (pageSize * (curPageNo + 1) > list.size()) {
					if (pageSize * curPageNo > list.size()) {
						this.showList = list.subList(list.size() - pageSize,
								list.size());// 返回最后一页的数据
					} else {
						this.showList = list.subList(pageSize * curPageNo, list
								.size());
					}
				} else {
					this.showList = list.subList(pageSize * curPageNo, pageSize
							* (curPageNo + 1));
				}
			} else {
				if (pageSize * ((curPageNo % groupSize) + 1) > list.size()) {
					this.showList = list.subList(pageSize
							* (curPageNo % groupSize), list.size());
				} else {
					this.showList = list.subList(pageSize
							* (curPageNo % groupSize), pageSize
							* ((curPageNo % groupSize) + 1));
				}
			}
		}
	}

	public List getShowList() {
		return showList;
	}

	public int getCurPageNo() {
		return curPageNo;
	}

	public void setCurPageNo(int curPageNo) {
		this.curPageNo = curPageNo;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getGroupSize() {
		return groupSize;
	}

	public void setGroupSize(int groupSize) {
		this.groupSize = groupSize;
	}

	private int parseInt(Object s, int defaultValue) {
		if (s != null && s.toString().matches("\\d+")) {
			return Integer.parseInt(s.toString());
		} else {
			return defaultValue;
		}
	}

	/**
	 * 拆解简单sql type:1-取from，2-取where，3-取orderby
	 */
	private String parseHql(String sql, int type) {
		switch (type) {
		case 1:
			if (sql.indexOf("where") > 0) {
				return sql.substring(0, sql.indexOf("where") - 1);
			} else if (sql.indexOf("order by") > 0) {
				return sql.substring(0, sql.indexOf("order by") - 1);
			} else {
				return null;
			}
		case 2:
			if (sql.indexOf("where") > 0) {
				if (sql.indexOf("order by") > 0) {
					return sql.substring(sql.indexOf("where"), sql
							.indexOf("order by") - 1);
				} else {
					return sql.substring(sql.indexOf("where"));
				}
			} else {
				return null;
			}
		case 3:
			if (sql.indexOf("order by") > 0) {
				return sql.substring(sql.indexOf("order by"));
			} else {
				return null;
			}
		}

		return null;
	}

	/**
	 * 获取总记录条数
	 * 
	 * @param sql
	 * @return
	 */
	private int getTotalCount(String sql) {
		log.debug("query sql：" + sql);
		String from = parseHql(sql, 1);
		String where = parseHql(sql, 2);
		log.debug("parse sql result - from:" + from);
		log.debug("parse sql result - where:" + where);
		if (from == null) {
			log.error(">sql 无效：" + sql);
			return 0;
		}

		if (where == null)
			where = "";

		try {
			String fromTrim = from.substring(from.indexOf("from"));
			String s = "select count(1) " + fromTrim + " " + where;
			log.debug("get total count sql：" + s);

			SqlRowSet set = getSpringJdbcTemplate().queryForRowSet(s);
			return set.getInt(0);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 通过sql查询一页的数据
	 * 
	 * @param sql
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	private List getPageData(final String sql, final int firstResult,
			final int maxResults) {
		log.debug("$$sql=" + sql + "; firstResult=" + firstResult
				+ "; maxResults=" + maxResults);
		try {
			JdbcTemplate jt = getSpringJdbcTemplate();
			return queryForList(sql + " limit " + firstResult + ","
					+ maxResults, jt);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList();
	}

	public static List queryForList(String sql, JdbcTemplate jt) {
		log.debug("SpringJdbcTemplate:" + sql);
		final List list = new ArrayList();
		try {
			jt.query(sql, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					HashMap m = new HashMap();
					ResultSetMetaData meta = rs.getMetaData();
					if (meta != null) {
						int colCount = meta.getColumnCount();
						for (int i = 1; i <= colCount; i++) {
							String fieldName = meta.getColumnName(i);
							Object fieldValue = rs.getString(fieldName);

							m.put(fieldName, fieldValue);
							fieldName = null;
							fieldValue = null;
						}
					}
					list.add(m);
					m = null;
					meta = null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	* 需要实现此方法，返回一个SpringJdbcTemplate对象
	*/
	private JdbcTemplate getSpringJdbcTemplate() {
		//TODO
		return null;
	}
}
