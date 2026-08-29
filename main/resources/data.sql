INSERT INTO public."plan"
    (active, max_previews, max_projects, max_tokens_per_day, unlimitedai, id, "name", stripe_price_id)
SELECT
    true, 5, 5, 10000, NULL, 1, 'Pro Plan', 'price_1U3ZiYKBwdhOYgknaUzrZh0i'
WHERE NOT EXISTS (
    SELECT 1 FROM public."plan"
);

INSERT INTO public."plan"
    (active, max_previews, max_projects, max_tokens_per_day, unlimitedai, id, "name", stripe_price_id)
SELECT
    true, 15, 15, 40000, NULL, 2, 'Max Plan', 'price_1U3ZoLKBwdhOYgkn42UvAcWA'
WHERE NOT EXISTS (
    SELECT 1 FROM public."plan" WHERE name = 'Max Plan'
);