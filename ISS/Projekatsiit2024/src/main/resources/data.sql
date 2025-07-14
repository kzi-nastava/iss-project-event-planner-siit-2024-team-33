INSERT INTO Role (id, name) VALUES (0, 'ADMIN_ROLE');
INSERT INTO Role (id, name) VALUES (1, 'AUSER_ROLE');
INSERT INTO Role (id, name) VALUES (2, 'ORGANIZER_ROLE');
INSERT INTO Role (id, name) VALUES (3, 'PROVIDER_ROLE');

INSERT INTO authentified_user 
(email, password, name, surname, city, picture, is_deleted, suspension_end_date, role_id, last_password_reset_date, DTYPE) 
VALUES 
('admin@example.com', '$2a$12$mtBxcimVjok61JeRMS9.VefhFdTj61GQrlYjziOpzdaz3F0eZVinS', 'Admin', 'User', 'Paris', 'milion.jpg', false, NULL, 0, NULL, 'Admin');
INSERT INTO authentified_user 
(email, password, name, surname, city, picture, is_deleted, suspension_end_date, role_id, last_password_reset_date, residency, phone_number, DTYPE) 
VALUES 
('organizer@example.com', '$2a$12$mtBxcimVjok61JeRMS9.VefhFdTj61GQrlYjziOpzdaz3F0eZVinS', 'John', 'Doe', 'Paris', 'jbl.jpg', false, NULL, 2, CURRENT_TIMESTAMP, 'Berlin, Germany', '123-456-789', 'Organizer');
INSERT INTO authentified_user 
(email, password, name, surname, city, picture, is_deleted, suspension_end_date, role_id, last_password_reset_date, residency, phone_number, provider_name, description, DTYPE) 
VALUES 
('provider@example.com', '$2a$12$mtBxcimVjok61JeRMS9.VefhFdTj61GQrlYjziOpzdaz3F0eZVinS', 'Provider', 'Company', 'Paris', 'provider.jpg', false, NULL, 3, CURRENT_TIMESTAMP, 'Paris, France', '987-654-321', 'TechProvider', 'Provider of tech services', 'Provider');
INSERT INTO authentified_user 
(email, password, name, surname, city, picture, is_deleted, suspension_end_date, role_id, last_password_reset_date, DTYPE) 
VALUES 
('admin2@example.com', '$2a$12$mtBxcimVjok61JeRMS9.VefhFdTj61GQrlYjziOpzdaz3F0eZVinS', 'Alice', 'Johnson', 'Paris', 'admin2.jpg', false, NULL, 0, CURRENT_TIMESTAMP, 'Admin');
INSERT INTO authentified_user 
(email, password, name, surname, city, picture, is_deleted, suspension_end_date, role_id, last_password_reset_date, residency, phone_number, DTYPE) 
VALUES 
('organizer2@example.com', '$2a$12$mtBxcimVjok61JeRMS9.VefhFdTj61GQrlYjziOpzdaz3F0eZVinS', 'Steve', 'Williams', 'Paris', 'organizer2.jpg', false, NULL, 2, CURRENT_TIMESTAMP, 'Madrid, Spain', '321-654-987', 'Organizer');




INSERT INTO OFFER_CATEGORY (IS_ACCEPTED, IS_ENABLED, DESCRIPTION, NAME, OFFER_TYPE)
VALUES
(TRUE, TRUE, 'Fun music', 'Music', 'SERVICE'),
(TRUE, TRUE, 'Best service', 'Catering', 'SERVICE'),
(TRUE, TRUE, 'Delicious food', 'Food', 'PRODUCT');

INSERT INTO OFFER (
    CATEGORY_ID, CREATION_DATE, DISCOUNT, IS_DELETED, IS_PENDING, 
    OFFERID, PRICE, PROVIDER_ID, TYPE, DTYPE, DESCRIPTION, NAME, 
    AVAILABILITY, PICTURES, CITY
)
VALUES
(1, '2024-12-01', 10.0, FALSE, FALSE, 1, 49.99, 1, 'PRODUCT', 'Product', 'Great chinese quality speaker', 'JBL Speaker', 'AVAILABLE', ARRAY['jbl.jpg'], 'Paris'),
(2, '2024-11-30', 15.5, FALSE, FALSE, 2, 29.99, 1, 'PRODUCT', 'Product', 'Beautiful silver plates that will make you mega happy', 'Silver plates', 'AVAILABLE', ARRAY['image2.png'], 'Paris'),
(3, '2024-10-15', 20.0, FALSE, FALSE, 3, 79.99, 1, 'PRODUCT', 'Product', 'Beautiful sour yummy candies', 'Candy', 'AVAILABLE', ARRAY['image3.jpg'], 'Paris');

INSERT INTO OFFER (
    CANCELLATION_IN_HOURS, CATEGORY_ID, DISCOUNT, IS_AUTOMATIC, IS_DELETED, IS_PENDING, 
    MAX_LENGTH_IN_MINS, MIN_LENGTH_IN_MINS, OFFERID, PRICE, PROVIDER_ID, 
    RESERVATION_IN_HOURS, CREATION_DATE, DTYPE, DESCRIPTION, NAME, 
    AVAILABILITY, PICTURES, TYPE, CITY
)
VALUES (
    24, 1, 10.0, TRUE, FALSE, FALSE, 
    120, 5, 4, 99.99, 1, 
    48, '2024-12-01', 'Service', 
    'Overrated Norwegian black metal. Burzum (/ˈbɜːrzəm/; Norwegian: [ˈbʉ̀rtsʉm]) is a Norwegian music project founded by Varg Vikernes in 1991. Although Burzum never played live performances, it became a staple of the early Norwegian black metal scene and is considered one of the most influential acts in black metal''s history.',
    'BURZUM', 'AVAILABLE', ARRAY['zumzum.jpg', 'burzum.jpg'], 'SERVICE', 'New York'
);
VALUES (
    24, 1, 10.0, TRUE, FALSE, FALSE, 
    120, 5, 4, 9.99, 1, 
    48, '2024-12-01', 'Service', 
    'Overrated Norwegian black metal. Burzum (/ˈbɜːrzəm/; Norwegian: [ˈbʉ̀rtsʉm]) is a Norwegian music project founded by Varg Vikernes in 1991. Although Burzum never played live performances, it became a staple of the early Norwegian black metal scene and is considered one of the most influential acts in black metal''s history.',
    'BURZUM', 'AVAILABLE', ARRAY['zumzum.jpg', 'burzum.jpg'], 'SERVICE', 'New York'
);


INSERT INTO EVENT_TYPE(IS_ACTIVE, DESCRIPTION, NAME) VALUES
(TRUE, 'A generic type of event.', 'All'),
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
    date_of_event, end_of_event, is_private, latitude, longitude, num_of_attendees, organizer_id, event_type_id, description, name, place
) VALUES 
(
    CAST('2025-12-15 07:00:00' AS TIMESTAMP),
    CAST('2025-12-15 19:00:00' AS TIMESTAMP),
    FALSE,
    40.712776,
    -74.005974,
    150,
    5,
    1,
    'Neam pojma nesto kenjam kao neka jaka deskripcija sdjfksdjfksdjkfjsdkj sdfklsdfklsdlfkslkfklslkdfklsdf',
    'Winter Wonderlandsdafasdfasdfsdafdsa',
    'Paris'
),
(
    CAST('2025-12-15 07:00:00' AS TIMESTAMP),
    CAST('2025-12-15 19:00:00' AS TIMESTAMP),
    FALSE,
    40.712776,
    -74.005974,
    150,
    5,
    1,
    'Neam pojma nesto kenjam kao neka jaka deskripcija',
    'Alo najjaca zureza ikadas dfdskfdsk',
    'New York City'
),
(
    CAST('2025-12-15 07:00:00' AS TIMESTAMP),
    CAST('2025-12-15 19:00:00' AS TIMESTAMP),
    FALSE,
    40.712776,
    -74.005974,
    150,
    5,
    1,
    'Neam pojma nesto kenjam kao neka jaka deskripcija',
    'Alo najjaca zureza ikadas dfdskfdsk',
    'New York City'
),
(
    CAST('2025-12-15 07:00:00' AS TIMESTAMP),
    CAST('2025-12-15 19:00:00' AS TIMESTAMP),
    FALSE,
    40.712776,
    -74.005974,
    150,
    5,
    1,
    'Neam pojma nesto kenjam kao neka jaka deskripcija',
    'Alo najjaca zureza ikadas dfdskfdsk',
    'New York City'
),
(
    CAST('2025-12-15 07:00:00' AS TIMESTAMP),
    CAST('2025-12-15 19:00:00' AS TIMESTAMP),
    FALSE,
    40.712776,
    -74.005974,
    150,
    5,
    1,
    'Neam pojma nesto kenjam kao neka jaka deskripcija',
    'Alo najjaca zureza ikadas dfdskfdsk',
    'New York City'
),
(
    CAST('2025-12-15 07:00:00' AS TIMESTAMP),
    CAST('2025-12-15 19:00:00' AS TIMESTAMP),
    FALSE,
    40.712776,
    -74.005974,
    150,
    5,
    1,
    'Neam pojma nesto kenjam kao neka jaka deskripcija',
    'Alo najjaca zureza ikadas dfdskfdsk',
    'New York City'
),
(
    CAST('2024-12-15 07:00:00' AS TIMESTAMP),
    CAST('2024-12-15 19:00:00' AS TIMESTAMP),
    FALSE,
    40.712776,
    -74.005974,
    150,
    5,
    1,
    'Neam pojma nesto kenjam kao neka jaka deskripcija',
    'Alo najjaca zureza ikadas dfdskfdsk',
    'New York City'
),
(
    CAST('2024-12-15 07:00:00' AS TIMESTAMP),
    CAST('2024-12-15 19:00:00' AS TIMESTAMP),
    FALSE,
    40.712776,
    -74.005974,
    150,
    5,
    1,
    'Neam pojma nesto kenjam kao neka jaka deskripcija',
    'Alo najjaca zureza ikadas dfdskfdsk',
    'New York City'
),
(
    CAST('2024-12-15 07:00:00' AS TIMESTAMP),
    CAST('2024-12-15 19:00:00' AS TIMESTAMP),
    FALSE,
    40.712776,
    -74.005974,
    150,
    5,
    1,
    'Neam pojma nesto kenjam kao neka jaka deskripcija',
    'Alo najjaca zureza ikadas dfdskfdsk',
    'New York City'
),
(
    CAST('2024-12-15 07:00:00' AS TIMESTAMP),
    CAST('2024-12-15 19:00:00' AS TIMESTAMP),
    FALSE,
    40.712776,
    -74.005974,
    150,
    5,
    1,
    'Annual Winter Gala',
    'Winter Wonderland',
    'New York City'
),
(
    CAST('2024-12-15 07:00:00' AS TIMESTAMP),
    CAST('2024-12-15 19:00:00' AS TIMESTAMP),
    FALSE,
    40.712776,
    -74.005974,
    150,
    5,
    1,
    'Annual Winter Gala',
    'Winter Wonderland',
    'New York City'
),
(
    CAST('2024-12-15 07:00:00' AS TIMESTAMP),
    CAST('2024-12-15 19:00:00' AS TIMESTAMP),
    FALSE,
    40.712776,
    -74.005974,
    150,
    5,
    1,
    'Annual Winter Gala',
    'Winter Wonderland',
    'New York City'
);

INSERT INTO authentified_user_blocked_users (AUTHENTIFIED_USER_ID, BLOCKED_USERS_ID)
VALUES (2,1);

INSERT INTO EVENT_TYPE_RECOMMENDED_CATEGORIES(EVENT_TYPE_ID, RECOMMENDED_CATEGORIES_ID) VALUES (2,1), (2,2), (2,3);

INSERT INTO notification (content, time_of_sending, is_read,is_selected, receiver_id)
VALUES 
('You have been invited to the Annual Winter Gala event happening on 2024-12-15!', 
 '2024-12-05', 
 FALSE,
 TRUE,
 2),
 ('You have been invited to the Annual Winter Gala event happening on 2024-12-15!', 
 '2024-12-05', 
 FALSE,
 TRUE,
 2),
 ('You have been invited to the Annual Winter Gala event happening on 2024-12-15!', 
 '2024-12-05', 
 FALSE,
 TRUE,
 2),
 ('GASon 2024-12-15!', 
 '2024-12-05', 
 TRUE,
 TRUE,
 2),
 ('You have been invited to the Annbeen invited to the Annbeen invited to the Annbeen invited to the Annbeen invited to the Annbeen invited to the Annbeen invited to the Annual Winter Gala event happening on 2024-12-15!', 
 '2024-12-05', 
 TRUE,
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