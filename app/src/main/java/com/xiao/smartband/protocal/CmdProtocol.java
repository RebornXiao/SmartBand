package com.xiao.smartband.protocal;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Log;

public class CmdProtocol {
	public static byte[] newCmd(byte[] data,int nLen){//data是filetype append 其它 
		byte[] Cmd = new byte[nLen];
		for(int i = 0 ; i< nLen; i++){
			Cmd[i] = data[i];
		}
		return Cmd;
		
	}
	public static byte[] PetCmd(byte[] data,byte[] append,int nLen){//data是filetype append 其它 
		byte[] Cmd = new byte[nLen+data.length];
		for(int i = 0 ; i< data.length; i++){
			Cmd[i] = data[i];
		}
		for(int i = data.length ; i< data.length + nLen; i++){//i = data.length
			Cmd[i] = append[i - data.length];
		}
		return Cmd;
		
	}
	public static byte[] packageCmd(byte ncmd,byte[] data,int nLen){//nLen协议数据长度
		byte[] Cmd = new byte[nLen+ 6];
		Cmd[0] = (byte) 0x8e;
		Cmd[1] = (byte) ((nLen + 1)/256);//低字节在前，高字节在后
		Cmd[2] = (byte) ((nLen + 1)%256);
		Cmd[3] = ncmd;
		for(int i = 0 ; i < nLen; i++){
			Cmd[i +4] = data[i];
		}
		for(int i = 1 ; i< nLen + 4; i ++){
			    Cmd[nLen+4] = (byte) (Cmd[nLen+4]^Cmd[i]);
			    //Cmd[nLen+4] = (byte) (Cmd[nLen+4] + Cmd[i]);
		}
		Cmd[nLen + 5] = (byte) 0x8e;
		return Cmd;
	}
	 public static byte[] intToByteArray1(int i) {//一个int型占四个字节,32位
  	   byte[] result = new byte[4];   //JAVA 高位在后，低位在前
  	   result[3] = (byte)((i >> 24) & 0xFF);//把所属字节的8位取出来,0xFF(00000000 00000000 00000000 11111111),就是为了把后8位获取下来
  	   result[2] = (byte)((i >> 16) & 0xFF);
  	   result[1] = (byte)((i >> 8) & 0xFF); 
  	   result[0] = (byte)(i & 0xFF);
  	   return result;
  	  }
	 public static int AckExam(byte[] nBuf,int nLen,byte[] nCmdPara){
		 int Rcmd = 0 ;
		 
		 if(nBuf[0] == (byte)0X8e){// && nLen <= 1024
			 int nCmdLen = (nBuf[2] >= 0 ? nBuf[2] : nBuf[2] + 256) + nBuf[1]*256;
			 byte[] nCRCmd = new byte[1];//校验码
			 for(int i = 1; i< nLen - 2;i ++){
					 nCRCmd[0] = (byte) (nCRCmd[0]^nBuf[i]);
			 }
			 if((nCmdLen - 1 + 6) == nLen && nBuf[nLen - 2] == nCRCmd[0] && nBuf[nLen -1] == (byte)0x8e){
				 Rcmd =  1;
				 nCmdPara[0] = nBuf[3];//命令码
				// nCmdPara[2] = nBuf[4];//参数码0,1
			 }
		 }
		 return Rcmd ;
	 }
	 public static int GetPackageItem(byte [] pbData, int nLen, int nOffset, byte [] pvItem, int nItemLen)
     {
     	int nRtn = -1;
     	if(nLen >= nOffset + nItemLen)//offset初始下标或者说是上一项的下标
     	{
     		for(int i = nOffset; i < nOffset + nItemLen; i++)
     			pvItem[i - nOffset] = pbData[i];
     			nRtn = nOffset + nItemLen;//nRtn就是下标
     	}

     	return nRtn;
     }
	 
	//4个byte字节转换成int型，判断bData[0]是否大于0，否则+256变成正数 -127 - 128
     //为什么加256，如 -127 + 256 = 128
     public static int Byte2DateTimeInt(byte [] bData, int nOffset){
  	   int nDate = (bData[nOffset] >= 0 ? bData[nOffset] : 256 + bData[nOffset]) + 
  			   (bData[nOffset + 1] >= 0 ? bData[nOffset + 1] : 256 + bData[nOffset + 1]) * 256 + 
 			   (bData[nOffset + 2] >= 0 ? bData[nOffset + 2] : 256 + bData[nOffset + 2]) * 256 * 256 + 
 			   (bData[nOffset + 3] >= 0 ? bData[nOffset + 3] : 256 + bData[nOffset + 3]) * 256 * 256 * 256;
  	   
  	   return nDate;
     }
     public static int Byte2Int(byte [] bData, int nOffset){
    	   int nDate = (bData[nOffset] >= 0 ? bData[nOffset] : 256 + bData[nOffset]) * 256 * 256 * 256 + 
    			   (bData[nOffset + 1] >= 0 ? bData[nOffset + 1] : 256 + bData[nOffset + 1]) * 256*256 + 
   			   (bData[nOffset + 2] >= 0 ? bData[nOffset + 2] : 256 + bData[nOffset + 2]) * 256 + 
   			   (bData[nOffset + 3] >= 0 ? bData[nOffset + 3] : 256 + bData[nOffset + 3]);
    	   
    	   return nDate;
       }
     public static int Byte2DateTimeInt2(byte [] bData, int nOffset){
  	   int nDate = (bData[nOffset] >= 0 ? bData[nOffset] : 256 + bData[nOffset]) + 
  			   (bData[nOffset + 1] >= 0 ? bData[nOffset + 1] : 256 + bData[nOffset + 1]) * 256;
  	   
  	   return nDate;
     }
	 /*String2byte*/
	 public static byte[] String2byte(String str){
		 byte[] nresult = null;
		 if(str != null && !str.equals("")){
			 try {
				nresult = str.getBytes("GBK");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		return nresult;
		 
	 }
	 /*byte to String*/
	 public static  String Bytes2String(byte [] bData){
  	   int nLen = 0;
  	   for(int i = 0; i < bData.length; i++){
  		   if(bData[i] != 0) nLen += 1;
  		   else break;
  	   }
  	   byte [] bStringBytes = new byte[nLen];
  	   for(int j = 0; j < nLen; j++){
  		   bStringBytes[j] = bData[j];
  	   }
  	   String sbyte = null;
			try {
				sbyte = new String(bStringBytes, "GBK");// "mnw 
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}        	   
  	   return sbyte;// "mnw 
     }
	 private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
	        'A', 'B', 'C', 'D', 'E', 'F' };
  
	        public static byte[] toHexString(byte[] b) {  //String to  byte
	                 StringBuilder sb = new StringBuilder(b.length * 2);  
	                 for (int i = 0; i < b.length; i++) {  
	                     sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);  
	                     sb.append(HEX_DIGITS[b[i] & 0x0f]);  
	                 }  
	                 return sb.toString().getBytes();  
	        }
	          public byte[] md532(byte[] data,int len) {
	        	  byte[] nCmd = new byte[len];
	        	//  for(int i = 0 ;i < len; i++){
	        	  for(int i = 0 ;i < data.length; i++){
	        		  nCmd[i] = data[i]; 
	        	  }
	                try {
	                    // Create MD5 Hash
	                    MessageDigest digest = MessageDigest.getInstance("MD5");
	                    digest.update(data);
	                    byte messageDigest[] = digest.digest();//16进制的           
	                     return toHexString(messageDigest);//32位需要调用这个方法
	                } catch (NoSuchAlgorithmException e) {
	                    e.printStackTrace();
	                }
	                        
	                return nCmd;
	            }
	          public byte[] md516(byte[] data,int len) {
	        	  		byte[] nCmd = new byte[len];
	                try {
	                	
	                	for(int i = 0 ;i < len; i ++){
	                		nCmd[i] = data[i];
	                	}
	                    // Create MD5 Hash
	                    MessageDigest digest = MessageDigest.getInstance("MD5");
	                    digest.update(nCmd);
	                    byte messageDigest[] = digest.digest();//16进制的
	                    return messageDigest;
	                } catch (NoSuchAlgorithmException e) {
	                    e.printStackTrace();
	                }
	                        
	                return nCmd;
	            }
	          
	          
	       public byte[] md5(byte[] data) {
           
	    	   try {
               // Create MD5 Hash
               MessageDigest digest = MessageDigest.getInstance("MD5");
               digest.update(data);
               byte messageDigest[] = digest.digest();//16进制的
               
               return toHexString(messageDigest);
           } catch (NoSuchAlgorithmException e) {
               e.printStackTrace();
           }
                   
           return data;
       }
	         
	  public static String toHexStringg(byte[] b) {  //String to  byte
	           StringBuilder sb = new StringBuilder(b.length * 2);  
	           for (int i = 0; i < b.length; i++) {  
	                sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);  
	                sb.append(HEX_DIGITS[b[i] & 0x0f]);  
	                }  
	               return sb.toString();  
	              }
	  public static String md5str(String s) {
	       try {
	         // Create MD5 Hash
	          MessageDigest digest = MessageDigest.getInstance("MD5");
	          digest.update(s.getBytes());
	          byte messageDigest[] = digest.digest();          
	          return toHexStringg(messageDigest);
	          
	           } catch (NoSuchAlgorithmException e) {
	                e.printStackTrace();
	         }                
	            return "";
	       }
	  private static byte[] cache = null;
	  public static int DecodeCmdData(byte[] data,int nLen,byte[] buffer){
		int k , m = 0;
		int ncacheLen = (cache != null ? cache.length:0);
		
		byte[] lastData = cache;

		cache = new byte[ncacheLen + nLen];
		for(int n = 0; n < ncacheLen; n++)
		{
			cache[n] = lastData[n];
		}
		lastData = null;
		
		for(int i = 0;i<nLen; i++)
		{
			cache[ncacheLen + i] = data[i];//缓存的数据+data
		}
		nLen += ncacheLen;
		for( k = 0; k <nLen; k++)
		{
			if(cache[k] == (byte)0x8e) break;
		}
		
		lastData = cache;
		nLen -= k;
		//删除协议头前面不合理的数据
		if(nLen > 0 && k > 0) cache = new byte[nLen];
		for(int i = 0; i <nLen && k > 0; i ++)
		{
			cache[ i ] = lastData[ i + k];
		}
		
		data = cache;
		k = 0 ;
		if(data.length <= 2){
			for(int x = 0 ; x < data.length; x ++){
				Log.i("info", "dataaa="+data[x]);
			}
			return 0;
		}
		int nCmdLen = (data[2] >= 0 ? data[2] : data[2] + 256) + data[1]*256;//消息体的长度
		int nDataLen = 3 + nCmdLen +  2;//头 + 长度2个字节 + 消息长度 + 尾巴

		//把找到协议头、协议尾,把这些数据放到 buffer里面，然后在界面解析
		if(nLen >= nDataLen )
		{	
			    m = 1;
			for(int i = 0; i < nDataLen; i++)
			{
				buffer[i] = cache[i];
			}
		}
		
		if( m == 1){
			
		     nLen -= nDataLen;
		 if( nLen > 0 ){
			 cache = new byte [nLen];
			 
				for(int i = 0 ; i < nLen && nLen >= nCmdLen; i ++){//可能?
					cache[i] = lastData[ nLen - nCmdLen ];} 
				
		    }else {
		    	cache = null;
		    }
		 
		}else {
			nDataLen = 0 ;
		}
		
		
		return nDataLen; 
		
	      }
      }
