package com.example.demo;

import com.example.demo.crawler.model.AstronomyDay;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import com.example.demo.crawler.Reader;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.net.URL;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;


public class HelloController implements Initializable {
    private Reader reader;

    private AstronomyDay astDayClass;

    private SimpleDateFormat ymd;
    private Calendar calendar;

    private SimpleDateFormat dayOfYear;
    private SimpleDateFormat dayOfMon;
    private SimpleDateFormat dayOfDay;
    private SimpleDateFormat dayMon;

    @FXML
    private Label chooseDayLabel;

    @FXML
    private Label chooseMonLabel;
    @FXML
    private Label chooseYearLabel;
    @FXML
    private ListView<String> monthList;
    private List<String> monthInfo;
    @FXML
    private Label lunarDay;
    @FXML
    private Label solarTerms;
    @FXML
    private Label phenomenon;

    @FXML
    private DatePicker pickDay;

    @FXML
    private ImageView moonImg;

    private Image img;
    @FXML
    private TitledPane monTitlePane;



    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        ymd = new SimpleDateFormat("yyyy-MM-dd");
        dayOfYear = new SimpleDateFormat("yyyy");
        dayOfMon = new SimpleDateFormat("MMM");
        dayMon = new SimpleDateFormat("MM");

        dayOfDay = new SimpleDateFormat("dd");

        calendar = Calendar.getInstance();

        Date dateObj = calendar.getTime();
        String formattedDate = ymd.format(dateObj);
        String monStr = dayMon.format(dateObj);
        int monInt = Integer.parseInt(monStr);
        String str = dayOfMon.format(dateObj);

        try {
            reader = new Reader();
            reader.astronomyMonth();
            monthInfo = reader.getMonth(monInt);
            monthList.getItems().addAll(monthInfo);
            monTitlePane.setText(str + "天文現象");

            pickDay.setValue(LocalDate.now());
            setDayInfo(formattedDate);


//            listview
            monthList.setOnMouseClicked (new EventHandler<MouseEvent> () {
                @Override
                public void handle(MouseEvent event) {
                    final Alert alert = new Alert(AlertType.INFORMATION); // 實體化Alert對話框物件，並直接在建構子設定對話框的訊息類型
                    alert.setTitle(" 天文現象"); //設定對話框視窗的標題列文字
                    alert.setHeaderText(monthList.getSelectionModel().getSelectedItem()); //設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
                    alert.setContentText(reader.getContent(pickDay.getValue().getMonthValue() ,monthList.getSelectionModel().getSelectedIndex())); //設定對話框的訊息文字
                    alert.showAndWait(); //顯示對話框，並等待對話框被關閉時才繼續執行之後的程式
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    void crawlerDay(ActionEvent event){
        setDayInfo(pickDay.getValue().toString());
        System.out.println(pickDay.getValue().toString());

        monthInfo = reader.getMonth(pickDay.getValue().getMonthValue());

        monthList.getItems().clear();
        monthList.getItems().addAll(monthInfo);
        monTitlePane.setText( reader.getAstMon(pickDay.getValue().getMonthValue()).getMonth() + "月 天文現象");


    }

    private void setDayInfo(String strDay){
        try {
            reader.astronomyDay(strDay);
            astDayClass = reader.getAstDay();
            chooseDayLabel.setText(dayOfDay.format(astDayClass.getSolarDay()));
            chooseMonLabel.setText(dayOfMon.format(astDayClass.getSolarDay()));
            chooseYearLabel.setText(dayOfYear.format(astDayClass.getSolarDay()));


            lunarDay.setText(astDayClass.getLunarDay());
            solarTerms.setText(astDayClass.getSolarTerm());
            phenomenon.setText(astDayClass.getPhenomenon());


            File file = new File("src/main/resources/com/example/demo/moon.jpg");
            String str = file.toURI().toString();
            img = new Image(str);
            moonImg.setImage(img);

        } catch ( ParseException e){
            e.printStackTrace();
        }



    }
}