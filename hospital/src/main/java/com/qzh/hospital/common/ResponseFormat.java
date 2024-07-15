package com.qzh.hospital.common;


import lombok.Data;

@Data
public class ResponseFormat {
    private String msg;
    private Long total;
    private Object data;


    public static ResponseFormat fail(){
        return responseFormat("fail",0L,null);
    }

    public static ResponseFormat suc(){
        return responseFormat("success",0L,null);
    }


    public static ResponseFormat suc(Object data){
        return responseFormat("success",0L,data);
    }
    public static ResponseFormat suc(Long total,Object data){
        return responseFormat("success",total,data);
    }

    private static ResponseFormat responseFormat(String msg, Long total, Object data) {
        ResponseFormat res = new ResponseFormat();
        res.setData(data);
        res.setMsg(msg);
        res.setTotal(total);
        return res;
    }
}
