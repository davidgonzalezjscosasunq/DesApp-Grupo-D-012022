package ar.edu.unq.desapp.grupod.backenddesappapi.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses (packages = "ar.edu.unq.desapp.grupod.backenddesappapi")
public class ArchitectureTest {

    @ArchTest
    public static final ArchRule servicesShouldOnlyBeAccessedByControllersAndAspects = classes()
            .that().resideInAPackage ("..service..")
            .should().onlyBeAccessed().byAnyPackage("..controller..", "..service..", "..aspects..");

    @ArchTest
    public static final ArchRule controllersShouldNotBeAccessedByAnyone = classes()
            .that().resideInAPackage("..controller..")
            .should().onlyBeAccessed().byAnyPackage("..controller..");

    @ArchTest
    public static final ArchRule persistenceClassesShouldOnlyBeAccessedByServices = classes()
            .that().resideInAPackage("..persistence..")
            .should().onlyBeAccessed().byAnyPackage("..service..", "..persistence..");

    @ArchTest
    public static final ArchRule classesWithNameServiceAtTheEndShouldBeOnlyInServicePackage = classes()
            .that().haveSimpleNameEndingWith("Service")
            .should().resideInAPackage("..service..");

    @ArchTest
    public static final ArchRule classesWithNameControllerAtTheEndShouldBeOnlyInControllerPackage = classes()
            .that().haveSimpleNameEndingWith("Controller")
            .should().resideInAPackage("..controller..");

    @ArchTest
    public static final ArchRule classesWithNameRepositoryAtTheEndShouldBeOnlyInPersistencePackage = classes()
            .that().haveSimpleNameEndingWith("Repository")
            .should().resideInAPackage("..persistence..");

    @ArchTest
    public static final ArchRule classesWithNameDTOAtTheEndShouldBeOnlyInDtosPackage = classes()
            .that().haveSimpleNameEndingWith("DTO")
            .should().resideInAPackage("..controller.dtos..");

    @ArchTest
    public static final ArchRule classesWithNameValidatorAtTheEndShouldBeOnlyInValidatorsPackage = classes()
            .that().haveSimpleNameEndingWith("Validator")
            .should().resideInAPackage("..model.validators..");

    @ArchTest
    public static final ArchRule repositoryClassesShouldBeInterfaces = classes()
            .that().haveSimpleNameEndingWith("Repository")
            .should().beInterfaces();

    @ArchTest
    public static final ArchRule onlyClassesInServiceShouldHaveTransactionalAnnotation = classes()
            .that().areAnnotatedWith(Transactional.class)
            .should().resideInAPackage("..service..");

    @ArchTest
    public static final ArchRule onlyClassesInControllerShouldHaveRestControllerAnnotation = classes()
            .that().areAnnotatedWith(RestController.class)
            .should().resideInAPackage("..controller..");

    @ArchTest
    public static final ArchRule onlyClassesInPersistenceShouldHaveRepositoryAnnotation = classes()
            .that().areAnnotatedWith(Repository.class)
            .should().resideInAPackage("..persistence..");

    @ArchTest
    public static final ArchRule onlyClassesInModelShouldHaveEntityAnnotation = classes()
            .that().areAnnotatedWith(Entity.class)
            .should().resideInAPackage("..model..");

    @ArchTest
    public static final ArchRule onlyClassesInConfigurationShouldHaveConfigurationAnnotation = classes()
            .that().areAnnotatedWith(Configuration.class)
            .should().resideInAPackage("..configuration..");

}
