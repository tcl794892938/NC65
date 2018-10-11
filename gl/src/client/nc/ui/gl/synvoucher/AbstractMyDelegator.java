package nc.ui.gl.synvoucher;

import java.util.Hashtable;

/**
 * 
 * 抽象的业务代理类
 * 
 * @author author
 * @version tempProject version
 */
public abstract class AbstractMyDelegator extends
		nc.ui.trade.bsdelegate.BDBusinessDelegator {

	public Hashtable loadChildDataAry(String[] tableCodes, String key)
			throws Exception {

		Hashtable dataHashTable = new Hashtable();
		return dataHashTable;

	}

	/**
	 * 该方法用于获取查询条件，用户在缺省实现中可以对该方法进行修改。
	 */
	public String getBodyCondition(Class bodyClass, String key) {

		return null;
	}

}
