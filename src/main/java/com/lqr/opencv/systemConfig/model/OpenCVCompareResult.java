package com.lqr.opencv.systemConfig.model;
/**
* @author 作者 :lqr
* @version 创建时间：2021年10月21日 下午1:39:34
* 类说明<br/>
* Correlation 相关性比较  					参数：Imgproc.CV_COMP_CORREL     ==》值越大时表示相似度越高 <br/>
* Chi - Square 卡方比较   					参数：Imgproc.CV_COMP_CHISQR <br/>
* Intersection 十字交叉性 					参数：Imgproc.CV_COMP_INTERSECT  ==》值越大时表示相似度越高 <br/>
* Bhattacharyya distance 巴氏距离		参数：Imgproc.CV_COMP_BHATTACHARYYA <br/>
* 
* 默认使用 Correlation
*/
public class OpenCVCompareResult {
	
	/**
	 * 相似度 范围 [0-1]
	 */
	private double similarityRate;
	
	/**
	 * 建议标志
	 * 两图片建议是相似图片返回： true
	 * 两图片建议是不同图片、活不相似返回：false
	 */
	private boolean suggestFlag;

	public double getSimilarityRate() {
		return similarityRate;
	}

	public void setSimilarityRate(double similarityRate) {
		this.similarityRate = similarityRate;
	}

	public boolean isSuggestFlag() {
		return suggestFlag;
	}

	public void setSuggestFlag(boolean suggestFlag) {
		this.suggestFlag = suggestFlag;
	}
	
}
