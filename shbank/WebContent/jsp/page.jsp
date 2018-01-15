<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<%
//int s = (int)request.getAttribute("totalCount");
int totalCount = Integer.parseInt(request.getAttribute("totalCount").toString());
//System.out.println("totalCount======"+totalCount);
%>

<input type="hidden" name="pagedivcontent" value=""/>
<input type="hidden" name="currPage" value=""/>
<input type="hidden" id="maxpage" name="maxPage" value="<%=totalCount %>"/>
<div id="divpageid" class="page">

<!--<a href="javascript:;" class="anprevpage antrue">上一页</a>-->

<%

if (null == request.getParameter("pagedivcontent") || "".equals(request.getParameter("pagedivcontent")))
{
	int pagelength = 5;
	if (totalCount < 5)
	{
		pagelength = totalCount;
	}
	
%>
	<%
	if (totalCount != 1 && totalCount != 0)
	{
	%>
	<span class="astyle acurrent" id="pageNum" onclick="pageOK();">1</span>
	<%
	}%>
<%
	for (int i =1; i < pagelength; i++)
	{
		
%>
		<a href="javascript:;pageOK();" class="astyle"><%=i+1 %></a>
<%
		
	}
%>

	<%if (totalCount != 1 && totalCount != 0)
	  {
	 %>
	 	<span>...</span>

    	<a href="javascript:;pageOK();" class="anextpage annext">下一页</a>
    	<span>共<%=totalCount %>页</span>
    <%} %>
    
    
<%
}
else
{
%>
	<%=request.getParameter("pagedivcontent") %>
<%
}
%>
   
</div>


<script type="text/javascript">

	//点击下一页的效果
    $('.page > a.anextpage').click(function(){
    	var maxpage = document.getElementById("maxpage").value;
        var _this   =   $(this);
       // var _page   =   _this.html();

        var _snum   =   _this.parent('div.page').find('span.astyle.acurrent').html();
        var _page   =   parseInt(_snum)+1;
        if (_page>maxpage)
        {
        	return;
        }
        
        var _span   =   '<span class="astyle acurrent" id="pageNum" onclick="pageOK();">'+_page+'</span>';
        var _a      =   '<a href="javascript:void(0)" class="astyle">'+_snum+'</a>';
        var _point  =   '<span class="pointer">...</span>';
        if(_snum>8){
            var len=$('.page > span').length;
            $('.page').find('a.pointer').eq(i).css('display','none');
            for(var i=2;i<(_snum-8);i++){
                $('.page').find('a').eq(i).css('display','none');
              //  $('.page').find('a').eq('3').css('display','none');
            }
            if(len<4){
            $('.page').find('a').eq('1').after(_point);}
            }
        _this.parent('div.page').find('span.astyle.acurrent').before(_a);
        _this.parent('div.page').find('span.astyle.acurrent').after(_span);
        _this.parent('div.page').find('span.astyle.acurrent').eq(0).detach();
        _this.parent('div.page').find('span.astyle.acurrent').next('a.astyle').detach();
       
        pageOK();
    })

    //翻页效果
    $(document).on('click','a.astyle',function(){
        var _this   =   $(this);
        var _page   =   _this.html();
        var _span   =   '<span class="astyle acurrent" id="pageNum" onclick="pageOK();">'+_page+'</span>';
        var _snum   =   _this.parent('div.page').find('span.astyle.acurrent').html();
        var _a      =   '<a href="javascript:void(0)" class="astyle">'+_snum+'</a>';
        var _point  =   '<span class="pointer">....</span>';
        _this.parent('div.page').find('span.astyle.acurrent').before(_a);
        _this.parent('div.page').find('span.astyle.acurrent').detach();
        _this.before(_span);
        _this.remove();
        if(_page<8){
            var len=$('.page > a.astyle').length;

            $('.page').find('a.astyle').css('display','inline-block');
            for(var i=11;i<len;i++){

                $('.page').find('a.astyle').eq(i).css('display','none');
//                console.log($('.page').find('a.astyle').eq(i));
            }
                $('.page').find('span.pointer').detach();

        }
        pageOK();

    });

     	function pageOK()
     	{
     		/*var contractTitle = form1.contractTitle.value;
    		if ("输入合同名称" == contractTitle)
   			{
    			form1.contractTitle.value = "";
   			}*/

    		form1.pagedivcontent.value=document.getElementById("divpageid").innerHTML;
     		form1.currPage.value=document.getElementById("pageNum").innerHTML-1;
     		//form1.pagedivcontent.value="";
     		//alert(form1.pagedivcontent.value);
     		form1.submit();
     	}
</script>
