-- task is only related to account money transfer, so lets pre-create some accounts for testing API

INSERT INTO "account"( "id", "owner_id", "balance_amount", "created_at", "modified_at" )
VALUES('50c19f57-6a65-4e85-ac40-2ebccf295b80', 'f287f83d-fcf5-40e6-9179-4f55a2025960', 100000, '2022-11-12 00:00:00.0', '2022-11-12 00:00:00.0');

INSERT INTO "account"( "id", "owner_id", "balance_amount", "created_at", "modified_at" )
VALUES('50c19f57-6a65-4e85-ac40-2ebccf295b81', 'f287f83d-fcf5-40e6-9179-4f55a2025960', 100000, '2022-11-12 00:00:00.0', '2022-11-12 00:00:00.0');

INSERT INTO "account"( "id", "owner_id", "balance_amount", "created_at", "modified_at" )
VALUES('50c19f57-6a65-4e85-ac40-2ebccf295b82', 'f287f83d-fcf5-40e6-9179-4f55a2025961', 100000, '2022-11-12 00:00:00.0', '2022-11-12 00:00:00.0');

INSERT INTO "account"( "id", "owner_id", "balance_amount", "created_at", "modified_at" )
VALUES('50c19f57-6a65-4e85-ac40-2ebccf295b83', 'f287f83d-fcf5-40e6-9179-4f55a2025961', 100000, '2022-11-12 00:00:00.0', '2022-11-12 00:00:00.0');
