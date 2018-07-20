import com.alibaba.fastjson.JSON;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;

/**
 * @author Wu Created by SKINK on 2018/2/5.
 */
public class TradeApi {

  //最小单位
  private static BigDecimal NQT = new BigDecimal("100000000");

  //初始创世时间
  private static Long Gentime = 1515369600L;

  //账户1
  //密码:gec1517815128011HO8q
  //{"accountRS":"EC-SPSS-YT2J-ZQS6-85Q9W","publicKey":"c53b16d3345e7c72262e14c40873d19ea36f93ccdf04e83faa9f72a48b89353b","requestProcessingTime":0,"account":"7934995055894091544"}

  //账户2
  //密码:gec1517815161561jQsx
  //{"accountRS":"EC-9JLG-6Y3K-KW6H-DLZDR","publicKey":"edc0e1bbda4f68ee353bb0d908a6f8ae0bb8ea50299c3ed8ce5a786c5056602d","requestProcessingTime":0,"account":"13524294457533645390"}

  public static void main(String[] args) {

    for(int i = 1; i<2;i++){
      sendMoney(i);
    }


    //queryBlockTransactionByAccount("EC-SPSS-YT2J-ZQS6-85Q9W");
    //getUnconfirmedTransactionsByAccount("EC-SPSS-YT2J-ZQS6-85Q9W");
    //getBalance("EC-SPSS-YT2J-ZQS6-85Q9W");
    //createAccount();
  }

  /*
  * 发送交易
  * */
  public static void sendMoney(int i){
    String method = "sendMoney";
    Map<String,Object> parmasMap = new HashMap<>();
    parmasMap.put("recipient","EC-DPWF-NZJR-4JLH-AC5W2");//接收方
    parmasMap.put("amountNQT",new BigDecimal(10).multiply(NQT).setScale(0,BigDecimal.ROUND_HALF_UP).toPlainString());//转账金额NQT
    parmasMap.put("feeNQT",new BigDecimal(0.1).multiply(NQT).setScale(0,BigDecimal.ROUND_HALF_UP).toPlainString());//手续费NQT
    parmasMap.put("secretPhrase","EC-9606323E8D56EFD7A63A73F4EB4FED2F");//发送方密码
    parmasMap.put("deadline",i+"");//默认1
    String result = sendRequest(method,parmasMap);
    System.out.println(result);

    /*
    *
    * */
  }

  /*
  * 随机生成密码
  * */
  public static String createPass(){
    StringBuilder builder = new StringBuilder();
    builder.append("gec").append(System.currentTimeMillis());
    String str="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklnmopqrstuvwxyz0123456789";
    StringBuilder sb = new StringBuilder(4);
    for(int i=0;i<4;i++)
    {
      char ch=str.charAt(new Random().nextInt(str.length()));
      sb.append(ch);
    }
    builder.append(sb);
    return builder.toString();
  }

  /*
  * 创建账号
  * */
  public static void createAccount(){
    String method = "getAccountId";
    Map<String,Object> parmasMap = new HashMap<>();
    String password = createPass();
    System.out.println("密码:"+password);//平台方应该记住此密码与账号关联，转账等资金操作需要用到改密码
    parmasMap.put("secretPhrase",password);//密码不能重复，推荐格式(平台缩写+时间戳+四位随机数)
    String result = sendRequest(method,parmasMap);
    System.out.println(result);
  }


  /*
  * 查询对应账号已经在区块中的交易流水
  * */
  public static void queryBlockTransactionByAccount(String account){
    String method = "getBlockchainTransactions";
    Map<String,Object> parmasMap = new HashMap<>();
    parmasMap.put("account",account);
    parmasMap.put("type",0);
    String result = sendRequest(method,parmasMap);
    System.out.println(result);
  }


  /*
  * 查询对应账号未确认的交易流水
  * */
  public static void getUnconfirmedTransactionsByAccount(String account){
    String method = "getUnconfirmedTransactions";
    Map<String,Object> parmasMap = new HashMap<>();
    parmasMap.put("account",account);
    String result = sendRequest(method,parmasMap);
    System.out.println(result);
  }

  /*
  * 根据账户查询余额
  * */
  public static void getBalance(String account){
    String method = "getBalance";
    Map<String,Object> parmasMap = new HashMap<>();
    parmasMap.put("account",account);
    String result = sendRequest(method,parmasMap);
    System.out.println(result);
    //{"unconfirmedBalanceNQT":"4999892537667600","forgedBalanceNQT":"2547111140","balanceNQT":"4999892537667600","requestProcessingTime":0}
    Map<String,Object> resultMap = JSON.parseObject(result,Map.class);
    String balanceNQT = (String) resultMap.get("balanceNQT");
    BigDecimal bigBalance = new BigDecimal(balanceNQT).divide(NQT);
    System.out.println(bigBalance.doubleValue());
    System.out.println(bigBalance.toPlainString());
  }

  public static String sendRequest(String method,Map<String,Object> parmas){
    HttpRequest request = HttpRequest.post("http://127.0.0.1:7876/ec");//请求钱包rpc地址
    parmas.put("requestType",method);
    request.form(parmas);
    HttpResponse response = request.send();
    return response.bodyText();
  }

  public static void dingWebHook() throws UnsupportedEncodingException {

    String webhook = "https://oapi.dingtalk.com/robot/send?access_token=2b013a954765a6cf4053e2e4d45129506bed7a231f74be6e38a5807cef6cae24";
    HttpRequest request = HttpRequest.post(webhook);
    Map<String,Object> reqMap = new HashMap<>();
    Map<String,Object> content = new HashMap<>();
    content.put("content","我是林所长");
    reqMap.put("msgtype","text");
    reqMap.put("text",JSON.toJSONString(content));
    System.out.println(JSON.toJSONString(reqMap));
    request.bodyText(JSON.toJSONString(reqMap),"application/json", "utf-8");
    System.err.println(request);
    HttpResponse response = request.send();
    System.out.println(response.bodyText());
    System.out.println(response.statusCode());
  }

}
