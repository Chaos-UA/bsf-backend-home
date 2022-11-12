-- CREATE TABLE "account" --------------------------------------
CREATE TABLE "account"
(
    "id"             UUid                        NOT NULL,
    "owner_id"       UUid                        NOT NULL,
    "balance_amount" BigInt                      NOT NULL,
    "created_at"     Timestamp Without Time Zone NOT NULL,
    "modified_at"    Timestamp Without Time Zone NOT NULL,
    PRIMARY KEY ("id")
);
-- -------------------------------------------------------------

-- CREATE TABLE "transaction" ----------------------------------
CREATE TABLE "transaction"
(
    "id"                UUid                        NOT NULL,
    "account_id"        UUid                        NOT NULL,
    "source_account_id" UUid,
    "operation_type"    Character Varying           NOT NULL,
    "description"       Character Varying,
    "amount"            BigInt                      NOT NULL,
    "created_at"        Timestamp Without Time Zone NOT NULL,
    PRIMARY KEY ("id")
);

CREATE INDEX "transaction_created_at_idx" ON "transaction" USING btree ("created_at" Desc);

ALTER TABLE "transaction"
    ADD CONSTRAINT "transaction_account_id_account_id_lnk" FOREIGN KEY ("account_id")
        REFERENCES "account" ("id")
        ON DELETE Cascade
        ON UPDATE Cascade;

ALTER TABLE "transaction"
    ADD CONSTRAINT "transaction_source_account_id_account_id_lnk" FOREIGN KEY ("source_account_id")
        REFERENCES "account" ("id")
        ON DELETE Cascade
        ON UPDATE Cascade;
-- -------------------------------------------------------------