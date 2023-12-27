package henrik.development.splitvajs.controller;

import henrik.development.splitvajs.model.PersonRequestModel;
import henrik.development.splitvajs.model.RequestModel;
import henrik.development.splitvajs.service.v2.Expense;
import henrik.development.splitvajs.service.v2.Person;
import henrik.development.splitvajs.service.v2.SplitvajsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
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
            return ResponseEntity.ok(service.getPeople());
        } catch (Exception e) {
            return getExceptionResponse(e);
        }
    }

    @PostMapping("/expense")
    public ResponseEntity addExpense(@RequestBody RequestModel request) {
        try {
            Expense added = service.addExpense(request);
            return ResponseEntity.ok(added);
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

    @PostMapping("/payer")
    public ResponseEntity addPerson(@RequestBody PersonRequestModel request) {
        try {
            Person added = service.addPerson(request.getName());
            return ResponseEntity.ok(added);
        } catch (Exception e) {
            return getExceptionResponse(e);
        }
    }

    private ResponseEntity<String> getExceptionResponse(Exception e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }
}
