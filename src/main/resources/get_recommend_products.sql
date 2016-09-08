SELECT p.pkey FROM (
SELECT p.pkey, ROW_NUMBER() OVER (PARTITION BY m.email ORDER BY score DESC) AS RowNum
FROM (
 SELECT *
 FROM [dd_email_recommender.RECOMMENDATION_RAW] T1) r
INNER JOIN [dd_email_recommender.MEMBER_UNIQUE_DYNAMIC] m ON m.customer_id = r.customer_id
INNER JOIN [dd_email_recommender.PRODUCT_NODUPS] p ON r.products_id = p.products_id
INNER JOIN [dd_email_recommender.PRODUCT_ACTIVE] a ON r.products_id = a.products_id
WHERE m.email IN (
  SELECT email
  FROM [dd_email_recommender.CUSTOMER_INHOUSE_SUBSCRIBED]))
WHERE RowNum <= #NOP
GROUP BY p.pkey
LIMIT #CACHING