package by.nces.test.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.json.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping(value = "/", produces = "text/plain;charset=UTF-8")
public class IndexController {

    private static final String UNP_GET = "http://www.portal.nalog.gov.by/grp/getData?";

    @GetMapping
    public String HomePage(HttpServletRequest request){
        return "Index";
    }
    @PostMapping
    public String HomePagePost(HttpServletRequest request,
                               //HttpServletResponse response,
                               Model model){

        String unpNumber = request.getParameter("unpNumber");
        System.out.println(unpNumber);
        try {
            String jsonString = sendGET(unpNumber);
            JSONObject obj = new JSONObject(jsonString);
            String name = obj.getJSONObject("ROW").getString("VNAIMP");
            String address = obj.getJSONObject("ROW").getString("VPADRES");
            String reg = obj.getJSONObject("ROW").getString("DREG");
            String status = obj.getJSONObject("ROW").getString("VKODS");
            model.addAttribute("name",name);
            model.addAttribute("address",address);
            model.addAttribute("reg",reg);
            model.addAttribute("status",status);
        } catch (IOException e) {
            System.out.println("ERROR");
            e.printStackTrace();
        }

        return "Index";
    }
//VNAIMP,VPADRES,DREG,VKODS
    private static String sendGET(String unpNumber) throws IOException {
        String message = null;
        URL obj = new URL(UNP_GET+"unp="+unpNumber+"&charset=UTF-8&type=json");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream(),"UTF-8"));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            message = response.toString();
        } else {
            System.out.println("GET request not worked");
        }
        return message;
    }

}
