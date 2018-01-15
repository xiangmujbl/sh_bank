/*
 * Copyright 2014-2015 Jiangsu SYAN tech. ltd.
 * netone.test.js
 */

var NetONEXTest = NetONEX.extend({

	pk1sign:function(inputdata)
	{
		var returnObj = null;
		var b64x = this.getBase64X();
		var hshx = this.getHashX();
		var colx = this.getCertificateCollectionX();
		colx.CF_Issuer_Contains = '';
		colx.CF_Subject_Contains = '';
		colx.Load();

		var crtx = colx.SelectCertificateDialog();
		if (!crtx) {
			throw new Error(colx.ErrorString);
		}
		this.certificate = crtx;
		this.b64x = b64x;
		this.hshx = hshx;
		//this.printCertificateX(crtx, 1); //打印信息
		
		e = crtx.PKCS1Base64(b64x.EncodeUtf8String(inputdata.toString()));
		returnObj = new Object();
		returnObj.SXCertificate = crtx.Content; //证书信息
		returnObj.SXSerialNumber=crtx.SerialNumber;//证书序列号
		returnObj.SXInput = b64x.EncodeUtf8String(inputdata.toString());//签名原文
		returnObj.SXSignature = e; //签名原文后的信息
		returnObj.Subject = crtx.Subject;//主题项，内含工商号类似OID.2.5.4.1=gszcxb123456
		returnObj.BeforTime=crtx.NotBeforeSystemTime;//证书有效时间
		returnObj.AfterTime=crtx.NotAfterSystemTime;//证书有效时间
		returnObj.Issuer=crtx.Issuer;//颁发证书单位
		returnObj.t = crtx.ThumbprintSHA1;//最后一次缩略图
		return returnObj;
	},
	
	pk1sign2:function(inputdata,crtx)
	{
		var returnObj = null;
		var b64x = this.getBase64X();
		var hshx = this.getHashX();
		var colx = this.getCertificateCollectionX();
		colx.CF_Issuer_Contains = '';
		colx.CF_Subject_Contains = '';
		colx.Load();
	
	//	var crtx = colx.SelectCertificateDialog();
	//	if (!crtx) {
	//		throw new Error(colx.ErrorString);
	//	}
		this.certificate = crtx;
		this.b64x = b64x;
		this.hshx = hshx;
		//this.printCertificateX(crtx, 1); //打印信息
		
		e = crtx.PKCS1Base64(b64x.EncodeUtf8String(inputdata.toString()));
		returnObj = new Object();
		returnObj.SXCertificate = crtx.Content; //证书信息
		returnObj.SXSerialNumber=crtx.SerialNumber;//证书序列号
		returnObj.SXInput = b64x.EncodeUtf8String(inputdata.toString());//签名原文
		returnObj.SXSignature = e; //签名原文后的信息
		returnObj.Subject = crtx.Subject;//主题项，内含工商号类似OID.2.5.4.1=gszcxb123456
		returnObj.BeforTime=crtx.NotBeforeSystemTime;//证书有效时间
		returnObj.AfterTime=crtx.NotAfterSystemTime;//证书有效时间
		returnObj.Issuer=crtx.Issuer;//颁发证书单位
		returnObj.t = crtx.ThumbprintSHA1;//最后一次缩略图
		return returnObj;
	},
	showCert:function()
	{
		var colx = this.getCertificateCollectionX();
		colx.CF_Issuer_Contains = '';
		colx.CF_Subject_Contains = '';
		colx.Load();

		var crtx = colx.SelectCertificateDialog();
		if (!crtx) {
			throw new Error(colx.ErrorString);
		}
		return crtx;
	}
	

});

/*$(document).ready(function() {
	var obj = new NetONEXTest();
	obj.setupObject();
	obj.run();
});*/
