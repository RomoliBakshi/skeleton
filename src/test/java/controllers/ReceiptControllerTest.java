package controllers;

import api.CreateReceiptRequest;
import api.ReceiptResponse;
import dao.ReceiptDao;
import generated.tables.records.ReceiptsRecord;
import org.junit.Before;
import org.junit.Test;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ReceiptControllerTest {

    private ReceiptDao mockReceiptsDao;
    private ReceiptController receiptController;
    private CreateReceiptRequest receiptRequest;
    private int expectedReceiptId;
    private List<ReceiptResponse> expectedReceiptResponses;
    private List<ReceiptsRecord> intermediateReceiptsRecord;

    @Before
    public void setup() {
        mockReceiptsDao = mock(ReceiptDao.class);
        receiptController = new ReceiptController(mockReceiptsDao);

        // Set up CreateReceiptRequest
        receiptRequest = new CreateReceiptRequest();
        receiptRequest.merchant = "Merchant1";
        receiptRequest.amount = new BigDecimal(5.00);

        // Used in receipt responses/records
        expectedReceiptId = 1;
        Time creationTime = new Time(12, 12, 12);
        int receiptType = 2;

        //Initialize expected receipt responses
        expectedReceiptResponses = new ArrayList<ReceiptResponse>();
        expectedReceiptResponses.add(new ReceiptResponse(expectedReceiptId, receiptRequest.merchant,
                receiptRequest.amount, creationTime));

        // Initialize receipts record used in stub for DAO getAllReceipts() func
        intermediateReceiptsRecord = new ArrayList<ReceiptsRecord>();
        intermediateReceiptsRecord.add(new ReceiptsRecord(expectedReceiptId, creationTime, receiptRequest.merchant,
                receiptRequest.amount, receiptType));

        // Stub statements
        when(mockReceiptsDao.insert(anyString(), any(BigDecimal.class))).thenReturn(expectedReceiptId);
        when(mockReceiptsDao.getAllReceipts()).thenReturn(intermediateReceiptsRecord);
    }

    @Test
    public void testCreateReceipt() {
        // Check that DAO insert() func is called
        int returnedReceiptId = receiptController.createReceipt(receiptRequest);
        assertEquals(expectedReceiptId, returnedReceiptId);
    }

    @Test
    public void testGetReceipts() {
        // Check that DAO getAllReceipts() func is called
        List<ReceiptResponse> returnedReceiptResponses = receiptController.getReceipts();
        assertEquals(expectedReceiptResponses.size(), returnedReceiptResponses.size());
    }
}
