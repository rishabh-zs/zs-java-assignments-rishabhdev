package com.zs.assignment4.repositories;

import com.zs.assignment4.models.Category;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The type In memory category repository.
 */
public class InMemoryCategoryRepository implements CategoryRepository {
    private final Map<String, Category> data = new LinkedHashMap<>();

    @Override
    public void save(Category category) {
        data.put(category.getName().toLowerCase(), category);
    }

    @Override
    public Category findByName(String name) {
        return data.get(name.toLowerCase());
    }

    @Override
    public Category deleteByName(String name) {
        return data.remove(name.toLowerCase());
    }

    @Override
    public List<Category> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public boolean existsByName(String name) {
        return data.containsKey(name.toLowerCase());
    }
}
