package com.patrick.example.util;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Desciption
 * Create By  li.bo
 * CreateTime 2017/8/21 17:42
 * UpdateTime 2017/8/21 17:42
 */
@Component
public class BatchUtils {

    private RestTemplate template;
    Connection conn;
    String driver = "com.mysql.jdbc.Driver";// the MySQL driver
    String url = "jdbc:mysql://rm-pz5dtox1qvw4j81pbo.mysql.rds.aliyuncs.com:3306/consumerfinance_dev";// URL points to destination database to manipulate
    String user = "bsfit";//user name for the specified database
    String pwd = "f!2E$5-Op";//the corresponding password
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * create db connection
     * @return
     */
    public Connection getConnection() {
        try {
            Class.forName(driver);// add MySQL driver
            System.out.println("Database driver is successfully added");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection(url, user, pwd);//create a connection object
            System.out.println("Database connection is successful");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * initialize Resttemplate header
     */
    @Before
    public void init() {
//        ParserConfig.getGlobalInstance().addAccept("cn.com.bsfit.frms.workflow.obj.");
        int poolSize = 5;
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager();
        connMgr.setMaxTotal(poolSize + 1);
        connMgr.setDefaultMaxPerRoute(poolSize);
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connMgr).build();

        template = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
        List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
        FastJsonHttpMessageConverter fastjson = new FastJsonHttpMessageConverter();

        FastJsonConfig fastJsonConfig = new FastJsonConfig();

        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteClassName, SerializerFeature.BrowserCompatible,
                SerializerFeature.DisableCircularReferenceDetect);
        fastjson.setFastJsonConfig(fastJsonConfig);

        List<MediaType> supportedMediaTypes = Lists.newArrayList();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastjson.setSupportedMediaTypes(supportedMediaTypes);

        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        converters.add(stringHttpMessageConverter);
        converters.add(fastjson);
        template.setMessageConverters(converters);
    }

    /**
     * merchant
     *
     * @throws SQLException
     * @throws ParseException
     * @throws UnsupportedEncodingException
     */
    @Test
    public void merchantAccess() throws SQLException, ParseException, UnsupportedEncodingException {

        Connection con;
        PreparedStatement ps = null;
        ResultSet rs = null;
        BatchUtils batchUtils = new BatchUtils();

        try {
            con = batchUtils.getConnection();
            String sql = "select * from merchant_info limit 10";
            ps = con.prepareStatement(sql);
            for (int i = 0; i < 100; i++) {
                //关键方法1：打包
                ps.addBatch();
            }
            ResultSet is = ps.executeQuery();
            while (is.next()) {
                System.out.println(is.getString("frms_sh_min_opentime"));
                Map<String, Object> map = Maps.newHashMap();
                map.put("frms_biz_code", "PAY.REG");
                map.put("frms_user_id", "user00001");
                map.put("frms_flow_id", "125");
                map.put("frms_customer_id", "homefax");

                map.put("merchant_id", is.getString("merchant_id"));
                map.put("frms_sh_booth_cnt", is.getInt("frms_sh_booth_cnt"));
                map.put("frms_sh_booth_optimal_lev", is.getInt("frms_sh_booth_optimal_lev"));
                map.put("frms_sh_lst12_rent_arrears", is.getInt("frms_sh_lst12_rent_arrears"));
                map.put("frms_sh_min_opentime", sdf.parse(is.getString("frms_sh_min_opentime")));
                map.put("frms_sh_lst12_sal_amt", is.getDouble("frms_sh_lst12_sal_amt"));
                map.put("frms_sh_lst12_rent_amt", is.getDouble("frms_sh_lst12_rent_amt"));
                map.put("frms_sh_lst6_sal_amt", is.getDouble("frms_sh_lst6_sal_amt"));
                map.put("frms_sh_lst12_sal_amt", is.getDouble("frms_sh_lst12_sal_amt"));
                map.put("frms_sh_lst18_sal_amt", is.getDouble("frms_sh_lst18_sal_amt"));
                map.put("frms_sh_lst6_rent_amt", is.getDouble("frms_sh_lst6_rent_amt"));
                map.put("frms_sh_hcomplain_cnt", is.getDouble("frms_sh_hcomplain_cnt"));
                map.put("frms_sh_mcomplain_cnt", is.getDouble("frms_sh_mcomplain_cnt"));

                map.put("frms_refund_risk_level", "低"); // 商户店内申请消费贷客户退货高危名单
                map.put("frms_overdue_risk_level", "低"); // 商户店内申请消费贷客户信用高危名单
                map.put("frms_sh_if_lawxp_black", "0"); // 商户法人征信
                map.put("frms_sh_if_overdue", "0"); // 商户贷款情况
                map.put("frms_sh_lst24_max_overdue", 2);

                /*map.put("frms_sh_if_refound_hlist", "0");
                map.put("frms_sh_if_credit_hlist", "0");
                map.put("frms_sh_if_refound_mlist", "0");
                map.put("frms_sh_if_credit_mlist", "0");

                map.put("frms_sh_overdue_balance_amt", 0);

                map.put("frms_sh_lst6_todaybuy_amt", 100);
                map.put("frms_sh_1st6_todaybuy_overdue_amt", 2);
                map.put("frms_sh_1st6_todaybuy_customs", 50);*/
                System.out.println(map.toString());

                sendRequest(map, "flow6");
            }
        } finally {
            //TODO
        }
    }

    /**
     * factory
     *
     * @throws SQLException
     * @throws ParseException
     * @throws UnsupportedEncodingException
     */
    @Test
    public void factoryAccess() throws SQLException, ParseException, UnsupportedEncodingException {

        Connection con;
        PreparedStatement ps = null;
        ResultSet rs = null;
        BatchUtils batchUtils = new BatchUtils();

        try {
            con = batchUtils.getConnection();
            String sql = "select * from factory_info limit 10";
            ps = con.prepareStatement(sql);
            //关键方法1：打包
            ps.addBatch();
            ResultSet is = ps.executeQuery();
            while (is.next()) {

                Map<String, Object> map = Maps.newHashMap();
                // 请求头
                map.put("frms_biz_code", "PAY.REG");
                map.put("frms_user_id", "user00001");
                map.put("frms_flow_id", "125");
                map.put("frms_customer_id", "homefax");

                map.put("factory_id", is.getString("factory_id"));
                map.put("frms_gc_store_cnt", is.getDouble("frms_gc_store_cnt"));
                map.put("frms_gc_lst12_sal_amt", is.getDouble("frms_gc_lst12_sal_amt"));
                map.put("frms_gc_lst24_sal_amt", is.getDouble("frms_gc_lst24_sal_amt"));
                map.put("frms_gc_brand_control", is.getString("frms_gc_brand_control"));

//                map.put("frms_gc_if_abnormal", "0");
//                map.put("frms_gc_if_blacklist", "0");
//                map.put("frms_gc_managetime", 30);
                map.put("frms_gc_if_overdue", "0"); // 直营商户贷款逾期
                map.put("frms_gc_max_overdue", 2);

                sendRequest(map, "flow2");
            }
        } finally {
            //TODO
        }
    }

    /**
     * brand
     *
     * @throws SQLException
     * @throws ParseException
     * @throws UnsupportedEncodingException
     */
    @Test
    public void brandAccess() throws SQLException, ParseException, UnsupportedEncodingException {

        Connection con;
        PreparedStatement ps = null;
        ResultSet rs = null;
        BatchUtils batchUtils = new BatchUtils();

        try {
            con = batchUtils.getConnection();
            String sql = "select * from brand_info limit 10";
            ps = con.prepareStatement(sql);
            //关键方法1：打包
            ps.addBatch();
            ResultSet is = ps.executeQuery();
            while (is.next()) {

                Map<String, Object> map = Maps.newHashMap();
                // 请求头
                map.put("frms_biz_code", "PAY.REG");
                map.put("frms_user_id", "user00001");
                map.put("frms_flow_id", "125");
                map.put("frms_customer_id", "homefax");

                map.put("brand_id", is.getString("brand_id"));
                map.put("frms_pp_store_cnt", is.getInt("frms_pp_store_cnt"));
                map.put("frms_pp_lst18_sal_amt", is.getDouble("frms_pp_lst18_sal_amt"));
                map.put("frms_pp_lst12_sal_amt", is.getDouble("frms_pp_lst12_sal_amt"));
                map.put("frms_pp_lst6_sal_amt", is.getDouble("frms_pp_lst6_sal_amt"));

                sendRequest(map, "flow4");
            }
        } finally {
            //TODO
        }
    }

    /**
     * mall
     *
     * @throws SQLException
     * @throws ParseException
     * @throws UnsupportedEncodingException
     */
    @Test
    public void mallAccess() throws SQLException, ParseException, UnsupportedEncodingException {

        Connection con;
        PreparedStatement ps = null;
        ResultSet rs = null;
        BatchUtils batchUtils = new BatchUtils();

        try {
            con = batchUtils.getConnection();
            String sql = "select * from mall_info limit 10";
            ps = con.prepareStatement(sql);
            //关键方法1：打包
            ps.addBatch();
            ResultSet is = ps.executeQuery();
            while (is.next()) {

                Map<String, Object> map = Maps.newHashMap();
                // 请求头
                map.put("frms_biz_code", "PAY.REG");
                map.put("frms_user_id", "user00001");
                map.put("frms_flow_id", "125");
                map.put("frms_customer_id", "homefax");

                map.put("frms_sc_opentime", sdf.parse(is.getString("open_date")));
                map.put("frms_sc_if_citys", is.getString("frms_sc_if_citys"));
                map.put("frms_sc_if_normal", is.getString("frms_sc_if_normal"));

                map.put("frms_sc_partner_property", is.getString("frms_sc_partner_property"));
                map.put("frms_sc_lst12_sal_amt", is.getDouble("frms_sc_lst12_sal_amt"));
                map.put("frms_sc_lst12_rent_per", is.getDouble("frms_sc_lst12_rent_per"));
                map.put("frms_sc_lst12_capture_per", is.getDouble("frms_sc_lst12_capture_per"));

                sendRequest(map, "flow5");
            }
        } finally {
            //TODO
        }
    }

    /**
     * shop
     *
     * @throws SQLException
     * @throws ParseException
     * @throws UnsupportedEncodingException
     */
    @Test
    public void shopAccess() throws SQLException, ParseException, UnsupportedEncodingException {

        Connection con;
        PreparedStatement ps = null;
        ResultSet rs = null;
        BatchUtils batchUtils = new BatchUtils();

        try {
            con = batchUtils.getConnection();
            String sql = "select * from shop_info limit 10";
            ps = con.prepareStatement(sql);
            //关键方法1：打包
            ps.addBatch();
            ResultSet is = ps.executeQuery();
            while (is.next()) {

                Map<String, Object> map = Maps.newHashMap();
                // 请求头
                map.put("frms_biz_code", "PAY.REG");
                map.put("frms_user_id", "user00001");
                map.put("frms_flow_id", "125");
                map.put("frms_customer_id", "homefax");

                map.put("frms_dp_id", is.getString("frms_dp_id"));
                map.put("frms_dp_lst12_avg_amt", is.getString("frms_dp_lst12_avg_amt")); // >= 20000
                map.put("frms_dp_lst12_avg_rentamt", 10000); // 不大于月均销售额的一半
                map.put("frms_dp_booth_lev", is.getInt("frms_dp_booth_lev")); // < 9
                map.put("frms_dp_lst12_amt0_mth", 1); // <= 3
                map.put("frms_dp_opentime", sdf.parse(is.getString("frms_dp_opentime") == null ? "2014-06-21 00:00:00" : is.getString("frms_dp_opentime"))); // 距今月份大于12
                map.put("frms_dp_lst18_sale_amt", is.getDouble("frms_dp_lst18_sale_amt")); // 近6月同比销售额下降幅度
                map.put("frms_dp_lst12_sale_amt", is.getDouble("frms_dp_lst12_sale_amt"));
                map.put("frms_dp_lst6_sale_amt", is.getDouble("frms_dp_lst6_sale_amt"));
                map.put("frms_dp_lst2_firstoverdue3d_per", 0.2); // > 0.5
                map.put("frms_dp_lst2_first_refund_cnt", 39); // > 10

                /*map.put("frms_dp_lst12_avg_amt", 300000); // >= 20000
                map.put("frms_dp_lst12_avg_rentamt", 10000); // 不大于月均销售额的一半
                map.put("frms_dp_booth_lev", 6); // < 9
                map.put("frms_dp_lst12_amt0_mth", 1); // <= 3
                map.put("frms_dp_opentime", sdf.parse("2014-06-21 00:00:00")); // 距今月份大于12
                map.put("frms_dp_lst18_sale_amt", 40); // 近6月同比销售额下降幅度
                map.put("frms_dp_lst12_sale_amt", 20);
                map.put("frms_dp_lst6_sale_amt", 12);
                map.put("frms_dp_lst2_firstoverdue3d_per", 0.2); // > 0.5
                map.put("frms_dp_lst2_first_refund_cnt", 39); // > 10*/

                // 另外四个决策流的结果
                map.put("frms_gc_id_access", true);
                map.put("frms_sc_id_access", true);
                map.put("frms_sh_id_access", true);
                map.put("frms_pp_id_access", true);

                sendRequest(map, "flow3");
            }
        } finally {
            //TODO
        }
    }

    /**
     * send request to bangsun
     *
     * @param map
     */
    private void sendRequest(Map<String, Object> map, String flow) {
        String flow_url = "http://139.224.144.178:9181/flow/syncAudit?processKey=" + flow;
        String flow_result = template.postForObject(flow_url, map, String.class);
        System.out.println("before: " + flow_result);
        flow_result = unicodeToString(flow_result);
        System.out.println("after: " + flow_result);
    }

    /**
     * unicode转中文
     *
     * @param unicode
     * @return
     */
    public static String unicodeToString(String unicode) {

        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(unicode);

        char ch;
        while (matcher.find()) {

            ch = (char) Integer.parseInt(matcher.group(2), 16);
            unicode = unicode.replace(matcher.group(1), ch + "");
        }

        return unicode;
    }

    @Scheduled(initialDelay = 1000,fixedDelay=60000)
    public void timerInit() {
        System.out.println("init : " + 1);
    }

    @PostConstruct
    public void postInit() {
        System.out.println("post : " + 2);
    }

    public static void main(String[] args) throws SQLException, ParseException {
        /*BatchUtils b = new BatchUtils();
        createBatch();*/

    }
}
