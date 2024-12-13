package vttp.batch5.ssf.noticeboard.controllers;

import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.validation.Valid;
import vttp.batch5.ssf.noticeboard.models.Notice;
import vttp.batch5.ssf.noticeboard.services.NoticeService;

import org.springframework.web.bind.annotation.PostMapping;



// Use this class to write your request handlers

@Controller
@RequestMapping("")
public class NoticeController {

    @Autowired
    NoticeService noticeService;

    @GetMapping("/")
    public String landingPage(Model model) {
        Notice notice = new Notice();
        model.addAttribute("notice", notice);

        return "notice";
    }

    @PostMapping("/notice")
    public String postNotice(@Valid @ModelAttribute("notice") Notice notice, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "notice";
        }

        try {
            ResponseEntity<String> resp = noticeService.postToNoticeServer(notice);
            Object payload = resp.getBody();
            System.out.println(payload);

            String data = resp.getBody();
            JsonReader jReader = Json.createReader(new StringReader(data));
            JsonObject jObject = jReader.readObject();

            String id = jObject.getString("id");
            System.out.println(id);
            System.out.println(data);

            noticeService.insertNotice(id, data);

            model.addAttribute("id", id);

            return "view2";

        } catch (Exception e) {
            System.out.println(e);

            model.addAttribute("error", e);
            return "view3";
        }
        
    }

    @GetMapping("/status")
    public ResponseEntity<String> getHealth() {

        try {
            String id = noticeService.checkHealth();
            System.out.println(id);

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("{}");
        } catch (Exception e) {
            return ResponseEntity.status(503).contentType(MediaType.APPLICATION_JSON).body("{}");
        }
    }
    
}
