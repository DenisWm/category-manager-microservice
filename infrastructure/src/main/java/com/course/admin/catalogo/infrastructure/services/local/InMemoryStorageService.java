package com.course.admin.catalogo.infrastructure.services.local;

import com.course.admin.catalogo.domain.resource.Resource;
import com.course.admin.catalogo.infrastructure.services.StorageService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryStorageService implements StorageService {

    private final Map<String, Resource> storage;

    public InMemoryStorageService() {
        this.storage = new ConcurrentHashMap<>();
    }

    public Map<String, Resource> storage() {
        return this.storage;
    }

    public void reset() {
        this.storage.clear();
    }

    @Override
    public void deleteAll(final Collection<String> names) {
        names.forEach(storage::remove);
    }

    @Override
    public Optional<Resource> get(final String name) {
        return Optional.ofNullable(this.storage.get(name));
    }

    @Override
    public List<String> list(final String prefix) {
        if(prefix == null) {
            return Collections.emptyList();
        }
        return this.storage.keySet()
                .stream()
                .filter(name -> name.startsWith(prefix))
                .collect(Collectors.toList());
    }

    @Override
    public void store(final String name, final Resource resource) {
        this.storage.put(name, resource);
    }
}
