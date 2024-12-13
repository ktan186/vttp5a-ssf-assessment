package vttp.batch5.ssf.noticeboard.controllers;

import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.validation.Valid;
import vttp.batch5.ssf.noticeboard.models.Notice;
import vttp.batch5.ssf.noticeboard.repositories.NoticeRepository;
import vttp.batch5.ssf.noticeboard.services.NoticeService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



// Use this class to write your request handlers

@Controller
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    NoticeService noticeService;

    @Autowired
    NoticeRepository noticeRepository;

    @GetMapping("")
    public String landingPage(Model model) {
        Notice n = new Notice();
        model.addAttribute("notice", n);

        return "notice";
    }

    @PostMapping("")
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

            noticeRepository.insertNotices("notice", id, payload);

            model.addAttribute("id", id);

            return "view2";
        } catch (Exception e) {
            System.out.println(e);

            return "view3";
        }
    }
    
    // @GetMapping("/status")
    // public ModelAndView getHealth() {
    //     ModelAndView mav = new ModelAndView();

    //     try {
    //         Object data = noticeService.checkHealth();
    //         System.out.println(data);

    //         mav.setViewName("healthy");
    //         mav.setStatus(HttpStatusCode.valueOf(200));
    //     } catch (Exception e) {
    //         mav.setViewName("unhealthy");
    //         mav.setStatus(HttpStatusCode.valueOf(503));
    //     }
    //     return mav;
    // }

    @GetMapping("/status")
    public ResponseEntity<String> getHealth() {

        try {
            Object data = noticeService.checkHealth();
            System.out.println(data);

            return ResponseEntity.ok("{}");
        } catch (Exception e) {
            return ResponseEntity.status(503).body("{}");
        }
    }
    
}
