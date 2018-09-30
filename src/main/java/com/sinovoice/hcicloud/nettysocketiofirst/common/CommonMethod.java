package com.sinovoice.hcicloud.nettysocketiofirst.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class CommonMethod {

	/**
	 * 将base64字符保存文本文件
	 * 
	 * @param base64Code
	 * @param targetPath
	 * @throws Exception
	 */
	public static void toFile(String base64Code, String targetPath)
			throws Exception {

		byte[] buffer = base64Code.getBytes();
		FileOutputStream out = new FileOutputStream(targetPath);
		out.write(buffer);
		out.close();
	}

	public static String doPostQuery(String url, String query, int time)
			throws Exception {
		String result = null;
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(url);
		method.setRequestHeader("Connection", "close");
		method.setRequestHeader("Content-type",
				"application/json;charset=UTF-8");
		client.getHttpConnectionManager().getParams()
				.setConnectionTimeout(time);
		method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, time);
		try {
			RequestEntity requestEntity = new ByteArrayRequestEntity(
					query.getBytes("UTF-8"), "UTF-8");
			method.setRequestEntity(requestEntity);
		} catch (Exception e) {
			e.printStackTrace();
		} // 发出请求
		int stateCode = 0;
		try {
			stateCode = client.executeMethod(method);
		} catch (HttpException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		} finally {
			if (stateCode == HttpStatus.SC_OK) {
				try {
					result = method.getResponseBodyAsString();
				} catch (IOException e) {
					throw e;
				}
			}
			method.abort();
			try {
				((SimpleHttpConnectionManager) client
						.getHttpConnectionManager()).shutdown();
			} catch (Exception e) {
				throw e;
			}
		}
		return result;
	}

	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * java 根据数字获得指定区间
	 */
	public static List<String> getNumIntervalStr(Long value, Integer Interval){
		List<String> list = new ArrayList<String>();
		long temp1 = 1;
		long temp = Interval;
		System.out.println(value/Interval);
		for(int i = 0; i< Math.ceil(value.doubleValue()/Interval); i++){
			list.add(temp1+"~"+temp);
			temp1 = temp+1;
			if(temp<value){
				temp = temp+Interval;
			}else{
				temp = temp+value;
			}
		}
		return list;
	}
	
	/**
	 * 读取文本内容
	 * @param path
	 * @return
	 */
	public static String getFileContent(String path){
	    File file = new File(path);
	    if(!file.exists()||file.isDirectory()){
	        return null;
	    }
	    StringBuffer content = new StringBuffer();
	    FileReader fileReader = null;
	    BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            String line = null;
            while((line=bufferedReader.readLine())!=null){
                content.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(bufferedReader!=null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fileReader!=null){
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	    
	    return content.toString();
	    
	}
	
	/**
     * 获取录音信息
     * @param
     * @return
     */
//    public static JSONObject getVoiceInfo(String uuid) {
//        StringBuilder searchBuilder = new StringBuilder();
//        searchBuilder.append("{\"query\":{")
//                        .append("\"filtered\":{")
//                        .append("\"query\":{")
//                        .append("\"match\":{")
//                        .append("\"UUID\":\"")
//                        .append(uuid)
//                        .append("\"}}}}}");
//        JSONObject resultJObject = ESMethod.find("*", searchBuilder.toString());
//        JSONArray voiceJObject = resultJObject.getJSONArray("voices");
//        return voiceJObject.getJSONObject(0);
//    }
    
    
    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;  
        int minute = 0;  
        int second = 0;  
        if (time <= 0)  
            return "00:00";  
        else {  
            minute = time / 60;  
            if (minute < 60) {  
                second = time % 60;  
                timeStr = unitFormat(minute) + ":" + unitFormat(second);  
            } else {  
                hour = minute / 60;  
                if (hour > 99)  
                    return "99:59:59";  
                minute = minute % 60;  
                second = time - hour * 3600 - minute * 60;  
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);  
            }  
        }  
        return timeStr;  
    }  
    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)  
            retStr = "0" + Integer.toString(i);
        else  
            retStr = "" + i;  
        return retStr;  
    } 
    //转换转写字符串为话者分离格式
    public static String transContent(String transContent){
    	
    	  JSONArray contaArray =  JSONObject.parseArray(transContent);
    	  StringBuffer sb = new StringBuffer();
          for (int j = 0; j < contaArray.size(); ++j) {
            JSONObject jsonObject = contaArray.getJSONObject(j);

            String result = jsonObject.getString("content");
            String talkertype = jsonObject.getString("talkertype");
            if ((result != null) && (!"".equals(result))) {
              String speakContent = result.substring(0,result.indexOf(";"));
              String timeStamp = result.substring(result.indexOf(";time=") + 6, result.length());
              String[] timeStampArray = timeStamp.split(" ");
              timeStamp = "[" + secToTime((int)(Long.parseLong(timeStampArray[0]) / 1000L)) +  "]";
              if (("".equals(speakContent)) ||  (speakContent == null)){
            	  continue;
              }
              if ("1".equals(talkertype))
              {
                speakContent = timeStamp + "[客服]" + speakContent;
              } else if ("2".equals(talkertype)) {
                speakContent = timeStamp + "[用户]" + speakContent;
              }
              sb.append(speakContent+"<br>");
            }
          }

    	return sb.toString();
    }

	//转换转写字符串为话者分离格式
	public static String transContent(String userPhone,String callContent){

		String userPhoneList[] = userPhone.split(",");
		String callContentList[] = callContent.split("☆");
		if (userPhoneList.length != callContentList.length) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (int j = 0; j < userPhoneList.length; ++j) {
			String userPhoneVo = userPhoneList[j];
			String callContentVo = callContentList[j];

			if (StringUtils.isEmpty(callContentVo)) {
				continue;
			}
			if ((userPhoneVo != null) && (!"".equals(userPhoneVo))) {
				String speakContent = callContentVo;
				String userPhoneVoArray[] = userPhoneVo.split("-");
				String timeStamp = "[" + secToTime((int) (Long.parseLong(userPhoneVoArray[0]) / 1000L)) + "]";
				if ("A".equals(userPhoneVoArray[2])) {
					speakContent = timeStamp + "[客服]" + speakContent;
				} else if ("C".equals(userPhoneVoArray[2])) {
					speakContent = timeStamp + "[用户]" + speakContent;
				}
				sb.append(speakContent + "<br>");
			}
		}
		return sb.toString();
	}

	//成功单话者分离测试
//	public static void main(String[] args){
//		String userPhone = "550-4320-C,4510-5060-A,5310-12760-C,13010-14840-A,15250-16100-C,16530-19660-A,19890-21540-C,21730-27040-A,27250-28620-C,28890-31280-A,31970-32300-C,33090-34160-A,34310-34640-C,60010-60760-C,84410-85540-C,160390-161080-C,161730-164520-C,165890-166450-C,166450-172860-A,173310-173520-C,173930-178860-A,179190-180000-C,180650-184540-A,185150-186740-C";
//		String callContent = "哎喂哎你好卢先生哎你好☆你好☆呃这边的话之前给您支付的时候看到您这个单当天的这个交易限额超限了您需要改成这个☆建行的信用卡来缴费是吧☆嗯对是☆好行那您稍等一下我帮您切入到这个信用卡☆这个支付系统里面☆那的话是需要您把信用卡带着身边输入信用卡卡号有效期和安全验证码的☆总共有五个操作步骤☆稍后呢您根据系统提示来完成好吗☆好的☆好的那您稍等☆嗯☆嗯☆嗯☆喂你好☆嗯你好嗯帮您确认一下您稍等☆好的更☆你你已经缴费成功了稍后的话呢扣费短信和同样的承保信息也会发到您手机上的刘先生☆嗯☆好行那这边也就不多耽误您的时间了有问题您联系畅通就可以了☆嗯好的☆好祝您平安您需要我们的保障给宝贝送学生幸福再见☆嗯再见嗯好☆";
//		System.out.println(transContent(userPhone,callContent));
//	}
    
}
