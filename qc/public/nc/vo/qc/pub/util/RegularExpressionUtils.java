package nc.vo.qc.pub.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * <p>
 * <b>本类主要完成以下功能：</b>
 * <ul>
 * <li>正则表达式校验工具类
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.5
 * @since 6.5
 * @author fengjqc
 * @time 2015-9-1 上午9:40:56
 */
public class RegularExpressionUtils {
  /**
   * 只能输入数字
   */
  public static void checkDouble(String value) {
    String exp = "^(-)?[0-9]+(\\.[0-9]+)?$";
    Pattern p = Pattern.compile(exp);
    Matcher m = p.matcher(value);
    if (!m.find()) {
      String msg =
          NCLangRes4VoTransl.getNCLangRes().getStrByID("c010000_0",
              "0c010000-0716");
      msg += "\n" + value;
      ExceptionUtils.wrappBusinessException(msg);
    }
  }
}
