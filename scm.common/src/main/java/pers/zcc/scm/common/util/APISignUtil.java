
package pers.zcc.scm.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pers.zcc.scm.common.vo.PageVO;

public class APISignUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(APISignUtil.class);

    private static final long SIGN_EXPIRE = 1000 * 300;

    /**
     * 客户端根据自己的appId，时间戳，请求的接口入参生成签名.并将appId、timestamp、sign附在请求头中
     *
     * @param appId the app id 客户端应用id
     * @param timestamp the timestamp
     * @param parameters 接口入参，包括路径参数,可以是数组,按<Strong>接口形参顺序</Strong>装入数组或集合
     * @param appSecret the app secret 私钥
     * @return rsa 公钥加密密文
     * @throws Exception the exception
     */
    public static String sign(String appId, long timestamp, Object parameters, String appSecret) throws Exception {
        String objectFields = buildParamString(parameters);
        String str2Encrypt = "&" + appId + "&" + timestamp + "&" + objectFields;
        LOGGER.debug("str2Encrypt:" + str2Encrypt);
        return RSAUtil.sign(str2Encrypt.getBytes("UTF-8"), appSecret);
    }

    /**
     * 服务端使用公钥验签防参数篡改，接口有效期300s.
     *
     * @param appId the app id 客户端应用id，请求头appId
     * @param timestamp the timestamp 请求头 timestamp
     * @param parameters the parameters
     *            请求接口入参，包括路径参数,可以是数组,按<Strong>接口形参顺序</Strong>装入数组或集合
     * @param sign the sign 客户端应用的appId和客户端应用的私钥生成的签名串
     * @param appPublicKey the app public key 服务端保有的公钥
     * @return true, if successful
     * @throws Exception the exception
     */
    public static boolean verify(String appId, long timestamp, Object parameters, String sign, String appPublicKey)
            throws Exception {
        long now = System.currentTimeMillis();
        String objectFields = buildParamString(parameters);
        String content = "&" + appId + "&" + timestamp + "&" + objectFields;
        LOGGER.debug("content:" + content);
        return (now - timestamp < SIGN_EXPIRE) && RSAUtil.verify(content.getBytes("UTF-8"), appPublicKey, sign);
    }

    private static String buildParamString(Object parameters) {
        if (parameters == null) {
            return "";
        }
        String objectFields;
        if (parameters instanceof Object[]) {
            StringBuilder builder = new StringBuilder();
            for (Object o : ((Object[]) parameters)) {
                builder.append(getObjectFields(o));
            }
            objectFields = builder.toString();
        } else if (parameters instanceof List) {
            StringBuilder builder = new StringBuilder();
            ((List<?>) parameters).forEach(it -> builder.append(getObjectFields(it)));
            objectFields = builder.toString();
        } else if (parameters instanceof String || parameters instanceof Number || parameters instanceof Boolean) {
            objectFields = StringUtils.isEmpty(parameters.toString()) ? "" : parameters.toString() + "&";
        } else {
            objectFields = getObjectFields(parameters);
        }
        return objectFields;
    }

    private static String getObjectFields(Object object) {
        if (object == null) {
            return "";
        }
        if (object instanceof String || object instanceof Number || object instanceof Boolean) {
            return StringUtils.isEmpty(object.toString()) ? "" : object.toString() + "&";
        }
        final Field[] fields = object.getClass().getDeclaredFields();
        final TreeMap<String, Object> treeMap = new TreeMap<>();
        Stream.of(fields).map(Field::getName).forEach(it -> treeMap.put(it, getFieldValueByName(it, object)));
        final StringBuilder builder = new StringBuilder();
        treeMap.forEach((k, v) -> {
            if (!StringUtils.isEmpty(v.toString())) {
                builder.append(v);
            }
        });
        return builder.toString();
    }

    private static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            final Method method = o.getClass().getMethod(getter, new Class[] {});
            final Object value = method.invoke(o, new Object[] {});
            if (null == value) {
                return "";
            }
            if (value instanceof List) {
                return ((List<?>) value).stream().map(it -> {
                    if (it instanceof String || it instanceof Number || it instanceof Boolean) {
                        return StringUtils.isEmpty(it.toString()) ? "" : it.toString() + "&";
                    } else {
                        return getObjectFields(it);
                    }
                }).reduce((it1, it2) -> it1 + it2).get();
            } else if (value instanceof String || value instanceof Number || value instanceof Boolean) {
                return StringUtils.isEmpty(value.toString()) ? "" : value.toString() + "&";
            } else {
                return getObjectFields(value);
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static String getAppPublicKey(String appId) {
        if (EnvironmentProps.getApplicationProp("application.common.pcms.appId").equalsIgnoreCase(appId)) {
            return EnvironmentProps.getApplicationProp("application.common.pcms.appPublicKey");
        }
        return EnvironmentProps.getApplicationProp(appId + ".appPublicKey");
    }

    public static void main(String[] args) {
        Object[] parameters = new Object[3];
        parameters[0] = "2021-05-01 00:00:00";
        parameters[1] = "2021-06-15 00:00:00";
        PageVO pagevo = new PageVO();
        pagevo.setPageNum(1);
        pagevo.setPageSize(5);
        parameters[2] = pagevo;
        long timestamp = System.currentTimeMillis();
        System.out.println("timestamp:" + timestamp);
        String sign = "";
        String appId = "";
        String appPublicKey = "";
        String appSecret = "";
        try {
            appId = "www.srcloud.com";
            appPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCwOqcFCgrXYrFmuC61WUw2Vue7jcwAk4ViIBi4j2/3sh88eD+kRooEo1fxqEBxJNMFFx9ufQ8ZuHHU7slzdooje7VwMRsobwa3Q97a6XDXliXkj3/1O4CLT3Npxjj7tOpZwbDQGd1I/bR2pVOkha016vmKsyfbah8h5tll4E/MaQIDAQAB";
            appSecret = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALA6pwUKCtdisWa4LrVZTDZW57uNzACThWIgGLiPb/eyHzx4P6RGigSjV/GoQHEk0wUXH259Dxm4cdTuyXN2iiN7tXAxGyhvBrdD3trpcNeWJeSPf/U7gItPc2nGOPu06lnBsNAZ3Uj9tHalU6SFrTXq+YqzJ9tqHyHm2WXgT8xpAgMBAAECgYEAkE12tgmjFvsZiKMInDai0VwYJNvXUCF79v2RQI8zf/+61Cki88FPAr0+JgM/wWYF5QUqF68Pzl/Z1iIxkvpd4ZOLYYxy9xrCV3E0olKqjietaTA+gM4RvHT/I1qxYOxMdrh02HzXsRvgDFKu6QYOZmtvTfYUcoluykUYYs/rQgECQQDkOR6CEiNpzPZCns74VCpZnvvaxLw7SsMCiU+8hu5aCF1VwDLJcsxOE+9waSkOQwxPfjntjlxcMmVuMQCXOBmlAkEAxa2GinQDRpezoXqSjCWG1Vq2MFgRtXlblDmEnpYjMg92XkxuXXeMd/q1EOmWO8SgZrZsh3VGvD+vjbYYxDeEdQJAZwEsttIO77KVNw5MJaJ/FsH9tAh1WGFu0Jc+yL0xmKsLzScZBPjuIxM2T0r3P7udM8epx4EoSGhqhShStu2YDQJAESgWXT3kauQPuxwgS/mV0j6lQVzjbJSz1hGH6RuuwlGFPHn1ujb+AgvIW0dqupU+Nqvuj3MmSIBehDYhwCXxYQJBAKMas8KqT6SjyquRKNMrDGYl2AQq74K8oPtgQO03r3au2nEyXpqo9M2+BxY87pT3frI5gN71J9XSwlHdLAzmj1U=";
            // Map<String, Object> map = RSAUtil.genKeyPair();
            // appPublicKey = RSAUtil.getPublicKey(map);
            // appSecret = RSAUtil.getPrivateKey(map);
            System.out.println("publicKey:" + appPublicKey);
            System.out.println("privateKey:" + appSecret);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            sign = sign(appId, timestamp, parameters, appSecret);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("sign:" + sign);
        try {
            System.out.println(verify(appId, timestamp, parameters, sign, appPublicKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
