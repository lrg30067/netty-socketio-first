/** 
* @Project sap
* @Package com.hcicloud.sap.common.network
* @author lixianji
* @date 2016年1月19日 下午3:14:05
*/
package com.sinovoice.hcicloud.nettysocketiofirst.common;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/** 
 * @Title HTTPMethod.java
 * @Package com.hcicloud.sap.common.network
 * @author lixianji
 * @date 2016年1月19日 下午3:14:05
 */
public class HTTPMethod {

	/**
	 * 日志类
	 */
	private static Logger log = Logger.getLogger(HTTPMethod.class);
	
	/**
	 * put
	 * @param url
	 * @param query
	 * @param time
	 * @return
	 */
	public static String doPutQuery(String url, String query, int time){
		StringBuffer stringBuffer = new StringBuffer();
		HttpClient client = new HttpClient();
		PutMethod method = new PutMethod(url);
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
		}
		// 发出请求
		int stateCode = 0;
		StopWatch stopWatch = new StopWatch();
		try {
			stopWatch.start();
			stateCode = client.executeMethod(method);
			log.info("==============stateCode" + stateCode + "=======");
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stopWatch.stop();
			if (stateCode == HttpStatus.SC_OK) {
				try {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(
									method.getResponseBodyAsStream(), "utf-8"));
					String str = "";
					while ((str = reader.readLine()) != null) {
						stringBuffer.append(str);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		method.abort();
		try {
			((SimpleHttpConnectionManager) client
					.getHttpConnectionManager()).shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuffer.toString();
	}
	
	/**
	 * post
	 * @param url
	 * @param query
	 * @param time
	 * @return
	 * @throws HttpException
	 */
	public static String doPostQuery(String url, String query, int time){
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(url);
		StringBuffer stringBuffer = new StringBuffer();
		try {
			method.setRequestHeader("Connection", "close");
			method.setRequestHeader("Content-type",
					"application/json;charset=UTF-8");
			client.getHttpConnectionManager().getParams()
					.setConnectionTimeout(time);
			method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, time);

				RequestEntity requestEntity = new ByteArrayRequestEntity(
						query.getBytes("UTF-8"), "UTF-8");
				method.setRequestEntity(requestEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 发出请求
		int stateCode = 0;
		StopWatch stopWatch = new StopWatch();
		try {
			stopWatch.start();
			stateCode = client.executeMethod(method);
			log.info("==============stateCode" + stateCode + "=======");
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stopWatch.stop();
			if (stateCode == HttpStatus.SC_OK) {
				try {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(
									method.getResponseBodyAsStream(), "utf-8"));
					String str = "";
					while ((str = reader.readLine()) != null) {
						stringBuffer.append(str);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			method.abort();
			try {
				((SimpleHttpConnectionManager) client
						.getHttpConnectionManager()).shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return stringBuffer.toString();
	}

	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String doGetQuery(String url, int time) {
	    StringBuffer stringBuffer = new StringBuffer();
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(url);
        method.setRequestHeader("Connection", "keep-alive");
        method.setRequestHeader(" Accept-Language", "zh-CN,zh;q=0.8");
        method.setRequestHeader(" Accept-Encoding", "gzip, deflate, sdch");
        method.setRequestHeader("Content-type",
                "application/json;charset=UTF-8");
        client.getHttpConnectionManager().getParams()
                .setConnectionTimeout(time);
        method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, time);
        // 发出请求
        int stateCode = 0;
        StopWatch stopWatch = new StopWatch();
        try {
            stopWatch.start();
            stateCode = client.executeMethod(method);
            log.info("==============stateCode" + stateCode + "=======");
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stopWatch.stop();
            if (stateCode == HttpStatus.SC_OK) {
                try {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                                    method.getResponseBodyAsStream(),"utf-8"));
                    String str = "";
                    while ((str = reader.readLine()) != null) {
                        stringBuffer.append(str);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            method.abort();
            try {
                ((SimpleHttpConnectionManager) client
                        .getHttpConnectionManager()).shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return stringBuffer.toString();           
	}
	
	/**
	 * delete
	 * @param url
	 * @param query
	 * @param time
	 * @return
	 * @throws HttpException
	 */
	public static String doDeleteQuery(String url, int time)
			throws HttpException {
		StringBuffer stringBuffer = new StringBuffer();
		HttpClient client = new HttpClient();
		DeleteMethod method = new DeleteMethod(url);
		method.setRequestHeader("Connection", "close");
		method.setRequestHeader("Content-type",
				"application/json;charset=UTF-8");
		client.getHttpConnectionManager().getParams()
				.setConnectionTimeout(time);
		method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, time);
		/*try {
			RequestEntity requestEntity = new ByteArrayRequestEntity(
					query.getBytes("UTF-8"), "UTF-8");
			method.setRequestEntity(requestEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		// 发出请求
		int stateCode = 0;
		StopWatch stopWatch = new StopWatch();
		try {
			stopWatch.start();
			stateCode = client.executeMethod(method);
			log.info("==============stateCode" + stateCode + "=======");
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stopWatch.stop();
			if (stateCode == HttpStatus.SC_OK) {
				try {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(
									method.getResponseBodyAsStream(), "utf-8"));
					String str = "";
					while ((str = reader.readLine()) != null) {
						stringBuffer.append(str);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			method.abort();
			try {
				((SimpleHttpConnectionManager) client
						.getHttpConnectionManager()).shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return stringBuffer.toString();
	}
	/**
	 * 
	 * @param url
	 * @param query
	 * @param time
	 * @return
	 */
	public static InputStream doPost(String url, String query , int time){
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(url);
		InputStream inputStream =null;
		try {
			method.setRequestHeader("Connection", "close");
			method.setRequestHeader("Content-type",
					"application/json;charset=UTF-8");
			client.getHttpConnectionManager().getParams().setConnectionTimeout(time);
			method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, time);
			RequestEntity requestEntity = new ByteArrayRequestEntity(
					query.getBytes("UTF-8"), "UTF-8");
			method.setRequestEntity(requestEntity);	

		} catch (Exception e) {
			e.printStackTrace();
		}
		// 发出请求
		int stateCode = 0;
		StopWatch stopWatch = new StopWatch();
		try {
			stopWatch.start();
			stateCode = client.executeMethod(method);
			log.info("==============stateCode" + stateCode + "=======");
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {stopWatch.stop();
		if (stateCode == HttpStatus.SC_OK) {
			System.out.println("返回的statecode"+stateCode);
			
			try {
				inputStream =   method.getResponseBodyAsStream();
				return inputStream;
				}catch (IOException e) {
				e.printStackTrace();
			}
		}
		method.abort();
		try {
			((SimpleHttpConnectionManager) client.getHttpConnectionManager()).shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    return inputStream;  
    
    
    
}
}
