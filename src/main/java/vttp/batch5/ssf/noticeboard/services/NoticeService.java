package vttp.batch5.ssf.noticeboard.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import vttp.batch5.ssf.noticeboard.models.Notice;
import vttp.batch5.ssf.noticeboard.repositories.NoticeRepository;

@Service
public class NoticeService {

	@Value("${api.endpoint}")
	private String apiEndpoint;

	@Autowired
	NoticeRepository noticeRepository;

	RestTemplate restTemplate = new RestTemplate();

	// TODO: Task 3
	// You can change the signature of this method by adding any number of parameters
	// and return any type
	public ResponseEntity<String> postToNoticeServer(Notice notice) {

		List<String> cat = new ArrayList<>();
		cat = notice.getCategories();

		JsonArrayBuilder jArrayBuilder = Json.createArrayBuilder();

		for (int i = 0; i < cat.size(); i++) {
			jArrayBuilder.add(cat.get(i));
		}
		JsonArray jArray = jArrayBuilder.build();

		BigDecimal pd = BigDecimal.valueOf(notice.getPostDate().getTime());

		JsonObject jObject = Json.createObjectBuilder()
			.add("title", notice.getTitle())
			.add("poster", notice.getPoster())
			.add("postDate", pd)
			.add("categories", jArray)
			.add("text", notice.getText())
			.build();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		
		String payload = jObject.toString();
		System.out.println(jObject);

		HttpEntity<String> request = new HttpEntity<>(payload, headers);
		
		try {
			ResponseEntity<String> response = restTemplate.postForEntity(apiEndpoint, request, String.class);
			// System.out.println(response.getBody());
			return response;
		} catch (Exception e) {
			System.out.println(e);
			throw e;
		}
	}

	public void insertNotice(String id, Object payload) {
		noticeRepository.insertNotices(id, payload);
	}

	public String checkHealth() throws Exception {
		String key = noticeRepository.getRandomValue();
		System.out.println(key);

		return key;
	}
}
