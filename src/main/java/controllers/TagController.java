package controllers;

import api.ReceiptResponse;
import dao.TagDao;
import org.jooq.Record;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.ArrayList;
import static generated.Tables.RECEIPTS;

@Path("/tags")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TagController {
    final TagDao tags;

    public TagController(TagDao tags) {
        this.tags = tags;
    }

    @PUT
    @Path("/{tag}")
    public void toggleTag(@PathParam("tag") String tagName, int receiptId) {
        tags.toggle(receiptId, tagName);
    }

    @GET
    @Path("/{tag}")
    public List<ReceiptResponse> getReceiptsWithTag(@PathParam("tag") String tagName) {
        List<Record> receiptRecords = tags.getAllReceiptsWithTag(tagName);
        List<ReceiptResponse> receiptResponses = new ArrayList<ReceiptResponse>();
        for (Record record: receiptRecords) {
            ReceiptResponse receiptResponse = new ReceiptResponse(
                    record.getValue(RECEIPTS.ID),
                    record.getValue(RECEIPTS.MERCHANT),
                    record.getValue(RECEIPTS.AMOUNT),
                    record.getValue(RECEIPTS.UPLOADED));
            receiptResponses.add(receiptResponse);
        }
        return receiptResponses;
    }
}
