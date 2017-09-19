package com.patrick.example.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Desciption
 * Create By  li.bo
 * CreateTime 2017/8/21 17:42
 * UpdateTime 2017/8/21 17:42
 */
@Component
public class TestMerchantRisk {

    public static final String SH_15 = "SH15";
    public static final String HUIFA_QIANKUAN = "huifa_qiankuan";
    private RestTemplate template;
    Connection conn;
    String driver = "com.mysql.jdbc.Driver";// the MySQL driver
    String url = "jdbc:mysql://rm-pz5dtox1qvw4j81pbo.mysql.rds.aliyuncs.com:3306/supportcenter";// URL points to destination database to manipulate
    String user = "bsfit";//user name for the specified database
    String pwd = "f!2E$5-Op";//the corresponding password
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

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
        TestMerchantRisk batchUtils = new TestMerchantRisk();

        try {
            con = batchUtils.getConnection();
            String sql = "SELECT DISTINCT (a.PARTNER_CODE), b.*, c.huifa_zhixin, c.huifa_shixin, c.huifa_zuifan, c.huifa_qianshui, c.huifa_qiankuan FROM test_jinqiao_mall a LEFT JOIN test_merchant b ON b.merchant_id = a.PARTNER_CODE LEFT JOIN test_jinqiao_huifa_copy c ON c.shop_id = a.shop_id";
            ps = con.prepareStatement(sql);
            /*for (int i = 0; i < 100; i++) {
                //关键方法1：打包
            }*/
            ps.addBatch();
            ResultSet is = ps.executeQuery();
            while (is.next()) {
                Map<String, Object> map = Maps.newHashMap();
                map.put("frms_biz_code", "PAY.REG");
                map.put("frms_user_id", "user00001");
                map.put("frms_flow_id", "125");
                map.put("frms_customer_id", "homefax");

                map.put("frms_sh_booth_cnt", is.getInt("frms_sh_booth_cnt"));
                map.put("frms_sh_booth_optimal_lev", is.getInt("frms_sh_booth_optimal_lev"));
                map.put("frms_sh_lst12_rent_arrears", is.getLong("frms_sh_lst12_rent_arrears"));
                map.put("frms_sh_lst24_max_overdue", is.getInt("frms_sh_lst24_max_overdue"));
                map.put("frms_sh_if_overdue", is.getInt("frms_sh_if_overdue"));
                map.put("frms_sh_min_opentime", sdf.parse(is.getString("frms_sh_min_opentime")));
                map.put("frms_sh_lst6_sal_amt", is.getLong("frms_sh_lst6_sal_amt"));
                map.put("frms_sh_lst12_sal_amt", is.getLong("frms_sh_lst12_sal_amt"));
                map.put("frms_sh_lst18_sal_amt", is.getLong("frms_sh_lst18_sal_amt"));
                map.put("frms_sh_lst12_rent_amt", is.getLong("frms_sh_lst12_rent_amt"));
//                map.put("frms_sh_if_lawxp_black", is.getInt("match_huifa"));
                map.put("frms_sh_id", is.getString("merchant_id"));
                map.put("frms_sh_hcomplain_cnt", is.getInt("frms_sh_hcomplain_cnt"));
                map.put("frms_sh_mcomplain_cnt", is.getInt("frms_sh_mcomplain_cnt"));
                map.put("frms_sh_overdue_balance_amt", is.getLong("frms_sh_overdue_balance_amt"));

                // 汇法-接口数据
                map.put("frms_huifa_zhixin", is.getInt("huifa_zhixin"));
                map.put("frms_huifa_shixin", is.getInt("huifa_shixin"));
                map.put("frms_huifa_zuifan", is.getInt("huifa_zuifan"));
                map.put("frms_huifa_qianshui", is.getInt("huifa_qianshui"));
                map.put("frms_huifa_qiankuan", is.getInt("huifa_qiankuan"));

                /*map.put("huifa_xiangao", 1);
                map.put("huifa_xianchu", 1);
                map.put("huifa_caipan", 1);
                map.put("huifa_shenpan", 1);
                map.put("huifa_weifa", 1);
                map.put("huifa_feizheng", 1);*/

                System.out.println(map.toString());

                String riskResult = sendRequest(map, "flow6");

                int isAccess = isAccess(riskResult, "if_sh_access");
                System.out.println("风控结果：" + isAccess);

                String merchantId = is.getString("merchant_id");
                ps = con.prepareStatement("UPDATE test_jinqiao_mall SET shaccess=" + isAccess + " WHERE PARTNER_CODE=" + merchantId);
                int t = ps.executeUpdate();
                System.out.println("插入数据库结果：" + t);

                // 插入风控结果表test_jinqiao_sh_result
                Double score = 0.0;
                if (isAccess == 1) {
                    score = getScore(riskResult, "result");
                }
                ps = con.prepareStatement("INSERT INTO test_jinqiao_sh_rules VALUES("+ merchantId+","
                        +isAccess+","
                        +score+","
                        +getRules(riskResult, "SH01 :")+","
                        +getRules(riskResult, "SH02 :")+","
                        +getRules(riskResult, "SH03 :")+","
                        +getRules(riskResult, "SH04 :")+","
                        +getRules(riskResult, "SH05 :")+","
                        +getRules(riskResult, "SH06 :")+","
                        +getRules(riskResult, "SH07 :")+","
                        +getRules(riskResult, "SH08 :")+","
                        +getRules(riskResult, "SH09 :")+","
                        +getRules(riskResult, "SH10 :")+","
                        +getRules(riskResult, "SH11 :")+","
                        +getRules(riskResult, "SH12 :")+","
                        +getRules(riskResult, "SH13 :")+","
                        +getRules(riskResult, "SH14 :")+","
                        +getRules(riskResult, "SH15 :")+","
                        +"NOW()"+
                        ")");

                int t2 = ps.executeUpdate();
                System.out.println("插入数据库结果：" + t2);

            }
        } finally {
            //TODO
        }
    }

    /**
     * 风控返回结果，是否准入
     * @param riskResult
     * @param accessType
     * @return
     */
    public int isAccess(String riskResult, String accessType) {
        if (StringUtils.isBlank(riskResult)) {
            return 0;
        }
        int a = riskResult.lastIndexOf(accessType);
        if (a < 0 || a == 0 || a > riskResult.length()) {
            return 0;
        }
        String result = riskResult.substring(a+accessType.length()+2, a+accessType.length()+3);
        if (result == null) {
            return 0;
        }
        return Integer.parseInt(result);
    }

    /**
     * X规则是否触发
     * @param riskResult
     * @param rule
     * @return
     */
    public int getRules(String riskResult, String rule) {
        if (StringUtils.isBlank(riskResult)) {
            return 0;
        }

        int ruleNum = 0;
       if(riskResult.contains(rule)){
           ruleNum = 1;
       }
        return ruleNum;

    }

    /**
     * 准入通过后，获取最终评分
     * @param riskResult
     * @param type
     * @return
     */
    public Double getScore(String riskResult, String type) {
        if (StringUtils.isBlank(riskResult)) {
            return 0.0;
        }
        Double score = 0.0;
        if (riskResult.lastIndexOf(type) > 0) {
            String str = riskResult.substring(riskResult.lastIndexOf(type));
            String score_str = str.substring(type.length()+2, str.indexOf(",")-1);
            if (StringUtils.isNotBlank(score_str)) {
                score = Double.parseDouble(score_str);
            }
            System.out.println(score);
        }
        return score;
    }

    /**
     * 店铺准入
     * @throws SQLException
     * @throws ParseException
     * @throws UnsupportedEncodingException
     */
    @Test
    public void shopAccess() throws SQLException, ParseException, UnsupportedEncodingException {

        Connection con;
        PreparedStatement ps = null;
        ResultSet rs = null;
        TestMerchantRisk batchUtils = new TestMerchantRisk();

        try {
            con = batchUtils.getConnection();
            String sql = "SELECT DISTINCT(a.shop_id), b.*, a.shaccess AS shaccess, a.gcaccess AS gcaccess, a.ppaccess AS ppaccess FROM test_jinqiao_mall a LEFT JOIN test_shop b ON b.frms_dp_id = a.shop_id";
            ps = con.prepareStatement(sql);
            /*for (int i = 0; i < 100; i++) {
                //关键方法1：打包
            }*/
            ps.addBatch();
            ResultSet is = ps.executeQuery();
            while (is.next()) {
                // 店铺所有规则需要触发，方可准入
                Map<String, Object> map = Maps.newHashMap();
                map.put("frms_biz_code", "PAY.REG");
                map.put("frms_user_id", is.getString("frms_dp_id"));
                map.put("frms_flow_id", "125");
                map.put("frms_customer_id", "homefax");

                map.put("frms_dp_lst90d_sale_amt", is.getLong("frms_dp_lst90d_sale_amt"));
                map.put("frms_dp_avg_amt", is.getLong("frms_dp_avg_amt"));
                map.put("frms_dp_avg_rentamt", is.getLong("frms_dp_avg_rentamt"));
                if (is.getString("frms_dp_early_deal_time") == null) {
                    map.put("frms_dp_early_deal_time", sdf1.parse("2017-9-14"));
                }else{
                    map.put("frms_dp_early_deal_time", sdf1.parse(is.getString("frms_dp_early_deal_time")));
                }
                map.put("frms_dp_lst90d_sale_cnt", is.getInt("frms_dp_lst90d_sale_cnt"));
                map.put("frms_dp_lst30d_sale_cnt", is.getInt("frms_dp_lst30d_sale_cnt"));
                map.put("frms_dp_lst30d_sale_amt", is.getLong("frms_dp_lst30d_sale_amt"));
                map.put("frms_dp_booth_lev", is.getInt("frms_dp_booth_lev"));

                // 无数据，默认值设置触发规则
                map.put("frms_dp_lst2_firstoverdue3d_per", 0.2);
                map.put("frms_dp_lst2_first_refund_cnt", 5);

                /*// 此4条，实际入参中传入，与四个准入结果冲突
                map.put("frms_gc_id", is.getString("frms_gc_id"));
                map.put("frms_sc_id", is.getString("frms_sc_id"));
                map.put("frms_sh_id", is.getString("frms_sh_id"));
                map.put("frms_pp_id", is.getString("frms_pp_id"));*/

                // 实际决策流传入的是四个ID，此处传入决策结果
                map.put("frms_gc_id_access", is.getLong("gcaccess")==1? true : false);
                map.put("frms_sc_id_access", true);
                map.put("frms_sh_id_access", is.getString("shaccess")=="1"? true : false);
                map.put("frms_pp_id_access", is.getLong("ppaccess")==1? true : false);

                System.out.println("ttttt:" + is.getString("gcaccess"));

                System.out.println(map.toString());

                String riskResult = sendRequest(map, "flow3");

                int isAccess = isAccess(riskResult, "if_dp_access");
                System.out.println("风控结果：" + isAccess);

                String shopId = is.getString("frms_dp_id");
                // 风控准入结果存入test_jinqiao_mall中
                ps = con.prepareStatement("UPDATE test_jinqiao_mall SET dpaccess=" + isAccess + " WHERE shop_id=" + shopId);
                int t = ps.executeUpdate();
                System.out.println("插入数据库结果：" + t);

                // 风控策略，触发规则存入test_jinqiao_rules中
                ps = con.prepareStatement("INSERT INTO test_jinqiao_dp_rules VALUES("+ shopId+","
                        +isAccess+","
                        +getRules(riskResult, "DP01")+","
                        +getRules(riskResult, "DP02")+","
                        +getRules(riskResult, "DP03")+","
                        +getRules(riskResult, "DP04")+","
                        +getRules(riskResult, "DP05")+","
                        +getRules(riskResult, "DP06")+","
                        +getRules(riskResult, "DP09")+","
                        +getRules(riskResult, "DP12")+","
                        +"now())");

                int t2 = ps.executeUpdate();
                System.out.println("插入数据库结果：" + t2);

            }
        } finally {
            //TODO
        }
    }

    /**
     * 汇法接口调用，跑测试
     * @throws SQLException
     * @throws ParseException
     * @throws IOException
     */
//    @Test
    public void huifaService() throws SQLException, ParseException, IOException {

        Connection con;
        PreparedStatement ps = null;
        ResultSet rs = null;
        TestMerchantRisk batchUtils = new TestMerchantRisk();

        try {
            con = batchUtils.getConnection();
            String sql = "select DISTINCT(PARTNER_CODE) as merchantId, a.* from test_jinqiao_mall a";
            ps = con.prepareStatement(sql);
            /*for (int i = 0; i < 100; i++) {
                //关键方法1：打包
            }*/
            ps.addBatch();
            ResultSet is = ps.executeQuery();
            while (is.next()) {
                JSONObject result = sendRequest2Today("queryPreciseInfo", is.getString("MERCHANTSNAME"), is.getString("IDNUM"));
                if (result != null) {
                    int huifa = result.getInteger("fxmsgnum");
                    System.out.println("汇法结果：" + huifa);
                    int hf = 0;
                    if (huifa > 0) {
                        hf = 1;
                    }

                    JSONObject fxcontent = result.getJSONObject("fxcontent");
                    int huifa_zhixin = fxcontent.getJSONArray("zhixin") == null? 0:fxcontent.getJSONArray("zhixin").size();
                    int huifa_shixin = fxcontent.getJSONArray("shixin") == null? 0:fxcontent.getJSONArray("shixin").size();
                    int huifa_xiangao = fxcontent.getJSONArray("xiangao") == null? 0:fxcontent.getJSONArray("xiangao").size();
                    int huifa_xianchu = fxcontent.getJSONArray("xianchu") == null? 0:fxcontent.getJSONArray("xianchu").size();
                    int huifa_caipan = fxcontent.getJSONArray("caipan") == null? 0:fxcontent.getJSONArray("caipan").size();
                    int huifa_shenpan = fxcontent.getJSONArray("shenpan") == null? 0:fxcontent.getJSONArray("shenpan").size();
                    int huifa_zuifan = fxcontent.getJSONArray("zuifan") == null? 0:fxcontent.getJSONArray("zuifan").size();
                    int huifa_weifa = fxcontent.getJSONArray("weifa") == null? 0:fxcontent.getJSONArray("weifa").size();
                    int huifa_qianshui = fxcontent.getJSONArray("qianshui") == null? 0:fxcontent.getJSONArray("qianshui").size();
                    int huifa_feizheng = fxcontent.getJSONArray("feizheng") == null? 0:fxcontent.getJSONArray("feizheng").size();
                    int huifa_qiankuan = fxcontent.getJSONArray("qiankuan") == null? 0:fxcontent.getJSONArray("qiankuan").size();

                    String merchantId = is.getString("merchantId");
//                    ps = con.prepareStatement("UPDATE test_jinqiao_mall SET match_huifa=" + hf + " WHERE shop_id=" + shopId);
                    ps = con.prepareStatement("INSERT INTO test_jinqiao_huifa VALUES("+ merchantId+","
                            +huifa_zhixin+","
                            +huifa_shixin+","
                            +huifa_xiangao+","
                            +huifa_xianchu+","
                            +huifa_caipan+","
                            +huifa_shenpan+","
                            +huifa_zuifan+","
                            +huifa_weifa+","
                            +huifa_qianshui+","
                            +huifa_feizheng+","
                            +huifa_qiankuan+")");

                    int t = ps.executeUpdate();
                    System.out.println("插入数据库结果：" + t);
                }
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
    private String sendRequest(Map<String, Object> map, String flow) {
        String flow_url = "http://139.224.144.178:9181/flow/syncAudit?processKey=" + flow;
        String flow_result = template.postForObject(flow_url, map, String.class);
        System.out.println("before: " + flow_result);
        flow_result = BatchUtils.unicodeToString(flow_result);
        System.out.println("after: " + flow_result);
        return flow_result;
    }

    /**
     * send request to gootoday for getting data
     * @param method
     * @param name
     * @param idNum
     * @return
     */
    public JSONObject sendRequest2Today(String method, String name, String idNum) throws IOException {
        String today_url = "http://tgapitest.homefax.cn/web/portal";
        JSONObject jo = new JSONObject();
        jo.put("method", method); // queryPreciseInfo
        jo.put("id", idNum);
        jo.put("n", name);
        jo.put("stype", "1");
        if (name.length() > 5) {
            jo.put("stype", "2");
        }

        String result = Demo.doHttpPost(jo.toString(), today_url);
        System.out.println(result);

        JSONObject resultObject = JSONObject.parseObject(result);
        if (!StringUtils.equals("0000", resultObject.getString("state"))) {
            return null;
        }

        return resultObject.getJSONObject("data");

    }

    public static void main(String[] args) throws SQLException, ParseException, IOException {
        TestMerchantRisk b = new TestMerchantRisk();
        JSONObject obj = b.sendRequest2Today("queryPreciseInfo", "上海怡野装饰设计有限公司", "91310120MA1HLNLQ82");
        System.out.println(obj == null? "": obj.toString());
    }
}
