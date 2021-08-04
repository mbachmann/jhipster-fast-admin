package ch.united.fastadmin;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("ch.united.fastadmin");

        noClasses()
            .that()
            .resideInAnyPackage("ch.united.fastadmin.service..")
            .or()
            .resideInAnyPackage("ch.united.fastadmin.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..ch.united.fastadmin.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
