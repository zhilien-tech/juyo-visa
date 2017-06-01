package io.znz.jsite.ext.upload;

/**
 * 绝对路径提供类
 */
public interface RealPathAware {
	/**
	 * 获得绝对路径
	 *
	 * @param path
	 * @return
	 * @see javax.servlet.ServletContext#getRealPath(String)
	 */
	public String getRealPath(String path);
}
