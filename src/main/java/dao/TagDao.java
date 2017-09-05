package dao;

import generated.tables.records.TagsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.impl.DSL;
import java.util.List;

import static generated.Tables.RECEIPTS;
import static generated.Tables.TAGS;

public class TagDao {
    DSLContext dsl;

    public TagDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
    }

    public void toggle(int receiptId, String tagName) {
        TagsRecord tagExists = dsl.selectFrom(TAGS)
                                    .where(TAGS.RECEIPT_ID.equal(receiptId))
                                    .and(TAGS.TAG.equal(tagName))
                                    .fetchOne();

        // Remove record if specified tagName exists for specified receiptId, else add in the tag
        if(tagExists == null)
        {
            dsl.insertInto(TAGS, TAGS.RECEIPT_ID, TAGS.TAG)
                    .values(receiptId, tagName)
                    .execute();
        }
        else{
            dsl.executeDelete(tagExists);
        }
    }

    public List<Record> getAllReceiptsWithTag(String tagName) {
        return dsl.select()
                .from(RECEIPTS)
                .join(TAGS)
                .on(TAGS.RECEIPT_ID.equal(RECEIPTS.ID))
                .where(TAGS.TAG.equal(tagName))
                .fetch();
    }
}

