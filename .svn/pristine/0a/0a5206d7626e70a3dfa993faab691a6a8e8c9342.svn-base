/*
 * Copyright 2014-2015 Jiangsu SYAN tech. ltd.
 * netone.base.src.js
 */



/*
  http://dean.edwards.name/Base/Base.js
  Base.js, version 1.1a
  Copyright 2006-2010, Dean Edwards
  License: http://www.opensource.org/licenses/mit-license.php
  Renamed to objectclass.js
*/

var objectclass = function() {
    // dummy
};

objectclass.extend = function(_instance, _static) { // subclass
    var extend = objectclass.prototype.extend;

    // build the prototype
    objectclass._prototyping = true;
    var proto = new this;
    extend.call(proto, _instance);
    proto.objectclass = function() {
        // call this method from any other method to invoke that method's ancestor
    };
    delete objectclass._prototyping;

    // create the wrapper for the constructor function
    //var constructor = proto.constructor.valueOf(); //-dean
    var constructor = proto.constructor;
    var klass = proto.constructor = function() {
        if (!objectclass._prototyping) {
            if (this._constructing || this.constructor == klass) { // instantiation
                this._constructing = true;
                constructor.apply(this, arguments);
                delete this._constructing;
            } else if (arguments[0] != null) { // casting
                return (arguments[0].extend || extend).call(arguments[0], proto);
            }
        }
    };

    // build the class interface
    klass.ancestor = this;
    klass.extend = this.extend;
    klass.forEach = this.forEach;
    klass.implement = this.implement;
    klass.prototype = proto;
    klass.toString = this.toString;
    klass.valueOf = function(type) {
        //return (type == "object") ? klass : constructor; //-dean
        return (type == "object") ? klass : constructor.valueOf();
    };
    extend.call(klass, _static);
    // class initialisation
    if (typeof klass.init == "function") klass.init();
    return klass;
};

objectclass.prototype = {
    extend: function(source, value) {
        if (arguments.length > 1) { // extending with a name/value pair
            var ancestor = this[source];
            if (ancestor && (typeof value == "function") && // overriding a method?
                // the valueOf() comparison is to avoid circular references
                (!ancestor.valueOf || ancestor.valueOf() != value.valueOf()) &&
                /\bobjectclass\b/.test(value)) {
                // get the underlying method
                var method = value.valueOf();
                // override
                value = function() {
                    var previous = this.objectclass || objectclass.prototype.objectclass;
                    this.objectclass = ancestor;
                    var returnValue = method.apply(this, arguments);
                    this.objectclass = previous;
                    return returnValue;
                };
                // point to the underlying method
                value.valueOf = function(type) {
                    return (type == "object") ? value : method;
                };
                value.toString = objectclass.toString;
            }
            this[source] = value;
        } else if (source) { // extending with an object literal
            var extend = objectclass.prototype.extend;
            // if this object has a customised extend method then use it
            if (!objectclass._prototyping && typeof this != "function") {
                extend = this.extend || extend;
            }
            var proto = {
                toSource: null
            };
            // do the "toString" and other methods manually
            var hidden = ["constructor", "toString", "valueOf"];
            // if we are prototyping then include the constructor
            var i = objectclass._prototyping ? 0 : 1;
            while (key = hidden[i++]) {
                if (source[key] != proto[key]) {
                    extend.call(this, key, source[key]);

                }
            }
            // copy each of the source object's properties to this object
            for (var key in source) {
                if (!proto[key]) extend.call(this, key, source[key]);
            }
        }
        return this;
    }
};

// initialise
objectclass = objectclass.extend({
    constructor: function() {
        this.extend(arguments[0]);
    }
}, {
    ancestor: Object,
    version: "1.1",

    forEach: function(object, block, context) {
        for (var key in object) {
            if (this.prototype[key] === undefined) {
                block.call(context, object[key], key, object);
            }
        }
    },

    implement: function() {
        for (var i = 0; i < arguments.length; i++) {
            if (typeof arguments[i] == "function") {
                // if it's a function, call it
                arguments[i](this.prototype);
            } else {
                // add the interface using the extend method
                this.prototype.extend(arguments[i]);
            }
        }
        return this;
    },

    toString: function() {
        return String(this.valueOf());
    }
});


var NetONEX = objectclass.extend({
	CONTROL_MAP: {},
	LOGSHOW_ID: "",
	NETONEX_ID: "",
	NETONEX_ACTIVEX_CODEBASE: "",
	NETONEX_NPAPI_CODEBASE: "",
	NETONEX_VERSION: "",
	DEBUG: 0,
	QUIET: 0,
	VERSION: "1.0.1",

	isie: function() {
		var x = navigator.userAgent.toLowerCase();
		return /msie/.test(x) || /trident/.test(x);
	},

	vardump: function(arr, level) {
		var dumped_text = "";
		if(!level) level = 0;
		
		//The padding given at the beginning of the line.
		var level_padding = "";
		for(var j=0;j<level+1;j++) level_padding += "    ";
		
		if(typeof(arr) == 'object') { //Array/Hashes/Objects 
			for(var item in arr) {
				var value = arr[item];
				
				if(typeof(value) == 'object') { //If it is an array,
					dumped_text += level_padding + "'" + item + "' ...\n";
					dumped_text += dump(value,level+1);
				} else {
					dumped_text += level_padding + "'" + item + "' => \"" + value + "\"\n";
				}
			}
		} else { //Stings/Chars/Numbers etc.
			dumped_text = "===>"+arr+"<===("+typeof(arr)+")";
		}
		return dumped_text;
	},
	
	getLogElementID: function() {
		return this.LOGSHOW_ID;
	},

	getNetONEXElementID: function() {
		return this.NETONEX_ID;
	},

	log: function(e) {
		var x = this.getLogElementID();
		if (!x) {
			return;
		}		
		var msg = '';
		if ('string' == typeof e) {
			msg = $.sprintf('<span class="green">%s</span>', e);
		}
		else {
			msg = this.DEBUG ? $.sprintf('<span class="red">[%s] %s</span>', e.stack, e.message) : $.sprintf('<span class="red">%s</span>', e.message);
		}
		var t = new Date();
		var d = $.sprintf('<br/>%d-%d-%d %d:%d:%d %s', t.getFullYear(), t.getMonth() + 1, t.getDate(), t.getHours(), t.getMinutes(), t.getSeconds(), msg);
		$('#'+x).append(d);	
	},

	logClean:function() {
		var x = this.getLogElementID();
		$('#'+x).html('');
	},

	// <object width="1" height="1" id="NetONEX" classid="CLSID:EC336339-69E2-411A-8DE3-7FF7798F8307" codebase="/activex/NetONEX.v1.2.1.0.cab#Version=1,2,1,0"/>

	getMainX: function() {
		var id = this.getNetONEXElementID();
		var r = document.getElementById(id);
		if (this.isie()) {
			if (!('Quiet' in r)) {
				throw new Error('ActiveX NetONEX is not running.');
			}
			return r;
		}
		var m = this.CONTROL_MAP['MainX'];
		if ('undefined' != typeof m) {
			return m;
		}
		if (!('NP_IMainX' in r)) {
			throw new Error('NPAPI npNetONE is not running');
		}
		m = r.NP_IMainX();
		this.CONTROL_MAP['MainX'] = m;
		return m;
	},

	getBase64X: function() {
		return this.getInstanceX('Base64X');
	},

	getHashX: function() {
		return this.getInstanceX('HashX');		
	},

	getCertificateCollectionX: function() {
		return this.getInstanceX('CertificateCollectionX');		
	},
	
	getCSPEnrollX: function() {
		return this.getInstanceX('CSPEnrollX');
	},

	getSKFEnrollX: function() {
		return this.getInstanceX('SKFEnrollX');
	},

	getInstanceX: function(id) {
		var m = this.getMainX();
		var r = this.CONTROL_MAP[id];
		if ('undefined' != typeof r) {
			return this.CONTROL_MAP[id];
		}
		if (id == 'Base64X') {
			r = m.CreateBase64XInstance();
		}
		else if (id == 'HashX') {
			r = m.CreateHashXInstance();
		}
		else if (id == 'CertificateCollectionX') {
			r = m.CreateCertificateCollectionXInstance();
		}
		else if (id == 'CSPEnrollX') {
			r = m.CreateCSPEnrollXInstance();
		}
		else if (id == 'SKFEnrollX') {
			r = m.CreateSKFEnrollXInstance();
		}
		if (!('Quiet' in r)) {
			throw new Error($.sprintf('%s is not running.', id));
		}
		this.CONTROL_MAP[id] = r;
		return r;
	},

	vb2js_array: function(v) {
		if (typeof VBArray !== 'undefined') {
			var j = (new VBArray(v)).toArray();
			return j;
		}
		return v;
	},

	js2vb_array: function(v) {
		if (typeof VBArray !== 'undefined') {
			// we can convert VBArray to javascript array in IE
			var dictionary = new ActiveXObject("Scripting.Dictionary");
			var i;
			var n = 0;
			for (i in v) {
				dictionary.add(n, v[i]);
				n ++;
			}
			return dictionary.Items();
		}
		return v;
	},
	
	asInt: function(v) {
		if (isNaN(v)) return 0;
		return parseInt(v);
	},

	asString: function(v) {
		if (!v) return '';
		return '' + v;
	},
	
	showTesting: function() {
		var self = this;
		if (!this.isie()) {
			$('div[action=netonex]').each(function() {
				self.onNpapiNotInstalled($(this));
			});
		}
		this.log('This is a testing string');
		this.log(new Error('This is an error test string.'));
	},
	
	isNpapiInstalled: function() {
		var x = navigator.mimeTypes['application/x-netone'];
		return (x === undefined) ? 0 : 1;
	},
	
	isNpapiUptodate: function() {
		if (!this.NETONEX_VERSION) return true;
		var d = this.NETONEX_VERSION.split('.');
		var i, v = 0;	
		for (i in d) {
			var q = this.asInt(d[i]);
			v = v * 0x100 + q;
		}
		var x = this.getMainX();
		var a = this.asInt(x.Version);
		return (a >= v) ? 1 : 0;
	},

	onNpapiNotInstalled: function(div) {
		var r = 'NPAPI npNetONE is not installed.';
		if (this.NETONEX_NPAPI_CODEBASE) {
			r = $.sprintf('%s Please download and install npNetONE from <a href="%s">here</a>, then restart your browser.', r, this.NETONEX_NPAPI_CODEBASE);
		}
		r = $.sprintf('<span class="red">%s</span>', r);
		div.append(r);
	},

	onNpapiNotUptodate: function(div) {
		var r = $.sprintf('NPAPI npNetONE (%s) is not installed.', this.NETONEX_VERSION);
		if (this.NETONEX_NPAPI_CODEBASE) {
			r = $.sprintf('%s Please download and install npNetONE from <a href="%s">here</a>, then restart your browser.', r, this.NETONEX_NPAPI_CODEBASE);
		}
		r = $.sprintf('<span class="red">%s</span>', r);
		div.append(r);
	},

	getObjectElement: function() {
		var r = document.createElement('object');
		r.id = this.NETONEX_ID;
		r.width = "1px";
		r.height = "1px";
		if (this.isie()) {
			r.classid = "CLSID:EC336339-69E2-411A-8DE3-7FF7798F8307";
			r.codeBase = $.sprintf('%s#Version=%s', this.NETONEX_ACTIVEX_CODEBASE, this.NETONEX_VERSION.replace(/\./g, ','));
		}
		else {
			r.type = 'application/x-netone';
		}
		return r;
	},
	
	onSetup: function(div) {
		this.LOGSHOW_ID = div.attr('logshowid');
		this.NETONEX_ID = div.attr('netonexid');
		this.NETONEX_ACTIVEX_CODEBASE = div.attr('activex_codebase');
		this.NETONEX_NPAPI_CODEBASE = div.attr('npapi_codebase');
		this.NETONEX_VERSION = div.attr('version');
		this.DEBUG = div.attr('debug') ? 1 : 0;
		this.QUIET = div.attr('quiet') ? 1 : 0;
		var s = this.getObjectElement();
		div.append(s);
		if (!this.isie()) {
			if (!this.isNpapiInstalled()) {
				this.onNpapiNotInstalled(div);
			}
			else {
				try {
					if (!this.isNpapiUptodate()) {
						this.onNpapiNotUptodate(div);
					}
				}
				catch (e) {
					//chrome will be failed at getMainX() sometimes but it still works fine
					//this.log(e);
				}
			}
		}
	},

	setupObject: function() {
		var self = this;
		$('div[action=netonex]').each(function() {
			self.onSetup($(this));
			return false;
		});
		//this.showTesting();
	}
});


