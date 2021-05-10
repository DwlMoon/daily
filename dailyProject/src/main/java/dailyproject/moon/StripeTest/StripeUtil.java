package dailyproject.moon.StripeTest;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Paths;
import java.util.Map;

/**
 * @program: daily_test
 * @description:
 * @create: 2021-04-22 14:26
 **/

public class StripeUtil {

    public static void main (String[] args)throws Exception {
        Stripe.apiKey = "sk_test_51IivTTCL7nNRBWMPjrCpZ7brlbXQMF25IrTzCXTsXLKqdgqE6ZglL03Xom2bq12nwC1eScw0DVtE6VjE8ZYkRPLL00uhAMRjYJ";

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(777L)
                        .setCurrency("usd")
                        .addPaymentMethodType("card")
//                        .setReceiptEmail("jenny.rosen@example.com")
                        .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);
        System.out.println(paymentIntent.getClientSecret());
    }


    @ResponseBody
    @RequestMapping(value = {"/create-payment-intent"}, method = RequestMethod.POST)
    public String paymentOrder(@RequestBody Map<String, String> map,
                               HttpServletRequest request, HttpServletResponse response) throws StripeException {

        Stripe.apiKey = "sk_live_51HZCAR00sBFiQi4h";//自己sk_key 开发里面有自己找
        PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder()
                .setAmount(Long.decode(map.get("price")))//设置金额
                .setCurrency("twd")//设置货币
                .setDescription("购买"+map.get("title")+",订单号："+ map.get("order_no"))//描述
                .putMetadata("order_id", map.get("order_no"))//自定义参数
                .putMetadata("money", map.get("price"))//自定义参数
                .addPaymentMethodType("card")
                .setReceiptEmail(map.get("email"))//通知这个邮箱
                .build();//还有很多参数 自己看中文文档去
        PaymentIntent intent = PaymentIntent.create(createParams);
//        CreatePaymentResponse paymentResponse = new CreatePaymentResponse(intent.getClientSecret());
//        return .toJson(paymentResponse);
        return null;
    }

}
