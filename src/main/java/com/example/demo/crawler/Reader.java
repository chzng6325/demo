package com.example.demo.crawler;
import com.example.demo.crawler.model.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Date;
import org.openqa.selenium.*;

import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.openqa.selenium.chrome.ChromeOptions;

import java.net.MalformedURLException;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Reader {
    private WebDriver driver;
    private List<AstronomyMonth> astMonth;

    private AstronomyDay astDay;

    private SimpleDateFormat dayOfWeek;


    public Reader() throws IOException {

        /* 初始化 */
        astMonth = new ArrayList<>();
        astDay = new AstronomyDay();
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("-headless");
        driver = new ChromeDriver(chromeOptions); // googleChrome
    }

    public List<String> getMonth(int i){

        return astMonth.get(i-1).getTitle();
    }
    public String getContent(int i, int j){
        return astMonth.get(i-1).getContent().get(j);
    }
    public void astronomyMonth() {
        // 到每月星象頁
        List<String> stringList;
        List<String> contentList;

        driver.get("https://starwalk.space/zh-Hant/news/astronomy-calendar-2023");

        String cssSel = "";
        AstronomyMonth crawlerMonAst ;

        int contentIdx = 4;
        for (int i=3, j = 1; i<15; i++, j++){
//            獲得每個月分的大標題
            crawlerMonAst = new AstronomyMonth();
            stringList = new ArrayList<>();
            contentList = new ArrayList<>();

            cssSel = "#root > div.vfrcau0.rp1gn40 > main > article > div.x6h6yq4 > div.fkpcmm0.x6h6yq9 > ul > li:nth-child(" + i +") > a";
            WebElement elm = driver.findElement(By.cssSelector(cssSel));

            crawlerMonAst.setMonth(j);
            crawlerMonAst.setMonthTitle(elm.getText());


            //爬每個月的詳細天文資料
            cssSel = "#root > div.vfrcau0.rp1gn40 > main > article > div.x6h6yq4 > div.fkpcmm0.x6h6yq9 > ul > li:nth-child("+i+") > ul > li";
            WebElement[] elementList;
            elementList = driver.findElements(By.cssSelector(cssSel)).toArray(new WebElement[0]);

            for (WebElement elmFor: elementList){
                stringList.add(elmFor.getText());
            }
            crawlerMonAst.setTitle(stringList);


            //爬天文資料內容
            for (int k=0; k<stringList.size(); k++){
                cssSel = "//*[@id=\"root\"]/div[1]/main/article/div[5]/div[2]/p["+contentIdx+"]";
                WebElement elmContent = driver.findElement(By.xpath(cssSel));
                contentList.add(elmContent.getText());
                if (contentIdx ==8 ) contentIdx = 10;
                else contentIdx++;
            }
            crawlerMonAst.setContent(contentList);

            //爬取內容
            astMonth.add(crawlerMonAst);
        }

    }


    public void astronomyDay(String strDay) throws ParseException {
        // 到每日星象頁
        List<String> stringList = new ArrayList<>();
        dayOfWeek = new SimpleDateFormat("EEEE");

        driver.get("https://www.cwb.gov.tw/V8/C/K/astronomy_day.html");
        driver.manage().timeouts().pageLoadTimeout(1, TimeUnit.SECONDS);
        JavascriptExecutor removeAttribute = (JavascriptExecutor) driver;
        removeAttribute.executeScript("var setDate=document.getElementById(\"dateY\");setDate.removeAttribute('readonly');");

        WebElement date = driver.findElement(By.id("dateY"));
        WebElement enter = driver.findElement(By.id("enter"));
        date.clear();
        date.sendKeys(strDay);
        enter.click();

        String str = date.getAttribute("value");
        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(str);
        astDay.setSolarDay(date1);
        astDay.setWhichDay(dayOfWeek.format(date1));


        WebElement[] elementList;
        elementList = driver.findElements(By.cssSelector("#day-1 > div.row.margin-bottom-20 > div:nth-child(2) > ul > li > p")).toArray(new WebElement[0]);

        for (WebElement elm : elementList) {
            stringList.add(elm.getText());
        }
        parseDay(stringList);

        WebElement img = driver.findElement(By.cssSelector("#day-1 > div.row.margin-bottom-20 > div.col-md-3.col-sm-6.margin-bottom-20 > img"));

        String urlStr = img.getAttribute("src");
        System.out.println(urlStr);
        try {
            downloadImageFromUrl(urlStr, "C:\\Users\\yang\\Desktop\\demo\\src\\main\\resources\\com\\example\\demo", "moon");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseDay(List<String> stringList) {
        int j;
        for (int i = 0; i < stringList.size(); i++) {
            j = i % 3;
            if (j == 0) astDay.setLunarDay(stringList.get(i));
            else if (j == 1) astDay.setSolarTerm(stringList.get(i));
            else if (j == 2) astDay.setPhenomenon(stringList.get(i));
        }
    }




    public AstronomyDay getAstDay() {
        return astDay;
    }

    public AstronomyMonth getAstMon(int i){
        return astMonth.get(i-1);
    }

    public static String downloadImageFromUrl(String url, String fileDirectoryPath, String fileNameWithoutFormat) {
        String filePath = null;

        BufferedInputStream in = null;
        ByteArrayOutputStream out = null;
        HttpURLConnection httpUrlConnection = null;
        FileOutputStream file = null;

        try {

            if (url.startsWith("https://")) {
                //HTTPS時
                httpUrlConnection = getHttpURLConnectionFromHttps(url);
            }
            //如果不是HTTPS或是沒成功得到httpUrlConnection，用HTTP的方法
            if (httpUrlConnection == null) {
                httpUrlConnection = (HttpURLConnection) (new URL(url)).openConnection();
            }

            // 設置User-Agent，偽裝成一般瀏覽器，不然有些伺服器會擋掉機器程式請求
            httpUrlConnection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Linux; Android 4.2.1; Nexus 7 Build/JOP40D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166  Safari/535.19");
            httpUrlConnection.connect();

            String imageType;
            if (httpUrlConnection.getResponseCode() == 200) {
                //成功取得response，
                //取得contentType
                String contentType = httpUrlConnection.getHeaderField("Content-Type");
                // 只處理image的回應
                if ("image".equals(contentType.substring(0, contentType.indexOf("/")))) {
                    //得到對方Server提供的圖片副檔名，如jpg, png等
//                    imageType = contentType.substring(contentType.indexOf("/") + 1);
                    imageType = "jpg";

                    if (imageType != null && !"".equals(imageType)) {
                        //由HttpUrlConnection取得輸入串流
                        in = new BufferedInputStream(httpUrlConnection.getInputStream());
                        out = new ByteArrayOutputStream();

                        //建立串流Buffer
                        byte[] buffer = new byte[1024];

                        file = new FileOutputStream(new File(fileDirectoryPath + File.separator + fileNameWithoutFormat + "." + imageType));

                        int readByte;
                        while ((readByte = in.read(buffer)) != -1) {
                            //輸出檔案
                            out.write(buffer, 0, readByte);
                        }

                        byte[] response = out.toByteArray();
                        file.write(response);

                        //下載成功後，返回檔案路徑
                        filePath = fileDirectoryPath + File.separator + fileNameWithoutFormat + "." + imageType;
                    }
                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //關閉各種串流
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                if (httpUrlConnection != null) {
                    httpUrlConnection.disconnect();
                }
                if (file != null) {
                    file.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return filePath;
    }

    public static HttpURLConnection getHttpURLConnectionFromHttps(String url) {
        HttpURLConnection httpUrlConnection = null;
        //建立一個信認所有憑證的X509TrustManager，放到TrustManager裡面
        TrustManager[] trustAllCerts;
        try {
            // Activate the new trust manager
            trustAllCerts = new TrustManager[]{new X509TrustManager() {

                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {     // TODO Auto-generated method stub
                    //不作任何事
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {     // TODO Auto-generated method stub
                    //不作任何事
                }

                public X509Certificate[] getAcceptedIssuers() {
                    //不作任何事
                    return null;
                }

            }};

            //設置SSL設定
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            //跟HTTP一樣，用Url建立連線
            httpUrlConnection = (HttpURLConnection) (new URL(url)).openConnection();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return httpUrlConnection;
    }

}
