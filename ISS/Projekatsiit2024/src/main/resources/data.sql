INSERT INTO AUTHENTIFIED_USER(IS_DELETED, SUSPENSION_END_DATE, DTYPE, DESCRIPTION, EMAIL, NAME, PASSWORD, PHONE_NUMBER, PICTURES, PROVIDER_NAME, RESIDENCY, SURNAME, CITY)
VALUES
(0, '2024-12-31', 'Provider', 'Provider 1', 'milion@example.com', 'Milion', '$2a$10$eZ1GE0kxl7jEmOR93JKNHuBPb28AJjZhJfOKWfgGuC/vS7cO39NMi', '1234567890', ARRAY['milion.jpg'], 'Provider1', 'USA', 'Doe', 'Houston'),
(0, '2024-11-30', 'Provider', 'Provider 2', 'mirkodjukic718@gmail.com', 'Lobanja', 'MikroMirza123!MikroMirza123!', '9876543210', 'image2.png', 'Provider2', 'Canada', 'Smith', 'New York City'),
(0, '2024-12-31', 'Provider', 'Provider 3', 'mikro@example.com', 'Mikro', 'hashedpassword123', '1234567890', 'image1.jpg', 'Provider1', 'USA', 'Doe', 'Jew York City'),
(0, '2024-11-30', 'Provider', 'Provider 4', 'magistrala@domain.com', 'Magistrala', 'encryptedpassword456', '9876543210', 'image2.png', 'Provider2', 'Canada', 'Smith', '3ew York City');

INSERT INTO AUTHENTIFIED_USER (
    ID, IS_DELETED, SUSPENSION_END_DATE, DTYPE, DESCRIPTION, EMAIL, NAME, PASSWORD, PHONE_NUMBER, PICTURES, PROVIDER_NAME, RESIDENCY, SURNAME, CITY
) VALUES (
    5, 0, '2024-12-02', 'Organizer', 'Organizer 1', 'BOOMBOOMPALACINKE@domain.com', 'BOOMBOOM', 'encryptedpassword456', '9876543210', 'image2.png', 'BUMBUM', 'Swaziland', 'xSmithy', 'Jew York City'
);

INSERT INTO OFFER_CATEGORY (IS_ACCEPTED, IS_ENABLED, DESCRIPTION, NAME)
VALUES
(TRUE, TRUE, 'Fun music', 'Music'),
(TRUE, TRUE, 'Best service', 'Catering'),
(TRUE, TRUE, 'Delicious food', 'Food');

INSERT INTO OFFER (CATEGORY_ID, CREATION_DATE, DISCOUNT, IS_DELETED, IS_PENDING, OFFERID, PRICE, PROVIDER_ID, TYPE, DTYPE, DESCRIPTION, NAME, AVAILABILITY, PICTURES,CITY)
VALUES
(1, '2024-12-01', 10.0, FALSE, FALSE, 1, 49.99, 1, 'PRODUCT', 'Product', 'Great chinese quality speaker', 'JBL Speaker', 'AVAILABLE', ARRAY['jbl.jpg'],'Mew York City'),
(2, '2024-11-30', 15.5, FALSE, FALSE, 2, 29.99, 2, 'PRODUCT', 'Product', 'Beautiful silver plates that will make you mega happy', 'Silver plates', 'AVAILABLE', 'image2.png','Jew York City'),
(3, '2024-10-15', 20.0, FALSE, FALSE, 3, 79.99, 3, 'PRODUCT', 'Product', 'Beautiful sour yummy candeis', 'Candy', 'AVAILABLE', 'image3.jpg','New  City');

INSERT INTO OFFER (CANCELLATION_IN_HOURS, CATEGORY_ID, DISCOUNT, IS_AUTOMATIC, IS_DELETED, IS_PENDING, MAX_LENGTH_IN_MINS, MIN_LENGTH_IN_MINS, OFFERID, PRICE, PROVIDER_ID, RESERVATION_IN_HOURS, CREATION_DATE, DTYPE, DESCRIPTION, NAME, AVAILABILITY, PICTURES, TYPE, CITY)
VALUES
(24, 1, 10.0, TRUE, FALSE, FALSE, 120, 5, 4, 99.99, 2, 48, '2024-12-01', 'Service', 'overrated norwegian black metal lol Burzum (/ˈbɜːrzəm/; Norwegian: [ˈbʉ̀rtsʉm]) is a Norwegian music project founded by Varg Vikernes in 1991. Although Burzum never played live performances, it became a staple of the early Norwegian black metal scene and is considered one of the most influential acts in black metal`s history.', 'BURZUM', 'AVAILABLE', ARRAY['zumzum.jpg', 'burzum.jpg'], 'SERVICE', 'New York ');


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
INSERT INTO EVENT (
    DATE_OF_EVENT, END_OF_EVENT, IS_PRIVATE, ITS_JOEVER, LATITUDE, LONGITUDE, NUM_OF_ATTENDEES, ORGANIZER_ID, PRICE, DESCRIPTION, NAME, PICTURE, PLACE
) VALUES 
(
    CAST('2024-12-15 07:00:00' AS TIMESTAMP), 
    CAST('2024-12-15 19:00:00' AS TIMESTAMP), 
    0, 
    0, 
    40.712776, 
    -74.005974, 
    150, 
    5, 
    50, 
    'Annual Winter Gala', 
    'Winter Wonderland', 
    'event1.jpg', 
    'New York City'
),
(
    CAST('2024-12-15 07:00:00' AS TIMESTAMP), 
    CAST('2024-12-15 19:00:00' AS TIMESTAMP), 
    0, 
    0, 
    40.712776, 
    -74.005974, 
    150, 
    5, 
    50, 
    'Annual Winter Gala', 
    'Winter Wonderland', 
    'event1.jpg', 
    'Jew York City'
),
(
    CAST('2024-12-15 07:00:00' AS TIMESTAMP), 
    CAST('2024-12-15 19:00:00' AS TIMESTAMP), 
    0, 
    0, 
    40.712776, 
    -74.005974, 
    150, 
    5, 
    50, 
    'Annual Winter Gala', 
    'Winter Wonderland', 
    'event1.jpg', 
    'New York City'
),
(
    CAST('2024-12-15 07:00:00' AS TIMESTAMP), 
    CAST('2024-12-15 19:00:00' AS TIMESTAMP), 
    0, 
    0, 
    40.712776, 
    -74.005974, 
    150, 
    5, 
    50, 
    'Annual Winter Gala', 
    'Winter Wonderland', 
    'event1.jpg', 
    'New York City'
),
(
    CAST('2024-12-15 07:00:00' AS TIMESTAMP), 
    CAST('2024-12-15 19:00:00' AS TIMESTAMP), 
    0, 
    0, 
    40.712776, 
    -74.005974, 
    150, 
    5, 
    50, 
    'Annual Winter Gala', 
    'Winter Wonderland', 
    'event1.jpg', 
    'New York City'
);

INSERT INTO authentified_user_blocked_users (AUTHENTIFIED_USER_ID, BLOCKED_USERS_ID)
VALUES (2,1);

INSERT INTO EVENT_TYPE_RECOMMENDED_CATEGORIES(EVENT_TYPE_ID, RECOMMENDED_CATEGORIES_ID) VALUES (1,1), (1,2), (1,3);
INSERT INTO EVENT_EVENT_TYPES(EVENT_ID, EVENT_TYPES_ID) VALUES (1,1), (1,2);

INSERT INTO notification (content, time_of_sending, is_read,is_selected, receiver_id)
VALUES 
('You have been invited to the Annual Winter Gala event happening on 2024-12-15!', 
 '2024-12-05', 
 FALSE,
 TRUE,
 2);
 INSERT INTO rating (rating_value, comment, accepted, is_deleted, author_id, offer_id) VALUES
(5, 'Excellent deal, highly recommend!', FALSE, FALSE, 1, 1),
(4, 'Good value for the price.', FALSE, FALSE, 2, 1),
(3, 'Average offer, nothing special.', FALSE, FALSE, 3, 2),
(5, 'Absolutely fantastic!', FALSE, FALSE, 1, 3),
(2, 'Not worth the money.', FALSE, FALSE, 2, 2),
(4, 'Pretty good but could be better.', TRUE, FALSE, 3, 3),
(1, 'Terrible experience.', FALSE, FALSE, 1, 1),
(5, 'Best deal I have ever found!', FALSE, FALSE, 2, 3);

INSERT INTO report (content, date_of_sending, author_id, receiver_id) VALUES
('This is a report about the annual meeting.', '2024-12-05', 1, 2),
('Report on the recent marketing strategy review.', '2024-12-06', 2, 1),
('Feedback on the event organization.', '2024-12-07', 1, 3),
('Summary of the last quarter performance.', '2024-12-08', 2, 3),
('Report concerning the new project updates.', '2024-12-09', 3, 1);

