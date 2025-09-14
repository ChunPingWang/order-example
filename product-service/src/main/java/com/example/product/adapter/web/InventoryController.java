package com.example.product.adapter.web;

import com.example.product.application.port.in.InventoryUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory API", description = "庫存管理相關 API")
public class InventoryController {
    
    private final InventoryUseCase inventoryUseCase;
    
    @PostMapping("/{productId}/increase")
    @Operation(summary = "增加庫存")
    public ResponseEntity<Void> increaseStock(
            @PathVariable String productId,
            @RequestBody IncreaseStockRequest request
    ) {
        inventoryUseCase.increaseStock(new InventoryUseCase.IncreaseStockCommand(
                productId,
                request.amount(),
                request.reason(),
                request.operatedBy()
        ));
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{productId}/decrease")
    @Operation(summary = "減少庫存")
    public ResponseEntity<Void> decreaseStock(
            @PathVariable String productId,
            @RequestBody DecreaseStockRequest request
    ) {
        inventoryUseCase.decreaseStock(new InventoryUseCase.DecreaseStockCommand(
                productId,
                request.amount(),
                request.reason(),
                request.operatedBy()
        ));
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{productId}/reserve")
    @Operation(summary = "預留庫存")
    public ResponseEntity<Void> reserveStock(
            @PathVariable String productId,
            @RequestBody ReserveStockRequest request
    ) {
        inventoryUseCase.reserveStock(new InventoryUseCase.ReserveStockCommand(
                productId,
                request.amount(),
                request.reason(),
                request.operatedBy()
        ));
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{productId}/reserve/cancel")
    @Operation(summary = "取消預留庫存")
    public ResponseEntity<Void> cancelReservation(
            @PathVariable String productId,
            @RequestBody CancelReservationRequest request
    ) {
        inventoryUseCase.cancelReservation(new InventoryUseCase.CancelReservationCommand(
                productId,
                request.amount(),
                request.reason(),
                request.operatedBy()
        ));
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{productId}/reserve/confirm")
    @Operation(summary = "確認預留庫存")
    public ResponseEntity<Void> confirmReservation(
            @PathVariable String productId,
            @RequestBody ConfirmReservationRequest request
    ) {
        inventoryUseCase.confirmReservation(new InventoryUseCase.ConfirmReservationCommand(
                productId,
                request.amount(),
                request.reason(),
                request.operatedBy()
        ));
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{productId}/alert-threshold")
    @Operation(summary = "更新庫存警示閾值")
    public ResponseEntity<Void> updateAlertThreshold(
            @PathVariable String productId,
            @RequestBody UpdateAlertThresholdRequest request
    ) {
        inventoryUseCase.updateAlertThreshold(new InventoryUseCase.UpdateAlertThresholdCommand(
                productId,
                request.threshold(),
                request.operatedBy()
        ));
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{productId}")
    @Operation(summary = "查詢單一產品庫存")
    public ResponseEntity<InventoryUseCase.InventoryResponse> getInventory(
            @PathVariable String productId
    ) {
        return ResponseEntity.ok(inventoryUseCase.getInventory(productId));
    }
    
    @GetMapping
    @Operation(summary = "查詢多個產品庫存")
    public ResponseEntity<List<InventoryUseCase.InventoryResponse>> getInventories(
            @RequestParam List<String> productIds
    ) {
        return ResponseEntity.ok(inventoryUseCase.getInventories(productIds));
    }
    
    @GetMapping("/{productId}/history")
    @Operation(summary = "查詢庫存歷史記錄")
    public ResponseEntity<List<InventoryUseCase.InventoryHistoryResponse>> getInventoryHistory(
            @PathVariable String productId,
            @RequestParam(required = false) List<String> operations
    ) {
        if (operations != null && !operations.isEmpty()) {
            return ResponseEntity.ok(inventoryUseCase.getInventoryHistoryByOperation(productId, operations));
        }
        return ResponseEntity.ok(inventoryUseCase.getInventoryHistory(productId));
    }
    
    record IncreaseStockRequest(Integer amount, String reason, String operatedBy) {}
    record DecreaseStockRequest(Integer amount, String reason, String operatedBy) {}
    record ReserveStockRequest(Integer amount, String reason, String operatedBy) {}
    record CancelReservationRequest(Integer amount, String reason, String operatedBy) {}
    record ConfirmReservationRequest(Integer amount, String reason, String operatedBy) {}
    record UpdateAlertThresholdRequest(Integer threshold, String operatedBy) {}
}
