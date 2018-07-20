import com.inesv.ecchain.common.crypto.Crypto;
import com.inesv.ecchain.common.util.Convert;
import com.inesv.ecchain.kernel.core.Account;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;

/**
 * @author Wu Created by SKINK on 2018/1/16.
 */
public class test {




  public static void main(String[] args) {
    Double a = 50000000000D;
    Double b = 0.0000005D;
    Double wei = 100000000000000000D;
    Double wei2= 1000000000000000000D;

    Double bb = new BigInteger("746a528800",16).doubleValue();
    System.out.println(new BigDecimal(4712388*bb/wei).toPlainString());
     //d = 0x746a528800;

    //交易费 = gasUsed * gasPrice
    System.out.println(21000*500000000000D);

    //100000000000000000

    System.out.println(new BigDecimal(100000000000000000000D/100).toPlainString());
    //1000000000000000000

    System.out.println(new BigDecimal(a/b).toPlainString());
  }






}
