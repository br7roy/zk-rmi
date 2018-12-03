package com.rust.submit;

import junit.framework.TestCase;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.rust.submit.Constants.ScheduleJobTimeEnum;
import com.rust.submit.util.DateUtil;

/**
 * Unit test for simple App.
 */
public class AppTest 
extends TestCase {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void testDuration() throws InterruptedException {
        Calendar c = Calendar.getInstance();
        c.set(2018, Calendar.NOVEMBER, 6, 18, 30);
        Date from = Date.from(c.toInstant());
        // Date from = new Date();

        // TimeUnit.SECONDS.sleep(1);
      long minus=  TimeUnit.HOURS.convert(System.currentTimeMillis() - from.getTime(), TimeUnit.MILLISECONDS);
        System.out.println(minus);

    }
    @Test
    public void testTime() throws InterruptedException {
        long s = 1541671902217L;
        long e = 1541585567943L;
        long duration = e - s;
        long nextTime = TimeUnit.HOURS.convert(0-duration, TimeUnit.MILLISECONDS);
        System.out.println(nextTime);
        System.out.println(ScheduleJobTimeEnum.SUBMIT_WORK);
    }

    public void testDayOfWeek() {
        LocalDateTime ldt = LocalDateTime.now();

        boolean supported =
                ldt.getDayOfWeek().isSupported(ChronoField.DAY_OF_WEEK);
        // System.out.println(supported);

        int i = ldt.getDayOfWeek().get(ChronoField.DAY_OF_WEEK);
        // ChronoField.DAY_OF_WEEK.adjustInto()
        // DayOfWeek.SUNDAY.adjustInto()
        // ldt.adjustInto(LocalDate.)

        // ChronoField.DAY_OF_WEEK.getBaseUnit().isDateBased()
        System.out.println(DateUtil.checkDateIfNotNeedSchedule());
    }

    public void testUrlEnDe() throws UnsupportedEncodingException {
        String url = "utf8=%E2%9C%93&f%5B%5D=spent_on&op%5Bspent_on%5D=t&f%5B%5D=user_id&op%5Buser_id%5D=%3D&v%5Buser_id%5D%5B%5D=me&f%5B%5D=&c%5B%5D=project&c%5B%5D=spent_on&c%5B%5D=user&c%5B%5D=activity&c%5B%5D=issue&c%5B%5D=comments&c%5B%5D=hours";
        String decode = URLDecoder.decode(url,
                StandardCharsets.UTF_8.displayName());
        System.out.println(decode);

        String durl = "utf8=✓&f[]=spent_on&op[spent_on]=t&f[]=user_id&op" +
                "[user_id]==&v[user_id][]=me&f[]=&c[]=project&c[]=spent_on&c" +
                "[]=user&c[]=activity&c[]=issue&c[]=comments&c[]=hours";


        String dateUrl = "utf8=%E2%9C%93&f%5B%5D=spent_on&op%5Bspent_on%5D=%3D&v%5Bspent_on%5D%5B%5D=2018-11-26&f%5B%5D=user_id&op%5Buser_id%5D=%3D&v%5Buser_id%5D%5B%5D=me&f%5B%5D=&c%5B%5D=project&c%5B%5D=spent_on&c%5B%5D=user&c%5B%5D=activity&c%5B%5D=issue&c%5B%5D=comments&c%5B%5D=hours";
        String decode1 = URLDecoder.decode(dateUrl, "utf-8");
        System.out.println( decode1);


    }
    public void testConditionQuery() throws IOException {
        HttpGet request = new HttpGet("http://badao.pinganfu.net/time_entries?utf8=%E2%9C%93&f%5B%5D=spent_on&op%5Bspent_on%5D=%3D&v%5Bspent_on%5D%5B%5D=2018-11-26&f%5B%5D=user_id&op%5Buser_id%5D=%3D&v%5Buser_id%5D%5B%5D=me&f%5B%5D=&c%5B%5D=project&c%5B%5D=spent_on&c%5B%5D=user&c%5B%5D=activity&c%5B%5D=issue&c%5B%5D=comments&c%5B%5D=hours");
        URIBuilder newBuilder = new URIBuilder(request.getURI());
        newBuilder.setParameter("v[spent_on][]", "2018-11-22");
        //获取键值对列表
        List<NameValuePair> params = newBuilder.getQueryParams();
        //转换为键值对字符串
        String str = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
        System.out.println(str);
/*        List<NameValuePair> spent_on =
                params.stream().filter(new Predicate<NameValuePair>() {
            @Override
            public boolean test(NameValuePair nameValuePair) {
                if (nameValuePair.getName().equals("v[spent_on][]")) {
                    // nameValuePair.getValue().
                            return true;
                } return false;
            }
        }).collect(Collectors.toList());*/







        // System.out.println(spent_on);
/*        params.forEach(new Consumer<NameValuePair>() {
            @Override
            public void accept(NameValuePair nameValuePair) {
                System.out.println("name:" + nameValuePair.getName() + "," +
                        "value:" + nameValuePair.getValue());
            }
        });*/






/*     List<BasicNameValuePair>   list = Lists.newArrayList();

        BasicNameValuePair bnv = new BasicNameValuePair("utf8", "");
        bnv= new BasicNameValuePair("time_entry[spent_on]", "2018-11-26");
        list.add(bnv);
        System.out.println(str);*/

    }

    public void testReal()throws Exception{
        String time = "2018-11-26";
        String param = "utf8=%E2%9C%93&f%5B%5D=spent_on&op%5Bspent_on%5D=t&f%5B%5D=user_id&op%5Buser_id%5D=%3D&v" +
                "%5Buser_id%5D%5B%5D=me&f%5B%5D=&c%5B%5D=project&c%5B%5D=spent_on&c%5B%5D=user&c%5B%5D=activity&c%5B" +
                "%5D=issue&c%5B%5D=comments&c%5B%5D=hours";
        HttpGet oriGet = new HttpGet("http://badao.pinganfu.net/time_entries" +
                "?" + param);

        URIBuilder newBuilder = new URIBuilder(oriGet.getURI());
        newBuilder.setParameter("v[spent_on][]", time);
        List<NameValuePair> params = newBuilder.getQueryParams();
        String str = EntityUtils.toString(new UrlEncodedFormEntity(params,
                Consts.UTF_8));
        System.out.println(str);
        HttpGet request = new HttpGet("http://badao.pinganfu.net/time_entries" + "?" + str);
        request.addHeader("Cookie", "autologin=09d7c2e22e061cc160ff4c6b3e257611c27459f5; sidebar_hide=hide; " +
                "_redmine_session" +
                "=UVFDNkZhTnBEZGZUWlFjeEZPVHlrbDJPc0dlQ25vTjA1WVhhWHpXZEcwY3EwbEJ2TGVubjVVbUlUd2xYYzA3TEwySUlsN3pQKzRDMVZ3Q1pzeVFnSmxDZ3ZkYnNhaFFKb3ZoNDFSamFaMHd2WW5nNUJCSHBBZS9JOGVYdTQ0VGowYWk3b0ZzbUNMN2dQNXJHSHJhQ2JGVHRlTE5EdDNHSXJHZ2ZEVHVIbmdieFZkYm05dkdHRmJWMEpGQU1UQUd6YjFONnMvYmdKaTN0bDJ6ZUxiUGJKcWQ5dGxLanNJVTlaWnVmc0xWTFltUGpWVXJVdVRJYVJYT2gwam1IeDBwNjZiRjJ0VXdWemhmdEt3aWIwTGZhYm0weWdEMU5VMUNZSDVkdEdNWjMvUzZVL01CaExYK0xVRERORG9hQzVNQkQ3RXFhOXQ4SG1GNVNLc1Y1K2JKa3Z4cnNZTTZESzZOQ2JvOG9WRVNva1JnVjBWRmVjUW4wcmhpb3pQeVUrTFVwTVdkM0Q4YjlnNThLaG83WFF3djQ5cG94Zm0rZVBlakpiUWJHa09oV2hZZmZMYUV3UGtEdDZPcHlmYW1JZndSNTJtaENYWkR0d0NsUkt4cEtQVkRrNlpvMVI0Si9IeUVQTmhWaStadXMybTdwcWFKcktrVlhLWnlNMzNFSjdxR0dYdy9pazBGSWxlcE9XVzJwbS9DUlp3c3oyUlc3VWI0eXdTR3Jwd0ZBaWlzUVEwanNYZnJZZ2xCS216NXZqWXVyOUxDZjZrcFZWdTlFQzF6RjJkWS9SQT09LS1zbTQ1QjlqMnhUMXNuOUxZenVCdm9RPT0%3D--d5f6e2c06f5b5a11c6c1f594e671e5609555facd");
        request.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        URIBuilder bb = new URIBuilder(request.getURI());
        //获取键值对列表
        List<NameValuePair> asd = bb.getQueryParams();
        //转换为键值对字符串
        String sdsd = EntityUtils.toString(new UrlEncodedFormEntity(asd,
                Consts.UTF_8));

        HttpGet realRequest = new HttpGet("http://badao.pinganfu" +
                ".net/time_entries" + "?" + sdsd);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(realRequest);
        StatusLine statusLine = response.getStatusLine();
        System.out.println(statusLine);



    }
}
