INSERT INTO AUTHENTIFIED_USER(IS_DELETED, SUSPENSION_END_DATE, DTYPE, DESCRIPTION, EMAIL, NAME, PASSWORD, PHONE_NUMBER, PICTURE, PROVIDER_NAME, RESIDENCY, SURNAME)
VALUES
(0, '2024-12-31', 'Provider', 'Provider 1', 'milion@example.com', 'Milion', 'hashedpassword123', '1234567890', 'image1.jpg', 'Provider1', 'USA', 'Doe'),
(0, '2024-11-30', 'Provider', 'Provider 2', 'lobanja@domain.com', 'Lobanja', 'encryptedpassword456', '9876543210', 'image2.png', 'Provider2', 'Canada', 'Smith'),
(0, '2024-12-31', 'Provider', 'Provider 3', 'mikro@example.com', 'Mikro', 'hashedpassword123', '1234567890', 'image1.jpg', 'Provider1', 'USA', 'Doe'),
(0, '2024-11-30', 'Provider', 'Provider 4', 'magistrala@domain.com', 'Magistrala', 'encryptedpassword456', '9876543210', 'image2.png', 'Provider2', 'Canada', 'Smith');

INSERT INTO OFFER_CATEGORY (IS_ACCEPTED, IS_ENABLED, DESCRIPTION, NAME)
VALUES
(TRUE, TRUE, 'Fun music', 'Music'),
(TRUE, TRUE, 'Best service', 'Catering'),
(TRUE, TRUE, 'Delicious food', 'Food');

INSERT INTO OFFER (CATEGORY_ID, CREATION_DATE, DISCOUNT, IS_DELETED, IS_PENDING, OFFERID, PRICE, PROVIDER_ID, TYPE, DTYPE, DESCRIPTION, NAME, AVAILABILITY, PICTURES)
VALUES
(1, '2024-12-01', 10.0, FALSE, FALSE, 1, 49.99, 1, 'PRODUCT', 'Product', 'Great chinese quality speaker', 'JBL Speaker', 'AVAILABLE', 'image1.jpg'),
(2, '2024-11-30', 15.5, FALSE, FALSE, 2, 29.99, 2, 'PRODUCT', 'Product', 'Beautiful silver plates that will make you mega happy', 'Silver plates', 'AVAILABLE', 'image2.png'),
(3, '2024-10-15', 20.0, FALSE, FALSE, 3, 79.99, 3, 'PRODUCT', 'Product', 'Beautiful sour yummy candeis', 'Candy', 'AVAILABLE', 'image3.jpg');

INSERT INTO OFFER (CANCELLATION_IN_HOURS, CATEGORY_ID, DISCOUNT, IS_AUTOMATIC, IS_DELETED, IS_PENDING, MAX_LENGTH_IN_MINS, MIN_LENGTH_IN_MINS, OFFERID, PRICE, PROVIDER_ID, RESERVATION_IN_HOURS, CREATION_DATE, DTYPE, DESCRIPTION, NAME, AVAILABILITY, PICTURES, TYPE)
VALUES
(24, 1, 10.0, TRUE, FALSE, FALSE, 120, 5, 4, 99.99, 2, 48, '2024-12-01', 'Service', 'overrated norwegian black metal lol Burzum (/ˈbɜːrzəm/; Norwegian: [ˈbʉ̀rtsʉm]) is a Norwegian music project founded by Varg Vikernes in 1991. Although Burzum never played live performances, it became a staple of the early Norwegian black metal scene and is considered one of the most influential acts in black metal`s history.', 'BURZUM', 'AVAILABLE', ARRAY['zumzum.jpg', 'burzum.jpg'], 'SERVICE');

INSERT INTO EVENT_TYPE(IS_ACTIVE, DESCRIPTION, NAME) VALUES
(TRUE, 'A formal event typically attended by professionals.', 'Conference'),
(TRUE, 'A social gathering for celebration or networking.', 'Party'),
(TRUE, 'A celebration of marriage.', 'Wedding'),
(TRUE, 'An informal meeting or discussion.', 'Workshop'),
(TRUE, 'A performance or display of artistic works.', 'Exhibition'),
(FALSE, 'An organized public or private event with music and dancing.', 'Concert'),
(TRUE, 'A meeting to discuss corporate goals or projects.', 'Business Meeting'),
(FALSE, 'A sports-related event such as a game or tournament.', 'Sports Event'),
(TRUE, 'A community gathering or public fair.', 'Festival'),
(TRUE, 'A casual outdoor gathering with food and entertainment.', 'Picnic');