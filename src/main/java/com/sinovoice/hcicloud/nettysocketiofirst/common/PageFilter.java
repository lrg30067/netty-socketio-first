/** 
* @Project pingan_sap
* @Package com.sinovoice.hcicloud.model.common
* @author lixianji
* @date 2015年8月14日 下午2:35:24
*/
package com.sinovoice.hcicloud.nettysocketiofirst.common;
/** 
 * @Title PageFilter.java
 * @Package com.hcicloud.sap.common
 * @author mhn
 * @date 2017年9月21日 11:55:33
 */
public class PageFilter {
	 private int iDisplayStart;
	 private int iDisplayLength;
	 private int sEcho;
	 public PageFilter() {
	}
	public PageFilter(int iDisplayStart, int iDisplayLength) {
		this.iDisplayStart=iDisplayStart;
		this.iDisplayLength=iDisplayLength;
	}
	/**
	 * 获取sEcho  
	 * @return the sEcho
	 */
	public int getsEcho() {
		return sEcho;
	}
	/**
	 * 设置sEcho  
	 * @param sEcho the sEcho to set
	 */
	public void setsEcho(int sEcho) {
		this.sEcho = sEcho;
	}
	/**
	 * 获取iDisplayStart  
	 * @return the iDisplayStart
	 */
	public int getiDisplayStart() {
		return iDisplayStart;
	}
	/**
	 * 设置iDisplayStart  
	 * @param iDisplayStart the iDisplayStart to set
	 */
	public void setiDisplayStart(int iDisplayStart) {
		this.iDisplayStart = iDisplayStart;
	}
	/**
	 * 获取iDisplayLength  
	 * @return the iDisplayLength
	 */
	public int getiDisplayLength() {
		return iDisplayLength;
	}
	/**
	 * 设置iDisplayLength  
	 * @param iDisplayLength the iDisplayLength to set
	 */
	public void setiDisplayLength(int iDisplayLength) {
		this.iDisplayLength = iDisplayLength;
	}
	 
}
