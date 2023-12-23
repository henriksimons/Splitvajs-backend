package henrik.development.splitvajs.controller;

import henrik.development.splitvajs.model.RequestModel;
import henrik.development.splitvajs.service.v2.Expense;
import henrik.development.splitvajs.service.v2.SplitvajsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RequestMapping("/v2")
public class SplitvajsControllerV2 {
    private final SplitvajsServiceImpl service;


    @Autowired
    public SplitvajsControllerV2(SplitvajsServiceImpl service) {
        this.service = Objects.requireNonNull(service, "SplitvajsService can not be null.");
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }

    @GetMapping("/expenses")
    public ResponseEntity getExpenses() {
        try {
            return ResponseEntity.ok(service.getExpenses());
        } catch (Exception e) {
            return getExceptionResponse(e);
        }
    }

    @GetMapping("/result")
    public ResponseEntity getResult() {
        try {
            return ResponseEntity.ok(service.getResult());
        } catch (Exception e) {
            return getExceptionResponse(e);
        }
    }

    @GetMapping("/payers")
    public ResponseEntity getPayers() {
        try {
            return ResponseEntity.ok(service.getPayers());
        } catch (Exception e) {
            return getExceptionResponse(e);
        }
    }

    @PostMapping("/expense")
    public ResponseEntity addExpense(@RequestBody RequestModel request) {
        try {
            Expense added = service.addExpense(request);
            return ResponseEntity.ok(String.format("Expense item added: %s", added));
        } catch (Exception e) {
            return getExceptionResponse(e);
        }
    }


    @DeleteMapping("/expenses")
    public ResponseEntity clearAll() {
        try {
            service.clear();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return getExceptionResponse(e);
        }
    }

    private ResponseEntity<String> getExceptionResponse(Exception e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }
}
