package platform;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;

@Controller
public class CodeController {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    private final CodeRepository codeRepository;

    public CodeController(CodeRepository codeRepository) {
        this.codeRepository = codeRepository;
    }

    @GetMapping("/code/{uuid}")
    public String getCodeByUuidHtml(@PathVariable String uuid, Model model) {

        Code code = getCode(uuid);

        model.addAttribute("date", code.getDate());
        model.addAttribute("code", code.getCode());

        model.addAttribute("time", getRemainingTime(code));
        model.addAttribute("views", code.getViews());

        model.addAttribute("timeRestricted", code.getTime() > 0);
        model.addAttribute("viewsRestricted", code.isViewsRestricted());

        return "code";
    }

    @PostMapping("/api/code/new")
    @ResponseBody
    public Map<String, String> addCode(@RequestBody Map<String, Object> request) {

        String code = (String) request.get("code");
        long time = ((Number) request.get("time")).longValue();
        int views = ((Number) request.get("views")).intValue();

        Code newCode = new Code(
                code,
                LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).format(DATE_FORMATTER),
                time,
                views
        );

        Code savedCode = codeRepository.save(newCode);

        return Map.of("id", savedCode.getUuid());
    }

    @GetMapping("/api/code/{uuid}")
    @ResponseBody
    public CodeResponse getCodeByUuid(@PathVariable String uuid) {

        Code code = getCode(uuid);

        return new CodeResponse(
                code.getCode(),
                code.getDate(),
                getRemainingTime(code),
                code.getViews()
        );
    }

    @GetMapping("/api/code/latest")
    @ResponseBody
    public List<CodeResponse> getLatestCode() {

        return codeRepository
                .findTop10ByTimeLessThanEqualAndViewsRestrictedFalseOrderByIdDesc(0)
                .stream()
                .map(code -> new CodeResponse(
                        code.getCode(),
                        code.getDate(),
                        0,
                        0
                ))
                .toList();
    }

    @GetMapping("/code/latest")
    public String getLatestCodeHtml(Model model) {

        List<Code> latest = codeRepository
                .findTop10ByTimeLessThanEqualAndViewsRestrictedFalseOrderByIdDesc(0);

        model.addAttribute("snippets", latest);

        return "latest";
    }

    @GetMapping("/code/new")
    public String getNewCodePage() {
        return "new";
    }

    private Code getCode(String uuid) {

        Code code = codeRepository.findByUuid(uuid)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Time limit check
        if (code.getTime() > 0) {
            LocalDateTime expiresAt =
                    code.getCreatedAt().plusSeconds(code.getTime());

            if (!LocalDateTime.now().isBefore(expiresAt)) {
                codeRepository.delete(code);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }

        // View restriction check
        if (code.isViewsRestricted()) {

            if (code.getViews() <= 0) {
                codeRepository.delete(code);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            code.setViews(code.getViews() - 1);
            codeRepository.save(code);
        }

        return code;
    }

    private long getRemainingTime(Code code) {

        if (code.getTime() <= 0) {
            return 0;
        }

        LocalDateTime expiresAt =
                code.getCreatedAt().plusSeconds(code.getTime());

        return Math.max(
                0,
                Duration.between(LocalDateTime.now(), expiresAt).getSeconds()
        );
    }

}