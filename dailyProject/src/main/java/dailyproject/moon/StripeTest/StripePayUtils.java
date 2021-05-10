package dailyproject.moon.StripeTest;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Card;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Token;
import org.junit.Test;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * stripe 支付工具类
 */


@Component
public class StripePayUtils {

    private static final String key = "sk_test_51IivTTCL7nNRBWMPjrCpZ7brlbXQMF25IrTzCXTsXLKqdgqE6ZglL03Xom2bq12nwC1eScw0DVtE6VjE8ZYkRPLL00uhAMRjYJ";


    /**
     * 创建初始用户
     *
     * cus_JM1MieGLxmep25
     *
     * @param description 描述-用户编号
     * @return
     * @throws StripeException
     */
    @Test
    public void createStripeCustomNoCard () throws StripeException {
        String description="test";
        Stripe.apiKey = key;
        Map<String, Object> params = new HashMap<>();
        params.put("description", description);
        Customer customer = Customer.create(params);
        System.out.println(customer);
        System.out.println(customer.getId());
//        return customer.getId();
    }


    /**
     * 更新用户绑定银行卡
     *
     * @param stripeMemberId 客户stripeId
     * @param cardParam      银行卡参数
     * @return
     * @throws StripeException
     */
    public String updateStripeCustomWithCard (String stripeMemberId, Map<String, Object> cardParam) throws StripeException {
        Stripe.apiKey = key;
        //创建银行卡Token，顺便借用stripe内部验证校验银行卡信息是否正确
        String cardTokenId = createCardToken(cardParam);
        Map<String, Object> retrieveParams = new HashMap<>();
        List<String> expandList = new ArrayList<>();
        expandList.add("sources");
        retrieveParams.put("expand", expandList);
        Customer customer = Customer.retrieve(stripeMemberId, retrieveParams, null);

        Map<String, Object> params = new HashMap<>();
        params.put("source", cardTokenId);
        Card card = (Card) customer.getSources().create(params);
        return card.getId();
    }


    /**
     * 修改默认支付银行卡
     *
     * @param stripeMemberId 客户stripeId
     * @param stripeCardId   银行卡stripeId
     * @throws StripeException
     */
    public void updateStripeDefaultCard (String stripeMemberId, String stripeCardId) throws StripeException {
        Stripe.apiKey = key;
        Map<String, Object> retrieveParams = new HashMap<>();
        List<String> expandList = new ArrayList<>();
        expandList.add("sources");
        retrieveParams.put("expand", expandList);
        Customer customer = Customer.retrieve(stripeMemberId, retrieveParams, null);

        Map<String, Object> params = new HashMap<>();
        params.put("default_source", stripeCardId);
        customer.update(params);
    }


    /**
     * 删除用户绑定银行卡
     *
     * @param stripeMemberId 客户stripeId
     * @param cardId         银行卡stripeId
     * @return
     * @throws StripeException
     */
    public void deleteCard (String stripeMemberId, String cardId) throws StripeException {
        Stripe.apiKey = key;
        Map<String, Object> retrieveParams = new HashMap<>();
        List<String> expandList = new ArrayList<>();
        expandList.add("sources");
        retrieveParams.put("expand", expandList);
        Customer customer = Customer.retrieve(stripeMemberId, retrieveParams, null);

        Card card = (Card) customer.getSources().retrieve(cardId);

        Card deletedCard = (Card) card.delete();
    }


    /**
     * 生成卡token(鉴定卡对不对)
     *
     * @param cardParam number 卡号  exp_month 有效期月份 exp_year 有效期年份  cvc 安全码
     * @return
     * @throws StripeException
     */
    public String createCardToken (Map<String, Object> cardParam) throws StripeException {
        Stripe.apiKey = key;
        //生成卡片token
        Map<String, Object> params = new HashMap<>();
        params.put("card", cardParam);
        Token token = Token.create(params);
        return token.getId();
    }


    /**
     * 修改银行卡信息
     *
     * @param stripeMemberId 客户stripeId
     * @param stripeCardId   银行卡stripeId
     * @param params         需要修改的参数
     * @throws StripeException
     */
    public void updateCard (String stripeMemberId, String stripeCardId, Map<String, Object> params) throws StripeException {
        Stripe.apiKey = key;
        Map<String, Object> retrieveParams = new HashMap<>();
        List<String> expandList = new ArrayList<>();
        expandList.add("sources");
        retrieveParams.put("expand", expandList);
        Customer customer = Customer.retrieve(stripeMemberId, retrieveParams, null);

        Card card = (Card) customer.getSources().retrieve(stripeCardId);

        card.update(params);
    }

    /**
     * 同步支付
     *
     * @param stripeMemberId 客户stripeId
     * @param money          实际支付金额
     * @param moneyType      货币单位
     * @param description    订单描述
     * @return
     * @throws StripeException
     */
    @Test
    public void charge () throws StripeException {
        String stripeMemberId="cus_JM1MieGLxmep25"; Double money=100D; String moneyType="USD"; String description="456";
        Stripe.apiKey = key;

        //发起支付
        Map<String, Object> payParams = new HashMap<>();
        //1元=100
        payParams.put("amount", money);
        payParams.put("currency", moneyType);
        payParams.put("description", description);
        payParams.put("customer", stripeMemberId);
        payParams.put("capture", true);
        Charge charge = Charge.create(payParams);
        System.err.println(charge.toString());
        //charge  支付是同步通知
        if ("succeeded".equals(charge.getStatus())) {
            //交易成功后，需要更新我们的订单表，修改业务参数，此处省略
//            return true;
        } else {
//            return false;
        }
    }


    /**
     * 捕获stripe平台返回的错误状态码
     * @param code
     */
//        public void catchError(String code){
//            switch (code){
//                case "account_number_invalid":
//                    //银行卡号不正确
//                    throw new RRException(null, ErrorPromptEnum.BANK_STRIPE_account_number_invalid.getValue());
//                case "balance_insufficient":
//                    //账户余额不足
//                    throw new RRException(null, ErrorPromptEnum.BANK_STRIPE_balance_insufficient.getValue());
//                case "bank_account_declined":
//                    //所提供的银行帐户尚未通过验证或不受支持，因此无法用于收费
//                    throw new RRException(null, ErrorPromptEnum.BANK_STRIPE_bank_account_declined.getValue());
//                case "bank_account_exists":
//                    //账户已存在
//                    throw new RRException(null, ErrorPromptEnum.BANK_STRIPE_bank_account_exists.getValue());
//                case "bank_account_unusable":
//                    //提供的银行帐户不能用于付款。必须使用其他银行帐户。
//                    throw new RRException(null, ErrorPromptEnum.BANK_STRIPE_bank_account_unusable.getValue());
//                case "expired_card":
//                    //卡已过期
//                    throw new RRException(null, ErrorPromptEnum.BANK_STRIPE_expired_card.getValue());
//                case "incorrect_cvc":
//                    //卡的安全码不正确
//                    throw new RRException(null, ErrorPromptEnum.BANK_STRIPE_incorrect_cvc.getValue());
//                case "incorrect_number":
//                    //卡号不正确
//                    throw new RRException(null, ErrorPromptEnum.BANK_STRIPE_incorrect_number.getValue());
//                case "incorrect_zip":
//                    //卡的邮政编码不正确
//                    throw new RRException(null, ErrorPromptEnum.BANK_STRIPE_incorrect_zip.getValue());
//                case "instant_payouts_unsupported":
//                    //此卡不支持即时付款
//                    throw new RRException(null, ErrorPromptEnum.BANK_STRIPE_instant_payouts_unsupported.getValue());
//                case "invalid_cvc":
//                    //卡的安全码无效
//                    throw new RRException(null, ErrorPromptEnum.BANK_STRIPE_invalid_cvc.getValue());
//                case "invalid_expiry_month":
//                    //卡的有效期限不正确
//                    throw new RRException(null, ErrorPromptEnum.BANK_STRIPE_invalid_expiry_month.getValue());
//                case "invalid_expiry_year":
//                    //卡的有效期不正确
//                    throw new RRException(null, ErrorPromptEnum.BANK_STRIPE_invalid_expiry_year.getValue());
//                case "invalid_number":
//                    //卡号无效
//                    throw new RRException(null, ErrorPromptEnum.BANK_STRIPE_invalid_number.getValue());
//                default:
//                    //系统异常
//                    throw new RRException(code, ErrorPromptEnum.BANK_STRIPE_error.getValue());
//            }
//        }

}
