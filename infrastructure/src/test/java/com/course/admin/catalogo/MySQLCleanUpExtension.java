package com.course.admin.catalogo;

import com.course.admin.catalogo.infrastructure.castmember.persistence.CastMemberRepository;
import com.course.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import com.course.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.List;

class MySQLCleanUpExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(final ExtensionContext context) {
        final var appContext = SpringExtension.getApplicationContext(context);

        cleanUp(List.of(
                appContext.getBean(GenreRepository.class),
                appContext.getBean(CategoryRepository.class),
                appContext.getBean(CastMemberRepository.class)
        ));
    }
    private void cleanUp(final Collection<CrudRepository> repositories) {
        repositories.forEach(CrudRepository::deleteAll);
    }
}