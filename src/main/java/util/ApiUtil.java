package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ApiUtil {

    public static <T, P> P postMethod(String url, T request, Class<P> responseClass, String cookie, ContentType contentType) {
        CloseableHttpResponse response = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(url);

            String body;
            if(contentType.equals(ContentType.REQUEST_PAYLOAD)){
                post.addHeader("content-type", "application/json");
                body = buildBody(request);
            }
            else {
                post.addHeader("content-type", "application/x-www-form-urlencoded");
                body = setParam((String)request);
            }

            post.setEntity(new StringEntity(body,"UTF-8"));
            post.addHeader("cookie",cookie);

            response = httpClient.execute(post);

            String result = EntityUtils.toString(response.getEntity());

//            System.out.println(result);
            return responseClass != null ? new Gson().fromJson(result, responseClass) : null;

        } catch (Exception e) {
            e.printStackTrace();

            return null;
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static <T, P> P getMethod(String url, T request, Class<P> responseClass,String cookie) {
        StringBuilder apiUrl = new StringBuilder(url);
        if (request != null) {
            String param = setParam(request);
            apiUrl.append("?").append(param);
        }

        HttpGet get = new HttpGet(apiUrl.toString());
        get.addHeader("content-type", "application/json");
        get.addHeader("cookie",cookie);
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(get)) {

            String result = null;
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity);
            }

            if (result == null){
                return null;
            }

            return new Gson().fromJson(result, responseClass);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T, P> String getMethodToString(String url, T request, String cookie) {
        StringBuilder apiUrl = new StringBuilder(url);
        if (request != null) {
            String param = setParam(request);
            apiUrl.append("?").append(param);
        }

        HttpGet get = new HttpGet(apiUrl.toString());
        get.addHeader("content-type", "application/json");
        get.addHeader("cookie", cookie);
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(get)) {

            String result = null;
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity);
            }

            if (result == null) {
                return null;
            }

            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String buildBody(Object object) throws JsonProcessingException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return new Gson().toJson(object);
    }

    public static String setParam(Object object) {
        StringBuilder queryString = new StringBuilder();

        Field[] fields = object.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                if (ReflectionUtil.get(object, field) != null) {
                    queryString.append(field.getName()).append("=").append(ReflectionUtil.get(object, field)).append("&");
                }
            }

            queryString.deleteCharAt(queryString.length() - 1);

        } catch (Exception ex) {

            return queryString.toString();
        }

        return queryString.toString();
    }

    public static String setParam(String string) {
        return string;
    }
}
