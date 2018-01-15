namespace java com.mmec.thrift.service

struct ReturnData{
1:string retCode;
2:string desc;
3:string descEn;
4:string pojo;
}

service VideoRMIServices {

// 1、视频编码注册接口
ReturnData registerVideoCode(1: map<string,string> datamap),

// 2、查询视频编码
ReturnData queryVideoCode(1: map<string,string> datamap),

// 3、保存签署视频附件
ReturnData saveSignVideo(1: map<string,string> datamap),

// 4、查询视频附件
ReturnData querySignVideo(1: map<string,string> datamap);
}
