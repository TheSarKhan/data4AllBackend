package org.example.dataprotal.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.dataprotal.model.chatbot.Category;
import org.example.dataprotal.model.chatbot.Question;
import org.example.dataprotal.repository.chatbot.CategoryRepository;
import org.example.dataprotal.repository.chatbot.QuestionRepository;
import org.example.dataprotal.service.BotService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/chatbot")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') ")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Chatbot Controller",description = "Provides chatbot-related information for admin dashboard")
public class ChatbotController {
    private final BotService botService;
    private final CategoryRepository categoryRepository;
    private final QuestionRepository questionRepository;

    @PostMapping("/chat")
    public ResponseEntity<?> chat(@RequestBody Map<String, String> body) {
        String userMessage = body.get("message");
        String botResponse = botService.askBot(userMessage.toLowerCase());
        return ResponseEntity.ok(Collections.singletonMap("response", botResponse));
    }

    @GetMapping("/getAllCategories")
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/getAllQuestions")
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @PostMapping("/category/add")
    public ResponseEntity<Category> addCategory(@RequestParam String categoryName) {
        Category category=Category.builder().categoryName(categoryName).build();
        Category savedCategory = categoryRepository.save(category);
        return ResponseEntity.ok(savedCategory);
    }

    @PostMapping("/question/add")
    public ResponseEntity<?> addQuestion(@RequestBody QuestionRequest request) {
        Optional<Category> categoryOpt = categoryRepository.findById(request.getCategoryId());

        if (categoryOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Category bulunamadı.");
        }

        Question question = new Question();
        question.setCategory(categoryOpt.get());
        question.setQuestion(request.getQuestion());
        question.setAnswer(request.getAnswer());

        Question savedQuestion = questionRepository.save(question);
        return ResponseEntity.ok(savedQuestion);
    }

    @PutMapping("/question/{id}")
    public ResponseEntity<?> updateQuestion(@PathVariable Long id, @RequestBody QuestionRequest request) {
        Optional<Question> questionOpt = questionRepository.findById(id);
        if (questionOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Question question = questionOpt.get();
        question.setQuestion(request.getQuestion());
        question.setAnswer(request.getAnswer());

        if (request.getCategoryId() != null) {
            Optional<Category> categoryOpt = categoryRepository.findById(request.getCategoryId());
            categoryOpt.ifPresent(question::setCategory);
        }

        return ResponseEntity.ok(questionRepository.save(question));
    }

    @DeleteMapping("/question/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        if (!questionRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        questionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        if (!categoryRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        categoryRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/export")
    public ResponseEntity<?> exportQuestions() {
        List<Category> categories = categoryRepository.findAll();
        List<Map<String, Object>> response = categories.stream().map(cat -> Map.of(
                "category", cat.getCategoryName(),
                "questions", cat.getQuestions().stream().map(q -> Map.of(
                        "question", q.getQuestion(),
                        "answer", q.getAnswer()
                )).toList()
        )).toList();

        return ResponseEntity.ok(response);
    }


    @Data
    public static class QuestionRequest {
        private Long categoryId;
        private String question;
        private String answer;
    }
}
