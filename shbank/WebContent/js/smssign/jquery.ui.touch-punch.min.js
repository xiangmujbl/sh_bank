/*!
 * jQuery UI Touch Punch 0.2.3
 *
 * Copyright 2011–2014, Dave Furfero
 * Dual licensed under the MIT or GPL Version 2 licenses.
 *
 * Depends:
 *  jquery.ui.widget.js
 *  jquery.ui.mouse.js
 */
!(function(b){function c(a,b){if(!(1<a.originalEvent.touches.length)){a.preventDefault();var c=a.originalEvent.changedTouches[0],d=document.createEvent("MouseEvents");d.initMouseEvent(b,!0,!0,window,1,c.screenX,c.screenY,c.clientX,c.clientY,!1,!1,!1,!1,0,null);a.target.dispatchEvent(d)}}b.support.touch="ontouchend"in document;if(b.support.touch){var d=b.ui.mouse.prototype,f=d._mouseInit,g=d._mouseDestroy,e;d._touchStart=function(a){!e&&this._mouseCapture(a.originalEvent.changedTouches[0])&&(e=!0,this._touchMoved=
!1,c(a,"mouseover"),c(a,"mousemove"),c(a,"mousedown"))};d._touchMove=function(a){e&&(this._touchMoved=!0,c(a,"mousemove"))};d._touchEnd=function(a){e&&(c(a,"mouseup"),c(a,"mouseout"),this._touchMoved||c(a,"click"),e=!1)};d._mouseInit=function(){b(document).on({touchstart:b.proxy(this,"_touchStart"),touchmove:b.proxy(this,"_touchMove"),touchend:b.proxy(this,"_touchEnd")},this.element);f.call(this)};d._mouseDestroy=function(){b(document).off({touchstart:b.proxy(this,"_touchStart"),touchmove:b.proxy(this,
"_touchMove"),touchend:b.proxy(this,"_touchEnd")},this.element);g.call(this)}}})(jQuery);