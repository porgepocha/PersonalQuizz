package com.jorge.constanca.server.web;

import com.jorge.constanca.model.QuizSubmissionRequest;
import com.jorge.constanca.model.QuizSubmissionResponse;
import com.jorge.constanca.model.StoredSubmissionView;
import com.jorge.constanca.model.TextAnswer;
import com.jorge.constanca.server.service.SubmissionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/submissions")
public class QuizSubmissionController {

    private final SubmissionService submissionService;

    public QuizSubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PostMapping
    public QuizSubmissionResponse submit(@Valid @RequestBody SubmissionPayload payload) {
        QuizSubmissionRequest request = new QuizSubmissionRequest(
                payload.participantName(),
                payload.selectedOptionIds(),
                payload.textAnswers(),
                payload.clientVersion()
        );
        return submissionService.saveSubmission(request);
    }

    @GetMapping
    public List<StoredSubmissionView> listSubmissions() {
        return submissionService.listSubmissions();
    }

    public record SubmissionPayload(
            String participantName,
            @NotEmpty List<String> selectedOptionIds,
            List<TextAnswer> textAnswers,
            String clientVersion
    ) {
    }
}
