package com.damenghai.chahuitong2.pay.alipay;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.damenghai.chahuitong2.utils.L;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Sgun on 15/10/29.
 */
public class AlipayManager {
    // 商户PID
    public static final String PARTNER = "2088811966668469";
    // 商户收款账号
    public static final String SELLER = "damenghai@aliyun.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALlI4P2vLDUUBGPt" +
            "aRGexiJ6uQr1Wnuoc46n/a9j+y2hLuVuj8fg1XNP3NDSxziQ7i9qovuGFJVdZ1t4" +
            "/ExDBe+S4rqRNkDljJtALyJlEjUiWRc39nlpAlOr5G981KhNx7Y1zH+368/bAIbb" +
            "+g2RmvNmgs2VBMAjdPX9/rJcNFWhAgMBAAECgYA1RQi6ptRU5k5k1Gryz+0zGIl+" +
            "5w9+oe2nRBEhjeM3OTIccG8SDkgQARQBTbhNQQ3ZwBTfr6rXTqVanSwcWOCphPGD" +
            "0sNW/XZZLLVcfh/UIo3c2Ppvjv7FYYRpdCzmFh4ITViMhD7v5/2N22WCiv5OGzOs" +
            "Hi6CTViPuIXqJy5DCQJBAOVLZZKQymU08izEXvEnxvkH6dfpA9onLzbSXtKj7M3N" +
            "8aStBaDdDNht+kT8seYxaqGwpXukGOyDbZniKmzQH5MCQQDO3Ur9u9tphpt5jejS" +
            "uBoOAgeJ2trsHEWT41BS7pbGYVHZeVdMInXaM0Ed1RaIpWHWDUSUKlO9d4mEgcv9" +
            "TG57AkEArecuJSgNaQ7r8coJm+soPG2lDG0cz9LSO0j4Q9ex3TEp6VSUpTmh6XAX" +
            "1US2BDlMf09DYu/FO3Q+puxict6yYwJBAINfHgTNjKYTiiFD8hwANuu2djsh9e4E" +
            "dltY/OXuB2qs22IVIU+EgvuzmSLs9IaLIPrjKi4+QzgL9hVkqLuQEokCQQCZFoXA" +
            "beDU/iVKVxiUz6q4p4jwvCNMfjS+xOmmJUDd2WMbjnO8tlpUPUIePl48/JGn5+lH" +
            "+uWVtY9jRZvx6Vtn";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC5SOD9ryw1FARj7WkRnsYierkK" +
            "9Vp7qHOOp/2vY/stoS7lbo/H4NVzT9zQ0sc4kO4vaqL7hhSVXWdbePxMQwXvkuK6" +
            "kTZA5YybQC8iZRI1IlkXN/Z5aQJTq+RvfNSoTce2Ncx/t+vP2wCG2/oNkZrzZoLN" +
            "lQTAI3T1/f6yXDRVoQIDAQAB";

    public static final int SDK_PAY_FLAG = 1;

    public static final int SDK_CHECK_FLAG = 2;

    private static AlipayManager mInstance;

    private static Context mContext;

    private AlipayListener mListener;

    public static AlipayManager getInstance(Context context) {
        if(mInstance == null) {
            mContext = context;
            synchronized (mContext) {
                if(mInstance == null) {
                    mInstance = new AlipayManager();
                }
            }
        }
        return mInstance;
    }

    @SuppressWarnings("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AlipayManager.SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    L.d("msg: " + msg.obj + ", payResult: " + payResult + ", resultInfo: " + resultInfo);

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        mListener.onSuccess();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            mListener.onConfirming();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            mListener.onError();
                        }
                    }
                    break;
                }
                case AlipayManager.SDK_CHECK_FLAG: {
                    L.d("检查结果为：" + msg.obj);
                    break;
                }
                default:
                    break;
            }
        }
    };

    public void pay(String subject, String body, String price, String tradeNo, AlipayListener listener) {
        // 订单
        String orderInfo = getOrderInfo(subject, body, price, tradeNo);

        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask((Activity) mContext);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);

                Message msg = new Message();
                msg.what = AlipayManager.SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }

        };

        mListener = listener;

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * create the order info. 创建订单信息
     *
     */
    public String getOrderInfo(String subject, String body, String price, String tradeNo) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + (TextUtils.isEmpty(tradeNo) ? getOutTradeNo() : tradeNo) + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://www.chahuitong.com/mobile/api/payment/alipay/notify_url.php"
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     *
     */
    public String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
                Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content
     *            待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }

    public interface AlipayListener {
        void onSuccess();
        void onConfirming();
        void onError();
    }

}
