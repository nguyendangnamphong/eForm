package com.vnu.uet.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vnu.uet.EformApp;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * Integration tests for API Logic and detailed reporting.
 * This class generates a rich Markdown report in target/api-test-results.md
 */
@SpringBootTest(classes = EformApp.class, properties = {
    "spring.liquibase.enabled=false",
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MySQL",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "application.uaa-name=http://mock-uaa",
    "application.eflow-url=http://mock-eflow"
})
@AutoConfigureMockMvc
@WithMockUser(username = "admin", roles = {"ADMIN"})
class APICustomLogicIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final List<TestDetail> testDetails = new ArrayList<>();

    private static class TestDetail {
        String endpoint;
        String scenario;
        String statusIcon;
        String resultType;
        String expected;
        String actual;
        String curl;
        String response;
        int httpStatus;

        public TestDetail(String endpoint, String scenario, String statusIcon, String resultType, String expected, String actual, String curl, String response, int httpStatus) {
            this.endpoint = endpoint;
            this.scenario = scenario;
            this.statusIcon = statusIcon;
            this.resultType = resultType;
            this.expected = expected;
            this.actual = actual;
            this.curl = curl;
            this.response = response;
            this.httpStatus = httpStatus;
        }
    }

    private String toCurl(String method, String url, String body) {
        StringBuilder curl = new StringBuilder("curl -X ").append(method).append(" '").append(url).append("'");
        curl.append(" -H 'Content-Type: application/json'");
        if (body != null && !body.isEmpty()) {
            // Escape single quotes for bash
            String escapedBody = body.replace("'", "'\\''");
            curl.append(" -d '").append(escapedBody).append("'");
        }
        return curl.toString();
    }

    private MvcResult performAndLog(String method, String url, String body, String scenario, String expected) throws Exception {
        String curl = toCurl(method, url, body);
        MvcResult result;
        if ("POST".equalsIgnoreCase(method)) {
            result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body != null ? body : ""))
                .andReturn();
        } else {
            result = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body != null ? body : ""))
                .andReturn();
        }

        int status = result.getResponse().getStatus();
        String responseRaw = result.getResponse().getContentAsString();
        String responsePretty = responseRaw;

        try {
            if (!responseRaw.isEmpty()) {
                Object json = objectMapper.readValue(responseRaw, Object.class);
                responsePretty = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
            }
        } catch (Exception e) {
            // Fallback to raw if not JSON
        }

        boolean isSuccess = status >= 200 && status < 300;
        String statusIcon = isSuccess ? "🟢" : "🔴";
        String resultType = isSuccess ? "Thành Công" : "Lỗi Hệ Thống";
        String actual = isSuccess ? "HTTP " + status : "Lỗi HTTP " + status;

        testDetails.add(new TestDetail(
            method + " " + url, 
            scenario, 
            statusIcon, 
            resultType, 
            expected, 
            actual, 
            curl, 
            responsePretty.isEmpty() ? "(Trống)" : responsePretty, 
            status
        ));
        return result;
    }

    @Test
    void testGetAccount_BasicLogic() throws Exception {
        performAndLog("GET", "/api/account", null, 
            "Kiểm tra thông tin tài khoản hiện tại", 
            "Trả về thông tin user admin với HTTP 200");
    }

    @Test
    void testGetCommonInfo_InvalidId() throws Exception {
        performAndLog("GET", "/api/owner/form?formId=invalid-id", null, 
            "Truy xuất thông tin form không tồn tại", 
            "Trả về thông báo 'FormId not exist'");
    }

    @AfterAll
    static void generateReport() {
        try {
            Path path = Paths.get("target/api-test-results.md");
            Files.createDirectories(path.getParent());
            
            long total = testDetails.size();
            long success = testDetails.stream().filter(d -> d.statusIcon.equals("🟢")).count();
            long failed = total - success;

            StringBuilder md = new StringBuilder();
            md.append("# 🚀 CHI TIẾT KẾT QUẢ KIỂM THỬ API LOGIC - EFORM\n\n");
            md.append("## 📊 TỔNG QUAN\n");
            md.append("> [!IMPORTANT]\n");
            md.append(String.format("> - **Tổng số kịch bản:** `%d` \n", total));
            md.append(String.format("> - **Thành công:** 🟢 `%d` \n", success));
            md.append(String.format("> - **Thất bại/Cảnh báo:** 🔴 `%d` \n\n", failed));
            md.append("--- \n\n");

            for (int i = 0; i < testDetails.size(); i++) {
                TestDetail d = testDetails.get(i);
                md.append(String.format("### %d. %s %s\n", i + 1, d.statusIcon, d.scenario));
                md.append(String.format("- **Endpoint:** `%s`\n", d.endpoint));
                md.append(String.format("- **Kết quả:** `%s`\n", d.resultType));
                md.append(String.format("- **Dự đoán:** %s\n", d.expected));
                md.append(String.format("- **Thực tế:** %s\n\n", d.actual));
                
                md.append("<details><summary><b>🛠 DIAGNOSTIC (cURL & Response)</b></summary>\n\n");
                md.append("#### 📥 Input (cUrl)\n```bash\n").append(d.curl).append("\n```\n\n");
                md.append(String.format("#### 📤 Output (Response) - HTTP `%d`\n```json\n%s\n```\n\n", d.httpStatus, d.response));
                md.append("</details>\n\n---\n\n");
            }

            Files.writeString(path, md.toString(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
