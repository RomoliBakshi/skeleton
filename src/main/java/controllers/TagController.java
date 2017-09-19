package controllers;

import api.ReceiptResponse;
import api.TagResponse;
import dao.TagDao;
import org.jooq.Record;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.ArrayList;
import static generated.Tables.RECEIPTS;
import static generated.Tables.TAGS;

@Path("/tags")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TagController {
    final TagDao tags;

    public TagController(TagDao tags) {
        this.tags = tags;
    }
    @GET
    @Path("")
    public List<TagResponse> getReceiptsWithAllTags(){
        List<Record> tagRecords = tags.getAllReceipts();
        List<TagResponse> tagResponses = new ArrayList<TagResponse>();
        for (Record record: tagRecords) {
            TagResponse tagResponse = new TagResponse(
                    record.getValue(TAGS.RECEIPT_ID),
                    record.getValue(TAGS.TAG));
            tagResponses.add(tagResponse);
        }
        return tagResponses;
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
